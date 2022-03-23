#include <iostream>
#include <fstream>
#include <cmath>
#include "util.h"
#include "LinkedList.h"
#include "AdjacencyList.h"
#include "Heap.h"
#include "Vertex.h"
#include "PriorityQueue.h"
#include "Dijkstra.h"

using namespace std;

//AdjacencyList* list;
//PriorityQueue* queue;
//Vertex** vertices;
//Vertex** finalized;

int source;
int destination;
//
//bool computed = false;

bool isFlagValid(int f) {
    return f == 0 || f == 1;
}

bool isDestValid(int s, int d) {
    return d != s;
}

bool isSourceValid(int s) {
    return s >= 0 && s < list->numVertices;
}

void initAdjList(const string& filepath, const bool& direction) {
    ifstream mfile(filepath);
    if(!mfile.good()) {
//        cout << filepath << " is bad!!!" << endl;
        return;
    }

    string line1;
    getline(mfile, line1);
    const char* nm = line1.c_str();
    int s = line1.length();
    int n = stoi(getNextArgument(nm, s));
    int m = stoi(getNextArgument(nm, s));

//    cout << n << ", " << m << endl;

    list = new AdjacencyList(n, m, direction);

    string line;
    while (getline(mfile, line)) {
        const char* args = line.c_str();
        int l = line.length();
        auto* data = new float[4];
        data[0] = stof(getNextArgument(args, l)) - 1;
        data[1] = stof(getNextArgument(args, l)) - 1;
        data[2] = stof(getNextArgument(args, l)) - 1;
        data[3] = stof(getNextArgument(args, l));
//        cout << data[0] << ", " << data[1] << ", " << data[2] << ", " << data[3] << endl;
        list->addEdge(data);
//        free(data);
        delete[] (data);
    }
}

//string appendPath(int v) {
//    if (v == -1) return "";
//    return appendPath(vertices[v]->predecessor) + ", " + to_string(vertices[v]->id);
//}

int pathLength;
int * createPath(int d) {
    int* l = new int[list->numVertices];
    int v = d;
    int pl = 0;
    while (v != -1) {
        v = vertices[v]->predecessor;
        pl++;
    }
    int i = pl - 1;
    v = d;
    while (v != -1) {
        l[i--] = v + 1;
        v = vertices[v]->predecessor;
    }
    pathLength = pl;
    return l;
}

void case1(int d) {
    int* l = createPath(d);

    cout << "Shortest path: <";
    cout << listToString(l, pathLength, ", ");
    cout << ">" << endl;
    printf("The path weight is: %12.4f\n", vertices[d]->distance);

    delete [] l;
}

void case2(int d) {
    int* l = createPath(d);

    cout << "Path not known to be shortest: <";
    cout << listToString(l, pathLength, ", ");
    cout << ">" << endl;
    printf("The path weight is: %12.4f\n", vertices[d]->distance);

    delete [] l;
}

void case3(int s, int d) {
    cout << "No " << s+1 << "-" << d+1 << " path has been computed." << endl;
}

void case4(int s, int d) {
    cout << "No " << s+1 << "-" << d+1 << " path exists." << endl;
}

void writePath(int s, int d) {
    if (vertices[d] && vertices[d]->distance < INFINITY) {
        if (vertices[d]->finalized) {
            case1(d);
        } else {
            case2(d);
        }
    } else {
        if (destination > list->numVertices) {
            case4(s, d);
        } else {
            case3(s, d);
        }
    }
}

/**
 * Denote  bysource,destination,  andflagthe  values  of<source>,<destination>,  and<flag>,respectively.This query is valid ifsource∈V,destinationis an integer not equal tosource, andflag∈{0,1}.If the query is not valid, your program should write a message tostdoutusing the following formatand wait for the next query."Error: invalid find query\n"Now  assume  that  thefindquery  is  valid.   Your  program  should  execute  the  variant  of  Dijkstra’salgorithm as specified in§1.2, and wait for the next query
 */
void find(int s, int d, int f) {
//    find <source> <destination> <flag>
    if (!isSourceValid(s) || !isDestValid(s, d) || !isFlagValid(f)) {
        cout << "Error: invalid find query\n";
        return;
    }
    source = s;
    destination = d;
    dijkstra(s, d, f);
}

/**
 *
 * @param line
 */
void handleQuery(const string& line) {
    int l = line.length();
    const char* q = line.c_str();
    string arg1 = getNextArgument(q, l);
    if (arg1 == "find") {
        computed = false;
//        string ar2 = getNextArgument(q, l);
        int s = stoi(getNextArgument(q, l)) - 1;
        int d = stoi(getNextArgument(q, l)) - 1;
        int f = stoi(getNextArgument(q, l));

        find(s, d, f);
    }
    else if (arg1 == "write") {
        string arg2 = getNextArgument(q, l);
        if (arg2 == "path") {
            //write path <s> <d>
            int s = stoi(getNextArgument(q, l)) - 1;
            int d = stoi(getNextArgument(q, l)) - 1;
//            cout << "Query: " << line << endl;

            if (!computed) {
                cout << "Error: no path computation done\n";
                return;
            }
            if (s != source || !isDestValid(s, d) || !vertices[d]) {
                cout << "Error: invalid source destination pair" << endl;
//                cout << "Error: invalid find query\n";
                return;
            }
            writePath(source, d);
        }
    }
}

int main(int argc, char *argv[]) {
    if (argc < 3) return 0;

    string filename = argv[1];
    string direction = argv[2];

    bool d = false;
    if (direction == "directed")
        d = true;

    initAdjList(filename, d);

    string line;
    getline(cin, line);
    cout << "Query: " << line << endl;
    while (line != "stop") {
        handleQuery(line);
        getline(cin, line);
        cout << "Query: " << line << endl;
    }

    if (vertices)
        for (int i = 0; i < list->numVertices; i++)
            if (vertices[i])
                delete vertices[i];
    delete [] vertices;
    delete [] finalized;
    delete queue;

    return 0;
}
