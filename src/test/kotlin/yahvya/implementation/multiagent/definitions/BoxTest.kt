package yahvya.implementation.multiagent.definitions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import yahvya.implementation.multiagent.TestInit

/**
 * @brief box test
 */
class BoxTest {
    companion object{
        @BeforeAll
        @JvmStatic
        fun setupTest() = TestInit.initForTest()

        /**
         * @return test box
         */
        fun createTestBox(): TestInit.TestBox = TestInit.TestBox().apply{
            name = "test"
        }
    }

    @Test
    fun testSaveAndRecreate(){
        val box: TestInit.TestBox = createTestBox()

        assertDoesNotThrow{
            val config = box.exportConfig()
            val createdBox = Box.createFromConfiguration(configuration= config)

            assertTrue(createdBox is TestInit.TestBox)
            assertEquals((createdBox as TestInit.TestBox).name,box.name)
        }
    }
}