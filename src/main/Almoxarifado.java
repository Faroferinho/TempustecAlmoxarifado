package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import functions.Log;
import functions.Login;
import pages.Admnistrator;
import pages.Employee;
import pages.PartsList;

public class Almoxarifado extends Canvas implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, ActionListener{
	private static final long serialVersionUID = 1L;
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static byte state = 1;
	
	public static Login login;
	public static UserInterface ui;
	public static Admnistrator admProfile;
	public static Employee workProfile;
	public static DBConector cnctr;
	public static PartsList partsList;
	public static ImageManager imgManag;
	public static Log history;
	
	public static String name = "";
	public static String cpf = "";
	public static String rdf = "";
	public static String type = "";
	
	public static int mX;
	public static int mY;
	public static boolean mPressed = false;
	public static int quantityWorkers = 0;
	public static int quantityParts = 0;
	
	//public static int workerQuantity = DBConector;
	
	
	public static void main(String args[]) {
		
		Almoxarifado almox = new Almoxarifado();
		
		imgManag = new ImageManager("spritesheet.png");
		ui = new UserInterface();
		//profile = new Admnistrator("Marcelinho Rubsheck da Silva Sauro", "1970", "16700");
		
		cnctr = new DBConector();
		quantityWorkers = cnctr.qnttWrks;
		quantityParts = cnctr.qnttPrts;
		System.out.println("Quantidade de Funcionarios: " + quantityWorkers);
		if(state == 0) {
			login = new Login();
		}
		System.out.println("type: " + type);
		
		if(type.equals("1\n")) {
			System.out.println("é Administrador");
			admProfile = new Admnistrator(name, rdf, cpf);
		}else {
			System.out.println("é Funcionario");
			workProfile = new Employee(name, rdf, cpf);
		}
		partsList = new PartsList();
		history = new Log();
		
		history.writeOnLog("Conrado", "o Log");
		
		
		inicializarTela(almox);
		
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
	
	public static void inicializarTela(Almoxarifado tp) {
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
		
		ui.tick();
		switch(state) {
		case 0:
			break;
		case 1:
			if(type.equals("1\n")) {
				admProfile.tick();
			}else {
				workProfile.tick();
			}
			break;
		case 2:
			partsList.tick();
			break;
		case 3:
			break;
		case 4:
			break;
		}
		
		//System.out.println("Estado: " + state);
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
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.lightGray);
		g.fillRect(10, 10, WIDTH-20, HEIGHT-20);
		g.setColor(Color.gray);
		g.fillRect(20, 20, WIDTH-40, HEIGHT-40);
		
		
		ui.render(g);
		switch(state) {
		case 1:
			if(type.equals("1\n")) {
				admProfile.render(g);
			}else {
				workProfile.render(g);
			}
			break;
		case 2: 
			partsList.render(g);
			break;
		}
		
		
		
		bs.show();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			tick();
			render();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
		// TODO Auto-generated method stub
		
		mPressed = true;
		if(type.equals("1\n")) {
			admProfile.mouseStatus = true;
		}else {
			workProfile.mouseStatus = true;
		}
		partsList.mouseStatus = true;
		
		//System.out.println("Status do Mouse: " + profile.mouseStatus);
		//System.out.println("X: " + e.getX() + ", Y: " + e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		mPressed = false;
		if(type.equals("1\n")) {
			admProfile.mouseStatus = false;
		}else {
			workProfile.mouseStatus = false;
		}
		partsList.mouseStatus = false;
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
		// TODO Auto-generated method stub
		//System.out.println("Rolagem do mouse: " + e.getUnitsToScroll());
		PartsList.scroll = e.getUnitsToScroll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
