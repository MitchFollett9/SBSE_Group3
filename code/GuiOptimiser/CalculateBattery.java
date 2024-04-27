package code.GuiOptimiser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class CalculateBattery {
    
    static float redpower = (float)120/(float)3686400;

    static float bluepower = (float)240/(float)3686400;

    static float greenpower = (float)140/(float)3686400;
    // static int 
    static float calculatePixel(int R, int G, int B) { // 10-3
        // code to be executed
        return (R*redpower + B*bluepower + G*greenpower);
      }
    public static void main(String[] args) {
        try {
            System.out.println(redpower); 
            File file = new File("SBSE_Group3/code/GuiOptimiser/SS1.PNG");
            if (!file.exists()) {
                System.out.println("Image file NOT FOUND at: " + file.getAbsolutePath());
            } else if (!file.canRead()) {
                System.out.println("No READ perms for file at: " + file.getAbsolutePath());
            } else {
                System.out.println("Loading valid image file from: " + file.getAbsolutePath());
            }
            System.out.println(file); 
            BufferedImage image = ImageIO.read(file);
            
            // Get image width and height
            int w = image.getWidth();
            int h = image.getHeight();

            float totalPower = 0;

            for( int i = 0; i < w; i++ )
                for( int j = 0; j < h; j++ )
                {
                    int clr = image.getRGB( i, j );
                    int red =   (clr & 0x00ff0000) >> 16;
                    int green = (clr & 0x0000ff00) >> 8;
                    int blue =   clr & 0x000000ff;
                    totalPower = totalPower + calculatePixel(red, green, blue);
                }
            System.out.println(totalPower * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}