# 🚀 Multi-User File Transfer Application  

A simple yet robust Java application that allows multiple clients to upload and download files to/from a central server simultaneously.  
It uses Java sockets and multi-threading to handle concurrent connections efficiently.

---

## ✨ Features
- 🧵 Multi-threaded server handles many clients at the same time.
- 📤 Upload files from client to server.
- 📥 Download files from server to client.
- 🗂 All uploaded files are saved in a dedicated uploads/ folder on the server.
- 🔧 Easy to extend for more features such as authentication or chat.

---

## 🛠 Tech Stack
- ☕ *Java SE*  
- 🔌 *Java Socket Programming*  
- 🧵 *Multi-Threading*

---

## 📂 Project Structure
Multi-User FileTransfer Application
|
|---src/
|  |---client/
|  |  |---client.java
|  |---server/
|  |  |---server.java
|---uploaads/
|---README.md

## How to Run

### 1. Compile the code  
Open a terminal inside the MultiUserFileTransfer folder and run:

javac src/server/Server.java
javac src/client/Client.java

### 2. Run the server
In the same terminal (or a new one):

java server.Server

The server will start and wait for clients.

### 3. Run the client
Open another terminal window and run:

java client.Client

You’ll see a prompt from the server asking for a command.

### 4. Commands on Client
UPLOAD → Upload a file to the server. It will ask for the file path.

DOWNLOAD → Download a file from the server. It will ask for the file name.

EXIT → Disconnect from the server.

### 5. Notes

Uploaded files are stored in the uploads/ folder on the server.

Downloaded files are saved in the client directory with the prefix downloaded_.

Always start the server before running any client.
