#ifndef PROJECT3_LINKEDLIST_H
#define PROJECT3_LINKEDLIST_H

//#include "Node.h"

#include <malloc.h>

template <typename T>
class LinkedList {
    class Node {
    public:
        T* data;
        Node* next;

        explicit Node(T* d) : next(nullptr) { data = d; }
    };

    public:
        Node* root;

    LinkedList() {
        root = nullptr;
    }

    void insertHead(T* data) {
        if (root == nullptr) {
            root = new Node(data);
            return;
        }
        Node* tmp = root;
        root = new Node(data);
        root->next = tmp;
    }

    bool deleteNode(T data) {
        Node last;
        Node n = root;
        while (n != nullptr) {
            if (n.data == data) {
                if (last != nullptr) {
                    last.next = n.next;
                    delete n;
                    return true;
                }
            }
            last = n;
            n = n.next;
        }
        return false;
    }

    Node* lastNode = nullptr;

    Node* next() {
        if (lastNode == nullptr) {
            lastNode = root;
            return lastNode;
        }
        if (hasNext()) {
            lastNode = lastNode->next;
            return lastNode;
        } else {
            return nullptr;
        }
    }

    bool hasNext() {
        return lastNode == nullptr || lastNode->next != nullptr;
    }

    void resetIterator() {
        lastNode = nullptr;
    }
};

#endif //PROJECT3_LINKEDLIST_H
