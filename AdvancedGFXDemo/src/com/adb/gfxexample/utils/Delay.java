/*
 * Delay.java    $Revision: 1.1.2.2 $    $Date: 2010-03-01 11:07:24 $    T.Mlynarczyk
 *
 * Copyright (c) 2007 OSMOSYS, S.A.
 * All Rights Reserved.
 */
package com.adb.gfxexample.utils;


/**
* Delay useful for delaying and reduce to one multiple task call.
* 
*/
public class Delay implements Runnable
{

   /**
    * delay thread priority.
    */
   private static final int PRIORITY = Thread.MAX_PRIORITY - 1;

   /**
    * delay in millisecond.
    */
   private long delay;

   /**
    * task to execute past time defined in delay.
    */
   private DoIt doIt;

   /**
    * flag for ending thread loop.
    */
   private boolean end;

   /**
    * Thread.
    */
   private Thread th;

   /**
    * if is true first delay is half reduced.
    */
   private boolean firstFast;

   /**
    * lock object for synchronization.
    */
   private Object repleaceLoc = new Object();

   private int maxDelay = -1;
   
   public long lastWakeUp = -1;

   /**
    * Constructs a Delay object.
    * @param i
    *            delay in milliseconds.
    */
   public Delay(long i)
   {
       delay = i;
       firstFast = true;
       startThread();
   }

   /**
    * Constructs a Delay object.
    * @param i
    *            delay in milliseconds.
    * @param firstFast
    *            if true first delay is half reduced
    */
   public Delay(long i, boolean firstFast)
   {
       delay = i;
       this.firstFast = firstFast;
       startThread();
   }

   public Delay(int i, boolean firstFast, int maxDelay)
   {
       this(i, firstFast);
       this.maxDelay = maxDelay;
   }

private void startThread()
   {
       th = new Thread(this, "Delay " + delay + "ms " + firstFast);
       th.setPriority(PRIORITY);
       th.start();
   }

   /**
    * set new task and delay start from beginning.
    * 
    * @param it
    *            new task to execute
    */
   public void use(DoIt it)
   {
       synchronized (repleaceLoc)
       {
           doIt = it;
       }
       if (th.isAlive())
       {
    	   synchronized (th)
           {
    		   th.notify();
           }
       }
       else
       {
           th.start();
       }
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Runnable#run()
    */
   public void run()
   {
       boolean breaked;
       long sleepTime;
       int counter = 0;

       while (!end)
       {
           counter++;
           sleepTime = delay;
           breaked = false;
           try
           {
               if (doIt == null)
               {
                   if (firstFast)
                   {
                       sleepTime >>= 1;
                   }
                   synchronized (th)
                   {
                       th.wait();
                   }
                   lastWakeUp = currentUptimeMillis();
               }
           }
           catch (InterruptedException e)
           {
               breaked = true;
           }

           try
           {
               long t1 = currentUptimeMillis();
               synchronized (th)
               {
            	   th.wait(sleepTime);
               }
               if ((currentUptimeMillis() - t1) < sleepTime)
               {
                   breaked = true;
               }
           }
           catch (Throwable e)
           {
               breaked = true;
           }
           if (!breaked ||
        	   (maxDelay != -1 && (currentUptimeMillis() - lastWakeUp) > maxDelay))
           {
               DoIt tmpDoIt = null;
               synchronized (repleaceLoc)
               {
                   tmpDoIt = doIt;
                   doIt = null;
               }

               if (tmpDoIt != null)
               {
                   try
                   {
                       lastWakeUp = currentUptimeMillis();
                       tmpDoIt.doIt();
                   }
                   catch (Exception e)
                   {
                           e.printStackTrace();
                   }
               }
           }
       }
   }

   /**
    * end tread.
    */
   public void endLoop()
   {
       end = true;
       doIt = null;
       th.interrupt();
   }
   
   protected long currentUptimeMillis()
   {
       return System.currentTimeMillis();
   }

}