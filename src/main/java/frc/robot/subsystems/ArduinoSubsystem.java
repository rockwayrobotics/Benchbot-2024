package frc.robot.subsystems;

import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTablesJNI;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ArduinoSubsystem extends SubsystemBase {
  
  public class I2SQUEED extends Thread {
    int dist = 0; 
    

    public void run(){
      int count = 0; 
      I2C arduinoI2C = new I2C(Constants.I2C.NAVX_I2C,0x55);
      //byte[] toSend = new byte[1];
      byte[] toGet = new byte[2];
      
      while(true){
        
        toGet[0] = 0x00;
        toGet[1] = 0x00;

    
        // about 167 us 
        boolean aborted = arduinoI2C.readOnly(toGet, 2);

        if (aborted){
          dist = -1;
        }
        else{
          int x = (toGet[0] & 0xff) | ((toGet[1] & 0xff) << 8);
          dist = x; 
          //sDist.set((int) toGet[0] & 0xffL);
        }
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
         // e.printStackTrace();
        }
        //sDist.set(aborted ? -1 : (int) toGet[0]);
      }
    }
  }

  IntegerPublisher sDist = NetworkTableInstance.getDefault()
  .getIntegerTopic("/Vision/Dist1")
  .publish();

  I2SQUEED squeed;

  public ArduinoSubsystem(){
    squeed = new I2SQUEED();
    squeed.start();
  }

  public void periodic(){
   long timing = NetworkTablesJNI.now();

   sDist.set(squeed.dist); 

   long elapsed = NetworkTablesJNI.now() - timing; 
   //System.out.println(elapsed);
  
  }
}
