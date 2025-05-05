package JSONTest

import androidx.compose.runtime.mutableStateOf
import java.io.File
import model.Graph
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import view.io.JsonView
import viewModel.graph.GraphViewModel

@Testcontainers
class JSONIntegrationTest {
    private lateinit var converter: JsonView
    private lateinit var graph: Graph
    private lateinit var graphViewModel: GraphViewModel

    companion object {
        @Container
        val fileStore =
            GenericContainer("busybox:1.36.1").apply {
                withCommand("sh", "-c", String.format("sleep 5 && echo \"%s\"", "Test"))
            }
    }

    @BeforeEach
    fun initJSON() {
        graph = Graph()
        converter = JsonView()
    }

    //    @AfterTest
    //    fun cleanup() {
    //        Thread.sleep(Duration.ofSeconds(25))
    //    }

    @Test
    fun `store and load directed weighted graph`() {
        assertThat(fileStore.isRunning()).isTrue()
        fileStore.execInContainer("touch", "test.json")
        val lsResult = fileStore.execInContainer("ls", "-al", "/")
        val stdout = lsResult.stdout
        val exitCode = lsResult.exitCode
        assertThat(stdout).contains("test.json")
        assertThat(exitCode).isZero()

        graph = Graph(direction = true, weight = true)
        graph.addVertex(0, "a")
        graph.addVertex(1, "b")
        graph.addVertex(2, "c")
        graph.addEdge(0, 1, "A", 0)
        graph.addEdge(1, 2, "B", 1)
        graph.addEdge(2, 0, "C", 2)
        converter.fileDialog.directory = "~/"
        converter.fileDialog.file = "test.json"
        converter.storeToJson(
            GraphViewModel(
                graph,
                mapOf(),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
            )
        ) {}

        val file = File("/home", "/test.json")
        println(file.readText())
        assert(false)
    }
}
