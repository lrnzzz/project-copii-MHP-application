package be.telenet;

import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import be.telenet.utils.version.TelenetVersionUtils;
import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;

/**
 *
 * @author fncnvt
 */
public class Main implements Xlet {

    private XletContext ctx;

    public void initXlet(XletContext ctx) throws XletStateChangeException {
    	System.out.println("initXlet");
        this.ctx = ctx;
       Engine.initialize();
        Engine.getInstance().setCtx(ctx);
    }

    public void startXlet() throws XletStateChangeException {
        new Thread(new Runnable() {

            public void run() {
                TelenetVersionUtils.getInstance(TelenetVersionUtils.initialize(ctx, Engine.appname)).reportVersion("1.0.2", "2011/07/08 14:00");
                Engine.getInstance().start();
                Engine.getInstance().logEvent("Application started");
            }
        }).start();
    }

    public void pauseXlet() {
        this.ctx.notifyPaused();
    }

    public void destroyXlet(boolean unconditional) throws XletStateChangeException {
        this.ctx.notifyDestroyed();
    }
}
