package yahvya.implementation.configurations

/**
 * @brief application path configuration
 */
data object PathsConfig{
    /**
     * @brief controllers fxml pages directory from the source
     */
    const val PAGES_DIRECTORY_PATH:String = "pages"

    /**
     * @brief application "favicon" path
     */
    const val APPLICATION_ICON_PATH:String = "icons/icon.png"

    /**
     * @brief stylesheets directory path
     */
    const val STYLESHEETS_DIRECTORY_PATH:String = "stylesheets"

    /**
     * @brief plugins jar directory path
     */
    const val PLUGINS_DIRECTORY_PATH:String = "plugins"
}