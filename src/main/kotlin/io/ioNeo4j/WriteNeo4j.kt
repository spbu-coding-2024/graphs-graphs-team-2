package io.ioNeo4j

import androidx.compose.ui.unit.Dp
import model.Graph
import model.abstractGraph.AbstractVertex
import org.springframework.boot.builder.SpringApplicationBuilder
import viewModel.graph.GraphViewModel

fun WriteNeo4j(username: String, password: String, graphViewModel: GraphViewModel) {
    val context = SpringApplicationBuilder(Neo4jApplication::class.java)
        .properties(
            mapOf(
                "spring.neo4j.authentication.username" to username,
                "spring.neo4j.authentication.password" to password
            )
        )
        .run()
    context.getBean(Neo4jService::class.java).writeData(graphViewModel)
    context.close()
}