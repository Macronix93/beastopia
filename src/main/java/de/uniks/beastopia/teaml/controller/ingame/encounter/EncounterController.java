package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.rest.AbilityDto;
import de.uniks.beastopia.teaml.rest.AbilityMove;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.Opponent;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

@SuppressWarnings("unused")
public class EncounterController extends Controller {

    @FXML
    VBox attackBox1;
    @FXML
    VBox attackBox2;
    @FXML
    VBox attackBox3;
    @FXML
    VBox attackBox4;
    @FXML
    VBox enemyBeastInfo;
    @FXML
    Button leaveEncounter;
    @FXML
    Button changeMonster;
    @FXML
    VBox actionInfoBox;
    @FXML
    TextArea actionInfoText;
    @FXML
    VBox beastInfoBox;
    @FXML
    HBox enemyMonstersBox;
    @FXML
    HBox ownMonstersBox;
    @FXML
    Label attackNameLabel1;
    @FXML
    Label attackTypeLabel1;
    @FXML
    Label accLabel1;
    @FXML
    Label powerLabel1;
    @FXML
    Label attackNameLabel2;
    @FXML
    Label attackTypeLabel2;
    @FXML
    Label accLabel2;
    @FXML
    Label powerLabel2;
    @FXML
    Label attackNameLabel3;
    @FXML
    Label attackTypeLabel3;
    @FXML
    Label accLabel3;
    @FXML
    Label powerLabel3;
    @FXML
    Label attackNameLabel4;
    @FXML
    Label attackTypeLabel4;
    @FXML
    Label accLabel4;
    @FXML
    Label powerLabel4;

    @Inject
    Provider<EnemyBeastInfoController> enemyBeastInfoControllerProvider;
    @Inject
    Provider<BeastInfoController> beastInfoControllerProvider;
    @Inject
    Provider<RenderBeastController> renderBeastControllerProvider;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<ChangeBeastController> changeBeastControllerProvider;
    @Inject
    Provider<EndScreenController> endScreenControllerProvider;
    @Inject
    Provider<LevelUpController> levelUpControllerProvider;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    PresetsService presetsService;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    Prefs prefs;
    @Inject
    EventListener eventListener;

    //monster on the substitute's bench
    @SuppressWarnings({"FieldCanBeLocal"})
    private List<Monster> ownMonsters = new ArrayList<>();
    private final List<Monster> allyMonsters = new ArrayList<>();
    @SuppressWarnings({"FieldCanBeLocal"})
    private List<Monster> enemyMonsters = new ArrayList<>();

    private final List<Monster> enemyAllyMonsters = new ArrayList<>();

    //monsters in the fight
    private Monster myMonster;
    private Monster allyMonster;
    private Monster enemyMonster;
    private Monster enemyAllyMonster;

    @SuppressWarnings({"FieldCanBeLocal"})
    private String allyTrainer;
    private String enemyTrainer;
    @SuppressWarnings({"FieldCanBeLocal"})
    private String enemyAllyTrainer;

    private boolean shouldUpdateUIOnChange = false;

    private AbilityDto ability1;
    private AbilityDto ability2;
    private AbilityDto ability3;
    private AbilityDto ability4;
    EnemyBeastInfoController enemyBeastInfoController1;
    EnemyBeastInfoController enemyBeastInfoController2;
    RenderBeastController renderBeastController1;
    RenderBeastController renderBeastController2;
    BeastInfoController beastInfoController1;
    BeastInfoController beastInfoController2;

    @Inject
    public EncounterController() {
    }

    @Override
    public void onResize(int width, int height) {
        if (renderBeastController1 != null)
            renderBeastController1.onResize(width, height);
        if (renderBeastController2 != null)
            renderBeastController2.onResize(width, height);
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        beastInfoController1 = beastInfoControllerProvider.get().setMonster(myMonster);
        beastInfoBox.getChildren().addAll(beastInfoController1.render());
        renderBeastController1 = renderBeastControllerProvider.get().setMonster1(myMonster);
        Parent ownMonster = renderBeastController1.render();
        ownMonstersBox.getChildren().addAll(ownMonster);
        HBox.setHgrow(ownMonster, Priority.ALWAYS);

        // Set my monster opponent ID
        cache.getCurrentOpponents().stream()
                .filter(opponent -> opponent.monster().equals(beastInfoController1.getMonster()._id()))
                .findFirst()
                .ifPresent(opponent -> renderBeastController1.setMonsterOneOpponentId(opponent._id()));

        if (allyMonster != null) {
            beastInfoController2 = beastInfoControllerProvider.get().setMonster(allyMonster);
            beastInfoBox.getChildren().addAll(beastInfoController2.render());
            renderBeastController1.setMonster2(allyMonster);
            ownMonstersBox.getChildren().clear();
            Parent allyMonster = renderBeastController1.render();
            ownMonstersBox.getChildren().addAll(allyMonster);
            HBox.setHgrow(allyMonster, Priority.ALWAYS);

            // Set ally monster opponent ID
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> opponent.monster().equals(beastInfoController2.getMonster()._id()))
                    .findFirst()
                    .ifPresent(opponent -> renderBeastController1.setMonsterTwoOpponentId(opponent._id()));
        }

        enemyBeastInfoController1 = enemyBeastInfoControllerProvider.get().setMonster(enemyMonster);
        enemyBeastInfo.getChildren().addAll(enemyBeastInfoController1.render());
        renderBeastController2 = renderBeastControllerProvider.get().setMonster1(enemyMonster);
        Parent enemyMonster = renderBeastController2.render();
        enemyMonstersBox.getChildren().addAll(enemyMonster);
        HBox.setHgrow(enemyMonster, Priority.ALWAYS);

        // Set enemy monster opponent ID
        cache.getCurrentOpponents().stream()
                .filter(opponent -> opponent.monster().equals(enemyBeastInfoController1.getMonster()._id()))
                .findFirst()
                .ifPresent(opponent -> renderBeastController2.setMonsterOneOpponentId(opponent._id()));

        if (enemyAllyMonster != null) {
            enemyBeastInfoController2 = enemyBeastInfoControllerProvider.get().setMonster(enemyAllyMonster);
            enemyBeastInfo.getChildren().addAll(enemyBeastInfoController2.render());
            renderBeastController2.setMonster2(enemyAllyMonster);
            enemyMonstersBox.getChildren().clear();
            Parent enemyAlly = renderBeastController2.render();
            enemyMonstersBox.getChildren().addAll(enemyAlly);
            HBox.setHgrow(enemyAlly, Priority.ALWAYS);

            // Set enemy ally monster opponent ID
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> opponent.monster().equals(enemyBeastInfoController2.getMonster()._id()))
                    .findFirst()
                    .ifPresent(opponent -> renderBeastController2.setMonsterTwoOpponentId(opponent._id()));
        }

        setNumberOfAttacks();

        if (shouldUpdateUIOnChange) {
            updateUIOnChange();
        }

        if (!cache.getCurrentEncounter().isWild()) {
            leaveEncounter.setVisible(false);
        }

        disposables.add(eventListener.listen("encounters." + cache.getCurrentEncounter()._id() + ".trainers.*.opponents.*.created", Opponent.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(o -> {
                            cache.addCurrentOpponent(o.data());
                            allyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), o.data().trainer(), o.data().monster()).blockingFirst();

                            beastInfoController2 = beastInfoControllerProvider.get().setMonster(allyMonster);
                            beastInfoBox.getChildren().addAll(beastInfoController2.render());
                            renderBeastController1.setMonster2(allyMonster);
                            ownMonstersBox.getChildren().clear();
                            Parent allyMonster = renderBeastController1.render();
                            ownMonstersBox.getChildren().addAll(allyMonster);
                            HBox.setHgrow(allyMonster, Priority.ALWAYS);

                            // Set ally monster opponent ID
                            cache.getCurrentOpponents().stream()
                                    .filter(opponent -> opponent.monster().equals(beastInfoController2.getMonster()._id()))
                                    .findFirst()
                                    .ifPresent(opponent -> renderBeastController1.setMonsterTwoOpponentId(opponent._id()));
                        },
                        error -> System.err.println("Fehler: " + error.getMessage()))
        );

        return parent;
    }

    private void setNumberOfAttacks() {
        if (myMonster.abilities().size() == 1) {
            attackBox2.setVisible(false);
            attackBox3.setVisible(false);
            attackBox4.setVisible(false);
        } else if (myMonster.abilities().size() == 2) {
            attackBox3.setVisible(false);
            attackBox4.setVisible(false);
        } else if (myMonster.abilities().size() == 3) {
            attackBox4.setVisible(false);
        }
        setAttackBoxes(myMonster.abilities().size());
    }

    private void setAttackBoxes(int size) {
        Stack<Integer> stack = new Stack<>();
        Set<String> keys = myMonster.abilities().keySet();
        keys.forEach(key -> stack.push(Integer.parseInt(key)));
        switch (size) {
            case 4:
                setAttack4(presetsService.getAbility(stack.pop()).blockingFirst());
            case 3:
                setAttack3(presetsService.getAbility(stack.pop()).blockingFirst());
            case 2:
                setAttack2(presetsService.getAbility(stack.pop()).blockingFirst());
            case 1:
                setAttack1(presetsService.getAbility(stack.pop()).blockingFirst());
        }
    }

    //onClicked leave encounter button
    @FXML
    public void leaveEncounter() {
        System.out.println("leave encounter");

        if (cache.getCurrentEncounter().isWild()) {
            System.out.println("current encounter: " + cache.getCurrentEncounter() + " current opponent: " + cache.getOpponentByTrainerID(cache.getTrainer()._id())._id());

            disposables.add(
                    encounterOpponentsService.deleteOpponent(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id(), cache.getOpponentByTrainerID(cache.getTrainer()._id())._id())
                            .observeOn(FX_SCHEDULER)
                            .doFinally(() -> {
                                cache.setCurrentEncounter(null);
                                cache.getCurrentOpponents().clear();

                                IngameController controller = ingameControllerProvider.get();
                                controller.setRegion(cache.getJoinedRegion());
                                app.show(controller);
                            })
                            .subscribe()
            );
        }
    }

    //onClicked change monster button
    @FXML
    public void changeMonster() {
        ChangeBeastController controller = changeBeastControllerProvider.get();
        controller.setCurrentMonster(myMonster);
        controller.setEncounterController(this);
        app.show(controller);
    }

    // Trying to update UI when monster changed
    public void setToUpdateUIOnChange() {
        this.shouldUpdateUIOnChange = true;
    }

    //setter methods for monsters
    public EncounterController setOwnMonsters(List<Monster> ownMonsters) {
        this.ownMonsters = ownMonsters;
        return this;
    }

    public EncounterController setOwnMonster(Monster ownMonster) {
        this.myMonster = ownMonster;
        return this;
    }

    public EncounterController setEnemyMonsters(List<Monster> enemyMonsters) {
        this.enemyMonsters = enemyMonsters;
        return this;
    }

    public EncounterController setAllyMonster(Monster allyMonster) {
        this.allyMonster = allyMonster;
        return this;
    }

    public EncounterController setEnemyMonster(Monster enemyMonster) {
        this.enemyMonster = enemyMonster;
        return this;
    }

    public EncounterController setEnemyAllyMonster(Monster enemyAllyMonster) {
        this.enemyAllyMonster = enemyAllyMonster;
        return this;
    }

    public EncounterController setAllyTrainer(String allyTrainer) {
        this.allyTrainer = allyTrainer;
        return this;
    }

    public EncounterController setEnemyTrainer(String enemyTrainer) {
        this.enemyTrainer = enemyTrainer;
        return this;
    }

    public EncounterController setEnemyAllyTrainer(String enemyAllyTrainer) {
        this.enemyAllyTrainer = enemyAllyTrainer;
        return this;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

    //methods for attack buttons
    @FXML
    public void executeAttack1() {
        setAttackWithClick(attackBox1, ability1);
    }

    @FXML
    public void executeAttack2() {
        setAttackWithClick(attackBox2, ability2);
    }

    @FXML
    public void executeAttack3() {
        setAttackWithClick(attackBox3, ability3);
    }

    @FXML
    public void executeAttack4() {
        setAttackWithClick(attackBox4, ability4);
    }

    //methods for setting labels in attack boxes
    private void setAttack1(AbilityDto abilityDto) {
        attackNameLabel1.setText(abilityDto.name());
        attackTypeLabel1.setText("Type: " + abilityDto.type());
        accLabel1.setText("Accuracy: " + abilityDto.accuracy());
        powerLabel1.setText("Power: " + abilityDto.power());
        ability1 = abilityDto;
    }

    private void setAttack2(AbilityDto abilityDto) {
        attackNameLabel2.setText(abilityDto.name());
        attackTypeLabel2.setText("Type: " + abilityDto.type());
        accLabel2.setText("Accuracy: " + abilityDto.accuracy());
        powerLabel2.setText("Power: " + abilityDto.power());
        ability2 = abilityDto;
    }

    private void setAttack3(AbilityDto abilityDto) {
        attackNameLabel3.setText(abilityDto.name());
        attackTypeLabel3.setText("Type: " + abilityDto.type());
        accLabel3.setText("Accuracy: " + abilityDto.accuracy());
        powerLabel3.setText("Power: " + abilityDto.power());
        ability3 = abilityDto;
    }

    private void setAttack4(AbilityDto abilityDto) {
        attackNameLabel4.setText(abilityDto.name());
        attackTypeLabel4.setText("Type: " + abilityDto.type());
        accLabel4.setText("Accuracy: " + abilityDto.accuracy());
        powerLabel4.setText("Power: " + abilityDto.power());
        ability4 = abilityDto;
    }

    private void setAttackWithClick(VBox attackBox, AbilityDto abilityDto) {
        System.out.println(abilityDto.toString());
        System.out.println(cache.getOpponentByTrainerID(enemyTrainer).toString());
        Monster before = myMonster;
        Monster beforeEnemy = enemyMonster;

        Opponent o = encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(),
                cache.getCurrentEncounter()._id(), cache.getOpponentByTrainerID(cache.getTrainer()._id())._id(), null
                , new AbilityMove("ability", abilityDto.id(), enemyTrainer)).blockingFirst();

        /*disposables.add(encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(),
                        cache.getCurrentEncounter()._id(), cache.getOpponentByTrainerID(cache.getTrainer()._id())._id(), null
                        , new AbilityMove("ability", abilityDto.id(), enemyTrainer))
                .observeOn(FX_SCHEDULER)
                .subscribe(
                        e -> updateUIOnChange()
                ));*/
        updateUIOnChange();
    }

    public void updateUIOnChange() {
        // Get the monster from the current opponents of the encounter
        disposables.add(encounterOpponentsService.getEncounterOpponents(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(o -> {
                    // When there is no opponent registered on the server anymore = lose
                    if (o.size() == 0) {
                        beastInfoController1.hpLabel.setText("0 / " + myMonster.attributes().health() + " (HP)");
                        beastInfoController1.setLifeBarValue(0);
                    if (o.isEmpty()) {
                        EndScreenController endScreenController;
                        Monster myMon = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), ownMonster._id()).blockingFirst();
                        if (myMon.currentAttributes().health() <= 0) {
                            beastInfoController1.hpLabel.setText("0 / " + myMonster.attributes().health() + " (HP)");
                            beastInfoController1.setLifeBarValue(0);

                            endScreenController = setEndScreen(false, myMonster, allyMonster, enemyMonster, enemyAllyMonster);
                            app.show(endScreenController);
                        } else {
                            endScreenController = setEndScreen(true, enemyMonster, enemyAllyMonster, myMonster, allyMonster);

                            if (myMon.level() > myMonster.level()) { //Level Up
                                LevelUpController controller = levelUpControllerProvider.get();
                                if (myMon.abilities().size() > myMonster.abilities().size()) { //new attack
                                    if (myMon.type() != myMonster.type()) { // Evolved
                                        controller.setBeast(myMonster, true, true, myMonster.attributes().attack() - myMonster.attributes().health(), endScreenController);
                                    } else {
                                        controller.setBeast(myMon, true, false, 0, endScreenController);
                                    }
                                } else {
                                    controller.setBeast(myMon, false, false, 0, endScreenController);
                                }
                                app.show(controller);
                            } else {
                                app.show(endScreenController);
                            }
                        }
                    } else {
                        for (Opponent opponent : o) {
                            // Check if the opponent is our trainers id
                            if (opponent.trainer().equals(cache.getTrainer()._id())) {
                                // If the monster has died during change, show 0 HP, otherwise the current HP of the monster
                                if (opponent.monster() != null) {
                                    myMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), opponent.monster()).blockingFirst();
                                    beastInfoController1.hpLabel.setText(myMonster.currentAttributes().health() + " / " + myMonster.attributes().health() + " (HP)");
                                    beastInfoController1.setLifeBarValue(myMonster.currentAttributes().health() / (double) myMonster.attributes().health());
                                } else {
                                    beastInfoController1.hpLabel.setText("0 / " + myMonster.attributes().health() + " (HP)");
                                    beastInfoController1.setLifeBarValue(0);
                                    //TODO Change beast from Sylvan
                                }
                            } else {
                                if (opponent.monster() != null) {
                                    Monster beforeMonster = enemyMonster;
                                    enemyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), enemyTrainer, opponent.monster()).blockingFirst();
                                    enemyBeastInfoController1.setLifeBarValue((double) enemyMonster.currentAttributes().health() / (double) enemyMonster.attributes().health());
                                    if (beforeMonster.type() != enemyMonster.type()) {
                                        enemyMonstersBox.getChildren().removeAll();
                                        renderBeastController2.destroy();
                                        if (renderBeastController2.monster1 == beforeMonster) {
                                            renderBeastController2 = renderBeastControllerProvider.get().setMonster1(enemyMonster);
                                        } else {
                                            renderBeastController2 = renderBeastControllerProvider.get().setMonster2(enemyMonster);
                                        }
                                        Parent enemyMonster = renderBeastController2.render();
                                        enemyMonstersBox.getChildren().addAll(enemyMonster);
                                        HBox.setHgrow(enemyMonster, Priority.ALWAYS);
                                    }
                                }
                            }
                        }
                    }
                }));
    }

    public EndScreenController setEndScreen(boolean wonFight, Monster loser1, Monster loser2, Monster winner1, Monster winner2) {
        EndScreenController controller = endScreenControllerProvider.get();
        controller.setWinner(wonFight);
        controller.setLoserMonster1(loser1);
        if (loser2 != null) {
            controller.setLoserMonster2(loser2);
        }
        controller.setWinnerMonster1(winner1);
        if (winner2 != null) {
            controller.setWinnerMonster2(winner2);
        }
        return controller;
    }
}
