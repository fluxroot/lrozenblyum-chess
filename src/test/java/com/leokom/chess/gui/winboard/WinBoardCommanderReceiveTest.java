package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 13.11.12 21:33
 */
public class WinBoardCommanderReceiveTest {

	@Test
	public void noProtoverLineSent() {
		Communicator communicator = new Communicator() {
			@Override
			public void send( String command ) {
			}

			@Override
			public String receive() {
				return "Any line not starting by 'protover'";
			}
		};


		WinboardCommander commander = new WinboardCommanderImpl( communicator );
		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.setProtoverListener( listener );

		commander.getInput();

		assertEquals( 0, listener.callsCount );
	}

	private static class ProtoverListenerMock implements ProtoverListener {
		private int callsCount = 0;

		@Override
		public void execute() {
			callsCount++;
		}
	}
}