
import java.io.* ;
import java.net.* ;
import java.util.* ;

public final class WebServer {
	public static void main(String argv[]) throws Exception {
		System.out.println("Acquiring port number...\n");
		int listeningPort = (new Integer(argv[0])).intValue();

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