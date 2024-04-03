package functions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import objects.Suggestion;

public class TextSuggestor {

	JTextField localTxtFld;
	JPanel localPanel;
	Window localWin;
	JWindow popUpWindow;
	
	ArrayList<String> suggestions = new ArrayList<>();
	
	int windowWidth = 0;
	int windowHeight = 0;
	
	int currIndex = 0;
	
	final Color background;
	final Color foreground;
	final Color border;
	
	DocumentListener dcmntLstnr = new DocumentListener() {

		@Override
		public void insertUpdate(DocumentEvent e) {
			//System.out.println("Adicionou");
			checkChange();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			//System.out.println("Removeu");
			checkChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			//System.out.println("Alteração feita");
			checkChange();
			
		}
		
	};

	public TextSuggestor(JTextField userInput, Window window) {
		localTxtFld = userInput;
		localWin = window;
		background = Color.gray;
		foreground = Color.gray;
		border = Color.gray;
		
		fillArray();
		
		popUpWindow = new JWindow(window);
		
		localPanel = new JPanel();
		localPanel.setLayout(new GridLayout(0, 1));;
		
		localTxtFld.getDocument().addDocumentListener(dcmntLstnr);
		
		keyBiding();
	}
	
	public TextSuggestor(JTextField userInput, Window window, float opacity) {
		localTxtFld = userInput;
		localWin = window;
		background = Color.gray;
		foreground = Color.gray;
		border = Color.gray;
		
		fillArray();
		
		popUpWindow = new JWindow(window);
		popUpWindow.setOpacity(opacity);
		
		localPanel = new JPanel();
		localPanel.setLayout(new GridLayout(0, 1));;
		
		localTxtFld.getDocument().addDocumentListener(dcmntLstnr);
		
		keyBiding();
	}
	
	public TextSuggestor(JTextField userInput, Window window, Color backgroudColor, Color foregroudColor, Color borderColor) {
		localTxtFld = userInput;
		localWin = window;
		background = backgroudColor;
		foreground = foregroudColor;
		border = borderColor;
		
		fillArray();
		
		popUpWindow = new JWindow(window);
		
		localPanel = new JPanel();
		localPanel.setLayout(new GridLayout(0, 1));
		localPanel.setBackground(background);
		
		localTxtFld.getDocument().addDocumentListener(dcmntLstnr);
		keyBiding();
	}
	
	public TextSuggestor(JTextField userInput, Window window, Color backgroudColor, Color foregroudColor, Color borderColor, float opacity) {
		localTxtFld = userInput;
		localWin = window;
		background = backgroudColor;
		foreground = foregroudColor;
		border = borderColor;
		
		fillArray();
		
		popUpWindow = new JWindow(window);
		popUpWindow.setOpacity(opacity);
		
		localPanel = new JPanel();
		localPanel.setLayout(new GridLayout(0, 1));
		localPanel.setBackground(background);
		
		localTxtFld.getDocument().addDocumentListener(dcmntLstnr);
		keyBiding();
	}
	
	private void fillArray() {
		if(suggestions.equals(null)) {
			suggestions = new ArrayList<String>();
		}
		
		suggestions.clear();
		
		suggestions.add("aarakocra");
        suggestions.add("aasimar");
        suggestions.add("air-genasi");
        suggestions.add("Artificer");
        suggestions.add("Astral Elf");
        suggestions.add("Autognome");
        suggestions.add("bugbear");
        suggestions.add("Barbarian");
        suggestions.add("Bard");
        suggestions.add("Blood Hunter");
        suggestions.add("centaur");
        suggestions.add("Cervan");
        suggestions.add("changeling");
        suggestions.add("Cleric");
        suggestions.add("Corvum");
        suggestions.add("dragonborn");
        suggestions.add("Druid");
        suggestions.add("duergar");
        suggestions.add("dwarf");
        suggestions.add("eladrin");
        suggestions.add("elf");
        suggestions.add("earth-genasi");
        suggestions.add("fairy");
        suggestions.add("fighter");
        suggestions.add("fire-genasi");
        suggestions.add("firblog");
        suggestions.add("gallus");
        suggestions.add("Giff");
		suggestions.add("githyanki");
		suggestions.add("githzerai");
		suggestions.add("gnome");
		suggestions.add("goblin");
		suggestions.add("goliath");
		suggestions.add("Grung");
		suggestions.add("Hadozee");
		suggestions.add("Half-Elf");
		suggestions.add("halfling");
		suggestions.add("half-orc");
		suggestions.add("hedge");
		suggestions.add("human");
		suggestions.add("harengon");
		suggestions.add("hobgoblin");
		suggestions.add("Jerbeen");
		suggestions.add("kalashtar");
		suggestions.add("Kender");
		suggestions.add("kenku");
		suggestions.add("Kobold");
		suggestions.add("leonin");
		suggestions.add("lizardfolk");
		suggestions.add("Locathah");
		suggestions.add("Loxodon");
		suggestions.add("luma");
		suggestions.add("mapach");
		suggestions.add("minotaur");
		suggestions.add("Monk");
		suggestions.add("orc");
		suggestions.add("owlin");
		suggestions.add("Paladin");
		suggestions.add("Plasmoid");
		suggestions.add("Ranger");
		suggestions.add("Raptor");
		suggestions.add("Rogue");
		suggestions.add("satyr");
		suggestions.add("sea-elf");
		suggestions.add("Shifter");
		suggestions.add("Simic Hybrid");
		suggestions.add("Sorcerer");
		suggestions.add("Strig");
		suggestions.add("Tabaxi");
		suggestions.add("Thri-Kreen");
		suggestions.add("tiefling");
		suggestions.add("tortle");
		suggestions.add("Triton");
		suggestions.add("Verdan");
		suggestions.add("Vedalken");
		suggestions.add("Vulpin");
		suggestions.add("water-genasi");
		suggestions.add("Warforged");
		suggestions.add("Warlock");
		suggestions.add("Wizard");
		suggestions.add("Yuan-ti");
		
		//System.out.println("Feito");
	}
	
	private void checkChange(){
		String currWord = getCurrWord(localTxtFld.getText());
		//System.out.println("Ultima Palavra: " + currWord);
		
		boolean wasFilled = getMatches(currWord);
		
		if(wasFilled == false) {
			popUpWindow.setVisible(false);
		}else {
			showPopUpWindow();
			resetFocus();
		}
	}
	
	private String getCurrWord(String text) {
		String toReturn = "";
		int lastSpaceIndex = 0;
		
		windowWidth = 0;
		windowHeight = 0;
		
		if(text.equals(null)) {
			return "";
		}else {
			for(int i = 0; i < text.length(); i++) {
				if(text.charAt(i) == ' ') {
					lastSpaceIndex = i+1;
				}
			}
		}
		
		toReturn = text.substring(lastSpaceIndex);
		
		return toReturn;
	}
	
	private boolean getMatches(String toMatch) {
		boolean fillPossibilities = false;
		String lwcsToMatch = toMatch.toLowerCase();
		String lwcsCurr = "";
		localPanel.removeAll();
		
		if(toMatch.equals("")) {
			return false;
		}
		
		//System.out.println("======================================================================\nFrase para comparar:\n" + toMatch);
		
		for(String curr : suggestions) {
			lwcsCurr = curr.toLowerCase();
			
			if(lwcsCurr.startsWith(lwcsToMatch)) {
				//System.out.println("----------------------------------------------------------------------\n" + curr);
				addToPanelMatches(curr);
				
				fillPossibilities = true;
			}
		}
		
		//System.out.println("======================================================================");
		
		
		
		return fillPossibilities;
	}
	
	private void addToPanelMatches(String name) {
		Suggestion lbl = new Suggestion(name, border, foreground, this);
		
		ajustPopUpSize(lbl);
		
		localPanel.add(lbl);
	}
	
	private void ajustPopUpSize(JLabel lbl) {
		if(windowWidth < lbl.getPreferredSize().width) {
			windowWidth = lbl.getPreferredSize().width;
		}
		windowHeight += 30;
		
		//System.out.println("Altura do Elemento: " + windowHeight);
	}
	
	private void showPopUpWindow(){
		popUpWindow.getContentPane().add(localPanel);
		popUpWindow.setMinimumSize(new Dimension(localTxtFld.getWidth(), 30));
		popUpWindow.setSize(windowWidth, windowHeight);
		popUpWindow.setVisible(true);
		
		int initialX = 0;
		int initialY = 0;
		
		initialX = localWin.getX() + localTxtFld.getX() + 8;
		if(localPanel.getHeight() > popUpWindow.getMinimumSize().height) {
			initialY = localWin.getY() + localTxtFld.getY() + localTxtFld.getHeight() + popUpWindow.getMinimumSize().height;
		}else {
			initialY = localWin.getY() + localTxtFld.getY() + localTxtFld.getHeight() + popUpWindow.getHeight();
		}
		
		popUpWindow.setLocation(initialX, initialY);
		popUpWindow.setMinimumSize(new Dimension(localTxtFld.getWidth(), 30));
		popUpWindow.revalidate();
		popUpWindow.repaint();
		resetFocus();
	}
	
	public void resetFocus() {
		localWin.toFront();
		localWin.requestFocus();
		localTxtFld.requestFocus();
	}
	
	public void keyBiding() {
		System.out.println("instanciado");
		
		localTxtFld.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down Released");
		localTxtFld.getActionMap().put("Down Released", new AbstractAction() {
			
			private static final long serialVersionUID = 5419855511987329205L;

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("pra baixo");
				
				for(int i = 0; i < localPanel.getComponentCount(); i++) {
					if(localPanel.getComponent(i) instanceof Suggestion) {
						((Suggestion)localPanel.getComponent(i)).setIsFocused(true);
						popUpWindow.toFront();
						popUpWindow.requestFocusInWindow();
						localPanel.requestFocusInWindow();
						localPanel.getComponent(i).requestFocusInWindow();
						break;
					}
				}
			}
			
		});
		
		localPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down Released");
		localPanel.getActionMap().put("Down Released", new AbstractAction(){
			
			private static final long serialVersionUID = -1847500111970714220L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<Suggestion> suggestionLabelList = getSuggestionList();
				int quantitySuggestions = suggestionLabelList.size();
				
				if(quantitySuggestions > 1) {
					for(int i = 0; i < quantitySuggestions; i++) {
						Suggestion currSggstnLbl = suggestionLabelList.get(i);
						if(currSggstnLbl.isFocused()) {
							if(currIndex == (quantitySuggestions - 1)) {
								currIndex = 0;
								currSggstnLbl.setIsFocused(false);
								popUpWindow.setVisible(false);
								resetFocus();
								checkChange();
							}else {
								currSggstnLbl.setIsFocused(false);
								currIndex = i;
							}
						}else if(currIndex < (i + 1)) {
							if(i < quantitySuggestions) {
								currSggstnLbl.setIsFocused(true);
								popUpWindow.toFront();
								popUpWindow.requestFocusInWindow();
								localPanel.requestFocusInWindow();
								localPanel.getComponent(i).requestFocusInWindow();
								currIndex = i;
								break;
							}
						}
					}
				}else {
					popUpWindow.setVisible(false);
					resetFocus();
					checkChange();
				}
				
				System.out.println("Index Atual: " + currIndex);
			}
			
		});
		
		localPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "Up Released");
		localPanel.getActionMap().put("Up Released", new AbstractAction() {
			
			private static final long serialVersionUID = 833964563979153361L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Suggestion> suggestionLabelList = getSuggestionList();
				int quantitySuggestions = suggestionLabelList.size();
				
				System.out.println("pra cima");
				
				if(quantitySuggestions > 1) {
					for(int i = quantitySuggestions-1; i > -1; i--) {
						Suggestion currSggstnLbl = suggestionLabelList.get(i);
						if(currSggstnLbl.isFocused()) {
							if(currIndex == (quantitySuggestions)) {
								currSggstnLbl.setIsFocused(false);
								popUpWindow.setVisible(false);
								resetFocus();
								checkChange();
							}else {
								currSggstnLbl.setIsFocused(false);
								currIndex = i;
							}
						}else if(currIndex > (i - 1)) {
							if(i > -1) {
								currSggstnLbl.setIsFocused(true);
								popUpWindow.toFront();
								popUpWindow.requestFocusInWindow();
								localPanel.requestFocusInWindow();
								localPanel.getComponent(i).requestFocusInWindow();
								currIndex = i;
								break;
							}
						}
					}
				}else {
					popUpWindow.setVisible(false);
					resetFocus();
					checkChange();
				}
				System.out.println("Index Atual: " + currIndex);
			}
			
		});
	}
	
	private ArrayList<Suggestion> getSuggestionList() {
		ArrayList<Suggestion> toReturn = new ArrayList<>();
		
		for(int i = 0; i < localPanel.getComponentCount(); i++) {
			if(localPanel.getComponent(i) instanceof Suggestion) {
				toReturn.add((Suggestion) localPanel.getComponent(i));
			}
		}
		
		return toReturn;
	}
	
	public JWindow getAutoPopUpWindow() {
		return popUpWindow;
	}
	
	public JTextField getTextField() {
		return localTxtFld;
	}
}
