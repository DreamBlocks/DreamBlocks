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

import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.lgs.dreamblocks.MainGame;

public class AwtEventsHandler {
	MainGame game;
	
	public AwtEventsHandler(MainGame game, Canvas canvas) {
		this.game = game;
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		canvas.addKeyListener(new KeyInputHandler());
		canvas.addMouseListener(new MouseInputHander());
		canvas.addMouseWheelListener(new MouseWheelInputHander());
		canvas.addMouseMotionListener(new MouseMoveInputHander());
		
		// TODO: A lot of this should be calling a nicer function in Game to handle
		// mouse+keyboard/touch input
	}
	
	private class MouseWheelInputHander implements MouseWheelListener {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int notches = e.getWheelRotation();
			game.hotbar.moveSelectionRight(notches);
		}
	}
	
	private class MouseMoveInputHander implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent arg0) {
			game.screenMousePos.x = arg0.getX();
			game.screenMousePos.y = arg0.getY();
		}
		
		@Override
		public void mouseMoved(MouseEvent arg0) {
			game.screenMousePos.x = arg0.getX();
			game.screenMousePos.y = arg0.getY();
		}
	}
	
	private class MouseInputHander extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent arg0) {
			switch (arg0.getButton()) {
			case MouseEvent.BUTTON1:
				game.leftClick = true;
				break;
			case MouseEvent.BUTTON2: // fall through
			case MouseEvent.BUTTON3:
				game.rightClick = true;
				break;
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			switch (arg0.getButton()) {
			case MouseEvent.BUTTON1:
				game.leftClick = false;
				break;
			case MouseEvent.BUTTON2: // fall through
			case MouseEvent.BUTTON3:
				game.rightClick = false;
				break;
			}
		}
	}
	
	private class KeyInputHandler extends KeyAdapter {
		/**
		 * Notification from AWT that a key has been pressed. Note that
		 * a key being pressed is equal to being pushed down but *NOT*
		 * released. Thats where keyTyped() comes in.
		 * 
		 * @param e
		 *            The details of the key that was pressed
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			// Jump
			case KeyEvent.VK_W:
			// climb e jump
			case KeyEvent.VK_SPACE:
				game.player.startClimb();
				break;
			// move left
			case KeyEvent.VK_A:
				game.player.startLeft(e.isShiftDown());
				break;
			// move right
			case KeyEvent.VK_D:
				game.player.startRight(e.isShiftDown());
				break;
			}
		}
		
		/**
		 * Notification from AWT that a key has been released.
		 * 
		 * @param e
		 *            The details of the key that was released
		 */
		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
			case KeyEvent.VK_SPACE:
				game.player.endClimb();
				break;
			case KeyEvent.VK_A:
				game.player.stopLeft();
				break;
			case KeyEvent.VK_D:
				game.player.stopRight();
				break;
			// pressed ESC to return to the game menu
			case KeyEvent.VK_ESCAPE:
				if (game.isInInventory()) {
					game.closeInventory();
				} else {
					game.goToMainMenu();
				}
				break;
			}
		}
		
		// block selection in the hotbar via numeric keys on the keyboard
		public void keyTyped(KeyEvent e) {
			switch (e.getKeyChar()) {
			case '1':
			case '2': // these all fall through to 9
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			    game.hotbar.setHotbarIdx(e.getKeyChar() - '1');
				break;
			case '0':
			    game.hotbar.setHotbarIdx(9);
				break;
			case 'e': // inventory + crafting system
				if(game.isInInventory()){
					game.closeInventory();
				} else {
					game.openInventory();
				}
				break;
			case '+': // press + increase zoom
				game.zoom(1);
				break;
			case 'p': // game pause
				game.paused = !game.paused;
				break;
			case 'm': // music stop/play
				game.musicPlayer.toggleSound();
				break;
			case 'o': // restore zoom default
				game.zoom(0);
				break;
			case '-': // press - decrease zoom
				game.zoom(-1);
				break;
			case 'f': // view fps + memory free
				game.viewFPS = !game.viewFPS;
				break;
			case 'q':
				game.tossItem();
				break;
			}
		}
	}
}
