#ifndef PROJECT3_EDGEDATA_H
#define PROJECT3_EDGEDATA_H

#include <string>

using namespace std;

class EdgeData {
public:
    int id;
    int vertex;
    float weight;
    EdgeData(int id, int vertex, float weight);

    string toString() const;
};


#endif //PROJECT3_EDGEDATA_H
