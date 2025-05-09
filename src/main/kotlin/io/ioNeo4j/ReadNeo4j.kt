package io.ioNeo4j

import androidx.compose.ui.unit.Dp
import model.Graph
import model.abstractGraph.AbstractVertex
import org.springframework.boot.builder.SpringApplicationBuilder

fun ReadNeo4j(
    username: String,
    password: String,
): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>> {
    val context =
        SpringApplicationBuilder(Neo4jApplication::class.java)
            .properties(
                mapOf(
                    "spring.neo4j.authentication.username" to username,
                    "spring.neo4j.authentication.password" to password,
                )
            )
            .run()
    val graph = context.getBean(Neo4jService::class.java).readData()
    context.close()
    return graph
}
