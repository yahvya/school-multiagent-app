package yahvya.implementation.multiagent.simulation

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import yahvya.implementation.multiagent.TestInit
import yahvya.implementation.multiagent.environment.Environment
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * @brief simulation storage test
 */
class SimulationStorageTest {
    companion object{
        @BeforeAll
        @JvmStatic
        fun setup() = TestInit.initForTest()

        /**
         * @return created simulation
         */
        fun createTestSimulation() = Simulation(configuration = SimulationConfiguration(
            name = "test",
            environment = Environment(),
            agentsInitialConfig = mutableListOf(
                listOf(TestInit.BehaviourOne::class.java, TestInit.BehaviourTwo::class.java),
                listOf(TestInit.BehaviourOne::class.java),
                listOf(),
            )
        ))
    }

    @Test
    fun testStoreAndLoad(){
        val simulation = createTestSimulation()

        assertDoesNotThrow{
            ByteArrayOutputStream().use{ outputStream ->
                assertEquals(outputStream.size(),0)
                assertTrue(SimulationStorage.storeIn(simulation = simulation, outputStream = outputStream))
                assertTrue(outputStream.size() > 0)

                ByteArrayInputStream(outputStream.toByteArray()).use{ inputStream ->
                    val createdSimulation = SimulationStorage.loadFrom(inputStream = inputStream)

                    assertEquals(simulation.configuration.name,createdSimulation.configuration.name)
                    assertEquals(simulation.configuration.environment.name,createdSimulation.configuration.environment.name)
                    assertEquals(simulation.configuration.agentsInitialConfig.size,createdSimulation.configuration.agentsInitialConfig.size)

                    simulation.configuration.agentsInitialConfig.forEachIndexed { key, agentInitialConfig ->
                        val createdSimulationAgentInitialConfig = createdSimulation.configuration.agentsInitialConfig[key]

                        agentInitialConfig.forEachIndexed { innerKey, behaviourClass ->
                            assertEquals(behaviourClass.canonicalName,createdSimulationAgentInitialConfig[innerKey].canonicalName)
                        }
                    }
                }
            }
        }
    }
}