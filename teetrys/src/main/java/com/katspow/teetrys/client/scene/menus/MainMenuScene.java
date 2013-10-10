package com.katspow.teetrys.client.scene.menus;

import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.foundation.ui.TextActor;
import com.katspow.teetrys.client.core.GameController;
import com.katspow.teetrys.client.statemachine.StateMachine.GameEvent;

public class MainMenuScene extends Scene {
    
    private Director director;
    private ActorContainer root;
    
    public enum MenuLink {
        TEETRYS("Teetrys"), START("Start"), SCORES("Scores"), ABOUT("About");
        
        private String label;

        private MenuLink(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }

    public MainMenuScene(Director director) throws Exception {
        this.director = director;
        
        root = new ActorContainer();
        root.setBounds(0, 0, director.canvas.getCoordinateSpaceWidth(), director.canvas.getCoordinateSpaceHeight());
        root.setFillStrokeStyle(CaatjaColor.valueOf("grey"));
        
        addChild(root);
        
        createMenuLinks();
    }

    private void createMenuLinks() throws Exception {
        addChild(createMenuLink(MenuLink.TEETRYS, 200));
        addChild(createMenuLink(MenuLink.START, 330));
        addChild(createMenuLink(MenuLink.SCORES, 400));
        addChild(createMenuLink(MenuLink.ABOUT, 470));
    }
    
    private TextActor createMenuLink(final MenuLink ml, double y) {
        TextActor textActor = new TextActor() {
            @Override
            public void mouseClick(CAATMouseEvent mouseEvent) throws Exception {
                switch (ml) {
                
                case START:
                    GameController.sendEvent(GameEvent.CHOOSE_GAME);
                    break;
                    
                case SCORES:
                    GameController.sendEvent(GameEvent.CHOOSE_HIGHSCORES);
                    break;
                    
                case ABOUT:
                    GameController.sendEvent(GameEvent.CHOOSE_ABOUT);
                    break;
                
                case TEETRYS:
                default:
                    break;
                
                }
                
            }

            @Override
            public void mouseEnter(CAATMouseEvent mouseEvent) {
                if (ml != MenuLink.TEETRYS) {
                    Actor actor = mouseEvent.source;
                    actor.setScale(1.2, 1.2);
                }
            }

            @Override
            public void mouseExit(CAATMouseEvent mouseEvent) {
                if (ml != MenuLink.TEETRYS) {
                    Actor actor = mouseEvent.source;
                    actor.setScale(1, 1);
                }
            }

            @Override
            public void mouseDrag(CAATMouseEvent mouseEvent) {
                // FIXME Caatja
            }
            
        };
                textActor.setFont("50px sans-serif").
                setText(ml.getLabel()).
                calcTextSize(director).
                setTextFillStyle("white").
                disableDrag();
        
        textActor.setLocation((GameController.GAME_WIDTH - textActor.width) / 2, y);
        textActor.cacheAsBitmap();
        
        return textActor;
    }

}
