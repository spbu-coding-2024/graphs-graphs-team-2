package io

import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.neo4j.core.schema.Relationship.Direction

@Node("Vertex")
class VertexNeo4j(
    @Id @GeneratedValue
    var id: Long? = null,

    @Property
    val x: Double? = null,
    @Property
    val y: Double? = null,
) {
    @Relationship(type = "Edge",direction = Direction.OUTGOING)
    var edges: Set<EdgeNeo4j>? = null

    @Relationship(type = "Graph type", direction = Direction.OUTGOING)
    var graphType : GraphType? = null
}
