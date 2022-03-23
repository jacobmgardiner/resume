#ifndef PROJECT3_VERTEX_H
#define PROJECT3_VERTEX_H

#include <limits>
#include "HeapNode.h"

using namespace std;

class Vertex : public HeapNode {
public:
    int id;
    float distance;
    int predecessor;
    bool finalized;
    int heapIdx;

    explicit Vertex(int id);

    explicit Vertex(int id, float distance, int predecessor, bool finalized);

//    ~Vertex();

    int compareWith(HeapNode* n) override;

    bool equals(HeapNode* n) override;

    string toString() override;

//    HeapNode* copy() override;
};


#endif //PROJECT3_VERTEX_H
