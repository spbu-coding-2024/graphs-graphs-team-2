package io.ioNeo4j

import org.springframework.data.neo4j.core.schema.*

@RelationshipProperties
class EdgeNeo4j(
    _vertex: VertexNeo4j,
    @GeneratedValue
    @RelationshipId
    private var id: Long? = null
) {
    private var weight: Long? = null;

    @TargetNode
    private var vertex = _vertex;
}