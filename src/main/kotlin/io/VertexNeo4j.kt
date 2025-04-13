package io


import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.neo4j.core.schema.Relationship.Direction
import androidx.compose.ui.unit.Dp

@Node("Vertex")
class VertexNeo4j(
    @Id @GeneratedValue
    var id: Long? = null,
    val x: Dp,
    val y: Dp
){
    @Relationship(direction = Direction.OUTGOING)
    var edges: HashSet<VertexNeo4j>? = null
}
