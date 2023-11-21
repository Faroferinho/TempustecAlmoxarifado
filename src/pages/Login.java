package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Almoxarifado;

public class Login {
	
	public static String user;
	public static String password;
	public static int spaceWidth = Almoxarifado.WIDTH/4;
	public static int spaceHeight = Almoxarifado.HEIGHT - 50;
	
	public static String userIndicatorLable = "Registro de Funcionario:";
	public static String passwordIndicatorLable = "Senha:";
	
	public Login() {
		
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Almoxarifado.WIDTH, Almoxarifado.HEIGHT);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(Almoxarifado.WIDTH/2 - spaceWidth, 25, spaceWidth*2, spaceHeight, 40, 40);
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(Almoxarifado.WIDTH/2 - spaceWidth + 7, 25 + 7, spaceWidth*2 - 14, spaceHeight-14, 40, 40);
		
		g.setColor(Color.orange);
		g.fillRect(Almoxarifado.WIDTH/2 - 45, 25 + 14, 90, 80);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", 12, 0));
		g.drawString(userIndicatorLable, spaceWidth + 18, 200);
		
		
		
	}
	
}
