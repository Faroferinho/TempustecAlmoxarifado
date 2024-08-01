package functions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ImageManager {
	//Cria duas variáveis para armazenar imagens (a spritesheet e o icone da tempustec).
	private BufferedImage spritesheet;
	public BufferedImage TempustecIcon;

	/**
	 * Classe que armazena e controla imagens para melhor apresentação do sistema.
	 * 
	 * @param imgFile - Endereço da imagem na pasta res.
	 */
	public ImageManager(String imgFile) {
		try {
			spritesheet = ImageIO.read(new File("C:/Program Files/Almoxarifado/res/" + imgFile + ".png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Não Existe a SpriteSheet");
			e.printStackTrace();
		}
		
		TempustecIcon = getIcon();
		
		System.out.println("Carregou ImageManager: " + LocalDateTime.now());
	}
	
	/**
	 * Função para pegar imagens que estão na spritesheet.
	 * 
	 * @param x - Largura inicial da sub imagem da spritesheet.
	 * @param y - Altura inicia da sub imagem da spritesheet.
	 * @param width - Largura da sub imagem.
	 * @param height - Altura da sub imagem.
	 * @return sub imagem da spritesheet
	 */
	public BufferedImage getSprite(int x, int y, int width, int height) {
		BufferedImage sprite = spritesheet.getSubimage(x, y, width, height);
		
		return sprite;
	}
	
	/**
	 * Função que carrega imagens da pasta res.
	 * 
	 * @param fileName - Nome do arquivo.
	 * @return imagem na pasta.
	 */
	public BufferedImage getProjectImage(String fileName) {
		BufferedImage returnImg = null;
		
		try {
			returnImg = ImageIO.read(new File("C:/Program Files/Almoxarifado/res/" + fileName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnImg;
	}
	
	/**
	 * Pega o icone do programa na pasta res.
	 * 
	 * @return icone do programa.
	 */
	private BufferedImage getIcon() {
		BufferedImage returnImg = null;
		
		try {
			returnImg = ImageIO.read(new File("C:/Program Files/Almoxarifado/res/Almox-Logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnImg;
	}
	
}
