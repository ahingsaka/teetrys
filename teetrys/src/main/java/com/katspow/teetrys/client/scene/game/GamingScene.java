package com.katspow.teetrys.client.scene.game;

import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;

public class GamingScene extends Scene {
    
    private Director director;
    private ActorContainer root;
    
    private List<Actor> currentTeetrymino;

    public GamingScene(Director director) throws Exception {
        
        this.director = director;

        root = new ActorContainer();
        root.setBounds(0, 0, director.canvas.getCoordinateSpaceWidth(), director.canvas.getCoordinateSpaceHeight());
        root.setFillStrokeStyle(CaatjaColor.valueOf("#161714"));

        addChild(root);
        
    }
    
    public List<Actor> getCurrentTeetrymino() {
        return currentTeetrymino;
    }
    
    public void setCurrentTeetrymino(List<Actor> currentTeetrymino) {
        this.currentTeetrymino = currentTeetrymino;
    }
    
    

}
