package yahvya.implementation.multiagent.environment

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import yahvya.implementation.multiagent.TestInit

/**
 * @brief environment cell test
 */
class EnvironmentCellTest {
    companion object{
        @BeforeAll
        @JvmStatic
        fun setupTest() = TestInit.initForTest()

        /**
         * @return the test cell
         */
        fun createTestCell(): TestInit.TestCell = TestInit.TestCell()
    }

    @Test
    fun testSaveAndRecreate(){
        val cell: TestInit.TestCell = createTestCell()

        assertDoesNotThrow{
            val exportConfig = cell.exportConfig()
            val createdCell = EnvironmentCell.createFromConfiguration(exportConfig)

            assertTrue(createdCell is TestInit.TestCell)
            assertEquals(createdCell.getDisplayName(),"Test")
        }
    }
}