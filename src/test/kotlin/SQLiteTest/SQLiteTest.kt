package SQLiteTest

import io.SQLiteExposed.Edges
import io.SQLiteExposed.Graphs
import io.SQLiteExposed.SQLiteEXP
import io.SQLiteExposed.Vertices
import io.SQLiteExposed.edgeInfo
import io.SQLiteExposed.vertexInfo
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.random.Random
import kotlin.test.assertEquals

class SQLiteTest {


    private val connection=SQLiteEXP("test.db")
    @AfterEach
    fun tearDown() {
        transaction {
            SchemaUtils.drop(Graphs, Edges, Vertices)
        }
    }
    @Test
    fun test_addGraph(){
        val m = connection.addGraph("graph1",false,false)
        assert(m>0)
    }

    @Test
    fun test_addGraphTwice(){
        val c1 = connection.addGraph("graph2",false,false)
        assert(c1>0)
        val exception=assertThrows<ExposedSQLException> {
            connection.addGraph("graph2",false,false)
        }

        assert(exception.message?.contains("A UNIQUE constraint failed") ?: false)
    }

    @Test
    fun testGraphFinding(){
        val c1 = connection.addGraph("graph3",false,false)
        val gi = connection.findGraph("graph3")
        assertEquals(c1,gi?.id)
        assertEquals(false, gi?.isWeighted)
        assertEquals(false, gi?.isWeighted)
    }

    @Test
    fun deleteAllGraphs(){
        val graphArray = arrayOf("graph4","graph5","graph6","graph7","graph8")
        graphArray.forEach {
            connection.addGraph(it,false,false)
        }
        connection.deleteAll()
        val p = transaction {
            Graphs.selectAll().empty()
        }
        assert(p)
    }

    @Test
    fun testGraphList(){
        connection.deleteAll()
        val graphArray = arrayOf("graph9","graph10","graph11","graph12","graph13")
        graphArray.forEach {
            connection.addGraph(it,false,false)
        }
        val grNames=connection.makeListFromNames()
        assertEquals(graphArray.size, grNames.size)
        graphArray.forEach {
            assert(grNames.contains(it))
        }
    }

    @Test
    fun testAddAndGetVertices(){
        val c1 = connection.addGraph("graph14",false,false)
        val vertices1= Array<vertexInfo>(3,{vertexInfo(Random.nextLong(),Random.nextFloat(),Random.nextFloat(),"52")})
        vertices1.forEach {
            connection.addVertex(c1,it.vert,it.x,it.y,it.label)
        }
        val vert2=connection.findVertices(c1)
        assertEquals(vertices1.size, vert2.size)
        vertices1.forEach {
            assert(vert2.contains(it))
        }
    }

    @Test
    fun testAddandGetEdges(){
        val c1 = connection.addGraph("graph15",false,false)
        val vertices1= Array<vertexInfo>(3,{vertexInfo(Random.nextLong(),Random.nextFloat(),Random.nextFloat(),"52")})
        vertices1.forEach {
            connection.addVertex(c1,it.vert,it.x,it.y,it.label)
        }
        val edges1= mutableListOf<edgeInfo>()
        for(i in 0..2){
            edges1.add(edgeInfo(vertices1[i].vert,vertices1[(i+1)%3].vert,Random.nextFloat(), i.toLong(),i.toString()))
        }
        edges1.forEach {
            connection.addEdge(c1,it.vertexFrom,it.vertexTo,it.weight,it.id,it.label)
        }
        val edges2=connection.findEdges(c1)
        assertEquals(edges1.size, edges2.size)
        edges1.forEach {
            assert(edges2.contains(it))
        }
    }

    @Test
    fun addVertexTwice(){
        val c1 = connection.addGraph("graph16",false,false)
        val vertex=vertexInfo(1L,1f,1f,"0")
        connection.addVertex(c1,vertex.vert,vertex.x,vertex.y,vertex.label)
        val exception=assertThrows<ExposedSQLException> {
            connection.addVertex(c1,vertex.vert,vertex.x,vertex.y,vertex.label)
        }
        assert(exception.message?.contains("A UNIQUE constraint failed") ?: false)
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun cleanUp(): Unit {
            File("test.db").delete()
        }
    }
}