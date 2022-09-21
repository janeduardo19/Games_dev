package com.nexttech.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class Menu {
	
	public String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	public boolean up, down, enter, pause;
	
	public void update() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "Novo Jogo") {
				Game.gameState = "NORMAL";
				pause = false;
			} else if(options[currentOption] == "Sair") {
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.RED);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString("Geral Game", (Game.WIDTH*Game.SCALE) / 2 - 110, (Game.HEIGHT*Game.SCALE) / 2 - 130);
		
		//Menu options
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 24));
		if(pause == false)
			g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE) / 2 - 60, (Game.HEIGHT*Game.SCALE) / 2 - 40);
		else
			g.drawString("Continuar", (Game.WIDTH*Game.SCALE) / 2 - 70, (Game.HEIGHT*Game.SCALE) / 2 - 40);
		g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE) / 2 - 90, (Game.HEIGHT*Game.SCALE) / 2 + 20);
		g.drawString("Sair", (Game.WIDTH*Game.SCALE) / 2 - 20, (Game.HEIGHT*Game.SCALE) / 2 + 80);
		
		if(options[currentOption] == "Novo Jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 105, (Game.HEIGHT*Game.SCALE) / 2 - 40);
		} else if(options[currentOption] == "Carregar Jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 125, (Game.HEIGHT*Game.SCALE) / 2 + 20);
		} else if(options[currentOption] == "Sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 55, (Game.HEIGHT*Game.SCALE) / 2 + 80);
		}
	}
	
	public void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for (int j = 0; j < value.length; j++) {
				value[j] += encode;
				current+=value[j];
			}
			try {
				write.write(current);
				if(i < val1.length - 1)
					write.newLine();
			} catch (IOException e) {}
		}
		try {
			write.flush();
			write.close();
		} catch (IOException e) {}
	}
}
