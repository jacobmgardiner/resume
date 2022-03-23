#ifndef PROJECT3_DIJKSTRA_H
#define PROJECT3_DIJKSTRA_H

#include <iostream>
#include "AdjacencyList.h"
#include "Heap.h"
#include "Vertex.h"
#include "PriorityQueue.h"

class Dijkstra {
    AdjacencyList *list;
    PriorityQueue *queue;
    Vertex **vertices;
    Vertex **finalized;

    int adjSize;
    int finalizedSize;

    bool computed = false;

    Dijkstra();
    ~Dijkstra();

    EdgeData **getAdjacent(Vertex *u);

    void dijkstra(int s, int d, bool flag);
}


#endif //PROJECT3_DIJKSTRA_H
