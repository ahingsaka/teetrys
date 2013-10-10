package com.katspow.teetrys.client.core;

import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.core.image.CaatjaImageLoaderCallback;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.Scene.Ease;
import com.katspow.caatja.foundation.actor.Actor.Anchor;
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

    // TODO Move to constants file
    public static final int GAME_HEIGHT = 840;
    public static final int GAME_WIDTH = 480;

    private static StateMachine stateMachine;
    private Director director;
    private CaatjaCanvas canvas;
    
    private LoadingScene loadScene;
    private MainMenuScene mainMenuScene;
    private AboutMenuScene aboutMenuScene;
    private HighscoresScene highscoresScene;
    private GamingScene gamingScene;
    
    public GameController() throws Exception {
        stateMachine = new StateMachine(this);
        canvas = Caatja.createCanvas();
        director = new Director();
        director.initialize(GAME_WIDTH, GAME_HEIGHT, canvas);
        
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
        
    }
    
    private void finishImageLoading() throws Exception {
        director.imagesCache = Caatja.getCaatjaImagePreloader().getCaatjaImages();
        
        // TODO Set to introduction when it's done
        stateMachine.setState(GameState.MENUS);
        
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
        director.easeIn(director.getSceneIndex(getMainMenuScene()), Ease.SCALE, 2000, false, Anchor.CENTER, new Interpolator().createElasticOutInterpolator(2.5, .4, false));
    }
    
    public void enterGaming() throws Exception {
        EaseInOut.scenesFromUpToDown(director, getGamingScene(), director.getCurrentScene());
        
        // Start game loop !
        
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
   
    public GamingScene getGamingScene() throws Exception {
        if (gamingScene == null) {
            gamingScene = new GamingScene(director);
            director.addScene(gamingScene);
        }
        return gamingScene;
    }

    public static void sendEvent(GameEvent gameEvent) throws Exception {
        stateMachine.sendEvent(gameEvent);
    }

    public void enterAboutMenu() throws Exception {
        EaseInOut.scenesFromRightToLeft(director, getAboutMenuScene(), director.getCurrentScene());
    }

    public void enterHighscores() throws Exception {
        EaseInOut.scenesFromLeftToRight(director, getHighscoresScene(), director.getCurrentScene());
    }

}
