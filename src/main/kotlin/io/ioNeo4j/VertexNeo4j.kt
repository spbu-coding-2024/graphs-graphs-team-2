package io.ioNeo4j

import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.neo4j.core.schema.Relationship.Direction

@Node("Vertex")
class VertexNeo4j(
    @Id @GeneratedValue var id: Long? = null,
    @Property var label: String = "",
    @Property val x: Float? = null,
    @Property val y: Float? = null,
    @Property val isDirected: Boolean = false,
    @Property val isWeighted: Boolean = false,
) {
    @Relationship(type = "Edge", direction = Direction.OUTGOING)
    var edges: MutableList<EdgeNeo4j> = mutableListOf()
}
