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

public class Player extends LivingEntity {
	private static final long serialVersionUID = 1L;
	
	public Int2 handBreakPos = new Int2(0, 0);
	public Int2 handBuildPos = new Int2(0, 0);
	public float handStartX;
	public float handStartY;
	public float handEndX;
	public float handEndY;

	private String leftWalkSpriteId;
	private String rightWalkSpriteId;
	
	public Player(boolean gravityApplies, float x, float y, int width, int height) {
		super(gravityApplies, x, y, width, height);

		leftWalkSpriteId = "sprites/entities/left_man.png";
		rightWalkSpriteId = "sprites/entities/right_man.png";
		spriteId = "sprites/entities/player.gif";
	}

	public void updateHand(GraphicsHandler g, float cameraX, float cameraY, float mouseX,
			float mouseY, World world, int tileSize) {

		float x = .5f + (int) this.getCenterX(tileSize);
		float y = .5f + (int) this.getCenterY(tileSize);
		
		handStartX = x;
		handStartY = y;
		
		float tMax = (float) Math.ceil(armLength);
		int hitX = -1;
		int hitY = -1;
		handEndX = -1;
		handEndY = -1;
		
		handBuildPos.x = -1;
		handBuildPos.y = -1;
		
		float m;
		if (x - mouseX == 0) {
			m = Float.MAX_VALUE;
		} else {
			m = (y - mouseY) / (x - mouseX);
		}
		
		for (float i = 0; i <= Math.ceil(armLength) * 2; i++) {
			for (float j = 0; j <= Math.ceil(armLength) * 2; j++) {
				float px = (float) (x - Math.ceil(armLength) + i) - .5f;
				float py = (float) (y - Math.ceil(armLength) + j) - .5f;
				if (!world.isBreakable((int) px, (int) py)) {
					continue;
				}
				
				float down = -1;
				float left = -1;
				float up = -1;
				float right = -1;
				
				float downY = py + 1;
				float rightX = px + 1;
				float upY = py - 1;
				float leftX = px;
				
				if ((x >= px && x >= mouseX) && // left
						(y >= py && y >= mouseY)) // up
				{
					right = findIntersection(x, y, m, px + 1, py, px + 1, py + 1);
					down = findIntersection(x, y, m, px, py + 1, px + 1, py + 1);
				} else if ((x - .5f <= px && x <= mouseX) && // right
						(y - .5f >= py && y >= mouseY)) // up
				{
					left = findIntersection(x, y, m, px, py, px, py + 1);
					down = findIntersection(x, y, m, px, py + 1, px + 1, py + 1);
				} else if ((x >= px && x >= mouseX) && // left
						(y - 1 < py && y <= mouseY)) // down
				{
					right = findIntersection(x, y, m, px + 1, py, px + 1, py + 1);
					up = findIntersection(x, y, m, px, py, px + 1, py);
				} else if ((x - .5f <= px && x <= mouseX) && // right
						(y - .5f <= py && y <= mouseY)) // down
				{
					left = findIntersection(x, y, m, px, py, px, py + 1);
					up = findIntersection(x, y, m, px, py, px + 1, py);
				} else {
					continue;
				}
				
				if (down != -1 || left != -1 || up != -1 || right != -1) {
					// Int2 pos = StockMethods.computeDrawLocationInPlace(cameraX, cameraY,
					// tileSize, tileSize, tileSize, x, y);
					
					// int posX = pos.x;
					// int posY = pos.y;
					// pos = StockMethods.computeDrawLocationInPlace(cameraX, cameraY, tileSize,
					// tileSize, tileSize, px, py);
					// g.setColor(Color.red);
					// g.drawLine(pos.x, pos.y, posX, posY);
					// SpriteStore.get().getSprite("sprites/tiles/diamondore.png").draw(g, pos.x,
					// pos.y, tileSize, tileSize);
					
					float newTMax = (float) Math.sqrt(Math.pow(Math.abs(x) - Math.abs(px), 2)
							+ Math.pow(Math.abs(y) - Math.abs(py), 2));
					if (newTMax >= tMax) {
						continue;
					}
					
					if (up != -1) {
						handEndX = up;
						handEndY = upY;
						handBuildPos.x = (int) px;
						handBuildPos.y = (int) py - 1;
					}
					if (down != -1) {
						handEndX = down;
						handEndY = downY;
						handBuildPos.x = (int) px;
						handBuildPos.y = (int) py + 1;
					}
					if (left != -1) {
						handEndX = leftX;
						handEndY = left;
						handBuildPos.x = (int) px - 1;
						handBuildPos.y = (int) py;
					}
					if (right != -1) {
						handEndX = rightX;
						handEndY = right;
						handBuildPos.x = (int) px + 1;
						handBuildPos.y = (int) py;
					}
					
					hitX = (int) px;
					hitY = (int) py;
					
					tMax = newTMax;
				}
			}
		}
		handBreakPos.x = hitX;
		handBreakPos.y = hitY;
	}
	
	@Override
	public void draw(GraphicsHandler g, SpriteStore spriteStore, float cameraX, float cameraY, int screenWidth,
			int screenHeight, int tileSize) {
		Int2 pos = StockMethods.computeDrawLocationInPlace(cameraX, cameraY, screenWidth,
				screenHeight, tileSize, x, y);
		if (StockMethods.onScreen) {
			int frame = (int) x % 4;// (int) ((ticksAlive/20)%4);
			if (facingRight) {
				if (frame == 0 || frame == 2 || dx <= 0) {
					Sprite sprite = spriteStore.getSprite(spriteId);
					g.drawImage(sprite, pos.x, pos.y, widthPX, heightPX);
				} else if (frame == 1) {
					Sprite rightWalkSprite = spriteStore.getSprite(rightWalkSpriteId);
					g.drawImage(rightWalkSprite, pos.x, pos.y, widthPX, heightPX);
				} else {
					Sprite leftWalkSprite = spriteStore.getSprite(leftWalkSpriteId);
					g.drawImage(leftWalkSprite, pos.x, pos.y, widthPX, heightPX);
				}
			} else {
				if (frame == 0 || frame == 2 || dx >= 0) {
					Sprite sprite = spriteStore.getSprite(spriteId);
					g.drawImage(sprite, pos.x + widthPX, pos.y, -widthPX, heightPX);
				} else if (frame == 1) {
					Sprite rightWalkSprite = spriteStore.getSprite(rightWalkSpriteId);
					g.drawImage(rightWalkSprite, pos.x + widthPX, pos.y, -widthPX, heightPX);
				} else {
					Sprite leftWalkSprite = spriteStore.getSprite(leftWalkSpriteId);
					g.drawImage(leftWalkSprite, pos.x + widthPX, pos.y, -widthPX, heightPX);
				}
			}
		}
	}
}
