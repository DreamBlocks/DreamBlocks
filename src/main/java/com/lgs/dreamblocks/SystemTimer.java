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

import com.dnsalias.java.timer.AdvancedTimer;

//AdvancedTimer;

/**
 * A wrapper class that provides timing methods. This class
 * provides us with a central location where we can add
 * our current timing implementation. Initially, we're going to
 * rely on the GAGE timer. (@see http://java.dnsalias.com)
 * 
 * @author Kevin Glass
 */
public class SystemTimer {
	/** Our link into the GAGE timer library */
	private static AdvancedTimer timer = new AdvancedTimer();
	/** The number of "timer ticks" per second */
	private static long timerTicksPerSecond;
	
	/** A little initialization at startup, we're just going to get the GAGE timer going */
	static {
		timer.start();
		timerTicksPerSecond = AdvancedTimer.getTicksPerSecond();
	}
	
	/**
	 * Get the high resolution time in milliseconds
	 * 
	 * @return The high resolution time in milliseconds
	 */
	public static long getTime() {
		// we get the "timer ticks" from the high resolution timer
		// multiply by 1000 so our end result is in milliseconds
		// then divide by the number of ticks in a second giving
		// us a nice clear time in milliseconds
		
		return (timer.getClockTicks() * 1000) / timerTicksPerSecond;
	}
	
	/**
	 * Sleep for a fixed number of milliseconds.
	 * 
	 * @param duration
	 *            The amount of time in milliseconds to sleep for
	 */
	public static void sleep(long duration) {
		timer.sleep((duration * timerTicksPerSecond) / 1000);
	}
}