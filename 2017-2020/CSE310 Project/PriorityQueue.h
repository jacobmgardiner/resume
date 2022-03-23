//
// Created by jmgar on 4/17/2021.
//

#ifndef PROJECT3_PRIORITYQUEUE_H
#define PROJECT3_PRIORITYQUEUE_H


#include "HeapNode.h"
#include "Heap.h"
#include "Vertex.h"

class PriorityQueue {
public:
    Heap* heap;
    int heapSize;
    bool max = true;
    explicit PriorityQueue(int size);
    explicit PriorityQueue(int size, bool isMax);
    ~PriorityQueue();
    void insert(HeapNode* node) const;
    void replace(HeapNode* node) const;
    HeapNode* extractHighestPriority() const;
    HeapNode* getHighestPriority() const;
    void deleteHighestPriority() const;
//    void edited(HeapNode* node) const;
};


#endif //PROJECT3_PRIORITYQUEUE_H
