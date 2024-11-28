package org.icesi.gifbackground.model;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.icesi.gifbackground.structures.AdjacencyListGraph;

import java.net.URL;
import java.util.*;
public class Game1Controller implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Button backButton;


    @FXML
    private Pane graphContainer;


    @FXML
    private Canvas graphCanvas;

    @FXML
    private GraphicsContext gc;


    private AdjacencyListGraph<Integer> graph;
    private Map<Integer, Point2D> nodePositions;


    private Player player;
    private Fish fish;


    private Image hookImage;
    private Image fishImage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            Image image = new Image(getClass().getResource("/animations/background/back2.png").toExternalForm());
            backgroundImage.setImage(image);
            backgroundImage.setFitWidth(800);
            backgroundImage.setFitHeight(780);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hookImage = new Image(getClass().getResource("/assets/hook.png").toExternalForm());
        fishImage = new Image(getClass().getResource("/assets/fish.png").toExternalForm());  // Agregar esta línea

        addBackButtonImage();
        createGraph();
        player = new Player("1", 0);

        // Aquí generamos un nodo aleatorio que no esté en la lista de nodos en los que no puede aparecer el pecesito,
        String randomPosition = generateRandomNode();

        fish = new Fish(randomPosition, graphContainer);  // Creamos el objeto Fish con la posición aleatoria


        gc = graphCanvas.getGraphicsContext2D();

        if (graphCanvas != null) {
            gc = graphCanvas.getGraphicsContext2D();
        } else {
            System.err.println("El Canvas no se ha inicializado correctamente.");
        }

        Platform.runLater(() -> {
            if (graphContainer.getWidth() > 0 && graphContainer.getHeight() > 0) {
                drawGraph();
            } else {
                System.out.println(".");
            }
        });
    }


    public String generateRandomNode() {
        List<String> prohibitedNodes = Arrays.asList("1", "2", "3", "4", "5", "9", "10", "11", "12", "13"); //en estos nodos no puede generarse el pez ya que estan muy cercanos al nodo en el que empieza el jugador
        List<String> availableNodes = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            String node = String.valueOf(i);
            if (!prohibitedNodes.contains(node)) {
                availableNodes.add(node);
            }
        }

        // Seleccionar un nodo aleatorio
        Random rand = new Random();
        return availableNodes.get(rand.nextInt(availableNodes.size()));
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
            stage.setX(480);
            stage.setY(180);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onHintButtonClick() {
        // obtener la posición actual del hook y la del pez
        int hookNode = Integer.parseInt(player.getPosition());
        int fishNode = Integer.parseInt(fish.getPosition());

        // Llamar al método BFS para calcular la ruta más corta entre el hook y el pez
        List<Integer> path = bfs(hookNode, fishNode);

        // Resaltar la ruta en el grafo de otro color
        highlightPath(path);


    }

    private void createGraph() {
        graph = new AdjacencyListGraph<>();

        // Agregar nodos al grafo
        for (int i = 1; i <= 50; i++) {
            graph.addNode(i);
        }

        // Crear conexiones
        for (int i = 1; i < 50; i++) {
            graph.addEdge(i, i + 1, 0);
            graph.addEdge(i + 1, i, 0);
        }

        Random rand = new Random();
        for (int i = 1; i <= 10; i++) {
            int node1 = rand.nextInt(50) + 1;
            int node2 = rand.nextInt(50) + 1;
            if (node1 != node2) {
                graph.addEdge(node1, node2, 0);
                graph.addEdge(node2, node1, 0);
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
        ArrayList<Integer> nodes = new ArrayList<>(graph.getNodes());

        nodePositions = calculateNodePositions(nodes, canvas.getWidth(), canvas.getHeight());
        Set<Integer> pathNodes = new HashSet<>();// Llenar pathNodes con los nodos de la ruta que deseas resaltar


//

        drawEdges(gc, nodePositions, pathNodes);
        drawNodes(gc, nodePositions, pathNodes);
        drawHook(gc);
        handleMouseClick(canvas);
        drawFish(gc);
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

    private void drawNodes(GraphicsContext gc, Map<Integer, Point2D> nodePositions, Set<Integer> pathNodes) {
        for (Map.Entry<Integer, Point2D> entry : nodePositions.entrySet()) {
            int nodeId = entry.getKey();
            Point2D position = entry.getValue();

            if (pathNodes.contains(nodeId)) {
                // Resaltar el nodo del camino de la pista de rojo
                gc.setFill(Color.RED);
            } else {
                gc.setFill(Color.rgb(70, 130, 180));  // Color por defecto de los nodos
            }

            gc.fillOval(position.getX() - 15, position.getY() - 15, 30, 30);
            gc.setFill(Color.WHITE);
            gc.fillText(String.valueOf(nodeId), position.getX() - 5, position.getY() + 5);
        }
    }

    private void drawEdges(GraphicsContext gc, Map<Integer, Point2D> nodePositions, Set<Integer> pathNodes) {
        if (pathNodes == null) {
            pathNodes = new HashSet<>();
        }

        for (int node1 : graph.getNodes()) {
            Point2D pos1 = nodePositions.get(node1);
            for (int node2 : graph.getNeighbors(node1)) {
                Point2D pos2 = nodePositions.get(node2);

                // Dibujar las aristas por defecto
                gc.setStroke(Color.GRAY);
                gc.strokeLine(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());

                // Resaltar las aristas de la ruta
                if (pathNodes.contains(node1) && pathNodes.contains(node2)) {
                    gc.setStroke(Color.RED);  // Resaltar las aristas de la ruta en rojo
                    gc.strokeLine(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
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

        // Verificar si la posición del hook es la misma que la del pez
        if (currentNode == Integer.parseInt(fish.getPosition())) {
            winnn();
        }
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

        List<Integer> neighbors = graph.getNeighbors(currentNode);

        if (neighbors.contains(targetNode)) {
            player.move(String.valueOf(targetNode));
            System.out.println("Jugador movido al nodo " + targetNode);

            // Redibujar el grafo con la nueva posición del jugador
            drawGraph();
        } else {
            System.out.println("No hay una conexión válida entre los nodos.");
        }
    }

    private void drawFish(GraphicsContext gc) {
        // Obtener la posicision del pez
        String randomPosition = generateRandomNode();
        int nodeId = Integer.parseInt(randomPosition);
        Point2D position = nodePositions.get(nodeId);

        if (position != null) {
            double imageWidth = 50;
            double imageHeight = 50;

            gc.drawImage(fishImage, position.getX() - imageWidth / 2, position.getY() - imageHeight / 2, imageWidth, imageHeight);
        }
    }

    private void winnn() {
        // Crear la alerta
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡You winnnnnnnnnnnnnnnn!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! You caught the fish");

        // Mostrar la alerta
        alert.showAndWait();
    }

    private void highlightPath(List<Integer> path) {
        Set<Integer> pathNodes = new HashSet<>(path);

        // Dibujar nodos
        drawNodes(gc, nodePositions, pathNodes);

        // Dibujar las aristas de la ruta
        drawEdges(gc, nodePositions, pathNodes);  // Pasar pathNodes a drawEdges
    }



    //BFS

    public List<Integer> bfs(int startNode, int targetNode) {

        List<Integer> path = new ArrayList<>();

        // Usaremos un mapa para almacenar el nodo previo en la ruta más corta
        Map<Integer, Integer> previousNodes = new HashMap<>();

        // Cola para la BFS
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startNode);

        Set<Integer> visited = new HashSet<>();
        visited.add(startNode);

        boolean found = false;

        while (!queue.isEmpty() && !found) {
            int currentNode = queue.poll();

            if (currentNode == targetNode) {
                found = true;
                break;
            }

            for (int neighbor : graph.getNeighbors(currentNode)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    previousNodes.put(neighbor, currentNode);
                }
            }
        }

        if (found) {
            int current = targetNode;
            while (current != startNode) {
                path.add(current);
                current = previousNodes.get(current);
            }
            path.add(startNode);
            Collections.reverse(path); }

        return path;
    }


}