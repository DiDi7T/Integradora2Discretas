package org.icesi.gifbackground.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Fish {
    private Pane graphPane;
    private String position;
    private ImageView fishGraphic;

    public Fish(String position, Pane graphPane) {
        this.position = position;
        this.graphPane = graphPane;


        fishGraphic = new ImageView(new Image(getClass().getResourceAsStream("/assets/fish.png")));
        fishGraphic.setFitWidth(50);
        fishGraphic.setFitHeight(50);


        graphPane.getChildren().add(fishGraphic);

        //updateFishGraphicPosition();
    }

    public String getPosition() {
        return position;
    }

//    public void setPosition(String position) {
//        this.position = position;
//        updateFishGraphicPosition();
//    }
//
//
//    public void move(IGraph<String> graph) {
//        String newPosition = graph.getRandomNeighbor(position);
//        if (newPosition != null) {
//            position = newPosition;
//            updateFishGraphicPosition();
//        }
//    }
//
//
//    private void updateFishGraphicPosition() {
//        double[] coordinates = GraphRenderer.getNodeCoordinates(position);  /
//        fishGraphic.setX(coordinates[0]);
//        fishGraphic.setY(coordinates[1]);
//    }
//
//
//    public ImageView getFishGraphic() {
//        return fishGraphic;
//    }
}
