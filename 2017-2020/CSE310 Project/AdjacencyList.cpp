#include <iostream>
#include "AdjacencyList.h"
#include "EdgeData.h"

AdjacencyList::AdjacencyList(int n, int m, bool isDirected) {
    numVertices = n;
    numEdges = m;
    directed = isDirected;
    vertices = (LinkedList<EdgeData>*)(malloc(sizeof(LinkedList<EdgeData>) * n));
    for (int i = 0; i < n; i++) {
        vertices[i] = LinkedList<EdgeData>();
    }
}

void AdjacencyList::addEdge(const float *d) const {
    auto* edge = new EdgeData((int)d[0], (int)d[2], d[3]);
    vertices[(int)d[1]].insertHead(edge);
    if (!directed) {
        auto* edge2 = new EdgeData((int)d[0], (int)d[1], d[3]);
        vertices[(int)d[2]].insertHead(edge2);
    }
}

int AdjacencyList::getAdjacent(const EdgeData** data, int id) const {
//    auto** data = new EdgeData*[numVertices];
    LinkedList<EdgeData> list = vertices[id];
    int i = 0;
    list.resetIterator();
    while (list.hasNext()) {
        data[i++] = (list.next()->data);
    }
    return i;
}

int AdjacencyList::getAdjacentSize(int id) const {
    LinkedList<EdgeData> list = vertices[id];
    int i = 0;
    list.resetIterator();
    while (list.hasNext()) {
        i++;
    }
    return i;
}