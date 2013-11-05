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
import com.katspow.caatja.foundation.timer.Callback;
import com.katspow.caatja.foundation.timer.TimerTask;
import com.katspow.caatja.math.Pt;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.Collision;
import com.katspow.teetrys.client.core.Cube;
import com.katspow.teetrys.client.core.GameController;
import com.katspow.teetrys.client.core.GameWorld;
import com.katspow.teetrys.client.core.Gui;
import com.katspow.teetrys.client.core.Score;
import com.katspow.teetrys.client.core.Cube.Full;
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
    private GameWorld gameWorld;
    
    private TimerTask timerTask;
    private Double blockUntilTime;
    
    private Teetrymino currentTeetrymino;
    private Teetrymino nextTeetrymino;
    
    public static boolean mouseDownOnLeftSide;
    public static boolean mouseDownOnRightSide;
    public static boolean mouseDownOnDownSide;
    
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
    
    public void startGame() throws Exception {
    	
    	if (timerTask != null) {
    		timerTask.cancel();
    		timerTask = null;
    	}
    	
    	FALL_TIME = Constants.START_FALL_TIME;
    	Teetrymino oldCurrentTeetrymino = getCurrentTeetrymino();
    	Teetrymino oldNextTeetrymino = getNextTeetrymino();
    	
    	if (oldCurrentTeetrymino != null) {
    		oldCurrentTeetrymino.expire();;
    	}
    	
    	if (oldNextTeetrymino != null)	 {
    		oldNextTeetrymino.expire();
    	}
    	
        // Init world
    	GameWorld gameWorld = new GameWorld();
        List<Actor> walls = gameWorld.createWalls();
        
        for (Actor cubeWall : walls) {
            addChild(cubeWall);
        }
        
        setGameWorld(gameWorld);
        
        addGuiFixedLabels();
        addGuiLeftButtons();
        addGuiDigits();
        
        // Start game !
        int x = Constants.LEFT_SPACE + Constants.START_POINT_X * Constants.CUBE_SIDE;
        int y = Constants.START_POINT_Y;
        
        Teetrymino currentTeetrymino = buildCurrentTeetrymino(x, y);
        setCurrentTeetrymino(currentTeetrymino);
        
        Teetrymino nextTeetrymino = buildNextTeetrymino();
        setNextTeetrymino(nextTeetrymino);
        
        Score.init();
        Gui.refreshScores();
        
        createGameTimer(0, FALL_TIME);
		
	}
    
    public void createGameTimer(double startTime, double duration) throws Exception {
        timerTask = createTimer(startTime, duration, new Callback() {
            public void call(double sceneTime, double ttime, TimerTask timerTask) {
                try {
                	
                	// Animation called
                	if (blockUntilTime != null) {
                		if (sceneTime <= blockUntilTime) {
                			timerTask.reset(sceneTime);
                			return;
                		} else {
                			blockUntilTime = null;
                			reinit();
                			timerTask.reset(sceneTime);
                		}
                		
                	}
                	
                    Teetrymino currentTeetrymino = getCurrentTeetrymino();
                    List<Actor> currentCubes = currentTeetrymino.getCubes();
                    
                    boolean collisionFound = Collision.checkCollisionsForAllCubes(currentCubes, Direction.DOWN, Constants.CUBE_SIDE, gameWorld.getGameboard());
                    
                    if (collisionFound) {
                        
                        // If we are at the top, it's game over
                        if (getOrigin().y == Constants.START_POINT_Y) {
                            GameController.sendEvent(GameEvent.LOSE);
                            return;
                        }
                        
                        gameWorld.storeCubes(currentCubes, currentTeetrymino);
                        
                        List<Integer> fullLinesIndexes = gameWorld.findNumberOfFullLines();
                        
                        while (fullLinesIndexes.size() > 1) {
                            int endIndex = fullLinesIndexes.size() - 1;
                            checkLines(fullLinesIndexes.subList(0, endIndex), fullLinesIndexes.get(endIndex), sceneTime);
                            fullLinesIndexes = gameWorld.findNumberOfFullLines();
                        }
                        
                        if (blockUntilTime == null) {
                        	reinit();
                        	checkForLevel();
                        }
                        
                    } else {
                    	moveCubes(currentCubes, 0, Constants.CUBE_SIDE);
                        getOrigin().y += Constants.CUBE_SIDE;
                    }
                    
                    timerTask.reset(sceneTime);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Callback() {
            public void call(double time, double ttime, TimerTask timerTask) {
            	// Mouse down 
            	try {
	            	if (mouseDownOnLeftSide) {
	            		moveCurrentTeetrymino(Direction.LEFT);
	            	} else if (mouseDownOnRightSide) {
	            		moveCurrentTeetrymino(Direction.RIGHT);
	            	} else if (mouseDownOnDownSide) {
	            		moveCurrentTeetrymino(Direction.DOWN);
	            	}
            	} catch (Exception e) {
            		
            	}
            }
            
        }, new Callback() {
            public void call(double time, double ttime, TimerTask timerTask) {
                try {
                    createGameTimer(time, FALL_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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
    
	private void checkLines(List<Integer> fullLinesIndexes,
			Integer indexToCheckUpperLines, double sceneTime) {

		if (!fullLinesIndexes.isEmpty()) {

			double returnTime = sceneTime;
			double newReturnTime = sceneTime;

			for (Integer index : fullLinesIndexes) {
				Cube[] line = gameWorld.getGameboard()[index];

				for (int i = 1; i < line.length - 1; i++) {
					Full fullCube = (Full) line[i];
					newReturnTime = Effects.blinkAndDisappear(
							fullCube.getValue(), sceneTime);
				}
			}

			// Refresh gameworld
			gameWorld.removeCubes(fullLinesIndexes);

			// Make upper cubes fall
			gameWorld.makeAllCubesFall(fullLinesIndexes, newReturnTime);

			// Refresh scores
			Score.addLines(fullLinesIndexes.size());
			Gui.refreshScores();

			blockUntilTime = newReturnTime + 300;

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
    
    public void moveCurrentTeetrymino(Direction direction) throws Exception {
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
    public void hideGamingArea() throws Exception {
        
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
    
    public void suspend(boolean suspend) {
    	timerTask.suspended = suspend;
    }

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	public TimerTask getTimerTask() {
		return timerTask;
	}

}
