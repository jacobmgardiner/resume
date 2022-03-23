#ifndef PROJECT3_UTIL_H
#define PROJECT3_UTIL_H


std::string getNextArgument(const char* &cmd, int &length);

template<typename T> T* trimArray(T* arr, int newSize) {
    T* newArr = (T*)malloc(sizeof(T) * newSize);
    for (int i = 0; i < newSize; i++) {
        newArr[i] = arr[i];
    }
    return newArr;
};

std::string listToString(int* list, int size, const std::string& separator);

#endif //PROJECT3_UTIL_H
