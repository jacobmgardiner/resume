#include <string>
#include <cstring>
#include <fstream>
#include "util.h"
using namespace std;

/**
 * Get the next argument in the given string.
 * It changes the given pointer to point to the beginning of the next argument
 * as well as the length to reflect the new length of the string
 * so that this method can be used again with the same variables.
 * This method will account for quotes (i.e. it will return the contents of quotes as one argument regardless of spaces).
 * @param cmd A pointer to the char array. Changed to the first character of the next command.
 * @param length The number of characters in the char array. Changed
 * @return The next argument in the command.
 */
string getNextArgument(const char* &cmd, int &length) {
    string arg;
    char t = ' ';
    int i = 0;
    int s = 0;
    if (cmd[0] == '\"') {
        t = '\"';
        i++;
    }
    for (; i < length; i++) {
        if (cmd[i] == t || cmd[i] == '\t') {
            length = length - i - 1;
            cmd = cmd + i + 1;
            if (t == '\"') {
                cmd++;
            }
            if (s == 0) {
                return getNextArgument(cmd, length);
            }
            return arg;
        } else {
            arg += cmd[i];
            s++;
        }
    }
    return arg;
}

inline bool fileExists (const std::string& filepath) {
    ifstream file(filepath.c_str());
    return file.good();
}

std::string listToString(int* list, int size, const std::string& separator) {
    string str;
    for (int i = 0; i < size - 1; i++) {
        str += to_string(list[i]) + separator;
    }
    str += to_string(list[size - 1]);
    return str;
}