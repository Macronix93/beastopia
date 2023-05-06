package de.uniks.beastopia.teaml.controller;


import de.uniks.beastopia.teaml.rest.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.imageio.stream.FileImageInputStream;
import javax.inject.Inject;
import java.io.FileInputStream;

public class FriendController extends Controller {
    @FXML
    public ImageView friendAvatar;
    @FXML
    public Circle statusCircle;
    @FXML
    public Text name;
    @FXML
    public Button action;
    @FXML
    public Button chat;
    @FXML
    public Button pin;

    private User user;

    @Inject
    public FriendController() {

    }

    public FriendController setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        //TODO change avatar URL when avatar upload is implemented to individual link
        Image image = new Image("de/uniks/beastopia/teaml/assets/Lumnix_Logo_tr.png", 40.0,
                40.0, false, false);
        friendAvatar.setImage(image);

        name.setText(user.name());

        if (user.status().equals("online")) {
            statusCircle.setFill(Paint.valueOf("green"));
        } else {
            statusCircle.setFill(Paint.valueOf("red"));
        }

        return parent;
    }

    @FXML
    public void editFriendList(ActionEvent actionEvent) {
        //Icons for methods in assets\buttons
    }

    @FXML
    public void openFriendChat(ActionEvent actionEvent) {
    }

    @FXML
    public void pinFriend(ActionEvent actionEvent) {
    }
}