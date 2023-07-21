package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemDetailController extends Controller {

    @FXML
    public VBox VBoxItemDetail;
    @FXML
    public Label cost;
    @FXML
    public Label name;
    @FXML
    public ImageView itemImage;
    @FXML
    public Button shopBtn;
    @FXML
    public Label desc;
    @FXML
    public ImageView coinImg;
    @Inject
    PresetsService presetsService;
    @Inject
    DataCache cache;
    private ItemTypeDto itemType;
    private boolean isShop;

    public void setItem(ItemTypeDto itemType) {
        this.itemType = itemType;
    }

    public void setBooleanShop(boolean isShop) {
        this.isShop = isShop;
    }

    @Inject
    public ItemDetailController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        coinImg.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/coin.png"))));
        if (isShop) {
            shopBtn.setText(resources.getString("buy"));
            cost.setText(resources.getString("val") + ": " + itemType.price());
        } else {
            shopBtn.setText(resources.getString("sell"));
            if (itemType.price() == 0) {
                cost.setText(resources.getString("val") + ": " + itemType.price());
                shopBtn.setOpacity(0);
                shopBtn.setDisable(true);
            } else {
                cost.setText(resources.getString("val") + ": " + (int) (itemType.price() * 0.5));
            }
        }
        Map<Integer, Image> itemImages = new HashMap<>();
        if (cache.getItemImages().containsKey(itemType.id())) {
            name.setText(itemType.name());
            desc.setText(formatStringIfTooLong(itemType.description()));
            itemImage.setImage(cache.getItemImages().get(itemType.id()));
        } else {
            name.setText(itemType.name());
            desc.setText(formatStringIfTooLong(itemType.description()));
            disposables.add(presetsService.getItemImage(itemType.id())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(img -> {
                        itemImage.setImage(img);
                        itemImages.put(itemType.id(), img);
                        cache.setItemImages(itemImages);
                    }));
        }

        return parent;
    }

    public String formatStringIfTooLong(String itemName) {
        if (itemName.length() > 25) {
            int lastSpace = itemName.lastIndexOf(' ', 25);
            if (lastSpace != -1) { // \n after last space
                if (itemName.length() > 50) {
                    String lastPart = itemName.substring(lastSpace + 1);
                    return itemName.substring(0, lastSpace) + "\n" + formatStringIfTooLong(lastPart);
                }
                return itemName.substring(0, lastSpace) + "\n" + formatStringIfTooLong(itemName.substring(lastSpace + 1));
            } else { // if too long and no space
                if (itemName.length() > 50) {
                    String lastPart = itemName.substring(25);
                    return itemName.substring(0, 25) + "-\n" + formatStringIfTooLong(lastPart);
                }
                return itemName.substring(0, 25) + "-\n" + itemName.substring(25);
            }
        }
        return itemName;
    }
    @SuppressWarnings("unused")
    @FXML
    public void shopFunction(ActionEvent actionEvent) {
    }
}
