package com.katspow.teetrys.client.scene;

import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.foundation.ui.TextActor;
import com.katspow.teetrys.client.core.GameController;

/**
 * Scene used during loading of images.
 * 
 * Just a text in black background.
 * 
 */
public class LoadingScene extends Scene {

    private ActorContainer root;

    /**
     * Creates the black background and text.
     * 
     * @param director
     * @throws Exception
     */
    public LoadingScene(Director director) throws Exception {
        this.root = new ActorContainer();
        root.setBounds(0, 0, GameController.GAME_WIDTH, GameController.GAME_HEIGHT);
        root.setFillStrokeStyle(CaatjaColor.valueOf("#000000"));

        addChild(root);

        createLoadingText(director);
    }

    /**
     * Creates the displayed text.
     * 
     * @param director
     * @throws Exception
     */
    private void createLoadingText(Director director) throws Exception {

        TextActor loading = new TextActor();
        loading.setFont("30px sans-serif")
            .setTextBaseline("top")
            .setText("Loading ...")
            .calcTextSize(director)
            .setTextFillStyle("white");

        loading.setLocation((GameController.GAME_WIDTH - loading.width) / 2, (GameController.GAME_HEIGHT) / 2);

        addChild(loading);

    }

}
