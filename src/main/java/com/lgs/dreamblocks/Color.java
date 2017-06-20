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

public class Color implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// From AWT Color constant values
	public static final Color white = new Color(255, 255, 255);
	public static final Color darkGray = new Color(64, 64, 64);
	public static final Color black = new Color(0, 0, 0);
	public static final Color green = new Color(0, 255, 0);
	public static final Color gray = new Color(128, 128, 128);
	public static final Color blue = new Color(0, 0, 255);
	public static final Color LIGHT_GRAY = new Color(192, 192, 192);
	public static final Color DARK_GRAY = darkGray;
	public static final Color orange = new Color(255, 200, 0);
	
	public int R, G, B, A;
	
	public Color(int R, int G, int B) {
		this.R = R;
		this.G = G;
		this.B = B;
		this.A = 255;
	}

	public Color(int R, int G, int B, int A) {
		this.R = R;
		this.G = G;
		this.B = B;
		this.A = A;
	}
	
	// returns a new color, interpolated toward c by amount (in range [0,1])
	public Color interpolateTo(Color c, float amount) {
		int dR = (int) (amount * (c.R - this.R));
		int dG = (int) (amount * (c.G - this.G));
		int dB = (int) (amount * (c.B - this.B));
		return new Color(this.R + dR, this.G + dG, this.B + dB, this.A);
	}
}
