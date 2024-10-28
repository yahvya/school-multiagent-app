package yahvya.implementation.graphical.painter

import javafx.scene.layout.AnchorPane

/**
 * @brief painter element
 */
interface Painter{
    /**
     * @brief apply action on canvas
     * @param toApply action to do
     */
    fun applyOnCanvas(toApply: (drawZone:AnchorPane) -> Unit)
}