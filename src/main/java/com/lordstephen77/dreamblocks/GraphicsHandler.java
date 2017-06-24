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

import com.lordstephen77.dreamblocks.awtgraphics.AwtGraphicsHandler;

public abstract class GraphicsHandler {
	public static final boolean awtMode = true;
	
	protected static int screenWidth = 800;
	protected static int screenHeight = 600;
	
	private static GraphicsHandler single;
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public static GraphicsHandler get() {
		if (single == null) {
			if (awtMode) {
				single = new AwtGraphicsHandler();
			} else {
				// android!
			}
		}
		return single;
	}
	
	public abstract void init(MainGame game);
	
	public abstract void startDrawing();
	
	public abstract void finishDrawing();
	
	public abstract void setColor(Color color);
	
	public abstract void fillRect(int x, int y, int width, int height);
	
	public abstract void drawString(String string, int x, int y);
	
	public abstract void fillOval(int x, int y, int width, int height);
	
	public abstract void drawImage(Sprite sprite, int x, int y);

	public abstract void drawImage(Sprite sprite, int x, int y, Color tint);
	
	public abstract void drawImage(Sprite sprite, int x, int y, int width, int height);
	
	public abstract void drawImage(Sprite sprite, int x, int y, int width, int height, Color tint);
}
