#ifndef PROJECT3_ADJACENCYLIST_H
#define PROJECT3_ADJACENCYLIST_H

#include <malloc.h>
#include "LinkedList.h"
#include "EdgeData.h"
#include "Vertex.h"

class AdjacencyList {
    public:
//    struct adjacentData {
//        EdgeData** data;
//        int size;
//    };

        int numVertices;
        int numEdges;
        LinkedList<EdgeData>* vertices;
        bool directed;

    AdjacencyList(int n, int m, bool isDirected);

    void addEdge(const float* d) const;

    int getAdjacent(const EdgeData** data, int id) const;

    int getAdjacentSize(int id) const;
};


#endif //PROJECT3_ADJACENCYLIST_H
