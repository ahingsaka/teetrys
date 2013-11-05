package com.katspow.teetrys.client.core;

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
import com.katspow.caatja.foundation.actor.Actor.Anchor;
import com.katspow.caatja.foundation.actor.ImageActor;
import com.katspow.caatja.foundation.ui.TextActor;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.Gui.Labels;
import com.katspow.teetrys.client.effects.EaseInOut;
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
    
    public void startGame() throws Exception {
    	gamingScene.startGame();
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

    public void moveCurrentTeetrymino(Direction dir) throws Exception {
		gamingScene.moveCurrentTeetrymino(dir);
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
    			GamingScene.mouseDownOnDownSide = true;
    		} else if (x < (Constants.GAME_WIDTH / 2)) {
    			GamingScene.mouseDownOnLeftSide = true;
    		} else if (x > (Constants.GAME_WIDTH / 2)) {
    			GamingScene.mouseDownOnRightSide = true;
    		} 
    		
    		
    	} else if (gameEvent == GameEvent.CALL_MOUSE_UP) {
    		GamingScene.mouseDownOnLeftSide = false;
    		GamingScene.mouseDownOnRightSide = false;
    		GamingScene.mouseDownOnDownSide = false;
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
        gamingScene.suspend(true);
        gamingScene.hideGamingArea();
        Gui.addImage(170, 300, Labels.PAUSE, getGamingScene(), director);
    }
    
    public void enterQuit() throws Exception {
    	gamingScene.suspend(true);
    	gamingScene.hideGamingArea();
    	
    	Gui.addImage(200, 250, Labels.EXIT, getGamingScene(), director);
    	Gui.addImage(170, 360, Labels.OK, getGamingScene(), director);
    	Gui.addImage(250, 360, Labels.CANCEL, getGamingScene(), director);
    	
	}
    
    public void exitQuit() {
    	gamingScene.suspend(false);
    	getGamingScene().showGamingArea();
    	Gui.hideImage(Labels.EXIT);
    	Gui.hideImage(Labels.OK);
    	Gui.hideImage(Labels.CANCEL);
    }
    
    public void exitGameOver() {
    	gamingScene.suspend(false);
    	Gui.hideImage(Labels.GAME_OVER);
    	gamingScene = null;
    	
	}

    public void exitPause() {
    	gamingScene.suspend(false);
        getGamingScene().clearHideCubes();
        Gui.hideImage(Labels.PAUSE);
    }

    public void enterGameOver() throws Exception {
    	gamingScene.suspend(true);
    	gamingScene.hideGamingArea();
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
		gamingScene.suspend(true);
		gamingScene.hideGamingArea();
    	
    	Gui.addImage(125, 250, Labels.WELLDONE, getGamingScene(), director);
    	
    	TextActor number = Gui.displayNumber(350, Score.getScore(), getGamingScene(), director);
    	
    	number.setMouseClickListener(new MouseListener() {
			public void call(CAATMouseEvent e) throws Exception {
				GameController.sendEvent(GameEvent.CALL_MENU);
			}
		});
    	
	}

	public void exitEnd() {
		gamingScene.suspend(false);
    	Gui.hideImage(Labels.WELLDONE);
	}

}
