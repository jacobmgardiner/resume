#include <string>
#include "EdgeData.h"

using namespace std;

EdgeData::EdgeData(int id, int vertex, float weight) {
    this->id = id;
    this->vertex = vertex;
    this->weight = weight;
}

string EdgeData::toString() const {
    return "id: " + to_string(id) + ", to: " + to_string(vertex) + ", weight: "+ to_string(weight);
}
