package io.SQLiteExposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import view.graph.GraphView
import viewModel.graph.EdgeViewModel
import viewModel.graph.VertexViewModel


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

class SQLiteEXP(dbName: String) {
    var dbc =Database.connect(
        "jdbc:sqlite:$dbName",
        driver = "org.sqlite.JDBC",
        setupConnection = { it.createStatement().execute("PRAGMA foreign_keys=ON") })
    init {
        transaction(dbc) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Edges, Vertices, Graphs)
        }
    }


    fun addGraph(name: String,isDir: Boolean,isWeigh: Boolean): Int {
        var id  = 0
        transaction(dbc) {
            try {
                val t = Graphs.insertAndGetId {
                    it[graphName] = name
                    it[isDirected] = isDir
                    it[isWeighted] = isWeigh
                }
                id=t.value
            }catch (e: ExposedSQLException) {
                throw e
            }
        }
        return id
    }

    fun addEdge(graphId: Int, fromVertex: Long, toVertex: Long, weight_d : Float,edge_d:Long, label_d : String) {
        transaction(dbc) {
            Edges.insert {
                it[graph_id] = graphId
                it[edge]=edge_d
                it[weight] = weight_d
                it[vertexTO] = toVertex
                it[vertexFrom] = fromVertex
                it[label]=label_d
            }
        }
    }

    fun addVertex(graphId: Int, vertexnum: Long, xc: Float, yc: Float,labell:String) {
        transaction {
            try {
                Vertices.insert {
                    it[graph_id] = graphId
                    it[vertex] = vertexnum
                    it[x] = xc
                    it[y] = yc
                    it[label] = labell
                }
            }catch (e: ExposedSQLException) {
                throw e
            }
        }

    }

    fun findGraph(name: String): graphInfo?{
        val c = transaction {
            Graphs.select { Graphs.graphName eq name }.singleOrNull()?.let{it -> graphInfo(it[Graphs.id].value,it[Graphs.isDirected], it[Graphs.isWeighted])}
        }
        return c
    }

    fun findVertices(graphId: Int): List<vertexInfo> {
        val p = transaction {
            Vertices.select { Vertices.graph_id eq graphId }
                .map { vertexInfo(it[Vertices.vertex], it[Vertices.x], it[Vertices.y], it[Vertices.label]) }
        }
        return p
    }

    fun findEdges(graphId: Int): List<edgeInfo> {
        val p = transaction {
            Edges.select { Edges.graph_id eq graphId }.map { edgeInfo(it[Edges.vertexFrom], it[Edges.vertexTO],it[Edges.weight], it[Edges.edge],it[Edges.label]) }
        }
        return p
    }

    fun deleteGraph(graphId: Int) {
        //Database.connect("jdbc:sqlite:$path", driver = "org.sqlite.JDBC", setupConnection = { it.createStatement().execute("PRAGMA foreign_keys=ON") })
        transaction {
            Graphs.deleteWhere { Graphs.id eq graphId }
        }
    }

    fun makeListFromNames(): List<String> {
        val t=transaction {
            Graphs.slice(Graphs.graphName).selectAll().toList()
        }
        val p : MutableList<String> = ArrayList()
        t.forEach{ row -> p.add(row[Graphs.graphName]) }
        return p

    }
    fun deleteAll(){
        transaction {
            Graphs.deleteAll()
        }
    }

    fun addAllvertices(id: Int , vertices: Collection<VertexViewModel>){
        transaction(dbc) {
            vertices.forEach {
                val vert = it
                try {
                    Vertices.insert {
                        it[graph_id] = id
                        it[vertex] = vert.ID
                        it[x] = vert.x.value
                        it[y] = vert.y.value
                        it[label] = vert.label
                    }
                }catch (e: ExposedSQLException) {
                    throw e
                }
            }
        }
    }

    fun addAllEdges(id: Int , edges: Collection<EdgeViewModel>){
        transaction(dbc) {
            edges.forEach {
                val edge = it
                Edges.insert {
                    it[graph_id] = id
                    it[Edges.edge]=edge.ID
                    it[weight] = edge.weight.toFloat()
                    it[vertexTO] = edge.u.ID
                    it[vertexFrom] = edge.v.ID
                    it[label]= edge.label
                }
            }
        }
    }

}

data class vertexInfo(val vert: Long, val x: Float, val y: Float, val label : String)
data class edgeInfo(val vertexFrom: Long, val vertexTo: Long, val weight: Float, val id:Long, val label:String)
data class graphInfo(val id:Int, val isDirected:Boolean, val isWeighted:Boolean)
