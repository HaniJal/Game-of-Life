package life;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameOfLifeGUI extends Application {

    private static final int ROWS = 50;
    private static final int COLS = 50;
    private static final int CELL_SIZE = 12;

    private GameOfLife game;
    private Canvas canvas;
    private Timeline timeline;

    @Override
    public void start(Stage stage) {

        game = new GameOfLife(ROWS, COLS);
        canvas = new Canvas(COLS * CELL_SIZE, ROWS * CELL_SIZE);

        draw();

        // Click toggles a cell
        canvas.setOnMouseClicked(e -> {
            int c = (int)(e.getX() / CELL_SIZE);
            int r = (int)(e.getY() / CELL_SIZE);
            game.toggle(r, c);
            draw();
        });

        // Buttons
        Button startBtn = new Button("Start");
        Button stopBtn  = new Button("Stop");
        Button stepBtn  = new Button("Step");

        // Animation loop (updates repeatedly)
        timeline = new Timeline(new KeyFrame(Duration.millis(400), e -> {
            game.step();
            draw();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        // START → If board empty → randomize first
        startBtn.setOnAction(e -> {
            if (isBoardEmpty()) {
                game.randomize(0.25); // 25% of cells alive
                draw();
            }
            timeline.play();
        });

        // STOP → pause animation
        stopBtn.setOnAction(e -> timeline.stop());

        // STEP → one generation, no animation
        stepBtn.setOnAction(e -> {
            timeline.stop();
            game.step();
            draw();
        });

        // Layout
        HBox controls = new HBox(10, startBtn, stopBtn, stepBtn);
        controls.setStyle("-fx-padding: 10px; -fx-background-color: #ddd;");

        BorderPane root = new BorderPane();
        root.setTop(controls);
        root.setCenter(canvas);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Conway's Game of Life (Start / Stop / Step)");
        stage.show();
    }

    // Check if all cells are dead
    private boolean isBoardEmpty() {
        for (int r = 0; r < game.getRows(); r++) {
            for (int c = 0; c < game.getCols(); c++) {
                if (game.isAlive(r, c)) return false;
            }
        }
        return true;
    }

    private void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // Background
        g.setFill(Color.web("#f2f2f2"));
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Grid
        g.setStroke(Color.LIGHTGRAY);
        for (int r = 0; r <= game.getRows(); r++) {
            g.strokeLine(0, r * CELL_SIZE, COLS * CELL_SIZE, r * CELL_SIZE);
        }
        for (int c = 0; c <= game.getCols(); c++) {
            g.strokeLine(c * CELL_SIZE, 0, c * CELL_SIZE, ROWS * CELL_SIZE);
        }

        // Alive cells
        for (int r = 0; r < game.getRows(); r++) {
            for (int c = 0; c < game.getCols(); c++) {
                if (game.isAlive(r, c)) {
                    g.setFill(Color.BLACK);
                    g.fillRect(c * CELL_SIZE + 1, r * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
