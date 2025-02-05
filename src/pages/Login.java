package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import javax.swing.JOptionPane;

import functions.Archiver;
import functions.DBConector;
import functions.Functions;
import main.Almoxarifado;
import main.UserInterface;

public class Login {
	//Instancia as variaveis de controle e interação do usuário.	
	boolean isOnTheRightState = false;
	
	public boolean mouseClick = false;
	
	//Instancia as variáveis de apresentar imagem.
	int imgWidth = 222;
	int imgHeight = 190;
	int imgX = (Almoxarifado.WIDTH/2) - (imgWidth/2);
	int imgY = 30;
	
	BufferedImage tempustecLogo = Almoxarifado.imgManag.getProjectImage("TempustecIconeLogo");
	Image tempustec = tempustecLogo.getScaledInstance(imgWidth, imgHeight, 16);
	
	//Instancia as variáveis dos campos de texto.
	int textBoxX = Almoxarifado.WIDTH/4;
	int textBoxY = Almoxarifado.HEIGHT/5*2;
	int textBoxW = Almoxarifado.WIDTH/2;
	int textBoxH = 30;
	
	Rectangle firstBox = new Rectangle(textBoxX, textBoxY + 48, textBoxW, textBoxH);
	Rectangle secondBox = new Rectangle(textBoxX, (int) (textBoxY * 1.6), textBoxW, textBoxH);
	
	//Instancia a imagem e posição do botão de login.
	BufferedImage loginBttn = Almoxarifado.imgManag.getSprite(0, 0, 215, 60);
	int bttnX = (Almoxarifado.WIDTH/2) - (loginBttn.getWidth()/2);
	int bttnY = Almoxarifado.HEIGHT/16*13;
	
	//Instancia as variáveis de controle das caixas de texto. 
	String textInBoxCPF = "";
	String textInBoxPW = "";
	public boolean isOnCPF = false;
	boolean isOnPW = false;
	public boolean isWriting = false;
	
	private boolean blink;
	private int blinkAux = 0;
	
	private boolean cursorMove = false;
	private boolean cursorUp = false;
	private boolean cursorDown = false;
	private boolean cursorLeft = false;
	private boolean cursorRight = false;
	private int cursorAuxPositioner = 0;
	private int cursorIndex = 0;
	
	/**
	 * Construtor da pagina de Login.
	 */
	public Login() {
		System.out.println("Carregou Login: " + LocalDateTime.now());
	}
	
	/**
	 * Método que verifica se é uma conta ou senha validos 
	 * e posteriormente define o perfil do usuário com base 
	 * no tipo após o login.
	 */
	private void submitForm() {
		// Pega os dados dos funcionarios do banco de dados.
		String rawRegisters = DBConector.readDB("CPF", "Funcionarios");
		String rawPasswords = DBConector.readDB("password", "Funcionarios");
		
		String[] registers = rawRegisters.split(" § \n");
		String[] passwords = rawPasswords.split(" § \n");
		
		// Verifica o valor da montagem está na lista de ambos a conta e senha.
		for(int i = 0; i < registers.length; i++) {
			if(textInBoxCPF.equals(registers[i]) && textInBoxPW.equals(passwords[i])) {
				// Coloca os dados do banco de dados na pagina adequada e muda o estado para login.
				String auxString = "";
				auxString = DBConector.readDB("*", "Funcionarios", "CPF", textInBoxCPF);
				String[] toConfig = auxString.split(" § ");
				
				if(toConfig[4].equals("1")) {
					Almoxarifado.userProfile = new Admnistrator(toConfig[0]);
				}else {
					Almoxarifado.userProfile = new Employee(toConfig[0]);
				}
				
				Archiver.writeOnArchive("login", null, null, null);
				Almoxarifado.setState(1);
				return;
			}
		}
		
		JOptionPane.showMessageDialog(null, "Conta e/ou Senha Incorretas", "Erro ao efetuar Login", JOptionPane.ERROR_MESSAGE);
		
	}
	
	/**
	 * Metodo de formatar o CPF para que o dado em sí 
	 * não seja formatado para o banco de dados, apenas 
	 * quando exibido.
	 * 
	 * @param CPF - O CPF em forma de texto para ser formatado
	 * @return CPF formatado na norma XXX.XXX.XXX-XX.
	 */
	private String cpfFormater(String CPF) {
		String toReturn = "";
		
		String firstPart = "";
		String secondPart = "";
		String thirdPart = "";
		String fourthPart = "";
		
		if(CPF.length() < 3) {
			toReturn = CPF;
		}else if(CPF.length() > 2 && CPF.length() < 6) {
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, CPF.length());
			
			toReturn = firstPart + "." + secondPart;
		}else if(CPF.length() > 5 && CPF.length() < 9) {
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, 6);
			thirdPart = CPF.substring(6, CPF.length());
			
			toReturn = firstPart + "." + secondPart + "." + thirdPart;
		}else if(CPF.length() > 8 && CPF.length() < 12) {
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, 6);
			thirdPart = CPF.substring(6, 9);
			fourthPart = CPF.substring(9, CPF.length());
			
			toReturn = firstPart + "." + secondPart + "." + thirdPart + "-" + fourthPart;
		}else {
			toReturn = CPF;
		}
		
		return toReturn;
	}
	
	/**
	 * Basicamente transforma a String da senha em uma 
	 * sequencia de asteristicos.
	 * 
	 * @param pw - Senha a ser formatada
	 * @return Conjunto de asteristicos do tamanho da senha real.
	 */
	private String censoringPassword(String pw) {
		String returnString = "";
		
		for(int i = 0; i < pw.length(); i++) {
			returnString += "*";
		}
		
		return returnString;
	}
	
	/**
	 * Adiciona o texto inserido do usuario as caixas de texto.
	 * 
	 * @param text - texto original.
	 * @param toAdd - texto a ser adicionado.
	 * @return O texto original intercalado com o texto novo.
	 */
	private String writer(String text, String toAdd) {
		String toReturn = "";
		
		if(cursorIndex == 0) {
			if(!toAdd.equals("DELETE")) {
				toReturn = text + toAdd;
			}else {
				toReturn = text.substring(0, text.length()-1);
			}
		}else {
			if(!toAdd.equals("DELETE")) {
				String beforeCursor = text.substring(0, text.length() - cursorIndex);
				String afterCursor = text.substring(text.length() - cursorIndex, text.length());
				
				toReturn = beforeCursor + toAdd + afterCursor;
			}else {
				String beforeCursor = text.substring(0, text.length() - cursorIndex);
				String afterCursor = text.substring(text.length() - cursorIndex+1, text.length());
				
				toReturn = beforeCursor + afterCursor;
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Metodo para gerar o texto novo inserido pelo usuário,
	 * algumas teclas geram um comportamento especial, como 
	 * alterar a posição do cursor.
	 * 
	 * @param e - Evento do teclado pressionado.
	 */
	public void writingOnCanvas(KeyEvent e) {
		if(isOnCPF) {
			if((e.getKeyCode() > 47 && e.getKeyCode() < 58) || (e.getKeyCode() > 95 && e.getKeyCode() < 106)) {
				textInBoxCPF = writer(cpfFormater(textInBoxCPF), "" + e.getKeyChar());
				textInBoxCPF = textInBoxCPF.replaceAll("[.-]", "");
				
			} else {
				if(textInBoxCPF.length() > 0 && e.getKeyCode() == 8) {
					textInBoxCPF = writer(textInBoxCPF, "DELETE");
				}
			}
			
			if(textInBoxCPF.length() > 11) {
				textInBoxCPF = textInBoxCPF.substring(0, textInBoxCPF.length()-1);
			}
		}else if(isOnPW) {
			if((e.getKeyCode() > 20 && e.getKeyCode() < 144) && (e.getKeyCode() != 37 && e.getKeyCode() != 38 
			&& e.getKeyCode() != 39 && e.getKeyCode() != 40)) {
				textInBoxPW = writer(textInBoxPW, "" + e.getKeyChar());;
			} else {
				if(textInBoxPW.length() > 0 && e.getKeyCode() == 8) {
					textInBoxPW = writer(textInBoxPW, "DELETE");
				}
		
			}
			
			if(textInBoxPW.length() > 30) {
				textInBoxPW = textInBoxPW.substring(0, textInBoxPW.length()-1);
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_TAB) {
			if(isOnCPF) {
				isOnPW = true;
				isOnCPF = false;
			}else {
				isOnCPF = true;
				isOnPW = false;
			}
		}
		
		if(e.getKeyCode() > 36 && e.getKeyCode() < 41) {
			cursorMove = true;
			
			switch(e.getKeyCode()) {
			case 37:
				cursorLeft = true;
				break;
			case 38:
				cursorUp = true;
				break;
			case 39:
				cursorRight = true;
				break;
			case 40:
				cursorDown = true;
				break;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			submitForm();
		}
		
	}
	
	/**
	 * Verifica se o estado é valido e então aguarda pelo usuário
	 * interagir com o sistema. Com os campos preenchidos ele efetua
	 * o login e verifica se está ou não valido as creedenciais do 
	 * usuário. Após a validação o sistema muda para o perfil do 
	 * usuário.
	 */
	public void tick() {
		Almoxarifado.frame.setTitle("Almoxarifado - Login");
		
		if(Almoxarifado.getState() == Almoxarifado.loginState) {
			isOnTheRightState = true;
		}else {
			isOnTheRightState = false;
		}
		
		if(isOnTheRightState) {
		
			if(mouseClick) {
				if(Almoxarifado.mX > textBoxX && Almoxarifado.mX < textBoxX + textBoxW) {
					if(Almoxarifado.mY > textBoxY + 44 && Almoxarifado.mY < textBoxY + textBoxH + 49) {
						isWriting = true;
						isOnCPF = true;
						isOnPW = false;
						cursorAuxPositioner = 0;
						cursorIndex = 0;
					}else if(Almoxarifado.mY > (int) (textBoxY * 1.6) && Almoxarifado.mY < (int) (textBoxY * 1.6) + textBoxH) {
						isWriting = true;
						isOnCPF = false;
						isOnPW = true;
						cursorAuxPositioner = 0;
						cursorIndex = 0;
					}else {
						isWriting = false;
						cursorAuxPositioner = 0;
						cursorIndex = 0;
					}
				}
				
				if(Almoxarifado.mX > bttnX && Almoxarifado.mX < bttnX + loginBttn.getWidth() 
				&& Almoxarifado.mY > bttnY && Almoxarifado.mY < bttnY + loginBttn.getHeight()) {
					submitForm();
				}
			}
			
		}
	}

	/**
	 * Desenha a tela de login e as caixas de texto, junto com o texto 
	 * que os acompanha.
	 * 
	 * @param g - Metodo de desenho da janela.
	 */
	public void render(Graphics g) {
		g.fillRect(0, 0, Almoxarifado.WIDTH, Almoxarifado.HEIGHT);
		g.setColor(new Color(126, 126, 126));
		g.fillRoundRect((Almoxarifado.WIDTH/5), 15, (Almoxarifado.WIDTH/5)*3, Almoxarifado.HEIGHT-30, 45, 45);
		g.setColor(new Color(195, 195, 195));
		g.fillRoundRect((Almoxarifado.WIDTH/5) + 15, 15 + 15, (Almoxarifado.WIDTH/5)*3 - 30, Almoxarifado.HEIGHT-30 - 30, 15, 15);
		g.drawImage(tempustec, imgX, imgY, null);
		
		
		g.setColor(new Color(126, 126, 126));
		g.fillRoundRect(textBoxX - 10, textBoxY, (Almoxarifado.WIDTH / 2) + 20, 5, 5, 5);
		
		UserInterface.createTextBox(g, firstBox, 15);
		UserInterface.createTextBox(g, secondBox, 15);
		
		g.setColor(Color.white);
		g.setFont(new Font("Times New Roman", Font.BOLD, 35));
		Almoxarifado.drawStringBorder(g, "Inicie Sessão", (Almoxarifado.WIDTH/2) - (g.getFontMetrics().stringWidth("Inicie Sessão")/2), textBoxY - 20, 1, Color.DARK_GRAY, Color.white);
		g.setFont(new Font("Times New Roman", 0, 20));
		Almoxarifado.drawStringBorder(g, "CPF:", textBoxX + 15, textBoxY + 44, 1, Color.DARK_GRAY, Color.white);
		Almoxarifado.drawStringBorder(g, "Senha:", textBoxX + 15, (int) (textBoxY * 1.6) - 3, 1, Color.DARK_GRAY, Color.white);
		
		g.setFont(new Font("Times New Roman", 0, 14));
		g.setColor(Color.DARK_GRAY);
		if(Functions.isOnBox(secondBox.x  + secondBox.width - (g.getFontMetrics().stringWidth("Cadastre-se") + 15), 
							 secondBox.y + secondBox.height + 5,
							 g.getFontMetrics().stringWidth("Cadastre-se"), 
							 g.getFontMetrics().getHeight())) {
			g.setColor(new Color(235, 245, 178));
			if(mouseClick) {
				Almoxarifado.addWorker.SignIn = true;
				Almoxarifado.setState(8);
			}
		}
		g.drawString("Cadastre-se", secondBox.x  + secondBox.width - (g.getFontMetrics().stringWidth("Cadastre-se") + 15), secondBox.y + secondBox.height + g.getFontMetrics().getHeight());
		
		g.setColor(Color.black);		
		g.setFont(new Font("segoe ui", 0, 16));
		g.drawString(cpfFormater(textInBoxCPF), textBoxX + 5, textBoxY + textBoxH - (g.getFontMetrics().getHeight()/2) + 49);
		g.drawString(censoringPassword(textInBoxPW), textBoxX + 5, (int) (textBoxY * 1.6) + textBoxH - (g.getFontMetrics().getHeight()/4));
		
		blinkAux++;
		
		if(blinkAux%23 == 0) {
			if(blink) {
				blink = false;
			}else {
				blink = true;
			}
		}
		
		if(cursorMove) {
			if(isOnCPF) {
				if(cursorUp) {
					cursorAuxPositioner = g.getFontMetrics().stringWidth(cpfFormater(textInBoxCPF));
					cursorIndex = cpfFormater(textInBoxCPF).length();
					
					cursorUp = false;
				}else if(cursorDown) {
					cursorAuxPositioner = 0;
					cursorIndex = 0;
					
					cursorDown = false;
				}else if(cursorRight) {
					if(cursorIndex > 0) {
						cursorIndex--;
						cursorAuxPositioner = g.getFontMetrics().stringWidth(cpfFormater(textInBoxCPF).substring(cpfFormater(textInBoxCPF).length() - cursorIndex, cpfFormater(textInBoxCPF).length()));
					}
					
					cursorRight = false;
				}else if(cursorLeft) {
					if(cursorIndex < cpfFormater(textInBoxCPF).length()) {
						cursorIndex++;
						cursorAuxPositioner = g.getFontMetrics().stringWidth(cpfFormater(textInBoxCPF).substring(cpfFormater(textInBoxCPF).length() - cursorIndex, cpfFormater(textInBoxCPF).length()));
					}
					
					cursorLeft = false;
				}
			}else if(isOnPW) {
				if(cursorUp) {
					cursorAuxPositioner = g.getFontMetrics().stringWidth(censoringPassword(textInBoxPW));
					cursorIndex = textInBoxPW.length();
					
					cursorUp = false;
				}else if(cursorDown) {
					cursorAuxPositioner = 0;
					cursorIndex = 0;
					
					cursorDown = false;
				}else if(cursorRight) {
					if(cursorIndex > 0) {
						cursorIndex--;
						cursorAuxPositioner = g.getFontMetrics().stringWidth(censoringPassword(textInBoxPW).substring(textInBoxPW.length() - cursorIndex, textInBoxPW.length()));
					}
					
					cursorRight = false;
				}else if(cursorLeft) {
					if(cursorIndex < textInBoxPW.length()) {
						cursorIndex++;
						cursorAuxPositioner = g.getFontMetrics().stringWidth(censoringPassword(textInBoxPW).substring(textInBoxPW.length() - cursorIndex, textInBoxPW.length()));
					}
					
					cursorLeft = false;
				}
			}
		}
		cursorMove = false;
		
		if(blink && isWriting) {			
			if(isOnCPF) {
				g.fillRect(g.getFontMetrics().stringWidth(cpfFormater(textInBoxCPF)) - cursorAuxPositioner + textBoxX + 5, textBoxY + 5 + 49, 2, 18);
			}else if(isOnPW) {
				g.fillRect(g.getFontMetrics().stringWidth(censoringPassword(textInBoxPW)) - cursorAuxPositioner + textBoxX + 5, (int) (textBoxY * 1.6) + 5, 2, 18);
			}
		}
		
		g.drawImage(loginBttn, bttnX, bttnY, null);
		UserInterface.isOnBigButton(g, bttnX, bttnY);
	}
	
}
