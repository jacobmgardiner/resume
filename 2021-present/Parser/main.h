//
// Created by jmgar on 11/24/2021.
//

#ifndef PROJECT4_MAIN_H
#define PROJECT4_MAIN_H

#include <map>

class Parser {
public:
    LexicalAnalyzer lexer{};

    InstructionNode *parse_program();

//    int input_index = 0;

//    std::map<std::string, std::vector<int>>* locationMap = new std::map<std::string, std::vector<int>>;
    std::map<std::string, int>* locationMap = new std::map<std::string, int>;

private:
    Token expect(TokenType t);

    void parse_var_section();

    void parse_id_list();

    InstructionNode *parse_body();

    InstructionNode *parse_stmt_list();

    InstructionNode *parse_stmt();

    int parse_primary();

    ArithmeticOperatorType parse_op();

    InstructionNode *parse_output_stmt();

    InstructionNode *parse_input_stmt();

    ConditionalOperatorType parse_relop();

    InstructionNode *parse_default_case();

    void parse_num_list();

    void parse_inputs();

    bool followStmtList();

    bool firstStmtList();

    bool firstStmt();

    bool firstForStmt();

    bool firstInputStmt();

    bool firstOutputStmt();

    bool firstSwitchStmt();

    bool firstIfStmt();

    bool firstWhileStmt();

    bool firstAssignStmt();

    bool first(TokenType tt);

    static void append(InstructionNode *&inst, InstructionNode *inst2);

    int location(const std::string& var) const;

//    int valueOf(const Token& t);

    InstructionNode *parse_assign_stmt();

    void parse_condition(InstructionNode *&inst);

    InstructionNode *parse_for_stmt();

    InstructionNode *parse_while_stmt();

    InstructionNode *parse_if_stmt();

    InstructionNode *parse_case_list(InstructionNode *inst, int &condition, InstructionNode *&mainNoop);

    bool firstDefaultCase();

    bool followNumList();

    bool followInputs();

    bool firstNumList();

    InstructionNode *parse_case(int &condition, InstructionNode *&mainNoop);

    bool followCaseList();

    bool firstCase();

    bool firstCaseList();

    InstructionNode *parse_switch_stmt();

    bool firstPrimary();

    bool firstExpr();

//    void store(const std::string &id, const int &val);
//
//    int getVarVal(const std::string &id);
    void store(const std::string &id, const int &val);

    int allocateLocation(const std::string &id) const;

    void parse_expr(InstructionNode *&inst);

    void storeConstant(const std::string &id, const int &val);

    bool firstOp(int peek);
};

#endif //PROJECT4_MAIN_H
