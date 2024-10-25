package yahvya.implementation.graphical.controllers

import yahvya.implementation.graphical.navigation.AController
import yahvya.implementation.graphical.navigation.NavigationManager

/**
 * @brief application controller
 */
abstract class ApplicationController : AController {
    /**
     * @brief linked navigation manager
     */
    lateinit var navigationManager: NavigationManager

    /**
     * @brief controller datas
     */
    var datas: Any? = null

    override fun receiveDatas(datas: Any?, navigationManager: NavigationManager) {
        this.navigationManager = navigationManager
        this.datas = datas

        this.performActions()
    }

    override fun getStylesheets(): List<String> = listOf()

    override fun afterLoadingScene() {}

    /**
     * @brief called to perform action after receiving datas
     * @throws Nothing
     */
    open fun performActions(){}
}