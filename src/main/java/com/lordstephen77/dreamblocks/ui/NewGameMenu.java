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

import java.util.Optional;

import com.lordstephen77.dreamblocks.GraphicsHandler;
import com.lordstephen77.dreamblocks.MainGame;
import com.lordstephen77.dreamblocks.Sprite;
import com.lordstephen77.dreamblocks.SpriteStore;

/**
 * Created by Александр on 19.06.2017.
 */
public class NewGameMenu {

    /* menu sprites */
    private final Button menu_mini;
    private final Button menu_medium;
    private final Button menu_big;

    private final Sprite menu_bgTile = SpriteStore.get().getSprite("sprites/tiles/cobble.png");
    private final Sprite menu_logo = SpriteStore.get().getSprite("sprites/menus/title.png");
    private final Sprite menu_tag = SpriteStore.get().getSprite("sprites/menus/tag.png");

    private MainGame game;

    public NewGameMenu(MainGame g) {
        this.game = g;
        SpriteStore ss = SpriteStore.get();
        menu_mini = new Button(150, 160, 64, ss.getSprite("sprites/menus/mini_up.png"), ss.getSprite("sprites/menus/mini_down.png"));
        menu_medium = new Button(250, 160, 64, ss.getSprite("sprites/menus/med_up.png"), ss.getSprite("sprites/menus/med_down.png"));
        menu_big = new Button(350, 160, 64, ss.getSprite("sprites/menus/big_up.png"), ss.getSprite("sprites/menus/big_down.png"));
    }

    // menu title + animated logo
    public void draw(GraphicsHandler g) {
        game.drawTileBackground(g, menu_bgTile, 32);
        game.drawCenteredX(g, menu_logo, 70, 397, 50);
        float tagScale = ((float) Math.abs((game.ticksRunning % 100) - 50)) / 50 + 1;
        menu_tag.draw(g, 610, 60, (int) (60 * tagScale), (int) (37 * tagScale));

        int mouseX = game.screenMousePos.x;
        int mouseY = game.screenMousePos.y;
        if (menu_mini.isInside(mouseX, mouseY)) {
            menu_mini.drawHover(g);
        } else {
            menu_mini.drawUp(g);
        }
        if (menu_medium.isInside(mouseX, mouseY)){
            menu_medium.drawHover(g);
        } else {
            menu_medium.drawUp(g);
        }
        if (menu_big.isInside(mouseX, mouseY)){
            menu_big.drawHover(g);
        } else {
            menu_big.drawUp(g);
        }
    }

    public Optional<WORLD_WIDTH> handleClick(int x, int y){
        if(menu_mini.isInside(x, y)){
            return Optional.of(WORLD_WIDTH.MINI);
        } else if (menu_medium.isInside(x, y)){
            return Optional.of(WORLD_WIDTH.MEDIUM);
        } else if (menu_big.isInside(x, y)){
            return Optional.of(WORLD_WIDTH.BIG);
        } else {
            return Optional.empty();
        }
    }
}
