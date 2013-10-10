package com.katspow.teetrys.client.scene.menus;

import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.foundation.ui.TextActor;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.GameController;
import com.katspow.teetrys.client.statemachine.StateMachine.GameEvent;

public class HighscoresScene extends Scene {

    private Director director;
    private ActorContainer root;

    public HighscoresScene(Director director) throws Exception {
        this.director = director;

        root = new ActorContainer();
        root.setBounds(0, 0, director.canvas.getCoordinateSpaceWidth(), director.canvas.getCoordinateSpaceHeight());
        root.setFillStrokeStyle(Constants.BACKGROUND_COLOR);

        addChild(root);

        addHighscores();
    }

    private void addHighscores() throws Exception {

        TextActor ta = new TextActor() {

            @Override
            public void mouseClick(CAATMouseEvent mouseEvent) throws Exception {
                GameController.sendEvent(GameEvent.CHOOSE_MAIN_MENU);
            }

            @Override
            public void mouseEnter(CAATMouseEvent mouseEvent) {
                Actor actor = mouseEvent.source;
                actor.setScale(1.2, 1.2);
            }

            @Override
            public void mouseExit(CAATMouseEvent mouseEvent) {
                Actor actor = mouseEvent.source;
                actor.setScale(1, 1);
            }

            @Override
            public void mouseDrag(CAATMouseEvent mouseEvent) {
            }

        };

        ta.setFont("40px sans-serif").setText("back").calcTextSize(director).setTextFillStyle("white").disableDrag();

        ta.setLocation((GameController.GAME_WIDTH - ta.width) / 2, (GameController.GAME_HEIGHT - ta.height) / 2);
        ta.cacheAsBitmap();

        root.addChild(ta);

    }

}
