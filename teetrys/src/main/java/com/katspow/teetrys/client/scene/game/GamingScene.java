package com.katspow.teetrys.client.scene.game;

import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.math.Pt;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.Gui;
import com.katspow.teetrys.client.core.Teetrymino;
import com.katspow.teetrys.client.core.Gui.Labels;

public class GamingScene extends Scene {
    
    private Director director;
    private ActorContainer root;
    
    private Teetrymino currentTeetrymino;
    private Pt origin;
    
    public GamingScene(Director director) throws Exception {
        
        this.director = director;

        root = new ActorContainer();
        root.setBounds(0, 0, director.canvas.getCoordinateSpaceWidth(), director.canvas.getCoordinateSpaceHeight());
        root.setFillStrokeStyle(CaatjaColor.valueOf("#161714"));

        addChild(root);
        
    }
    
    public void addGuiFixedLabels() throws Exception {
        Gui.addImage(332, 10, Labels.SCORE, this, director);
        Gui.addImage(414, 190, Labels.LEVEL, this, director);
        Gui.addImage(367, 100, Labels.LINES, this, director);
        Gui.addImage(367, 280, Labels.NEXT, this, director);
    }
    
    public Teetrymino getCurrentTeetrymino() {
        return currentTeetrymino;
    }
    
    public void setCurrentTeetrymino(Teetrymino currentTeetrymino) {
        this.currentTeetrymino = currentTeetrymino;
    }
    
    public Pt getOrigin() {
        return origin;
    }
    
    public void setOrigin(Pt origin) {
        this.origin = origin;
    }

    public void addGuiLeftButtons() throws Exception {
        Gui.addImage(0, 0, Labels.QUIT, this, director);
        Gui.addImage(0, Constants.CUBE_SIDE, Labels.PAUSE, this, director);
    }
    
}
