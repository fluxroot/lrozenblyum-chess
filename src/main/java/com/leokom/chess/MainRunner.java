package com.leokom.chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class MainRunner {
	private static Logger logger = Logger.getLogger(MainRunner.class);
	public static void main(String[] args) throws IOException {
        //critically important to send this sequence at the start
        //to ensure the Winboard won't ignore our 'setfeature' commands
        //set feature commands must be sent in response to protover
        System.out.println("feature done=0");
		logger.info( "Starting the chess" );

		//TODO: think about buffers, they're not recommended to use
		BufferedReader r = new BufferedReader(new InputStreamReader( System.in ));

        //TODO: this moveNumber is totally unreliable (after end-of-game it must be reset)
        int moveNumber = 0;
		while( true ) {
            //TODO: any Thread.sleep needed?
			String line = r.readLine();
			logger.info( "Line from engine = " + line );

            //TODO: what does it mean?
            if ( line == null ) {
                continue;
            }

            if ( line.equals( "quit" ) ) {
                logger.info( "Received quit command" );
                break;
            }

            // xboard
            // This command will be sent once immediately after your engine process is started.
            // You can use it to put your engine into "xboard mode" if that is needed.
            // If your engine prints a prompt to ask for user input,
            // you must turn off the prompt and output a newline when the "xboard" command comes in.

            //LR: because we don't print any prompt, I don't put any newline here
            if ( line.equals( "xboard" ) ) {
                logger.info( "Ready to work" );
            }

            //protover N
            //Beginning in protocol version 2 (in which N=2), this command will be sent immediately after the "xboard" command.
            //If you receive some other command immediately after "xboard" (such as "new"), you can assume that protocol version 1 is in use.
            //The "protover" command is the only new command that xboard always sends in version 2. All other new commands to the engine are sent only if the engine first enables them with the "feature" command. Protocol versions will always be simple integers so that they can easily be compared.
            //Your engine should reply to the protover command by sending the "feature" command (see below) with the list of non-default feature settings that you require, if any.
            //Your engine should never refuse to run due to receiving a higher protocol version number than it is expecting! New protocol versions will always be compatible with older ones by default; the larger version number is simply a hint that additional "feature" command options added in later protocol versions may be accepted.
            if ( line.startsWith( "protover" ) ) {
                //TODO: add analyze if this line is received immediately after xboard
                //if not - we may assume it's protocol v1

                //enable usermove prefixes for moves for easier parsing
                System.out.println( "feature usermove=1" );
                //signal end of initializations
                System.out.println( "feature done=1" );

                //TODO: check if 2'nd element exists
                logger.info( "Protocol version detected = " + line.split( " " )[ 1 ] );
            }

            //this is received only if we play white, is it true?
            //otherwise we'll get usermoves instead

            //the 2'nd check works because we enabled v2 feature...
            if ( line.equals( "go" ) || line.startsWith( "usermove" ) ) {
                moveNumber++;
                logger.info( "Detected allowance to go. Move number = " + moveNumber );
                switch ( moveNumber ) {
                    case 1:
                        System.out.println( "move e2e4" );
                        break;
                    case 2:
                        System.out.println( "move d2d4" );
                        //NOTE: interesting to implement - how much do we need to wait for result?
                        //NOTE2: it's not recommended way to offer draw after the move.
                        System.out.println( "offer draw" );
                        break;
                    default:
                        System.out.println( "resign" );
                }
            }

            //another player offers draw - accept always
            if ( line.equals( "draw" ) ) {
                System.out.println( "offer draw" );
            }
		}
	}
}