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

package com.lordstephen77.dreamblocks;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import com.lordstephen77.dreamblocks.Constants.TileID;
import com.lordstephen77.dreamblocks.fillers.TreeFiller;
import com.lordstephen77.dreamblocks.fillers.WaterFiller;
import com.lordstephen77.dreamblocks.light.LightingEngine;
import com.lordstephen77.dreamblocks.light.SourceBlocks;
import com.lordstephen77.dreamblocks.light.Sun;
import com.lordstephen77.dreamblocks.ui.*;

/**
 * <p>Main class</p>
 * @author Stefano Peris, Sonya Sitnikova, Kryukov Alexander
 * @version 0.3
 */
public class MainGame implements Resizable {
	
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
    private boolean inInventory = false;
    private final SpriteStore spriteStore;
	private final TileStore tileStore;
	private final StartMenu startMenu;
	private final NewGameMenu newGameMenu;
	public final Hotbar hotbar;
	public long ticksRunning;
	private Random random = new Random();
	
	public Player player;
	public World world;
	private LightingEngine lightingEngineSun;
	private LightingEngine lightingEngineSourceBlocks;
	
	public MusicPlayer musicPlayer = new MusicPlayer("sounds/music.ogg");
	public Int2 screenMousePos = new Int2(0, 0);
	
	/**
	 * Construct our game and set it running.
	 */
	public MainGame() {
	    spriteStore = SpriteStore.get();
		tileStore = new TileStore();
		startMenu = new StartMenu(this, spriteStore);
		newGameMenu = new NewGameMenu(this, spriteStore);
		int tileSize = 16;
        int margin = 10;
        hotbar = new Hotbar(spriteStore, tileSize, margin);
		GraphicsHandler.get().init(this);
		System.gc();
	}
	
	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	public void loadSprite(){
		lightingEngineSun = new Sun(world);
		lightingEngineSourceBlocks = new SourceBlocks(world);
        resize(GraphicsHandler.get().getScreenWidth(), GraphicsHandler.get().getScreenHeight());
		// load sprites
		builderIcon = spriteStore.getSprite("sprites/other/builder.png");
		minerIcon = spriteStore.getSprite("sprites/other/miner.png");
		fullHeart = spriteStore.getSprite("sprites/other/full_heart.png");
		halfHeart = spriteStore.getSprite("sprites/other/half_heart.png");
		emptyHeart = spriteStore.getSprite("sprites/other/empty_heart.png");
		bubble = spriteStore.getSprite("sprites/other/bubble.png");
		// there's no empty bubble image, so we'll just use this for now
		emptyBubble = spriteStore.getSprite("sprites/other/bubble_pop2.png");

		breakingSprites = new Sprite[8];
		for (int i = 0; i < 8; i++) {
			breakingSprites[i] = spriteStore.getSprite("sprites/tiles/break" + i + ".png");
		}

		musicPlayer.play();
		System.gc();
	}

	public void startGameLoad(int worldWidth){
		inMenu = false;
		System.out.println("Loading world, width: " + worldWidth);
		entities.clear();
		if(SaveLoad.doLoad(this)) {// check to see loading is possible (and if so load)
			for (Entity entity : entities) {
				if (entity instanceof Player) {
					player = (Player) entity;
					hotbar.setInventory(player.inventory);
					player.widthPX = 7 * (tileSize / 8);
				player.heightPX = 14 * (tileSize / 8);
				}
			}
		}
		loadSprite();
	}

	public void startGameNew(int width){
		inMenu = false;
		System.out.println("Creating world, width: " + width);
		worldWidth = width;
		entities.clear();
		// make a new world and player
		WorldGenerator gen = new WorldGenerator(worldWidth, worldHeight, random);

		int seed = random.nextInt();
		System.out.println("Seed: " + seed);
		random.setSeed(seed);
		world = new World(worldWidth, worldHeight, random);
		world.fillWith(tileStore, gen);
		WaterFiller waterFiller = new WaterFiller(world, random, tileStore);
		waterFiller.fill();
		TreeFiller treeFiller = new TreeFiller(world, random, tileStore);
		treeFiller.fill();
		player = new Player(true, world.spawnLocation.x, world.spawnLocation.y, + 7 * (tileSize / 8), 14 * (tileSize / 8));
		hotbar.setInventory(player.inventory);
		entities.add(player);
		if (Constants.DEBUG) {
			player.giveItem(Constants.itemTypes.get((char) 175).clone(), 1);
			player.giveItem(Constants.itemTypes.get((char) 88).clone(), 1);
			player.giveItem(Constants.itemTypes.get((char) 106).clone(), 64);
		}
		loadSprite();
	}
	
	public void drawCenteredX(GraphicsHandler g, Sprite s, int top, int width, int height) {
		g.drawImage(s, g.getScreenWidth() / 2 - width / 2, top, width, height);
	}

	public void processStartMenu(GraphicsHandler g){
		startMenu.draw(g);
		if (!leftClick){
			return;
		}
		switch (startMenu.handleClick(screenMousePos.x, screenMousePos.y)){
			case LOAD_GAME:
				startGameLoad(WORLD_WIDTH.MEDIUM.getWidth());
				break;
			case NEW_GAME:
				newGame = true;
				break;
			case QUIT_GAME:
				quit();
				break;
		default:
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
			startGameNew(result.get().getWidth());
			newGame = false;
			leftClick = false;
		}
	}

	@Override
	public void resize(int screenWidth, int screenHeight){
		if(player != null) {
			player.inventory.resize(screenWidth, screenHeight);
		}
		newGameMenu.resize(screenWidth, screenHeight);
		startMenu.resize(screenWidth, screenHeight);
	}

	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		
		if (Constants.DEBUG) {
			startGameNew(512);
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
			
			world.chunkUpdate(lightingEngineSun, lightingEngineSourceBlocks, tileStore);
			world.draw(g, spriteStore, 0, 0, screenWidth, screenHeight, cameraX, cameraY, tileSize, lightingEngineSun, lightingEngineSourceBlocks, tileStore);

            if (isInInventory()) {
                boolean inventoryFocus = player.inventory.handleClick(screenMousePos, leftClick, rightClick);
                if (inventoryFocus) {
                    leftClick = false;
                    rightClick = false;
                }
            }

			if (leftClick && player.handBreakPos.x != -1) {
				processLeftClick(cameraX, cameraY, g);
			} else {
				breakingTicks = 0;
			}

			if (rightClick) {
				processRightClick();
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
				entity.draw(g, spriteStore, cameraX, cameraY, screenWidth, screenHeight, tileSize);
			}

			if (viewFPS) {
				methodViewFPS(delta, g);
			}

			if (player.handBreakPos.x != -1) {
				drawUI(cameraX, cameraY, g);
			}

            if (isInInventory()){
                player.inventory.draw(g, spriteStore, screenMousePos);
            }
			hotbar.draw(g, screenWidth, screenHeight);

			// draw the mouse
			Int2 mouseTest = StockMethods.calculatePosition(worldMouseX, worldMouseY, cameraX, cameraY, tileSize);
			drawMouse(g, mouseTest);
			drawHeartsForHealthBar(screenWidth, screenHeight, g);

			if (player.isHeadUnderWater(world, tileSize)) {
				int heartY = screenHeight - 50;
				drawAirBubbles(screenWidth, g, heartY);
			}

			g.finishDrawing();

			SystemTimer.sleep(lastLoopTime + 16 - SystemTimer.getTime());
		}
	}

	public void processLeftClick(float cameraX, float cameraY, GraphicsHandler g){
		if (player.handBreakPos.equals(breakingPos)) {
			breakingTicks++;
		} else {
			breakingTicks = 0;
		}
		breakingPos = player.handBreakPos;

		InventoryItem inventoryItem = hotbar.getSelected();
		Item item = inventoryItem.getItem();
		int ticksNeeded = world.breakTicks(breakingPos.x, breakingPos.y, item);

		Int2 pos = StockMethods.calculatePosition(breakingPos.x, breakingPos.y, cameraX, cameraY, tileSize);
		int sprite_index = (int) (Math.min(1, (double) breakingTicks / ticksNeeded) * (breakingSprites.length - 1));
		g.drawImage(breakingSprites[sprite_index], pos.x, pos.y, tileSize, tileSize);

		if (breakingTicks >= ticksNeeded) {
			if (item != null && item.getClass() == Tool.class) {
				Tool tool = (Tool) item;
				tool.uses++;
				if (tool.uses >= tool.totalUses) {
					inventoryItem.setEmpty();
				}
			}
			breakingTicks = 0;
			TileType oldTileType = world.removeTile(player.handBreakPos, lightingEngineSun, lightingEngineSourceBlocks);
            //90% to produce sapling
            boolean canProduceSapling = oldTileType.id == TileID.LEAVES && random.nextDouble() < .1;
            boolean canProduceItem = Constants.itemTypes.containsKey(oldTileType.getBreaksInto());

			if (canProduceItem || canProduceSapling) {
                Item newItem = Constants.itemTypes.get(oldTileType.getBreaksInto());
                newItem = (Item) newItem.clone();
                newItem.x = player.handBreakPos.x + random.nextFloat()
                        * (1 - (float) newItem.widthPX / tileSize);
                newItem.y = player.handBreakPos.y + random.nextFloat()
                        * (1 - (float) newItem.widthPX / tileSize);
                newItem.dy = -.07f;
                entities.add(newItem);
            }
		}
	}

	public void processRightClick(){
		if (world.isCraft(player.handBreakPos.x, player.handBreakPos.y)) {
            openWorkbenchInventory();
		} else {
			placeBlock();
		}
	}

	public void placeBlock (){
		rightClick = false;
		InventoryItem current = hotbar.getSelected();
		if (!current.isEmpty()) {
			TileID itemID = TileID.of(current.getItem().item_id);
			if (itemID != TileID.NONE) {
				boolean isPassable = tileStore.tileTypes.get(itemID).passable;

				if (isPassable || !player.inBoundingBox(player.handBuildPos, tileSize)) {
					if (world.addTile(player.handBuildPos, itemID, lightingEngineSun, lightingEngineSourceBlocks, tileStore)) {
						// placed successfully
						hotbar.decreaseSelected(1);
					}
				}
			}
		}
	}

	/**
	 * <p>FPS - view frames per second</p>
	 * @param delta
	 * @param g
	 */
	public void methodViewFPS(long delta, GraphicsHandler g){
		String fps = "Fps: " + 1 / ((float) delta / 1000) + "("
				+ Runtime.getRuntime().freeMemory() / 1024 / 1024 + " / "
				+ Runtime.getRuntime().totalMemory() / 1024 / 1024 + ") Free MB";
		g.setColor(Color.white);
		g.drawString(fps, 10, 20);
	}

	/**
	 *
	 * @param cameraX
	 * @param cameraY
	 * @param g
	 */
	public void drawUI(float cameraX, float cameraY, GraphicsHandler g){
		Int2 pos = StockMethods.calculatePosition(player.handBuildPos.x, player.handBuildPos.y, cameraX, cameraY, tileSize);
		g.drawImage(builderIcon, pos.x, pos.y, tileSize, tileSize);

		pos = StockMethods.calculatePosition(player.handBreakPos.x, player.handBreakPos.y, cameraX, cameraY, tileSize);
		g.drawImage(minerIcon, pos.x, pos.y, tileSize, tileSize);
	}

	/**
	 * <p>Draw the bar of life (hearts)</p>
	 * @param screenWidth
	 * @param screenHeight
	 * @param g
	 */
	public void drawHeartsForHealthBar(final int screenWidth, final int screenHeight, GraphicsHandler g){
		int heartX = (screenWidth - 250) / 2;
		int heartY = screenHeight - 60;
		for (int heartIdx = 1; heartIdx <= 10; ++heartIdx) {
			int hpDiff = player.hitPoints - heartIdx * 10;
			if (hpDiff >= 0) {
				g.drawImage(fullHeart, heartX, heartY, 10, 10);
			} else if (hpDiff >= -5) {
				g.drawImage(halfHeart, heartX, heartY, 10, 10);
			} else {
				g.drawImage(emptyHeart, heartX, heartY, 10, 10);
			}
			heartX += 15;
		}
	}

	/**
	 * <p>Draw the bar of air-bubbles (bubble)</p>
	 * @param screenWidth
	 * @param g
	 * @param heartY
	 */
	public void drawAirBubbles(final int screenWidth, GraphicsHandler g, int heartY){
		int bubbleX = (screenWidth + 60) / 2;
		int numBubbles = player.airRemaining();
		for (int bubbleIdx = 1; bubbleIdx <= 10; ++bubbleIdx) {
			if (bubbleIdx <= numBubbles) {
				g.drawImage(bubble, bubbleX, heartY, 10, 10); // Scaling draw
			} else {
				g.drawImage(emptyBubble, bubbleX, heartY, 10, 10); // Scaling draw
			}
			bubbleX += 15; // separator
		}
	}

	/**
	 *
	 * @param g
	 * @param pos
	 */
	public void drawMouse(GraphicsHandler g, Int2 pos) {
		g.setColor(Color.white);
		g.fillOval(pos.x - 4, pos.y - 4, 8, 8);
		g.setColor(Color.black);
		g.fillOval(pos.x - 3, pos.y - 3, 6, 6);
	}

	/**
	 *
	 * @param g
	 * @param sprite
	 * @param tileSize
	 */
	public void drawTileBackground(GraphicsHandler g, Sprite sprite, int tileSize) {
		for (int i = 0; i <= GraphicsHandler.get().getScreenWidth() / tileSize; i++) {
			for (int j = 0; j <= GraphicsHandler.get().getScreenHeight() / tileSize; j++) {
				g.drawImage(sprite, i * tileSize, j * tileSize, tileSize, tileSize);
			}
		}
	}

	/**
	 *
	 * @param level
	 */
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
		InventoryItem inventoryItem = hotbar.getSelected();
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
	public void openWorkbenchInventory(){
		player.inventory.setTableSizeAvailable(3);
		inInventory = true;
	}
	public void openPlayerInventory(){
		player.inventory.setTableSizeAvailable(2);
	    inInventory = true;
    }
    public void closeInventory(){
        inInventory = false;
    }

    public boolean isInInventory() {
        return inInventory;
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
