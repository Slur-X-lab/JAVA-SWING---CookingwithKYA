import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

// Image Utilities
public class ImageUtils {
    public static ImageIcon loadAndScaleImage(String path, int width, int height) {
        if (path == null || path.isEmpty()) {
            return createPlaceholderImage(width, height);
        }

        try {
            File file = new File(path);
            if (!file.exists()) {
                return createPlaceholderImage(width, height);
            }

            BufferedImage originalImage = ImageIO.read(file);
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            return createPlaceholderImage(width, height);
        }
    }

    private static ImageIcon createPlaceholderImage(int width, int height) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(new Color(180, 180, 180));
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
        String text = "🍽️";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.dispose();
        return new ImageIcon(placeholder);
    }
}