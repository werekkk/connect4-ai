package view.main_view;

import ai.BoardUtils;
import ai.StateScore;
import ai.Ai;
import ai.AiConfig;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Board;
import model.Move;
import view.Dialogs;
import view.ObservableBoard;
import model.Color;
import view.ControlUtils;
import view.GameStats;
import view.board.BoardView;

import java.util.List;
import java.util.Random;

public class MainViewController {

    private final static int ROW_COUNT = 6;
    private final static int COLUMN_COUNT = 7;

    private final static int DEFAULT_DEPTH = 2;
    private final static int MIN_DEPTH = 1;
    private final static int MAX_DEPTH = 10;

    private final static int DEFAULT_REPEATS = 1;
    private final static int MIN_REPEATS = 1;
    private final static int MAX_REPEATS = 5000;

    @FXML private BoardView boardView;

    @FXML private Button startBtn;
    @FXML private Button resetBtn;

    @FXML private Label yellowCpScore;
    @FXML private Label redCpScore;
    @FXML private Label yellowCsScore;
    @FXML private Label redCsScore;
    @FXML private Label repeatLabel;
    @FXML private Label firstMoveLabel;

    @FXML private TextField depthYellowField;
    @FXML private TextField depthRedField;
    @FXML private TextField repeatCountField;

    @FXML private RadioButton aiYellowPlayerBtn;
    @FXML private RadioButton aiRedPlayerBtn;
    @FXML private RadioButton minmaxYellowBtn;
    @FXML private RadioButton minmaxRedBtn;
    @FXML private RadioButton cpYellowBtn;
    @FXML private RadioButton cpRedBtn;
    @FXML private RadioButton csYellowBtn;
    @FXML private RadioButton csRedBtn;

    @FXML private CheckBox randomFirstMove;

    @FXML private ChoiceBox<String> firstMoveBox;

    @FXML private GridPane aiYellowOptions;
    @FXML private GridPane aiRedOptions;

    private ObservableBoard board;
    private Ai ai;
    private Color currentColor = Color.YELLOW;
    private Color winner = null;
    private boolean isPlaying = false;
    private GameStats stats = new GameStats();

    private int moveCt = 1;
    private int currentRound = 1;
    private int totalRounds = 0;

    @FXML
    private void initialize() {
        board = new ObservableBoard(ROW_COUNT, COLUMN_COUNT);
        ai = new Ai();
        boardView.setBoard(board);
        boardView.setFieldClickedHandler(field -> onHumanSelectedColumn(field.getColumn()));
        boardView.setDisable(true);
        firstMoveBox.setItems(FXCollections.observableArrayList(List.of("1", "2", "3", "4", "5", "6", "7")));
        firstMoveBox.setValue("1");
        initTextFields();
        initButtons();
        refreshScores();
        setPlayingState(false);
    }

    private void initTextFields() {
        ControlUtils.initIntegerTextField(depthYellowField, DEFAULT_DEPTH, MIN_DEPTH, MAX_DEPTH);
        ControlUtils.initIntegerTextField(depthRedField, DEFAULT_DEPTH, MIN_DEPTH, MAX_DEPTH);
        ControlUtils.initIntegerTextField(repeatCountField, DEFAULT_REPEATS, MIN_REPEATS, MAX_REPEATS);
    }

    private void initButtons() {
        randomFirstMove.selectedProperty().addListener((obs, oldV, newV) -> {
            firstMoveLabel.setDisable(newV || !aiYellowPlayerBtn.isSelected());
            firstMoveBox.setDisable(newV || !aiYellowPlayerBtn.isSelected());
        });
        initRadioButtons();
    }

    private void initRadioButtons() {
        aiYellowOptions.setDisable(true);
        aiRedOptions.setDisable(true);
        aiYellowPlayerBtn.selectedProperty().addListener((obs, oldV, newV) -> {
            aiYellowOptions.setDisable(!newV);
            randomFirstMove.setDisable(!newV);
            firstMoveLabel.setDisable(!(newV && !randomFirstMove.isSelected()));
            firstMoveBox.setDisable(!(newV && !randomFirstMove.isSelected()));
        });
        aiRedPlayerBtn.selectedProperty().addListener((obs, oldV, newV) -> aiRedOptions.setDisable(!newV));
    }

    @FXML
    private void handleStartClicked() {
        clearBoard();
        ai.newGame(parseConfig());
        setPlayingState(true);
        stats = new GameStats();
        currentRound = 1;
        totalRounds = Integer.parseInt(repeatCountField.getText());
        new Thread(this::playAllRounds).start();
    }

    private void setPlayingState(boolean isPlaying) {
        resetBtn.setDisable(!isPlaying);
        startBtn.setDisable(isPlaying);
        repeatCountField.setDisable(isPlaying);
        repeatLabel.setDisable(isPlaying);
        if (isPlaying) {
            resetBtn.requestFocus();
        } else {
            startBtn.requestFocus();
        }
    }

    private void playAllRounds() {
        for (; currentRound <= totalRounds; currentRound++) {
            clearBoard();
            startRound();
            waitForRoundFinished();
            stats.finishRound(winner);
            repeatCountField.setText((totalRounds - currentRound) + "");
        }
        repeatCountField.setText(totalRounds + "");
        Dialogs.showFinishDialog(board.toBoard(), stats);
    }

    private void startRound() {
        isPlaying = true;
        moveCt = 1;
        stats.resetRound();
        if (aiYellowPlayerBtn.isSelected()) {
            makeFirstMove();
        }
        startPlayThread();
    }

    private void makeFirstMove() {
        Move move;
        if (randomFirstMove.isSelected()) {
            move = new Move((new Random()).nextInt(7), currentColor);
        } else {
            move = new Move(Integer.parseInt(firstMoveBox.getValue()) - 1, currentColor);
        }
        stats.addMove(currentColor, 0);
        apply(move);
    }

    private void waitForRoundFinished() {
        while (isPlaying) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private AiConfig parseConfig() {
        AiConfig aiConfig = new AiConfig();
        aiConfig.yellowIsAi = aiYellowPlayerBtn.isSelected();
        aiConfig.yellowAlg = (minmaxYellowBtn.isSelected() ? AiConfig.AiAlgorithm.MINIMAX : AiConfig.AiAlgorithm.ALPHABETA);
        aiConfig.yellowDepth = Integer.parseInt(depthYellowField.getText());
        aiConfig.redIsAi = aiRedPlayerBtn.isSelected();
        aiConfig.redAlg = (minmaxRedBtn.isSelected() ? AiConfig.AiAlgorithm.MINIMAX : AiConfig.AiAlgorithm.ALPHABETA);
        aiConfig.redDepth = Integer.parseInt(depthRedField.getText());
        aiConfig.yellowScoreHeuristic = (cpYellowBtn.isSelected() ? AiConfig.ScoreHeuristic.CURRENT_POSSIBILITIES
                : (csYellowBtn.isSelected() ? AiConfig.ScoreHeuristic.CURRENT_STATE : AiConfig.ScoreHeuristic.SIMPLE));
        aiConfig.redScoreHeuristic = (cpRedBtn.isSelected() ? AiConfig.ScoreHeuristic.CURRENT_POSSIBILITIES
                : (csRedBtn.isSelected() ? AiConfig.ScoreHeuristic.CURRENT_STATE : AiConfig.ScoreHeuristic.SIMPLE));
        return aiConfig;
    }

    @FXML
    private void handleResetClicked() {
        isPlaying = false;
        currentRound = totalRounds;
        clearBoard();
        setPlayingState(false);
        repeatCountField.setText(totalRounds + "");
    }

    private void clearBoard() {
        board.clear();
        boardView.clear();
        currentColor = Color.YELLOW;
        refreshScores();
        boardView.setDisable(true);
    }

    private void refreshScores() {
        Platform.runLater(() -> {
            yellowCpScore.setText(StateScore.currentPossibilitiesScore(board.toBoard(), Color.YELLOW) + "");
            redCpScore.setText(StateScore.currentPossibilitiesScore(board.toBoard(), Color.RED) + "");
            yellowCsScore.setText(StateScore.currentStateScore(board.toBoard(), Color.YELLOW) + "");
            redCsScore.setText(StateScore.currentStateScore(board.toBoard(), Color.RED) + "");
        });
    }

    private void onHumanSelectedColumn(int column) {
        if (board.canPlace(column)) {
            stats.addMove(currentColor, 0);
            apply(new Move(column, currentColor));
            boardView.setDisable(true);
            if (isPlaying)
                startPlayThread();
        }
    }

    private void apply(Move move) {
        boardView.setNumber(board.findLowestEmptyRow(move.column), move.column, moveCt);
        moveCt++;
        board.apply(move);
        refreshScores();
        currentColor = currentColor.opposite();
        checkBoard(board.toBoard());
    }

    private void checkBoard(Board board) {
        winner = BoardUtils.findWinner(board);
        if (winner != null || board.isFull()) {
            isPlaying = false;
        }
    }

    private void startPlayThread() {
        Thread playThread = new Thread(this::play);
        playThread.start();
    }

    private void play() {
        while (isPlaying && !ai.isHuman(currentColor)) {
            long start = System.currentTimeMillis();
            Move move = ai.suggestMove(board.toBoard(), currentColor, stats);
            if (isPlaying) {
                stats.addMove(currentColor, System.currentTimeMillis() - start);
                apply(move);
            }
        }
        if (isPlaying && ai.isHuman(currentColor)) {
            boardView.setDisable(false);
        }
    }

}
