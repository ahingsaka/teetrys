package com.katspow.teetrys.client.scene.game;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.MouseListener;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.math.Pt;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.GameWorld;
import com.katspow.teetrys.client.core.Gui;
import com.katspow.teetrys.client.core.Gui.Labels;
import com.katspow.teetrys.client.core.Teetrymino;
import com.katspow.teetrys.client.effects.Effects;

public class GamingScene extends Scene {
    
    private Director director;
    private ActorContainer root;
    
    private Teetrymino currentTeetrymino;
    private Teetrymino nextTeetrymino;
    
    private Pt origin;
    
    public GamingScene(Director director) throws Exception {
    	
        this.director = director;

        root = new ActorContainer();
        root.setBounds(0, 0, director.canvas.getCoordinateSpaceWidth(), director.canvas.getCoordinateSpaceHeight());
        root.setFillStrokeStyle(CaatjaColor.valueOf("#161714"));
        
        root.setMouseClickListener(new MouseListener() {
			public void call(CAATMouseEvent e) throws Exception {
				
			}
		});

        addChild(root);
        
    }
    
    public void addGuiFixedLabels() throws Exception {
        Gui.addImage(332, 10, Labels.SCORE, this, director);
        Gui.addImage(414, 190, Labels.LEVEL, this, director);
        Gui.addImage(367, 100, Labels.LINES, this, director);
        Gui.addImage(367, 280, Labels.NEXT, this, director);
    }
    
    public void addGuiDigits() throws Exception {
        Gui.createNumbers(this);
    }
    
    public void addGuiLeftButtons() throws Exception {
        Gui.addImage(0, 0, Labels.QUIT, this, director);
        Gui.addImage(0, Constants.CUBE_SIDE, Labels.SLEEP, this, director);
    }
    
    public Teetrymino getCurrentTeetrymino() {
        return currentTeetrymino;
    }
    
    public void setCurrentTeetrymino(Teetrymino currentTeetrymino) {
        this.currentTeetrymino = currentTeetrymino;
    }
    
    public Teetrymino getNextTeetrymino() {
        return nextTeetrymino;
    }
    
    public void setNextTeetrymino(Teetrymino nextTeetrymino) {
        this.nextTeetrymino = nextTeetrymino;
    }
    
    public Pt getOrigin() {
        return origin;
    }
    
    public void setOrigin(Pt origin) {
        this.origin = origin;
    }

    private List<Actor> hideCubes;
    
    public void clearHideCubes() {
    	for (Actor actor : hideCubes) {
    		actor.setExpired(true);
    		actor.setDiscardable(true);
    	}
    }

    // Hide all except first column for 'left buttons'
    public void hideGamingArea(GameWorld gameWorld) throws Exception {
        
        if (hideCubes == null) {

//            int gameboardLinesNb = gameWorld.getGameboardLinesNb();
//            int gameboardColumnsNb = gameWorld.getGameboardColumnsNb();

            hideCubes = new ArrayList<Actor>();

            int x = Constants.CUBE_SIDE;
            int y = 0;

//            for (int line = 0; line < gameboardLinesNb - 1; line++) {
//                for (int column = 1; column < gameboardColumnsNb; column++) {
                    Actor cube = Teetrymino.createCube(x, y, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, "#000000", "#000000");
                    Effects.scale(cube, time);
                    hideCubes.add(cube);
                    addChild(cube);

//                    x += Constants.CUBE_SIDE;
//                }
//
//                x = Constants.CUBE_SIDE;
//                y += Constants.CUBE_SIDE;
//
//            }
            
        } else {
            for (Actor actor : hideCubes) {
                Effects.scale(actor, time);
            }
        }
        
    }

    public void showGamingArea() {
        for (Actor cube : hideCubes) {
            Effects.scaleOutAndDisappear(cube, time);
        }
    }
    

}
