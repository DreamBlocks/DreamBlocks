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

public class InventoryItem implements java.io.Serializable {
	private static final long serialVersionUID = -2389571032163510795L;
	
	public final int maxCount = 64;
	public int count = 0;
	public Item item;

	public InventoryItem() {
		this.setItem(null);
	}
	
	// returns left overs
	public int add(Item item, int count) {
		if (this.isEmpty()) {
			this.setItem(item);
		}
		if (this.getItem().item_id != item.item_id) {
			return count;
		}
		int maxCount = this.maxCount;
		if (this.getItem().getClass() == Tool.class) {
			maxCount = 1;
		}
		if (this.getCount() + count <= maxCount) {
			this.setCount(this.getCount() + count);
			return 0;
		} else {
			int leftOver = count - (maxCount - this.getCount());
			this.setCount(maxCount);
			return leftOver;
		}
	}
	
	// returns left overs
	public int remove(int count) {
		if (0 <= this.getCount() - count) {
			this.setCount(this.getCount() - count);
			return 0;
		} else if (this.getCount() == count) {
			this.setEmpty();
			return 0;
		} else {
			int leftOver = count - this.getCount();
			this.setEmpty();
			return leftOver;
		}
	}
	
	public void setEmpty() {
		this.setCount(0);
		this.setItem(null);
	}
	
	public boolean isEmpty() {
		return this.getCount() == 0 || this.getItem() == null;
	}
	
	public boolean isFull() {
		return getCount() >= maxCount;
	}
	
	public void stack(InventoryItem other) {
		if (other.getItem().getClass() != Tool.class) {
			int result = this.add(other.getItem(), other.getCount());
			other.remove(other.getCount() - result);
		}
	}

	public void draw(GraphicsHandler g, int x, int y, int tileSize){
		if (this.getCount() <= 0) {
			return;
		}
		SpriteStore spriteStore = SpriteStore.get();
		Sprite sprite = spriteStore.getSprite(item.getSpriteId());
		g.drawImage(sprite, x, y, tileSize, tileSize);
	}

	public void draw(GraphicsHandler g, int x, int y, int tileSize, int seperation) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x + seperation - 2, y + seperation - 2, tileSize + 4, tileSize + 4);
		if (this.getCount() <= 0) {
			return;
		}
		SpriteStore spriteStore = SpriteStore.get();
		Sprite sprite = spriteStore.getSprite(item.getSpriteId());
		g.drawImage(sprite, x + seperation, y + seperation, tileSize, tileSize);
		if (this.getCount() > 1) {
			g.setColor(Color.white);
			g.drawString("" + this.getCount(), x + seperation, y + seperation + tileSize / 2);
		}
		if (item.getClass() == Tool.class) {
			Tool tool = (Tool) item;
			if (tool.uses != 0) {
				int left = x + seperation + 2;
				int width = (int) (((float) (tool.totalUses - tool.uses) / tool.totalUses) * (tileSize));
				int top = y + seperation + tileSize - 4;
				int height = 2;
				g.setColor(Color.green);
				g.fillRect(left, top, width, height);
			}
		}
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}

	public void handleLeftClick(InventoryItem holding){
		if (holding.isEmpty()) {
			pickWholeStack(holding);
		} else if (this.item == null) {
			dropWholeStackToEmptyTile(holding);
		} else if (holding.item.item_id == this.item.item_id && this.count < maxCount) {
			if ((holding.item.getClass() != Tool.class) && (this.item.getClass() != Tool.class)) {
				dropStackToStack(holding);
			}
		} else {
			swapItems(holding);
		}
	}

	public void handleRightClick(InventoryItem holding){
		if (holding.isEmpty()) {
			if (this.count > 1) {
				pickHalfOfStack(holding);
			} else {
				pickWholeStack(holding);
			}
		} else if (this.item == null) {
			dropSingleItemToEmptyTile(holding);
		} else if (holding.item.item_id == this.item.item_id && this.count < maxCount) {
			if ((holding.item.getClass() != Tool.class) && (this.item.getClass() != Tool.class)) {
				dropSingleItemToStack(holding);
			}
		} else {
			swapItems(holding);
		}
	}

	public void pickHalfOfStack(InventoryItem hand){
		hand.item = this.item;
		hand.count = (int) Math.ceil((double) this.count / 2);
		this.count = (int) Math.floor((double) this.count / 2);
	}

	public void pickWholeStack(InventoryItem hand){
		hand.item = this.item;
		hand.count = this.count;
		this.item = null;
		this.count = 0;
	}

	public void dropSingleItemToEmptyTile(InventoryItem hand){
		this.item = hand.item;
		this.count = 1;
		hand.count--;
		if (hand.count <= 0) {
			hand.item = null;
		}
	}

	public void dropWholeStackToEmptyTile(InventoryItem hand){
		this.item = hand.item;
		this.count = hand.count;
		hand.item = null;
		hand.count = 0;
	}

	public void dropSingleItemToStack(InventoryItem hand){
		this.count++;
		hand.count--;
		if (hand.count <= 0) {
			hand.item = null;
		}
	}

	public void dropStackToStack(InventoryItem hand){
		this.count += hand.count;
		if (this.count > maxCount) {
			hand.count = maxCount - this.count;
			this.count = maxCount;
		} else {
			hand.item = null;
			hand.count = 0;
		}
	}

	public void swapItems(InventoryItem hand){
		Item item = this.item;
		int count = this.count;
		this.item = hand.item;
		this.count = hand.count;
		hand.item = item;
		hand.count = count;
	}
}
