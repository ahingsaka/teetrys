package com.katspow.teetrys.client.scene.menus;

import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.MouseListener;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.foundation.ui.TextActor;
import com.katspow.caatja.foundation.ui.TextFont;
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

        TextActor ta = new TextActor();
        
        ta.setMouseEnterListener(new MouseListener() {
            public void call(CAATMouseEvent e) throws Exception {
                Actor actor = e.source;
                actor.setScale(1.2, 1.2);
            }
        });
        
        ta.setMouseExitListener(new MouseListener() {
            public void call(CAATMouseEvent e) throws Exception {
                Actor actor = e.source;
                actor.setScale(1, 1);
            }
        });
        
        ta.setMouseClickListener(new MouseListener() {
            public void call(CAATMouseEvent e) throws Exception {
                GameController.sendEvent(GameEvent.CHOOSE_MAIN_MENU);
            }
        });
        
        ta.setMouseDragListener(null);

        ta.setFont(new TextFont(40, "px", "sans-serif")).setText("back").calcTextSize(director).setTextFillStyle("white").disableDrag();

        ta.setLocation((Constants.GAME_WIDTH - ta.width) / 2, (Constants.GAME_HEIGHT - ta.height) / 2);
        ta.cacheAsBitmap();

        root.addChild(ta);

    }

}
