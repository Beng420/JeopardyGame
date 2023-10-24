// A basic game where you can move a ball with the arrow keys

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Game extends JFrame implements ActionListener{

    private Server server;
    private Client client;
    private HashMap<String, String> gameOptions;

    private JPanel panelMenu;
    private JButton btMenuStartGame;
    private JButton btMenuOptions;
    private JButton btMenuExitGame;

    private JPanel panelOptions;
    private JButton btOptionsBackToMenu;
    private JTextField tfOptionsDefaultAddress;
    private JLabel lbOptionsDefaultAddress;

    private JPanel panelStart;
    private JButton btStartHostGame;
    private JButton btStartConnectServer;
    private JButton btStartBackToMenu;
    private JTextField tfStartUsername;
    private JTextField tfStartIpAddress;
    private JLabel lbStartUsername;
    private JLabel lbStartIpAddress;

    private JPanel panelGame;
    private JButton btGameLeave;
    private JButton btGameSendMessage;
    private JTextField tfGameMessageToSend;
    private JTextArea taGameChat;
    private JScrollBar sbGameChat;
    private String FILLERSTRING = "Type a message...";

    public enum Panels {
        MainMenu,
        StartingScreen,
        ActualGame,
        OptionsMenu
    }
    
    public Game() {
        super("Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        panelMenu = new JPanel();
        panelMenu.setBounds(0, 0, 500, 500);
        panelMenu.setLayout(null);
        add(panelMenu);

        gameOptions = FileHandler.loadGameOptions();
        if(gameOptions == null) {
            gameOptions = new HashMap<String, String>();
            gameOptions.put("defaultAddress", "localhost");
        }
        

        // Main Menu
        btMenuStartGame = new JButton("Start Game");
        btMenuStartGame.setBounds(200, 200, 100, 50);
        btMenuStartGame.addActionListener(this);
        panelMenu.add(btMenuStartGame);
        btMenuOptions = new JButton("Options");
        btMenuOptions.setBounds(200, 250, 100, 50);
        btMenuOptions.addActionListener(this);
        panelMenu.add(btMenuOptions);
        btMenuExitGame = new JButton("Exit Game");
        btMenuExitGame.setBounds(200, 300, 100, 50);
        btMenuExitGame.addActionListener(this);
        panelMenu.add(btMenuExitGame);

        // Options
        panelOptions = new JPanel();
        panelOptions.setBounds(0, 0, 500, 500);
        panelOptions.setLayout(null);
        btOptionsBackToMenu = new JButton("Back");
        btOptionsBackToMenu.setBounds(10, 10, 100, 50);
        btOptionsBackToMenu.addActionListener(this);
        panelOptions.add(btOptionsBackToMenu);
        tfOptionsDefaultAddress = new JTextField(gameOptions.get("defaultAddress"));
        tfOptionsDefaultAddress.setBounds(200, 100, 100, 50);
        panelOptions.add(tfOptionsDefaultAddress);
        lbOptionsDefaultAddress = new JLabel("Default Address:");
        lbOptionsDefaultAddress.setBounds(100, 100, 100, 50);
        panelOptions.add(lbOptionsDefaultAddress);

        // Starting Screen
        panelStart = new JPanel();
        panelStart.setBounds(0, 0, 500, 500);
        panelStart.setLayout(null);
        btStartHostGame = new JButton("Host Game");
        btStartHostGame.setBounds(175, 250, 150, 50);
        btStartHostGame.addActionListener(this);
        panelStart.add(btStartHostGame);
        btStartConnectServer = new JButton("Connect to Server");
        btStartConnectServer.setBounds(175, 200, 150, 50);
        btStartConnectServer.addActionListener(this);
        panelStart.add(btStartConnectServer);
        btStartBackToMenu = new JButton("Back");
        btStartBackToMenu.setBounds(200, 400, 100, 50);
        btStartBackToMenu.addActionListener(this);
        panelStart.add(btStartBackToMenu);
        tfStartUsername = new JTextField();
        tfStartUsername.setBounds(200, 100, 100, 50);
        panelStart.add(tfStartUsername);
        tfStartIpAddress = new JTextField();
        tfStartIpAddress.setBounds(200, 150, 100, 50);
        panelStart.add(tfStartIpAddress);
        lbStartUsername = new JLabel("Username:");
        lbStartUsername.setBounds(100, 100, 100, 50);
        panelStart.add(lbStartUsername);
        lbStartIpAddress = new JLabel("IP Address:");
        lbStartIpAddress.setBounds(100, 150, 100, 50);
        panelStart.add(lbStartIpAddress);

        // Actual Game
        panelGame = new JPanel();
        panelGame.setBounds(0, 0, 500, 500);
        panelGame.setLayout(null);
        btGameLeave = new JButton("Leave");
        btGameLeave.setBounds(200, 400, 100, 50);
        btGameLeave.addActionListener(this);
        panelGame.add(btGameLeave);
        btGameSendMessage = new JButton("Send");
        btGameSendMessage.setBounds(200, 350, 100, 50);
        btGameSendMessage.addActionListener(this);
        panelGame.add(btGameSendMessage);
        tfGameMessageToSend = new JTextField();
        tfGameMessageToSend.setBounds(100, 350, 100, 50);
        tfGameMessageToSend.addActionListener(this);
        panelGame.add(tfGameMessageToSend);
        taGameChat = new JTextArea();
        taGameChat.setBounds(100, 100, 300, 200);
        panelGame.add(taGameChat);
        sbGameChat = new JScrollBar();
        sbGameChat.setBounds(400, 100, 50, 200);
        panelGame.add(sbGameChat);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if(src == btMenuStartGame) {
            setPanel(Panels.StartingScreen, Panels.MainMenu);
        } else if(src == btMenuOptions) {
            tfOptionsDefaultAddress.setText(gameOptions.get("defaultAddress"));
            setPanel(Panels.OptionsMenu, Panels.MainMenu);
        } else if(src == btMenuExitGame) {
            FileHandler.saveGameOptions(gameOptions);
            exitGame();
        }

        else if(src == btOptionsBackToMenu) {
            gameOptions.put("defaultAddress", tfOptionsDefaultAddress.getText());
            setPanel(Panels.MainMenu, Panels.OptionsMenu);
        }
        
        else if(src == btStartHostGame) {
            openServer();
        } else if(src == btStartConnectServer) {
            connectToServer(false);
        } else if(src == btStartBackToMenu) {
            tfStartIpAddress.setText("");
            tfStartUsername.setText("");
            setPanel(Panels.MainMenu, Panels.StartingScreen);
        } 
        
        else if(src == btGameLeave) {
            leaveGame();
        } else if(src == btGameSendMessage) {
            sendMessage();
        }
    }

    private void sendMessage() {
        String message = tfGameMessageToSend.getText();
        if(message.equals("") || message.equals(FILLERSTRING)) {
            return;
        }
        client.sendMessage(message);
        taGameChat.append("<"+client.getUsername()+">"+message + "\n");
        tfGameMessageToSend.setText("");
    }

    private void openServer() {
        String username = tfStartUsername.getText();
        if(username.equals("")) {
            return;
        }
        try {
            server = new Server(new ServerSocket(1234), username);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        connectToServer(true);
    }

    private void connectToServer(boolean localhosting) {
        String username = tfStartUsername.getText();
        String ipAddress = "localhost";
        if(!localhosting) {
            ipAddress = tfStartIpAddress.getText();
            if(username.equals("") || ipAddress.equals("")) {
                tfStartUsername.setText("");
                tfStartIpAddress.setText("");
                return;
            }
        }
        try {
            Socket socket = new Socket(ipAddress, 1234);
            client = new Client(socket, username);
            client.listenForMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(client == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String message;
                while(client != null) {
                    try {
                        Thread.sleep(100);
                        if((message = client.getLatestMessage()) == null) {
                            continue;
                        }
                        taGameChat.append(message + "\n");
                        client.clearLatestMessage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        setPanel(Panels.ActualGame, Panels.StartingScreen);
    }
    
    private void leaveGame() {
        if(server != null) {
            server.closeServerSocket();
            server = null;
        }
        if(client != null) {
            client.closeEverything();
            client = null;
        }
        setPanel(Panels.MainMenu, Panels.ActualGame);
    }
    
    private void setPanel(Panels panel, Panels prevPanel) {
        switch(prevPanel) {
            case MainMenu:
            remove(panelMenu);
            break;
            case StartingScreen:
            remove(panelStart);
            break;
            case ActualGame:
            remove(panelGame);
            break;
            case OptionsMenu:
            remove(panelOptions);
            break;
        }
        switch(panel) {
            case MainMenu:
            add(panelMenu);
            break;
            case StartingScreen:
            add(panelStart);
            break;
            case ActualGame:
            add(panelGame);
            break;
            case OptionsMenu:
            add(panelOptions);
            break;
        }
        repaint();
    }
    
    private void exitGame() {
        setVisible(false);
        dispose();
        System.exit(0);
    }
}
