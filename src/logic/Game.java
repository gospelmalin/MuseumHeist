package logic;

import java.io.FileNotFoundException;
import java.io.IOException;

import handler.Level;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import settings.Settings;

public class Game {
	private Pane root;
	private Canvas canvas;
	private GraphicsContext gc;
	
	private Level levelHandler = new Level();
	private Object[][] level;
	
	private int tileSize = Settings.getTileSize();
	private int width = Settings.getWidth();
	private int height = Settings.getHeight();
	private int xOffset = Settings.getOffsetX();
	private int yOffset = Settings.getOffsetY();
	
	private Tile floorTile = new Floor();
	private Tile wallTile = new Wall();
	
	private static Node player;
	private static Node laser;
	private static Node treasure;
	private static Node door;
	
	private static Node wall;
	private static Node floor;
	
	//TODO Create the gameboard, need tiles and entitys to make it easier
	public Pane init(String name) throws FileNotFoundException {
		root = new Pane();
		canvas = new Canvas(width,height);
		gc = canvas.getGraphicsContext2D();
		
		try {
			level = levelHandler.getLevel(name);
		} catch (IOException e) {
			throw new FileNotFoundException("Could not locate " + name);
		}
		
		BackgroundImage bgImg = new BackgroundImage(new Image(floorTile.getImg()), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(tileSize, tileSize, false, false, false, false));
		root.setBackground(new Background(bgImg));
		
		// Create the outer walls of the playfield
		// The playfield is 25x25 and walls is outside of playfield
		// So we need to loop 2 more
		for(int y=0;y<level.length+2;y++) {
			for(int x = 0; x<level.length+2;x++) {
				// If we are at the edges then we add a wall
				if(y == 0 || y == level.length + 1) {
					wall = mew ImageView(new Image(wallTile.getImg()));
					// Relocate wall to the right place
					// The playfield is column and cell based so we need to translate
					// cell index to x y in pixels based on window size and tile size
					wall.relocate((x*tileSize) + xOffset - tileSize, (y*tileSize)+yOffset-tileSize);
					root.getChildren().add(wall);
				}
				// If we are at the far right we add walls
				if(x == level.length+1) {
					wall = new ImageView(new Image(wallTile.getImg()));
					wall.relocate((x * tileSize) + xOffset - tileSize, (y * tileSize) + yOffset - tileSize);
					root.getChildren().add(wall);
				}
			}
			// Far left wall
			wall = new ImageView(new Image(wallTile.getImg()));
			wall.relocate((0 * tileSize) + xOffset- tileSize, (y * tileSize) + yOffset - tileSize);
			root.getChildren().add(wall);
		}
		
		return root;
	}
}
