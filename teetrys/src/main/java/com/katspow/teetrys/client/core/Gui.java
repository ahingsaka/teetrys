package com.katspow.teetrys.client.core;

import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.ImageActor;

public class Gui {
    
    public enum Labels {
        SCORE("score"), LINES("lines"), LEVEL("level"), NEXT("next"), PAUSE("pause"), QUIT("quit"), SLEEP("sleep");
        
        private String label;

        private Labels(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
        
    }
    
    public static void addImage(double x, double y, Labels labels, Scene scene, Director director) throws Exception {
        
        ImageActor imageActor = new ImageActor().
                setImage(director.getImage(labels.getLabel())).
                setLocation(x, y);
        
        scene.addChild(imageActor);
        scene.setZOrder(imageActor, Integer.MAX_VALUE);
        
    }

}
