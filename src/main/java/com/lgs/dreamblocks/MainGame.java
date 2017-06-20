/*
 _(`-')      (`-')  (`-')  _(`-')  _ <-. (`-')  <-.(`-')                               <-.(`-')  (`-').->
( (OO ).-><-.(OO )  ( OO).-/(OO ).-/    \(OO )_  __( OO)    <-.        .->    _         __( OO)  ( OO)_
 \    .'_ ,------,)(,------./ ,---.  ,--./  ,-.)'-'---.\  ,--. )  (`-')----.  \-,-----.'-'. ,--.(_)--\_)
 '`'-..__)|   /`. ' |  .---'| \ /`.\ |   `.'   || .-. (/  |  (`-')( OO).-.  '  |  .--./|  .'   //    _ /
 |  |  ' ||  |_.' |(|  '--. '-'|_.' ||  |'.'|  || '-' `.) |  |OO )( _) | |  | /_) (`-')|      /)\_..`--.
 |  |  / :|  .   .' |  .--'(|  .-.  ||  |   |  || /`'.  |(|  '__ | \|  |)|  | ||  |OO )|  .   ' .-._)   \
 |  '-'  /|  |\  \  |  `---.|  | |  ||  |   |  || '--'  / |     |'  '  '-'  '(_'  '--'\|  |\   \\       /
 `------' `--' '--' `------'`--' `--'`--'   `--'`------'  `-----'    `-----'    `-----'`--' '--' `-----'
                                     _                    _ ___
                                    |_) _    _ ._  _|  __|_  |o._ _  _
                                    |_)(/_\/(_)| |(_| (_)|   ||| | |(/_
                                          /


Copyright (C) 2017 Stefano Peris

nickname: LordStephen77
eMail: lordstephen77@gmail.com
Github: https://github.com/DreamBlocks

is free software: you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.lgs.dreamblocks;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import com.lgs.dreamblocks.Constants.TileID;
import com.lgs.dreamblocks.ui.NewGameMenu;
import com.lgs.dreamblocks.ui.StartMenu;
import com.lgs.dreamblocks.ui.WORLD_WIDTH;

public class MainGame {
	
	private int worldWidth = 512;
	private int worldHeight = 256;
	private boolean gameRunning = true;
	public boolean leftClick = false;
	public boolean rightClick = false;
	public boolean paused = true;
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private int tileSize = 32;
	
	private int breakingTicks;
	private Int2 breakingPos;
	
	private Sprite builderIcon;
	private Sprite minerIcon;
	private Sprite[] breakingSprites;
	private Sprite fullHeart, halfHeart, emptyHeart, bubble, emptyBubble;
	
	public boolean viewFPS = false;
	private boolean inMenu = true;
	private boolean newGame = false;
	private StartMenu startMenu;
	private NewGameMenu newGameMenu;
	public long ticksRunning;
	private Random random = new Random();
	
	public Player player;
	public World world;
	
	public MusicPlayer musicPlayer = new MusicPlayer("sounds/music.ogg");
	public Int2 screenMousePos = new Int2(0, 0);
	
	/**
	 * Construct our game and set it running.
	 */
	public MainGame() {
		startMenu = new StartMenu(this);
		newGameMenu = new NewGameMenu(this);
		GraphicsHandler.get().init(this);
		System.gc();
	}
	
	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	public void startGame(boolean load, int width) {
		inMenu = false;
		if (load) {
			System.out.println("Loading world, width: " + worldWidth);
		} else {
			System.out.println("Creating world, width: " + width);
			worldWidth = width;
		}
		
		entities.clear();
		if (load) {
			// check to see loading is possible (and if so load)
			load = SaveLoad.doLoad(this);
		}
		
		if (load) {
			for (Entity entity : entities) {
				if (entity instanceof Player) {
					player = (Player) entity;
					player.widthPX = 7 * (tileSize / 8);
					player.heightPX = 14 * (tileSize / 8);
				}
			}
		}
		if (!load) {
			// make a new world and player
			world = new World(worldWidth, worldHeight, random);
			player = new Player(true, world.spawnLocation.x, world.spawnLocation.y,
					7 * (tileSize / 8), 14 * (tileSize / 8));
			entities.add(player);
			if (Constants.DEBUG) {
				player.giveItem(Constants.itemTypes.get((char) 175).clone(), 1);
				player.giveItem(Constants.itemTypes.get((char) 88).clone(), 1);
				player.giveItem(Constants.itemTypes.get((char) 106).clone(), 64);
			}
		}
		
		// load sprites
		final SpriteStore ss = SpriteStore.get();
		builderIcon = ss.getSprite("sprites/other/builder.png");
		minerIcon = ss.getSprite("sprites/other/miner.png");
		fullHeart = ss.getSprite("sprites/other/full_heart.png");
		halfHeart = ss.getSprite("sprites/other/half_heart.png");
		emptyHeart = ss.getSprite("sprites/other/empty_heart.png");
		bubble = ss.getSprite("sprites/other/bubble.png");
		// there's no empty bubble image, so we'll just use this for now
		emptyBubble = ss.getSprite("sprites/other/bubble_pop2.png");
		
		breakingSprites = new Sprite[8];
		for (int i = 0; i < 8; i++) {
			breakingSprites[i] = ss.getSprite("sprites/tiles/break" + i + ".png");
		}
		
		musicPlayer.play();
		System.gc();
	}
	
	public void drawCenteredX(GraphicsHandler g, Sprite s, int top, int width, int height) {
		s.draw(g, g.getScreenWidth() / 2 - width / 2, top, width, height);
	}

	public void processStartMenu(GraphicsHandler g){
		startMenu.draw(g);
		if (!leftClick){
			return;
		}
		switch (startMenu.handleClick(screenMousePos.x, screenMousePos.y)){
			case LOAD_GAME:
				startGame(true, WORLD_WIDTH.MEDIUM.getWidth());
				break;
			case NEW_GAME:
				newGame = true;
				break;
			case QUIT_GAME:
				quit();
				break;
		}
		leftClick = false;
	}

	public void processNewGameMenu(GraphicsHandler g) {
		newGameMenu.draw(g);
		if (!leftClick) {
			return;
		}
		Optional<WORLD_WIDTH> result = newGameMenu.handleClick(screenMousePos.x, screenMousePos.y);
		if (result.isPresent()) {
			startGame(false, result.get().getWidth());
			newGame = false;
			leftClick = false;
		}
	}
	
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		
		if (Constants.DEBUG) {
			startGame(false, 512);
		}
		
		// keep looping round till the game ends
		while (gameRunning) {
			ticksRunning++;
			long delta = SystemTimer.getTime() - lastLoopTime;
			lastLoopTime = SystemTimer.getTime();
			
			GraphicsHandler g = GraphicsHandler.get();
			g.startDrawing();
			
			if (inMenu) {
				if (newGame){
					processNewGameMenu(g);
				} else {
					processStartMenu(g);
				}
				drawMouse(g, screenMousePos);
				g.finishDrawing();
				
				SystemTimer.sleep(lastLoopTime + 16 - SystemTimer.getTime());
				continue;
			}
			final int screenWidth = g.getScreenWidth();
			final int screenHeight = g.getScreenHeight();
			float cameraX = player.x - screenWidth / tileSize / 2;
			float cameraY = player.y - screenHeight / tileSize / 2;
			float worldMouseX = (cameraX * tileSize + screenMousePos.x) / tileSize;
			float worldMouseY = (cameraY * tileSize + screenMousePos.y) / tileSize - .5f;
			
			world.chunkUpdate();
			world.draw(g, 0, 0, screenWidth, screenHeight, cameraX, cameraY, tileSize);
			
			boolean inventoryFocus = player.inventory.updateInventory(screenWidth, screenHeight,
					screenMousePos, leftClick, rightClick);
			if (inventoryFocus) {
				leftClick = false;
				rightClick = false;
			}
			
			if (leftClick && player.handBreakPos.x != -1) {
				if (player.handBreakPos.equals(breakingPos)) {
					breakingTicks++;
				} else {
					breakingTicks = 0;
				}
				breakingPos = player.handBreakPos;
				
				InventoryItem inventoryItem = player.inventory.selectedItem();
				Item item = inventoryItem.getItem();
				int ticksNeeded = world.breakTicks(breakingPos.x, breakingPos.y, item);
				
				Int2 pos = StockMethods.computeDrawLocationInPlace(cameraX, cameraY, tileSize,
						tileSize, tileSize, breakingPos.x, breakingPos.y);
				int sprite_index = (int) (Math.min(1, (double) breakingTicks / ticksNeeded) * (breakingSprites.length - 1));
				breakingSprites[sprite_index].draw(g, pos.x, pos.y, tileSize, tileSize);
				
				if (breakingTicks >= ticksNeeded) {
					if (item != null && item.getClass() == Tool.class) {
						Tool tool = (Tool) item;
						tool.uses++;
						if (tool.uses >= tool.totalUses) {
							inventoryItem.setEmpty();
						}
					}
					
					breakingTicks = 0;
					TileID name = world.removeTile(player.handBreakPos.x, player.handBreakPos.y);
					if (name == TileID.GRASS) {
						name = TileID.DIRT;
					}
					if (name == TileID.STONE) {
						name = TileID.COBBLE;
					}
					if (name == TileID.LEAVES && random.nextDouble() < .1) {
						name = TileID.SAPLING;
					}
					Item newItem = Constants.itemTypes.get((char) name.breaksInto);
					if (newItem != null) // couldn't find that item
					{
						newItem = (Item) newItem.clone();
						newItem.x = player.handBreakPos.x + random.nextFloat()
								* (1 - (float) newItem.widthPX / tileSize);
						newItem.y = player.handBreakPos.y + random.nextFloat()
								* (1 - (float) newItem.widthPX / tileSize);
						newItem.dy = -.07f;
						entities.add(newItem);
					}
				}
			} else {
				breakingTicks = 0;
			}
			
			if (rightClick) {
				if (world.isCraft(player.handBreakPos.x, player.handBreakPos.y)) {
					// clicked on a crafting table
					// expand this to any item with a GUI
					player.inventory.tableSizeAvailable = 3;
					player.inventory.setVisible(true);
				} else {
					// placing a block
					rightClick = false;
					InventoryItem current = player.inventory.selectedItem();
					if (!current.isEmpty()) {
						TileID itemID = Constants.tileIDs.get(current.getItem().item_id);
						boolean isPassable = Constants.tileTypes.get(itemID).type.passable;
						
						if (isPassable || !player.inBoundingBox(player.handBuildPos, tileSize)) {
							if (world.addTile(player.handBuildPos, itemID)) {
								// placed successfully
								player.inventory.decreaseSelected(1);
							}
						}
					}
				}
			}
			
			player.updateHand(g, cameraX, cameraY, worldMouseX, worldMouseY, world, tileSize);
			
			java.util.Iterator<Entity> it = entities.iterator();
			while (it.hasNext()) {
				Entity entity = it.next();
				if (entity != player && player.collidesWith(entity, tileSize)) {
					if (entity instanceof Item || entity instanceof Tool) {
						player.giveItem((Item) entity, 1);
					}
					it.remove();
					continue;
				}
				entity.updatePosition(world, tileSize);
				entity.draw(g, cameraX, cameraY, screenWidth, screenHeight, tileSize);
			}
			
			if (viewFPS) {
				String fps = "Fps: " + 1 / ((float) delta / 1000) + "("
						+ Runtime.getRuntime().freeMemory() / 1024 / 1024 + " / "
						+ Runtime.getRuntime().totalMemory() / 1024 / 1024 + ") Free MB";
				g.setColor(Color.white);
				g.drawString(fps, 10, 10);
			}
			
			// Draw the UI
			if (player.handBreakPos.x != -1) {
				Int2 pos = StockMethods.computeDrawLocationInPlace(cameraX, cameraY, tileSize,
						tileSize, tileSize, player.handBuildPos.x, player.handBuildPos.y);
				builderIcon.draw(g, pos.x, pos.y, tileSize, tileSize);
				
				pos = StockMethods.computeDrawLocationInPlace(cameraX, cameraY, tileSize, tileSize,
						tileSize, player.handBreakPos.x, player.handBreakPos.y);
				minerIcon.draw(g, pos.x, pos.y, tileSize, tileSize);
			}
			
			// draw the hotbar, and optionally the inventory screen
			player.inventory.draw(g, screenWidth, screenHeight);
			
			// draw the mouse
			Int2 mouseTest = StockMethods.computeDrawLocationInPlace(cameraX, cameraY, tileSize,
					tileSize, tileSize, worldMouseX, worldMouseY);
			drawMouse(g, mouseTest);
			
			// HACK: draw hearts for health bar
			// TODO: move this elsewhere, don't use so many magic constants
			int heartX = (screenWidth - 250) / 2;
			int heartY = screenHeight - 50;
			for (int heartIdx = 1; heartIdx <= 10; ++heartIdx) {
				int hpDiff = player.hitPoints - heartIdx * 10;
				if (hpDiff >= 0) {
					fullHeart.draw(g, heartX, heartY, 10, 10);
				} else if (hpDiff >= -5) {
					halfHeart.draw(g, heartX, heartY, 10, 10);
				} else {
					emptyHeart.draw(g, heartX, heartY, 10, 10);
				}
				heartX += 15;
			}
			
			if (player.isHeadUnderWater(world, tileSize)) {
				// another HACK: draw air bubbles
				int bubbleX = (screenWidth + 50) / 2;
				int numBubbles = player.airRemaining();
				for (int bubbleIdx = 1; bubbleIdx <= 10; ++bubbleIdx) {
					if (bubbleIdx <= numBubbles) {
						bubble.draw(g, bubbleX, heartY, 10, 10);
					} else {
						emptyBubble.draw(g, bubbleX, heartY, 10, 10);
					}
					bubbleX += 15;
				}
			}
			
			g.finishDrawing();
			
			SystemTimer.sleep(lastLoopTime + 16 - SystemTimer.getTime());
		}
	}
	
	public void drawMouse(GraphicsHandler g, Int2 pos) {
		g.setColor(Color.white);
		g.fillOval(pos.x - 4, pos.y - 4, 8, 8);
		g.setColor(Color.black);
		g.fillOval(pos.x - 3, pos.y - 3, 6, 6);
	}
	
	public void drawTileBackground(GraphicsHandler g, Sprite sprite, int tileSize) {
		for (int i = 0; i <= GraphicsHandler.get().getScreenWidth() / tileSize; i++) {
			for (int j = 0; j <= GraphicsHandler.get().getScreenHeight() / tileSize; j++) {
				sprite.draw(g, i * tileSize, j * tileSize, tileSize, tileSize);
			}
		}
	}
	
	public void zoom(int level) {
		if (level == 0) {
			if (tileSize < 32) {
				zoom(1);
				zoom(0);
			}
			if (tileSize > 32) {
				zoom(-1);
				zoom(0);
			}
		} else if (level == 1) {
			if (tileSize < 128) {
				tileSize = tileSize * 2;
				for (Entity entity : entities) {
					entity.widthPX *= 2;
					entity.heightPX *= 2;
				}
				for (Item item : Constants.itemTypes.values()) {
					item.widthPX *= 2;
					item.heightPX *= 2;
				}
			}
		} else if (level == -1) {
			if (tileSize > 8) {
				tileSize = tileSize / 2;
				for (Entity entity : entities) {
					entity.widthPX /= 2;
					entity.heightPX /= 2;
				}
				for (Item item : Constants.itemTypes.values()) {
					item.widthPX /= 2;
					item.heightPX /= 2;
				}
			}
		}
	}
	
	public void tossItem() {
		// TODO: move this into Player
		InventoryItem inventoryItem = player.inventory.selectedItem();
		if (!inventoryItem.isEmpty()) {
			Item newItem = inventoryItem.getItem();
			if (!(newItem instanceof Tool)) {
				newItem = (Item) newItem.clone();
			}
			inventoryItem.remove(1);
			if (player.facingRight) {
				newItem.x = player.x + 1 + random.nextFloat();
			} else {
				newItem.x = player.x - 1 - random.nextFloat();
			}
			newItem.y = player.y;
			newItem.dy = -.1f;
			entities.add(newItem);
		}
	}
	
	public void goToMainMenu() {
		zoom(0);
		SaveLoad.doSave(this);
		musicPlayer.pause();
		inMenu = true; // go back to the main startMenu
	}
	
	public void quit() {
		musicPlayer.close();
		System.exit(0);
	}
	
	/**
	 * The entry point into the game. We'll simply create an
	 * instance of class which will start the display and game
	 * loop.
	 * 
	 * @param argv
	 *            The arguments that are passed into our game
	 */
	public static void main(String argv[]) {
		// really simple argument parsing
		for (String arg : argv) {
			if (arg.equals("-d") || arg.equals("--debug")) {
				Constants.DEBUG = true;
			} else {
				System.err.println("Unrecognized argument: "+arg);
			}
		}
		// initialize the game state
		MainGame g = new MainGame();
		
		// Start the main game loop, note: this method will not
		// return until the game has finished running. Hence we are
		// using the actual main thread to run the game.
		g.gameLoop();
	}
}
