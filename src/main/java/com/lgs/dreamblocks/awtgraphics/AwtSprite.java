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


package com.lgs.dreamblocks.awtgraphics;

import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.lgs.dreamblocks.Color;
import com.lgs.dreamblocks.GraphicsHandler;
import com.lgs.dreamblocks.SpriteStore;

/**
 * A sprite to be displayed on the screen. Note that a sprite
 * contains no state information, i.e. its just the image and
 * not the location. This allows us to use a single sprite in
 * lots of different places without having to store multiple
 * copies of the image.
 * 
 * @author Kevin Glass
 */
public class AwtSprite implements com.lgs.dreamblocks.Sprite {
	private static final long serialVersionUID = 1L;
	
	/** The image to be drawn for this sprite */
	transient public Image image;
	public String ref;
	
	// for serialization loading
	public AwtSprite() {
		AwtSprite s = (AwtSprite) SpriteStore.get().getSprite(ref);
		this.image = s.image;
	}
	
	/**
	 * Create a new sprite based on an image
	 * 
	 * @param image
	 *            The image that is this sprite
	 */
	public AwtSprite(Image image, String ref) {
		this.image = image;
		this.ref = ref;
	}
	
	/**
	 * Get the width of the drawn sprite
	 * 
	 * @return The width in pixels of this sprite
	 */
	public int getWidth() {
		return image.getWidth(null);
	}
	
	/**
	 * Get the height of the drawn sprite
	 * 
	 * @return The height in pixels of this sprite
	 */
	public int getHeight() {
		return image.getHeight(null);
	}
	
	/**
	 * Draw the sprite onto the graphics context provided
	 * 
	 * @param g
	 *            The graphics context on which to draw the sprite
	 * @param x
	 *            The x location at which to draw the sprite
	 * @param y
	 *            The y location at which to draw the sprite
	 */
	public void draw(GraphicsHandler g, int x, int y) {
		g.drawImage(this, x, y);
	}
	
	public void draw(GraphicsHandler g, int x, int y, Color tint) {
		g.drawImage(this, x, y, tint);
	}
	
	public void draw(GraphicsHandler g, int x, int y, int width, int height) {
		g.drawImage(this, x, y, width, height);
	}
	
	public void draw(GraphicsHandler g, int x, int y, int width, int height, Color tint) {
		g.drawImage(this, x, y, width, height, tint);
	}
	
	/**
	 * Always treat de-serialization as a full-blown constructor, by
	 * validating the final state of the de-serialized object.
	 */
	@Override
	public void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException,
			IOException {
		// always perform the default de-serialization first
		// aInputStream.defaultReadObject();
		ref = (String) aInputStream.readObject();
		this.image = ((AwtSprite) AwtSpriteStore.get().getSprite(ref)).image;
	}
	
	/**
	 * This is the default implementation of writeObject.
	 * Customise if necessary.
	 */
	@Override
	public void writeObject(ObjectOutputStream aOutputStream) throws IOException {
		// perform the default serialization for all non-transient, non-static fields
		aOutputStream.writeObject(ref);
		aOutputStream.defaultWriteObject();
	}
}