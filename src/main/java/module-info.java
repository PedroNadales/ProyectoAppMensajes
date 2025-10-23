module es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes {
    // Java modules
    requires java.xml;
    requires java.desktop;
    
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.swing;

    // Third-party modules
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    
    // Jakarta EE modules
    requires jakarta.activation;
    requires jakarta.xml.bind;
    requires org.glassfish.jaxb.runtime;

    // Open packages for JavaFX and JAXB
    opens es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes to 
        javafx.fxml, jakarta.xml.bind, org.glassfish.jaxb.runtime;
        
    opens es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model to 
        javafx.base, jakarta.xml.bind, org.glassfish.jaxb.runtime;
        
    opens es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers to 
        javafx.fxml;
        
    // Open packages for reflection
    opens es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils to
        org.glassfish.jaxb.core, org.glassfish.jaxb.runtime, jakarta.xml.bind;
    
    // Export packages
    exports es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes;
    exports es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers;
    exports es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model to 
        org.glassfish.jaxb.core, org.glassfish.jaxb.runtime, jakarta.xml.bind;
        
    exports es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils to
        org.glassfish.jaxb.core, org.glassfish.jaxb.runtime, jakarta.xml.bind;
    
    // Required for JavaFX
    uses javafx.application.Application;
    provides javafx.application.Application with 
        es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.MainApplication;
}