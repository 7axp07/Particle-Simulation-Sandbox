package com.company;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.company.Main.Material.*;

public class Main extends Application {

    enum Material {
        SAND(Color.SANDYBROWN), ROCK(Color.GRAY), WATER(Color.BLUE), EMPTY(Color.BLACK),
        WOOD(Color.SADDLEBROWN), ACID(Color.LIMEGREEN), LAVA(Color.ORANGERED), STEAM(Color.LIGHTGRAY),
        RAIN(Color.BLUE), SNOW(Color.WHITE);

        private final Color color;

        Material(Color color) {
            this.color = color;
        }
    }

    static final int WIDTH = 300;
    static final int HEIGHT = 300;

    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    Material[][] board = new Material[WIDTH][HEIGHT];
    Material current = SAND;
    List<Material> materialList = new ArrayList<>();

    int brushSize = 5;
    double density = 0.6;
    boolean isPressed = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        root.getChildren().add(canvas);
        for (int r = HEIGHT - 1; r >= 0; r--) {
            for (int c = 0; c < WIDTH; c++) {
                board[r][c] = EMPTY;
            }
        }

        materialList.add(SAND);
        materialList.add(WATER);
        materialList.add(LAVA);
        materialList.add(ROCK);
        materialList.add(WOOD);
        materialList.add(EMPTY);
        materialList.add(ACID);
        materialList.add(STEAM);
      //  materialList.add(RAIN);
        materialList.add(SNOW);



        Slider brushSizeSlider = new Slider(1, 10, 5);
        brushSizeSlider.setOnMouseClicked(event -> {
            brushSize = (int) brushSizeSlider.getValue();
        });

        brushSizeSlider.setMaxSize(WIDTH / 4, HEIGHT / 10);
        brushSizeSlider.setTranslateX(10);
        brushSizeSlider.setTranslateY(10);
        root.getChildren().add(brushSizeSlider);

        Slider densitySlider = new Slider(0.1, 1, 1);
        densitySlider.setOnMouseClicked(event -> {
            density = densitySlider.getValue();
        });
        densitySlider.setMaxSize(WIDTH / 4, HEIGHT / 10);
        densitySlider.setTranslateX(10);
        densitySlider.setTranslateY(40);
        root.getChildren().add(densitySlider);

        Rectangle currentMaterial = new Rectangle(WIDTH / 10, HEIGHT / 10, Color.SANDYBROWN);
        currentMaterial.setStroke(Color.WHITE);
        currentMaterial.setTranslateX(10);
        currentMaterial.setTranslateY(60);
        root.getChildren().add(currentMaterial);
        //ColorPicker

        ComboBox<Material> chooseMaterials = new ComboBox<>();
        chooseMaterials.setTranslateX(10);
        chooseMaterials.setTranslateY(100);
        chooseMaterials.getItems().addAll(materialList);
        root.getChildren().add(chooseMaterials);

        chooseMaterials.setOnAction(event -> {
            current = chooseMaterials.getValue();
            currentMaterial.setFill(chooseMaterials.getValue().color);
        });

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        //KEYBINDS
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.S) {
                current = SAND;
                currentMaterial.setFill(Color.SANDYBROWN);
            }
            if (event.getCode() == KeyCode.E) {
                current = EMPTY;
                currentMaterial.setFill(Color.BLACK);
            }
            if (event.getCode() == KeyCode.R) {
                current = ROCK;
                currentMaterial.setFill(Color.GRAY);
            }
            if (event.getCode() == KeyCode.W) {
                current = WATER;
                currentMaterial.setFill(Color.BLUE);
            }
            if (event.getCode() == KeyCode.L) {
                current = LAVA;
                currentMaterial.setFill(Color.ORANGERED);
            }
            if (event.getCode() == KeyCode.A) {
                current = STEAM;
                currentMaterial.setFill(Color.LIGHTGRAY);
            }
            if (event.getCode() == KeyCode.T) {
                current = WOOD;
                currentMaterial.setFill(Color.SADDLEBROWN);
            }
            if (event.getCode() == KeyCode.D) {
                current = ACID;
                currentMaterial.setFill(Color.LIMEGREEN);
            }
        });

        // dragging/clicking to draw
        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                isPressed = true;
                int x = (int) event.getX();
                int y = (int) event.getY();
                for (int r = -brushSize; r < brushSize; r++) {
                    for (int c = -brushSize; c < brushSize; c++) {
                        if (c + y >= 0 && x + r >= 0
                                && Math.random() < density
                                && c + y <= HEIGHT && x + r <= WIDTH) {
                            board[y + c][x + r] = current;
                        }
                    }
                }
            }
        });
        scene.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                isPressed = true;
                int x = (int) event.getX();
                int y = (int) event.getY();
                for (int r = -brushSize; r < brushSize; r++) {
                    for (int c = -brushSize; c < brushSize; c++) {
                        if (c + y >= 0 && x + r >= 0
                                && Math.random() < density
                                && c + y <= HEIGHT && x + r <= WIDTH) {
                            board[y + c][x + r] = current;
                        }
                    }
                }
            }
        });
        scene.setOnMouseDragReleased(event -> {
            isPressed = false;
        });

        primaryStage.setScene(scene);
        primaryStage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), event -> {
            iterate();
            draw();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void draw() {
        if (isPressed) {
            for (int r = HEIGHT - 1; r >= 0; r--) {
                for (int c = 0; c < WIDTH; c++) {
                    if (board[r][c] == EMPTY) {
                        gc.setFill(Color.BLACK);
                    }
                    else if (board[r][c] == SAND) {
                        gc.setFill(Color.SANDYBROWN);
                    }
                    else if (board[r][c] == ROCK) {
                        gc.setFill(Color.GRAY);
                    }
                    else if (board[r][c] == WATER) {
                        gc.setFill(Color.BLUE);
                    }
                    else if (board[r][c] == LAVA) {
                        gc.setFill(Color.ORANGERED);
                    }
                    else if (board[r][c] == STEAM) {
                        gc.setFill(Color.LIGHTGRAY);
                    }
                    else if (board[r][c] == WOOD) {
                        gc.setFill(Color.SADDLEBROWN);
                    }
                    else if (board[r][c] == ACID){
                        gc.setFill(Color.LIMEGREEN);
                    }
                    else if (board[r][c] == RAIN){
                        gc.setFill(Color.BLUE);
                    }
                    else if (board[r][c] == SNOW){
                        gc.setFill(Color.WHITE);
                    }
                    gc.fillRect(c, r, 1, 1);
                }
            }
        }

    }

    private void iterate() {
        for (int r = HEIGHT - 1; r >= 0; r--) {
            for (int c = 0; c < WIDTH; c++) {
                // rock or empty
                if (board[r][c] == EMPTY || board[r][c] == ROCK) {
                    continue;
                }
                //LAVA
                else if (board[r][c] == LAVA) {
                    double LR = Math.random() * 1;
                    double willHarden = Math.random() * 1;
                    if (r + 1 < HEIGHT && board[r + 1][c] == EMPTY) {
                        board[r + 1][c] = LAVA;
                        board[r][c] = EMPTY;
                    } else if (r + 1 < HEIGHT && c - 1 >= 0 && board[r + 1][c - 1] == EMPTY) {
                        board[r + 1][c - 1] = LAVA;
                        board[r][c] = EMPTY;
                    } else if (r + 1 < HEIGHT && c + 1 < WIDTH && board[r + 1][c + 1] == EMPTY) {
                        board[r + 1][c + 1] = LAVA;
                        board[r][c] = EMPTY;
                    } else if (c - 1 >= 0 && board[r][c - 1] == EMPTY && LR > 0 && LR < 0.5) {
                        board[r][c - 1] = LAVA;
                        board[r][c] = EMPTY;
                    } else if (c + 1 < WIDTH && board[r][c + 1] == EMPTY && LR >= 0.5) {
                        board[r][c + 1] = LAVA;
                        board[r][c] = EMPTY;
                    }
                    // if lava is next to it, stop
                    else if (c - 1 >= 0 && board[r][c - 1] == LAVA) {
                        board[r][c] = LAVA;
                    }
                    else if (c + 1 < WIDTH && board[r][c + 1] == LAVA) {
                        board[r][c] = LAVA;
                    }
                    //rocc
                    else if (r - 1 > 0 && board[r - 1][c] == EMPTY && willHarden < 0.9){
                        board[r][c] = ROCK;
                    }
                    // touching water
                    if (c - 1 >= 0 && board[r][c - 1] == WATER) {
                        board[r][c] = ROCK;
                    }
                    else if (c + 1 < WIDTH && board[r][c + 1] == WATER) {
                        board[r][c] = ROCK;
                    }
                    else if (r - 1 >= 0 && board[r - 1][c] == WATER) {
                        board[r][c] = ROCK;
                    }
                    else if (r + 1 < HEIGHT && board[r + 1][c] == WATER) {
                        board[r][c] = ROCK;
                    }

                }
                //WATER
                else if (board[r][c] == WATER) {
                    double LR = Math.random() * 1;
                    //basic physics
                    if (r + 1 < HEIGHT && board[r + 1][c] == EMPTY) {
                        board[r + 1][c] = WATER;
                        board[r][c] = EMPTY;
                    } else if (r + 1 < HEIGHT && c - 1 >= 0 && board[r + 1][c - 1] == EMPTY) {
                        board[r + 1][c - 1] = WATER;
                        board[r][c] = EMPTY;
                    } else if (r + 1 < HEIGHT && c + 1 < WIDTH && board[r + 1][c + 1] == EMPTY) {
                        board[r + 1][c + 1] = WATER;
                        board[r][c] = EMPTY;
                    } else if (c - 1 >= 0 && board[r][c - 1] == EMPTY && LR > 0 && LR < 0.5) {
                        board[r][c - 1] = WATER;
                        board[r][c] = EMPTY;
                    } else if (c + 1 < WIDTH && board[r][c + 1] == EMPTY && LR >= 0.5) {
                        board[r][c + 1] = WATER;
                        board[r][c] = EMPTY;
                    } else if (c - 1 >= 0 && board[r][c - 1] == WATER) {
                        board[r][c] = WATER;
                    } else if (c + 1 < WIDTH && board[r][c + 1] == WATER) {
                        board[r][c] = WATER;
                    }

                    // if touching lava, rockers
                    if (c - 1 >= 0 && board[r][c - 1] == LAVA) {
                        board[r][c] = ROCK;
                    }
                    else if (c + 1 < WIDTH && board[r][c + 1] == LAVA) {
                        board[r][c] = ROCK;
                    }
                    else if (r - 1 >= 0 && board[r - 1][c] == LAVA) {
                        board[r][c] = ROCK;
                    }
                    else if (r + 1 < HEIGHT && board[r + 1][c] == LAVA) {
                        board[r][c] = ROCK;
                    }

                   /* if (c + 1 < WIDTH && board[r][c + 1] == WATER && board[r][c-1]==WATER && board[r+1][c] == EMPTY){
                        board[r+1][c] = WATER;
                    }*/

                /*    else if (c -1 >= 0 && board[r][c - 1] == EMPTY) {
                        board[r][c + 1]= WATER;
                        // board[r][c-1] = WATER;
                        board[r-1][c] = EMPTY;
                        board[r][c] = EMPTY;
                    }*/

                }
                /* //RAIN
                else if (board[r][c] == RAIN){
                    double LR = Math.random() * 1;
                    if (r + 1 < HEIGHT && c - 1 >= 0 && board[r + 1][c - 1] == EMPTY && LR < 0.5) {
                        board[r + 1][c - 1] = RAIN;
                        board[r][c] = EMPTY;
                    }
                    else if (r + 1 < HEIGHT && c + 1 < WIDTH && board[r + 1][c + 1] == EMPTY && LR >= 0.5) {
                        board[r + 1][c + 1] = RAIN;
                        board[r][c] = EMPTY;}
                    else if (c - 1 >= 0 && board[r][c - 1] == EMPTY && LR > 0 && LR < 0.5) {
                        board[r][c - 1] = RAIN;
                        board[r][c] = EMPTY;
                    } else if (c + 1 < WIDTH && board[r][c + 1] == EMPTY && LR >= 0.5) {
                        board[r][c + 1] = RAIN;
                        board[r][c] = EMPTY;
                    } else if (c - 1 >= 0 && board[r][c - 1] == RAIN) {
                        board[r][c] = RAIN;
                    } else if (c + 1 < WIDTH && board[r][c + 1] == RAIN) {
                        board[r][c] = RAIN;
                    }
                }*/
                //SNOW
                else if (board[r][c] == SNOW){
                    double LR = Math.random() * 1;
                    if (r + 1 < HEIGHT && c - 1 >= 0 && board[r + 1][c - 1] == EMPTY && LR < 0.5) {
                        board[r + 1][c - 1] = SNOW;
                        board[r][c] = EMPTY;
                    }
                    else if (r + 1 < HEIGHT && c + 1 < WIDTH && board[r + 1][c + 1] == EMPTY && LR >= 0.5) {
                        board[r + 1][c + 1] = SNOW;
                        board[r][c] = EMPTY;}
                    else if (c - 1 >= 0 && board[r][c - 1] == EMPTY && LR > 0 && LR < 0.5) {
                        board[r][c - 1] = SNOW;
                        board[r][c] = EMPTY;
                    } else if (c + 1 < WIDTH && board[r][c + 1] == EMPTY && LR >= 0.5) {
                        board[r][c + 1] = SNOW;
                        board[r][c] = EMPTY;
                    } /*else if (c - 1 >= 0 && board[r][c - 1] == SNOW) {
                        board[r][c] = SNOW;
                    } else if (c + 1 < WIDTH && board[r][c + 1] == SNOW) {
                        board[r][c] = SNOW;
                    }*/
                    //TURN into water            aAAAAAAAAAAAAA
                    else if (r + 1 < HEIGHT && c - 1 >= 0 && board[r + 1][c - 1] == WATER){
                        board[r][c] = WATER;
                    }
                    else if (r + 1 < HEIGHT && c + 1 < WIDTH && board[r + 1][c + 1] == WATER) {
                        board[r][c] = WATER;}
                    else if (r - 1 >= 0 && c - 1 >= 0 && board[r-1][c] == WATER) {
                        board[r][c] = WATER;}
                    else if (r + 1 < HEIGHT && c - 1 >= 0 && board[r+1][c] == WATER) {
                        board[r][c] = WATER;}

                    //touch lava TURN INTO WATR
                    else if (r + 1 < HEIGHT && c - 1 >= 0 && board[r + 1][c - 1] == LAVA){
                        board[r][c] = WATER;
                    }
                    else if (r + 1 < HEIGHT && c + 1 < WIDTH && board[r + 1][c + 1] == LAVA) {
                        board[r][c] = WATER;}
                    else if (r + 1 < HEIGHT && c - 1 >= 0 && board[r][c - 1] == LAVA) {
                        board[r][c] = WATER;}
                }
                //SAND
                else if (board[r][c] == SAND) {                        // SAND
                    if (r + 1 < HEIGHT && board[r + 1][c] == EMPTY) {
                        board[r + 1][c] = SAND;
                        board[r][c] = EMPTY;
                    } else if (r + 1 < HEIGHT && c - 1 >= 0 && board[r + 1][c - 1] == EMPTY) {
                        board[r + 1][c - 1] = SAND;
                        board[r][c] = EMPTY;
                    } else if (r + 1 < HEIGHT && c + 1 < WIDTH && board[r + 1][c + 1] == EMPTY) {
                        board[r + 1][c + 1] = SAND;
                        board[r][c] = EMPTY;
                    }
                    double LR = Math.random() * 1;
                    if (r + 1 < HEIGHT && board[r + 1][c] == WATER) {
                        board[r + 1][c] = SAND;
                        board[r][c] = WATER;
                    }
                    //1
                    else if (r + 1 < HEIGHT && c - 1 >= 0 && board[r + 1][c - 1] == WATER && LR >= 0 && LR < 0.2) {
                        board[r + 1][c - 1] = SAND;
                        board[r][c] = WATER;
                    }
                    //2
                    else if (r + 1 < HEIGHT && c + 1 < WIDTH && board[r + 1][c + 1] == WATER && LR >= 0.2 && LR < 0.4) {                     // sand under water needs work
                        board[r + 1][c + 1] = SAND;
                        board[r][c] = WATER;
                    }
                    //3
                    else if (c - 1 >= 0 && board[r][c - 1] == WATER && LR >= 0.4 && LR < 0.75) {
                        board[r][c - 1] = SAND;
                        board[r][c] = WATER;
                    }
                    //4
                    else if (c + 1 < WIDTH && board[r][c + 1] == WATER && LR >= 0.75 && LR <= 1 ) {
                        board[r][c + 1] = SAND;
                        board[r][c] = WATER;
                    }
                }
                //ACID
                else if (board[r][c]==ACID){
                    double LR = Math.random() * 1;
                    //basic physics
                    if (r + 1 < HEIGHT && board[r + 1][c] == EMPTY) {
                        board[r + 1][c] = ACID;
                        board[r][c] = EMPTY;
                    }
                    else if (r + 1 < HEIGHT && c - 1 >= 0 && board[r + 1][c - 1] == EMPTY) {
                        board[r + 1][c - 1] = ACID;
                        board[r][c] = EMPTY;
                    }
                    else if (r + 1 < HEIGHT && c + 1 < WIDTH && board[r + 1][c + 1] == EMPTY) {
                        board[r + 1][c + 1] = ACID;
                        board[r][c] = EMPTY;
                    }
                    else if (c - 1 >= 0 && board[r][c - 1] == EMPTY && LR > 0 && LR < 0.5) {
                        board[r][c - 1] = ACID;
                        board[r][c] = EMPTY;
                    }
                    else if (c + 1 < WIDTH && board[r][c + 1] == EMPTY && LR >= 0.5) {
                        board[r][c + 1] = ACID;
                        board[r][c] = EMPTY;
                    }
                    else if (c - 1 >= 0 && board[r][c - 1] == ACID) {
                        board[r][c] = ACID;
                    }
                    else if (c + 1 < WIDTH && board[r][c + 1] == ACID) {
                        board[r][c] = ACID;
                    }
                    // om nom nom
                    if ((c - 1 >= 0 && board[r][c - 1] != EMPTY) && board[r][c - 1] != ACID){
                        board[r][c-1] = EMPTY;
                    }
                    else if ((c + 1 < WIDTH && board[r][c + 1] != EMPTY) && board[r][c + 1] != ACID) {
                        board[r][c+1] = EMPTY;
                    }
                    else if ((r - 1 >= 0 && board[r - 1][c] != EMPTY) && board[r - 1][c] != ACID) {
                        board[r-1][c] = EMPTY;
                      //  board[r][c] = ACID;
                    }
                    else if ((r + 1 < HEIGHT && board[r + 1][c] != EMPTY) && board[r + 1][c] != ACID) {
                        board[r+1][c] = EMPTY;
                      //  board[r][c] = ACID;
                    }
                    if (r == HEIGHT-1){
                        board[r][c] = EMPTY;
                    }
                }
                //WOOD
                else if (board[r][c] == WOOD) {
                    double LR = Math.random() * 1;
                    double LR2 = Math.random() * 1;
                    double LR3 = Math.random() * 1;
                    if (c - 1 >= 0 && board[r][c - 1] == LAVA && LR < 0.5) {
                        board[r][c] = EMPTY;
                    }
                    else if (c - 1 >= 0 && board[r][c - 1] == LAVA && LR >= 0.5) {
                        continue;
                    }
                    else if (c + 1 < WIDTH && board[r][c + 1] == LAVA && LR2 < 0.5) {
                        board[r][c] = EMPTY;
                    }
                    else if (c + 1 < WIDTH && board[r][c + 1] == LAVA && LR2 >= 0.5) {
                        continue;
                    }
                    else if (r + 1 < HEIGHT && board[r + 1][c] == LAVA && LR3 < 0.25) {
                        board[r][c] = LAVA;
                        board[r+1][c] = EMPTY;
                    }
                    else if (r + 1 < HEIGHT && board[r + 1][c] == LAVA && LR3 >= 0.25 && LR3 < 0.5) {
                        continue;
                    }
                    else if (r - 1 >= 0 && board[r - 1][c] == LAVA && LR3 >= 0.5 && LR3 < 0.75) {
                        board[r][c] = LAVA;
                        board[r-1][c] = EMPTY;
                    }
                    else if (r - 1 >= 0 && board[r - 1][c] == LAVA && LR3 >= 0.75 && LR3 <=1) {
                        continue;
                    }
                }
            }
        }
        // up ^^^^
        for (int r = 0; r < HEIGHT; r++) {
            for (int c = 0; c < WIDTH; c++) {
                 if (board[r][c]==STEAM){
                     double LR = Math.random() * 1;
                     double LR2 = Math.random() * 1;
                    if (r - 1 >= 0 && c - 1 >= 0 && board[r - 1][c - 1] == EMPTY && LR < 0.5) {
                        board[r - 1][c - 1] = STEAM;
                        board[r][c] = EMPTY;
                    }
                    else if (r - 1 >= 0 && c + 1 < WIDTH && board[r - 1][c + 1] == EMPTY && LR >= 0.5) {
                        board[r - 1][c + 1] = STEAM;
                        board[r][c] = EMPTY;
                    }
                    else if (c - 1 >= 0 && board[r][c - 1] == EMPTY && LR > 0 && LR2 < 0.5) {
                        board[r][c - 1] = STEAM;
                        board[r][c] = EMPTY;
                    }
                    else if (c + 1 < WIDTH && board[r][c + 1] == EMPTY && LR2 >= 0.5) {
                        board[r][c + 1] = STEAM;
                        board[r][c] = EMPTY;
                    }
                    else if (c - 1 >= 0 && board[r][c - 1] == STEAM) {
                        board[r][c] = STEAM;
                    }
                    else if (c + 1 < WIDTH && board[r][c + 1] == STEAM) {
                        board[r][c] = STEAM;
                    }

                    // LAVA
                    if (r - 1 >= 0 && board[r + 1][c] == LAVA) {
                        board[r - 1][c] = STEAM;
                        board[r][c] = LAVA;
                    }
                    else if (r - 1 >= 0 && c - 1 >= 0 && board[r - 1][c - 1] == LAVA) {
                        board[r - 1][c - 1] = STEAM;
                        board[r][c] = LAVA;
                    }
                    else if (r - 1 >= 0 && c + 1 < WIDTH && board[r - 1][c + 1] == LAVA) {
                        board[r - 1][c + 1] = STEAM;
                        board[r][c] = LAVA;
                    }
                }
            }
        }
    }
}


/* Materials:
* Sand
* Water
* Rock
* Air/Empty
*
* Lava?
* Steam
* Acid
* wood
* */
