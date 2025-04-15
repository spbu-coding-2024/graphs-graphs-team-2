package io

import org.springframework.data.neo4j.core.schema.*

@Node("GraphType")
class GraphType (
    @Id @GeneratedValue
    var id: Long? = null,

    @Property
    var isDirected : Boolean,

    @Property
    var isWeighted : Boolean
)