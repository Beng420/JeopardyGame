import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server server = this;
    private ServerSocket serverSocket;
    private Thread serverThread;
    private Game game;
    
    public Server(ServerSocket serverSocket, String username, Game game) {
        this.serverSocket = serverSocket;
        this.game = game;
    }

    public void startServer() {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!serverSocket.isClosed()) {
                        if(ClientHandler.clientHandlers.size() == 5) continue;
                        Socket socket = serverSocket.accept();
                        System.out.println("Client connected");
                        ClientHandler clientHandler = new ClientHandler(socket, server);
                        game.setPlayer(ClientHandler.clientHandlers.size()-1, clientHandler.getUsername());
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

    public void handleMessage(String messageFromClient, ClientHandler clientHandler) {
        if(messageFromClient.contains("question")){
            String[] message = messageFromClient.split("###");
            game.recommendQuestionToHost(Integer.parseInt(message[1]), Integer.parseInt(message[2]));
        } else if(messageFromClient.equals("requestBoard")) {
            clientHandler.sendMessageToClient("boardContents###"+game.getBoard());
        }
    }
}
