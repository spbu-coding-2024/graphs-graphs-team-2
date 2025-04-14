package io

import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.neo4j.core.schema.Relationship.Direction

@Node("Vertex")
class VertexNeo4j(
    @Id @GeneratedValue
    var id: Long? = null,

    val x: Double? = null,
    val y: Double? = null,
) {
    @Relationship(direction = Direction.OUTGOING)
    var edges: Set<EdgeNeo4j>? = null
}
