package model

interface AbstractEdge {
    var id: Long
    var label: String
    val vertices: Pair<AbstractVertex, AbstractVertex>
    val weight: Float
}
