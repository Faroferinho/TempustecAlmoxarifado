package functions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ImageManager {
	
	private BufferedImage spritesheet;
	
	public BufferedImage TempustecIcon;
	
	public ImageManager(String imgFile) {
		try {
			spritesheet = ImageIO.read(new File("res/" + imgFile + ".png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Não Existe a SpriteSheet");
			e.printStackTrace();
		}
		
		TempustecIcon = this.getProjectImage("TempustecLogoIcone1");
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) {
		
		BufferedImage sprite = spritesheet.getSubimage(x, y, width, height);
		
		return sprite;
		
	}
	
	public BufferedImage getProjectImage(String fileName) {
		BufferedImage returnImg = null;
		
		try {
			returnImg = ImageIO.read(new File("res/" + fileName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnImg;
	}
	
}
