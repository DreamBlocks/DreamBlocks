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
	protected TileID name;
	protected boolean passable;
	protected boolean liquid;
	protected int lightBlocking;
	protected int lightEmitting;

	public TileType(String spriteid, TileID name) {
		this(spriteid, name, false, false, Constants.LIGHT_VALUE_OPAQUE);
	}
	
	public TileType(String spriteid, TileID name, boolean passable, boolean liquid, int lightBlocking) {
		this(spriteid, name, passable, liquid, lightBlocking, 0);
	}
	
	public TileType(String spriteid, TileID name, boolean passable, boolean liquid, int lightBlocking,
					int lightEmitting) {
		this.spriteid = spriteid;
		this.name = name;
		this.passable = passable;
		this.liquid = liquid;
		this.lightBlocking = lightBlocking;
		this.lightEmitting = lightEmitting;
	}

	public String getSpriteid() {
		return spriteid;
	}
}
