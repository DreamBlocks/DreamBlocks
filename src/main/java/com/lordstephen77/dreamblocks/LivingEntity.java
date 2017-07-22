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

public abstract class LivingEntity extends Entity {
	private static final long serialVersionUID = 1L;
	protected static final int maxHP = 100;
	
	public int hitPoints;
	public boolean climbing = false;
	public boolean facingRight = true;
	public Inventory inventory;
	
	protected final float walkSpeed = .1f;
	protected final float swimSpeed = .04f;
	protected float armLength = 4.5f;
	protected float moveDirection = 0;
	protected long ticksAlive = 0;
	protected int ticksUnderwater = 0;
	protected boolean jumping = false;
	
	public LivingEntity(boolean gravityApplies, float x, float y, int width, int height) {
		super(null, gravityApplies, x, y, width, height);
		this.hitPoints = maxHP;
		inventory = new Inventory(10, 4);
	}

	public boolean giveItem(Item item, int count) {
		return inventory.addItem(item, count) == 0;
	}
	
	public int airRemaining() {
		return Math.max(10 - (ticksUnderwater / 50), 0);
	}
	
	public void jump(World world, int tileSize) {
		if (jumping) {
			return;
		}
		
		if (!this.isInWater(world, tileSize)) {
			dy = -.3f;
			jumping = true;
		} else {
			dy = -maxWaterDY - .000001f;// BIG HACK
		}
	}
	
	@Override
	public void updatePosition(World world, int tileSize) {
		ticksAlive++;
		boolean isSwimClimb = this.isInWaterOrClimbable(world, tileSize);
		if (isSwimClimb) {
			dx = moveDirection * swimSpeed;
		} else {
			dx = moveDirection * walkSpeed;
		}
		if (climbing) {
			if (isSwimClimb) {
				jumping = false;
				dy = -maxWaterDY - .000001f;// BIG HACK
			} else {
				jump(world, tileSize);
			}
		}
		super.updatePosition(world, tileSize);
		if (this.dy == 0) {
			jumping = false;
		}
		if (this.isInWater(world, tileSize)) {
			jumping = false;
		}
		// drown check
		if (this.isHeadUnderWater(world, tileSize)) {
			ticksUnderwater++;
			if (this.airRemaining() == 0) {
				this.takeDamage(5);
				// back to about 4 bubbles' worth of air
				ticksUnderwater = 300;
			}
		} else {
			ticksUnderwater = 0;
		}
	}
	
	public void startLeft(boolean slow) {
		facingRight = false;
		if (slow) {
			moveDirection = -.2f;
		} else {
			moveDirection = -1;
		}
	}
	
	public void stopLeft() {
		if (moveDirection < 0) {
			moveDirection = 0;
		}
	}
	
	public void startRight(boolean slow) {
		facingRight = true;
		if (slow) {
			moveDirection = .2f;
		} else {
			moveDirection = 1;
		}
	}
	
	public void stopRight() {
		if (moveDirection > 0) {
			moveDirection = 0;
		}
	}
	
	public void startClimb() {
		climbing = true;
	}
	
	public void endClimb() {
		climbing = false;
	}
	
	public float findIntersection(float rayOx, float rayOy, float m, float p1x, float p1y,
			float p2x, float p2y) {
		float freeVar = -1;
		if (p1x == p2x)// segment is vertical
		{
			freeVar = -m * (rayOx - p1x) + rayOy;// y1
			if ((freeVar < p1y && freeVar < p2y) || (freeVar > p1y && freeVar > p2y)) {
				return -1;
			}
		} else if (p1y == p2y)// segment is horizontal
		{
			freeVar = -(rayOy - p1y) / m + rayOx;// x1
			if ((freeVar < p1x && freeVar < p2x) || (freeVar > p1x && freeVar > p2x)) {
				return -1;
			}
		} else {
			System.err.println("Find intersection -- bad arguments");
		}
		return freeVar;
	}
	
	@Override
	public void takeDamage(int amount) {
		this.hitPoints -= amount;
		// TODO: play sound, check for death
		System.out.println("Took " + amount + " damage. Current health = " + this.hitPoints);
	}
	
	@Override
	public void heal(int amount) {
		int newHP = this.hitPoints + amount;
		this.hitPoints = (newHP > maxHP) ? maxHP : newHP;
		System.out.println("Healed " + amount + ". Current health = " + this.hitPoints);
	}
}