package com.katspow.teetrys.client.core;

import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.ImageActor;

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
    
//    Gui.addImage('pause', 170, 300, @scene)

}
