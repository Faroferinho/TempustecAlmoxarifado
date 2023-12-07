package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
import pages.Employee;
import pages.Login;
import pages.PartsList;
import pages.Project;
import pages.ProjectList;

public class Almoxarifado extends Canvas implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener{
	private static final long serialVersionUID = 1L;
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static byte state = 5;
	
	public static Login login;
	public static UserInterface ui;
	public static Admnistrator admProfile;
	public static Employee workProfile;
	public static DBConector cnctr;
	public static PartsList partsList;
	public static ImageManager imgManag;
	public static ProjectList projectList;
	public static Project project;
	
	public static String name = "";
	public static String cpf = "";
	public static String rdf = "";
	public static String type = "";
	
	public static int mX;
	public static int mY;
	public static boolean mPressed = false;
	
	public static int quantityWorkers = 0;
	public static int quantityParts = 0;
	public static int quantityAssembly = 0;
	
	
	public static void main(String args[]) {
		
		Almoxarifado almox = new Almoxarifado();
		
		imgManag = new ImageManager("spritesheet.png");
		ui = new UserInterface();
		//profile = new Admnistrator("Marcelinho Rubsheck da Silva Sauro", "1970", "16700");

		cnctr = new DBConector();
		quantityWorkers = cnctr.qnttWrks;
		quantityParts = cnctr.qnttPrts;
		quantityAssembly = cnctr.qnttAssbly;
		
		System.out.println("Quantidade de Funcionarios: " + quantityWorkers);
		System.out.println("Quantidade de Peças: " + quantityParts);
		System.out.println("Quantidade de Montagens: " + quantityAssembly);
		if(state == 0) {
			login = new Login();
		}
		System.out.println("type: " + type);
		
		if(type.equals("1\n")) {
			System.out.println("é Administrador");
			admProfile = new Admnistrator(name, rdf, cpf);
		}else {
			System.out.println("é Funcionario");

			workProfile = new Employee(name,rdf, cpf);
		}
		partsList = new PartsList();
		projectList = new ProjectList();
		project = new Project();
		
		screenManager(almox);
		
		new Thread(almox).start();
	}
	
	public Almoxarifado() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		WIDTH = (tk.getScreenSize().width / 4) * 3;
		HEIGHT = (tk.getScreenSize().height / 4) * 3;
		
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		
		System.out.println("Largura: " + WIDTH + " Altura: " + HEIGHT);
	}
	
	public static void screenManager(Almoxarifado tp) {
		JFrame frame = new JFrame("Tela Java");
		
		frame.add(tp);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.black);
		frame.setVisible(true);
	}
	
	public void tick() {
		
		switch(state) {
		case 0:
			login.tick();
			break;
		case 1:
			ui.tick();
			if(type.equals("1\n")) {
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
			break;
		case 5:
			ui.tick();
			project.tick();
			break;
		}
		
		//System.out.println("Estado: " + state);
	}
	
	public void backgroundRender(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.lightGray);
		g.fillRect(10, 10, WIDTH-20, HEIGHT-20);
		g.setColor(Color.gray);
		g.fillRect(20, 20, WIDTH-40, HEIGHT-40);
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
		
		//Basicamente desenho o esqueleto da UI;
		
		backgroundRender(g);
		
		ui.clearBox(g);
		switch(state) {
		case 0:
			login.render(g);
			break;
		case 1:
			if(type.equals("1\n")) {
				//System.out.println("Admnistrador");
				admProfile.render(g);
			}else {
				//System.out.println("Colaborador");
				workProfile.render(g);
			}
			ui.limitScrollToWorkspaceArea(g);
			ui.render(g);
			break;
		case 2: 
			partsList.render(g);
			ui.limitScrollToWorkspaceArea(g);
			ui.render(g);
			break;
		case 3:
			projectList.render(g);
			ui.limitScrollToWorkspaceArea(g);
			ui.render(g);
			break;
		case 4:
			ui.limitScrollToWorkspaceArea(g);
			ui.render(g);
			break;
		case 5:
			ui.limitScrollToWorkspaceArea(g);
			project.render(g);
			ui.render(g);
			break;
		}
		
		bs.show();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {		
		mPressed = true;
		if(type.equals("1\n")) {
			admProfile.mouseStatus = true;
		}else {
			workProfile.mouseStatus = true;
		}
		partsList.mouseStatus = true;
		projectList.mouseStatus = true;
		//TODO remover comentarios - login.click = true;
		//System.out.println("Status do Mouse: " + profile.mouseStatus);
		//System.out.println("X: " + e.getX() + ", Y: " + e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mPressed = false;
		if(type.equals("1\n")) {
			admProfile.mouseStatus = false;
		}else {
			workProfile.mouseStatus = false;
		}
		partsList.mouseStatus = false;
		projectList.mouseStatus = false;
		//TODO remover comentarios - login.click = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		// TODO Add threadSleep para o programa parar;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mX = e.getX();
		mY = e.getY();
		
		//System.out.println("X do Mouse: " + mX + " , Y do Mouse: " + mY);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		//System.out.println("Rolagem do mouse: " + e.getUnitsToScroll());
		
		switch(state) {
		case 1:
			//TODO: Perfil, verificação de "está listando";
			break;
		case 2:
			PartsList.scroll = e.getUnitsToScroll();
			break;
		case 3:
			ProjectList.scroll = e.getUnitsToScroll();
			break;
		case 4:
			//TODO: Arquivo;
			break;
		case 5:
			//TODO: Projeto;
			break;
		}
		
		//System.out.println("ProjectList.scroll: " + ProjectList.scroll + "\ne.getUnitsToScroll(): " + e.getUnitsToScroll());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Caracter escrito foi: " + e.getKeyChar() + " e seu Código é: " + e.getKeyCode());
		if(login.isWriting != 0) {
			login.writingOnCanvas(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
