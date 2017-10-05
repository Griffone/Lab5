/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lab5;

import java.util.LinkedList;
import java.util.Queue;
import se.kth.id1020.Graph;
import se.kth.id1020.DataSource;
import se.kth.id1020.Edge;
import se.kth.id1020.Vertex;

/**
 * 
 * @author Griffone
 */
public class Paths {
    
    public static void main(String[] args) {
        Graph g = DataSource.load();
        
        // Debug info
        System.out.println("The supplied graph has:");
        System.out.println(g.numberOfVertices() + " verticies");
        System.out.println(g.numberOfEdges() + " edges");
        System.out.println();
        
        // Subgraphs
        System.out.println("There are " + findSubgraphs(g) + " subgraphs");
        
        // Find 2 verticies to path between:
        int a = 0, b = 0;
        for (Vertex v : g.vertices())
            if (v.label.compareTo("Renyn") == 0)
                a = v.id;
            else if (v.label.compareTo("Parses") == 0)
                b = v.id;
        
        // Path.bPrintFullPath = true;
        
        // Output the provided paths
        System.out.println("Paths from Renyn to Parses:");
        System.out.println(Path.findUnweightedPath(g, a, b));
        System.out.println(Path.findWeightedPath(g, a, b));
    }

    public static boolean isConnected(Graph g) {
        return findSubgraphs(g) == 1;
    }

    public static int findSubgraphs(Graph g) {
        boolean marked[] = new boolean[g.numberOfVertices()];
        int subgraph_count = 0;
        Queue<Integer> vtx_q = new LinkedList();
        for (int i = 0; i < marked.length; i++) {
            if (!marked[i]) {
                subgraph_count++;
                vtx_q.add(i);
                marked[i] = true;
                while (!vtx_q.isEmpty()) {
                    Iterable<Edge> edges = g.adj(vtx_q.remove());
                    edges.forEach(edge -> {
                        if (!marked[edge.to]) {
                            marked[edge.to] = true;
                            vtx_q.add(edge.to);
                        }
                    });
                }
            }
        }
        return subgraph_count;
    }
}