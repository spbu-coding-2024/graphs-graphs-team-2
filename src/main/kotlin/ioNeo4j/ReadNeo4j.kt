package ioNeo4j

import model.Graph
import org.springframework.context.annotation.Bean
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import model.UndirectedWeightedGraph


fun main() {
    val graph = ReadNeo4j()
}

fun ReadNeo4j(): Graph<Long?, Long?> {
    val context = runApplication<ReadDatabase>()
    return context.getBean("initializedGraph") as Graph<Long?, Long?>
}

@SpringBootApplication
@EnableNeo4jRepositories
open class ReadDatabase {

    @Bean
    open fun graphInitializer(
        graphRepository: ReadRepositoryNeo4j,
        configurableContext: ConfigurableApplicationContext
    ) = ApplicationRunner {


        val graph = UndirectedWeightedGraph<Long?, Long?>()


        val allVertex = graphRepository.findAll()
        graph.isDirected = allVertex[0].isDirected
        graph.isWeighted = allVertex[0].isWeighted


        allVertex.forEach { node ->
            graph.addVertex(node.id)
            node.edges?.forEach { edge ->
                graph.addEdge(node.id, edge.vertex.id, edge.id, edge.weight)
            }
        }

        configurableContext.beanFactory.registerSingleton("initializedGraph", graph)
    }
}
