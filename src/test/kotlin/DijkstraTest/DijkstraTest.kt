package DijkstraTest

import model.Graph
import java.util.stream.Stream
import org.junit.jupiter.params.provider.Arguments
import kotlin.math.min
import kotlin.random.Random

class DijkstraTest {
    private val infinity = 1_000_000_000_000_000_000F
    private val firstId = 0
    private val secondId = 100


    fun graphGenerator(): Stream<Arguments> {
        return Stream.generate {

            val calculateEdgeId = { firstVertexId: Int, secondVertexId: Int ->
                firstVertexId * 1000 + secondVertexId
            }

            val weightsOfEdges = mutableMapOf<Int, Int>()

            val graph = Graph(true, true)
            val start = Random.nextInt(firstId, secondId)
            graph.addVertex(start.toLong(), "")
            val end = Random.nextInt(firstId, secondId)
            graph.addVertex(end.toLong(), "")

            val minWaysWeights = Array(secondId + 1) { if (it == start) 0F else infinity }
            val parent = Array(secondId + 1) { infinity.toInt() }
            val countOfWays = Random.nextInt(5, 6)
            val maxWeight = 2000

            for (i in 0..countOfWays) {
                var totalWeight = 0F
                var oldVertex = start
                do {
                    val newVertex = Random.nextInt(firstId, secondId)
                    graph.addVertex(newVertex.toLong(), "")
                    var weight = Random.nextInt(100, 1000)
                    val edge = graph.addEdge(
                        oldVertex.toLong(),
                        newVertex.toLong(),
                        "",
                        calculateEdgeId(oldVertex, newVertex).toLong(),
                        weight.toFloat()
                    )
                    weight = edge.weight.toInt()
                    weightsOfEdges.put(calculateEdgeId(oldVertex, newVertex), weight)
                    totalWeight += weight

                    if (totalWeight < minWaysWeights[newVertex]) {
                        minWaysWeights[newVertex] = totalWeight
                        parent[newVertex] = oldVertex
                        var currentVertex = newVertex
                        val vertexForChangeWeight = ArrayDeque<Int>()
                        do {
                            graph.vertices.forEach { vertex ->
                                if (parent[vertex.id.toInt()] == currentVertex) {
                                    if (minWaysWeights[currentVertex] + (weightsOfEdges.get(
                                            calculateEdgeId(
                                                currentVertex,
                                                vertex.id.toInt()
                                            )
                                        ) ?: infinity.toInt()) < minWaysWeights[vertex.id.toInt()]
                                    ) {
                                        minWaysWeights[vertex.id.toInt()] =
                                            minWaysWeights[currentVertex] + (weightsOfEdges.get(
                                                calculateEdgeId(
                                                    currentVertex,
                                                    vertex.id.toInt()
                                                )
                                            ) ?: infinity.toInt())
                                        vertexForChangeWeight.add(vertex.id.toInt())
                                    }
                                }
                            }
                            vertexForChangeWeight.remove(currentVertex)
                            currentVertex = vertexForChangeWeight.get(0)
                        } while (vertexForChangeWeight.size != 0)
                    } else {
                        totalWeight = minWaysWeights[newVertex]
                    }
                    oldVertex = newVertex
                } while (totalWeight < maxWeight && oldVertex != end)
                if(oldVertex != end) {
                    val edge = graph.addEdge(
                        oldVertex.toLong(),
                        end.toLong(),
                        "",
                        calculateEdgeId(oldVertex, end).toLong(),
                        Random.nextInt(100, 1000).toFloat()
                    )
                    val weight = edge.weight.toInt()
                    weightsOfEdges.put(calculateEdgeId(oldVertex, end), weight)
                    totalWeight += weight
                    if (totalWeight < minWaysWeights[end]) {
                        minWaysWeights[end] = totalWeight
                        parent[end] = oldVertex
                    }
                }
            }
            Arguments.of(graph, parent)
        }.limit(10)
    }
}