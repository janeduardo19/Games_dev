package com.nexttech.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
//import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.nexttech.entities.Enemy;
import com.nexttech.entities.Entity;
import com.nexttech.entities.LifePotion;
import com.nexttech.entities.Player;
import com.nexttech.entities.Shoot;
import com.nexttech.graficos.Spritesheet;
import com.nexttech.graficos.UI;
import com.nexttech.world.World;

public class Game extends Canvas implements Runnable,KeyListener {
	
	public static JFrame frame;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<LifePotion> lifepacks;
	public static List<Shoot> shoots;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random rand;
	public static String gameState = "MENU";
	public UI ui;
	public Menu menu;
	
	private static final long serialVersionUID = 1L;
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;
	private Thread thread;
	private boolean isRunning = true;
	private boolean showMessageGameOver = true;
	private boolean restartGame = false;
	private int framesGameOver = 0;
	
	public Game() {
		Sound.musicBackground.loop();
		rand = new Random();
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		//Inicializando objetos
		menu = new Menu();
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		lifepacks = new ArrayList<LifePotion>();
		shoots = new ArrayList<Shoot>();
		spritesheet = new Spritesheet("/spritesheet_02.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/map1.png");
	}
	
	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}
	
	public void update() {
		if(gameState == "MENU") {
			menu.update();
		} else if(gameState == "NORMAL") {
			this.restartGame = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.update();
			}
			
			for(int i = 0; i < shoots.size(); i++) {
				shoots.get(i).update();
			}
			
			if(enemies.size() == 0) {
				//Avançar para o próximo level
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "map"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}
		} else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver)
					showMessageGameOver = false;
				else
					showMessageGameOver = true;
			}
			
			if(restartGame) {
				this.restartGame = false;
				gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "map"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < shoots.size(); i++) {
			shoots.get(i).render(g);
		}
		ui.render(g);
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		/*g.setFont(new Font("arial", Font.BOLD, 19));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.man, 560, 45);*/
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD, 32));
			g.setColor(Color.RED);
			g.drawString("Game Over", (WIDTH*SCALE)/2 - 75, (HEIGHT*SCALE)/2 -50);
			g.setFont(new Font("arial", Font.BOLD, 20));
			g.setColor(Color.WHITE);
			if(showMessageGameOver)
				g.drawString(">Pressione Enter para reiniciar<", (WIDTH*SCALE)/2 - 170, (HEIGHT*SCALE)/2 - 10);
		} else if(gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				update();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS:"+ frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_J) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_P) {
			gameState = "MENU";
			menu.pause = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			if(gameState == "MENU") {
				menu.up = false;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			if(gameState == "MENU") {
				menu.down = false;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_J) {
			player.shoot = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

}

