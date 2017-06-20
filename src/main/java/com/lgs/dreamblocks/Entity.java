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

public abstract class Entity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	protected static final float gravityAcceleration = .03f;
	protected static final float waterAcceleration = .015f;
	protected static final float maxWaterDY = .05f;
	
	public float x;
	public float y;
	public float dx;
	public float dy;
	
	protected Sprite sprite;
	protected boolean gravityApplies;
	protected int widthPX;
	protected int heightPX;
	
	public Entity(String ref, boolean gravityApplies, float x, float y, int width, int height) {
		if (ref != null) {
			this.sprite = SpriteStore.get().getSprite(ref);
		}
		this.gravityApplies = gravityApplies;
		this.x = x;
		this.y = y;
		this.widthPX = width;
		this.heightPX = height;
		this.dx = this.dy = 0;
	}
	
	@Override
	protected Entity clone() throws CloneNotSupportedException {
		return (Entity) super.clone();
	}
	
	public void updatePosition(World world, int tileSize) {
		int pixels = (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)) * tileSize);
		
		boolean favorVertical = (Math.abs(dy) > Math.abs(dx));
		boolean hitTop = false;
		boolean hitBottom = false;
		
		float left = this.getLeft(tileSize);
		float right = this.getRight(tileSize);
		float top = this.getTop(tileSize);
		float bottom = this.getBottom(tileSize);
		
		boolean topLeft = true;
		boolean topRight = true;
		boolean bottomLeft = true;
		boolean bottomRight = true;
		boolean middleLeft = true;
		boolean middleRight = true;
		
		float scale = 1.f / pixels;
		
		if (favorVertical) {
			for (int i = 1; i <= pixels && topLeft && topRight && bottomLeft && bottomRight; i++) {
				top = top + dy * scale;
				bottom = bottom + dy * scale;
				
				topLeft = world.passable((int) left, (int) top);
				topRight = world.passable((int) right, (int) top);
				bottomLeft = world.passable((int) left, (int) bottom);
				bottomRight = world.passable((int) right, (int) bottom);
				middleLeft = world.passable((int) left, (int) (top + (bottom - top) / 2));
				middleRight = world.passable((int) right, (int) (top + (bottom - top) / 2));
				
				if (!(topLeft && topRight && bottomLeft && bottomRight && middleLeft && middleRight)) {
					hitTop |= !topLeft || !topRight;
					hitBottom |= !bottomLeft || !bottomRight;
					top = top - dy * scale;
					bottom = bottom - dy * scale;
				}
			}
			for (int i = 1; i <= pixels && topLeft && topRight && bottomLeft && bottomRight; i++) {
				left = left + dx * scale;
				right = right + dx * scale;
				
				topLeft = world.passable((int) left, (int) top);
				topRight = world.passable((int) right, (int) top);
				bottomLeft = world.passable((int) left, (int) bottom);
				bottomRight = world.passable((int) right, (int) bottom);
				middleLeft = world.passable((int) left, (int) (top + (bottom - top) / 2));
				middleRight = world.passable((int) right, (int) (top + (bottom - top) / 2));
				
				if (!(topLeft && topRight && bottomLeft && bottomRight && middleLeft && middleRight)) {
					left = left - dx * scale;
					right = right - dx * scale;
				}
			}
		} else {
			for (int i = 1; i <= pixels && topLeft && topRight && bottomLeft && bottomRight; i++) {
				left = left + dx * scale;
				right = right + dx * scale;
				
				topLeft = world.passable((int) left, (int) top);
				topRight = world.passable((int) right, (int) top);
				bottomLeft = world.passable((int) left, (int) bottom);
				bottomRight = world.passable((int) right, (int) bottom);
				middleLeft = world.passable((int) left, (int) (top + (bottom - top) / 2));
				middleRight = world.passable((int) right, (int) (top + (bottom - top) / 2));
				
				if (!(topLeft && topRight && bottomLeft && bottomRight && middleLeft && middleRight)) {
					left = left - dx * scale;
					right = right - dx * scale;
				}
			}
			for (int i = 1; i <= pixels && topLeft && topRight && bottomLeft && bottomRight; i++) {
				top = top + dy * scale;
				bottom = bottom + dy * scale;
				
				topLeft = world.passable((int) left, (int) top);
				topRight = world.passable((int) right, (int) top);
				bottomLeft = world.passable((int) left, (int) bottom);
				bottomRight = world.passable((int) right, (int) bottom);
				middleLeft = world.passable((int) left, (int) (top + (bottom - top) / 2));
				middleRight = world.passable((int) right, (int) (top + (bottom - top) / 2));
				
				if (!(topLeft && topRight && bottomLeft && bottomRight && middleLeft && middleRight)) {
					hitTop |= !topLeft || !topRight;
					hitBottom |= !bottomLeft || !bottomRight;
					top = top - dy * scale;
					bottom = bottom - dy * scale;
				}
			}
		}
		
		if (gravityApplies) {
			if (world.isClimbable((int) left, (int) top)
					|| world.isClimbable((int) right, (int) top)
					|| world.isClimbable((int) left, (int) bottom)
					|| world.isClimbable((int) right, (int) bottom)) {
				dy += waterAcceleration;
				if (dy > 0) {
					dy = Math.min(maxWaterDY, dy);
				} else {
					dy = Math.max(-maxWaterDY, dy);
				}
			} else {
				dy += gravityAcceleration;
			}
			if (hitBottom) {			
				// mathemagically derived to mimic the damage from
				//   counting the number of meters dropped
				int dmg = ((int) (114 * dy)) - 60;
				if (dmg > 0) {
					this.takeDamage(dmg);
				}
				dx *= 0.9;  // loss of energy due to friction
			}
		}
		if (hitTop) {
			dy = 0.0000001f;
		} else if (hitBottom) {
			dy = 0;
		}
		
		x = left;
		y = top;
	}
	
	public float getCenterY(int tileSize) {
		return y + (float) heightPX / (2 * tileSize);
	}
	
	public float getCenterX(int tileSize) {
		return x + (float) widthPX / (2 * tileSize);
	}
	
	public float getTop(int tileSize) {
		return y;
	}
	
	public float getBottom(int tileSize) {
		return (y + (float) (heightPX) / tileSize);
	}
	
	public float getLeft(int tileSize) {
		return x;
	}
	
	public float getRight(int tileSize) {
		return x + (float) (widthPX) / tileSize;
	}
	
	public boolean isInWater(World world, int tileSize) {
		int left = (int) this.getLeft(tileSize);
		int right = (int) this.getRight(tileSize);
		int top = (int) this.getTop(tileSize);
		int bottom = (int) this.getBottom(tileSize);
		return (world.isLiquid(left, top) || world.isLiquid(right, top)
				|| world.isLiquid(left, bottom) || world.isLiquid(right, bottom));
	}
	
	public boolean isHeadUnderWater(World world, int tileSize) {
		int top = (int) this.getTop(tileSize);
		int centerX = (int) this.getCenterX(tileSize);
		return world.isLiquid(centerX, top);
	}
	
	public boolean isInWaterOrClimbable(World world, int tileSize) {
		int left = (int) this.getLeft(tileSize);
		int right = (int) this.getRight(tileSize);
		int top = (int) this.getTop(tileSize);
		int bottom = (int) this.getBottom(tileSize);
		return (world.isLiquid(left, top) || world.isLiquid(right, top)
				|| world.isLiquid(left, bottom) || world.isLiquid(right, bottom)
				|| world.isClimbable(left, top) || world.isClimbable(right, top)
				|| world.isClimbable(left, bottom) || world.isClimbable(right, bottom));
	}
	
	public boolean collidesWith(Entity entity, int tileSize) {
		float left1, left2;
		float right1, right2;
		float top1, top2;
		float bottom1, bottom2;
		
		left1 = this.x;
		left2 = entity.x;
		right1 = this.getRight(tileSize);
		right2 = entity.getRight(tileSize);
		top1 = this.y;
		top2 = entity.y;
		bottom1 = this.getBottom(tileSize);
		bottom2 = entity.getBottom(tileSize);
		
		return !(bottom1 < top2 || top1 > bottom2 || right1 < left2 || left1 > right2);
	}

	public boolean inBoundingBox(Int2 pos, int tileSize) {
		int left = (int) this.getLeft(tileSize);
		int right = (int) this.getRight(tileSize);
		int top = (int) this.getTop(tileSize);
		int bottom = (int) this.getBottom(tileSize);
		
		return pos.x >= left && pos.x <= right && pos.y >= top && pos.y <= bottom;
	}
	
	public void draw(GraphicsHandler g, float cameraX, float cameraY, int screenWidth,
			int screenHeight, int tileSize) {
		Int2 pos = StockMethods.computeDrawLocationInPlace(cameraX, cameraY, screenWidth,
				screenHeight, tileSize, x, y);
		if (StockMethods.onScreen) {
			sprite.draw(g, pos.x, pos.y, widthPX, heightPX);
		}
	}
	
	// Only living entities have hitpoints; they should override these methods.
	public void takeDamage(int amount) {
	}
	
	public void heal(int amount) {
	}
}
