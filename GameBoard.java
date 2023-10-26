import java.util.ArrayList;

public class GameBoard {
    private String boardID;
    private int boardCount = 0;
    private ArrayList<String[]> categories;
    private ArrayList<String[][]> questions;
    private ArrayList<String[][]> answers;
    private ArrayList<int[][]> points;
    public GameBoard(String boardID) {
        this.boardID = boardID;
        categories = new ArrayList<String[]>();
        questions = new ArrayList<String[][]>();
        answers = new ArrayList<String[][]>();
        points = new ArrayList<int[][]>();
        addboard();
    }
    public void addboard(){
        boardCount++;
        if(boardCount > 1){
            categories.add(categories.get(categories.size()-1).clone());
        } else
            categories.add(new String[]{"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"});
        questions.add(new String[][]{
            {"Question 1", "Question 2", "Question 3", "Question 4", "Question 5"}, 
            {"Question 6", "Question 7", "Question 8", "Question 9", "Question 10"}, 
            {"Question 11", "Question 12", "Question 13", "Question 14", "Question 15"}, 
            {"Question 16", "Question 17", "Question 18", "Question 19", "Question 20"}, 
            {"Question 21", "Question 22", "Question 23", "Question 24", "Question 25"}});
        answers.add(new String[][]{
            {"Answer 1", "Answer 2", "Answer 3", "Answer 4", "Answer 5"}, 
            {"Answer 6", "Answer 7", "Answer 8", "Answer 9", "Answer 10"}, 
            {"Answer 11", "Answer 12", "Answer 13", "Answer 14", "Answer 15"}, 
            {"Answer 16", "Answer 17", "Answer 18", "Answer 19", "Answer 20"}, 
            {"Answer 21", "Answer 22", "Answer 23", "Answer 24", "Answer 25"}});
        points.add(new int[][]{
            {200, 300, 400, 500, 600},
            {200, 300, 400, 500, 600},
            {200, 300, 400, 500, 600},
            {200, 300, 400, 500, 600},
            {200, 300, 400, 500, 600}});
    }
    public void changeCategory(int boardNumber, int category, String newCategory){
        System.out.println("Changing category "+category+" from "+categories.get(boardNumber)[category]+" to "+newCategory+" on board "+boardNumber+"...");
        categories.get(boardNumber)[category] = newCategory;
    }
    public void changeQuestion(int boardNumber, int category, int question, String newQuestion){
        questions.get(boardNumber)[category][question] = newQuestion;
    }
    public void changeAnswer(int boardNumber, int category, int question, String newAnswer){
        answers.get(boardNumber)[category][question] = newAnswer;
    }
    public void changePoints(int boardNumber, int category, int question, int newPoints){
        points.get(boardNumber)[category][question] = newPoints;
    }
    public int getNumberOfBoards() {
        return boardCount;
    }
    public String getCategory(int boardNumber, int category){
        return categories.get(boardNumber)[category];
    }
    public String getQuestion(int boardNumber, int category, int question){
        return questions.get(boardNumber)[category][question];
    }
    public String getAnswer(int boardNumber, int category, int question){
        return answers.get(boardNumber)[category][question];
    }
    public int getPoints(int boardNumber, int category, int question){
        return points.get(boardNumber)[category][question];
    }
    public String getBoardID(){
        return boardID;
    }
    public String toString(){
        String board = boardID+"\n";
        board += boardCount+"\n";
        for(int i = 0; i < boardCount; i++){
            for(int j = 0; j < 5; j++){
                board += categories.get(i)[j]+"\n";
                for(int k = 0; k < 5; k++){
                    board += questions.get(i)[j][k]+"\n";
                    board += answers.get(i)[j][k]+"\n";
                    board += points.get(i)[j][k]+"\n";
                }
            }
        }
        return board;
    }
    public static GameBoard fromString(String boardContent){
        String[] lines = boardContent.split("\n");
        GameBoard gameBoard = new GameBoard(lines[0]);
        try {
            int boardCount = Integer.parseInt(lines[1]);
            if(boardCount>1){
                while(boardCount>1){
                    gameBoard.addboard();
                    boardCount--;
                }
            }
            int line = 2;
            for(int i = 0; i < gameBoard.boardCount; i++){
                for(int j = 0; j < 5; j++){
                    gameBoard.categories.get(i)[j] = lines[line];
                    line++;
                    for(int k = 0; k < 5; k++){
                        gameBoard.questions.get(i)[j][k] = lines[line];
                        line++;
                        gameBoard.answers.get(i)[j][k] = lines[line];
                        line++;
                        gameBoard.points.get(i)[j][k] = Integer.parseInt(lines[line]);
                        line++;
                    }
                }
            }
            return gameBoard;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}