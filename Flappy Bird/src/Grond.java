import javax.tv.xlet.*;
import org.havi.ui.*;
import java.awt.*;
import org.dvb.ui.*;
import java.awt.Image; 

public class Grond extends HComponent{
    public static int x = 0, y = 0;
    private Image bmap; 
    private MediaTracker mtrack;
    
    public Grond(String bitmap, int x, int y){
        bmap = this.getToolkit().getImage("ground.png");
        mtrack = new MediaTracker(this);
        mtrack.addImage(bmap, 0);
        try{mtrack.waitForAll();}
        catch(Exception e){System.out.println(e.toString());}
        this.setBounds(x, y, bmap.getWidth(null), bmap.getHeight(null));
    }
    
    public void paint(Graphics g){
        g.drawImage(bmap, x, y, null);
    }

    public static void move(){
        if(x <= -14){
            x = 0;
        }
        x -= 2;
    }
}