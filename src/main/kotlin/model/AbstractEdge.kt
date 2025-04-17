package model

interface AbstractEdge {
    val id: Long
    val label: String
    val vertices: Pair<AbstractVertex, AbstractVertex>
    val weight: Float
}
