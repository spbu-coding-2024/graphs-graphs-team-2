package algo


class AlgoDijkstra(val graph: Array<Array<Pair<Int, Int>>>, val firstVertex: Int, val secondVertex: Int) {
    private val infinity: Int = 1_000_000_000
    private val sizeOfGraph = graph.size
    private val distance = Array<Int>(sizeOfGraph) { if (it == firstVertex) 0 else infinity }
    private val labels = Array<Boolean>(sizeOfGraph) { it == firstVertex }
    private val parents = Array<Int>(sizeOfGraph) {infinity}

    fun dijkstra(V: Int) {
        for (i in 0..<graph[V].size) {
            val OutV = graph[V][i].first
            val weight = graph[V][i].second
            if (!labels[OutV]) {
                if(distance[V] + weight < distance[OutV]){
                    distance[OutV] = distance[V] + weight
                    parents[OutV] = V
                }
            }
        }

        var minDistance = infinity
        var new_V : Int = infinity
        for(i in 0..<sizeOfGraph){
            if(!labels[i]){
                if(distance[i] < minDistance){
                    minDistance = distance[i]
                    new_V = i
                }
            }
        }
        if(new_V != infinity) {
            labels[new_V] = true
            if(new_V == secondVertex){
                findMinWay(new_V)
            }
            else{
                dijkstra(new_V)
            }
        }
    }

    private fun findMinWay(V : Int){
        println(V)
        if(V != firstVertex) {
            findMinWay(parents[V])
        }
    }
}
