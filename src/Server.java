import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    // Port number for the server to listen on
    private static final int PORT = 12345;

    // Thread-safe list to hold client PrintWriter objects for broadcasting messages
    private static List<PrintWriter> clientWriters = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("Server is running...");

        // Creating a server socket to listen for client connections
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Accept a new client connection and handle it in a new thread
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to broadcast a message to all connected clients
    private static void broadcast(String message) {
        // Iterate through each client and send the message
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    // ClientHandler class to manage communication with a single client
    private static class ClientHandler extends Thread {
        private Socket socket;  // Socket for communication with the client
        private PrintWriter out;  // Output stream for sending data to the client
        private BufferedReader in;  // Input stream for reading data from the client

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Initialize input and output streams for communication with the client
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Add the client's PrintWriter to the list of writers for broadcasting
                clientWriters.add(out);

                String message;
                // Continuously read messages from the client
                while ((message = in.readLine()) != null) {
                    // If the message is "SEND_CV", send the CV to the client
                    if (message.equals("SEND_CV")) {
                        sendCV(out);
                    } else {
                        // Otherwise, broadcast the message to all clients
                        broadcast(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Cleanup when the client disconnects
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Remove the client's PrintWriter from the list of writers
                clientWriters.remove(out);

                // Close the input and output streams
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Method to send the CV (HTML file) to the requesting client
        private void sendCV(PrintWriter out) {
            // Path to the CV file
            String filePath = "D:/3. Study/2. SHA HICTI CS/4th Year/S1/Courses/NP/2. Assessments/2. Project/Student-CV-Chat-App/src/student_cv.html";
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                // Read the content of the CV file and send it to the client
                StringBuilder cvContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    cvContent.append(line);
                }
                // Send the CV content as a response to the client
                out.println(cvContent.toString());
            } catch (IOException e) {
                // In case of an error, send an error message to the client
                e.printStackTrace();
                out.println("Error sending CV.");
            }
        }
    }
}
