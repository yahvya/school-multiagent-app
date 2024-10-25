package yahvya.implementation.graphical.controllers


/**
 * @brief test controller
 */
class TestController : ApplicationController(){
    override fun storeCurrentVersionOnSwitch(): Boolean = true

    override fun showAfter(): Boolean = true

    companion object{
        fun getFxmlPath():String = "/test.fxml"
    }
}