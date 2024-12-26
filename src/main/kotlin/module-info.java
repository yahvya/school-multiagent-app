module yahvya.implementation {
    requires javafx.controls;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires io.github.microutils.kotlinlogging;
    requires ch.qos.logback.core;
    requires org.slf4j;
    requires com.google.gson;
    requires jade;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;
    requires java.desktop;
    requires kotlin.reflect;

    opens yahvya.implementation.graphical.controllers to javafx.fxml;

    exports yahvya.implementation.multiagent.simulation;
    exports yahvya.implementation.graphical.painter;
    exports yahvya.implementation.multiagent.environment;
    exports yahvya.implementation.multiagent.definitions;
    exports yahvya.implementation.multiagent.agent;
    exports yahvya.implementation;
}
