package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import functions.DBConector;
import functions.ImageManager;
import pages.Admnistrator;
import pages.Archive;
import pages.Employee;
import pages.Login;
import pages.PartsList;
import pages.Project;
import pages.ProjectList;

public class Almoxarifado extends Canvas implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener{
	private static final long serialVersionUID = 1L;
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static byte state = 4;
	
	public static JFrame frame;
	public static Toolkit tk;
	public static Login login;
	public static UserInterface ui;
	public static Admnistrator admProfile;
	public static Employee workProfile;
	public static DBConector cnctr;
	public static PartsList partsList;
	public static ImageManager imgManag;
	public static ProjectList projectList;
	public static Project project;
	public static Archive archive;
	
	public static String name = "Leoncio Pafuncio Figueiredo";
	public static String cpf = "321.456.987-13";
	public static String rdf = "2082";
	public static String type = "1";
	
	public static int mX;
	public static int mY;
	public static boolean mPressed = false;
	
	public static int quantityWorkers = 0;
	public static int quantityParts = 0;
	public static int quantityAssembly = 0;
	public static int quantityArchives = 0;
	public static int quantityArchiveParts = 0;
	
	
	public static void main(String args[]) {
		
		Almoxarifado almox = new Almoxarifado();
		
		imgManag = new ImageManager("Spritesheet");
		ui = new UserInterface();

		cnctr = new DBConector();
		quantityWorkers = cnctr.qnttWrks;
		quantityParts = cnctr.qnttPrts;
		quantityAssembly = cnctr.qnttAssbly;
		quantityArchives = cnctr.qnttArchvs;
		quantityArchiveParts = cnctr.qnttArchvParts;
		
		login = new Login();
		
		admProfile = new Admnistrator(name, rdf, cpf); 
		workProfile = new Employee(name, rdf, cpf); 
		
		projectList = new ProjectList();
		project = new Project();
		partsList = new PartsList();
		archive = new Archive();
		
		frame = new JFrame();
		
		frame.add(almox);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.black);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		frame.setIconImage(imgManag.TempustecIcon);
		
		new Thread(almox).start();
	}
	
	public Almoxarifado() {
		tk = Toolkit.getDefaultToolkit();
		
		WIDTH = tk.getScreenSize().width;
		HEIGHT = tk.getScreenSize().height - 60;
		
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		this.setFocusTraversalKeysEnabled(false);
	}
	
	public void tick() {
		
		switch(state) {
		case 0:
			login.tick();
			break;
		case 1:
			ui.tick();
			if(type.equals("1")) {
				admProfile.tick();
			}else {
				workProfile.tick();
			}
			break;
		case 2:
			ui.tick();
			partsList.tick();
			break;
		case 3:
			ui.tick();
			projectList.tick();
			break;
		case 4:
			ui.tick();
			archive.tick();
			break;
		case 5:
			ui.tick();
			project.tick();
			break;
		}
	}
	
	public void backgroundRender(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(new Color(196, 196, 196));
		g.fillRoundRect(10, 10, WIDTH-20, HEIGHT-20, 20, 20);
 
		g.setColor(new Color(126, 126, 126));
		g.fillRoundRect(23, 23, WIDTH-46, HEIGHT-46, 10, 10);
	}
	
	public void render() {
		// Eu crio uma BufferStrategy, ou seja, guardo memória para poder mostrar a prox. imagem;
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		//Crio um grafico usando os graficos do bs;
		Graphics g = bs.getDrawGraphics();
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Basicamente desenho o esqueleto da UI;
		
		backgroundRender(g);
		
		ui.clearBox(g);
		switch(state) {
		case 0:
			login.render(g);
			break;
		case 1:
			if(type.equals("1")) {
				admProfile.render(g);
			}else {
				workProfile.render(g);
			}
			break;
		case 2: 
			partsList.render(g);
			break;
		case 3:
			projectList.render(g);
			break;
		case 4:
			archive.render(g);
			break;
		case 5:
			project.render(g);
			
			break;
		}
		if(state != 0) {
			ui.limitScrollToWorkspaceArea(g);
			ui.render(g);
		}
		
		bs.show();
	}
	
	public static void drawStringBorder(Graphics2D g2, String text, int x, int y, int borderThickness, Color borderColor, Color textColor) {
		g2.setColor(borderColor);
		for(int i = -borderThickness; i < borderThickness+1; i++) {
			for(int j = -borderThickness; j < borderThickness+1; j++) {
				g2.drawString(text, x+i, y+j);
			}
		}
		
		g2.setColor(textColor);
		g2.drawString(text, x, y);
		
	}

	@Override
	public void run() {
		while(true) {
			tick();
			render();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mPressed = true;
		login.mouseClick = true;
		switch(state) {
		case 1:
			if(type.equals("1")) {
				admProfile.mouseStatus = true;
			}else {
				workProfile.mouseStatus = true;
			}
			break;
		case 2:
			partsList.mouseStatus = true;
			break;
		case 3:
			projectList.mouseStatus = true;
			break;
		case 4:
			archive.mouseStatus = true;
			break;
		case 5:
			project.mouseStatus = true;
			break;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mPressed = false;
		login.mouseClick = false;
		if(type.equals("1")) {
			admProfile.mouseStatus = false;
		}else {
			workProfile.mouseStatus = false;
		}
		partsList.mouseStatus = false;
		projectList.mouseStatus = false;
		project.mouseStatus = false;
		archive.mouseStatus = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int y = e.getY() - (UserInterface.bttnY + UserInterface.boxHeight + 18);
		
		if(partsList.isDragging) {
			partsList.ofsetHeight = ((PartsList.maximumHeight * y) / ((UserInterface.maximunHeight - 12) - partsList.thumbHeight)) * -1;
			
			partsList.scrollPositioner();
		}else if(projectList.isDragging) {
			
			projectList.ofsetHeight = ((projectList.maximumHeight * y) / ((UserInterface.maximunHeight - 12) - projectList.thumbHeight)) * -1;
			
			projectList.scrollPositioner();
		}else if(project.isDragging) {
			project.ofsetHeight = ((project.maximumHeight * y) / ((UserInterface.maximunHeight - 12) - project.thumbHeight)) * -1;
			
			project.scrollPositioner();
		}else if(archive.isDragging) {
			archive.ofsetHeight = ((archive.maximumHeight * y) / ((UserInterface.maximunHeight - 12) - archive.thumbHeight)) * -1;
			
			archive.scrollPositioner();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mX = e.getX();
		mY = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		switch(state) {
		case 1:
			if(type.equals("1") && admProfile.isListing) {
				admProfile.scroll = e.getUnitsToScroll();
			}
			break;
		case 2:
			PartsList.scroll = e.getUnitsToScroll();
			break;
		case 3:
			ProjectList.scroll = e.getUnitsToScroll();
			break;
		case 4:
			Archive.scroll = e.getUnitsToScroll();
			break;
		case 5:
			Project.scroll = e.getUnitsToScroll();
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(state == 0) {
			if(login.isWriting) {
				login.writingOnCanvas(e);
			}else if(e.getKeyCode() == KeyEvent.VK_TAB) {
				login.isWriting = true;
				login.isOnCPF = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
