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
import java.time.LocalDateTime;

import javax.swing.JFrame;

import functions.Archiver;
import functions.DBConector;
import functions.Functions;
import functions.ImageManager;
import pages.AddAssembly;
import pages.AddPart;
import pages.Archive;
import pages.Login;
import pages.PartsList;
import pages.Profile;
import pages.Project;
import pages.ProjectList;
import pages.AddWorker;
import pages.Admnistrator;

public class Almoxarifado extends Canvas implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener{
	//Inicialização das Variaveis escenciais para o usuário interagir com o sistema;
	private static final long serialVersionUID = 1L;
	
	public static int WIDTH;
	public static int HEIGHT;
	
	private static byte state = 0;
	
	public static int mX;
	public static int mY;
	public static boolean mPressed = false;
	
	//Iniciar classes importantes para o sistema.
	public static JFrame frame;
	public static Toolkit tk;
	public static ImageManager imgManag;
	
	//Paginas do Sistema
	public static UserInterface ui;
	public static Profile userProfile;
	public static Login login;
	public static PartsList partsList;
	public static ProjectList projectList;
	public static Project project;
	public static Archive archive;
	public static AddPart addPart;
	public static AddAssembly addAssembly;
	public static AddWorker addWorker;
	
	//Criação de Multiplas Threads
	public static Thread mainThread;
	public static Thread fortnightVerificatorThread;
	
	//Variáveis que armazenam a quantidade de entradas das tabelas.
	public static int quantityWorkers = 0;
	public static int quantityParts = 0;
	public static int quantityAssembly = 0;
	public static int quantityArchives = 0;
	public static int quantityArchiveParts = 0;
	
	//Thread Principal:
	public static void main(String args[]) {
		Almoxarifado almox = new Almoxarifado();
		
		//Instanciando as classes que são escenciais para as páginas funcionarem.
		imgManag = new ImageManager("Spritesheet");
		ui = new UserInterface();
		
		//Instanciando as Páginas
		login = new Login();
		userProfile = null;
		userProfile = new Admnistrator("8523");
		projectList = new ProjectList();
		project = new Project();
		partsList = new PartsList();
		archive = new Archive();
		addPart = new AddPart();
		addAssembly = new AddAssembly();
		addWorker = new AddWorker();
		
		//Instanciando e Administrando a janela.
		frame = new JFrame();
		
		frame.add(almox);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setBackground(Color.black);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setIconImage(imgManag.TempustecIcon);
		System.out.println("Carregou Tela: " + LocalDateTime.now());
		
		//Faz a contagem inicial da quantidade de Peças.
		quantityWorkers = DBConector.counterOfElements("funcionarios");
		quantityParts = DBConector.counterOfElements("pecas");
		quantityAssembly = DBConector.counterOfElements("Montagem");
		quantityArchives = DBConector.counterOfElements("Arquivo");
		quantityArchiveParts = DBConector.counterOfElements("Arquivo_Pecas");
		System.out.println("Carregou Quantidades: " + LocalDateTime.now());
		
		//Dá inicio as ambas threads.
		mainThread = new Thread(almox);
		fortnightVerificatorThread = new Thread() {
			public void run(){
				Archiver.logInfo();
			}
		};
		
		mainThread.start();
		fortnightVerificatorThread.start();
		
		System.out.println("Carregou as Threads: " + LocalDateTime.now());
	}
	
	public Almoxarifado() {
		//Instancia e configura a janela usando os dados do computador.
		tk = Toolkit.getDefaultToolkit();
		
		WIDTH = tk.getScreenSize().width;
		HEIGHT = tk.getScreenSize().height - 60;
		
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		this.setFocusTraversalKeysEnabled(false);
		System.out.println("Carregou Almoxarifado: " + LocalDateTime.now());
	}
	
	//Encapsulamento da variável State.
	public static int getState() {
		return state;
	}
	
	public static void setState(int changeState) {
		login.mouseClick = false;
		userProfile.mouseStatus = false;
		partsList.mouseStatus = false;
		projectList.mouseStatus = false;
		archive.mouseStatus = false;
		project.mouseStatus = false;
		addPart.click = false;
		addAssembly.click = false;
		addWorker.click = false;
		
		state = (byte) changeState;
	}
	
	/**
	 * Função para Cotrolar e a lógica do Sistema,
	 * Adicionando e controlando os estados das
	 * variaveis do programa com base no state.
	 */
	public void tick() {
		//Verifica se alguém fechou o sistema, se entrar ele gera o inquerito de pedido.
		if(!frame.isShowing()) {
			Functions.generatePurchaseInquery();
			System.exit(0);
		}
		
		//Controla a lógica da pagina.
		switch(state) {
		case 0:
			login.tick();
			break;
		case 1:
			userProfile.tick();
			break;
		case 2:
			partsList.tick();
			break;
		case 3:
			projectList.tick();
			break;
		case 4:
			archive.tick();
			break;
		case 5:
			project.tick();
			break;
		case 6:
			addPart.tick();
			break;
		case 7:
			addAssembly.tick();
			break;
		case 8:
			addWorker.tick();
			break;
		default:
			state = 0;
		}
		
		if(state != 0) {
			ui.tick();			
		}
	}
	
	/**
	 * Uma função que desenha uma tela padrão para trabalhar
	 * nos padroes do sistema.
	 * 
	 * @param g é a classe de desenho da tela.
	 */
	public void backgroundRender(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(new Color(196, 196, 196));
		g.fillRoundRect(10, 10, WIDTH-20, HEIGHT-20, 20, 20);
 
		g.setColor(new Color(126, 126, 126));
		g.fillRoundRect(23, 23, WIDTH-46, HEIGHT-46, 10, 10);
	}
	
	/**
	 * A função que desenha o sistema.
	 * 
	 */
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
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		
		//Controlo quando vai ser desenhado cada pagina.		
		backgroundRender(g);
		
		ui.clearBox(g);
		switch(state) {
		case 0:
			login.render(g);
			break;
		case 1:
			userProfile.render(g);
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
		case 6:
			addPart.render(g);
			break;
		case 7:
			addAssembly.render(g);
			break;
		case 8:
			addWorker.render(g);
			break;
		}
		
		//desenha a interface de usuário comum de todas as paginas em qualquer pagina exceto pelo login.
		if(state != 0) {
			ui.limitScrollToWorkspaceArea(g);
			ui.render(g);
		}
		
		bs.show();
	}
	
	/**
	 * Desenha texto com borda em volta dele.
	 * Sempre define a cor como a cor do texto.
	 * 
	 * @param 	g - Graficos de Desenho da Janela.
	 * @param	text - O Texto a ser desenhado.
	 * @param	x - Altura inicial do Texto.
	 * @param	y - Largura inicial do Texto.
	 * @param	borderThickness - Grossura da borda.
	 * @param 	borderColor - Cor da borda.
	 * @param	textColor - Cor do Texto.
	 */
	public static void drawStringBorder(Graphics g, String text, int x, int y, int borderThickness, Color borderColor, Color textColor) {
		g.setColor(borderColor);
		
		for(int i = borderThickness * (-1); i < (borderThickness + 1); i++) {
			
			for(int j = borderThickness * (-1); j < (borderThickness + 1); j++) {
				
				g.drawString(text, x+i, y+j);
			
			}
			
		}
		
		g.setColor(textColor);
		g.drawString(text, x, y);
		
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
				userProfile.mouseStatus = true;
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
			case 6:
				addPart.click = true;
				break;
			case 7:
				addAssembly.click = true;
				break;
			case 8:
				addWorker.click = true;
				break;
			}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mPressed = false;
		
		switch(state) {
		case 0:
			login.mouseClick = false;
			break;
		case 1:
			userProfile.mouseStatus = false;
			break;
		case 2:
			partsList.mouseStatus = false;
			break;
		case 3:
			projectList.mouseStatus = false;
			break;
		case 4:
			archive.mouseStatus = false;
			break;
		case 5:
			project.mouseStatus = false;
			break;
		case 6:
			addPart.click = false;
			break;
		case 7:
			addAssembly.click = false;
			break;
		case 8:
			addWorker.click = false;
			break;
		}
		
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
			partsList.offsetHeight = ((PartsList.maximumHeight * y) / ((UserInterface.maximunHeight - 12) - partsList.thumbHeight)) * -1;
			
			partsList.scrollPositioner();
		}else if(projectList.isDragging) {
			
			projectList.offsetHeight = ((projectList.maximumHeight * y) / ((UserInterface.maximunHeight - 12) - projectList.thumbHeight)) * -1;
			
			projectList.scrollPositioner();
		}else if(project.isDragging) {
			project.offsetHeight = ((project.maximumHeight * y) / ((UserInterface.maximunHeight - 12) - project.thumbHeight)) * -1;
			
			project.scrollPositioner();
		}else if(archive.isDragging) {
			archive.offsetHeight = ((archive.maximumHeight * y) / ((UserInterface.maximunHeight - 12) - archive.thumbHeight)) * -1;
			
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
		
		System.out.println("O Caractere " + e.getKeyChar() + " tem como código " + e.getExtendedKeyCode());
		
		if(state == 0) {
			if(login.isWriting) {
				login.writingOnCanvas(e);
			}else if(e.getKeyCode() == KeyEvent.VK_TAB) {
				login.isWriting = true;
				login.isOnCPF = true;
			}
		}else if(state == 2) {
			if(partsList.isWriting) {
				partsList.getText(e);
			}
		}else if(state == 6) {
			if(addPart.isWriting == true) {
				addPart.writer(e);
			}else if(e.getKeyCode() == KeyEvent.VK_TAB) {
				addPart.isWriting = true;				
			}
		}else if(state == 7) {
			if(addAssembly.isWriting == true) {
				addAssembly.writer(e);
			}else if(e.getKeyCode() == KeyEvent.VK_TAB) {
				addAssembly.isWriting = true;				
			}
		}else if(state == 8) {
			if(addWorker.isWriting == true) {
				addWorker.writer(e);
			}else if(e.getKeyCode() == KeyEvent.VK_TAB) {
				addWorker.isWriting = true;				
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
