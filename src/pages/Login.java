package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;

import main.Almoxarifado;

public class Login {
	
	int borderLeft = Almoxarifado.WIDTH/4;
	int borderTop = 24;
	
	public int insertInfo = 0;
	public boolean click = false;
	
	public String userID = "";
	public String userPassword = "";

	public Login() {
		
	}
	
	public void tick() {
		if(Almoxarifado.state == 0){
			if(click) {
				System.out.println("Clique");
				click = false;
			}
		}
	}
	
	
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Almoxarifado.WIDTH, Almoxarifado.HEIGHT);
		
		g.setColor(new Color(226, 163, 0));
		g.fillRoundRect(borderLeft-12, 12, borderLeft*2 + 24, Almoxarifado.HEIGHT-24*3, 23, 23);
		
		
		g.setColor(Color.red);
		g.fillRoundRect(borderLeft, 24, borderLeft*2, Almoxarifado.HEIGHT-24*4, 16, 16);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", 1, 16));
		g.drawString("Conta: ", borderLeft+25, Almoxarifado.HEIGHT/3);
		g.drawString("Senha: ", borderLeft+25, Almoxarifado.HEIGHT/3*2 - 50);
		
		g.fillRoundRect(borderLeft + 20, Almoxarifado.HEIGHT/3 + 10, borderLeft*2 - 40, 40, 6, 6);
		g.fillRoundRect(borderLeft + 20, Almoxarifado.HEIGHT/3*2 - 40, borderLeft*2 - 40, 40, 6, 6);
		
		g.setColor(new Color(226, 163, 0));
		g.fillRect(Almoxarifado.WIDTH/2-50, Almoxarifado.HEIGHT - (48 * 3) - 16, 100, 60);
		g.setColor(new Color(226, 255, 0));
		
		g.drawRect(Almoxarifado.WIDTH/2-50, Almoxarifado.HEIGHT - (48 * 3) - 16, 100, 60);
		
		g.setColor(Color.black);
		g.drawRoundRect(borderLeft + 20, Almoxarifado.HEIGHT/3 + 10, borderLeft*2 - 40, 40, 6, 6);
		g.drawRoundRect(borderLeft + 20, Almoxarifado.HEIGHT/3*2 - 40, borderLeft*2 - 40, 40, 6, 6);
		
		g.drawString(userID, borderLeft + 25, Almoxarifado.HEIGHT/3 + 37);
		g.drawString(userPassword, borderLeft + 25, Almoxarifado.HEIGHT/3*2  - 14);
		
		g.drawImage(Almoxarifado.imgManag.getSprite(640-64*2, 64*3, 128, 128), Almoxarifado.WIDTH/2 - 64, 40, 128, 128, null);
	}
	
}
