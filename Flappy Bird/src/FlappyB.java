import javax.tv.xlet.*;
import org.havi.ui.*;
import org.davic.resources.*;
import org.havi.ui.event.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import org.dvb.event.*;

public class FlappyB implements Xlet, ResourceClient, HBackgroundImageListener, UserEventListener{
    private XletContext actueleXletContext;
    public static HScene scene;
    private boolean debug = true;
    private HScreen screen;
    private HBackgroundDevice bgDevice;
    private HBackgroundConfigTemplate bgTemplate;
    private HStillImageBackgroundConfiguration bgConfiguration;
    private HBackgroundImage agrondimg = new HBackgroundImage("bg.png");
    private Grond ground;
    private Pipes buizen1, buizen2, buizen3;
    private Timer timer = new Timer();
    private FloorTimer objGameTimer;
    private Flappy bird;
    private GUIText txtGameOver, txtHero, txtPoints;
    private Random randomY = new Random();
    private ArrayList lijst = new ArrayList();
    private boolean running = false;
    public int Score = 0;
    
    public void callback(){
        Grond.move();
        Object[] a = lijst.toArray();
        for (int i = 0; i < a.length; i++){
            ((Sprite)a[i]).autoMove();
            if(i != 0){
                ((Pipes)a[i]).collisionDetection(bird);
            }
        }
        FlappyB.scene.repaint();
    }
    
    public void gameOver(){
        objGameTimer.cancel();
        running = false;
        txtGameOver.setVisible(true);
    }
    
    public void points(){
        Score++;
        int eenheden = Score % 10;
        int tientallen = ((Score % 100) - eenheden) / 10;
        int honderdtallen = (Score - eenheden - tientallen) / 100;
        String eenhedenImage = "numbers/" + eenheden + ".png";
        String tientallenImage = "numbers/" + tientallen + ".png";
        String honderdtallenImage = "numbers/" + honderdtallen + ".png";
        txtPoints.bmap0 = txtPoints.getToolkit().getImage(honderdtallenImage);
        txtPoints.bmap1 = txtPoints.getToolkit().getImage(tientallenImage);
        txtPoints.bmap2 = txtPoints.getToolkit().getImage(eenhedenImage);
        if(Score < 10){
            txtPoints.scoreNumbers = 1;
            txtPoints.setBounds((360 - (txtPoints.getWidth() / 2)), 100, 14, 20);
        }
        else if(Score >= 10 && Score < 100){
            txtPoints.scoreNumbers = 2;
            txtPoints.setBounds((360 - (txtPoints.getWidth() / 2)), 100, 28, 20);
        }
        else if(Score >= 100){
            txtPoints.scoreNumbers = 3;
            txtPoints.setBounds((360 - (txtPoints.getWidth() / 2)), 100, 42, 20);
        }
    }
    
    public void restart(){
        if(!running){
            Score = 0;
            txtPoints.scoreNumbers = 1;
            txtPoints.setBounds(360, 100, txtPoints.bmap1.getWidth(null), txtPoints.bmap1.getHeight(null));
            txtPoints.bmap2 = txtPoints.getToolkit().getImage("");
            objGameTimer = new FloorTimer();
            objGameTimer.setXlet(this);
            buizen1.setLocation(720, -randomY.nextInt(305));
            buizen2.setLocation(976, -randomY.nextInt(305));
            buizen3.setLocation(1234, -randomY.nextInt(305));
            buizen1.x = 720; buizen1.y = -randomY.nextInt(305);
            buizen2.x = 976; buizen1.y = -randomY.nextInt(305);
            buizen3.x = 1234; buizen1.y = -randomY.nextInt(305);
            buizen1.x = 720; buizen1.y = -randomY.nextInt(305);
            buizen2.x = 976; buizen1.y = -randomY.nextInt(305);
            buizen3.x = 1234; buizen1.y = -randomY.nextInt(305);
            bird.y = 240;
            bird.setLocation(100, 240);
            txtGameOver.setVisible(false);
            txtHero.setVisible(false);
            timer.scheduleAtFixedRate(objGameTimer, 0, 20);
            running = true;
        }
    }
    
    public void register(Sprite s){
        lijst.add(s);
    }
        
    public void initXlet(XletContext context){
        if(debug){System.out.println("Xlet Initialiseren");}
        this.actueleXletContext = context;
        HSceneTemplate sceneTemplate = new HSceneTemplate();
        sceneTemplate.setPreference(HSceneTemplate.SCENE_SCREEN_DIMENSION, new HScreenDimension(1.0f,1.0f),HSceneTemplate.REQUIRED);
        sceneTemplate.setPreference(HSceneTemplate.SCENE_SCREEN_LOCATION, new HScreenPoint(0.0f,0.0f), HSceneTemplate.REQUIRED);
        scene = HSceneFactory.getInstance().getBestScene(sceneTemplate);
        screen = HScreen.getDefaultHScreen();
        bgDevice = screen.getDefaultHBackgroundDevice();
        if(bgDevice.reserveDevice(this)){System.out.println("BackgroundImage device has been reserved");}
        else{System.out.println("Background image device cannot be reserved");}
        bgTemplate = new HBackgroundConfigTemplate();
        bgTemplate.setPreference(HBackgroundConfigTemplate.STILL_IMAGE, HBackgroundConfigTemplate.REQUIRED);
        bgConfiguration = (HStillImageBackgroundConfiguration)bgDevice.getBestConfiguration(bgTemplate);
        try{ bgDevice.setBackgroundConfiguration(bgConfiguration); }
        catch(Exception e){ System.out.println(e.toString()); }
        EventManager manager = EventManager.getInstance();
        UserEventRepository repository = new UserEventRepository("Voorbeeld");
        ground = new Grond("ground.png", 0, 463);
        buizen1 = new Pipes(720, -randomY.nextInt(305));
        buizen2 = new Pipes(976, -randomY.nextInt(305));
        buizen3 = new Pipes(1234, -randomY.nextInt(305));
        bird = new Flappy(100, 240);
        txtGameOver = new GUIText("gameover.png", false);
        txtHero = new GUIText("flappybird.png", false);
        txtPoints = new GUIText("", true);
        scene.add(txtGameOver);
        scene.add(txtHero);
        scene.add(txtPoints);
        txtGameOver.setVisible(false);
        scene.add(bird);
        scene.add(ground);
        scene.add(buizen1);
        scene.add(buizen2);
        scene.add(buizen3);
        repository.addKey(org.havi.ui.event.HRcEvent.VK_UP);
        repository.addKey(org.havi.ui.event.HRcEvent.VK_DOWN);
        repository.addKey(org.havi.ui.event.HRcEvent.VK_ACCEPT);
        repository.addKey(org.havi.ui.event.HRcEvent.VK_0);
        repository.addKey(org.havi.ui.event.HRcEvent.VK_SPACE);
        
        manager.addUserEventListener(this, repository);
    }
    
    public void userEventReceived(org.dvb.event.UserEvent e){
        if(e.getType() == KeyEvent.KEY_PRESSED){
            int speed = 8;
            switch(e.getCode()){
                case HRcEvent.VK_UP:
                    if(running) bird.Move(true, speed);
                    break;
                case HRcEvent.VK_DOWN:
                    if(running) bird.Move(false, speed);
                    break;
                case HRcEvent.VK_ACCEPT:
                    restart();
                    break;
                case HRcEvent.VK_0:
                    restart();
                    break;
                case HRcEvent.VK_SPACE:
                    restart();
                    break;
            }
            scene.repaint();
        }    
    }
    
    public void startXlet(){
        System.out.println("startXlet");
        agrondimg.load(this);
        scene.validate();
        scene.setVisible(true);
        buizen1.setXlet(this);
        buizen2.setXlet(this);
        buizen3.setXlet(this);
        bird.setXlet(this);
        register(bird);
        register(buizen1);
        register(buizen2);
        register(buizen3);
        objGameTimer = new FloorTimer();
        objGameTimer.setXlet(this);
    }
    
    public void pauseXlet(){System.out.println("pauseXlet");}
    public void destroyXlet(boolean unconditional){agrondimg.flush();}
    private Object getToolkit(){throw new UnsupportedOperationException("Not yet implemented");}
    public void notifyRelease(ResourceProxy proxy){}
    public void release(ResourceProxy proxy){}
    public boolean requestRelease(ResourceProxy proxy, Object requestData){return false;}
    public void imageLoaded(HBackgroundImageEvent e){
        try{bgConfiguration.displayImage(agrondimg);}
        catch(Exception s){System.out.println(s.toString());}
    }
    public void imageLoadFailed(HBackgroundImageEvent e){System.out.println("Image kan niet geladen worden.");}
}