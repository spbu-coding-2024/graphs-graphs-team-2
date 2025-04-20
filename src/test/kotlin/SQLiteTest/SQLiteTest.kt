package SQLiteTest

import inpout.Graphs
import inpout.SQLiteEXP
import inpout.edgeInfo
import inpout.vertexInfo
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.random.Random
import kotlin.test.assertEquals

class SQLiteTest {


    private val connection=SQLiteEXP("test.db")
    @Test
    fun test_addGraph(){
        val m = connection.addGraph("graph1",false,false)
        assert(m>0)
    }

    @Test
    fun test_addGraphTwice(){
        val c1 = connection.addGraph("graph2",false,false)
        assert(c1>0)
        val c2 = connection.addGraph("graph2",false,false)
        assert(c2==-1)
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
        val edges1= listOf<edgeInfo>()
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun cleanUp(): Unit {
            File("test.db").delete()
        }
    }
}