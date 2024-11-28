package org.icesi.gifbackground.model;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.icesi.gifbackground.structures.AdjacencyMatrixGraph;
import org.icesi.gifbackground.structures.Edge;
import org.icesi.gifbackground.structures.Vertex;

import java.net.URL;
import java.util.*;

public class Game2Controller implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Button backButton;

    @FXML
    private Pane graphContainer;

    private AdjacencyMatrixGraph<Integer> graph;
    private Player player;
    private Map<Integer, Point2D> nodePositions;

    private Image hookImage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image image = new Image(getClass().getResource("/animations/background/back3.png").toExternalForm());
            backgroundImage.setImage(image);
            backgroundImage.setFitWidth(800);
            backgroundImage.setFitHeight(780);

        } catch (Exception e) {
            e.printStackTrace();
        }
        hookImage = new Image(getClass().getResource("/assets/hook.png").toExternalForm());

        addBackButtonImage();
        createGraph();
        player = new Player("1", 100);

        Platform.runLater(() -> {
            if (graphContainer.getWidth() > 0 && graphContainer.getHeight() > 0) {

                drawGraph();
            } else {
                System.out.println("El tamaño del Pane aún no es válido.");
            }
        });
    }

    private void addBackButtonImage() {
        ImageView backButtonImageView = new ImageView(new Image(getClass().getResourceAsStream("/assets/buttonx.png")));

        backButtonImageView.setFitWidth(50);
        backButtonImageView.setFitHeight(50);
        backButton.setGraphic(backButtonImageView);
        backButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    }

    @FXML
    public void onBackButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/icesi/gifbackground/hello-view.fxml"));

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setX(480);
            stage.setY(180);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createGraph() {

        graph = new AdjacencyMatrixGraph<>(50);


        for (int i = 1; i <= 50; i++) {
            graph.addNode(i);
        }


        for (int i = 1; i <= 50; i++) {
            for (int j = i + 1; j <= 50; j++) {
                int weight = (int) (Math.random() * 10 + 1);
                graph.addEdge(i, j, weight);
                graph.addEdge(j, i, weight);
            }
        }
    }

    private void drawGraph() {
        if (graphContainer.getWidth() <= 0 || graphContainer.getHeight() <= 0) {
            System.out.println("Error: El tamaño del Pane no es válido.");
            return;
        }

        Canvas canvas = new Canvas(graphContainer.getWidth(), graphContainer.getHeight());
        graphContainer.getChildren().clear();
        graphContainer.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        ArrayList<Integer> nodes = graph.getNodes();
        nodePositions = calculateNodePositions(nodes, canvas.getWidth(), canvas.getHeight());

        drawEdges(gc, nodePositions);
        drawNodes(gc, nodePositions);

        drawHook(gc);
        handleMouseClick(canvas);
        drawEnergyBar(gc);
    }

    private Map<Integer, Point2D> calculateNodePositions(List<Integer> nodes, double width, double height) {
        Map<Integer, Point2D> positions = new HashMap<>();
        int rows = (int) Math.sqrt(nodes.size());
        int cols = (int) Math.ceil((double) nodes.size() / rows);
        double margin = 20;
        double nodeSpacingX = (width - 3 * margin) / (cols - 1);
        double nodeSpacingY = (height - 3 * margin) / (rows - 1);

        for (int i = 0; i < nodes.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            double x = margin + col * nodeSpacingX;
            double y = margin + row * nodeSpacingY;
            positions.put(nodes.get(i), new Point2D(x, y));
        }
        return positions;
    }


    private void drawNodes(GraphicsContext gc, Map<Integer, Point2D> nodePositions) {
        for (Map.Entry<Integer, Point2D> entry : nodePositions.entrySet()) {
            int nodeId = entry.getKey();
            Point2D position = entry.getValue();

            gc.setFill(Color.rgb(235,130,50));
            gc.fillOval(position.getX() - 15, position.getY() - 15, 30, 30);

            gc.setFill(Color.WHITE);
            gc.fillText(String.valueOf(nodeId), position.getX() - 5, position.getY() + 5);
        }
    }


    private void drawEdges(GraphicsContext gc, Map<Integer, Point2D> nodePositions) {
        double maxDistance = 150;

        for (Map.Entry<Integer, Point2D> entry1 : nodePositions.entrySet()) {
            for (Map.Entry<Integer, Point2D> entry2 : nodePositions.entrySet()) {
                if (!entry1.getKey().equals(entry2.getKey())) {
                    int node1 = entry1.getKey();
                    int node2 = entry2.getKey();

                    int weight = graph.getEdgeWeight(node1, node2);

                    double distance = entry1.getValue().distance(entry2.getValue());
                    if (distance <= maxDistance && weight > 0) {
                        gc.setStroke(Color.BLACK);
                        gc.setLineWidth(1);
                        gc.strokeLine(entry1.getValue().getX(), entry1.getValue().getY(),
                                entry2.getValue().getX(), entry2.getValue().getY());

                        double midX = (entry1.getValue().getX() + entry2.getValue().getX()) / 2;
                        double midY = (entry1.getValue().getY() + entry2.getValue().getY()) / 2;

                        gc.setFill(Color.WHITE);
                        gc.fillText(String.valueOf(weight), midX, midY);
                    }
                }
            }
        }
    }
    private void drawHook(GraphicsContext gc) {
        int currentNode = Integer.parseInt(player.getPosition());
        Point2D position = nodePositions.get(currentNode);

        double imageWidth = 40;
        double imageHeight = 40;

        gc.drawImage(hookImage, position.getX() - imageWidth / 2, position.getY() - imageHeight / 2, imageWidth, imageHeight);
    }
    private void handleMouseClick(Canvas canvas) {
        canvas.setOnMouseClicked(event -> {
            Point2D clickPosition = new Point2D(event.getX(), event.getY());

            for (Map.Entry<Integer, Point2D> entry : nodePositions.entrySet()) {
                double distance = clickPosition.distance(entry.getValue());
                if (distance <= 15) {
                    movePlayer(entry.getKey());
                    break;
                }
            }
        });
    }
    private void movePlayer(int targetNode) {
        int currentNode = Integer.parseInt(player.getPosition());

        int edgeWeight = graph.getEdgeWeight(currentNode, targetNode);

        if (edgeWeight > 0) {
            if (player.getEnergy() >= edgeWeight) {
                player.move(String.valueOf(targetNode));
                player.decreaseEnergy(edgeWeight);

                System.out.println("Jugador movido al nodo " + targetNode);
                System.out.println("Energía restante: " + player.getEnergy());

                drawGraph();
            } else {
                System.out.println("No tienes suficiente energía para moverte a este nodo.");
            }
        } else {
            System.out.println("No hay una conexión entre los nodos.");
        }
    }

    private void drawEnergyBar(GraphicsContext gc) {
        double energyBarWidth = 200;
        double energyBarHeight = 20;

        gc.setFill(Color.GRAY);
        gc.fillRect(10, 10, energyBarWidth, energyBarHeight);


        double currentEnergyWidth = (energyBarWidth * player.getEnergy()) / 100.0;
        gc.setFill(Color.GREEN);
        gc.fillRect(10, 10, currentEnergyWidth, energyBarHeight);
    }
}
