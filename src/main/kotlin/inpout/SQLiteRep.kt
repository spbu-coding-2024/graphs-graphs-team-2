package inpout

import mu.KotlinLogging
import java.io.Closeable
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private val logger = KotlinLogging.logger {}

class SQLiteRep (private val dbPath: String): Closeable {
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        ?: throw SQLException("Cannot connect to database")
    private val addGraphStatement by lazy {connection.prepareStatement("INSERT INTO graphs(name) VALUES (?);")}
    private val findGraphInDB by lazy {connection.prepareStatement("SELECT * FROM graphs where name = ?;")}
    fun createDB(){
        connection.createStatement().also { statement ->
            try {
                statement.execute("PRAGMA foreign_keys=ON")
                statement.execute("CREATE TABLE IF NOT EXISTS graphs(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE);")
                logger.info("Database created")
            }catch (ex: Exception){
                logger.error(ex){"Database creation failed"}
            }
        }
    }

    fun addGraphByName(graphName: String){
        addGraphStatement.setString(1, graphName)
        try{
            addGraphStatement.execute()
        }catch (ex: Exception){
            logger.error(ex){"Database already exists $graphName"}
        }
    }

    fun findGraphId(graphName: String): Int{
           findGraphInDB.setString(1, graphName)
        try {
            val c=findGraphInDB.executeQuery()
            c.next()
            val id=c.getInt("id")
            return id
        }catch (ex: Exception){
            logger.error(ex){"Graph not found: $graphName"}
            return -1
        }
    }

    fun createVerticesTable(id: Int){
        connection.createStatement().also { statement ->
            try {
                statement.execute("CREATE TABLE IF NOT EXISTS vertices" +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT,vertex INTEGER, x FLOAT, y FLOAT," +
                        "graphid INTEGER,FOREIGN KEY (graphid) REFERENCES graphs(id) ON DELETE CASCADE);")
            }catch (ex: Exception){
                logger.error(ex){"Vertices create failed"}
            }
        }
    }
    fun createEdgesTable(id:Int){
        connection.createStatement().also { statement ->
            try {
                statement.execute("CREATE TABLE IF NOT EXISTS edges(id INTEGER AUTOINCREMENT,fromVertex INTEGER, toVertex  INTEGER," +
                        "graphid INTEGER,FOREIGN KEY (graphid) REFERENCES graphs(id) ON DELETE CASCADE);")
            }catch (ex: Exception){
                logger.error(ex){"Edges create failed"}
            }
        }
    }
    fun addVertex(tableId: Int, vertexID: Int,x: Float, y: Float){
        connection.createStatement().also { statement ->
            try {
                statement.execute("INSERT INTO vertices(id,x,y,graphid) VALUES (${vertexID},$x,$y,${tableId})")
            }catch (ex: Exception){
                logger.error(ex){"Cannot add vertex"}
            }
        }
    }
    fun addEdge(tableId: Int, fromID: Int, toID: Int){
        connection.createStatement().also { statement ->
            try{
                statement.execute("INSERT INTO edges(fromID,toID,graphid) VALUES (${fromID},${toID},${tableId})")
            }catch (ex: Exception){
                logger.error(ex){"Cannot add edge"}
            }
        }
    }




    override fun close() {
        connection.close()
    }
}