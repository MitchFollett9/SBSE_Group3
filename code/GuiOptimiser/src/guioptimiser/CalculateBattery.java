package guioptimiser;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CalculateBattery {

    // folowing are all in mAh    
    static float redpower = (float)120;
    static float bluepower = (float)240;
    static float greenpower = (float)140;
    static float blackpower = (float)60;

    static float calculatePixelmAh(int R, int G, int B) {
        return ((R*redpower + B*bluepower + G*greenpower) - blackpower)/(float)3686400;
      }
    public static float calculateChargeConsumptionPerPixel(String pathname) {
        try {
            File file = new File(pathname);
            if (!file.exists()) {
                System.out.println("Image file NOT FOUND at: " + file.getAbsolutePath());
            } else if (!file.canRead()) {
                System.out.println("No READ perms for file at: " + file.getAbsolutePath());
            } else {
                System.out.println("Loading valid image file from: " + file.getAbsolutePath());
            }
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
                    totalPower = totalPower + calculatePixelmAh(red, green, blue);
                }
            return totalPower;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
