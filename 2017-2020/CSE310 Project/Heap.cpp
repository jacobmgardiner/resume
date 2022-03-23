#include "Heap.h"
#include "HeapNode.h"
#include <iostream>
#include <cmath>
#include <cstring>

using namespace std;

Heap::Heap(int size, int type) {
    this->type = type;
    if (type == TYPE_MAX) {
        typeVal = 1; //max
    } else {
        typeVal = -1; //min
    }
    arraySize = size;
    heapArray = new HeapNode*[size];
    heapSize = 0;
    for (int i = parent(heapSize); i >= 0; i--) {
        heapify(i);
    }
}

Heap::~Heap() {
    delete [] heapArray;
}

void Heap::printHeap() const {
    for (int i = 0; i < heapSize; i++) {
        cout << i << ": " << heapArray[i]->toString() << endl;
    }
}

int Heap::parent (int i) {
    return floor((i - 1) / 2);
}

int Heap::left (int i) {
    return i * 2 + 1;
}

int Heap::right (int i) {
    return i * 2 + 2;
}

void Heap::swap(int i, int largest) const {
    HeapNode* tmp = heapArray[i];

    heapArray[i] = heapArray[largest];
    heapArray[i]->heapIdx = i;

    heapArray[largest] = tmp;
    heapArray[largest]->heapIdx = largest;
}

/**
 *
 * @param i The index of the unsorted node.
 * @param n The size of the heap.
 */
void Heap::heapify (int i) {
    if (heapSize < 2) return;

//    printHeap();

    int l = left(i);
    int r = right(i);
    int largest = i;
    if (l < heapSize && heapArray[l]->compareWith(heapArray[largest]) * typeVal < 0) {
        largest = l;
    }
    if (r < heapSize && heapArray[r]->compareWith(heapArray[largest]) * typeVal < 0) {
        largest = r;
    }
    if (largest != i) {
        swap(i, largest);
        heapify(largest);
    }

//    printHeap();
}

HeapNode* Heap::max() const {
    return heapArray[0];
}

HeapNode* Heap::extractMaximum() {
    if (heapSize < 1) return nullptr;
//    cout << "root: " << heapArray[0]->toString() << endl;
    HeapNode* root = heapArray[0]/*->copy()*/;
    root->heapIdx = -1;

    heapArray[0] = heapArray[heapSize-1];
//    free(heapArray[heapSize-1]);
//    heapArray[heapSize-1] = nullptr;
    heapSize--;
    heapify(0);
    return root;
}

void Heap::add(HeapNode* node) {
    if (heapSize >= arraySize) return;
    heapArray[heapSize] = node;
    node->heapIdx = heapSize;
    heapSize++;
    heapify(parent(heapSize - 1));
}

//int Heap::findIndexOf(HeapNode *n) const {
//    for (int i = 0; i < heapSize; i++) {
//        if (heapArray[i]->equals(n)) {
//            return i;
//        }
//    }
//    return -1;
//}

void Heap::replace(HeapNode *node) {
//    int i = findIndexOf(node);
    if (node->heapIdx != -1) {
//        delete heapArray[node->heapIdx];
        heapArray[node->heapIdx] = node;
        heapify(node->heapIdx);
    } else
        add(node);
}
