import javax.tv.xlet.*;
import org.havi.ui.*;
import java.awt.*;
import org.dvb.ui.*;
import java.awt.Image; 
import java.util.Random;

public class Pipes extends Sprite{
    public int x = 0, y = 0;
    private Image bovenBuis, onderBuis;
    private int openingsGrootte = 90;
    FlappyB xlet;
    
    public Pipes(int x, int y){
        super(x, y);
        this.x = x; 
        this.y = y;
        bovenBuis = this.getToolkit().getImage("toppipe.png");
        onderBuis = this.getToolkit().getImage("bottompipe.png");
        bmap1 = bovenBuis;
        bmap2 = onderBuis;
        mtrack = new MediaTracker(this);
        try{mtrack.waitForAll();}
        catch(Exception e){System.out.println(e.toString());}
        this.setBounds(x, y, bmap1.getWidth(null), bmap1.getHeight(null) + openingsGrootte + bmap2.getHeight(null));
    }
    
    public void setXlet(FlappyB xl){
        xlet = xl;
    }
        
    public void paint(Graphics g){
        g.drawImage(bmap1, 0, 0, null);
        g.drawImage(bmap2, 0, bmap1.getHeight(null)+ openingsGrootte, null);
    }

    public void autoMove(){
        Random randomY = new Random(System.currentTimeMillis());
        x -= 2;
        this.setBounds(x, y, bmap1.getWidth(null), bmap1.getHeight(null) + openingsGrootte + bmap2.getHeight(null));
        if (x < -52){
            x = 720; y = -randomY.nextInt(305);
        }
    }
    
    public void collisionDetection(Flappy f){
        if (x <= 134 && x >= 49)
        {
            if ((f.y < y + bmap1.getHeight(null)) || (f.y + f.getH() > y + bmap1.getHeight(null) + openingsGrootte)){
                xlet.gameOver();
            }
        }
        if (x == 132){
            xlet.points();
        }
    }
}
