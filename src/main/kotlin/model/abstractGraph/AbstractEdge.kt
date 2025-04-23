package model.abstractGraph

import model.abstractGraph.AbstractVertex

interface AbstractEdge {
    val id: Long
    val label: String
    val vertices: Pair<AbstractVertex, AbstractVertex>
    val weight: Float
}