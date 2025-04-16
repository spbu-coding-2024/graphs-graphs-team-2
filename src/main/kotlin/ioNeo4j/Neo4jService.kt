package ioNeo4j

import model.Graph
import model.UndirectedWeightedGraph
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.stereotype.Service

@SpringBootApplication
@EnableNeo4jRepositories
class Neo4jApplication

@Service
class Neo4jService(
    private val writeRepo: WriteRepositoryNeo4j,
    private val readRepo: ReadRepositoryNeo4j
) {
    fun writeData() {
        val v1 = VertexNeo4j(x = 100.0, y = 100.0, isWeighted = true)
        val v2 = VertexNeo4j(x = 100.0, y = 100.0, isWeighted = true)
        val v3 = VertexNeo4j(x = 100.0, y = 100.0, isWeighted = true)
        v1.edges = listOf(EdgeNeo4j(vertex = v2, weight = 3), EdgeNeo4j(vertex = v3, weight = 3))
        v2.edges = listOf(EdgeNeo4j(vertex = v1, weight = 3), EdgeNeo4j(vertex = v3, weight = 3))
        v3.edges = listOf(EdgeNeo4j(vertex = v2, weight = 3), EdgeNeo4j(vertex = v1, weight = 3))
        writeRepo.save<VertexNeo4j>(v1)
        writeRepo.save<VertexNeo4j>(v2)
        writeRepo.save<VertexNeo4j>(v3)
    }

    fun readData(): Graph<Long?, Long?> {
        val graph = UndirectedWeightedGraph<Long?, Long?>()


        val allVertex = readRepo.findAll()
        graph.isDirected = allVertex[0].isDirected
        graph.isWeighted = allVertex[0].isWeighted


        allVertex.forEach { node ->
            graph.addVertex(node.id)
            node.edges?.forEach { edge ->
                graph.addEdge(node.id, edge.vertex.id, edge.id, edge.weight)
            }
        }
        return graph
    }
}

fun main() {
    val context = runApplication<Neo4jApplication>()
    val neo4jService = context.getBean(Neo4jService::class.java)

    neo4jService.writeData()
    val graph = neo4jService.readData()
}