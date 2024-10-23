module yahvya.implementation {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires io.github.microutils.kotlinlogging;
    requires ch.qos.logback.core;

    opens yahvya.implementation.multiagent.agent;

    exports yahvya.implementation;
}