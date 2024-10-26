package yahvya.implementation.graphical.components

import javafx.scene.Node

/**
 * @brief describe a component builder
 */
interface ComponentBuilder {
    /**
     * @return the built node
     */
    fun build(): Node
}