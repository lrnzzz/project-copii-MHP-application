import javax.tv.xlet.*;
import org.havi.ui.*;
import java.awt.*;
import org.dvb.ui.*;
import java.awt.Image; 

public class Flappy extends Sprite {
    int teller = 0, vertraag = 0;
    protected Image op, midden, neer;
    FlappyB xlet;
    public int getH(){
        return 24;
    }
    
    public Flappy(int x, int y){
        super(x, y);
        this.x = x; this.y = y;
        midden = this.getToolkit().getImage("flappy/midden.png");
        op = this.getToolkit().getImage("flappy/op.png");
        neer = this.getToolkit().getImage("flappy/neer.png");
        bmap1 = midden;
        mtrack = new MediaTracker(this);
        mtrack.addImage(bmap1, 0);
        try{mtrack.waitForAll();}
        catch(Exception e){System.out.println(e.toString());}
        this.setBounds(x, y, bmap1.getWidth(null), bmap1.getHeight(null));
    }
    
    public void setXlet(FlappyB xl){
        xlet = xl;
    }
    
    public void autoMove(){
        vertraag++;
        if (vertraag > 5){
            flap(); 
            vertraag = 0;
        }
        if(y >= 439){
            xlet.gameOver();
        }
    }
    
    public void flap(){
        teller++;
        if(teller > 3) teller = 0;
        if(teller == 0) bmap1 = op;
        if(teller == 1) bmap1 = midden;
        if(teller == 2) bmap1 = neer;
        if(teller == 3) bmap1 = midden;
    }
}