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
        fun createEmptyTestSimulation() = Simulation(configuration = SimulationConfiguration(
            name = "test",
            environment = Environment()
        ))

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

        /**
         * @return created simulation
         */
        fun createTestLaunchSimulation() = Simulation(configuration = SimulationConfiguration(
            name = "test",
            environment = Environment(),
            agentsInitialConfig = mutableListOf(
                listOf(TestInit.BehaviourOne::class.java, TestInit.BehaviourTwo::class.java),
                listOf(TestInit.BehaviourOne::class.java)
            )
        ))
    }

    @Test
    fun testSaveAndRecreateEmptySimulation(){
        val simulation = createEmptyTestSimulation()

        assertDoesNotThrow{
            val exportedConfiguration = simulation.exportConfig()
            val createdSimulation = Simulation.createFromConfiguration(configuration = exportedConfiguration)

            assertEquals(simulation.configuration.name,createdSimulation.configuration.name)
            assertEquals(simulation.configuration.environment.name,createdSimulation.configuration.environment.name)
            assertEquals(simulation.configuration.agentsInitialConfig.size,createdSimulation.configuration.agentsInitialConfig.size)
        }
    }

    @Test
    fun testSaveAndRecreate(){
        val simulation = createTestSimulation()

        assertDoesNotThrow{
            val exportedConfiguration = simulation.exportConfig()
            val createdSimulation = Simulation.createFromConfiguration(configuration = exportedConfiguration)

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

    @Test
    fun testSimulation(){
        val simulation = createTestLaunchSimulation()

        val execution = Thread(simulation)

        execution.start()
        execution.join()
    }
}