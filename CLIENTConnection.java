import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CLIENTConnection extends Thread {

  private String clientName = null;
  private Socket clientSocket;
  private DataInputStream in = null;
  private ServerSocket serverDataSocket;
  private PrintStream os;
  private static String USER = "";
  private final CLIENTConnection[] threads;
  private int maxClientsCount;

  public CLIENTConnection(Socket client, CLIENTConnection[] threads) {
   this.clientSocket = client;
   this.threads = threads;
   maxClientsCount = threads.length;
  }

  @Override
public void run() {
 try {
	 int maxClientsCount = this.maxClientsCount;
	 CLIENTConnection[] threads = this.threads;
	 String clientSelection;
	 String name;
	 
	 in = new DataInputStream(clientSocket.getInputStream());;
     
     os = new PrintStream(clientSocket.getOutputStream());
     
/*	 while (true) {
	        os.println("Enter your name.");
	        name = in.readLine().trim();
	        if (name.indexOf('@') == -1) {
	          break;
	        } else {
	          os.println("The name should not contain '@' character.");
	        }
	      }
	 os.println("Welcome to Messenger App");*/
	 name = "";
    
    
    while(true){
    	
    	 String line = in.readLine();
    	 System.out.println(line);
    	 String command[] = line.split(" ");
         if (line.startsWith("/quit")) {
           break;
         }
         if (line.startsWith("@")) {
             String[] words = line.split("\\s", 2);
             if (words.length > 1 && words[1] != null) {
               words[1] = words[1].trim();
               if (!words[1].isEmpty()) {
                 synchronized (this) {
                   for (int i = 0; i < maxClientsCount; i++) {
                     if (threads[i] != null && threads[i] != this
                         && threads[i].clientName != null
                         && threads[i].clientName.equals(words[0])) {
                       threads[i].os.println("<" + name + "> " + words[1]);
                       /*
                        * Echo this message to let the client know the private
                        * message was sent.
                        */
                       //this.os.println(">" + name + "> " + words[1]);
                       break;
                     }
                   }
                 }
               }
             }
           }
         else if( command[0].equals("Login")){
        	 System.out.println("Inside Login");
        	 if (command[1].equals(command[2])){
        		 name = command[1];
        		 synchronized (this) {
        	            for (int i = 0; i < maxClientsCount; i++) {
        	              if (threads[i] != null && threads[i] == this) {
        	                clientName = "@" + name;
        	                System.out.println("Client name is "+ name);
        	                break;
        	              }
        	            }  
        	     	}
        		 os.println("true");
        		 
        	 }
        	 
        	 else os.println("false");
         }
    }
    
    synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] != this
              && threads[i].clientName != null) {
            threads[i].os.println("*** The user " + name
                + " is leaving the chat room !!! ***");
          }
        }
      }
      os.println("*** Bye " + name + " ***");
      
      synchronized (this) {
          for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] == this) {
              threads[i] = null;
            }
          }
        }
      
      in.close();
      os.close();
      clientSocket.close();

     } catch (IOException ex) {
    Logger.getLogger(CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
   }
}

  
  	public String checkIns() throws IOException{
  		String clientSelection = null;
  		while ((clientSelection = in.readLine()) != null) {
  			return clientSelection;
  		}
  		return clientSelection;
  	} 
  	
   public synchronized void receiveFile() {
   try {
	   System.out.println("Going inside recieve file function");
     int bytesRead;
 	//Thread.sleep(3000);

     DataInputStream clientData;
    clientData = new DataInputStream(clientSocket.getInputStream());
    String fileName = clientData.readUTF();
    File a = new File(USER+"\\"+fileName);
    OutputStream output = new FileOutputStream((a));
    long size = clientData.readLong();
    byte[] buffer = new byte[1024];
    while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
        output.write(buffer, 0, bytesRead);
        size -= bytesRead;
    }

    //output.close();
    //clientData.close();

    System.out.println("File "+fileName+" received from client.");
	os.println(fileName+ " Received. This s server resp");
} catch (IOException ex) {
    System.err.println("Client error. Connection closed.");
      }
   }

   public synchronized void sendFile(String fileName) {
    try {
    //handle file read
    File myFile = new File(USER+"\\"+fileName);
    byte[] mybytearray = new byte[(int) myFile.length()];

    FileInputStream fis = new FileInputStream(myFile);
    BufferedInputStream bis = new BufferedInputStream(fis);
    //bis.read(mybytearray, 0, mybytearray.length);

    DataInputStream dis = new DataInputStream(bis);
    dis.readFully(mybytearray, 0, mybytearray.length);

    //handle file send over socket
    OutputStream os = clientSocket.getOutputStream();

    //Sending file name and file size to the server
    DataOutputStream dos = new DataOutputStream(os);
    dos.writeUTF(myFile.getName());
    dos.writeLong(mybytearray.length);
    dos.write(mybytearray, 0, mybytearray.length);
    dos.flush();
    System.out.println("File "+fileName+" sent to client.");
 } catch (Exception e) {
    System.err.println("File does not exist!");
  } 
   }
}