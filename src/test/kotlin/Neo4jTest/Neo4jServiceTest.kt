package Neo4jTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import model.Graph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import viewModel.graph.GraphViewModel
import androidx.compose.ui.unit.dp
import io.ioNeo4j.Neo4jService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan


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

        @DynamicPropertySource
        @JvmStatic
        fun neo4jProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.neo4j.uri") { neo4j.boltURI().toString() }
            registry.add("spring.neo4j.authentication.username") { "neo4j" }
            registry.add("spring.neo4j.authentication.password") { null }
        }
    }

    @Autowired
    private lateinit var neo4jService: Neo4jService

    @Test
    fun `test write and read graph`() {
        val mapOfPlacement = mutableMapOf<String,Pair<Dp,Dp>>()

        val graph = Graph(true, true)
        val v1 = graph.addVertex(1, "Dima")
        val v2 = graph.addVertex(2, "Egorr")
        graph.addEdge(1, 2, "Friend", 3, 10.0F)
        graph.addEdge(2, 1, "Friendd", 4, 12.0F)


        val placement = mapOf(
            v1 to Pair(10.dp, 10.dp),
            v2 to Pair(20.dp, 20.dp)
        )

        placement.forEach { v ->
            mapOfPlacement.put(v.key.label,v.value)
        }


        val viewModel = GraphViewModel(
            graph,
            placement,
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false)
        )

        neo4jService.writeData(viewModel)

        val (readGraph, readPlacement) = neo4jService.readData()

        assertEquals(readGraph.isDirected, graph.isDirected)
        assertEquals(readGraph.isWeighted, graph.isWeighted)
        assertEquals(readGraph.vertices.size, graph.vertices.size)
        assertEquals(readGraph.edges.size, graph.edges.size)


        readPlacement.forEach { (readV, readCoords) ->
            assertEquals(mapOfPlacement[readV.label],readCoords)
        }


        val sortedReadGraphVertex = readGraph.vertices.sortedBy { it.label.length }
        val sortedGraphVertex = graph.vertices.sortedBy { it.label.length }

        for( i in 0..<sortedGraphVertex.size) {
            assertEquals(sortedGraphVertex[i].label,sortedReadGraphVertex[i].label)
        }


        val sortedReadGraphEdge = readGraph.edges.sortedBy { it.label.length }
        val sortedGraphEdge = graph.edges.sortedBy { it.label.length }

        for( i in 0..<sortedGraphEdge.size) {
            assertEquals(sortedGraphEdge[i].label,sortedReadGraphEdge[i].label)
            assertEquals(sortedGraphEdge[i].vertices.first.label,sortedReadGraphEdge[i].vertices.first.label)
            assertEquals(sortedGraphEdge[i].vertices.second.label,sortedReadGraphEdge[i].vertices.second.label)
            assertEquals(sortedGraphEdge[i].weight,sortedReadGraphEdge[i].weight)
        }
    }
}

