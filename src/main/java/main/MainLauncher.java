package main;

import javafx.application.Application;

/**
 * Classe Launcher pour JavaFX.
 * Indispensable pour lancer une application JavaFX avec Java 11+ sans modules.
 */
public class MainLauncher {
    public static void main(String[] args) {
        Application.launch(MainFx.class, args);
    }
}
