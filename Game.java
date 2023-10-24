import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Font;
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
    private JButton btOptionsSaveLocationBrowse;
    private JTextField tfOptionsDefaultAddress;
    private JTextField tfOptionsSaveLocation;
    private JLabel lbOptionsDefaultAddress;
    private JLabel lbOptionsSaveLocation;

    private JPanel panelStart;
    private JButton btStartHostGame;
    private JButton btStartConnectServer;
    private JButton btStartCreateGame;
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

    private JPanel panelHost;
    private JButton btHostBackToMenu;

    private JPanel panelGameBoardEditor;
    private JButton btGameBoardEditorBackToMenu;
    private JButton btGameBoardEditorCreateNewGameBoard;
    private JButton btGameBoardEditorLoadGameBoard;


    public enum Panels {
        MainMenu,
        StartingScreen,
        ActualGame,
        OptionsMenu,
        HostScreen,
        CreateGame
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
            gameOptions.put("saveLocation", "%UserProfile%");
        } else {
            if(gameOptions.get("defaultAddress").equals("")) {
                gameOptions.put("defaultAddress", "localhost");
                System.out.println("defaultAddress is null");
            }
            if(gameOptions.get("saveLocation").equals("")) {
                gameOptions.put("saveLocation", "C:\\Users\\Public\\Documents\\JeopardyGame\\");
                System.out.println("saveLocation is null");
            }
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
        btOptionsSaveLocationBrowse = new JButton("Browse");
        btOptionsSaveLocationBrowse.setBounds(400, 165, 50, 20);
        btOptionsSaveLocationBrowse.addActionListener(this);
        btOptionsSaveLocationBrowse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btOptionsSaveLocationBrowse.setFont(new Font("Arial", Font.PLAIN, 12));
        panelOptions.add(btOptionsSaveLocationBrowse);
        
        tfOptionsDefaultAddress = new JTextField(gameOptions.get("defaultAddress"));
        tfOptionsDefaultAddress.setBounds(200, 100, 100, 50);
        panelOptions.add(tfOptionsDefaultAddress);
        tfOptionsSaveLocation = new JTextField(gameOptions.get("saveLocation"));
        tfOptionsSaveLocation.setBounds(200, 150, 200, 50);
        panelOptions.add(tfOptionsSaveLocation);
        
        lbOptionsDefaultAddress = new JLabel("Default Address:");
        lbOptionsDefaultAddress.setBounds(100, 100, 100, 50);
        panelOptions.add(lbOptionsDefaultAddress);
        lbOptionsSaveLocation = new JLabel("Save Location:");
        lbOptionsSaveLocation.setBounds(100, 150, 100, 50);
        panelOptions.add(lbOptionsSaveLocation);



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
        btStartCreateGame = new JButton("Game Board Editor");
        btStartCreateGame.setBounds(175, 300, 150, 50);
        btStartCreateGame.addActionListener(this);
        panelStart.add(btStartCreateGame);
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



        // Game Board Editor
        panelGameBoardEditor = new JPanel();
        panelGameBoardEditor.setBounds(0, 0, 500, 500);
        panelGameBoardEditor.setLayout(null);
        
        btGameBoardEditorBackToMenu = new JButton("Back");
        btGameBoardEditorBackToMenu.setBounds(10, 10, 100, 50);
        btGameBoardEditorBackToMenu.addActionListener(this);
        panelGameBoardEditor.add(btGameBoardEditorBackToMenu);
        btGameBoardEditorCreateNewGameBoard = new JButton("Create New Game Board");
        btGameBoardEditorCreateNewGameBoard.setBounds(150, 200, 200, 50);
        btGameBoardEditorCreateNewGameBoard.addActionListener(this);
        panelGameBoardEditor.add(btGameBoardEditorCreateNewGameBoard);
        btGameBoardEditorLoadGameBoard = new JButton("Load Game Board");
        btGameBoardEditorLoadGameBoard.setBounds(150, 250, 200, 50);
        btGameBoardEditorLoadGameBoard.addActionListener(this);
        panelGameBoardEditor.add(btGameBoardEditorLoadGameBoard);



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



        // Host's Screen
        panelHost = new JPanel();
        panelHost.setBounds(0, 0, 500, 500);
        panelHost.setLayout(null);
        
        btHostBackToMenu = new JButton("Back");
        btHostBackToMenu.setBounds(10, 10, 100, 50);
        btHostBackToMenu.addActionListener(this);
        panelHost.add(btHostBackToMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // Main Menu
        if(src == btMenuStartGame) {
            setPanel(Panels.StartingScreen, Panels.MainMenu);
        } else if(src == btMenuOptions) {
            tfOptionsDefaultAddress.setText(gameOptions.get("defaultAddress"));
            setPanel(Panels.OptionsMenu, Panels.MainMenu);
        } else if(src == btMenuExitGame) {
            exitGame();
        }

        // Options
        else if(src == btOptionsBackToMenu) {
            gameOptions.put("defaultAddress", tfOptionsDefaultAddress.getText());
            gameOptions.put("saveLocation", tfOptionsSaveLocation.getText());
            FileHandler.saveGameOptions(gameOptions);
            setPanel(Panels.MainMenu, Panels.OptionsMenu);
        } else if(src == btOptionsSaveLocationBrowse) {
            JFileChooser chooser = new JFileChooser(gameOptions.get("saveLocation"));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int status = chooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (file == null) {
                    return;
                }
                String fileName = chooser.getSelectedFile().getAbsolutePath();
                tfOptionsSaveLocation.setText(fileName);
            }
        }
        
        // Starting Screen
        else if(src == btStartCreateGame) {
            setPanel(Panels.CreateGame, Panels.StartingScreen);
        } else if(src == btStartHostGame) {
            openServer();
        } else if(src == btStartConnectServer) {
            connectToServer(false);
        } else if(src == btStartBackToMenu) {
            tfStartIpAddress.setText("");
            tfStartUsername.setText("");
            setPanel(Panels.MainMenu, Panels.StartingScreen);
        } 
        
        // Game Board Editor
        else if(src == btGameBoardEditorBackToMenu) {
            setPanel(Panels.StartingScreen, Panels.CreateGame);
        } else if(src == btGameBoardEditorCreateNewGameBoard) {

        } else if(src == btGameBoardEditorLoadGameBoard) {

        }

        // Actual Game
        else if(src == btGameLeave) {
            leaveGame();
        } else if(src == btGameSendMessage) {
            sendMessage();
        }

        // Host's Screen
        else if(src == btHostBackToMenu) {
            setPanel(Panels.MainMenu, Panels.HostScreen);
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
        
        setPanel(Panels.HostScreen, Panels.StartingScreen);
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
            case HostScreen:
            remove(panelHost);
            break;
            case CreateGame:
            remove(panelGameBoardEditor);
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
            case HostScreen:
            add(panelHost);
            break;
            case CreateGame:
            add(panelGameBoardEditor);
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
