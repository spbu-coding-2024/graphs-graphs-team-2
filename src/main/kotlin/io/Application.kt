package io

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
@EnableNeo4jRepositories
open class Application {

    @Bean
    open fun databaseInitializer(graphRepository: GraphRepository) = ApplicationRunner {
        val v1 = VertexNeo4j(x = 50.0, y = 50.0)
        val v2 = VertexNeo4j(x = 100.0, y = 10.0)
        val v3 = VertexNeo4j(x = 200.0, y = 20.0)
        val v4 = VertexNeo4j()
        val v5 = VertexNeo4j(x = 200.0, y = 20.0)
        v1.edges = setOf(EdgeNeo4j(vertex = v2, weight = 2), EdgeNeo4j(vertex = v3, weight = 3))
        v2.edges = setOf(EdgeNeo4j(vertex = v1, weight = 2), EdgeNeo4j(vertex = v3, weight = 3))
        v3.edges = setOf(EdgeNeo4j(vertex = v2, weight = 2), EdgeNeo4j(vertex = v1, weight = 3))
        graphRepository.save<VertexNeo4j>(v1)
        graphRepository.save<VertexNeo4j>(v2)
        graphRepository.save<VertexNeo4j>(v3)
        graphRepository.save<VertexNeo4j>(v4)
        graphRepository.save<VertexNeo4j>(v5)

        graphRepository.findAll().forEach { println(it) }
    }
}