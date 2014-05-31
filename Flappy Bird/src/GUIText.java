import javax.tv.xlet.*;
import org.havi.ui.*;
import java.awt.*;
import org.dvb.ui.*;
import java.awt.Image; 

public class GUIText extends HComponent{
    public Image bmap0, bmap1, bmap2;
    protected MediaTracker mtrack;
    public int scoreNumbers = 1;
    private boolean numbers = false;
    
    public GUIText(String desc, boolean score){
        numbers = score;
        if(numbers){
            bmap0 = this.getToolkit().getImage("numbers/0.png");
            bmap1 = this.getToolkit().getImage("numbers/0.png");
            bmap2 = this.getToolkit().getImage("numbers/0.png");
        }
        else{
            bmap0 = this.getToolkit().getImage(desc);
        }

        mtrack = new MediaTracker(this);
        mtrack.addImage(bmap0, 0);
        mtrack.addImage(bmap1, 0);
        mtrack.addImage(bmap2, 0);
        try{mtrack.waitForAll();}
        catch(Exception e){System.out.println(e.toString());}
        
        if(desc.equals("gameover.png")){
            this.setBounds(360-(bmap0.getWidth(null)/2), 220, bmap0.getWidth(null), bmap0.getHeight(null));
        }
        else if(desc.equals("flappybird.png")){
            this.setBounds(360-(bmap0.getWidth(null)/2), 150, bmap0.getWidth(null), bmap0.getHeight(null));
        }
        else if(desc.equals("numbers/0.png")){
            this.setBounds(360-(bmap0.getWidth(null)/2), 100, bmap0.getWidth(null), bmap0.getHeight(null));
        }
    }
    
    public void paint(Graphics g){
        if(numbers){
            if(scoreNumbers == 1) {
                g.drawImage(bmap2, 0, 0, null);
            }
            else if(scoreNumbers == 2) {
                g.drawImage(bmap1, 0, 0, null);
                g.drawImage(bmap2, bmap1.getWidth(this), 0, null);
            }
            else if(scoreNumbers == 3) {
                g.drawImage(bmap0, 0, 0, null);
                g.drawImage(bmap1, bmap0.getWidth(this), 0, null);
                g.drawImage(bmap2, bmap0.getWidth(this) + bmap1.getWidth(this), 0, null);
            }
        }
        else{
            g.drawImage(bmap0, 0, 0, null);
        }
    }
}