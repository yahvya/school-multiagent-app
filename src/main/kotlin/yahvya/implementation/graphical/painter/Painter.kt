package yahvya.implementation.graphical.painter

import javafx.scene.canvas.Canvas

/**
 * @brief painter element
 */
interface Painter{
    /**
     * @brief apply a action on canvas
     * @param toApply action to do
     */
    fun applyOnCanvas(toApply: (canvas:Canvas) -> Unit)
}