

import java.io.* ;
import java.net.* ;
import java.util.* ;


final class HttpRequest implements Runnable {
	final static String CRLF = "\r\n";
	Socket socket;

	
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	
	private void processRequest() throws Exception {
		// Get a reference to the socket's input and output streams
		InputStream is = this.socket.getInputStream();
		DataOutputStream os = new DataOutputStream(this.socket.getOutputStream());

		// Set up input stream filters
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// Get the request line of the HTTP request message
		String requestLine = br.readLine();

		// Extract the filename from the request line
		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken(); 
		String fileName = tokens.nextToken();
		
		fileName = "." + fileName ;
		
		FileInputStream fis = null ;
		boolean fileExists = true ;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			fileExists = false ;
		}
		System.out.println("Incoming!!!");
		System.out.println(requestLine);
		String headerLine = null;
		while ((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
		}

		// Construct the response message
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		if (fileExists) {
			statusLine = "HTTP/1.0 200 OK" + CRLF;
			contentTypeLine = "Content-Type: "
			                + contentType(fileName)
			                + CRLF;
		} else {
			statusLine = "HTTP/1.0 404 Not Found" + CRLF;
			contentTypeLine = "Content-Type: text/html" + CRLF;
			entityBody = "<HTML>"
			           + "<HEAD><TITLE>Not Found</TITLE></HEAD>"
			           + "<BODY>File Not Found on the server</BODY></HTML>";
		}
		os.writeBytes(statusLine);

		// Send the content type line
		os.writeBytes(contentTypeLine);
		os.writeBytes(CRLF);
		if (fileExists) {
			sendBytes(fis, os);
			fis.close();
		} else {
			os.writeBytes(entityBody);
		}
		os.close();
		br.close();
		socket.close();
	}

	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
	    byte[] buffer = new byte[1024];
	    int bytes = 0;
		
		while ((bytes = fis.read(buffer)) != -1) {
     	    System.out.println("buffer= " + buffer);
			os.write(buffer, 0, bytes);
		}
	}

	private static String contentType(String fileName) {
		if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
			return "text/html";
		} else if (fileName.endsWith(".gif")) {
			return "image/gif";
		} else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
			return "image/jpeg";
		} else {
			return "application/octet-stream" ;
		}
	}
}
