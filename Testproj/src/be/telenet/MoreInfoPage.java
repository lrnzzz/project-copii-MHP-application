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

import java.awt.Toolkit;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author fncnvt
 */
public class MoreInfoPage extends NavigationPage implements NavigationActionListener {

    private int pageNumber = 1;
    private NavigationButton home;
    private NavigationButton more;
    private NavigationButton buy;
    private NavigationManager navmanager;
    private String backgroundLocation;

    public MoreInfoPage() {
        super();

        this.navmanager = new NavigationManager(Engine.appname, "navmanager_moreinfo");
        this.navmanager.initializeDefault();
        this.navmanager.createUserEventRepository("navmanager_moreinfo");

        this.navmanager.disableNavigationManager();

        this.home = new NavigationButton();
        this.buy = new NavigationButton();
        this.more = new NavigationButton();

        this.home.setBounds(170, 490, 162, 42);
        this.buy.setBounds(500, 490, 162, 42);
        this.more.setBounds(335, 490, 162, 42);

//        this.home.setNonfocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/prices_nofocus_" + Engine.getInstance().getLanguage() + ".jpg"));
//        this.home.setFocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/prices_focus_" + Engine.getInstance().getLanguage() + ".jpg"));
//        this.buy.setNonfocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/koop_nofocus_" + Engine.getInstance().getLanguage() + ".jpg"));
//        this.buy.setFocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/koop_focus_" + Engine.getInstance().getLanguage() + ".jpg"));
//        this.more.setNonfocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_nofocus_" + Engine.getInstance().getLanguage() + ".jpg"));
//        this.more.setFocusImage(Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_focus_" + Engine.getInstance().getLanguage() + ".jpg"));


        this.home.setNonfocusImage(Engine.pricesImageNoFocus);
        this.home.setFocusImage(Engine.pricesImageFocus);
        this.buy.setNonfocusImage(Engine.koopImageNoFocus);
        this.buy.setFocusImage(Engine.koopImageFocus);
        this.more.setNonfocusImage(Engine.moreInfoImageNoFocus);
        this.more.setFocusImage(Engine.moreInfoImageFocus);

        this.add(home);
        this.add(buy);
        this.add(more);

        this.addActionListener(this);
        this.home.addActionListener(this);
        this.buy.addActionListener(this);
        this.more.addActionListener(this);

        this.more.getFocus();

        this.navmanager.addNavigationItem(this);
        this.navmanager.addNavigationItem(this.home);
        this.navmanager.addNavigationItem(this.buy);
        this.navmanager.addNavigationItem(this.more);

        //this.looseFocus();

        //this.setBackgroundLocation(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_1_" + Engine.getInstance().getLanguage() + ".m2v");

        this.repaint();
        //Engine.getInstance().getNavigationManager().printNavigationItems();
    }

    public void setBackgroundLocation(String location) {
        this.backgroundLocation = location;
    }

    public void showPage() {
        this.pageNumber = 1;
        this.setBackgroundLocation(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_1_" + Engine.getInstance().getLanguage() + ".m2v");
        Engine.getInstance().getDeviceUtils().displayBackgroundImage(backgroundLocation);
        Engine.getInstance().getDeviceUtils().scaleVideoZero();
        this.setNavigationManager(this.navmanager);
        super.showPage();
    }

    public void navigationAction(NavigationItem source, int action) {
        Engine.getInstance().debug("Recieved action " + action + " for source: " + source);
        if (action == NavigationManager.KEY_OK) {
            if (source.equals(more)) {
                if (this.pageNumber == 1) {
                    Engine.getInstance().debug("Displaying page 2");
                    this.pageNumber = 2;
                    this.buy.getFocus();
                    this.more.looseFocus();
                    this.setBackgroundLocation(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_2_" + Engine.getInstance().getLanguage() + ".m2v");
                    Engine.getInstance().getDeviceUtils().displayBackgroundImage(backgroundLocation);
                    repaint();
                } else if (this.pageNumber == 2) {
                    Engine.getInstance().debug("Displaying page 1");
                    this.pageNumber = 1;
                    this.more.getFocus();
                    this.buy.looseFocus();
                    this.setBackgroundLocation(System.getProperty("user.dir") + "/be/telenet/resource/meerinfo_1_" + Engine.getInstance().getLanguage() + ".m2v");
                    Engine.getInstance().getDeviceUtils().displayBackgroundImage(backgroundLocation);
                    repaint();
                }
            } else if (source.equals(home)) {
                this.hidePage();
                this.getPreviousPage().showPage();
            } else if (source.equals(buy)) {
              /*  try {
                    Engine.getInstance().logEvent("Koop gedrukt");
                   Engine.getInstance().getTelenetAPI().showProgress(true,
                            Integer.parseInt(TelenetUtils.getORGID_APPID(Engine.getInstance().getCtx())[0], 16),
                            Integer.parseInt(TelenetUtils.getORGID_APPID(Engine.getInstance().getCtx())[1], 16));
                       
                    Engine.getInstance().getTelenetAPI().openTelenetPage(TelenetAPI.TELENETSHOPPAGE);
                    
                    
                    
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                } catch (NotBoundException ex) {
                    ex.printStackTrace();
                }*/
            }
        } else if (action == NavigationManager.KEY_ARROW_RIGHT) {
            Engine.getInstance().debug("Nav right on: " + source);
            this.navmanager.navigateRight(source);
        } else if (action == NavigationManager.KEY_ARROW_LEFT) {
            this.navmanager.navigateLeft(source);
        } else if (action == NavigationManager.KEY_BACK) {
            this.more.getFocus();
            this.buy.looseFocus();
            this.home.looseFocus();
            this.hidePage();
            this.getPreviousPage().showPage();
        }
    }
}
