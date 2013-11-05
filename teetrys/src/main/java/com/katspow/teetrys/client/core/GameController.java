package com.katspow.teetrys.client.core;

import java.util.List;

import com.katspow.caatja.CAATKeyListener;
import com.katspow.caatja.behavior.AlphaBehavior;
import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.core.image.CaatjaImageLoaderCallback;
import com.katspow.caatja.core.image.CaatjaPreloader;
import com.katspow.caatja.event.CAATKeyEvent;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.MouseListener;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.Scene.Ease;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.Actor.Anchor;
import com.katspow.caatja.foundation.actor.ImageActor;
import com.katspow.caatja.foundation.timer.Callback;
import com.katspow.caatja.foundation.timer.TimerTask;
import com.katspow.caatja.foundation.ui.TextActor;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.Cube.Full;
import com.katspow.teetrys.client.core.Gui.Labels;
import com.katspow.teetrys.client.effects.EaseInOut;
import com.katspow.teetrys.client.effects.Effects;
import com.katspow.teetrys.client.scene.LoadingScene;
import com.katspow.teetrys.client.scene.game.GamingScene;
import com.katspow.teetrys.client.scene.menus.AboutMenuScene;
import com.katspow.teetrys.client.scene.menus.HighscoresScene;
import com.katspow.teetrys.client.scene.menus.MainMenuScene;
import com.katspow.teetrys.client.statemachine.StateMachine;
import com.katspow.teetrys.client.statemachine.StateMachine.GameEvent;
import com.katspow.teetrys.client.statemachine.StateMachine.GameState;

/**
 * As the name implies, the game is controlled here.
 * 
 */
public class GameController {

    private static StateMachine stateMachine;
    
    private Director director;
    private CaatjaCanvas canvas;
    
    private LoadingScene loadScene;
    private MainMenuScene mainMenuScene;
    private AboutMenuScene aboutMenuScene;
    private HighscoresScene highscoresScene;
    private GamingScene gamingScene;
    private TimerTask timerTask;
    
    private GameWorld gameWorld;
    
    private Double blockUntilTime;
    
    private static boolean mouseDownOnLeftSide;
    private static boolean mouseDownOnRightSide;
    private static boolean mouseDownOnDownSide;
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    public GameController() throws Exception {
        stateMachine = new StateMachine(this);
        canvas = Caatja.createCanvas();
        director = new Director();
        director.initialize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, canvas);
        
        loadScene = new LoadingScene(director);
        director.addScene(loadScene);
    }

    public void start() throws Exception {
        setupCaatja();
        loadImages();
    }
    
    private void loadImages() {
        preloadImages();

        CaatjaImageLoader caatjaImageLoader = Caatja.getCaatjaImageLoader();
        caatjaImageLoader.loadImages(Caatja.getCaatjaImagePreloader(), new CaatjaImageLoaderCallback() {
            @Override
            public void onFinishedLoading() throws Exception {
                finishImageLoading();
            }
        });
    }

    private void preloadImages() {
        final CaatjaPreloader preloader = Caatja.getCaatjaImagePreloader();
        
        preloader.addImage(Labels.TEETRYS.getLabel(), "teetrys.png");
        preloader.addImage(Labels.SCORE.getLabel(), "score.png");
        preloader.addImage(Labels.LINES.getLabel(), "lines.png");
        preloader.addImage(Labels.LEVEL.getLabel(), "lvl.png");
        preloader.addImage(Labels.NEXT.getLabel(), "next.png");
        preloader.addImage(Labels.SLEEP.getLabel(), "sleep.png");
        preloader.addImage(Labels.QUIT.getLabel(), "quit.png");
        preloader.addImage(Labels.PAUSE.getLabel(), "pause.png");
        preloader.addImage(Labels.NUMBERS.getLabel(), "numbers.png");
        preloader.addImage(Labels.GAME_OVER.getLabel(), "gameover.png");
        preloader.addImage(Labels.OK.getLabel(), "ok.png");
        preloader.addImage(Labels.CANCEL.getLabel(), "cancel.png");
        preloader.addImage(Labels.EXIT.getLabel(), "exit.png");
        preloader.addImage(Labels.WELLDONE.getLabel(), "welldone.png");
        
    }
    
    private void finishImageLoading() throws Exception {
        director.imagesCache = Caatja.getCaatjaImagePreloader().getCaatjaImages();
        
        Gui.createCompoundImage(director.getImage("numbers"), 1, 10);
        
        stateMachine.setState(GameState.MENUS);
        
        registerMovementKeys();
        
        stateMachine.start();
    }

    private void setupCaatja() throws Exception {
        Caatja.addCanvas(canvas);
        Caatja.loop(60);
    }

    /**
     * When we enter the main menu, we show an effect and the different choices.
     * @throws Exception 
     */
    public void enterMainMenu() throws Exception {
        director.easeIn(director.getSceneIndex(getMainMenuScene()), Ease.SCALE, 2000, false, Anchor.CENTER, Interpolator.createElasticOutInterpolator(2.5, .4, false));
    }
    
    // Move to gaming scene ??
    public void startGame() throws Exception {
    	
    	if (timerTask != null) {
    		timerTask.cancel();
    		timerTask = null;
    	}
    	
    	gamingScene.FALL_TIME = Constants.START_FALL_TIME;
    	Teetrymino oldCurrentTeetrymino = getGamingScene().getCurrentTeetrymino();
    	Teetrymino oldNextTeetrymino = getGamingScene().getNextTeetrymino();
    	
    	if (oldCurrentTeetrymino != null) {
    		oldCurrentTeetrymino.expire();;
    	}
    	
    	if (oldNextTeetrymino != null)	 {
    		oldNextTeetrymino.expire();
    	}
    	
        // Init world
        gameWorld = new GameWorld();
        List<Actor> walls = gameWorld.createWalls();
        
        for (Actor cubeWall : walls) {
            getGamingScene().addChild(cubeWall);
        }
        
        // TODO We should use zorder
        getGamingScene().addGuiFixedLabels();
        getGamingScene().addGuiLeftButtons();
        getGamingScene().addGuiDigits();
        
        // Start game !
        int x = Constants.LEFT_SPACE + Constants.START_POINT_X * Constants.CUBE_SIDE;
        int y = Constants.START_POINT_Y;
        
        Teetrymino currentTeetrymino = gamingScene.buildCurrentTeetrymino(x, y);
        getGamingScene().setCurrentTeetrymino(currentTeetrymino);
        
        Teetrymino nextTeetrymino = gamingScene.buildNextTeetrymino();
        getGamingScene().setNextTeetrymino(nextTeetrymino);
        
        Score.init();
        Gui.refreshScores();
        
        createGameTimer(0, gamingScene.FALL_TIME);
        
    }
    
    private void registerMovementKeys() {

        CAAT.registerKeyListener(new CAATKeyListener() {
            public void call(CAATKeyEvent keyEvent) {
                try {
                    
                    if (keyEvent.getAction().equals("down")) { 
                        if (keyEvent.getKeyCode() == CAAT.Keys.DOWN.getValue()) {
                            stateMachine.sendEvent(GameEvent.CALL_DOWN);
                        } else if (keyEvent.getKeyCode() == CAAT.Keys.LEFT.getValue()) {
                            stateMachine.sendEvent(GameEvent.CALL_LEFT);
                        } else if (keyEvent.getKeyCode() == CAAT.Keys.RIGHT.getValue()) {
                            stateMachine.sendEvent(GameEvent.CALL_RIGHT);
                        } else if (keyEvent.getKeyCode() == CAAT.Keys.UP.getValue()) {
                            stateMachine.sendEvent(GameEvent.CALL_UP);
                        } else if (keyEvent.getKeyCode() == CAAT.Keys.p.getValue()) {
                            stateMachine.sendEvent(GameEvent.CALL_PAUSE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void createGameTimer(double startTime, double duration) throws Exception {
        timerTask = getGamingScene().createTimer(startTime, duration, new Callback() {
            public void call(double sceneTime, double ttime, TimerTask timerTask) {
                try {
                	
                	// Animation called
                	if (blockUntilTime != null) {
                		if (sceneTime <= blockUntilTime) {
                			timerTask.reset(sceneTime);
                			return;
                		} else {
                			blockUntilTime = null;
                			gamingScene.reinit();
                			timerTask.reset(sceneTime);
                		}
                		
                	}
                	
                    Teetrymino currentTeetrymino = gamingScene.getCurrentTeetrymino();
                    List<Actor> currentCubes = currentTeetrymino.getCubes();
                    
                    boolean collisionFound = Collision.checkCollisionsForAllCubes(currentCubes, Direction.DOWN, Constants.CUBE_SIDE, gameWorld.getGameboard());
                    
                    if (collisionFound) {
                        
                        // If we are at the top, it's game over
                        if (gamingScene.getOrigin().y == Constants.START_POINT_Y) {
                            stateMachine.sendEvent(GameEvent.LOSE);
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
                        	gamingScene.reinit();
                        	gamingScene.checkForLevel();
                        }
                        
                    } else {
                    	gamingScene.moveCubes(currentCubes, 0, Constants.CUBE_SIDE);
                        getGamingScene().getOrigin().y += Constants.CUBE_SIDE;
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
	            		gamingScene.moveCurrentTeetrymino(Direction.LEFT, gameWorld);
	            	} else if (mouseDownOnRightSide) {
	            		gamingScene.moveCurrentTeetrymino(Direction.RIGHT, gameWorld);
	            	} else if (mouseDownOnDownSide) {
	            		gamingScene.moveCurrentTeetrymino(Direction.DOWN, gameWorld);
	            	}
            	} catch (Exception e) {
            		
            	}
            }
            
        }, new Callback() {
            public void call(double time, double ttime, TimerTask timerTask) {
                try {
                    createGameTimer(time, gamingScene.FALL_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    
    private void checkLines(List<Integer> fullLinesIndexes, Integer indexToCheckUpperLines, double sceneTime) {
        
        if (!fullLinesIndexes.isEmpty()) {
        	
            double returnTime = sceneTime;
            double newReturnTime = sceneTime;
            
            for (Integer index : fullLinesIndexes) {
                Cube[] line = gameWorld.getGameboard()[index];
                
                for (int i = 1; i < line.length - 1; i++) {
                    Full fullCube = (Full) line[i];
                    newReturnTime = Effects.blinkAndDisappear(fullCube.getValue(), sceneTime);
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
    
    public void moveCurrentTeetrymino(Direction dir) throws Exception {
		gamingScene.moveCurrentTeetrymino(dir, gameWorld);
	}
    
    private Scene getMainMenuScene() throws Exception {
        if (mainMenuScene == null) {
            mainMenuScene = new MainMenuScene(director);
            director.addScene(mainMenuScene);
        }
        
        return mainMenuScene;
    }
    
    private Scene getAboutMenuScene() throws Exception {
        if (aboutMenuScene == null) {
            aboutMenuScene = new AboutMenuScene(director);
            director.addScene(aboutMenuScene);
        }
        return aboutMenuScene;
    }
    
    public HighscoresScene getHighscoresScene() throws Exception {
        if (highscoresScene == null) {
            highscoresScene = new HighscoresScene(director);
            director.addScene(highscoresScene);
        }
        return highscoresScene;
    }
   
    public GamingScene getGamingScene() {
        return gamingScene;
    }

    public static void sendEvent(GameEvent gameEvent) throws Exception {
        stateMachine.sendEvent(gameEvent);
    }
    
    /**
     * 
     * @param gameEvent
     * @param mouseEvent
     * @throws Exception
     */
    public static void sendEvent(GameEvent gameEvent, CAATMouseEvent mouseEvent) throws Exception {
    	
    	if (gameEvent == GameEvent.CALL_MOUSE_DOWN) {
    		
    		double x = mouseEvent.x;
    		double y = mouseEvent.y;
    		
    		if (y > Constants.GAME_HEIGHT - Constants.CUBE_SIDE * 2) {
    			mouseDownOnDownSide = true;
    		} else if (x < (Constants.GAME_WIDTH / 2)) {
    			mouseDownOnLeftSide = true;
    		} else if (x > (Constants.GAME_WIDTH / 2)) {
    			mouseDownOnRightSide = true;
    		} 
    		
    		
    	} else if (gameEvent == GameEvent.CALL_MOUSE_UP) {
    		mouseDownOnLeftSide = false;
    		mouseDownOnRightSide = false;
    		mouseDownOnDownSide = false;
    	}
        
    }
    

    public void enterAboutMenu() throws Exception {
        EaseInOut.scenesFromRightToLeft(director, getAboutMenuScene(), director.getCurrentScene());
    }

    public void enterHighscores() throws Exception {
        EaseInOut.scenesFromLeftToRight(director, getHighscoresScene(), director.getCurrentScene());
    }

    public void enterGaming() throws Exception {
    	gamingScene = null;
    	
    	gamingScene = new GamingScene(director);
        director.addScene(gamingScene);
    	
        EaseInOut.scenesFromUpToDown(director, getGamingScene(), director.getCurrentScene());
        stateMachine.sendEvent(GameEvent.START_GAME);
    }

    public void enterPause() throws Exception {
        timerTask.suspended = true;
        getGamingScene().hideGamingArea(gameWorld);
        Gui.addImage(170, 300, Labels.PAUSE, getGamingScene(), director);
    }
    
    public void enterQuit() throws Exception {
    	timerTask.suspended = true;
    	getGamingScene().hideGamingArea(gameWorld);
    	
    	Gui.addImage(200, 250, Labels.EXIT, getGamingScene(), director);
    	Gui.addImage(170, 360, Labels.OK, getGamingScene(), director);
    	Gui.addImage(250, 360, Labels.CANCEL, getGamingScene(), director);
    	
	}
    
    public void exitQuit() {
    	timerTask.suspended = false;
    	getGamingScene().showGamingArea();
    	Gui.hideImage(Labels.EXIT);
    	Gui.hideImage(Labels.OK);
    	Gui.hideImage(Labels.CANCEL);
    }
    
    public void exitGameOver() {
    	timerTask.suspended = false;
    	Gui.hideImage(Labels.GAME_OVER);
    	gamingScene = null;
    	
	}

    public void exitPause() {
        timerTask.suspended = false;
        getGamingScene().clearHideCubes();
        Gui.hideImage(Labels.PAUSE);
    }

    public void enterGameOver() throws Exception {
		timerTask.suspended = true;
		getGamingScene().hideGamingArea(gameWorld);
		ImageActor gameOverImage = Gui.addImage(97, 300, Labels.GAME_OVER,
				getGamingScene(), director);

		// Move that !
		gameOverImage.setAlpha(0);

		AlphaBehavior alpha_behavior = new AlphaBehavior().setValues(0, 1)
				.setFrameTime(getGamingScene().time, 2000);

		gameOverImage.addBehavior(alpha_behavior);

		TextActor number = Gui.displayNumber(350, Score.getScore(),
				getGamingScene(), director);

		number.setMouseClickListener(new MouseListener() {
			public void call(CAATMouseEvent e) throws Exception {
				GameController.sendEvent(GameEvent.CALL_MENU);
			}
		});
        
    }

	public void enterEnd() throws Exception {
		timerTask.suspended = true;
    	getGamingScene().hideGamingArea(gameWorld);
    	
    	Gui.addImage(125, 250, Labels.WELLDONE, getGamingScene(), director);
    	
    	TextActor number = Gui.displayNumber(350, Score.getScore(), getGamingScene(), director);
    	
    	number.setMouseClickListener(new MouseListener() {
			public void call(CAATMouseEvent e) throws Exception {
				GameController.sendEvent(GameEvent.CALL_MENU);
			}
		});
    	
	}

	public void exitEnd() {
		timerTask.suspended = false;
    	Gui.hideImage(Labels.WELLDONE);
	}

}
