package DijkstraTest

import algo.AlgoDijkstra
import model.Graph
import org.junit.jupiter.params.ParameterizedTest
import java.util.stream.Stream
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.random.Random
import kotlin.test.assertEquals

class DijkstraTest {

    companion object {

        @JvmStatic
        fun graphGenerator(): Stream<Arguments> {
            val infinity = 1_000_000_000_000_000_000F
            val firstId = 0
            val lastId = 100
            return Stream.generate {

                val calculateEdgeId = { firstVertexId: Int, secondVertexId: Int ->
                    firstVertexId * 1000 + secondVertexId
                }

                val weightsOfEdges = mutableMapOf<Int, Int>()

                val graph = Graph(true, true)
                val start = Random.nextInt(firstId, lastId)
                graph.addVertex(start.toLong(), "")

                var end = Random.nextInt(firstId, lastId)
                while (end == start) {
                    end = Random.nextInt(firstId, lastId)
                }

                graph.addVertex(end.toLong(), "")

                val minWaysWeights = Array(lastId + 1) { if (it == start) 0F else infinity }
                val parent = Array(lastId + 1) { infinity.toInt() }
                val countOfWays = Random.nextInt(5, 20)
                val maxWeight = 5000

                for (i in 0..countOfWays) {
                    var totalWeight = 0F
                    var oldVertex = start
                    do {
                        var newVertex = Random.nextInt(firstId, lastId)
                        while (newVertex == oldVertex) {
                            newVertex = Random.nextInt(firstId, lastId)
                        }
                        graph.addVertex(newVertex.toLong(), "")

                        val edge = graph.addEdge(
                            oldVertex.toLong(),
                            newVertex.toLong(),
                            "",
                            calculateEdgeId(oldVertex, newVertex).toLong(),
                            Random.nextInt(100, 1000).toFloat()
                        )
                        val weight = edge.weight.toInt()
                        weightsOfEdges.put(calculateEdgeId(oldVertex, newVertex), weight)
                        totalWeight += weight


                        if (totalWeight < minWaysWeights[newVertex]) {
                            minWaysWeights[newVertex] = totalWeight
                            parent[newVertex] = oldVertex
                            val vertexForChangeWeight = ArrayDeque<Int>()
                            vertexForChangeWeight.add(newVertex)
                            do {
                                val currentVertex = vertexForChangeWeight.get(0)
                                vertexForChangeWeight.remove(currentVertex)
                                graph.edges.forEach { edge ->
                                    if (edge.vertices.first.id.toInt() == currentVertex) {
                                        if (minWaysWeights[currentVertex] + edge.weight < minWaysWeights[edge.vertices.second.id.toInt()]) {
                                            minWaysWeights[edge.vertices.second.id.toInt()] =
                                                minWaysWeights[currentVertex] + edge.weight
                                            parent[edge.vertices.second.id.toInt()] = currentVertex
                                            vertexForChangeWeight.add(edge.vertices.second.id.toInt())
                                        }
                                    }
                                }
                            } while (vertexForChangeWeight.isNotEmpty())
                        } else {
                            totalWeight = minWaysWeights[newVertex]
                        }
                        oldVertex = newVertex
                    } while (totalWeight < maxWeight && oldVertex != end)
                    if (oldVertex != end) {
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
                Arguments.of(graph, start, end, minWaysWeights[end])
            }.limit(5000)
        }
    }

    @ParameterizedTest(name = "test for dijkstra")
    @MethodSource("graphGenerator")
    fun `check for random graph`(
        graph: Graph, start: Int, end: Int, correctWeight: Float
    ) {
        val algoDijkstra = AlgoDijkstra(graph, start.toLong(), end.toLong())
        algoDijkstra.dijkstra(start.toLong())

        assertEquals(correctWeight, algoDijkstra.weightMinWay)
    }
}
