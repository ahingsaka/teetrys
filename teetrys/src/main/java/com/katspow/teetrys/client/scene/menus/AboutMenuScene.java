package com.katspow.teetrys.client.scene.menus;

import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.MouseListener;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.foundation.ui.TextActor;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.GameController;
import com.katspow.teetrys.client.statemachine.StateMachine.GameEvent;

public class AboutMenuScene extends Scene {

    private Director director;
    private ActorContainer root;

    public AboutMenuScene(Director director) throws Exception {
        this.director = director;

        root = new ActorContainer();
        root.setBounds(0, 0, director.canvas.getCoordinateSpaceWidth(), director.canvas.getCoordinateSpaceHeight());
        root.setFillStrokeStyle(Constants.BACKGROUND_COLOR);

        addChild(root);
        
        addAboutText();
    }
    
    private void addAboutText() throws Exception {
    	
    	TextActor text = new TextActor();
    	
    	text.
    	setFont("30px sans-serif").
    	setText("Powered by Caatja").
    	calcTextSize(director).
        setTextFillStyle("white").
        setLocation((Constants.GAME_WIDTH - text.width) / 2, (Constants.GAME_HEIGHT - text.height) / 2 - 100);
    	
    	TextActor text2 = new TextActor();
    	
    	text2.
    	setFont("25px sans-serif").
    	setText("Author : Ahingsaka Samsenesena").
    	calcTextSize(director).
        setTextFillStyle("white").
        setLocation((Constants.GAME_WIDTH - text2.width) / 2, (Constants.GAME_HEIGHT - text2.height) / 2 + 20);
    	
        TextActor ta = new TextActor();
        
        ta.setMouseClickListener(new MouseListener() {
            public void call(CAATMouseEvent e) throws Exception {
                GameController.sendEvent(GameEvent.CHOOSE_MAIN_MENU);
            }
        });
        
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
        
        ta.setMouseDragListener(null);
        
        ta.setFont("40px sans-serif").
        setText("back").
        calcTextSize(director).
        setTextFillStyle("white").
        disableDrag();
        
        ta.setLocation((Constants.GAME_WIDTH - ta.width) / 2, (Constants.GAME_HEIGHT - ta.height) / 2);
        ta.cacheAsBitmap();
        
        root.addChild(text);
        root.addChild(text2);
        root.addChild(ta);
        
    }

}
