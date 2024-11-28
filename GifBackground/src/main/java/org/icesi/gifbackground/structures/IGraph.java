package org.icesi.gifbackground.structures;

import java.util.ArrayList;

public interface IGraph<T> {

    void addNode(T node);

    void addEdge(T from, T to, int weight);

    ArrayList<T> getNeighbors(T node);

    int getEdgeWeight(T from, T to);

    boolean hasEdge(T from, T to);

    ArrayList<T> getNodes();

    Edge<T> getEdge(T from, T to);
}
