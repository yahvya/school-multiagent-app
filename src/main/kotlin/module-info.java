module yahvya.implementation {
    requires javafx.controls;
    requires kotlin.stdlib;
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

    opens yahvya.implementation.multiagent.agent;
    opens yahvya.implementation.graphical.controllers to javafx.fxml;

    exports yahvya.implementation;
}