package org.icesi.gifbackground.structures;

import javafx.scene.paint.Color;

public class Vertex<T> {

    private T data;

    private int x;
    private int y;

    private Color color;

    private Vertex<T> pi; //predecessor of the vertex



    public Vertex() {
        this.data = null;
        this.color = Color.WHITE;
        this.pi = null;

    }

    public Vertex(T data) {
        this.data = data;
        this.color = Color.WHITE;
        this.pi = null;

    }

    public Vertex(T data, int x, int y) {
        this.data = data;
        this.color = Color.WHITE;
        this.pi = null;

        this.x = x;
        this.y = y;
    }

    public T getData() {
        return data;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vertex<T> getPi() {
        return pi;
    }

    public void setPi(Vertex<T> pi) {
        this.pi = pi;
    }



    @Override
    public String toString(){
        return "Vertex value: "+this.data;
    }


}
