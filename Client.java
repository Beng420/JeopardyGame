import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private Game game;

    public Client(Socket socket, String username, Game game) {
        try {
            this.game = game;
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = username;
            sendMessage(username);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromServer;
                while (socket.isConnected()) {
                    try {
                        messageFromServer = bufferedReader.readLine();
                        if(messageFromServer == null) {
                            closeEverything(socket, bufferedReader, bufferedWriter);
                            break;
                        }
                        handleMessage(messageFromServer);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    protected void handleMessage(String messageFromServer) {
        String[] message = messageFromServer.split("###");
        if(message[0].contains("player")) {
            game.setPlayer(Integer.parseInt(message[0].substring(6)), message[1]);
        } else if (message[0].equals("boardContents")) {
            String[] boardContents = new String[30];
            for (int i = 0; i < 30; i++) {
                boardContents[i] = message[i+1];
            }
            game.importBoard(boardContents);
        }
    }

    public String getUsername() {
        return clientUsername;
    }

    public void closeEverything() {
        closeEverything(socket, bufferedReader, bufferedWriter);
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
