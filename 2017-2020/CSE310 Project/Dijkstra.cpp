#include <cmath>
#include "Dijkstra.h"

Dijkstra::Dijkstra() {
    computed = true;
    queue = new PriorityQueue(list->numVertices, false);
    auto* src = new Vertex(s, 0, -1, true);
    queue->insert(src);
    if (flag)
        printf("Insert vertex %d, key=%12.4f\n", src->id + 1, src->distance);
    vertices = new Vertex*[list->numVertices];
    for (int i = 0; i < list->numVertices; i++) {
        vertices[i] = new Vertex(i);
    }
    vertices[s] = src;
    finalized = new Vertex*[list->numVertices];
}

Dijkstra::~Dijkstra() {
    if (vertices)
        for (int i = 0; i < list->numVertices; i++)
            if (vertices[i])
                delete vertices[i];
    delete [] vertices;
    delete [] finalized;
    delete queue;
}

EdgeData** Dijkstra::getAdjacent(Vertex* u) {
    auto** data = (new EdgeData*[list->numVertices]);

    LinkedList<EdgeData> l = list->vertices[u->id];
    int i = 0;
    l.resetIterator();
    while (l.hasNext()) {
        EdgeData* next = l.next()->data;
        if (!vertices[next->vertex]->finalized)
            data[i++] = next;
    }
    adjSize = i;
    return data;
}

void Dijkstra::dijkstra(int s, int d, bool flag) {
//    if (vertices)
//        for (int i = 0; i < list->numVertices; i++)
//            if (vertices[i])
//                //vertices[i] = new Vertex(i);
//                delete vertices[i];
//    //vertices = new Vertex*[list->numVertices];
//    delete [] vertices;
//    delete [] finalized;
//    delete queue;

//    computed = true;
//    queue = new PriorityQueue(list->numVertices, false);
//    auto* src = new Vertex(s, 0, -1, true);
//    queue->insert(src);
//    if (flag)
//        printf("Insert vertex %d, key=%12.4f\n", src->id + 1, src->distance);
//    vertices = new Vertex*[list->numVertices];
//    for (int i = 0; i < list->numVertices; i++) {
//        vertices[i] = new Vertex(i);
//    }
//    vertices[s] = src;
//    finalized = new Vertex*[list->numVertices];
    for (int i = 0; i < list->numVertices - 1; i++) {
        if (queue->heap->heapSize == 0) {
            return;
        }
        auto* u = dynamic_cast<Vertex*>(queue->extractHighestPriority());
        if (flag)
            printf("Delete vertex %d, key=%12.4f\n", u->id + 1, u->distance);
        finalized[i] = u;
        u->finalized = true;

        finalizedSize++;
        if (u->id == d) {
            return;
        }
        EdgeData** data = getAdjacent(u);
        for (int j = 0; j < adjSize; j++) {
            auto* v = vertices[data[j]->vertex];
            if (v->distance > u->distance + data[j]->weight) {
                float old = v->distance;
                v->distance = u->distance + data[j]->weight;
                v->predecessor = u->id;
                if (flag) {
                    if (old == INFINITY) {
                        printf("Insert vertex %d, key=%12.4f\n", v->id + 1, v->distance);
                    } else {
                        printf("Decrease key of vertex %d, from %12.4f to %12.4f\n",
                               v->id + 1,
                               old,
                               v->distance);
                    }
                }
                queue->replace(v);
            }
        }
        delete [] (data);
    }
    if (flag)
        printf("Delete vertex %d, key=%12.4f\n",
               dynamic_cast<Vertex*>(queue->heap->heapArray[0])->id + 1,
               dynamic_cast<Vertex*>(queue->heap->heapArray[0])->distance);
}
