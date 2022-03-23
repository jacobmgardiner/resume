#include "PriorityQueue.h"

PriorityQueue::PriorityQueue(int size) {
    heapSize = size;
    heap = new Heap(size, Heap::TYPE_MAX);
}

PriorityQueue::PriorityQueue(int size, bool isMax) {
    heapSize = size;
    max = isMax;
    heap = new Heap(size, isMax);
}

void PriorityQueue::insert(HeapNode* node) const {
    heap->add(node);
}

HeapNode* PriorityQueue::extractHighestPriority() const {
    return heap->extractMaximum();
}

HeapNode* PriorityQueue::getHighestPriority() const {
    return heap->max();
}

void PriorityQueue::deleteHighestPriority() const {
    heap->extractMaximum();
}

//void PriorityQueue::edited(HeapNode *node) const {
//    heap->heapify(node);
//}

void PriorityQueue::replace(HeapNode *node) const {
    heap->replace(node);
}

PriorityQueue::~PriorityQueue() {
    delete heap;
}
