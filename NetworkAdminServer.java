import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkAdminServer {
    private static List<NetworkAdminHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5001); // Choose a suitable port

            System.out.println("Server is running. Waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                NetworkAdminHandler handler = new NetworkAdminHandler(clientSocket, clients);
                clients.add(handler);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
