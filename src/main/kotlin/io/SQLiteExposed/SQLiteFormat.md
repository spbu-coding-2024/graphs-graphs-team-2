# SQLite format
To load your graph from a SQLite database, provide it in this format:


```kotlin
object Graphs : IntIdTable() {  
    val graphName = varchar("graph_name", 255).uniqueIndex()  
    val isDirected = bool("is_directed")  
    val isWeighted = bool("is_weighted")  
}  
  
object Vertices : IntIdTable() {  
    val vertex = long("vertex_num")  
    val x = float("x")  
    val y = float("y")  
    val label = text("label")  
    val graph_id = integer("graph_id").references(Graphs.id, onDelete = ReferenceOption.CASCADE)  
  
    init {  
        uniqueIndex(vertex, graph_id)  
    }  
}  
  
object Edges : IntIdTable() {  
    val weight = float("weight")  
    val edge = long("edge_id")  
    val vertexFrom = long("vertex_numFrom")  
    val vertexTO = long("vertex_numTo")  
    val label = text("label")  
    val graph_id = integer("graph_id").references(Graphs.id, onDelete = ReferenceOption.CASCADE)  
}
```
To work with it , you may use Kotlin Exposed lib for ORM requests.

If you don`t want write code for working with this Database , you can use SQLiteConverter class , which you can find in source directory.