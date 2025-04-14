package io

import org.springframework.data.neo4j.core.schema.*

@RelationshipProperties
class EdgeNeo4j(
    @Id @GeneratedValue
    var id: Long? = null,

    @TargetNode
    var vertex: VertexNeo4j,

    var weight: Long? = null
)