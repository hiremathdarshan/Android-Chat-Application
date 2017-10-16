import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

 private static ServerSocket serverSocket;
 private static Socket clientSocket = null;
 private static final int maxClientsCount = 10;
 private static final CLIENTConnection[] threads = new CLIENTConnection[maxClientsCount];

      public static void main(String[] args) throws IOException {

try {
    serverSocket = new ServerSocket(4447);
    System.out.println("Server started.");
} catch (Exception e) {
    System.err.println("Port already in use.");
    System.exit(1);
}

while (true) {
    try {
        clientSocket = serverSocket.accept();
        System.out.println("Accepted connection : " + clientSocket);

        int i = 0;
        for (i = 0; i < maxClientsCount; i++) {
          if (threads[i] == null) {
            (threads[i] = new CLIENTConnection(clientSocket, threads)).start();
            break;
          }
        }
        if (i == maxClientsCount) {
            PrintStream os = new PrintStream(clientSocket.getOutputStream());
            os.println("Server too busy. Try later.");
            os.close();
            clientSocket.close();
          }
        //Thread t = new Thread(new CLIENTConnection(clientSocket));

       // t.start();

    } catch (Exception e) {
        System.err.println("Error in connection attempt.");
     }
        }
         }
         }