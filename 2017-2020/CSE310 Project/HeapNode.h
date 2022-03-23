#ifndef PROJECT3_HEAPNODE_H
#define PROJECT3_HEAPNODE_H

#include <string>

class HeapNode {
public:
    int heapIdx = -1;
    virtual int compareWith(HeapNode* n) = 0;
    virtual bool equals(HeapNode* n) = 0;
    virtual std::string toString() = 0;
//    virtual HeapNode* copy() = 0;
};


#endif //PROJECT3_HEAPNODE_H
