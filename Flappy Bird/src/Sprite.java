import javax.tv.xlet.*;
import org.havi.ui.*;
import java.awt.*;
import org.dvb.ui.*;
import java.awt.Image; 

public class Sprite  extends HComponent{
    public  int x = 0, y = 0;
    protected Image bmap1;
    protected Image bmap2;
    protected MediaTracker mtrack;
    
    public Sprite(int x, int y){
    }
    
    public void paint(Graphics g){
        g.drawImage(bmap1, 0, 0, null);
    }

    public  void Move(boolean up, int speed){
        if(up && y > -20){
            y -= speed;
            
        }
        else if(!up){
            y += speed;
        }
        this.setBounds(x, y, bmap1.getWidth(null), bmap1.getHeight(null));
    }
    
    public void autoMove(){}
}