package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.LED.modes;

import java.nio.file.Path;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class ZonaLedSubsystem extends SubsystemBase{

  private AddressableLED m_led;
  private AddressableLEDBuffer m_ledBuffer;

  private int counter = 0; 
  private int pacer = 1; 

  private int row = 0;

  BufferedImage image;

  public ZonaLedSubsystem(){
    String jerma = "/home/lvuser/deploy/images/badapple.png";

    m_led = new AddressableLED(Constants.LED.LED_PWM);
    m_ledBuffer = new AddressableLEDBuffer(Constants.LED.LED_LENGTH);
  
    m_led.setLength(m_ledBuffer.getLength());
    m_led.setData(m_ledBuffer);
    m_led.start();

    try {
      image = ImageIO.read(new File (jerma));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void periodic(){
    if (++counter >= pacer){
      counter = 0; 

      for (var i = 0; i < m_ledBuffer.getLength(); i++) {
        int pixel = image.getRGB(i, row);
        int r = (pixel >> 16) & 0xff; 
        int g = (pixel >> 8) & 0xff;
        int b = (pixel >> 0) & 0xff; 
        m_ledBuffer.setRGB(i, r, g, b);
      }

      m_led.setData(m_ledBuffer);
      if (++row >= image.getHeight()){
        row = 0; 
      }
    }
  }
}

  