package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class MenuController extends Controller {
    private final List<Controller> subControllers = new ArrayList<>();
    @Inject
    Provider<RegionController> regionControllerProvider;
    @Inject
    Provider<FriendListController> friendListControllerProvider;
    @FXML
    private VBox friendListContainer;
    @FXML
    private VBox regionContainer;

    @Inject
    public MenuController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        Controller friendListController = friendListControllerProvider.get();
        subControllers.add(friendListController);
        friendListContainer.getChildren().add(friendListController.render());
        Controller regionController = regionControllerProvider.get();
        subControllers.add(regionController);
        regionContainer.getChildren().add(regionController.render());
        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    @Override
    public String getTitle() {
        return "Beastopia - Main Menu";
    }

    public void logout() {

    }
}
