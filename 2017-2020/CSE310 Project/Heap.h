#ifndef PROJECT3_HEAP_H
#define PROJECT3_HEAP_H

#include <malloc.h>
#include "HeapNode.h"

class Heap {
private:
    int typeVal;
public:
    static constexpr const int TYPE_MAX = 0;
    static constexpr const int TYPE_MIN = 1;

    int arraySize;
    int heapSize;
    HeapNode** heapArray;
    int type;

    explicit Heap(int size, int type);

    ~Heap();

    void printHeap() const;

    static int parent(int i);

    static int left(int i);

    static int right(int i);

    void heapify(int i);

//    void heapify(HeapNode* node);

    HeapNode* extractMaximum();

    HeapNode* max() const;

    void add(HeapNode* node);

    void replace(HeapNode* node);

//    int findIndexOf(HeapNode *n) const;

//private:
//    void maxHeapify(int i);
    void swap(int i, int largest) const;
};


#endif //PROJECT3_HEAP_H
