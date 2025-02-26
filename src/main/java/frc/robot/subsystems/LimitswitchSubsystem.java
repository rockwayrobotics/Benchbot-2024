package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LimitswitchSubsystem extends SubsystemBase{
  
  DigitalInput m_limitswitch;
  DigitalInput m_colourmark;
  DigitalInput m_beambreak;

  ShuffleboardTab digital = Shuffleboard.getTab("Digital");

    GenericEntry limitswitchValue =
      digital.add("Limitswitch ;3", false)
      .getEntry();

    GenericEntry colourmarkValue = 
      digital.add("Colourmark :3",false)
      .getEntry();

    GenericEntry beamBreakSensor = 
      digital.add("Beam Break Sensor >:3", false)
      .getEntry(); 


  public LimitswitchSubsystem(){
    m_limitswitch = new DigitalInput(Constants.Digital.LIMITSWITCH);
    m_colourmark = new DigitalInput(Constants.Digital.COLOURMARK);
    m_beambreak = new DigitalInput(Constants.Digital.BEAMBREAK);
  }

  
  public void periodic(){
    limitswitchValue.setBoolean(m_limitswitch.get());
    colourmarkValue.setBoolean(m_colourmark.get()); 
    beamBreakSensor.setBoolean(m_beambreak.get());
  }
}