package io.ioNeo4j

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
    fun writeData(graphViewModel: GraphViewModel) {
        val allVertex = mutableMapOf<Long, VertexNeo4j>()
        graphViewModel.vertices.forEach { vertex ->
            allVertex.put(
                vertex.ID,
                VertexNeo4j(
                    label = vertex.label,
                    x = vertex.x.value,
                    y = vertex.y.value,
                    isDirected = graphViewModel.isDirected,
                    isWeighted = graphViewModel.isWeighted
                )
            )
        }
        graphViewModel.edges.forEach { edge ->
            val firstVertex = allVertex[edge.u.ID] ?: throw IllegalArgumentException("no such vertex in graph")
            val weightEdge: Float?
            if (firstVertex.isWeighted == false) {
                weightEdge = null
            } else {
                weightEdge = edge.weight.toFloat()
            }
            firstVertex.edges.add(
                EdgeNeo4j(
                    vertex = allVertex[edge.v.ID] ?: throw IllegalArgumentException("no such vertex in graph"),
                    label = edge.label,
                    weight = weightEdge
                )
            )
        }
        allVertex.forEach { vertex -> writeRepo.save<VertexNeo4j>(vertex.value) }
    }

    fun readData(): GraphViewModel {

        val allVertex = readRepo.findAll()

        val graph = Graph(allVertex[0].isDirected, allVertex[0].isWeighted)
        val placement: MutableMap<AbstractVertex, Pair<Dp, Dp>?> = mutableMapOf()

        for (i in 0..<allVertex.size) {
            val firstNode = allVertex[i]
            val firstNodeId = firstNode.id ?: throw IllegalArgumentException("node without Id")

            val AddedVertex = graph.addVertex(firstNodeId, firstNode.label)
            val x = firstNode.x
            val y = firstNode.y
            if (x == null || y == null) {
                placement.put(AddedVertex, null)
            } else {
                placement.put(AddedVertex, Pair(x.dp, y.dp))
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

//fun main() {
    //val context = runApplication<Neo4jApplication>()
    //val neo4jService = context.getBean(Neo4jService::class.java)

    //val graph = Graph(true, true)
    //val dima = graph.addVertex(1, label = "Dima")
    //val egor = graph.addVertex(2, label = "Egor")
    //val ulia = graph.addVertex(3, label = "Ulia")
    //graph.addEdge(1, 2, "Friend", 4, 10.0F)
    //graph.addEdge(1, 3, "Friend", 5, 10.0F)
    //graph.addEdge(2, 3, "BoyFriend", 6, 10.0F)
    //val placement = mapOf(
        //dima to Pair(10.0.dp, 10.0.dp), egor to Pair(20.0.dp, 20.0.dp), ulia to Pair(30.0.dp, 30.0.dp)
    //)

    //var graphViewModel =
        //GraphViewModel(graph, placement, mutableStateOf(false), mutableStateOf(false), mutableStateOf(false))

    //neo4jService.writeData(graphViewModel)
    //val graphViewModel = neo4jService.readData()
    //print("1")
//}