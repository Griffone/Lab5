/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lab5;

import java.util.*;
import se.kth.id1020.Edge;
import se.kth.id1020.Graph;

/**
 *  Representation of a path from a to b
 * 
 * @author Griffone
 */
public class Path {

    public static boolean bPrintFullPath = false;

    public final Iterable<Integer> path;
    public final int size;
    public final boolean exists;
    public final double weightLength;
    public final int a, b;
    
    private Path(int a, int b, List<Integer> path, boolean exists, double weight) {
        this.path = path;
        this.exists = exists;
        this.size = (exists) ? path.size() : 0;
        this.weightLength = weight;
        this.a = a;
        this.b = b;
    }
    
    /**
     * Finds the shortest possible path between two vertices in a graph
     * 
     * @param g the graph
     * @param a the id of one vertex
     * @param b the id of the other vertex
     * @param weighted should edge weight be taken into consideration
     * @return the path 
     */
    public static Path findPath(Graph g, int a, int b, boolean weighted) {
        return weighted ? findWeightedPath(g, a, b) : findUnweightedPath(g, a, b);
    }
    
    /**
     * Finds the shortest possible path between two vertices in a graph
     * without taking edge weights into consideration
     * 
     * @param g the graph
     * @param a the id of one vertex
     * @param b the id of the other vertex
     * @return the path 
     */
    public static Path findUnweightedPath(Graph g, int a, int b) {
        if (a >= g.numberOfVertices() || b >= g.numberOfVertices())
            throw new IndexOutOfBoundsException("Vertex id is out of bounds!");
        boolean marked[] = new boolean[g.numberOfVertices()];
        int pathTo[] = new int[g.numberOfVertices()];
        marked[a] = true;
        pathTo[a] = -1;
        Queue<Integer> q = new LinkedList();
        q.add(a);
        boolean found = false;
        while (!q.isEmpty() && !found) {
            int vtx = q.remove();
            Iterable<Edge> edges = g.adj(vtx);
            for (Edge edge : edges) {
                if (!marked[edge.to]) {
                    pathTo[edge.to] = vtx;
                    marked[edge.to] = true;
                    if (edge.to == b) {
                        found = true;
                        break;
                    }
                    q.add(edge.to);
                }
            }
        }
        if (!found)
            return new Path(a, b, null, false, 0.0);
        LinkedList<Integer> path = new LinkedList();
        int vtx = b;
        double len = 0.0;
        while (vtx != -1) {
            Iterable<Edge> edges = g.adj(vtx);
            for (Edge edge : edges)
                if (edge.to == pathTo[vtx])
                    len += edge.weight;
            path.addFirst(vtx);
            vtx = pathTo[vtx];
        }
        return new Path(a, b, path, true, len); 
    }
    
    /**
     * Finds the shortest possible path between two vertices in a graph
     * considering the edge weights
     * 
     * @param g the graph
     * @param a the id of one vertex
     * @param b the id of the other vertex
     * @return the path 
     */
    public static Path findWeightedPath(Graph g, int a, int b) {
        if (a >= g.numberOfVertices() || b >= g.numberOfVertices())
            throw new IndexOutOfBoundsException("Vertex id is out of bounds!");
        boolean marked[] = new boolean[g.numberOfVertices()];   // is [x] accesable from a
        int pathTo[] = new int[g.numberOfVertices()];           // previous edge on the shortest path
        double distTo[] = new double[g.numberOfVertices()];     // distance from [x] to a
        distTo[a] = 0.0;
        marked[a] = true;
        pathTo[a] = -1;
        Queue<Integer> q = new LinkedList();
        q.add(a);
        boolean found = false;
        while (!q.isEmpty()) {
            int vtx = q.remove();
            Iterable<Edge> edges = g.adj(vtx);
            for (Edge edge : edges) {
                if (marked[edge.to]) {
                    // Check if the path incoming from edge.to is shorter
                        if (distTo[edge.to] + edge.weight < distTo[vtx]) {
                        // update if it is
                        distTo[vtx] = distTo[edge.to] + edge.weight;
                        pathTo[vtx] = edge.to;
                        q.add(vtx);
                    // check if the path incoming from vtx is shorter
                    } else if (distTo[edge.to] > edge.weight + distTo[vtx]) {
                        distTo[edge.to] = distTo[vtx] + edge.weight;
                        pathTo[edge.to] = vtx;
                        q.add(edge.to);
                    }
                } else {    // mark the vertex
                    pathTo[edge.to] = vtx;
                    marked[edge.to] = true;
                    distTo[edge.to] = edge.weight + distTo[vtx];
                    if (edge.to == b)
                        found = true;
                    q.add(edge.to);
                }   
            }
        }
        if (!found)
            return new Path(a, b, null, false, 0.0);
        LinkedList<Integer> path = new LinkedList();
        int vtx = b;
        while (vtx != -1) {
            path.addFirst(vtx);
            vtx = pathTo[vtx];
        }
        return new Path(a, b, path, true, distTo[b]); 
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (exists) {
            Iterator<Integer> it = path.iterator();
            sb.append('(').append(a).append(" : ").append(b).append(") ");
            sb.append('[').append(size).append(" : ").append(weightLength).append("] ");
            if (bPrintFullPath) {
                sb.append(it.next());
                while (it.hasNext())
                    sb.append("->").append(it.next());
            }
        } else
            sb.append("Path from ").append(a).append(" to ").append(b).append(" does not exist");
        
        return sb.toString();
    }
}