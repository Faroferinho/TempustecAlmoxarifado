 package functions;

import java.awt.image.BufferedImage;

import main.Almoxarifado;

public interface BidimensionalList {
	public static final int firstLine = 0;
	
	static final BufferedImage upIndicator = Almoxarifado.imgManag.getSprite(455, 415, 9, 9);
	static final BufferedImage downIndicator = Almoxarifado.imgManag.getSprite(464, 415, 9, 9);
	
	public abstract String getColumn(int i);
	
	
}
