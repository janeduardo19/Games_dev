package com.nexttech.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.nexttech.entities.*;
import com.nexttech.main.Game;

public class World {
	
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					switch (pixelAtual) {
						case 0xFF000000:
							//Floor
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
							break;
						case 0xFFFFF6F6:
							//Wall
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
							break;
						case 0xFF091ED9:
							//Player
							Game.player.setX(xx*16);
							Game.player.setY(yy*16);
							break;
						case 0xFFF80000:
							//Enemy
							Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN));
							break;
						case 0xFF6C6C6C:
							//Weapon
							Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
							break;
						case 0xFFE77878:
							//Life Pack
							Game.entities.add(new Lifepack(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN));
							break;
						case 0xFFFFEC07:
							//Bullet
							Game.entities.add(new Bullet(xx*16, yy*16, 16, 16, Entity.BULLET_EN));
							break;
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		//Inicio camera
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		//Fim camera
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
