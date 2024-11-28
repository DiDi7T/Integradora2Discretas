package org.icesi.gifbackground.structures;

import java.util.ArrayList;

public class AdjacencyMatrixGraph<T> implements IGraph<T> {
    private final ArrayList<Vertex<T>> nodes;
    private final Edge<T>[][] edges;
    private final int size;

    @SuppressWarnings("unchecked")
    public AdjacencyMatrixGraph(int size) {
        this.size = size;
        nodes = new ArrayList<>();
        edges = new Edge[size][size];
    }

    @Override
    public void addNode(T node) {
        if (getNode(node) == null) {
            nodes.add(new Vertex<>(node));
        }
    }

    @Override
    public void addEdge(T from, T to, int weight) {
        int fromIndex = getNodeIndex(from);
        int toIndex = getNodeIndex(to);

        if (fromIndex == -1 || toIndex == -1) {
            throw new IllegalArgumentException("Both nodes must exist in the graph.");
        }
        if (weight > 20) {
            weight = 20;
        }


        Edge<T> edge = new Edge<>(nodes.get(fromIndex), nodes.get(toIndex), weight);
        edges[fromIndex][toIndex] = edge;
        //edges[toIndex][fromIndex] = edge;
    }

    @Override
    public ArrayList<T> getNeighbors(T node) {
        ArrayList<T> neighbors = new ArrayList<>();
        int index = getNodeIndex(node);

        if (index == -1) return neighbors;

        for (int i = 0; i < size; i++) {
            if (edges[index][i] != null) {
                neighbors.add(nodes.get(i).getData());
            }
        }
        return neighbors;
    }

    @Override
    public int getEdgeWeight(T from, T to) {
        Edge<T> edge = getEdge(from, to);

        return edge != null ? edge.getWeight() : 0;
    }

    @Override
    public boolean hasEdge(T from, T to) {
        return getEdge(from, to) != null;
    }

    public ArrayList<T> getNodes() {
        ArrayList<T> nodeData = new ArrayList<>();
        for (Vertex<T> node : nodes) {
            nodeData.add(node.getData());
        }
        return nodeData;
    }

    @Override
    public Edge<T> getEdge(T from, T to) {
        int fromIndex = getNodeIndex(from);
        int toIndex = getNodeIndex(to);

        if (fromIndex == -1 || toIndex == -1) {
            return null;
        }

        return edges[fromIndex][toIndex];
    }

    private int getNodeIndex(T node) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getData().equals(node)) {
                return i;
            }
        }
        return -1;
    }

    public Vertex<T> getNode(T nodeData) {
        for (Vertex<T> vertex : nodes) {
            if (vertex.getData().equals(nodeData)) {
                return vertex;
            }
        }
        return null;
    }
}
