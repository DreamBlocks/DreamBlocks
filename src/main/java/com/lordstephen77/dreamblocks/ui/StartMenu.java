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


package com.lordstephen77.dreamblocks.ui;

import com.lordstephen77.dreamblocks.GraphicsHandler;
import com.lordstephen77.dreamblocks.MainGame;
import com.lordstephen77.dreamblocks.Sprite;
import com.lordstephen77.dreamblocks.SpriteStore;


/**
 * <p>Main menu definition class (first screen)</p>
 * @author Stefano Peris, Александр
 * @version 0.3
 */
public class StartMenu implements Resizable {
	
	/* menu sprites */
	private final Sprite menu_bgTile;
	private final Sprite menu_logo;
	private final Button menuNew;
	private final Button menuLoad;
	private final Button menuQuit;
	private final Sprite menu_tag;

	private MainGame game;
	
	public StartMenu(MainGame mainGame, SpriteStore ss) {
		this.game = mainGame;
		menu_bgTile = ss.getSprite("sprites/tiles/wood.png");
		menu_logo = ss.getSprite("sprites/menus/title.png");
		menu_tag = ss.getSprite("sprites/menus/tag.png");
		menuNew = new Button(200, 160, 64, ss.getSprite("sprites/menus/new_up.png"),  ss.getSprite("sprites/menus/new_down.png"));
		menuLoad = new Button(300, 160, 64, ss.getSprite("sprites/menus/load_up.png"), ss.getSprite("sprites/menus/load_down.png"));
		menuQuit = new Button(400, 160, 64, ss.getSprite("sprites/menus/quit_up.png"), ss.getSprite("sprites/menus/quit_down.png"));
	}

	@Override
	public void resize(int screenWidth, int screenHeight){
		menuNew.resize(screenWidth, screenHeight);
		menuLoad.resize(screenWidth, screenHeight);
		menuQuit.resize(screenWidth, screenHeight);
	}

	// menu title + animated logo
	public void draw(GraphicsHandler g) {
		game.drawTileBackground(g, menu_bgTile, 60);
		game.drawCenteredX(g, menu_logo, 70, 397, 50);
		float tagScale = ((float) Math.abs((game.ticksRunning % 100) - 50)) / 50 + 1;
		g.drawImage(menu_tag, 610, 60, (int) (60 * tagScale), (int) (37 * tagScale));

		g.drawImage(menuNew.getSprite(game.screenMousePos), menuNew.getBounds());
		g.drawImage(menuLoad.getSprite(game.screenMousePos), menuLoad.getBounds());
		g.drawImage(menuQuit.getSprite(game.screenMousePos), menuQuit.getBounds());
	}

	public START_MENU_DIALOG_RESULT handleClick(int x, int y){
	    if (menuNew.isInside(x, y)){
	        return START_MENU_DIALOG_RESULT.NEW_GAME;
        } else if (menuLoad.isInside(x, y)){
            return START_MENU_DIALOG_RESULT.LOAD_GAME;
        } else if (menuQuit.isInside(x, y)){
            return START_MENU_DIALOG_RESULT.QUIT_GAME;
        } else {
			return START_MENU_DIALOG_RESULT.NOTHING_CLICKED;
		}
    }
}
