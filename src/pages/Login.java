package pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.Almoxarifado;

public class Login {
	
	BufferedImage tempustecLogo = Almoxarifado.imgManag.getProjectImage("Tempustec Logo Icone");
	int imgX = (Almoxarifado.WIDTH/2) - (tempustecLogo.getWidth()/2);
	int imgY = 30;
	int imgSize = 180;
	
	public boolean isWriting = false;
	
	public Login() {
		imgX = (Almoxarifado.WIDTH/2) - (imgSize/2);
	}
	
	public void writingOnCanvas(KeyEvent e) {
		
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.fillRect(0, 0, Almoxarifado.WIDTH, Almoxarifado.HEIGHT);
		g.setColor(Color.orange);
		g.fillRoundRect((Almoxarifado.WIDTH/5), 15, (Almoxarifado.WIDTH/5)*3, Almoxarifado.HEIGHT-30, 18, 18);
		g.setColor(Color.gray);
		g.fillRoundRect((Almoxarifado.WIDTH/5) + 8, 15 + 8, (Almoxarifado.WIDTH/5)*3 - 16, Almoxarifado.HEIGHT-30 - 16, 18, 18);
		g.drawImage(tempustecLogo, imgX, imgY, imgSize, imgSize, null);
		
		g.setColor(Color.white);
		g.fillRoundRect(Almoxarifado.WIDTH/8*2, Almoxarifado.HEIGHT/2, Almoxarifado.WIDTH/2, 40, 15, 15);
		g.fillRoundRect(Almoxarifado.WIDTH/8*2, Almoxarifado.HEIGHT/6*5 - 45, Almoxarifado.WIDTH/2, 40, 15, 15);
		g.setColor(Color.black);
		g.drawRoundRect(Almoxarifado.WIDTH/8*2, Almoxarifado.HEIGHT/2, Almoxarifado.WIDTH/2, 40, 15, 15);
		g.drawRoundRect(Almoxarifado.WIDTH/8*2, Almoxarifado.HEIGHT/6*5 - 45, Almoxarifado.WIDTH/2, 40, 15, 15);
	}
	
}
