import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class NetworkAdminHandler implements Runnable {
    private Socket clientSocket;
    private List<NetworkAdminHandler> clients;
    private PrintWriter writer;

    public NetworkAdminHandler(Socket clientSocket, List<NetworkAdminHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = reader.readLine()) != null) {
                if (message.startsWith("/broadcast")) {
                    broadcastMessage(message.substring(11)); // Send the message after "/broadcast"
                } else if (message.startsWith("/disconnect")) {
                    disconnectClient();
                    break;
                } else {
                    System.out.println("Received message from client: " + message);
                    writer.println("Server: " + message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clients.remove(this);
        }
    }

    private void broadcastMessage(String message) {
        for (NetworkAdminHandler client : clients) {
            client.writer.println("Broadcast: " + message);
        }
    }

    private void disconnectClient() {
        writer.println("Disconnecting from the server.");
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
