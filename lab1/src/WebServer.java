import java.net.*;
import java.util.*;

/**
 * Main class that runs the web server, which will listen to HTTP requests and create
 * their own request threads.
 * 
 * @author Nicolas McCallum 100936816
 */
public final class WebServer {

	public static void main(String argv[]) throws Exception {
		System.out.println("Acquiring port number...\n");
		
		// Check if the user actually entered a number as a runtime argument
		int listeningPort;
		if (argv.length < 1) {
			// We have to query the user for the port instead then
			System.out.println("Couldn't find port as argument, please enter a port to use:");
			
		    Scanner scanner = new Scanner(System.in);
		    listeningPort = scanner.nextInt();
		    scanner.close();
		
		} else {
			// Otherwise we can just grab the command line argument
			listeningPort = (new Integer(argv[0])).intValue();
		}

		// Establish the listen socket.
		ServerSocket server = new ServerSocket(listeningPort);
		System.out.println("Listening to port " + listeningPort + "\n");

		//Process HTTP service requests in an infinite loop
		while (true) {
			// Listen for a TCP connection request.
			Socket client = server.accept();

			//Construct an object to process the HTTP request message
			HttpRequest request = new HttpRequest(client);
			Thread thread = new Thread(request);
			thread.start();
		}
	}
}