package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(dis.readUTF()); // Prompt from server
            String command = sc.nextLine();
            dos.writeUTF(command);

            if (command.equalsIgnoreCase("UPLOAD")) {
                System.out.print("Enter file path to upload: ");
                String path = sc.nextLine();
                File file = new File(path);
                if (!file.exists()) {
                    System.out.println("File does not exist.");
                    continue;
                }

                FileInputStream fis = new FileInputStream(file);
                dos.writeUTF(file.getName());
                dos.writeLong(file.length());

                byte[] buffer = new byte[4096];
                int read;
                while ((read = fis.read(buffer)) > 0) {
                    dos.write(buffer, 0, read);
                }
                fis.close();
                System.out.println(dis.readUTF());

            } else if (command.equalsIgnoreCase("DOWNLOAD")) {
                System.out.print("Enter file name to download: ");
                String fileName = sc.nextLine();
                dos.writeUTF(fileName);

                String serverResponse = dis.readUTF();
                if (serverResponse.equals("READY")) {
                    long fileSize = dis.readLong();
                    FileOutputStream fos = new FileOutputStream("downloaded_" + fileName);

                    byte[] buffer = new byte[4096];
                    int read;
                    long remaining = fileSize;
                    while ((read = dis.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
                        fos.write(buffer, 0, read);
                        remaining -= read;
                    }
                    fos.close();
                    System.out.println(dis.readUTF());
                } else {
                    System.out.println(serverResponse);
                }

            } else if (command.equalsIgnoreCase("EXIT")) {
                System.out.println(dis.readUTF());
                break;
            } else {
                System.out.println(dis.readUTF());
            }
        }

        dos.close();
        dis.close();
        socket.close();
        sc.close();
    }
}
