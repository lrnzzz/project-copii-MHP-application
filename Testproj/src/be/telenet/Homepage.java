/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.telenet;

import be.telenet.utils.TelenetUtils;
import be.telenet.utils.navigation.NavigationActionListener;
import be.telenet.utils.navigation.NavigationButton;
import be.telenet.utils.navigation.NavigationItem;
import be.telenet.utils.navigation.NavigationManager;
import be.telenet.utils.navigation.NavigationPage;
//import com.zappware.ixc.TelenetAPI;
import java.awt.Color;
import java.awt.Toolkit;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author fncnvt
 */
public class Homepage extends NavigationPage implements NavigationActionListener {

    private String backgroundLocation = null;
    private NavigationButton meerinfo;
    private NavigationButton koop;
    private MoreInfoPage moreInfoPage;
    private NavigationManager navmanager;
    private Homepage me;

    public Homepage() {
        super();

        me = this;

        this.navmanager = new NavigationManager(Engine.appname, "navmanager_home");
        this.navmanager.initializeDefault();
        this.navmanager.createUserEventRepository("navmanager_home");

        this.koop = new NavigationButton();
        this.meerinfo = new NavigationButton();

        this.koop.setBounds(500, 490, 162, 42);
        this.meerinfo.setBounds(335, 490, 162, 42);

//        this.koop.setNonfocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/koop_nofocus_" + Engine.getInstance().getLanguage() + ".jpg"));
//        this.koop.setFocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/koop_focus_" + Engine.getInstance().getLanguage() + ".jpg"));
//        this.meerinfo.setNonfocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_nofocus_" + Engine.getInstance().getLanguage() + ".jpg"));
//        this.meerinfo.setFocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_focus_" + Engine.getInstance().getLanguage() + ".jpg"));

        this.koop.setNonfocusImage(Engine.koopImageNoFocus);
        this.koop.setFocusImage(Engine.koopImageFocus);
        this.meerinfo.setNonfocusImage(Engine.moreInfoImageNoFocus);
        this.meerinfo.setFocusImage(Engine.moreInfoImageFocus);

        this.add(koop);
        this.add(meerinfo);

        this.addActionListener(this);
        this.koop.addActionListener(this);
        this.meerinfo.addActionListener(this);

        this.koop.getFocus();

        this.navmanager.addNavigationItem(this);
        this.navmanager.addNavigationItem(this.koop);
        this.navmanager.addNavigationItem(this.meerinfo);

        new Thread(new Runnable() {

            public void run() {
                moreInfoPage = new MoreInfoPage();
                moreInfoPage.setHScene(Engine.getInstance().getHScene());
                moreInfoPage.setPreviousPage(me);
                moreInfoPage.hidePage();
            }
        }).start();

        this.repaint();
    }

    public void setBackgroundLocation(String location) {
        this.backgroundLocation = location;
    }

    public void showPage() {
        Engine.getInstance().getDeviceUtils().displayBackgroundImage(backgroundLocation);
        Engine.getInstance().getDeviceUtils().scaleVideoZero();
        this.setNavigationManager(this.navmanager);
        super.showPage();
    }

    public void navigationAction(NavigationItem source, int action) {
        Engine.getInstance().debug("Recieved action " + action + " for source: " + source);
        if (action == NavigationManager.KEY_OK) {
            if (source.equals(koop)) {
               // try {
                    Engine.getInstance().logEvent("Koop gedrukt");
          //          Engine.getInstance().getTelenetAPI().showProgress(true,
            //                Integer.parseInt(TelenetUtils.getORGID_APPID(Engine.getInstance().getCtx())[0], 16),
              //              Integer.parseInt(TelenetUtils.getORGID_APPID(Engine.getInstance().getCtx())[1], 16));
                //    Engine.getInstance().getTelenetAPI().openTelenetPage(TelenetAPI.TELENETSHOPPAGE);
             //   } catch (RemoteException ex) {
        //            ex.printStackTrace();
   //             } catch (NotBoundException ex) {
     //               ex.printStackTrace();
       //         }
            } 
                else if (source.equals(meerinfo)) {
                Engine.getInstance().logEvent("Meer info gedrukt");
                this.moreInfoPage.showPage();
                this.hidePage();
            }
        } else if (action == NavigationManager.KEY_BACK) {
        //    try {
         //       Engine.getInstance().getTelenetAPI().openTelenetPage(-1);
       //     } catch (RemoteException ex) {
      //          ex.printStackTrace();
       //     } catch (NotBoundException ex) {
        //        ex.printStackTrace();
       //     }
        } else if (action == NavigationManager.KEY_ARROW_DOWN) {
            this.navmanager.navigateDown(source);
        } else if (action == NavigationManager.KEY_ARROW_UP) {
            this.navmanager.navigateUp(source);
        } else if (action == NavigationManager.KEY_ARROW_LEFT) {
            this.navmanager.navigateLeft(source);
        } else if (action == NavigationManager.KEY_ARROW_RIGHT) {
            this.navmanager.navigateRight(source);
        }
    }
}
