package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 1234;
    private static final String UPLOAD_DIR = "uploads/";

    public static void main(String[] args) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdir();

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);

        ExecutorService pool = Executors.newFixedThreadPool(10);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            pool.execute(new ClientHandler(clientSocket));
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        ) {
            while (true) {
                dos.writeUTF("Enter command (UPLOAD/DOWNLOAD/EXIT):");
                String command = dis.readUTF();

                if (command.equalsIgnoreCase("UPLOAD")) {
                    receiveFile(dis, dos);
                } else if (command.equalsIgnoreCase("DOWNLOAD")) {
                    sendFile(dis, dos);
                } else if (command.equalsIgnoreCase("EXIT")) {
                    dos.writeUTF("Goodbye!");
                    break;
                } else {
                    dos.writeUTF("Invalid command");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }

    private void receiveFile(DataInputStream dis, DataOutputStream dos) throws IOException {
        String fileName = dis.readUTF();
        long fileSize = dis.readLong();

        FileOutputStream fos = new FileOutputStream("uploads/" + fileName);
        byte[] buffer = new byte[4096];
        int read;
        long remaining = fileSize;
        while ((read = dis.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
            fos.write(buffer, 0, read);
            remaining -= read;
        }
        fos.close();
        dos.writeUTF("File uploaded successfully: " + fileName);
        System.out.println("Received file: " + fileName);
    }

    private void sendFile(DataInputStream dis, DataOutputStream dos) throws IOException {
        dos.writeUTF("Enter file name to download:");
        String fileName = dis.readUTF();

        File file = new File("uploads/" + fileName);
        if (!file.exists()) {
            dos.writeUTF("File not found!");
            return;
        }

        dos.writeUTF("READY");
        dos.writeLong(file.length());

        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int read;
        while ((read = fis.read(buffer)) > 0) {
            dos.write(buffer, 0, read);
        }
        fis.close();
        dos.writeUTF("File downloaded successfully!");
        System.out.println("Sent file: " + fileName);
    }
}
