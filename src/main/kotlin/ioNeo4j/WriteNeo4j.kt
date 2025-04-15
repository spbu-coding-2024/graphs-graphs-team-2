package ioNeo4j


import org.neo4j.driver.Driver
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories

fun WriteNeo4j() {
    val context = runApplication<WriteInDatabase>()
}

@Configuration
@EnableNeo4jRepositories()
open class WriteInDatabase {

    @Bean
    open fun graphWriter(graphRepository: WriteRepositoryNeo4j) = ApplicationRunner {
        val v1 = VertexNeo4j(x = 100.0, y = 100.0, isWeighted = true)
        val v2 = VertexNeo4j(x = 100.0, y = 100.0, isWeighted = true)
        val v3 = VertexNeo4j(x = 100.0, y = 100.0, isWeighted = true)
        v1.edges = listOf(EdgeNeo4j(vertex = v2, weight = 3), EdgeNeo4j(vertex = v3, weight = 3))
        v2.edges = listOf(EdgeNeo4j(vertex = v1, weight = 3), EdgeNeo4j(vertex = v3, weight = 3))
        v3.edges = listOf(EdgeNeo4j(vertex = v2, weight = 3), EdgeNeo4j(vertex = v1, weight = 3))
        graphRepository.save<VertexNeo4j>(v1)
        graphRepository.save<VertexNeo4j>(v2)
        graphRepository.save<VertexNeo4j>(v3)
    }
}