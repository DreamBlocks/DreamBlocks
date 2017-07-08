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

import com.lordstephen77.dreamblocks.Constants.TileID;

public class TileType implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String spriteid;
	protected TileID id;
	protected boolean passable;
	protected boolean liquid;
	protected int opacity;
	protected int brightness;
	private char breaksInto;

	public TileType(String spriteid, TileID id, char breaksInto) {
		this(spriteid, id, false, false, Constants.OPAQUE, breaksInto);
	}
	
	public TileType(String spriteid, TileID id, boolean passable, boolean liquid, int opacity, char breaksInto) {
		this(spriteid, id, passable, liquid, opacity, 0, breaksInto);
	}
	
	public TileType(String spriteid, TileID id, boolean passable, boolean liquid, int opacity,
					int brightness, char breaksInto) {
		this.spriteid = spriteid;
		this.id = id;
		this.passable = passable;
		this.liquid = liquid;
		this.opacity = opacity;
		this.brightness = brightness;
		this.breaksInto = breaksInto;
	}

	public String getSpriteid() {
		return spriteid;
	}

	public char getBreaksInto() {
		return breaksInto;
	}

	public TileID getId(){
		return id;
	}

	public int getOpacity() {
		return opacity;
	}

	public int getBrightness() {
		return brightness;
	}
}
