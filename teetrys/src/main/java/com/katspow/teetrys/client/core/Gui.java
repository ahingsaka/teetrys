package com.katspow.teetrys.client.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.MouseListener;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.ImageActor;
import com.katspow.caatja.foundation.actor.SpriteActor;
import com.katspow.caatja.foundation.image.CompoundImage;
import com.katspow.teetrys.client.statemachine.StateMachine.GameEvent;

public class Gui {
    
    public enum Labels {
        SCORE("score"), LINES("lines"), LEVEL("level"), NEXT("next"), PAUSE("pause"), QUIT("quit"), SLEEP("sleep"), NUMBERS("numbers"), GAME_OVER("gameover"), OK("ok"), CANCEL("cancel"), EXIT("exit");
        
        private String label;
        
        private ImageActor img;

        private Labels(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
        
        public ImageActor getImg() {
            return img;
        }
        
        public void setImg(ImageActor img) {
            this.img = img;
        }
        
    }
    
    /**
     * Init all digits (score, level, lines) 
     * 
     * @param scene
     * @throws Exception
     */
    public static void createNumbers(Scene scene) throws Exception {
        createScoreDigits(scene);
        createLinesDigits(scene);
        createLevelDigits(scene);
    }
    
    public static void refreshScores() {
        refreshDigits(4, Score.getScore());
        refreshDigits(7, Score.getLines());
        refreshDigits(9, Score.getLevel());
    }
    
    private static void refreshDigits(int digits, int value) {
        if (value > 0) {
            // Why is there an empty string at the start of the table ?
            String[] tmp = String.valueOf(value).split("");
            digits -= tmp.length - 1;
            digits += 1;
            
            for (String val : tmp) {
                if (!val.isEmpty()) {
                    SpriteActor spriteActor = scoreDigits.get(digits);
                    spriteActor.setAnimationImageIndex(Arrays.asList(Integer.valueOf(val)));
                    digits += 1;
                }
            }
            
        }
    }
    
    private static CompoundImage compoundImage;
    
    public static void createCompoundImage(CaatjaImage image, int rows, int cols) {
        compoundImage = new CompoundImage().
                initialize(image, rows, cols);
    }
    
    public static ImageActor addImage(double x, double y, final Labels labels, Scene scene, Director director) throws Exception {
        ImageActor image = new ImageActor().setImage(director.getImage(labels.getLabel())).setLocation(x, y);
        
        image.setMouseClickListener(new MouseListener() {
            public void call(CAATMouseEvent e) throws Exception {
                if (labels == Labels.SLEEP) {
                	GameController.sendEvent(GameEvent.CALL_PAUSE);
                    
                } else if (labels == Labels.GAME_OVER) {
                    GameController.sendEvent(GameEvent.CALL_QUIT);
                }
            }
        });
        
        image.disableDrag();
        
        scene.addChild(image);
        scene.setZOrder(image, Integer.MAX_VALUE);
        
        labels.setImg(image);

        return image;
    }
    
    public static void hideImage(Labels label) {
        label.getImg().setDiscardable(true);
        label.getImg().setExpired(true);
    }
    
    public static SpriteActor createNumberSprite(double x, double y, Scene scene, Integer value) throws Exception {
        
        SpriteActor sa = new SpriteActor().
                setAnimationImageIndex(Arrays.asList(0)).
                setSpriteImage(compoundImage).
                setLocation(x, y);
        
        if (value != null) {
            sa.setAnimationImageIndex(Arrays.asList(value));
        }
        
        scene.addChild(sa);
        
        return sa;
    }
    
    private static List<SpriteActor> scoreDigits = new ArrayList<SpriteActor>();

    private static void createScoreDigits(Scene scene) throws Exception {
        scoreDigits.add(createNumberSprite(332, 35, scene, null));
        scoreDigits.add(createNumberSprite(359, 35, scene, null));
        scoreDigits.add(createNumberSprite(386, 35, scene, null));
        scoreDigits.add(createNumberSprite(413, 35, scene, null));
        scoreDigits.add(createNumberSprite(440, 35, scene, null));
    }

    private static void createLinesDigits(Scene scene) throws Exception {
        scoreDigits.add(createNumberSprite(386, 136, scene, null));
        scoreDigits.add(createNumberSprite(413, 136, scene, null));
        scoreDigits.add(createNumberSprite(440, 136, scene, null));
    }

    private static void createLevelDigits(Scene scene) throws Exception {
        scoreDigits.add(createNumberSprite(413, 225, scene, null));
        scoreDigits.add(createNumberSprite(440, 225, scene, null));
    }
    
}
