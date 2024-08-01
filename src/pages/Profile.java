package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import functions.DBConector;
import functions.Functions;
import main.Almoxarifado;

public abstract class Profile {
	//Inicia as variáveis importantes do banco de dados e para desenhar a montagem.
	protected String rdf = "";
	protected String name = "";
	protected String CPF = "";
	private String type = "";
	
	private Color rdfColor = Color.white;
	private Color nameColor = Color.white;
	private Color cpfColor = Color.white;
	
	protected BufferedImage bttn_changePW = Almoxarifado.imgManag.getSprite(475, 120, 165, 60);
	protected BufferedImage bttn_editInfo = Almoxarifado.imgManag.getSprite(475, 0, 165, 60);
	
	private int tryoutCounter = 0;
	
	public boolean mouseStatus = false;
	
	public boolean isEditing = false;
	
	/**
	 * Interface que pega os valores do banco de dados de registro, nome e CPF do usuário.
	 * 
	 * @param RdF - Id do funcionario.
	 */
	public Profile(String RdF) {
		rdf = RdF;
		name = DBConector.readDB("name", "funcionarios", "RdF", RdF).replaceAll(" § \n", "");
		CPF = DBConector.readDB("CPF", "funcionarios", "RdF", RdF).replaceAll(" § \n", "");
	}
	
	//Encapsulamento das variaveis escenciais.
	public String getName() {
		return name;
	}

	public String getRdF() {
		return rdf;
	}
	
	public String getCPF() {
		return CPF;
	}
	
	public String getType() {
		return type;
	}
	
	/**
	 * Função que altera os dados de senha do usuário após uma confirmação 
	 * da senha do usuário, caso a verificação falhe o usuário ainda tem 2 
	 * chances de confirmar sua identidade.
	 */
	protected void changePassword() {
		// Variável para confirmar a senha.
		String textInserted = "";
		
		// Dá mais 2 chances para o usuário.
		if(tryoutCounter < 3) {
			textInserted += JOptionPane.showInputDialog(null, "Confirme com a sua senha atual", "Confirmação de Edição de Senha", JOptionPane.WARNING_MESSAGE);
		}else {
			JOptionPane.showMessageDialog(null, "Contate o Suporte Técnico", "Limite de Senhas Incorretas Atingidas", JOptionPane.ERROR_MESSAGE);
		}
		
		// Verifica se o texto é nulo de alguma forma.
		if(Functions.emptyString(textInserted)) {
			return;
		}
		
		// verifica se o tamanho de tryoutCounter for menor que 3 ele repete a verificação da senha.
		while(tryoutCounter < 3) {
			if(textInserted.equals(DBConector.readDB("password", "funcionarios", "RdF", rdf).replaceAll(" § \n", ""))) {
				// Se a senha estiver correta verifica se a nova senha está vazia, caso não atualiza a senha.
				tryoutCounter = 0;
				
				String newPW = "" + JOptionPane.showInputDialog(null, "Insira uma nova senha", "Nova Senha", JOptionPane.PLAIN_MESSAGE);
				
				if(Functions.emptyString(newPW)) {
					return;
				}
				DBConector.writeDB("Funcionarios", "password", newPW, "RdF", rdf);
				break;
			}else{
				JOptionPane.showMessageDialog(null, "Tente Novamente, você possui " + (3 - tryoutCounter) + " restantes", "Senha Inserida Incorreta", JOptionPane.ERROR_MESSAGE);
				
				textInserted = "" + JOptionPane.showInputDialog(null, "Confirme com a sua senha atual", "Confirmação de Edição de Senha", JOptionPane.WARNING_MESSAGE);
				
				if(Functions.emptyString(textInserted)) {
					return;
				}
				
				tryoutCounter++;
			}
		}
	}
	
	/**
	 * Esse método verifica se está no modo de edição e delimita a área dos textos. 
	 * Caso o usuário esteja por cima destas áreas ele sinaliza para o usuário e 
	 * checa se é clicado, e neste caso ele altera os valores novos dos respectivos 
	 * campos. 	
	 */
	protected void editInfo() {
		if(isEditing) {
			if(Almoxarifado.mX > 80 && Almoxarifado.mX < 600) {
				if(Almoxarifado.mY > 140 && Almoxarifado.mY < 190) {
					nameColor = Color.gray;
					
					rdfColor = Color.white;
					cpfColor = Color.white;
					
					if(mouseStatus) {
						String newName = "" + JOptionPane.showInputDialog(null, "Insira novo Nome", "Alterar Perfil", JOptionPane.PLAIN_MESSAGE);
						alterName(newName);
					}
					
				}else if(Almoxarifado.mY > 220 && Almoxarifado.mY < 250){
					rdfColor = Color.gray;
					
					nameColor = Color.white;
					cpfColor = Color.white;

					if(mouseStatus) {
						String newRdF = "" + JOptionPane.showInputDialog(null, "Insira novo RdF", "Alterar Perfil", JOptionPane.PLAIN_MESSAGE);
						alterRdF(newRdF);
					}
					
				}else if(Almoxarifado.mY > 280 && Almoxarifado.mY < 310) {
					cpfColor = Color.gray;
					
					rdfColor = Color.white;
					nameColor = Color.white;
					
					if(mouseStatus) {
						String newCPF = "" + JOptionPane.showInputDialog(null, "Insira novo CPF", "Alterar Perfil", JOptionPane.PLAIN_MESSAGE);
						alterCPF(newCPF);
					}
				}else {
					nameColor = Color.white;
					rdfColor = Color.white;
					cpfColor = Color.white;
				}
			}else {
				nameColor = Color.white;
				rdfColor = Color.white;
				cpfColor = Color.white;
			}
		}else {
			nameColor = Color.white;
			rdfColor = Color.white;
			cpfColor = Color.white;
		}
		
	}
	
	/**
	 * Essa função pega os valoes do texto inserido e remove tudo que não forem numeros e checa 
	 * se o valor numerico é menor que 1000, caso seja valido o novo código ele atualiza os valores 
	 * atuais de rdf. Por termina a edição
	 * 
	 * @param newText - Texto contendo o valor do novo RdF.
	 */
	private void alterRdF(String newText) {
		
		if(Functions.emptyString(newText.replaceAll("[^0-9]", ""))) {
			return;
		}
		
		while(newText.replaceAll("[^0-9]", "").length() > 4) {
			newText = "" + JOptionPane.showInputDialog(null, "Insira um RdF valido", "Alterar Perfil", JOptionPane.ERROR_MESSAGE);
			if(Functions.emptyString(newText.replaceAll("[^0-9]", ""))) {
				return;
			}
		}
		
		DBConector.writeDB("funcionarios", "RdF", newText.replaceAll("[^0-9]", ""), "RdF", rdf);
		
		rdf = newText.replaceAll("[^0-9]", "");
		isEditing = false;
	}
	
	/**
	 * Altera o nome desde que o valor não seja nulo ou vazio.
	 * 
	 * @param newText - Texto com o novo nome.
	 */
	private void alterName(String newText) {
		
		if(Functions.emptyString(newText)) {
			return;
		}
		
		DBConector.writeDB("funcionarios", "name", newText, "RdF", rdf);
		
		name = newText;
		isEditing = false;
	}
	
	/**
	 * Altera o CPF desde que o valor não seja nulo ou vazio, esteja com
	 * um total de 11 characteres e sejam apenas numeros. Sempre ignora
	 * os characteres especiasi (- e .).
	 * 
	 * @param newText - Texto com o novo CPF.
	 */
	private void alterCPF(String newText) {
		
		if(Functions.emptyString(newText)) {
			return;
		}
		
		while(newText.replaceAll("[^0-9]", "").length() != 11) {
			newText = "" + JOptionPane.showInputDialog(null, "Insira um CPF valido", "Alterar Perfil", JOptionPane.ERROR_MESSAGE);
			
			if(Functions.emptyString(newText)) {
				return;
			}
		}
		
		DBConector.writeDB("funcionarios", "CPF", newText.replaceAll("[^0-9]", ""), "RdF", rdf);
		
		CPF = newText.replaceAll("[^0-9]", "");
		isEditing = false;
	}
	
	/**
	 * Formata o CPF da maneira que ele deveria ser apresentado, Exemplo:
	 * 123.456.789-10.
	 * 
	 * @param cpf - texto do CPF para ser formatado.
	 * @return string contendo o cpf formatado.
	 */
	protected String formatCPF(String cpf) {
		return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
	}
	
	/**
	 * Desenha os atributos compartinhados da tela do usuário e administrador.
	 * 
	 * @param g - Metodo de desenhar na tela os dados.
	 */
	protected void drawUserBasis(Graphics g) {
		g.setFont(new Font("segoi ui", 1, 40));
		g.setColor(nameColor);
		g.drawString(name, 170, 180);
		
		g.setFont(new Font("segoi ui", 1, 20));
		g.setColor(rdfColor);
		g.drawString(rdf, 170, 240);
		g.setColor(cpfColor);
		g.drawString(formatCPF(CPF), 170, 300);
	}
	
	/**
	 * Lógica da pagina dos usuários.
	 */
	public abstract void tick();
	
	/**
	 * Renderização da pagina dos usuários.
	 */
	public abstract void render(Graphics g);
}
