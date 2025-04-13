package io

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.boot.runApplication
import androidx.compose.ui.unit.dp

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
@EnableNeo4jRepositories
open class Application {

    @Bean
    open fun databaseInitializer(graphRepository: GraphRepository) = ApplicationRunner {
        if(graphRepository.count() == 0L){
            val v1 = VertexNeo4j(x = 50.dp,y = 50.dp)
            val v2 = VertexNeo4j(x = 100.dp,y = 10.dp)
            val v3 = VertexNeo4j(x = 200.dp,y = 20.dp)
            v1.edges = hashSetOf(v2,v3)
            v2.edges = hashSetOf(v1,v3)
            v3.edges = hashSetOf(v2,v1)
            graphRepository.save<VertexNeo4j>(v1)
            graphRepository.save<VertexNeo4j>(v2)
            graphRepository.save<VertexNeo4j>(v3)
        }
        graphRepository.findAll().forEach { println(it) }
    }
}