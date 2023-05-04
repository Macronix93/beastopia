package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngameController extends Controller {

    @FXML
    public HBox ingame;
    @FXML
    private Button pause;
    @Inject
    App app;
    @Inject
    Provider<MenuController> menuControllerProvider;

    @Inject
    public IngameController() {
    }

    public void pauseMenu() {
        //ToDo Show Pause Menu
    }

    @Override
    public String getTitle() {
        return "Beastopia";
    }

    public void escape(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            app.show(menuControllerProvider.get());
        }
    }
}
