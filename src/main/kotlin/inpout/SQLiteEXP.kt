package inpout

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


object Graphs : IntIdTable() {
    val graphName = varchar("graph_name", 255).uniqueIndex()
}

object Vertices : IntIdTable() {
    val vertex = long("vertex_num")
    val x = double("x")
    val y = double("y")
    val graph_id = integer("graph_id").references(Graphs.id, onDelete = ReferenceOption.CASCADE)
    init {
        uniqueIndex(vertex, graph_id)
    }
}

object Edges : IntIdTable() {
    val weight = double("weight")
    val edge = long("edge_id")
    val vertexFrom = integer("vertex_numFrom")
    val vertexTO = integer("vertex_numTo")
    val graph_id = integer("graph_id").references(Graphs.id, onDelete = ReferenceOption.CASCADE)
}

class SQLiteEXP(dbPath: String) {
    init {
        Database.connect(
            "jdbc:sqlite:$dbPath",
            driver = "org.sqlite.JDBC",
            setupConnection = { it.createStatement().execute("PRAGMA foreign_keys=ON") })
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Edges, Vertices, Graphs)
        }
    }

    private val path: String = dbPath
    fun addGraph(name: String): Int {
        //Database.connect("jdbc:sqlite:$path", driver = "org.sqlite.JDBC", setupConnection = { it.createStatement().execute("PRAGMA foreign_keys=ON") })
        val id =
            try {
                transaction {
                    val t = Graphs.insertAndGetId {
                        it[graphName] = name
                    }
                    return@transaction t.value
                }
            }catch (e:Exception){
                return -1
            }
        return id
    }

    fun addEdge(graphId: Int, fromVertex: Int, toVertex: Int, weight_d : Double,edge_d:Long) {
        transaction {
            Edges.insert {
                it[graph_id] = graphId
                it[edge]=edge_d
                it[weight] = weight_d
                it[vertexTO] = toVertex
                it[vertexFrom] = fromVertex
            }
        }
    }

    fun addVerrtex(graphId: Int, vertexnum: Long, xc: Double, yc: Double) {
        //Database.connect("jdbc:sqlite:$path", driver = "org.sqlite.JDBC", setupConnection = { it.createStatement().execute("PRAGMA foreign_keys=ON") })
        try{
        transaction {
            Vertices.insert {
                it[graph_id] = graphId
                it[vertex] = vertexnum
                it[x] = xc
                it[y] = yc
            }
        }
        }catch (e:Exception){
            return
        }
    }

    fun findGraph(name: String): Int {
        //Database.connect("jdbc:sqlite:$path", driver = "org.sqlite.JDBC", setupConnection = { it.createStatement().execute("PRAGMA foreign_keys=ON") })
        val c = transaction {
            Graphs.select { Graphs.graphName eq name }.singleOrNull()?.get(Graphs.id)?.value
        }
        return c ?: -1
    }

    fun findVertices(graphId: Int): List<vertexInfo> {
        val p = transaction {
            Vertices.select { Vertices.graph_id eq graphId }
                .map { vertexInfo(it[Vertices.vertex], it[Vertices.x], it[Vertices.y]) }
        }
        return p
    }

    fun findEdges(graphId: Int): List<edgeInfo> {
        val p = transaction {
            Edges.select { Edges.graph_id eq graphId }.map { edgeInfo(it[Edges.vertexTO], it[Edges.vertexFrom],it[Edges.weight], it[Edges.edge]) }
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
            Graphs.selectAll().map { it[Graphs.graphName] }
        }
        return t
    }
    fun deleteAll(){
        transaction {
            Graphs.deleteAll()
        }
    }
}

data class vertexInfo(val vert: Long, val x: Double, val y: Double)
data class edgeInfo(val vertexTo: Int, val edgeTo: Int, val weight: Double, val id:Long)
