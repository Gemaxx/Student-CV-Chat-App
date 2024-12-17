# **Student CV Display and Client-Server Chat Application**
## **Project Overview**

This project aims to create a **Student CV Display** and a **Client-Server Chat Application** using **JavaFX** for the graphical user interface (GUI). The application allows clients to connect to a server, view the student's CV in HTML format, and communicate with other clients in real-time via a chat interface.

## **Group Members**

- **Ahmed Ibrahim Metwally Negm** – ID: 322223887
- **Ibrahim Abouzeid Hassan Ahmed** – ID: 318180097
- **Ahmed Ihab Mohamed El-Sayed** – ID: 322220246

## **Project Objectives**

- **Student CV Page**: The server creates an HTML file that displays a student's personal details, education, skills, and experience. The client displays this information in a user-friendly GUI.
  
- **Client-Server Chat Application**: The server handles real-time communication between connected clients. The client allows users to send and receive chat messages.

## **Technologies Used**

- **Java**: For implementing both the client and server applications.
- **JavaFX**: For creating the graphical user interface (GUI) for the CV and chat display.
- **Socket Programming**: Using `java.net.Socket` (client) and `java.net.ServerSocket` (server) for real-time chat communication.
- **HTML**: For displaying the student CV on the client side.
- **File I/O**: For the server to create and serve the HTML CV file to the clients.

## **How It Works**

1. **Client-Side**:
    - The client connects to the server using the server’s IP address and port number.
    - The client receives the HTML file containing the student's CV from the server and displays it using JavaFX GUI.
    - The client can send and receive chat messages with the server and other connected clients in real-time.
  
2. **Server-Side**:
    - The server creates and serves the HTML file (student CV) to the client.
    - The server listens for incoming connections from clients and manages chat conversations.
    - The server broadcasts chat messages to all connected clients.

3. **Real-Time Communication**:
    - Clients and the server can exchange chat messages in real-time using socket communication.

## **Installation**

### **1. Prerequisites**
- Install **Java 17** or later.
- Install **JavaFX SDK** (download from [Gluon website](https://gluonhq.com/products/javafx/)) and extract it to a directory (e.g., `C:\javafx`).
- Install **Visual Studio Code**.
- Install **Java Extension Pack** in Visual Studio Code.

### **2. Project Setup**
1. Clone this repository or download the project folder.
2. Open the project folder in Visual Studio Code.
3. In VS Code, install the **JavaFX libraries** by adding the following in `.vscode/settings.json`:
   ```json
   {
       "java.project.referencedLibraries": [
           "lib/**/*.jar"
       ]
   }
   ```
4. Add the following configuration to `.vscode/launch.json` to run the project:
   ```json
   {
       "version": "0.2.0",
       "configurations": [
           {
               "type": "java",
               "name": "Run JavaFX",
               "request": "launch",
               "mainClass": "main.Main",
               "vmArgs": "--module-path ./lib --add-modules javafx.controls,javafx.fxml"
           }
       ]
   }
   ```
   - Make sure to replace `main.Main` with your package and class name.

### **3. Running the Application**
- Use **F5** or **Run > Start Debugging** in Visual Studio Code to run the application.
- The client will display the HTML CV and allow real-time chat communication.

### **4. Screenshots**
1. **Displaying the Student CV on the Client**
2. **Client-Server Chat Interface**
  ![image](https://github.com/user-attachments/assets/cc654211-bc35-48af-999b-9af4b3c3bcc2)
