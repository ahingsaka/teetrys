package com.katspow.teetrys.client.statemachine;

import com.katspow.teetrys.client.core.GameController;
import com.katspow.teetrys.client.core.GameController.Direction;

/**
 * Will control the different states of the game.
 * 
 */
public class StateMachine {
    
    private GameState currentState;
    
    private static GameController gameController;
    
    public StateMachine(GameController gameController) {
        StateMachine.gameController = gameController;
    }
    
    /**
     * All events that will make the statemachine react. 
     *
     */
    public enum GameEvent {
        
        CHOOSE_ABOUT,
        CHOOSE_GAME,
        CHOOSE_HIGHSCORES,
        CHOOSE_MAIN_MENU,
        
        CALL_PAUSE,
        CALL_DOWN,
        CALL_UP,
        CALL_LEFT,
        CALL_RIGHT,
        CALL_START,
        CALL_ROTATE,
        
        LOSE, START_GAME
    }
    
    /**
     *  The different states of the game are defined by this enum.
     *
     */
    public enum GameState {
        
        INTRODUCTION(null) {
            void process(StateMachine sm, GameEvent e) {
                
            }
        },
        
        // MENUS will be the parent of these states : MAIN_MENU, HIGHSCORES, ABOUT
        MENUS(null) {
            void process(StateMachine sm, GameEvent e) throws Exception {
                sm.enterState(MAIN_MENU);
                
                // Launch sound ?
                    
            }
        },
        
        MAIN_MENU(MENUS) {
            @Override
            void entry(StateMachine sm) throws Exception {
                gameController.enterMainMenu();
            }

            void process(StateMachine sm, GameEvent e) throws Exception {
                switch (e) {
                case CHOOSE_ABOUT:
                    sm.enterState(ABOUT);
                    break;
                    
                case CHOOSE_HIGHSCORES:
                    sm.enterState(HIGHSCORES);
                    break;
                
                case CHOOSE_GAME:
                    sm.enterState(GAME);
                    sm.sendEvent(GameEvent.START_GAME);
                    //sm.enterState(GAMING);
                    break;
                }
            }
        },
        
        HIGHSCORES(MENUS) {
            @Override
            void entry(StateMachine sm) throws Exception {
                gameController.enterHighscores();
            }
            
            void process(StateMachine sm, GameEvent e) throws Exception {
                switch (e) {
                case CHOOSE_MAIN_MENU:
                    sm.enterState(MAIN_MENU);
                    break;
                }
            }
        },
        
        ABOUT(MENUS) {
            
            @Override
            void entry(StateMachine sm) throws Exception {
                gameController.enterAboutMenu();
            }
            
            void process(StateMachine sm, GameEvent e) throws Exception {
                switch (e) {
                case CHOOSE_MAIN_MENU:
                    sm.enterState(MAIN_MENU);
                    break;
                }
            }
        },
        
        // GAME will be the parent of these states : GAMING, PAUSE, GAME_OVER, INTERMEZZO(S), END
        GAME(null) {
            
            @Override
            void entry(StateMachine sm) throws Exception {
                gameController.enterGaming();
            }
            
            void process(StateMachine sm, GameEvent e) throws Exception {
                switch (e) {
                case START_GAME:
                    sm.enterState(GAMING);
                    gameController.startGame();
                }
            }
        },
        
        GAMING(GAME) {
            
            void process(StateMachine sm, GameEvent e) throws Exception {
                
                switch(e) {
                
                case CALL_DOWN:
                    gameController.moveCurrentTeetrymino(Direction.DOWN);
                    break;
                    
                case CALL_UP:
                    gameController.moveCurrentTeetrymino(Direction.UP);
                    break;
                    
                case CALL_LEFT:
                    gameController.moveCurrentTeetrymino(Direction.LEFT);
                    break;
                    
                case CALL_RIGHT:
                    gameController.moveCurrentTeetrymino(Direction.RIGHT);
                    break;
                    
                case CALL_PAUSE:
                    sm.enterState(PAUSE);
                    break;
                    
                case CALL_ROTATE:
                    gameController.moveCurrentTeetrymino(Direction.UP);
                    break;
                    
                case LOSE:
                    sm.enterState(GAME_OVER);
                    break;
                    
                }
                
            }
        },
        
        PAUSE(GAME) {
            void process(StateMachine sm, GameEvent e) throws Exception {
                switch (e) {
                case CALL_PAUSE:
                    exit(sm);
                    sm.enterState(GAMING);
                    break;
                }
            }

            @Override
            void entry(StateMachine sm) throws Exception {
                gameController.enterPause();
            }

            @Override
            void exit(StateMachine sm) {
                try {
                    gameController.exitPause();
                } catch (Exception e) {
                    
                }
            }
        },
        
        GAME_OVER(GAME) {
            
            @Override
            void entry(StateMachine sm) throws Exception {
                gameController.enterGameOver();
            }

            void process(StateMachine sm, GameEvent e) {
                // TODO Auto-generated method stub
            }
        },
        
        INTERMEZZO_1(GAME) {
            void process(StateMachine sm, GameEvent e) {
                // TODO Auto-generated method stub
            }
        },
        
        INTERMEZZO_2(GAME) {
            void process(StateMachine sm, GameEvent e) {
                // TODO Auto-generated method stub
            }
        },
        
        INTERMEZZO_3(GAME) {
            void process(StateMachine sm, GameEvent e) {
                // TODO Auto-generated method stub
            }
        },
        
        END(GAME) {
            void process(StateMachine sm, GameEvent e) {
                // TODO Auto-generated method stub
            }
        }
        
        ;
        
        private GameState parent;
        
        private GameState(GameState state) {
            this.parent = state;
        }
        
        abstract void process(StateMachine sm, GameEvent e) throws Exception;
        
        void entry(StateMachine sm) throws Exception { }
        void exit(StateMachine sm) { }
        
    }
    
    private void enterState(GameState... states) throws Exception {
        
        System.out.println(states);
        
        for (GameState gameState : states) {
            gameState.entry(this);
        }
        
        currentState = states[states.length - 1];
        System.out.println("assign " + currentState);
    }

    public void setState(GameState mainMenu) {
        currentState = mainMenu;
    }

    public void start() throws Exception {
        currentState.process(this, null);
    }

    public void sendEvent(GameEvent gameEvent) throws Exception {
        currentState.process(this, gameEvent);
    }

}
