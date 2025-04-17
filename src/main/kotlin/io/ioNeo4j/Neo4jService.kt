package io.ioNeo4j

import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.AbstractVertex
import model.Graph
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.stereotype.Service
import viewModel.GraphViewModel

@SpringBootApplication
@EnableNeo4jRepositories
class Neo4jApplication

@Service
class Neo4jService(
    private val writeRepo: WriteRepositoryNeo4j,
    private val readRepo: ReadRepositoryNeo4j
) {
    fun writeData() {
        val v1 = VertexNeo4j(x = 100.0, y = 100.0, isWeighted = true, isDirected = true, label = "Dima")
        val v2 = VertexNeo4j(x = 100.0, y = 100.0, isWeighted = true, label = "Sema")
        val v3 = VertexNeo4j(x = 100.0, y = 100.0, isWeighted = true, label = "Shamil")
        v1.edges = listOf(
            EdgeNeo4j(vertex = v2, weight = 3.0, label = "fuckyou"),
            EdgeNeo4j(vertex = v3, weight = 3.0, label = "fuckyou")
        )
        v2.edges = listOf(
            EdgeNeo4j(vertex = v1, weight = 3.0, label = "fuckyou"),
            EdgeNeo4j(vertex = v3, weight = 3.0, label = "fuckyou")
        )
        v3.edges = listOf(
            EdgeNeo4j(vertex = v2, weight = 3.0, label = "fuckyou"),
            EdgeNeo4j(vertex = v1, weight = 3.0, label = "fuckyou")
        )
        writeRepo.save<VertexNeo4j>(v1)
        writeRepo.save<VertexNeo4j>(v2)
        writeRepo.save<VertexNeo4j>(v3)
    }

    fun readData(): GraphViewModel {

        val allVertex = readRepo.findAll()

        val graph = Graph(allVertex[0].isDirected, allVertex[0].isWeighted)
        val placement: MutableMap<AbstractVertex, Pair<Double, Double>?> = mutableMapOf()

        for (i in 0..<allVertex.size) {
            val firstNode = allVertex[i]
            val firstNodeId = firstNode.id ?: throw IllegalArgumentException("node without Id")

            val AddedVertex = graph.addVertex(firstNodeId, firstNode.label)
            val x = firstNode.x
            val y = firstNode.y
            if (x == null || y == null) {
                placement.put(AddedVertex, null)
            } else {
                placement.put(AddedVertex, Pair(x, y))
            }

            val edges = firstNode.edges

            for (i in 0..<(edges.size)) {
                val edge = firstNode.edges[i]
                val edgeId = edge.id ?: throw IllegalArgumentException("edge without Id")

                val secondNode = edge.vertex
                val secondNodeId = secondNode.id ?: throw IllegalArgumentException("node without Id")

                graph.addVertex(secondNodeId, secondNode.label)
                graph.addEdge(firstNodeId, secondNodeId, edge.label, edgeId, edge.weight?.toFloat() ?: 1.0F)
            }
        }

        val graphViewModel =
            GraphViewModel(graph, placement, mutableStateOf(false), mutableStateOf(false), mutableStateOf(false))

        return graphViewModel
    }
}

fun main() {
    val context = runApplication<Neo4jApplication>()
    val neo4jService = context.getBean(Neo4jService::class.java)

    //neo4jService.writeData()
    val graph = neo4jService.readData()
}