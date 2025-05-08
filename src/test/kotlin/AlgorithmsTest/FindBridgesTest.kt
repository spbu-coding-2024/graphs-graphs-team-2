package AlgorithmsTest

import algo.AlgoBridges
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.test.assertEquals
import model.Graph
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class FindBridgesTest {
    companion object {

        @JvmStatic
        fun graphGenerator(): Stream<Arguments> {
            return Stream.generate {
                    val bridges = ArrayDeque<Pair<Long, Long>>()

                    val components = ArrayDeque<Set<Int>>()

                    val calculateEdgeId = { firstVertexId: Int, secondVertexId: Int ->
                        firstVertexId * 100000 + secondVertexId
                    }

                    val graph = Graph()
                    val countOfComponents = Random.Default.nextInt(2, 100)
                    for (i in 0..<countOfComponents) {
                        val countNodesInComponent = Random.Default.nextInt(1, 100)
                        val firstId = i * 100
                        val lastId = i * 100 + countNodesInComponent
                        val component = mutableSetOf<Int>()
                        for (j in 0..<countNodesInComponent) {
                            val id = Random.Default.nextInt(firstId, lastId)
                            graph.addVertex(id.toLong(), "")
                            component.add(id)
                        }
                        component.forEach { firstVertex ->
                            component.forEach { secondVertex ->
                                if (firstVertex != secondVertex) {
                                    graph.addEdge(
                                        firstVertex.toLong(),
                                        secondVertex.toLong(),
                                        "",
                                        calculateEdgeId(firstVertex, secondVertex).toLong(),
                                    )
                                }
                            }
                        }
                        components.add(component)
                    }
                    for (i in 0..components.size - 2) {
                        val firstVertex = components[i].random()
                        val secondVertex = components[i + 1].random()
                        graph.addEdge(
                            firstVertex.toLong(),
                            secondVertex.toLong(),
                            "",
                            calculateEdgeId(firstVertex, secondVertex).toLong(),
                        )
                        bridges.add(Pair(firstVertex.toLong(), secondVertex.toLong()))
                    }
                    Arguments.of(graph, bridges)
                }
                .limit(500)
        }
    }

    @ParameterizedTest(name = "test for findBridges")
    @MethodSource("graphGenerator")
    fun `check for random graph`(graph: Graph, correctBridges: ArrayDeque<Pair<Long, Long>>) {
        val algoBridges = AlgoBridges(graph)
        algoBridges.findBridges()
        val bridges = algoBridges.bridges

        assertEquals(correctBridges.size, bridges.size)

        for (i in bridges) {
            assertEquals(
                correctBridges.contains(i) || correctBridges.contains(Pair(i.second, i.first)),
                true,
            )
        }
    }
}
