import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server server = this;
    private ServerSocket serverSocket;
    private Thread serverThread;
    
    public Server(ServerSocket serverSocket, String username) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!serverSocket.isClosed()) {
                        Socket socket = serverSocket.accept();
                        System.out.println("Client connected");
                        ClientHandler clientHandler = new ClientHandler(socket, server);
                        new Thread(clientHandler).start();
                    }
                } catch (IOException e) {
                    closeServerSocket();
                }
            }
        });
        serverThread.start();
    }

    public void closeServerSocket() {
        if(serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
