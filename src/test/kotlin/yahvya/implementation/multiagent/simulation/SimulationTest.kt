package yahvya.implementation.multiagent.simulation

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import yahvya.implementation.multiagent.TestInit
import yahvya.implementation.multiagent.environment.Environment

/**
 * @brief simulation test
 */
class SimulationTest {
    companion object{
        @BeforeAll
        @JvmStatic
        fun setup() = TestInit.initForTest()

        /**
         * @return created empty simulation
         */
        fun createEmptyTestSimulation() = Simulation(
            name = "test",
            environment = Environment()
        )

        /**
         * @return created simulation
         */
        fun createTestSimulation() = Simulation(
            name = "test",
            environment = Environment(),
            agentsInitialConfig = mutableListOf(
                listOf(TestInit.BehaviourOne::class.java, TestInit.BehaviourTwo::class.java),
                listOf(TestInit.BehaviourOne::class.java),
                listOf(),
            )
        )
    }

    @Test
    fun testSaveAndRecreateEmptySimulation(){
        val simulation = createEmptyTestSimulation()

        assertDoesNotThrow{
            val exportedConfiguration = simulation.exportConfig()
            val createdSimulation = Simulation.createFromConfiguration(configuration = exportedConfiguration)

            assertEquals(simulation.name,createdSimulation.name)
            assertEquals(simulation.environment.name,createdSimulation.environment.name)
            assertEquals(simulation.agentsInitialConfig.size,createdSimulation.agentsInitialConfig.size)
        }
    }

    @Test
    fun testSaveAndRecreate(){
        val simulation = createTestSimulation()

        assertDoesNotThrow{
            val exportedConfiguration = simulation.exportConfig()
            val createdSimulation = Simulation.createFromConfiguration(configuration = exportedConfiguration)

            assertEquals(simulation.name,createdSimulation.name)
            assertEquals(simulation.environment.name,createdSimulation.environment.name)
            assertEquals(simulation.agentsInitialConfig.size,createdSimulation.agentsInitialConfig.size)

           simulation.agentsInitialConfig.forEachIndexed { key, agentInitialConfig ->
              val createdSimulationAgentInitialConfig = createdSimulation.agentsInitialConfig[key]

               agentInitialConfig.forEachIndexed { innerKey, behaviourClass ->
                   assertEquals(behaviourClass.canonicalName,createdSimulationAgentInitialConfig[innerKey].canonicalName)
               }
           }
        }
    }
}