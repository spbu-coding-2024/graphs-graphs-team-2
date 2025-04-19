package io.ioNeo4j

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import viewModel.GraphViewModel

fun ReadNeo4j(username: String, password: String) : GraphViewModel{
    val context = SpringApplicationBuilder(Neo4jApplication::class.java)
        .properties(mapOf(
            "spring.neo4j.authentication.username" to username,
            "spring.neo4j.authentication.password" to password
        ))
        .run()
    return context.getBean(Neo4jService::class.java).readData()
}