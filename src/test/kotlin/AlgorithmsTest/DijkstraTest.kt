package AlgorithmsTest

import algo.AlgoDijkstra
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import model.Graph
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

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
                    val start = Random.Default.nextInt(firstId, lastId)
                    graph.addVertex(start.toLong(), "")

                    var end = Random.Default.nextInt(firstId, lastId)
                    while (end == start) {
                        end = Random.Default.nextInt(firstId, lastId)
                    }

                    graph.addVertex(end.toLong(), "")

                    val minWaysWeights = Array(lastId + 1) { if (it == start) 0F else infinity }
                    val parent = Array(lastId + 1) { infinity.toInt() }
                    val countOfWays = Random.Default.nextInt(5, 20)
                    val maxWeight = 5000

                    for (i in 0..countOfWays) {
                        var totalWeight = 0F
                        var oldVertex = start
                        do {
                            var newVertex = Random.Default.nextInt(firstId, lastId)
                            while (newVertex == oldVertex) {
                                newVertex = Random.Default.nextInt(firstId, lastId)
                            }
                            graph.addVertex(newVertex.toLong(), "")

                            val edge =
                                graph.addEdge(
                                    oldVertex.toLong(),
                                    newVertex.toLong(),
                                    "",
                                    calculateEdgeId(oldVertex, newVertex).toLong(),
                                    Random.Default.nextInt(100, 1000).toFloat(),
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
                                            if (
                                                minWaysWeights[currentVertex] + edge.weight <
                                                    minWaysWeights[edge.vertices.second.id.toInt()]
                                            ) {
                                                minWaysWeights[edge.vertices.second.id.toInt()] =
                                                    minWaysWeights[currentVertex] + edge.weight
                                                parent[edge.vertices.second.id.toInt()] =
                                                    currentVertex
                                                vertexForChangeWeight.add(
                                                    edge.vertices.second.id.toInt()
                                                )
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
                            val edge =
                                graph.addEdge(
                                    oldVertex.toLong(),
                                    end.toLong(),
                                    "",
                                    calculateEdgeId(oldVertex, end).toLong(),
                                    Random.Default.nextInt(100, 1000).toFloat(),
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
                }
                .limit(1000)
        }
    }

    /**
     * test accepts a randomly generated graph. during the graph generation, the shortest path is
     * calculated. test checks the correctness of the path that the dijkstra will give
     */
    @ParameterizedTest(name = "test for dijkstra")
    @MethodSource("graphGenerator")
    fun `check for random graph`(graph: Graph, start: Int, end: Int, correctWeight: Float) {
        val algoDijkstra = AlgoDijkstra(graph, start.toLong(), end.toLong())
        algoDijkstra.dijkstra(start.toLong())

        assertEquals(correctWeight, algoDijkstra.weightMinWay)

        var weightWayDijkstra = 0F
        val way = algoDijkstra.way

        for (i in 0..way.size - 2) {
            graph.edges.forEach { edge ->
                if (edge.vertices.first.id == way[i] && edge.vertices.second.id == way[i + 1]) {
                    weightWayDijkstra += edge.weight
                }
            }
        }
        assertEquals(correctWeight, weightWayDijkstra)
    }

    /**
     *     A      Test finds the shortest path from:
     *    / \     A -> A = null, A -> B = 5, A -> C = 8
     *  5/   \9   in simple graph.
     *  ↓     ↓
     *  B ──→ C
     *     3
     */
    @Test
    fun `simple graph`() {
        val graph = Graph(true, true)
        graph.addVertex(1, "A")
        graph.addVertex(2, "B")
        graph.addVertex(3, "C")
        graph.addEdge(1, 2, "A -> B", 4, 5F)
        graph.addEdge(2, 3, "B -> C", 5, 3F)
        graph.addEdge(1, 3, "A -> C", 6, 9F)

        val algoDijkstra1 = AlgoDijkstra(graph, 1, 1)
        algoDijkstra1.dijkstra(1)

        assertEquals(null, algoDijkstra1.weightMinWay)

        val algoDijkstra2 = AlgoDijkstra(graph, 1, 2)
        algoDijkstra2.dijkstra(1)

        assertEquals(5F, algoDijkstra2.weightMinWay)

        val algoDijkstra3 = AlgoDijkstra(graph, 1, 3)
        algoDijkstra3.dijkstra(1)

        assertEquals(8F, algoDijkstra3.weightMinWay)
    }

    /**
     *     A     Test finds the shortest path from:
     *    / ↑    A -> A = null, A -> B = 2, A -> C = 5
     *  2/   \1  in graph with cycle
     *  ↓     \
     *  B ──→ C
     *     3
     */
    @Test
    fun `graph with cycle`() {
        val graph = Graph(true, true)
        graph.addVertex(1, "A")
        graph.addVertex(2, "B")
        graph.addVertex(3, "C")
        graph.addEdge(1, 2, "A -> B", 4, 2F)
        graph.addEdge(2, 3, "B -> C", 5, 3F)
        graph.addEdge(3, 1, "C -> A", 6, 1F)

        val algoDijkstra1 = AlgoDijkstra(graph, 1, 1)
        algoDijkstra1.dijkstra(1)

        assertEquals(null, algoDijkstra1.weightMinWay)

        val algoDijkstra2 = AlgoDijkstra(graph, 1, 2)
        algoDijkstra2.dijkstra(1)

        assertEquals(2F, algoDijkstra2.weightMinWay)

        val algoDijkstra3 = AlgoDijkstra(graph, 1, 3)
        algoDijkstra3.dijkstra(1)

        assertEquals(5F, algoDijkstra3.weightMinWay)
    }

    /**
     *     2
     *  A ──→ B    Test finds the shortest path from:
     *             A -> B = 2, A -> C = null, A -> D = null
     *  C ──→ D    in graph with unreachable way
     *     4
     */
    @Test
    fun `unconnected graph`() {
        val graph = Graph(true, true)
        graph.addVertex(1, "A")
        graph.addVertex(2, "B")
        graph.addVertex(3, "C")
        graph.addVertex(4, "D")
        graph.addEdge(1, 2, "A -> B", 5, 2F)
        graph.addEdge(3, 4, "C -> D", 6, 4F)

        val algoDijkstra1 = AlgoDijkstra(graph, 1, 2)
        algoDijkstra1.dijkstra(1)

        assertEquals(2F, algoDijkstra1.weightMinWay)

        val algoDijkstra2 = AlgoDijkstra(graph, 1, 3)
        algoDijkstra2.dijkstra(1)

        assertEquals(null, algoDijkstra2.weightMinWay)

        val algoDijkstra3 = AlgoDijkstra(graph, 1, 4)
        algoDijkstra3.dijkstra(1)

        assertEquals(null, algoDijkstra3.weightMinWay)
    }

    /**
     *        6
     *     A ──→ D      Test finds the shortest path from:
     *    / \    ↑      A -> B = 4, A -> C = 3, A -> D = 5
     *  5/   \3  /2     in graph with multiple path between:
     *  ↓     ↓ /       A -> D, A -> B.
     *  B ←── C
     *     1
     */
    @Test
    fun `multiple paths between nodes`() {
        val graph = Graph(true, true)
        graph.addVertex(1, "A")
        graph.addVertex(2, "B")
        graph.addVertex(3, "C")
        graph.addVertex(4, "D")
        graph.addEdge(1, 2, "A -> B", 5, 5F)
        graph.addEdge(1, 3, "A -> C", 6, 3F)
        graph.addEdge(3, 2, "C -> B", 7, 1F)
        graph.addEdge(1, 4, "A -> D", 7, 6F)
        graph.addEdge(3, 4, "C -> D", 7, 2F)

        val algoDijkstra1 = AlgoDijkstra(graph, 1, 2)
        algoDijkstra1.dijkstra(1)

        assertEquals(4F, algoDijkstra1.weightMinWay)

        val algoDijkstra2 = AlgoDijkstra(graph, 1, 3)
        algoDijkstra2.dijkstra(1)

        assertEquals(3F, algoDijkstra2.weightMinWay)

        val algoDijkstra3 = AlgoDijkstra(graph, 1, 4)
        algoDijkstra3.dijkstra(1)

        assertEquals(5F, algoDijkstra3.weightMinWay)
    }

    /** Test finds the shortest path from: A -> A = null in graph with one node. */
    @Test
    fun `graph with one node`() {
        val graph = Graph(true, true)
        graph.addVertex(1, "A")

        val algoDijkstra1 = AlgoDijkstra(graph, 1, 1)
        algoDijkstra1.dijkstra(1)

        assertEquals(null, algoDijkstra1.weightMinWay)
    }

    /**
     * A ──→ B ──→ C 0 5 Test finds the shortest path from: A -> B = 0, A -> C = 5 in graph with
     * zero weight edge.
     */
    @Test
    fun `graph with zero weight`() {
        val graph = Graph(true, true)
        graph.addVertex(1, "A")
        graph.addVertex(2, "B")
        graph.addVertex(3, "C")
        graph.addEdge(1, 2, "A -> B", 5, 0F)
        graph.addEdge(2, 3, "B -> C", 6, 5F)

        val algoDijkstra1 = AlgoDijkstra(graph, 1, 3)
        algoDijkstra1.dijkstra(1)

        assertEquals(5F, algoDijkstra1.weightMinWay)

        val algoDijkstra2 = AlgoDijkstra(graph, 1, 2)
        algoDijkstra2.dijkstra(1)

        assertEquals(0F, algoDijkstra2.weightMinWay)
    }

    /**
     *           A              Test finds the shortest path from:
     *         /   \            A -> B = 4, A -> C = 2, A -> D = 5, A -> E = 6
     *       4/     \2          in graph with cycle and multiple path between nodes.
     *       /       \
     *      ↓         ↓
     *      B ---5--→  C
     *       \        / \
     *       10\    /3   \8
     *          \  /      \
     *           ↓↓        ↓
     *            D --1--→ E
     *            ↑        |
     *            |<---4---|
     */
    @Test
    fun `big graph`() {
        val graph = Graph(true, true)
        graph.addVertex(1, "A")
        graph.addVertex(2, "B")
        graph.addVertex(3, "C")
        graph.addVertex(4, "D")
        graph.addVertex(5, "E")
        graph.addEdge(1, 2, "A -> B", 6, 4F)
        graph.addEdge(1, 3, "A -> C", 7, 2F)
        graph.addEdge(2, 3, "B -> C", 8, 5F)
        graph.addEdge(2, 4, "B -> D", 9, 10F)
        graph.addEdge(3, 4, "C -> D", 10, 3F)
        graph.addEdge(3, 5, "C -> E", 11, 8F)
        graph.addEdge(4, 5, "D -> E", 12, 1F)
        graph.addEdge(5, 4, "E -> D", 13, 4F)

        val algoDijkstra1 = AlgoDijkstra(graph, 1, 2)
        algoDijkstra1.dijkstra(1)

        assertEquals(4F, algoDijkstra1.weightMinWay)

        val algoDijkstra2 = AlgoDijkstra(graph, 1, 3)
        algoDijkstra2.dijkstra(1)

        assertEquals(2F, algoDijkstra2.weightMinWay)

        val algoDijkstra3 = AlgoDijkstra(graph, 1, 4)
        algoDijkstra3.dijkstra(1)

        assertEquals(5F, algoDijkstra3.weightMinWay)

        val algoDijkstra4 = AlgoDijkstra(graph, 1, 5)
        algoDijkstra4.dijkstra(1)

        assertEquals(6F, algoDijkstra4.weightMinWay)
    }
}
