import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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

    private final int WIDTH = 1244;
    private final int HEIGHT = 700;
    private final int STD_WIDTH = 100;
    private final int STD_WIDTH_HALF = 50;
    private final int STD_WIDTH_DOUBLE = 200;
    private final int STD_WIDTH_POINT_FIVE = 150;
    private final int STD_HEIGHT = 50;
    private final int STD_HEIGHT_HALF = 25;
    private final int STD_HEIGHT_DOUBLE = 100;
    private final int STD_HEIGHT_POINT_FIVE = 75;

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
    private GameBoard gameBoard;

    private JPanel panelEditingBoard;
    private JButton btEditingBoardBackToMenu;
    private JButton btEditingBoardSaveGameBoard;
    private JButton[][] btEditingBoardQuestions;
    private JTextField[] tfEditingBoardCategories;

    private JPanel panelEditingQuestion;
    private JTextField tfEditingQuestionQuestion;
    private JTextField tfEditingQuestionAnswer;
    private JTextField tfEditingQuestionValue;
    private JButton btEditingQuestionBackToBoard;
    private JLabel lbEditingQuestionQuestion;


    public enum Panels {
        MainMenu,
        StartingScreen,
        ActualGame,
        OptionsMenu,
        HostScreen,
        CreateGame,
        EditGame,
        EditQuestion
    }
    
    public Game() {
        super("Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        panelMenu = new JPanel();
        panelMenu.setBounds(0, 0, WIDTH, HEIGHT);
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
        btMenuStartGame.setBounds(200, 200, STD_WIDTH, STD_HEIGHT);
        btMenuStartGame.addActionListener(this);
        panelMenu.add(btMenuStartGame);
        btMenuOptions = new JButton("Options");
        btMenuOptions.setBounds(200, 250, STD_WIDTH, STD_HEIGHT);
        btMenuOptions.addActionListener(this);
        panelMenu.add(btMenuOptions);
        btMenuExitGame = new JButton("Exit Game");
        btMenuExitGame.setBounds(200, 300, STD_WIDTH, STD_HEIGHT);
        btMenuExitGame.addActionListener(this);
        panelMenu.add(btMenuExitGame);



        // Options
        panelOptions = new JPanel();
        panelOptions.setBounds(0, 0, WIDTH, HEIGHT);
        panelOptions.setLayout(null);
        
        btOptionsBackToMenu = new JButton("Back");
        btOptionsBackToMenu.setBounds(10, 10, 50, 25);
        btOptionsBackToMenu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btOptionsBackToMenu.addActionListener(this);
        panelOptions.add(btOptionsBackToMenu);
        btOptionsSaveLocationBrowse = new JButton("Browse");
        btOptionsSaveLocationBrowse.setBounds(400, 165, 50, 20);
        btOptionsSaveLocationBrowse.addActionListener(this);
        btOptionsSaveLocationBrowse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btOptionsSaveLocationBrowse.setFont(new Font("Arial", Font.PLAIN, 12));
        panelOptions.add(btOptionsSaveLocationBrowse);
        
        tfOptionsDefaultAddress = new JTextField(gameOptions.get("defaultAddress"));
        tfOptionsDefaultAddress.setBounds(200, 100, STD_WIDTH, STD_HEIGHT);
        panelOptions.add(tfOptionsDefaultAddress);
        tfOptionsSaveLocation = new JTextField(gameOptions.get("saveLocation"));
        tfOptionsSaveLocation.setBounds(200, 150, STD_WIDTH*2, STD_HEIGHT);
        panelOptions.add(tfOptionsSaveLocation);
        
        lbOptionsDefaultAddress = new JLabel("Default Address:");
        lbOptionsDefaultAddress.setBounds(100, 100, STD_WIDTH, STD_HEIGHT);
        panelOptions.add(lbOptionsDefaultAddress);
        lbOptionsSaveLocation = new JLabel("Save Location:");
        lbOptionsSaveLocation.setBounds(100, 150, STD_WIDTH, STD_HEIGHT);
        panelOptions.add(lbOptionsSaveLocation);



        // Starting Screen
        panelStart = new JPanel();
        panelStart.setBounds(0, 0, WIDTH, HEIGHT);
        panelStart.setLayout(null);
        
        btStartHostGame = new JButton("Host Game");
        btStartHostGame.setBounds(175, 250, (int)(STD_WIDTH*1.5), STD_HEIGHT);
        btStartHostGame.addActionListener(this);
        panelStart.add(btStartHostGame);
        btStartConnectServer = new JButton("Connect to Server");
        btStartConnectServer.setBounds(175, 200, (int)(STD_WIDTH*1.5), STD_HEIGHT);
        btStartConnectServer.addActionListener(this);
        panelStart.add(btStartConnectServer);
        btStartCreateGame = new JButton("Game Board Editor");
        btStartCreateGame.setBounds(175, 300, (int)(STD_WIDTH*1.5), STD_HEIGHT);
        btStartCreateGame.addActionListener(this);
        panelStart.add(btStartCreateGame);
        btStartBackToMenu = new JButton("Back");
        btStartBackToMenu.setBounds(10, 10, 50, 25);
        btStartBackToMenu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btStartBackToMenu.addActionListener(this);
        panelStart.add(btStartBackToMenu);
        
        tfStartUsername = new JTextField();
        tfStartUsername.setBounds(200, 100, STD_WIDTH, STD_HEIGHT);
        panelStart.add(tfStartUsername);
        tfStartIpAddress = new JTextField();
        tfStartIpAddress.setBounds(200, 150, STD_WIDTH, STD_HEIGHT);
        panelStart.add(tfStartIpAddress);
        
        lbStartUsername = new JLabel("Username:");
        lbStartUsername.setBounds(100, 100, STD_WIDTH, STD_HEIGHT);
        panelStart.add(lbStartUsername);
        lbStartIpAddress = new JLabel("IP Address:");
        lbStartIpAddress.setBounds(100, 150, STD_WIDTH, STD_HEIGHT);
        panelStart.add(lbStartIpAddress);



        // Game Board Editor
        panelGameBoardEditor = new JPanel();
        panelGameBoardEditor.setBounds(0, 0, WIDTH, HEIGHT);
        panelGameBoardEditor.setLayout(null);
        
        btGameBoardEditorBackToMenu = new JButton("Back");
        btGameBoardEditorBackToMenu.setBounds(10, 10, STD_WIDTH_HALF, STD_HEIGHT_HALF);
        btGameBoardEditorBackToMenu.addActionListener(this);
        btGameBoardEditorBackToMenu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        panelGameBoardEditor.add(btGameBoardEditorBackToMenu);
        btGameBoardEditorCreateNewGameBoard = new JButton("Create New Game Board");
        btGameBoardEditorCreateNewGameBoard.setBounds(150, 200, STD_WIDTH_DOUBLE, STD_HEIGHT);
        btGameBoardEditorCreateNewGameBoard.addActionListener(this);
        panelGameBoardEditor.add(btGameBoardEditorCreateNewGameBoard);
        btGameBoardEditorLoadGameBoard = new JButton("Load Game Board");
        btGameBoardEditorLoadGameBoard.setBounds(150, 250, STD_WIDTH_DOUBLE, STD_HEIGHT);
        btGameBoardEditorLoadGameBoard.addActionListener(this);
        panelGameBoardEditor.add(btGameBoardEditorLoadGameBoard);



        // Editing Screen
        panelEditingBoard = new JPanel();
        panelEditingBoard.setBounds(0, 0, WIDTH, HEIGHT);
        panelEditingBoard.setLayout(null);

        btEditingBoardBackToMenu = new JButton("Back");
        btEditingBoardBackToMenu.setBounds(10, 10, STD_WIDTH_HALF, STD_HEIGHT_HALF);
        btEditingBoardBackToMenu.addActionListener(this);
        btEditingBoardBackToMenu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        panelEditingBoard.add(btEditingBoardBackToMenu);
        btEditingBoardSaveGameBoard = new JButton("Save");
        btEditingBoardSaveGameBoard.setBounds(10, 40, STD_WIDTH_HALF, STD_HEIGHT_HALF);
        btEditingBoardSaveGameBoard.addActionListener(this);
        btEditingBoardSaveGameBoard.setMargin(new java.awt.Insets(0, 0, 0, 0));
        panelEditingBoard.add(btEditingBoardSaveGameBoard);

        btEditingBoardQuestions = new JButton[5][5];
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j ++) {
                btEditingBoardQuestions[i][j] = new JButton();
                btEditingBoardQuestions[i][j].setBounds(100 + (i * STD_WIDTH_POINT_FIVE), 100 + (j * STD_HEIGHT), STD_WIDTH_POINT_FIVE, STD_HEIGHT);
                btEditingBoardQuestions[i][j].addActionListener(this);
                panelEditingBoard.add(btEditingBoardQuestions[i][j]);
            }
        }

        tfEditingBoardCategories = new JTextField[5];
        for(int i = 0; i < 5; i++) {
            tfEditingBoardCategories[i] = new JTextField();
            tfEditingBoardCategories[i].setBounds(100 + (i * STD_WIDTH_POINT_FIVE), 50, STD_WIDTH_POINT_FIVE, STD_HEIGHT);
            panelEditingBoard.add(tfEditingBoardCategories[i]);
        }



        // Editing Question
        panelEditingQuestion = new JPanel();
        panelEditingQuestion.setBounds(0, 0, WIDTH, HEIGHT);
        panelEditingQuestion.setLayout(null);

        tfEditingQuestionQuestion = new JTextField();
        tfEditingQuestionQuestion.setBounds(100, 100, STD_WIDTH*3, STD_HEIGHT);
        panelEditingQuestion.add(tfEditingQuestionQuestion);
        tfEditingQuestionAnswer = new JTextField();
        tfEditingQuestionAnswer.setBounds(100, 150, STD_WIDTH*3, STD_HEIGHT);
        panelEditingQuestion.add(tfEditingQuestionAnswer);
        tfEditingQuestionValue = new JTextField();
        tfEditingQuestionValue.setBounds(100, 200, STD_WIDTH*3, STD_HEIGHT);
        panelEditingQuestion.add(tfEditingQuestionValue);

        btEditingQuestionBackToBoard = new JButton("Back");
        btEditingQuestionBackToBoard.setBounds(10, 10, STD_WIDTH_HALF, STD_HEIGHT_HALF);
        btEditingQuestionBackToBoard.addActionListener(this);
        btEditingQuestionBackToBoard.setMargin(new java.awt.Insets(0, 0, 0, 0));
        panelEditingQuestion.add(btEditingQuestionBackToBoard);

        lbEditingQuestionQuestion = new JLabel("Question:");
        lbEditingQuestionQuestion.setBounds(10, 100, STD_WIDTH, STD_HEIGHT);
        panelEditingQuestion.add(lbEditingQuestionQuestion);



        // Actual Game
        panelGame = new JPanel();
        panelGame.setBounds(0, 0, WIDTH, HEIGHT);
        panelGame.setLayout(null);
        
        btGameLeave = new JButton("Leave");
        btGameLeave.setBounds(200, 400, STD_WIDTH, STD_HEIGHT);
        btGameLeave.addActionListener(this);
        panelGame.add(btGameLeave);
        btGameSendMessage = new JButton("Send");
        btGameSendMessage.setBounds(200, 350, STD_WIDTH, STD_HEIGHT);
        btGameSendMessage.addActionListener(this);
        panelGame.add(btGameSendMessage);
        
        tfGameMessageToSend = new JTextField();
        tfGameMessageToSend.setBounds(100, 350, STD_WIDTH, STD_HEIGHT);
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
        panelHost.setBounds(0, 0, WIDTH, HEIGHT);
        panelHost.setLayout(null);
        
        btHostBackToMenu = new JButton("Back");
        btHostBackToMenu.setBounds(10, 10, STD_WIDTH_HALF, STD_HEIGHT_HALF);
        btHostBackToMenu.addActionListener(this);
        btHostBackToMenu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        panelHost.add(btHostBackToMenu);
    }

    @Override
    public void actionPerformed(ActionEvent evnt) {
        Object src = evnt.getSource();

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
            String boardName;
            try {
                boardName = JOptionPane.showInputDialog(this, "Enter the name of the game board", "Create New Game Board", JOptionPane.PLAIN_MESSAGE);
                if(boardName == null) {
                    return;
                }
                gameBoard = new GameBoard(boardName);
                FileHandler.createGameBoard(boardName, gameOptions.get("saveLocation"));
                FileHandler.saveGameBoard(boardName, gameOptions.get("saveLocation"), gameBoard.toString());
                loadGameBoard(0);
                setPanel(Panels.EditGame, Panels.CreateGame);
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow("Error creating game board");
            }
        } else if(src == btGameBoardEditorLoadGameBoard) {
            try {
                JFileChooser chooser = new JFileChooser(gameOptions.get("saveLocation"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int status = chooser.showOpenDialog(null);
                if (status == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (file == null) {
                        return;
                    }
                    String fileName = chooser.getSelectedFile().getName();
                    fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    gameBoard = GameBoard.fromString(FileHandler.loadGameBoard(fileName, gameOptions.get("saveLocation")));
                    loadGameBoard(0);
                    setPanel(Panels.EditGame, Panels.CreateGame);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow("Error loading game board");
            }
        }
        
        // Editing Screen
        else if(src == btEditingBoardBackToMenu) {
            setPanel(Panels.CreateGame, Panels.EditGame);
        } else if(src == btEditingBoardSaveGameBoard) {
            try {
                FileHandler.saveGameBoard(gameBoard.getBoardID(), gameOptions.get("saveLocation"), gameBoard.toString());
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow("Error saving game board");
            }
        } else if(src == btEditingQuestionBackToBoard) {
            setPanel(Panels.EditGame, Panels.EditQuestion);
        } else {
            for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 5; j ++) {
                    if(src == btEditingBoardQuestions[i][j]) {
                        tfEditingQuestionQuestion.setText(gameBoard.getQuestion(0, i, j));
                        tfEditingQuestionAnswer.setText(gameBoard.getAnswer(0, i, j));
                        tfEditingQuestionValue.setText(gameBoard.getPoints(0, i, j) + "");
                        lbEditingQuestionQuestion.setText("Question: " + gameBoard.getCategory(0, i)+" - "+gameBoard.getPoints(0, i, j));
                        setPanel(Panels.EditQuestion, Panels.EditGame);
                    }
                }
            }
        }

        // Actual Game
        if(src == btGameLeave) {
            leaveGame();
        } else if(src == btGameSendMessage) {
            sendMessage();
        }

        // Host's Screen
        else if(src == btHostBackToMenu) {
            setPanel(Panels.MainMenu, Panels.HostScreen);
        }
    }

    private void loadGameBoard(int boardNumber) {
        for (int col = 0; col < 5; col++) {
            tfEditingBoardCategories[col].setText(gameBoard.getCategory(boardNumber, col));
            System.out.println(gameBoard.getCategory(boardNumber, col));
            for (int row = 0; row < 5; row++) {
                btEditingBoardQuestions[col][row].setText(gameBoard.getPoints(boardNumber, col, row) + "");
            }
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
    
    private void showErrorWindow(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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
            case EditGame:
            remove(panelEditingBoard);
            break;
            case EditQuestion:
            remove(panelEditingQuestion);
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
            case EditGame:
            add(panelEditingBoard);
            break;
            case EditQuestion:
            add(panelEditingQuestion);
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
