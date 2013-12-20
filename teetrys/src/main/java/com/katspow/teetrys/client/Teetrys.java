package com.katspow.teetrys.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.katspow.caatjagwt.client.CaatjaGwt;
import com.katspow.teetrys.client.core.GameController;

public class Teetrys implements EntryPoint {

    public void onModuleLoad() {
        
//        new Caatja(new CaatjaGwtDate(), new CaatjaGwtNavigator(), new CaatjaGwtWindow(), new CaatjaGwtRootPanel(),
//                new CaatjaGwtService(), new CaatjaGwtEventManager(), new CaatjaGwtImageLoader(),
//                new CaatjaGwtPreloader(), new CAATGwt());
        
        try {
            CaatjaGwt.init();
            new GameController().start();
        } catch (Exception e) {
            Window.alert("Could not load teetrys !");
            e.printStackTrace();
        }
        
    }

}
