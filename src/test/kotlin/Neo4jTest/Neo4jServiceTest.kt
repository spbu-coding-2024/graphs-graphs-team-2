package Neo4jTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import model.Graph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import viewModel.graph.GraphViewModel
import androidx.compose.ui.unit.dp
import io.ioNeo4j.Neo4jService
import model.abstractGraph.AbstractVertex
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import java.util.stream.Stream
import kotlin.random.Random


@SpringBootApplication
@ComponentScan(basePackages = ["io.ioNeo4j", "Neo4jTest"])
class TestApp

@SpringBootTest(classes = [TestApp::class])
class Neo4jServiceTest {

    companion object {
        private lateinit var neo4j: Neo4j

        @BeforeAll
        @JvmStatic
        fun setupNeo4j() {
            neo4j = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .build()
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            neo4j.close()
        }


        @DynamicPropertySource
        @JvmStatic
        fun neo4jProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.neo4j.uri") { neo4j.boltURI().toString() }
            registry.add("spring.neo4j.authentication.username") { "neo4j" }
            registry.add("spring.neo4j.authentication.password") { null }
        }

        @JvmStatic
        fun graphGenerator(): Stream<Arguments> {
            return Stream.generate {
                val calculateEdgeId = { firstVertexId: Int, secondVertexId: Int ->
                    firstVertexId * 1000 + secondVertexId
                }
                val countOfNodes = Random.nextInt(10, 100)
                val firstId = 0
                val lastId = countOfNodes
                val graph = Graph(true, true)
                val nodes = mutableSetOf<Int>()

                for (i in 0..countOfNodes) {
                    val vertex = Random.nextInt(firstId, lastId)
                    graph.addVertex(vertex.toLong(), "$vertex")
                    nodes.add(vertex)
                }

                nodes.forEach { firstId ->
                    val countOfEdges = Random.nextInt(0, 10)
                    for (i in 0..countOfEdges) {
                        val secondId = nodes.random()
                        if (firstId != secondId) {
                            graph.addEdge(
                                firstId.toLong(),
                                secondId.toLong(),
                                "${calculateEdgeId(firstId, secondId)}",
                                calculateEdgeId(firstId, secondId).toLong(),
                                Random.nextFloat()
                            )
                        }
                    }
                }
                Arguments.of(graph)
            }.limit(1)
        }
    }

    @Autowired
    private lateinit var neo4jService: Neo4jService

    @ParameterizedTest(name = "test for Neo4j")
    @MethodSource("graphGenerator")
    fun `test write and read random graph`(
        correctGraph: Graph
    ) {

        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        val mapOfPlacement = mutableMapOf<String, Pair<Dp, Dp>>()
        correctGraph.vertices.forEach { vertex ->
            val cord = Pair(Random.nextInt(1, 100).dp, Random.nextInt(1, 100).dp)
            placement.put(vertex, cord)
            mapOfPlacement.put(vertex.label,cord)
        }


        val viewModel = GraphViewModel(
            correctGraph,
            placement,
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false)
        )

        neo4jService.writeData(viewModel)

        val (readGraph, readPlacement) = neo4jService.readData()

        assertEquals(readGraph.isDirected, correctGraph.isDirected)
        assertEquals(readGraph.isWeighted, correctGraph.isWeighted)
        assertEquals(readGraph.vertices.size, correctGraph.vertices.size)
        assertEquals(readGraph.edges.size, correctGraph.edges.size)


        readPlacement.forEach { (readV, readCoords) ->
            assertEquals(mapOfPlacement[readV.label], readCoords)
        }


        val sortedReadGraphVertex = readGraph.vertices.sortedBy { it.label.toInt() }
        val sortedGraphVertex = correctGraph.vertices.sortedBy { it.label.toInt() }

        for (i in 0..<sortedGraphVertex.size) {
            assertEquals(sortedGraphVertex[i].label, sortedReadGraphVertex[i].label)
        }


        val sortedReadGraphEdge = readGraph.edges.sortedBy { it.label.toInt() }
        val sortedGraphEdge = correctGraph.edges.sortedBy { it.label.toInt() }

        for (i in 0..<sortedGraphEdge.size) {
            assertEquals(sortedGraphEdge[i].label, sortedReadGraphEdge[i].label)
            assertEquals(sortedGraphEdge[i].vertices.first.label, sortedReadGraphEdge[i].vertices.first.label)
            assertEquals(sortedGraphEdge[i].vertices.second.label, sortedReadGraphEdge[i].vertices.second.label)
            assertEquals(sortedGraphEdge[i].weight, sortedReadGraphEdge[i].weight)
        }
    }
}

