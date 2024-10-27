package yahvya.implementation.graphical.components

import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.util.StringConverter
import yahvya.implementation.multiagent.agent.AppAgentBehaviour
import yahvya.implementation.multiagent.definitions.Box

/**
 * @brief app agent configuration component
 */
open class AppAgentConfigurationComponent(
    /**
     * @brief allow to print errors
     */
    var errorPrinter: (errorMessage:String) -> Unit,
    /**
     * @brief agent creation handler
     */
    var creationHandler: (
        agentDefaultBehavioursConfigurations: MutableList<Map<*,*>>,
        boxConfiguration: Map<String,String>,
        boxInstance: Box,
        countOfAgent: Int
    ) -> Unit
) : ComponentBuilder {
    /**
     * @brief behaviours configurations
     */
    val chosenBehavioursConfigurations: MutableList<Map<*,*>> = mutableListOf()

    /**
     * @brief behaviour configuration zone
     */
    protected val behaviourConfigurationZone: VBox = VBox().apply{
        spacing = 20.0
    }

    /**
     * @brief count of agent field
     */
    protected val countOfAgentField = TextField()

    /**
     * @brief linked box component
     */
    protected val agentBoxComponent = BoxConfigurationComponent()

    override fun build(): Node {
        val container = VBox()

        container.spacing = 15.0
        container.children.apply {
            countOfAgentField.promptText = "Nombre d'agent à créer"
            countOfAgentField.tooltip = Tooltip("Définir le nombre d'agent sur ce modèle à créer")

            val headerContainer = HBox()
            headerContainer.spacing = 20.0
            headerContainer.children.apply {
                val agentCreationButton = Button("Valider l'agent").apply{
                    cursor = Cursor.HAND
                    setOnMouseClicked {validateCreation()}
                }

                HBox.setHgrow(countOfAgentField, Priority.ALWAYS)
                HBox.setHgrow(agentCreationButton, Priority.ALWAYS)

                addAll(
                    countOfAgentField,
                    agentCreationButton
                )
            }

            HBox.setHgrow(headerContainer, Priority.ALWAYS)

            addAll(
                headerContainer,
                agentBoxComponent.build()
            )

            val behavioursChoiceBox = ChoiceBox<AppAgentBehaviour>().apply{
                tooltip = Tooltip("Comportements de l'agent")
                maxWidth = Double.MAX_VALUE
                converter = object : StringConverter<AppAgentBehaviour>() {
                    override fun toString(p0: AppAgentBehaviour?): String = if(p0 === null) "Choisir le comportement" else p0.getDisplayName()
                    override fun fromString(p0: String?): AppAgentBehaviour = throw Exception("Appel inattendu")
                }

                AppAgentBehaviour.AVAILABLE_AGENT_BEHAVIOURS_CLASSES.map{ behaviourClass ->
                    items.add(behaviourClass.getConstructor().newInstance())
                }

                selectionModel.selectedItemProperty().addListener { _, _, newValue ->
                    if(newValue === null)
                        return@addListener

                    showBehaviourConfiguration(behaviourInstance = newValue)
                    selectionModel.clearSelection()
                }
            }

            addAll(
                behavioursChoiceBox,
                behaviourConfigurationZone
            )
        }

        return container
    }

    /**
     * @brief show the behaviour configuration zone
     */
    protected fun showBehaviourConfiguration(behaviourInstance: AppAgentBehaviour){
        this.behaviourConfigurationZone.children.apply{
            clear()
            add(Label(behaviourInstance.getDisplayName()))
            val behaviourConfiguration:MutableMap<String,String> = mutableMapOf()

            // require behaviour configurations
            behaviourInstance.getToConfigure().forEach { configureKey ->
                val textfield = TextField()

                textfield.promptText = configureKey
                textfield.tooltip = Tooltip(configureKey)
                textfield.textProperty().addListener { _, _, newValue ->
                    behaviourConfiguration[configureKey] = newValue
                }

                add(textfield)
            }

            val creationConfirmationButton = Button("Ajouter le comportement").apply{
                cursor = Cursor.HAND
                setOnMouseClicked {
                    // add a behaviour to the agent
                    if(!behaviourInstance.isConfigurationValid(configuration = behaviourConfiguration)){
                        errorPrinter.invoke("Veuillez configurer le comportement")
                        return@setOnMouseClicked
                    }

                    behaviourInstance.receiveConfiguration(configuration = behaviourConfiguration)
                    chosenBehavioursConfigurations.add(behaviourInstance.exportConfig())

                    clear()
                }
            }

            add(creationConfirmationButton)
        }
    }

    /**
     * @brief validate the agent creation
     */
    protected fun validateCreation(){
        val countOfAgentRegex = Regex("^[0-9]+$")

        if(!this.countOfAgentField.text.matches(countOfAgentRegex)){
            this.errorPrinter.invoke("Veuillez fournir le nombre d'agents à créer")
            return
        }

        if(!this.agentBoxComponent.isBoxConfigurationValid()){
            this.errorPrinter.invoke("Veuillez configurer la box de l'agent")
            return
        }

        this.creationHandler.invoke(
            chosenBehavioursConfigurations,
            this.agentBoxComponent.boxConfiguration,
            this.agentBoxComponent.currentBoxInstance,
            this.countOfAgentField.text.toInt()
        )
    }
}