package model

interface AbstractEdge <E, V> {
    var element: E
    val vertices: Pair<AbstractVertex<V>, AbstractVertex<V>>
    val weight: Long?
}
