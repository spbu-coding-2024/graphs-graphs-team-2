package io.ioNeo4j

import org.springframework.data.neo4j.core.schema.*

@RelationshipProperties
class EdgeNeo4j(
    @Id @GeneratedValue
    var id: Long? = null,

    @TargetNode
    var vertex: VertexNeo4j,

    @Property
    var label: String = "",

    @Property
    var weight: Float? = null
)