package frc.robot.subsystems;

import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ArduinoSubsystem extends SubsystemBase {

  I2C arduinoI2C; 
  //byte[] toSend = new byte[1];
  byte[] toGet = new byte[2];

  IntegerPublisher sDist = NetworkTableInstance.getDefault()
  .getIntegerTopic("/Vision/Dist1")
  .publish();

  public ArduinoSubsystem(){
    arduinoI2C = new I2C(Constants.I2C.NAVX_I2C,0x55);
  }

  public void periodic(){
    toGet[0] = 0x00;
    toGet[1] = 0x00;
    boolean aborted = arduinoI2C.readOnly(toGet, 2);

    if (aborted){
      sDist.set(-1);
    }
    else{
      int x = (toGet[0] & 0xff) | ((toGet[1] & 0xff) << 8);
      sDist.set(x);

      //sDist.set((int) toGet[0] & 0xffL);
    }
    //sDist.set(aborted ? -1 : (int) toGet[0]);
  }
}
