package yahvya.implementation.multiagent.environment

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import yahvya.implementation.multiagent.TestInit

/**
 * @brief environment test
 */
class EnvironmentTest {
    companion object{
        @BeforeAll
        @JvmStatic
        fun setup() = TestInit.initForTest()

        /**
         * @brief create the test environment
         */
        fun createTestEnvironment(): Environment = Environment().apply{
            name = "test"
            cells.apply{
                add(TestInit.CellOne())
                add(TestInit.CellOne())
                add(TestInit.CellTwo())
            }
        }
    }

    @Test
    fun testSaveAndRecreate(){
        val environment = createTestEnvironment()

        assertDoesNotThrow{
            val exportConfig = environment.exportConfig()
            val createdEnvironment = Environment.createFromConfiguration(configuration = exportConfig)

            assertEquals(createdEnvironment.name,"test")
            assertEquals(createdEnvironment.cells.size,environment.cells.size)
        }
    }

    @Test
    fun testCellFilter(){
        val environment = createTestEnvironment()

        assertEquals(environment.getCellsOfType<TestInit.CellOne>().size,2)
        assertEquals(environment.getCellsOfType<TestInit.CellTwo>().size,1)
        assertEquals(environment.getCellsOfType<TestInit.TestCell>().size,0)
        assertEquals(environment.getCellsOfType<TestInit.CellOne>()[0],environment.cells[0])
        assertEquals(environment.getCellsOfType<TestInit.CellOne>()[1],environment.cells[1])
        assertEquals(environment.getCellsOfType<TestInit.CellTwo>()[0],environment.cells[2])
    }


}