import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import org.json.*;



public class Game extends JFrame implements ActionListener{

    private Server server;
    private Client client;
    private HashMap<String, String> gameOptions;

    private final int WIDTH = 1244;
    private final int HEIGHT = 700;
    private final int STD_WIDTH = 100;
    private final int STD_WIDTH_HALF = 50;
    private final int STD_WIDTH_DOUBLE = 200;
    private final int STD_WIDTH_ONE_POINT_FIVE = 150;
    private final int STD_HEIGHT = 50;
    private final int STD_HEIGHT_HALF = 25;
    private final int STD_HEIGHT_DOUBLE = 100;
    private final int STD_HEIGHT_POINT_FIVE = 75;
    private final String DEFAULT_SAVE_LOCATION = "C:\\Users\\Public\\Documents\\JeopardyGame\\";
    private final String DEFAULT_ADDRESS = "localhost";
    private final String DEFAULT_USERNAME = "unknown";

    private StyleContext.NamedStyle centerStyle;

    private JPanel panelMenu;
    private JButton btMenuStartGame;
    private JButton btMenuOptions;
    private JButton btMenuExitGame;

    private JPanel panelOptions;
    private JButton btOptionsBackToMenu;
    private JButton btOptionsSaveLocationBrowse;
    private JTextField tfOptionsDefaultAddress;
    private JTextField tfOptionsSaveLocation;
    private JTextField tfOptionsUsername;
    private JLabel lbOptionsDefaultAddress;
    private JLabel lbOptionsSaveLocation;
    private JLabel lbOptionsUsername;

    private JPanel panelStart;
    private JButton btStartHostGame;
    private JButton btStartConnectServer;
    private JButton btStartCreateGame;
    private JButton btStartBackToMenu;
    private JTextField tfStartIpAddress;
    private JLabel lbStartIpAddress;

    private JPanel panelGame;
    private JButton btGameLeave;
    private JButton btGameSendMessage;
    private JTextField tfGameMessageToSend;
    private JTextArea taGameChat;
    private JScrollBar sbGameChat;

    private JPanel panelHost;
    private JButton btHostBackToMenu;
    private JTextField[] tfHostCategories;
    private JButton[][] btHostQuestions;

    private JPanel panelGameBoardEditor;
    private JButton btGameBoardEditorBackToMenu;
    private JButton btGameBoardEditorCreateNewGameBoard;
    private JButton btGameBoardEditorLoadGameBoard;
    private GameBoard gameBoard;

    private JPanel panelEditingBoard;
    private JButton btEditingBoardBackToMenu;
    private JButton btEditingBoardSaveGameBoard;
    private JButton[][] btEditingBoardQuestions;
    private JButton btEditingBoardCycleBoardNumberDown;
    private JButton btEditingBoardCycleBoardNumberUp;
    private JButton btEditingBoardBoardNumber;
    private JButton btEditingBoardAddBoard;
    private JTextField[] tfEditingBoardCategories;

    private JPanel panelEditingQuestion;
    private JTextField tfEditingQuestionQuestion;
    private JTextField tfEditingQuestionAnswer;
    private JTextField tfEditingQuestionValue;
    private JButton btEditingQuestionBackToBoard;
    private JLabel lbEditingQuestionQuestion;
    private int currentBoardNumber = 0;
    private int currentCategory = 0;
    private int currentQuestion = 0;

    private JPanel panelQuestion;
    private JTextPane tpQuestionQuestionOrAnswer;
    private JTextPane[] tpQuestionAnswers;
    private JTextField[] tfQuestionPoints;

    private JTextPane tpQuestionHostQuestion;
    private JTextPane tpQuestionHostAnswer;
    private JButton btQuestionHostGivePoints;
    private JButton btQuestionHostRemovePoints;
    private JButton btQuestionHostShowQuestion;
    private JButton btQuestionHostLockAnswers;
    private JButton btQuestionHostRevealAnswer;
    private JButton btQuestionHostBackToBoard;
    private JCheckBox[] cbQuestionHostWinners;



    public enum Panels {
        MainMenu,
        StartingScreen,
        ActualGame,
        OptionsMenu,
        HostScreen,
        CreateGame,
        EditGame,
        EditQuestion,
        QuestionScreen
    }
    
    public Game() {
        super("Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        System.out.println("Latest Version: " + getLatestVersion());

        initComponents();
        setVisible(true);
    }

    public static String getLatestVersion() {
        try {
            URI uri = new URI("https://api.github.com/repos/Beng420/JeopardyGame/releases/latest");
            URL url = uri.toURL();
            InputStream is = url.openStream();
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("\"tag_name\":")) {
                    scanner.close();
                    is.close();
                    JSONObject json = new JSONObject(line);
                    return json.getString("name").split("v.")[1].substring(0, 5);
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initComponents() {
        panelMenu = new JPanel();
        panelMenu.setBounds(0, 0, WIDTH, HEIGHT);
        panelMenu.setLayout(null);

        loadOptions();

        
        centerStyle = StyleContext.getDefaultStyleContext().new NamedStyle();
        StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
        


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
        tfOptionsUsername = new JTextField(gameOptions.get("username"));
        tfOptionsUsername.setBounds(200, 200, STD_WIDTH, STD_HEIGHT);
        panelOptions.add(tfOptionsUsername);
        
        lbOptionsDefaultAddress = new JLabel("Default Address:");
        lbOptionsDefaultAddress.setBounds(100, 100, STD_WIDTH, STD_HEIGHT);
        panelOptions.add(lbOptionsDefaultAddress);
        lbOptionsSaveLocation = new JLabel("Save Location:");
        lbOptionsSaveLocation.setBounds(100, 150, STD_WIDTH, STD_HEIGHT);
        panelOptions.add(lbOptionsSaveLocation);
        lbOptionsUsername = new JLabel("Username:");
        lbOptionsUsername.setBounds(100, 200, STD_WIDTH, STD_HEIGHT);
        panelOptions.add(lbOptionsUsername);



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
        
        tfStartIpAddress = new JTextField();
        tfStartIpAddress.setBounds(200, 150, STD_WIDTH, STD_HEIGHT);
        panelStart.add(tfStartIpAddress);
        
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
        for(int col = 0; col < 5; col++) {
            for(int row = 0; row < 5; row ++) {
                btEditingBoardQuestions[col][row] = new JButton();
                btEditingBoardQuestions[col][row].setBounds(100 + (col * STD_WIDTH_ONE_POINT_FIVE), 100 + (row * STD_HEIGHT), STD_WIDTH_ONE_POINT_FIVE, STD_HEIGHT);
                btEditingBoardQuestions[col][row].addActionListener(this);
                panelEditingBoard.add(btEditingBoardQuestions[col][row]);
            }
        }
        btEditingBoardCycleBoardNumberDown = new JButton("<");
        btEditingBoardCycleBoardNumberDown.setBounds(100, 100 + (5 * STD_HEIGHT), STD_HEIGHT, STD_HEIGHT);
        btEditingBoardCycleBoardNumberDown.addActionListener(this);
        btEditingBoardCycleBoardNumberDown.setEnabled(false);
        panelEditingBoard.add(btEditingBoardCycleBoardNumberDown);
        btEditingBoardBoardNumber = new JButton(1+"");
        btEditingBoardBoardNumber.setBounds(100 + STD_HEIGHT, 100 + (5 * STD_HEIGHT), STD_HEIGHT, STD_HEIGHT);
        btEditingBoardBoardNumber.addActionListener(this);
        btEditingBoardBoardNumber.setEnabled(false);
        panelEditingBoard.add(btEditingBoardBoardNumber);
        btEditingBoardCycleBoardNumberUp = new JButton(">");
        btEditingBoardCycleBoardNumberUp.setBounds(100 + (2 * STD_HEIGHT), 100 + (5 * STD_HEIGHT), STD_HEIGHT, STD_HEIGHT);
        btEditingBoardCycleBoardNumberUp.addActionListener(this);
        btEditingBoardCycleBoardNumberUp.setEnabled(false);
        panelEditingBoard.add(btEditingBoardCycleBoardNumberUp);
        btEditingBoardAddBoard = new JButton("Add Board");
        btEditingBoardAddBoard.setBounds(100, 100 + (6 * STD_HEIGHT), STD_WIDTH_ONE_POINT_FIVE, STD_HEIGHT);
        btEditingBoardAddBoard.addActionListener(this);
        panelEditingBoard.add(btEditingBoardAddBoard);

        tfEditingBoardCategories = new JTextField[5];
        for(int i = 0; i < 5; i++) {
            tfEditingBoardCategories[i] = new JTextField();
            tfEditingBoardCategories[i].setBounds(100 + (i * STD_WIDTH_ONE_POINT_FIVE), 50, STD_WIDTH_ONE_POINT_FIVE, STD_HEIGHT);
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

        lbEditingQuestionQuestion = new JLabel("Question: ");
        lbEditingQuestionQuestion.setBounds(100, 50, STD_WIDTH*4, STD_HEIGHT);
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
        btHostQuestions = new JButton[5][5];

        tfHostCategories = new JTextField[5];
        for(int col = 0; col < 5; col++) {
            tfHostCategories[col] = new JTextField();
            tfHostCategories[col].setBounds(100 + (col * STD_WIDTH_ONE_POINT_FIVE), 50, STD_WIDTH_ONE_POINT_FIVE, STD_HEIGHT);
            panelHost.add(tfHostCategories[col]);
            for(int row = 0; row < 5; row ++) {
                btHostQuestions[col][row] = new JButton();
                btHostQuestions[col][row].setBounds(100 + (col * STD_WIDTH_ONE_POINT_FIVE), 100 + (row * STD_HEIGHT), STD_WIDTH_ONE_POINT_FIVE, STD_HEIGHT);
                btHostQuestions[col][row].addActionListener(this);
                panelHost.add(btHostQuestions[col][row]);
            }
        }



        // Questioning Screen
        panelQuestion = new JPanel();
        panelQuestion.setBounds(0, 0, WIDTH, HEIGHT);
        panelQuestion.setLayout(null);

        tpQuestionQuestionOrAnswer = new JTextPane();
        tpQuestionQuestionOrAnswer.setBounds(100, 100, STD_WIDTH*4, STD_HEIGHT*3);
        tpQuestionQuestionOrAnswer.setFocusable(false);
        tpQuestionQuestionOrAnswer.setEditable(false);
        initTextPane(tpQuestionQuestionOrAnswer);
        panelQuestion.add(tpQuestionQuestionOrAnswer);
        tpQuestionAnswers = new JTextPane[4];
        tfQuestionPoints = new JTextField[4];
        for(int i = 0; i < 4; i++) {
            tpQuestionAnswers[i] = new JTextPane();
            tpQuestionAnswers[i].setBounds(100 + (i * STD_WIDTH), 100+STD_HEIGHT*4 , STD_WIDTH, STD_HEIGHT*2);
            tpQuestionAnswers[i].setFocusable(false);
            tpQuestionAnswers[i].setEditable(false);
            initTextPane(tpQuestionAnswers[i]);
            panelQuestion.add(tpQuestionAnswers[i]);
            tfQuestionPoints[i] = new JTextField();
            tfQuestionPoints[i].setBounds(100 + (i * STD_WIDTH), 100+STD_HEIGHT*8 , STD_WIDTH, STD_HEIGHT_HALF);
            tfQuestionPoints[i].setFocusable(false);
            panelQuestion.add(tfQuestionPoints[i]);
        }

        add(panelMenu);
        repaint();
    }

    private void loadOptions() {
        gameOptions = FileHandler.loadGameOptions();
        if(gameOptions == null) {
            gameOptions = new HashMap<String, String>();
            gameOptions.put("defaultAddress", DEFAULT_ADDRESS);
            gameOptions.put("saveLocation", DEFAULT_SAVE_LOCATION);
            gameOptions.put("username", DEFAULT_USERNAME);
        } else {
            if(!(gameOptions.get("defaultAddress") != null && !gameOptions.get("defaultAddress").equals(""))) {
                gameOptions.put("defaultAddress", DEFAULT_ADDRESS);
                System.out.println("defaultAddress is null, setting to default");
            }
            if(!(gameOptions.get("saveLocation") != null && !gameOptions.get("saveLocation").equals(""))) {
                gameOptions.put("saveLocation", DEFAULT_SAVE_LOCATION);
                System.out.println("saveLocation is null, setting to default");
            }
            if(!(gameOptions.get("username") != null && !gameOptions.get("username").equals(""))) {
                gameOptions.put("username", "unknown");
                System.out.println("username is null, setting to default");
            }
        }

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
            chooseBoardToLoad();
            for(int col = 0; col < 5; col++) {
                tfHostCategories[col].setText(gameBoard.getCategory(0, col));
                for(int row = 0; row < 5; row ++) {
                    btHostQuestions[col][row].setText(gameBoard.getPoints(0, col, row) + "");
                }
            }
            openServer();
        } else if(src == btStartConnectServer) {
            connectToServer(false);
            setPanel(Panels.ActualGame, Panels.StartingScreen);
        } else if(src == btStartBackToMenu) {
            tfStartIpAddress.setText("");
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
            boolean successful = chooseBoardToLoad();
            if(successful){
                loadGameBoard(0);
                setPanel(Panels.EditGame, Panels.CreateGame);
                if(gameBoard.getNumberOfBoards()>1) {
                    btEditingBoardCycleBoardNumberUp.setEnabled(true);
                }
            }
        }
        
        // Editing Screen
        else if(src == btEditingBoardBackToMenu) {
            setPanel(Panels.CreateGame, Panels.EditGame);
        } else if(src == btEditingBoardSaveGameBoard) {
            saveGameBoard();
        } else if(src == btEditingQuestionBackToBoard) {
            try {
                gameBoard.changeQuestion(currentBoardNumber, currentCategory, currentQuestion, tfEditingQuestionQuestion.getText());
                gameBoard.changeAnswer(currentBoardNumber, currentCategory, currentQuestion, tfEditingQuestionAnswer.getText());
                gameBoard.changePoints(currentBoardNumber, currentCategory, currentQuestion, Integer.parseInt(tfEditingQuestionValue.getText()));
                btEditingBoardQuestions[currentCategory][currentQuestion].setText(gameBoard.getPoints(currentBoardNumber, currentCategory, currentQuestion) + "");
                setPanel(Panels.EditGame, Panels.EditQuestion);
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow("Error saving question");
            }
        } else {
            for(int col = 0; col < 5; col++) {
                for(int row = 0; row < 5; row ++) {
                    if(src == btEditingBoardQuestions[col][row]) {
                        currentCategory = col;
                        currentQuestion = row;
                        tfEditingQuestionQuestion.setText(gameBoard.getQuestion(0, col, row));
                        tfEditingQuestionAnswer.setText(gameBoard.getAnswer(0, col, row));
                        tfEditingQuestionValue.setText(gameBoard.getPoints(0, col, row) + "");
                        lbEditingQuestionQuestion.setText("Question: " + gameBoard.getPoints(0, col, row)+" - "+gameBoard.getCategory(0, col));
                        setPanel(Panels.EditQuestion, Panels.EditGame);
                    }
                }
            }
        } if (src == btEditingBoardCycleBoardNumberDown) {
            saveGameBoard();
            if(currentBoardNumber>0) {
                currentBoardNumber--;
                btEditingBoardBoardNumber.setText(""+(currentBoardNumber+1));
                if(currentBoardNumber==0) {
                    btEditingBoardCycleBoardNumberDown.setEnabled(false);
                }
                btEditingBoardCycleBoardNumberUp.setEnabled(true);
                loadGameBoard(currentBoardNumber);
            }
        } else if (src == btEditingBoardCycleBoardNumberUp) {
            saveGameBoard();
            if(currentBoardNumber<gameBoard.getNumberOfBoards()-1) {
                currentBoardNumber++;
                btEditingBoardBoardNumber.setText(""+(currentBoardNumber+1));
                if(currentBoardNumber==gameBoard.getNumberOfBoards()-1) {
                    btEditingBoardCycleBoardNumberUp.setEnabled(false);
                }
                btEditingBoardCycleBoardNumberDown.setEnabled(true);
                loadGameBoard(currentBoardNumber);
            }
        } else if (src == btEditingBoardAddBoard) {
            gameBoard.addboard();
            btEditingBoardCycleBoardNumberUp.setEnabled(true);
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
        } else {
            for(int col = 0; col < 5; col++) {
                for(int row = 0; row < 5; row ++) {
                    if(src == btHostQuestions[col][row]) {
                        openQuestionScreen(col, row, true);
                    }
                }
            }
        }

        // Questioning Screen
        
    }

    private boolean chooseBoardToLoad() {
        try {
            JFileChooser chooser = new JFileChooser(gameOptions.get("saveLocation"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int status = chooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (file == null) {
                    return false;
                }
                String fileName = chooser.getSelectedFile().getName();
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                gameBoard = GameBoard.fromString(FileHandler.loadGameBoard(fileName, gameOptions.get("saveLocation")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorWindow("Error loading game board");
            return false;
        }
        return true;
    }

    private void openQuestionScreen(int category, int question, boolean host) {
        if(host) {
            // add host buttons and stuff
            tpQuestionHostQuestion = new JTextPane();
            tpQuestionHostQuestion.setText(gameBoard.getQuestion(0, category, question));
            tpQuestionHostQuestion.setBounds(600, 100, STD_WIDTH*3, STD_HEIGHT*3);
            tpQuestionHostQuestion.setEditable(false);
            tpQuestionHostQuestion.setFocusable(false);
            initTextPane(tpQuestionHostQuestion);
            panelQuestion.add(tpQuestionHostQuestion);
            tpQuestionHostAnswer = new JTextPane();
            tpQuestionHostAnswer.setText(gameBoard.getAnswer(0, category, question));
            tpQuestionHostAnswer.setBounds(600, 100+STD_HEIGHT*3, STD_WIDTH*3, STD_HEIGHT*3);
            tpQuestionHostAnswer.setEditable(false);
            tpQuestionHostAnswer.setFocusable(false);
            initTextPane(tpQuestionHostAnswer);
            panelQuestion.add(tpQuestionHostAnswer);
            btQuestionHostShowQuestion = new JButton("Show Question");
            btQuestionHostShowQuestion.setBounds(600, 100+STD_HEIGHT*6, STD_WIDTH, STD_HEIGHT);
            btQuestionHostShowQuestion.addActionListener(this);
            btQuestionHostShowQuestion.setMargin(new java.awt.Insets(0, 0, 0, 0));
            panelQuestion.add(btQuestionHostShowQuestion);
            btQuestionHostLockAnswers = new JButton("Lock Answers");
            btQuestionHostLockAnswers.setBounds(600+STD_WIDTH, 100+STD_HEIGHT*6, STD_WIDTH, STD_HEIGHT);
            btQuestionHostLockAnswers.addActionListener(this);
            btQuestionHostLockAnswers.setMargin(new java.awt.Insets(0, 0, 0, 0));
            panelQuestion.add(btQuestionHostLockAnswers);
            btQuestionHostRevealAnswer = new JButton("Reveal Answer");
            btQuestionHostRevealAnswer.setBounds(600+STD_WIDTH_DOUBLE, 100+STD_HEIGHT*6, STD_WIDTH, STD_HEIGHT);
            btQuestionHostRevealAnswer.addActionListener(this);
            btQuestionHostRevealAnswer.setMargin(new java.awt.Insets(0, 0, 0, 0));
            panelQuestion.add(btQuestionHostRevealAnswer);
            cbQuestionHostWinners = new JCheckBox[4];
            for (int i = 0; i < 4; i++) {
                cbQuestionHostWinners[i] = new JCheckBox();
                int w = (int)(STD_WIDTH_HALF/2);
                cbQuestionHostWinners[i].setBounds(600+(i*w), 100+STD_HEIGHT*7, w, STD_HEIGHT);
                cbQuestionHostWinners[i].setHorizontalAlignment(SwingConstants.CENTER);
                panelQuestion.add(cbQuestionHostWinners[i]);
            }
            btQuestionHostGivePoints = new JButton("Award Points");
            btQuestionHostGivePoints.setBounds(600+STD_WIDTH, 100+STD_HEIGHT*7, STD_WIDTH, STD_HEIGHT);
            btQuestionHostGivePoints.addActionListener(this);
            btQuestionHostGivePoints.setMargin(new java.awt.Insets(0, 0, 0, 0));
            panelQuestion.add(btQuestionHostGivePoints);
            btQuestionHostRemovePoints = new JButton("Remove Points");
            btQuestionHostRemovePoints.setBounds(600+STD_WIDTH_DOUBLE, 100+STD_HEIGHT*7, STD_WIDTH, STD_HEIGHT);
            btQuestionHostRemovePoints.addActionListener(this);
            btQuestionHostRemovePoints.setMargin(new java.awt.Insets(0, 0, 0, 0));
            panelQuestion.add(btQuestionHostRemovePoints);
            btQuestionHostBackToBoard = new JButton("Back");
            btQuestionHostBackToBoard.setBounds(600, 100+STD_HEIGHT*8, STD_WIDTH_HALF, STD_HEIGHT_HALF);
            btQuestionHostBackToBoard.addActionListener(this);
            btQuestionHostBackToBoard.setMargin(new java.awt.Insets(0, 0, 0, 0));
            panelQuestion.add(btQuestionHostBackToBoard);
            setPanel(Panels.QuestionScreen, Panels.HostScreen);
        } else {
            setPanel(Panels.QuestionScreen, Panels.ActualGame);
        }
    }

    private void initTextPane(JTextPane textPane) {
        textPane.setLogicalStyle(centerStyle);
        textPane.setBorder(BorderFactory.createLineBorder(Color.black));
        textPane.setFont(new Font("Arial", Font.PLAIN, 20));
    }

    private void loadGameBoard(int boardNumber) {
        for (int col = 0; col < 5; col++) {
            tfEditingBoardCategories[col].setText(gameBoard.getCategory(boardNumber, col));
            for (int row = 0; row < 5; row++) {
                btEditingBoardQuestions[col][row].setText(gameBoard.getPoints(boardNumber, col, row) + "");
            }
        }
    }
    private void saveGameBoard() {
        for(int col = 0; col < 5; col++) {
            gameBoard.changeCategory(currentBoardNumber, col, tfEditingBoardCategories[col].getText());
        }

        try {
            FileHandler.saveGameBoard(gameBoard.getBoardID(), gameOptions.get("saveLocation"), gameBoard.toString());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorWindow("Error saving game board");
        }
    }

    private void sendMessage() {
        String message = tfGameMessageToSend.getText();
        if(message.equals("")) {
            return;
        }
        client.sendMessage(message);
        taGameChat.append("<"+client.getUsername()+">"+message + "\n");
        tfGameMessageToSend.setText("");
    }

    private void openServer() {
        String username = gameOptions.get("username");
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
        String username = gameOptions.get("username");
        String ipAddress = "localhost";
        if(!localhosting) {
            ipAddress = tfStartIpAddress.getText();
            if(username.equals("") || ipAddress.equals("")) {
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
            case QuestionScreen:
            remove(panelQuestion);
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
            case QuestionScreen:
            add(panelQuestion);
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
