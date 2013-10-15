package com.katspow.teetrys.client.core;

import java.util.List;

import com.katspow.caatja.CAATKeyListener;
import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.core.image.CaatjaImageLoaderCallback;
import com.katspow.caatja.event.CAATKeyEvent;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.Scene.Ease;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.Actor.Anchor;
import com.katspow.caatja.foundation.timer.Callback;
import com.katspow.caatja.foundation.timer.TimerTask;
import com.katspow.caatja.math.Pt;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.Cube.Full;
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
    
    public void startGame() throws Exception {
        
        // Move to gaming scene ??
        
        // Init world
        gameWorld = new GameWorld();
        List<Actor> walls = gameWorld.createWalls();
        
        for (Actor cubeWall : walls) {
            getGamingScene().addChild(cubeWall);
        }
        
        // Start game !
        int x = Constants.LEFT_SPACE + Constants.START_POINT_X * Constants.CUBE_SIDE;
        int y = Constants.START_POINT_Y;
        
        Teetrymino currentTeetrymino = buildCurrentTeetrymino(x, y);
        gamingScene.setCurrentTeetrymino(currentTeetrymino);
        
        // Register keys
        registerMovementKeys();
        
        createGameTimer(0, 1000);
        
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
                            System.out.println("send event");
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
            public void call(double time, double ttime, TimerTask timerTask) {
                try {
                    List<Actor> currentTeetrymino = getGamingScene().getCurrentTeetrymino().getCubes();
                    
                    boolean collisionFound = Collision.checkCollisionsForAllCubes(currentTeetrymino, Direction.DOWN, Constants.CUBE_SIDE, gameWorld.getGameboard());
                    
                    if (collisionFound) {
                        storeCubes(currentTeetrymino);
                        
                        // Check lines
                        List<Integer> fullLinesIndexes = gameWorld.findNumberOfFullLines();

                        if (fullLinesIndexes.size() > 1) {
                            // last item is special index
                            int endIndex = fullLinesIndexes.size() - 1;
                            checkLines(fullLinesIndexes.subList(0, endIndex), fullLinesIndexes.get(endIndex), time);
                            
                        } else {
                            reinit();
                        }
                        
                        
                    } else {
                        moveCubes(currentTeetrymino, 0, Constants.CUBE_SIDE);
                        getGamingScene().getOrigin().y += Constants.CUBE_SIDE;
                    }
                    
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
    
    private void checkLines(List<Integer> fullLinesIndexes, Integer startIndex, double sceneTime) {
        
        if (!fullLinesIndexes.isEmpty()) {
            double returnTime = sceneTime;
            double newReturnTime = sceneTime;
            
            for (Integer index : fullLinesIndexes) {
                Cube[] line = gameWorld.getGameboard()[index];
                
                for (int i = 1; i < line.length -2; i++) {
                    Full fullCube = (Full) line[i];
                    returnTime = Effects.blinkAndDisappear(fullCube.getValue(), sceneTime);
                }
                
            }
            
        }
        
//        # If there are lines
//        if (full_lines_table.length > 0)
//
//            return_time = time
//            new_return_time = time
//
//            for index in full_lines_table
//                line = gameboard[index]
//                cpt = 0
//                for cube in line[1..line.length-2]
//                    return_time = Effects.blink_and_disappear(cube, time)
//
//            # Refresh gameboard
//            this.remove_cubes_in_gameboard(full_lines_table, gameboard)
//
//            # Make upper cubes fall
//            index_found = this.find_empty_line_index_from(index_to_check_upper_lines)
//
//            if (index_found != -1)
//                lines_to_move = []
//
//                # Add +1, we don't need the empty line ...
//                for i in [index_to_check_upper_lines..index_found + 1]
//                    lines_to_move.push(i)
//
//                # TEST NEW FALL EFFECT
//                #new_return_time = Effects.fall_effect(lines_to_move, gameboard, return_time, full_lines_table.length)
//
//                new_return_time = Effects.fall(lines_to_move, gameboard, return_time, full_lines_table.length)
//
//                # Store in waiting time
//                @waiting_time = new_return_time
//
//            # Refresh scores
//            Scores.add_lines(full_lines_table.length)
//            Gui.refreshScores()
        
    }
    
    private void reinit() throws Exception {
        int x = Constants.LEFT_SPACE + Constants.START_POINT_X * Constants.CUBE_SIDE;
        int y = Constants.START_POINT_Y;
        Teetrymino currentTeetrymino = buildCurrentTeetrymino(x, y);
        gamingScene.setCurrentTeetrymino(currentTeetrymino);
    }
    
//    re_init: ->
//    @x = Globals.LEFT_SPACE + Globals.START_POINT_X * Globals.CUBE_SIDE
//    @y = Globals.START_POINT_Y
//    @current_max_y = 0
//    @current_left_bound = 0
//    @current_right_bound = 0
//
//    @cube_list = @next_cube_list
//
//    delta_x = 350 - @x
//    delta_y = 320 - @y
//
//    for cube in @cube_list
//        cube.setStrokeStyle('#000000')
//        cube.x -= delta_x
//        cube.y -= delta_y
//
//    @current_shape_number = @next_current_shape_number
//    @current_transformation = @next_current_transformation
//
//    this.build_next_shape(350, 320)
//
//    # Kind of a hack, ZOrder does seem to work for 'last' elements only ?
//    Gui.refreshZOrder()
//
//    # TODO Clean code ...
//    @current_max_y = 0
    
    
    
    // Store the cubes in gameboard
    // Disable the mouse event
    private void storeCubes(List<Actor> cubes) {
        for (Actor cube : cubes) {
            
            // FIXME Disable touch/mouse events
            
            int cube_x = (int) (cube.x / Constants.CUBE_SIDE);
            int cube_y = (int) (cube.y / Constants.CUBE_SIDE + 1);
            
            gameWorld.getGameboard()[cube_y][cube_x] = Cube.Full.valueOf(cube);
        }
    }
    
    // TODO Move ?
    private Teetrymino buildCurrentTeetrymino(double x, double y) throws Exception {
        Teetrymino teetrymino = Teetrymino.createNewTeetrymino(x, y);
        for (Actor actor : teetrymino.getCubes()) {
            getGamingScene().addChild(actor);
        }
        
        getGamingScene().setOrigin(new Pt(x, y));
        
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
    
    public void moveCurrentTeetrymino(Direction direction) throws Exception {
        switch (direction) {
        case DOWN:
            checkCollisionAndMoveCubes(direction, 0, Constants.CUBE_SIDE);
            break;
            
        case LEFT:
            checkCollisionAndMoveCubes(direction, -Constants.CUBE_SIDE, 0);
            break;
            
        case RIGHT:
            checkCollisionAndMoveCubes(direction, Constants.CUBE_SIDE, 0);
            break;
        case UP:
            // TODO Not pretty
            Pt origin = getGamingScene().getOrigin();
            getGamingScene().getCurrentTeetrymino().rotate(origin.x, origin.y, gameWorld.getGameboard());
            System.out.println("origin.y " + origin.y);
            
            break;
        }
        
         
    }
    
    
    private void checkCollisionAndMoveCubes(Direction direction, int movex, int movey) throws Exception {
        List<Actor> currentTeetrymino = getGamingScene().getCurrentTeetrymino().getCubes();
        boolean collisionFound  = Collision.checkCollisionsForAllCubes(currentTeetrymino, direction, Constants.CUBE_SIDE, gameWorld.getGameboard());
        if (!collisionFound) {
            moveCubes(currentTeetrymino, movex, movey);
            getGamingScene().getOrigin().x += movex;
            getGamingScene().getOrigin().y += movey;
        }
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

    public void enterGaming() throws Exception {
        EaseInOut.scenesFromUpToDown(director, getGamingScene(), director.getCurrentScene());
    }


}
