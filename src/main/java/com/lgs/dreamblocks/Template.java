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
Github: https://github.com/LordStephen77/SkyBlocks-Beyond-of-Time

"DreamBlocks - Beyond of Time" is free software: you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

"DreamBlocks - Beyond of Time" is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.lgs.dreamblocks;

public class Template implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public int outCount;
	public Int2 position = new Int2(0, 0);
	
	private char[][] matrix;
	
	public Template(int[][] matrix, int outCount) {
		if (matrix != null) {
			// temporary workaround while we convert templates to int[][]
			this.matrix = new char[matrix.length][matrix[0].length];
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					this.matrix[i][j] = (char) matrix[i][j];
				}
			}
		}
		this.outCount = outCount;
	}
	
	public boolean compare(char[][] input) {
		if (matrix == null) {
			return false;
		}
		// boolean foundSomething = false;
		for (int x = 0; x <= (input.length - matrix.length); x++) {
			for (int y = 0; y <= (input[0].length - matrix[0].length); y++) {
				boolean isGood = false;
				boolean isBad = false;
				// now try the template here -- some bruteforcing
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[0].length; j++) {
						if (matrix[i][j] != input[x + i][y + j]) {
							if (input[x + i][y + j] != 0) {
								return false;
							}
							if (matrix[i][j] != 0 && input[x + i][y + j] == 0) {
								isBad = true;
							}
						} else if (input[x + i][y + j] != 0 && matrix[i][j] != 0) {
							isGood = true;
						}
					}
				}
				
				if (isGood && !isBad) {
					position.x = x;
					position.y = y;
					return true;
				}
				
			}
		}
		
		return false;
	}
}
