/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.telenet;

import be.telenet.scene.TelenetSceneUtils;
import be.telenet.utils.device.TelenetDeviceUtils;
import com.zappware.ixc.TelenetAPI;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import javax.tv.xlet.XletContext;
import org.dvb.io.ixc.IxcRegistry;
import org.havi.ui.HScene;

/**
 *
 * @author fncnvt
 */
public class Engine {

    public static final String appname = "Sporting Telenet Infopage";
    private static Engine engine = null;
    private XletContext ctx;
    private Homepage homepage;
    private HScene hscene;
    private TelenetSceneUtils sceneutils;
    private TelenetDeviceUtils devutils;
    private String language;
    private Properties settings;
    public static Image koopImageFocus;
    public static Image koopImageNoFocus;
    public static Image moreInfoImageFocus;
    public static Image moreInfoImageNoFocus;
    public static Image pricesImageFocus;
    public static Image pricesImageNoFocus;

    public static void initialize() {
        engine = new Engine();
    }

    public static Engine getInstance() {
        return engine;
    }

    private Engine() {
        debug("initialize()");
    }

    public void start() {
        debug("Start()");
        loadProperties();
        logEvent("Application loading");
        this.sceneutils = TelenetSceneUtils.getInstance(TelenetSceneUtils.initialize(ctx, appname));
        this.hscene = this.sceneutils.getScene();
        this.devutils = TelenetDeviceUtils.getInstance(TelenetDeviceUtils.initialize(ctx, appname));
        determineLanguage();
        loadImages();
        this.homepage = new Homepage();
        this.homepage.setHScene(hscene);
        this.homepage.setBackgroundLocation(System.getProperty("user.dir") + "/be/telenet/resource/homepage_" + this.language + ".m2v");
        this.homepage.showPage();
        this.hscene.setVisible(true);
    }

    private void loadProperties() {
        this.settings = new Properties();
        try {
            this.settings.load(this.getClass().getResourceAsStream("/be/telenet/resource/settings.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadImages() {
        Engine.pricesImageNoFocus = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/prices_nofocus_" + Engine.getInstance().getLanguage() + ".jpg");
        Engine.pricesImageFocus = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/prices_focus_" + Engine.getInstance().getLanguage() + ".jpg");
        Engine.koopImageNoFocus = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/koop_nofocus_" + Engine.getInstance().getLanguage() + ".jpg");
        Engine.koopImageFocus = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/koop_focus_" + Engine.getInstance().getLanguage() + ".jpg");
        Engine.moreInfoImageNoFocus = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_nofocus_" + Engine.getInstance().getLanguage() + ".jpg");
        Engine.moreInfoImageFocus = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_focus_" + Engine.getInstance().getLanguage() + ".jpg");

        Engine.pricesImageFocus.getWidth(null);
        Engine.pricesImageNoFocus.getWidth(null);
        Engine.koopImageNoFocus.getWidth(null);
        Engine.koopImageFocus.getWidth(null);
        Engine.moreInfoImageNoFocus.getWidth(null);
        Engine.moreInfoImageFocus.getWidth(null);

        File file = new File(this.getClass().getResource("/be/telenet/resource/meerinfo_1_" + Engine.getInstance().getLanguage() + ".m2v").getPath());
        file.exists();
        file = new File(this.getClass().getResource("/be/telenet/resource/meerinfo_2_" + Engine.getInstance().getLanguage() + ".m2v").getPath());
        file.exists();
    }

    public String getProperty(String key) {
        return this.settings.getProperty(key);
    }

    public HScene getHScene() {
        return hscene;
    }

    private void determineLanguage() {
        if (getProperty("language.detection.on").equalsIgnoreCase("false")) {
            debug("Language detection disabled");
            this.language = "nld";
        } else {
            try {
                debug("Language detection enabled");
                this.language = getTelenetAPI().getOnScreenLanguage();
                debug("Using language: " + this.language);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            } catch (NotBoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getLanguage() {
        return language;
    }

    public XletContext getCtx() {
        return ctx;
    }

    public void setCtx(XletContext ctx) {
        this.ctx = ctx;
    }

    public TelenetAPI getTelenetAPI() throws NotBoundException, RemoteException {
        TelenetAPI api = (TelenetAPI) IxcRegistry.lookup(this.ctx, "/1/1/telenet");

        return api;
    }

    public TelenetDeviceUtils getDeviceUtils() {
        return this.devutils;
    }

    public void debug(Object object) {
        System.out.println(appname + " :: " + object.toString());
    }

    public void logEvent(String text) {
        try {
            debug("Creating eventlog: " + text);
            getTelenetAPI().logEvent(getProperty("application.providerid"), Engine.appname, text, null);
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
}
