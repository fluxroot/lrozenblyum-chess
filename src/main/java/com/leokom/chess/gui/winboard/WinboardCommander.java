package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Commander;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Author: Leonid
 * Date-time: 20.08.12 16:13
 */
class WinboardCommander implements Commander {
	/**
	 * I don't set up UTF-8 since specification says we'll get only latin characters and digits
	 * US-ASCII is 7-bit latin charset
	 */
	private static final String INPUT_ENCODING = "US-ASCII";
	private BufferedReader reader;
    private PrintStream outputStream;
	private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Create the winboard-commander with injected dependencies
     * @param inputStream
     * @param outputStream
     */
    public WinboardCommander( InputStream inputStream, PrintStream outputStream ) {
		final InputStreamReader streamReader;
		try {
			streamReader = new InputStreamReader( inputStream, INPUT_ENCODING );
		} catch ( UnsupportedEncodingException e ) {
			final InstantiationError instantiationError = new InstantiationError(
				"Java installation seems incorrect. It doesn't support standard encoding = " + INPUT_ENCODING );
			instantiationError.initCause( e );
			throw instantiationError;
		}
		//TODO: think about buffers, they're not recommended to use
		this.reader = new BufferedReader( streamReader );
        this.outputStream = outputStream;
    }

    @Override
    public void send(String command) {
		logger.info( "Sent: " + command );
        outputStream.println( command );
    }

    @Override
    public String receive() {
        try {
			final String line = reader.readLine();
			logger.info( "Received: " + line );
			return line;
        } catch ( IOException e ) { //avoid propagating internal exception to signature
            throw new RuntimeException( e );
        }
    }
}