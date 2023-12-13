package pages;

import java.awt.Color;
import java.awt.Graphics;

import main.Almoxarifado;

public class Archive {

	public Archive() {
		// TODO Auto-generated constructor stub
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 0));
		g.drawRect(0, 0, Almoxarifado.WIDTH, Almoxarifado.HEIGHT);
	}

}
