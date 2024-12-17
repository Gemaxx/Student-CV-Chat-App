// Import necessary libraries
import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class Client extends Application {
    private static Socket socket;  // Socket for communication with the server
    private static PrintWriter out;  // Output stream to send data to the server
    private static BufferedReader in;  // Input stream to read data from the server

    private WebView webView;  // WebView for displaying CV content
    private TextArea chatArea;  // TextArea to display chat messages
    private TextField messageField;  // TextField for message input
    private Button sendButton;  // Button to send messages
    private Button showCVButton;  // Button to request the CV from the server

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client Chat");

        // Set up the WebView to display the CV
        webView = new WebView();
        webView.setStyle("-fx-background-color: #f4f4f4;");
        webView.setPrefSize(500, 700);  // Set WebView size for CV

        Label cvTitleLabel = new Label("Curriculum Vitae");
        cvTitleLabel.setFont(Font.font("Cairo", 20));
        cvTitleLabel.setTextFill(Color.DODGERBLUE);

        // Set up the chat area to display messages
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-background-color: #f4f4f4;");
        chatArea.setPrefHeight(500);  // Set height for chat area
        chatArea.setPrefWidth(300);  // Set width for chat area

        // ScrollPane for the chat area to make it scrollable
        ScrollPane chatScrollPane = new ScrollPane(chatArea);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setPrefHeight(500);  // Set scrollable height for chat

        // Set up the message input field
        messageField = new TextField();
        messageField.setPromptText("Type your message here...");
        messageField.setStyle("-fx-font-size: 12px;");
        messageField.setPrefWidth(250);  // Set width for message input field

        // Button to send messages
        sendButton = new Button("Send");
        sendButton.setStyle("-fx-font-size: 12px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        sendButton.setOnAction(event -> sendMessage());  // Send message on button click

        // Button to request CV from the server
        showCVButton = new Button("Show CV");
        showCVButton.setStyle("-fx-font-size: 12px; -fx-background-color: #008CBA; -fx-text-fill: white;");
        showCVButton.setOnAction(event -> requestCV());  // Request CV when clicked

        // Layout for message input and send button
        HBox messagePanel = new HBox(10, messageField, sendButton);
        messagePanel.setAlignment(Pos.CENTER_LEFT);
        messagePanel.setPadding(new Insets(10));

        // Layout for the "Show CV" button
        HBox buttonPanel = new HBox(10, showCVButton);
        buttonPanel.setAlignment(Pos.CENTER_RIGHT);
        buttonPanel.setPadding(new Insets(10));

        // Layout for CV section (title and WebView)
        VBox cvLayout = new VBox(10, cvTitleLabel, webView);
        cvLayout.setPadding(new Insets(10));

        // Main layout to display chat and CV side by side
        HBox layout = new HBox(10, chatScrollPane, cvLayout);
        layout.setPadding(new Insets(10));

        // Full layout with chat, CV, and input panels
        VBox mainLayout = new VBox(10, layout, buttonPanel, messagePanel);
        VBox.setVgrow(layout, Priority.ALWAYS);

        // Create and display the scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToServer();  // Establish connection to the server
    }

    // Connect to the server
    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);  // Connect to the server at localhost, port 12345
            out = new PrintWriter(socket.getOutputStream(), true);  // Create output stream to send data
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Create input stream to read data

            new Thread(new IncomingMessages()).start();  // Start a new thread to listen for incoming messages
        } catch (IOException e) {
            e.printStackTrace();  // Print error if connection fails
        }
    }

    // Send a message to the server
    private void sendMessage() {
        String message = messageField.getText();
        out.println(message);  // Send message to the server
        messageField.clear();  // Clear the message field
    }

    // Request the CV from the server
    private void requestCV() {
        out.println("SEND_CV");  // Send a request to the server to send the CV
    }

    // Runnable class to handle incoming messages from the server
    class IncomingMessages implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                // Continuously read messages from the server
                while ((message = in.readLine()) != null) {
                    final String finalMessage = message;  // Make message final for usage in Platform.runLater

                    if (finalMessage.trim().startsWith("<!DOCTYPE html>")) {
                        // If the message is HTML (i.e., CV), load it in WebView
                        Platform.runLater(() -> webView.getEngine().loadContent(finalMessage));
                    } else {
                        // Otherwise, display the message in the chat area
                        Platform.runLater(() -> chatArea.appendText(finalMessage + "\n"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();  // Print error if something goes wrong with reading the message
            }
        }
    }
}
