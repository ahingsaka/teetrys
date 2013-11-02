package com.katspow.teetrys.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatjagwt.client.CAATGwt;
import com.katspow.caatjagwt.client.CaatjaGwtDate;
import com.katspow.caatjagwt.client.CaatjaGwtEventManager;
import com.katspow.caatjagwt.client.CaatjaGwtImageLoader;
import com.katspow.caatjagwt.client.CaatjaGwtNavigator;
import com.katspow.caatjagwt.client.CaatjaGwtPreloader;
import com.katspow.caatjagwt.client.CaatjaGwtRootPanel;
import com.katspow.caatjagwt.client.CaatjaGwtService;
import com.katspow.caatjagwt.client.CaatjaGwtWindow;
import com.katspow.teetrys.client.core.GameController;

/**
 * FIXME CAATJA - setScene + getSceneIndex
 * FIXME CAATJA - Passage des interpolators en statique ?
 * FIXME CAATJA - ZOrder
 * FIXME CAATJA - les double Double dans les behaviors
 * FIXME Throws exception everywhere !!!
 * 
 * TODO
 * 
 * Gestion vitesse - a tester -
 * 
 * End + score
 * Gestion touch
 * Quit
 * 
 * about
 * bug zorder
 * Game Over
 * 
 * degrade gauche et droite colonnes noires
 * 
 * @author asamsenesena
 *
 */
public class Teetrys implements EntryPoint {

    public void onModuleLoad() {
        
        new Caatja(new CaatjaGwtDate(), new CaatjaGwtNavigator(), new CaatjaGwtWindow(), new CaatjaGwtRootPanel(),
                new CaatjaGwtService(), new CaatjaGwtEventManager(), new CaatjaGwtImageLoader(),
                new CaatjaGwtPreloader(), new CAATGwt());
        
        try {
            new GameController().start();
        } catch (Exception e) {
            Window.alert("Could not load teetrys !");
            e.printStackTrace();
        }
        
    }

}
