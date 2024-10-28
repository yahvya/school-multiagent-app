package yahvya.implementation.graphical.controllers

import yahvya.implementation.graphical.navigation.AController
import yahvya.implementation.graphical.navigation.NavigationManager

/**
 * @brief application controller
 */
abstract class ApplicationController : AController {
    /**
     * @brief controller datas
     */
    var datas: Any? = null

    override fun receiveDatas(datas: Any?) {
        this.datas = datas

        this.performActions()
    }

    override fun getStylesheets(): List<String> = listOf()

    override fun afterLoadingScene() {}

    override fun beforeSwitch() {}

    /**
     * @brief called to perform action after receiving datas
     * @throws Nothing
     */
    open fun performActions(){}
}