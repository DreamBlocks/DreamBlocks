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

package com.lgs.dreamblocks.ui;

import com.lgs.dreamblocks.GraphicsHandler;
import com.lgs.dreamblocks.Sprite;

/**
 * Created by Александр on 19.06.2017.
 */
public class Button {
    private Sprite spriteHover;
    private Sprite spriteDefault;
    private int x;
    private int width;
    private int height;
    private int y;

    public Button(int y, int width, int height, Sprite spriteDefault, Sprite spriteHover){
        this.x = 0;
        this.y = y;
        this.width = width;
        this.height = height;
        this.spriteHover = spriteHover;
        this.spriteDefault = spriteDefault;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void drawHover(GraphicsHandler g){
        this.x = g.getScreenWidth() / 2 - width / 2;
        spriteHover.draw(g, x, y, width, height);
    }

    public  void drawUp(GraphicsHandler g){
        this.x = g.getScreenWidth() / 2 - width / 2;
        spriteDefault.draw(g, x, y, width, height);
    }

    public boolean isInside(int cx, int cy){
        return x <= cx && cx <= x + width
            && y <= cy && cy <= y + height;
    }
}
