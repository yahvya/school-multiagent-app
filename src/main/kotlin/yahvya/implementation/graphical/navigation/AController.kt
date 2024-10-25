package yahvya.implementation.graphical.navigation

/**
 * @brief mark an app controller
 */
interface AController {
    /**
     * @brief called method to receive the datas on the controller creation and configure the controller
     * @param datas received datas or null if no datas provided
     * @param navigationManager current navigation manager
     * @throws Nothing
     */
    fun receiveDatas(datas: Any?,navigationManager: NavigationManager)

    /**
     * @brief called when the scene is updated
     * @throws Nothing
     */
    fun afterLoadingScene()

    /**
     * @return if the navigation manager have to store the current page when the switch method is called
     * @throws Nothing
     */
    fun storeCurrentVersionOnSwitch(): Boolean

    /**
     * @return have to show the stage after
     * @throws Nothing
     */
    fun showAfter(): Boolean

    /**
     * @return page name
     */
    fun getPageName(): String

    /**
     * @return the list of stylesheets to load for this controller
     * @throws Nothing
     */
    fun getStylesheets():List<String>
}