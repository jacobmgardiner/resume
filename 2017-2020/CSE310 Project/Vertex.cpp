#include <string>
#include <iostream>
#include <cmath>
#include "Vertex.h"

//Vertex::~Vertex() {
//    delete this;
//}

Vertex::Vertex(int id, float distance, int predecessor, bool finalized) {
    this->id = id;
    this->distance = distance;
    this->predecessor = predecessor;
    this->finalized = finalized;
    this->heapIdx = -1;
}

Vertex::Vertex(int id) {
    this->id = id;
    distance = INFINITY;
    predecessor = -1;
    this->finalized = false;
    this->heapIdx = -1;
}

int Vertex::compareWith(HeapNode* n) {
    auto* v = dynamic_cast<Vertex*>(n);
    if (!v) { return 0; }
//    cout << this->toString() << ":" << v->toString() << ": " << distance - v->distance << endl;
    return distance - v->distance;
}

string Vertex::toString() {
    return to_string(id) + " " + to_string(distance) + " " + to_string(predecessor);
}

//HeapNode *Vertex::copy() {
//    return new Vertex(id, distance, predecessor, finalized);
//}

bool Vertex::equals(HeapNode *n) {
    auto* v = dynamic_cast<Vertex*>(n);
    return v && this->id == v->id;
}
