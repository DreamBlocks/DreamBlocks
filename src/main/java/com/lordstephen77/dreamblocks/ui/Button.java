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

import com.lordstephen77.dreamblocks.Int2;
import com.lordstephen77.dreamblocks.Sprite;

import java.awt.Rectangle;

/**
 * Created by Александр on 19.06.2017.
 */
public class Button implements Resizable {
    private Sprite spriteHover;
    private Sprite spriteDefault;
    private Rectangle bounds;

    public Button(int y, int width, int height, Sprite spriteDefault, Sprite spriteHover){
        this.bounds = new Rectangle(0, y, width, height);
        this.spriteHover = spriteHover;
        this.spriteDefault = spriteDefault;
    }

    public Sprite getSprite(Int2 mousePos){
        if (isInside(mousePos.x, mousePos.y)){
            return spriteHover;
        } else {
            return spriteDefault;
        }
    }

    public Rectangle getBounds(){
        return this.bounds;
    }

    @Override
    public void resize(int screenWidth, int screenHeight){
        this.bounds.x = screenWidth / 2 - this.bounds.width / 2;
    }

    public boolean isInside(int cx, int cy){
        return this.bounds.contains(cx, cy);
    }
}
