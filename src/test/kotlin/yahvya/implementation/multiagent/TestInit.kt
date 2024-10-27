package yahvya.implementation.multiagent

import javafx.application.Application
import yahvya.implementation.MultiAgentApplication
import yahvya.implementation.multiagent.agent.AppAgentBehaviour
import yahvya.implementation.multiagent.environment.EnvironmentCell
import yahvya.implementation.multiagent.definitions.Box

object TestInit {
    fun initForTest(){
        Application.launch(MultiAgentApplication::class.java)

        EnvironmentCell.loadAvailableCellsPlugins()
        Box.loadAvailableBoxPlugins()
        AppAgentBehaviour.loadAvailableAgentBehavioursPlugins()

        EnvironmentCell.AVAILABLE_CELLS_CLASSES.apply{
            add(TestCell::class.java)
            add(CellOne::class.java)
            add(CellTwo::class.java)
        }
        Box.AVAILABLE_BOX_CLASSES.add(TestBox::class.java)
        AppAgentBehaviour.AVAILABLE_AGENT_BEHAVIOURS_CLASSES.apply{
            add(BehaviourOne::class.java)
            add(BehaviourTwo::class.java)
        }
    }

    // test objects

    class BehaviourOne : AppAgentBehaviour(){
        override fun getDisplayName(): String = "behaviour one"

        override fun buildConfiguration(): Map<*, *> = mapOf<String,Any>()

        override fun action() {
            this.myAgent.doDelete()
        }

        override fun done(): Boolean = true

        override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean = true

        override fun getToConfigure(): List<String> = listOf()
    }

    class BehaviourTwo : AppAgentBehaviour(){
        override fun getDisplayName(): String = "behaviour two"

        override fun buildConfiguration(): Map<*, *> = mapOf<String,Any>()

        override fun action() {
            this.myAgent.doDelete()
        }

        override fun done(): Boolean = true

        override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean = true

        override fun getToConfigure(): List<String> = listOf()
    }

    class TestCell : EnvironmentCell{
        constructor(){
            this.box = TestBox().apply{
                name = "Bonjour"
            }
        }

        override fun getDisplayName(): String = "Test"

        override fun buildExportConfiguration(): Map<*, *> = mapOf<String,Any>()

        override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean = true

        override fun getToConfigure(): List<String> = listOf()
    }

    class TestBox : Box(){
        var name: String = "default name"

        override fun collideWith(x: Double, y: Double): Boolean = true
        
        override fun getDisplayName(): String = "test"

        override fun buildExportConfiguration(): Map<*, *> = mapOf<String,Any>(
            "name" to this.name
        )

        override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean{
            this.name = configuration["name"] as String
            return true
        }

        override fun getToConfigure(): List<String> = listOf()
    }

    class CellOne : EnvironmentCell{
        constructor(){
            this.box = TestBox().apply{
                name = "test name"
            }
        }

        override fun getDisplayName(): String = "Cell one"

        override fun buildExportConfiguration(): Map<*, *> = mapOf<String,Any>()

        override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean = true

        override fun getToConfigure(): List<String> = listOf()
    }

    class CellTwo : EnvironmentCell{
        constructor(){
            this.box = TestBox().apply{
                name = "test name"
            }
        }

        override fun getDisplayName(): String = "Cell two"

        override fun buildExportConfiguration(): Map<*, *> = mapOf<String,Any>()

        override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean = true

        override fun getToConfigure(): List<String> = listOf()
    }
}