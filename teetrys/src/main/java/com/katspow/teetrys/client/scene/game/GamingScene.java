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
import com.katspow.teetrys.client.core.Collision;
import com.katspow.teetrys.client.core.GameController;
import com.katspow.teetrys.client.core.GameWorld;
import com.katspow.teetrys.client.core.Gui;
import com.katspow.teetrys.client.core.Score;
import com.katspow.teetrys.client.core.GameController.Direction;
import com.katspow.teetrys.client.core.Gui.Labels;
import com.katspow.teetrys.client.core.Teetrymino;
import com.katspow.teetrys.client.effects.Effects;
import com.katspow.teetrys.client.statemachine.StateMachine.GameEvent;

public class GamingScene extends Scene {
	
	private static final int NEXT_Y = 280;
    private static final int NEXT_X = 310;
    
    public static int FALL_TIME;
    
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
        
        root.setMouseDownListener(new MouseListener() {
			public void call(CAATMouseEvent e) throws Exception {
				GameController.sendEvent(GameEvent.CALL_MOUSE_DOWN, e);
			}
		});
        
        root.setMouseUpListener(new MouseListener() {
			public void call(CAATMouseEvent e) throws Exception {
				GameController.sendEvent(GameEvent.CALL_MOUSE_UP, e);
			}
		});

        addChild(root);
        
    }
    
    public void reinit() throws Exception {
    	
        int x = Constants.LEFT_SPACE + Constants.START_POINT_X * Constants.CUBE_SIDE;
        int y = Constants.START_POINT_Y;
        
        setOrigin(new Pt(x, y));
        
        // Deactivate mouse click
        Teetrymino currentTeetrymino = getCurrentTeetrymino();
        for (Actor a : currentTeetrymino.getCubes()) {
        	a.setMouseClickListener(null);
        }
        
        Teetrymino nextTeetrymino = getNextTeetrymino();
        for (Actor a : nextTeetrymino.getCubes()) {
        	a.setMouseClickListener(new MouseListener() {
				public void call(CAATMouseEvent e) throws Exception {
					GameController.sendEvent(GameEvent.CALL_ROTATE);
				}
			});
        }
        
        Teetrymino.setPosition(nextTeetrymino, x, y);
        setCurrentTeetrymino(nextTeetrymino);
        
        Teetrymino buildNextTeetrymino = buildNextTeetrymino();
		setNextTeetrymino(buildNextTeetrymino);
    }
    
    public void checkCollisionAndMoveCubes(Direction direction, int movex, int movey, GameWorld gameWorld) throws Exception {
        List<Actor> currentTeetrymino = getCurrentTeetrymino().getCubes();
        boolean collisionFound  = Collision.checkCollisionsForAllCubes(currentTeetrymino, direction, Constants.CUBE_SIDE, gameWorld.getGameboard());
        if (!collisionFound) {
            moveCubes(currentTeetrymino, movex, movey);
            getOrigin().x += movex;
            getOrigin().y += movey;
        }
    }
    
    public void moveCubes(List<Actor> cubes, double addx, double addy) {
        for (Actor cube : cubes) {
            // If necessary ?
            if (cube.y < Constants.GAME_HEIGHT) {
                cube.x += addx;
                cube.y += addy;
            }
        }
    }
    
    public void moveCurrentTeetrymino(Direction direction, GameWorld gameWorld) throws Exception {
        switch (direction) {
        case DOWN:
            checkCollisionAndMoveCubes(direction, 0, Constants.CUBE_SIDE, gameWorld);
            break;
            
        case LEFT:
            checkCollisionAndMoveCubes(direction, -Constants.CUBE_SIDE, 0, gameWorld);
            break;
            
        case RIGHT:
            checkCollisionAndMoveCubes(direction, Constants.CUBE_SIDE, 0, gameWorld);
            break;
        case UP:
            // TODO Not pretty
            Pt origin = getOrigin();
            getCurrentTeetrymino().rotate(origin.x, origin.y, gameWorld.getGameboard());
            System.out.println("origin.y " + origin.y);
            
            break;
        }
        
    }
    
    public Teetrymino buildCurrentTeetrymino(double x, double y) throws Exception {
        Teetrymino teetrymino = Teetrymino.createNewTeetrymino(x, y);
        for (Actor actor : teetrymino.getCubes()) {
        	
        	actor.setMouseClickListener(new MouseListener() {
				public void call(CAATMouseEvent e) throws Exception {
					GameController.sendEvent(GameEvent.CALL_ROTATE);
				}
			});
        	
            addChild(actor);
        }
        
        setOrigin(new Pt(x, y));
        
        return teetrymino;
    }
    
    public Teetrymino buildNextTeetrymino() throws Exception {
        Teetrymino teetrymino = Teetrymino.createNewTeetrymino(NEXT_X, NEXT_Y);
        
        for (Actor actor : teetrymino.getCubes()) {
            addChild(actor);
        }
        
        return teetrymino;
    }
    
    public void checkForLevel() throws Exception {
		if (Score.checkForNextLevel()) {
			if (Score.getLevel() < 15) {
				FALL_TIME -= Constants.DECREASE_FALL_TIME;
				System.out.println("FALLTIME:" + FALL_TIME);
			} else if (Score.getLevel() == 20) {
				GameController.sendEvent(GameEvent.CALL_END);
			}
		}
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
