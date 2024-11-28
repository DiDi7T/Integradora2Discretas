package org.icesi.gifbackground.structures;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyListGraph<T> implements IGraph<T> {

    private final List<Vertex<T>> vertices;
    private final List<List<Edge<T>>> edges;

    public AdjacencyListGraph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    @Override
    public void addNode(T node) {
        if (getNode(node) == null) {
            vertices.add(new Vertex<>(node));
            edges.add(new ArrayList<>());
        }
    }
    @Override
    public void addEdge(T from, T to, int weight) {
        Vertex<T> fromVertex = getNode(from);
        Vertex<T> toVertex = getNode(to);



        if (!hasEdge(from, to)) {
            int fromIndex = vertices.indexOf(fromVertex);
            edges.get(fromIndex).add(new Edge<>(fromVertex, toVertex, 0)); // Peso 0 porque no necesitamos peso en 'lake'
        }
    }

    @Override
    public ArrayList<T> getNeighbors(T node) {
        ArrayList<T> neighbors = new ArrayList<>();
        Vertex<T> vertex = getNode(node);

        if (vertex != null) {
            int index = vertices.indexOf(vertex);

            List<Edge<T>> edgeList = edges.get(index);
            edgeList.sort((edge1, edge2) -> edge1.getTo().getData().toString().compareTo(edge2.getTo().getData().toString()));

            for (Edge<T> edge : edgeList) {
                neighbors.add(edge.getTo().getData());
            }
        }
        return neighbors;
    }


    @Override
    public int getEdgeWeight(T from, T to) {

        return 0;
    }

    @Override
    public boolean hasEdge(T from, T to) {
        return getEdge(from, to) != null;
    }

    @Override
    public ArrayList<T> getNodes() {
        ArrayList<T> nodeData = new ArrayList<>();
        for (Vertex<T> vertex : vertices) {
            nodeData.add(vertex.getData());
        }
        return nodeData;
    }

    @Override
    public Edge<T> getEdge(T from, T to) {
        Vertex<T> fromVertex = getNode(from);

        if (fromVertex != null) {
            int fromIndex = vertices.indexOf(fromVertex);
            for (Edge<T> edge : edges.get(fromIndex)) {
                if (edge.getTo().getData().equals(to)) {
                    return edge;
                }
            }
        }
        return null;
    }

    private Vertex<T> getNode(T node) {
        for (Vertex<T> vertex : vertices) {
            if (vertex.getData().equals(node)) {
                return vertex;
            }
        }
        return null;
    }
}
