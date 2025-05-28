package io.ioNeo4j

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.Graph
import model.abstractGraph.AbstractVertex
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.stereotype.Service
import viewModel.graph.GraphViewModel

@SpringBootApplication @EnableNeo4jRepositories open class Neo4jApplication

@Service
class Neo4jService(
    private val writeRepo: WriteRepositoryNeo4j,
    private val readRepo: ReadRepositoryNeo4j,
) {
    fun writeData(graphViewModel: GraphViewModel) {
        clearDatabase()
        val allVertex = mutableMapOf<Long, VertexNeo4j>()
        graphViewModel.vertices.forEach { vertex ->
            allVertex.put(
                vertex.ID,
                VertexNeo4j(
                    label = vertex.label,
                    x = vertex.x.value,
                    y = vertex.y.value,
                    isDirected = graphViewModel.isDirected,
                    isWeighted = graphViewModel.isWeighted,
                ),
            )
        }
        graphViewModel.edges.forEach { edge ->
            val firstVertex =
                allVertex[edge.u.ID] ?: throw IllegalArgumentException("no such vertex in graph")
            val weightEdge: Float?
            if (firstVertex.isWeighted == false) {
                weightEdge = null
            } else {
                weightEdge = edge.weight.toFloat()
            }
            firstVertex.edges.add(
                EdgeNeo4j(
                    vertex =
                        allVertex[edge.v.ID]
                            ?: throw IllegalArgumentException("no such vertex in graph"),
                    label = edge.label,
                    weight = weightEdge,
                )
            )
        }
        allVertex.forEach { vertex -> writeRepo.save<VertexNeo4j>(vertex.value) }
    }

    fun readData(): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>> {

        val allVertex = readRepo.findAll()

        val graph = Graph(allVertex[0].isDirected, allVertex[0].isWeighted)
        val placement: MutableMap<AbstractVertex, Pair<Dp?, Dp?>?> = mutableMapOf()

        for (i in 0..<allVertex.size) {
            val firstNode = allVertex[i]
            val firstNodeId = firstNode.id ?: throw IllegalArgumentException("node without Id")

            val AddedVertex = graph.addVertex(firstNodeId, firstNode.label)
            val x = firstNode.x
            val y = firstNode.y
            placement.put(AddedVertex, Pair(x?.dp, y?.dp))

            val edges = firstNode.edges

            for (i in 0..<(edges.size)) {
                val edge = firstNode.edges[i]
                val edgeId = edge.id ?: throw IllegalArgumentException("edge without Id")

                val secondNode = edge.vertex
                val secondNodeId =
                    secondNode.id ?: throw IllegalArgumentException("node without Id")

                graph.addVertex(secondNodeId, secondNode.label)
                graph.addEdge(
                    firstNodeId,
                    secondNodeId,
                    edge.label,
                    edgeId,
                    edge.weight?.toFloat() ?: 1.0F,
                )
            }
        }

        return Pair(graph, placement)
    }
    fun clearDatabase(){
        writeRepo.deleteAll()
        readRepo.deleteAll()
    }
}

