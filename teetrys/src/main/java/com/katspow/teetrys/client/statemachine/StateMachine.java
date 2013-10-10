package com.katspow.teetrys.client.statemachine;

import com.google.gwt.event.logical.shared.HasInitializeHandlers;
import com.katspow.teetrys.client.core.GameController;

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
        
        LOSE
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
            void process(StateMachine sm, GameEvent e) {
                
            }
        },
        
        GAMING(GAME) {
            void process(StateMachine sm, GameEvent e) {
                // TODO Auto-generated method stub
            }
        },
        
        PAUSE(GAME) {
            void process(StateMachine sm, GameEvent e) {
                // TODO Auto-generated method stub
            }
        },
        
        GAME_OVER(GAME) {
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
        for (GameState gameState : states) {
            gameState.entry(this);
        }
        
        currentState = states[states.length - 1];
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
