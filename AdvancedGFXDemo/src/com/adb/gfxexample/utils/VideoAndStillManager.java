package com.adb.gfxexample.utils;

import java.awt.Rectangle;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.davic.resources.ResourceClient;
import org.davic.resources.ResourceProxy;
import org.davic.resources.ResourceStatusEvent;
import org.davic.resources.ResourceStatusListener;
import org.havi.ui.HBackgroundConfiguration;
import org.havi.ui.HBackgroundDevice;
import org.havi.ui.HBackgroundImage;
import org.havi.ui.HConfigurationException;
import org.havi.ui.HPermissionDeniedException;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
import org.havi.ui.HScreen;
import org.havi.ui.HScreenConfigTemplate;
import org.havi.ui.HScreenRectangle;
import org.havi.ui.HStillImageBackgroundConfiguration;
import org.havi.ui.HVideoConfigTemplate;
import org.havi.ui.HVideoConfiguration;
import org.havi.ui.HVideoDevice;

/**
 * @author A.Skrzynski
 *
 */
public class VideoAndStillManager implements ResourceClient {

	private static final VideoAndStillManager instance = new VideoAndStillManager();
	/** Current still. */
	private HBackgroundImage iFrame;
	/** Reference to the still plane resource. */
//	private HBackgroundDevice backgroundDevice = HScreen.getDefaultHScreen().getDefaultHBackgroundDevice();
	/** Reference to the video plane resource. */
//	private HVideoDevice videoDevice = HScreen.getDefaultHScreen().getDefaultHVideoDevice();
	/** Video size. */
	private HScreenRectangle videoRectangle = new HScreenRectangle(0, 0, 0, 0);

	private Map stills = new HashMap();

	public static VideoAndStillManager getInstance() {
		return instance;
	}

	public void stopVideo() {
		playStopVideo(false, null);
	}

	public void playVideo(Rectangle videoBounds) {
		playStopVideo(true, videoBounds);
	}

	public HVideoConfiguration getCurrentVideoConf()
	{
	    return HScreen.getDefaultHScreen().getDefaultHVideoDevice().getCurrentConfiguration();
	}

	/**
	 * restore videoDevice;
	 */
	public void setConf(HVideoConfiguration videoConf)
    {
        if (videoConf != null)
        {
        	HScreen.getDefaultHScreen().getDefaultHVideoDevice().reserveDevice(this);
            try
            {
            	HScreen.getDefaultHScreen().getDefaultHVideoDevice().setVideoConfiguration(videoConf);
            }
            catch (HPermissionDeniedException e)
            {
                e.printStackTrace();
            }
            catch (HConfigurationException e)
            {
            	e.printStackTrace();
            }
        }
    }

	public void reSetupStill() {
		iFrame = null;
	}

	private void playStopVideo(final boolean play, final Rectangle videoBounds) {
//		System.out.println("[VideoStill] play video : " + play);
		if (play) {
			final HScene scene = HSceneFactory.getInstance().getDefaultHScene();
			System.out.println(" " + scene + " " + scene.getWidth() + "x" + scene.getHeight());
			final Rectangle scrDim = new Rectangle(1280, 720);//scene.getWidth(), scene.getHeight());
			videoRectangle = new HScreenRectangle(
					(float)videoBounds.x      / (float)scrDim.width, 
                    (float)videoBounds.y      / (float)scrDim.height,
                    (float)videoBounds.width  / (float)scrDim.width,
                    (float)videoBounds.height / (float)scrDim.height);
//			System.out.println("VIDEO RECT videoBounds : " + videoBounds);
//			System.out.println("VIDEO RECT x : " + ((float)videoBounds.x / (float)scrDim.width));
//			System.out.println("VIDEO RECT x : " + ((float)videoBounds.y / (float)scrDim.height));
//			System.out.println("VIDEO RECT x : " + ((float)videoBounds.width / (float)scrDim.width));
//			System.out.println("VIDEO RECT x : " + ((float)videoBounds.height / (float)scrDim.height));
		} else {
			videoRectangle  = new HScreenRectangle(0,0,0,0);
//			videoDevice.releaseDevice();
		}
		final HVideoDevice videoDevice = HScreen.getDefaultHScreen().getDefaultHVideoDevice();
		boolean reserved = videoDevice.reserveDevice(this);
		if (!reserved) {
			videoDevice.addResourceStatusEventListener(new ResourceStatusListener() {

				public void statusChanged(ResourceStatusEvent arg0) {
					playStopVideo(play, videoBounds);
					videoDevice.removeResourceStatusEventListener(this);
				}
			});
		} else {
//			System.out.println("reserved ::: " + reserved);
		    final HVideoConfigTemplate videoConfigTemplate = new HVideoConfigTemplate();
		    videoConfigTemplate.setPreference(
		            HScreenConfigTemplate.SCREEN_RECTANGLE,
		            videoRectangle,
		            HScreenConfigTemplate.REQUIRED);
		    final HVideoConfiguration videoConfiguration = HScreen.getDefaultHScreen().getDefaultHVideoDevice().getBestConfiguration(videoConfigTemplate);
	    	try {
	    		HScreen.getDefaultHScreen().getDefaultHVideoDevice().setVideoConfiguration(videoConfiguration);
			} catch (HPermissionDeniedException e) {
				e.printStackTrace();
			} catch (HConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	public void showStill(final HBackgroundImage iFrame) {
		if (this.iFrame != iFrame) {
			this.iFrame = iFrame;
			final HBackgroundDevice device = HScreen.getDefaultHScreen().getDefaultHBackgroundDevice();
			boolean reserved = device.reserveDevice(this);
			if (!reserved) {
				device.addResourceStatusEventListener(new ResourceStatusListener() {

					public void statusChanged(ResourceStatusEvent arg0) {
						showStill(iFrame);
						device.removeResourceStatusEventListener(this);
					}
				});
			} else {
	            final HBackgroundConfiguration backConf = HScreen.getDefaultHScreen().getDefaultHBackgroundDevice().getCurrentConfiguration();
	            if(backConf instanceof HStillImageBackgroundConfiguration) {
	            	final HStillImageBackgroundConfiguration canvas = (HStillImageBackgroundConfiguration) backConf;
	              	try {
//	            		System.out.println("[Still Manager] show still...");
	              		final HScreenRectangle hRect = new HScreenRectangle(0, 0, 1, 1);
	           			canvas.displayImage(iFrame, hRect);
	              	}
	              	catch(Exception e) {
	              		e.printStackTrace();
	              	}
	            }
			}
		}
	}

	public boolean isStillDisplayed() {
		return iFrame != null;
	}

	public void showStill(String iFrameName) {
		showStill(iFrameName, getClass().getClassLoader());
	}

	public void showStill(String iFrameName, ClassLoader cl) {
		HBackgroundImage still = (HBackgroundImage) stills.get(iFrameName);
		if (still == null) {
	        still = loadStill(iFrameName, cl);
	        stills.put(iFrameName, still);
		}
		showStill(still);
	}

    public HBackgroundImage loadStill(String name, ClassLoader cl) {
        HBackgroundImage iFrame = null;
        final URL bitmapURL = cl.getResource(name);
        if (bitmapURL != null) {
            iFrame = new HBackgroundImage(bitmapURL);
//            long t0 = System.currentTimeMillis();
//            iFrame.load(new HBackgroundImageListener() {
//				public void imageLoaded(HBackgroundImageEvent arg0) {
//					System.out.println("imageLoaded!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//				}
//
//				public void imageLoadFailed(HBackgroundImageEvent arg0) {
//					System.out.println("imageLoaded fail!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//				}
//            });
//            long t1 = System.currentTimeMillis();
//			System.out.println("after load took : " + (t1 - t0));
        }
        return iFrame;
    }

	public void hideStill() {
		if (iFrame != null) {
			iFrame = null;
//			showStill((HBackgroundImage) null);
			HScreen.getDefaultHScreen().getDefaultHBackgroundDevice().releaseDevice();
		}
	}

	public HVideoDevice getVideoDevice() {
		return HScreen.getDefaultHScreen().getDefaultHVideoDevice();
	}

	public void releasesDevices() {
		iFrame = null;
		HScreen.getDefaultHScreen().getDefaultHVideoDevice().releaseDevice();
		HScreen.getDefaultHScreen().getDefaultHBackgroundDevice().releaseDevice();
	}

	public boolean requestRelease(ResourceProxy arg0, Object arg1) {
		return true;
	}

	public void release(ResourceProxy arg0) {
		//System.out.println("[engine] release devices...");
	}

	public void notifyRelease(ResourceProxy arg0) {
		//System.out.println("[engine] notify release devices...");
	}

	private VideoAndStillManager() {
//		playStopVideo(false, null);
	}
}
