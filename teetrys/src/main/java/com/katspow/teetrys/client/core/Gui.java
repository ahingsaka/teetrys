package com.katspow.teetrys.client.core;

import java.util.Arrays;

import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.ImageActor;
import com.katspow.caatja.foundation.actor.SpriteActor;
import com.katspow.caatja.foundation.image.CompoundImage;

public class Gui {
    
    public enum Labels {
        SCORE("score"), LINES("lines"), LEVEL("level"), NEXT("next"), PAUSE("pause"), QUIT("quit"), SLEEP("sleep");
        
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
    
    private static CompoundImage compoundImage;
    
    public static void createCompoundImage(CaatjaImage image, int x, int y) {
        compoundImage = new CompoundImage().
                initialize(image, x, y);
    }
    
//    createCompoundImage: (imageName, x, y) ->
//    @compoundImage = new CAAT.CompoundImage().
//        initialize(MOCAAT.director.getImage(imageName), x, y)
    
    public static ImageActor addImage(double x, double y, Labels labels, Scene scene, Director director) throws Exception {
        ImageActor image = new ImageActor().setImage(director.getImage(labels.getLabel())).setLocation(x, y);
        
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
    
    public static void createScoreDigits(Scene scene, Integer value) throws Exception {
      createNumberSprite(332, 35, scene, null);
      createNumberSprite(359, 35, scene, null);
      createNumberSprite(386, 35, scene, null);
      createNumberSprite(413, 35, scene, null);
      createNumberSprite(440, 35, scene, null);
    }
    
//    createScoreDigits: (scene) ->
//    @score_digits.push(this.createNumberSprite(332, 35, scene))
//    @score_digits.push(this.createNumberSprite(359, 35, scene))
//    @score_digits.push(this.createNumberSprite(386, 35, scene))
//    @score_digits.push(this.createNumberSprite(413, 35, scene))
//    @score_digits.push(this.createNumberSprite(440, 35, scene))

}
