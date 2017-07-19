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

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public final class StockMethods {
	public static boolean isOnScreen(Int2 pos, int tileSize, float width, float height){
		return !(pos.x + tileSize < 0 || pos.x > width * tileSize || pos.y + tileSize < 0 || pos.y > height
				* tileSize);
	}

	public static Int2 calculatePosition(float positionX, float positionY, float cameraX, float cameraY, int tileSize){
		return new Int2(
			Math.round((positionX - cameraX) * tileSize),
			Math.round((positionY - cameraY) * tileSize)
		);
	}
	
	public static String readFile(String pathname) throws IOException {
		// NOTE: drops newlines
		StringBuilder fileContents = new StringBuilder();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(pathname);
		if (in == null) {
			throw new IOException("no resource found at " + pathname);
		}
		Scanner scanner = new Scanner(in);
		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine());
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}
	
	/**
	 * Smoothly interpolates between edge0 and edge1 by x
	 * 
	 * This function plays like a sigmoid but is easier to compute
	 * @param edge0
	 * @param edge1
	 * @param x
	 */
	public static float smoothStep(float edge0, float edge1, float x) {
		float t = clamp((x - edge0) / (edge1 - edge0), 0f, 1f);
		return t * t * (3f - 2f * t);
	}
	
	/**
	 * Clamps x to values [a,b]
	 * @param x
	 * @param a
	 * @param b
	 */
	public static float clamp(float x, float a, float b) {
		if (x < a) {
			return a;
		} else if (x > b) {
			return b;
		} else {
			return x;
		}
	}
}
