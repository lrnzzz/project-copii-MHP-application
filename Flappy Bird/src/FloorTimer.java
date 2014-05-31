import java.util.TimerTask;
import java.awt.*;
import org.havi.ui.*;
import org.dvb.ui.*;
import java.awt.Image; 
import java.util.ArrayList;
import javax.tv.xlet.*;

public class FloorTimer extends TimerTask {
	FlappyB xlet;
    
    public void setXlet(FlappyB xl){
        xlet = xl;
    }
    
    public void run(){
        xlet.callback();
    }
}