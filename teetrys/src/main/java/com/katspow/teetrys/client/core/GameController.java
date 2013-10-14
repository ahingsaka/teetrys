package com.katspow.teetrys.client.core;

import java.util.List;

import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.core.image.CaatjaImageLoaderCallback;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.Scene.Ease;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.Actor.Anchor;
import com.katspow.caatja.foundation.timer.Callback;
import com.katspow.caatja.foundation.timer.TimerTask;
import com.katspow.teetrys.client.Constants;
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
    private TimerTask timerTask;
    
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
        
        // Move to gaming scene ??
        
        // Init world
        GameWorld gameWorld = new GameWorld();
        List<Actor> walls = gameWorld.createWalls();
        
        for (Actor cubeWall : walls) {
            getGamingScene().addChild(cubeWall);
        }
        
        // Start game !
        int x = Constants.LEFT_SPACE + Constants.START_POINT_X * Constants.CUBE_SIDE;
        int y = Constants.START_POINT_Y;
        
        List<Actor> currentTeetrymino = buildCurrentTeetrymino(x, y);
        gamingScene.setCurrentTeetrymino(currentTeetrymino);
        
        createGameTimer(0, 1000);
        
    }
    
    private void createGameTimer(double startTime, double duration) throws Exception {
        timerTask = getGamingScene().createTimer(startTime, duration, new Callback() {
            public void call(double time, double ttime, TimerTask timerTask) {
                try {
                    moveCubes(getGamingScene().getCurrentTeetrymino(), 0, Constants.CUBE_SIDE);
                    
                    timerTask.reset(time);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }, new Callback() {
            public void call(double time, double ttime, TimerTask timerTask) {
                // TODO Auto-generated method stub
            }
            
        }, new Callback() {
            public void call(double time, double ttime, TimerTask timerTask) {
                // TODO Auto-generated method stub
            }
        });

    }
    
    // TODO Move ?
    private List<Actor> buildCurrentTeetrymino(double x, double y) throws Exception {
        List<Actor> teetrymino = Teetrymino.createNewTeetrymino(x, y);
        for (Actor actor : teetrymino) {
            getGamingScene().addChild(actor);
        }
        
        return teetrymino;
    }
    
    private void moveCubes(List<Actor> cubes, double addx, double addy) {
        for (Actor cube : cubes) {
            // If necessary ?
            if (cube.y < Constants.GAME_HEIGHT) {
                cube.x += addx;
                cube.y += addy;
            }
        }
        
        
    }
    
//    move_cubes: (cube_list, add_x, add_y) ->
//    for cube in cube_list
//        cube.x += add_x
//        cube.y += add_y
//    @y = @y + add_y
//    @x = @x + add_x
    
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
