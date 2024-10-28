package yahvya.implementation.graphical.navigation

import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.PathsConfig

/**
 * @brief application pages navigation manager
 */
open class NavigationManager(
    /**
     * @brief application main stage
     */
    var mainStage: Stage,
    /**
     * @brief application interface configurator
     */
    val interfaceConfigurator: InterfaceConfigurator
){
    /**
     * @brief pages storage
     */
    protected val pagesStore: HashMap<String, SceneDatas> = HashMap()

    /**
     * @brief current controller
     */
    protected var currentController: AController? = null

    /**
     * @brief switch page
     * @param fxmlPath path of the fxml file after the 'PathConfig.pagesBasePath'
     * @param datas datas to provide to the controller or null if no datas
     * @return success state
     * @attention step 1 : find the controller - step 2 : send data to the controller - step 3 : configure the interface - step 4 : if required, show the stage
     */
    fun switchOnController(fxmlPath: String,datas: Any? = null):Boolean{
        ApplicationConfig.LOGGER.info("Switching controller to $fxmlPath")

        val fxmlFinalPath = if(fxmlPath.startsWith("/")) fxmlPath else "/$fxmlPath"

        this.currentController?.beforeSwitch()
        this.mainStage.close()
        this.mainStage = Stage()

        try{
            // load the scene datas
            lateinit var sceneDatas: SceneDatas
            var isAPreviousOne = false

            if(!this.pagesStore.containsKey(fxmlFinalPath)){
                val fxmlLoader = FXMLLoader(ApplicationConfig.ROOT_CLASS.getResource("${PathsConfig.PAGES_DIRECTORY_PATH}$fxmlFinalPath"))

                sceneDatas = SceneDatas(
                    fxmlLoader= fxmlLoader,
                    loadedScene= Scene(fxmlLoader.load())
                )
            }
            else{
                sceneDatas = this.pagesStore[fxmlFinalPath]!!
                isAPreviousOne = true
            }

            // load and call controller configuration methods

            val sceneController: AController = sceneDatas.fxmlLoader.getController()
            this.currentController = sceneController

            sceneController.receiveDatas(datas= datas)

            if(sceneController.storeCurrentVersionOnSwitch())
                this.pagesStore[fxmlFinalPath] = sceneDatas
            else
                this.pagesStore.remove(fxmlFinalPath)

            // add stylesheets if new scene

            if(!isAPreviousOne){
                sceneController.getStylesheets().forEach {stylesheet ->
                    try{
                        val stylesheetCompletePath = ApplicationConfig.ROOT_CLASS.getResource("${PathsConfig.STYLESHEETS_DIRECTORY_PATH}/$stylesheet")
                            ?.toExternalForm()

                        sceneDatas.loadedScene.stylesheets.add(stylesheetCompletePath)
                    }
                    catch (e:Exception){
                        ApplicationConfig.LOGGER.error("Fail to load stylesheet due to <${e.message}>")
                    }
                }
            }

            // configure the interface and show the controller

            this.mainStage.scene = sceneDatas.loadedScene
            this.mainStage.title = "${ApplicationConfig.APPLICATION_NAME} | ${sceneController.getPageName()}"
            this.interfaceConfigurator.addGlobalStylesheets(stage= this.mainStage)
            this.interfaceConfigurator.configureInterface(stage= this.mainStage)

            if(sceneController.showAfter() && !this.mainStage.isShowing)
                this.mainStage.show()

            ApplicationConfig.LOGGER.info("Switched to $fxmlPath")

            return true
        }
        catch(e: Exception){
            ApplicationConfig.LOGGER.error("Fail to switch to the controller due to <${e.message}>")
            return false
        }
    }

    /**
     * @brief perform an action on the stage
     * @param action the action
     */
    fun performStageAction(action: () -> Unit) = Platform.runLater(action)

    /**
     * @brief scene stored datas
     */
    protected data class SceneDatas(
        /**
         * @brief linked fxml loader
         */
        val fxmlLoader: FXMLLoader,
        /**
         * @brief loaded parent
         */
        val loadedScene: Scene
    )
}