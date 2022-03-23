#include <cstdlib>
#include <cstdio>
#include <cstdlib>
#include <cstdarg>
#include <cctype>
#include <cstring>
#include "compiler.h"
#include "lexer.h"
#include "main.h"

#include <iostream>

//LexicalAnalyzer lexer;

void syntax_error() {
    //TODO
    printf("SYNTAX ERROR!!!\n");
    exit(1);
}

std::string tokenTypeNames[] = { "END_OF_FILE",
                      "VAR", "FOR", "IF", "WHILE", "SWITCH", "CASE", "DEFAULT", "INPUT", "OUTPUT", "ARRAY",
                      "PLUS", "MINUS", "DIV", "MULT",
                      "EQUAL", "COLON", "COMMA", "SEMICOLON",
                      "LBRAC", "RBRAC", "LPAREN", "RPAREN", "LBRACE", "RBRACE",
                      "NOTEQUAL", "GREATER", "LESS",
                      "NUM", "ID", "ERROR"
};

std::string tokenName(TokenType tt) {
    return tokenTypeNames[tt];
}

Token Parser::expect(TokenType tt) {
    //TODO
    Token t = lexer.GetToken();
//    printf("expecting: %s, %s\n", tokenName(tt).c_str(), t.lexeme.c_str());
    if (t.token_type == tt) {
        return t;
    }
    syntax_error();
}

bool Parser::first(TokenType tt) {
    return lexer.peek(1).token_type == tt;
}

bool Parser::firstAssignStmt() {
    return lexer.peek(1).token_type == ID;
}
bool Parser::firstWhileStmt() {
    return lexer.peek(1).token_type == WHILE;
}
bool Parser::firstIfStmt() {
    return lexer.peek(1).token_type == IF;
}
bool Parser::firstSwitchStmt() {
    return lexer.peek(1).token_type == SWITCH;
}
bool Parser::firstOutputStmt() {
    return lexer.peek(1).token_type == OUTPUT;
}
bool Parser::firstInputStmt() {
    return lexer.peek(1).token_type == INPUT;
}
bool Parser::firstForStmt() {
    return lexer.peek(1).token_type == FOR;
}

bool Parser::firstStmt() {
    return firstAssignStmt()    //ID
    || firstWhileStmt()         //WHILE
    || firstIfStmt()            //IF
    || firstSwitchStmt()        //SWITCH
    || firstOutputStmt()        //OUTPUT
    || firstInputStmt()         //INPUT
    || firstForStmt();          //FOR
}

bool Parser::firstStmtList() {
    return firstStmt();
}

bool Parser::firstDefaultCase() {
    return first(DEFAULT);
}

bool Parser::firstNumList() {
    return first(NUM);
}

bool Parser::firstCaseList() {
    return firstCase();
}

bool Parser::firstCase() {
    return first(CASE);
}

bool Parser::firstPrimary() {
    return first(ID) || first(NUM);
}

bool Parser::firstExpr() {
    return firstPrimary();
}

bool Parser::firstOp(int peek) {
    auto tt = lexer.peek(peek).token_type;
    return tt == PLUS ||
            tt == MINUS ||
            tt == DIV ||
            tt == MULT;
}



bool Parser::followStmtList() {
    return lexer.peek(1).token_type == RBRACE;
}

bool Parser::followNumList() {
    return followInputs();
}

bool Parser::followInputs() {
    return first(END_OF_FILE);
}

bool Parser::followCaseList() {
    return first(RBRACE) || firstDefaultCase();
}



void Parser::parse_num_list() {
    inputs.push_back(stoi(expect(NUM).lexeme));
    if (followNumList()) {
        return;
    } else if (firstNumList()) {
        parse_num_list();
    } else {
        printf("NUM LIST ERROR!!! EXPECTED NUM!!!");
        syntax_error();
    }
}

void Parser::parse_inputs() {
    parse_num_list();
}

struct InstructionNode* Parser::parse_default_case() {
    expect(DEFAULT);
    expect(COLON);
    auto* body = parse_body();
    return body;
}

struct InstructionNode* Parser::parse_case(int& condition, struct InstructionNode* &mainNoop) {
    auto* cj = new InstructionNode;
    cj->type = CJMP;

    expect(CASE);

    cj->cjmp_inst.operand1_index = condition;
//    printf("condition val: %d\n", mem[condition]);
    cj->cjmp_inst.condition_op = CONDITION_NOTEQUAL;
    auto c = parse_primary();
    cj->cjmp_inst.operand2_index = c;
    cj->next = nullptr;

    expect(COLON);
//    append(cj, parse_body());
    auto* body = parse_body();

    auto* j = new InstructionNode;
    j->type = JMP;
    j->jmp_inst.target = mainNoop;
    j->next = nullptr;
//    append(cj, j);

    auto* noop = new InstructionNode;
    noop->type = NOOP;
//    cj->cjmp_inst.target = noop;
    cj->next = noop;
    noop->next = nullptr;
//    append(cj, noop);

    cj->cjmp_inst.target = body;
//    append(cj, body);
    append(body, j);
    append(body, noop);

    return cj;
}

struct InstructionNode* Parser::parse_case_list(struct InstructionNode* inst, int& condition, struct InstructionNode* &mainNoop) {
    append(inst, parse_case(condition, mainNoop));
    if (followCaseList()) {
        return inst;
    } else if (firstCaseList()) {
        return parse_case_list(inst, condition, mainNoop);
    } else {
        printf("CASE LIST ERROR!!!\n");
        syntax_error();
    }
}

struct InstructionNode* Parser::parse_switch_stmt() {
    expect(SWITCH);
    //TODO
//    int c = location(expect(ID).lexeme);
    auto c = parse_primary();
    expect(LBRACE);

    auto* noop = new InstructionNode;
    noop->type = NOOP;
    noop->next = nullptr;
    InstructionNode* inst = nullptr;
    inst = parse_case_list(inst, c, noop);

    if (first(RBRACE)) {
        expect(RBRACE);
    } else if (firstDefaultCase()) {
        //TODO fix
        auto* d = parse_default_case();
        append(inst, d);
        expect(RBRACE);
    } else {
        printf("POST CASE LIST ERROR!!! EXPECTING DEFAULT OR RBRACE!!! INSTEAD GOT %s\n", tokenName(lexer.peek(1).token_type));
        syntax_error();
    }
    append(inst, noop);
    return inst;
}

struct InstructionNode* Parser::parse_for_stmt() {
    struct InstructionNode* inst;

    expect(FOR);
    expect(LPAREN);
    //first assign statement i.e. i=0;
    inst = parse_assign_stmt();

    //condition i.e. i<length;
    auto* c = new InstructionNode;
    c->type = CJMP;
    c->next = nullptr;
    parse_condition(c);
    append(inst, c);
    expect(SEMICOLON);

    //second assign statement i.e. i++;
    auto* a = parse_assign_stmt();

    expect(RPAREN);
    //body of the for statement
    append(inst, parse_body());

    //execute assignment2
    append(inst, a);

    //jmp to condition
    auto* jmp = new InstructionNode;
    jmp->type = JMP;
    jmp->next = nullptr;
    jmp->jmp_inst.target = c;
    append(inst, jmp);

    auto* noop = new InstructionNode;
    noop->type = NOOP;
    noop->next = nullptr;
    append(inst, noop);
    c->cjmp_inst.target = noop;

    return inst;
}

ConditionalOperatorType Parser::parse_relop() {
    if (first(GREATER)) {
        expect(GREATER);
        return CONDITION_GREATER;
    } else if (first(LESS)) {
        expect(LESS);
        return CONDITION_LESS;
    } else if (first(NOTEQUAL)) {
        expect(NOTEQUAL);
        return CONDITION_NOTEQUAL;
    } else {
        printf("RELOP ERROR!!!");
        syntax_error();
    }
}

struct InstructionNode* Parser::parse_input_stmt() {
    expect(INPUT);
    auto id = expect(ID).lexeme;

    //TODO
//    store(id, inputs[input_index]);
//    input_index++;

//    lexer.peek(1).Print();

    expect(SEMICOLON);

    auto* inst = new InstructionNode;
    inst->next = nullptr;
    inst->type = IN;
    inst->input_inst.var_index = location(id);
    return inst;
}

struct InstructionNode* Parser::parse_output_stmt() {
    expect(OUTPUT);
    auto id = expect(ID).lexeme;
    expect(SEMICOLON);

    auto* inst = new InstructionNode;
    inst->next = nullptr;
    inst->type = OUT;
    inst->output_inst.var_index = location(id);
    return inst;
}

ArithmeticOperatorType Parser::parse_op() {
    if (first(PLUS)) {
        expect(PLUS);
        return OPERATOR_PLUS;
    } else if (first(MINUS)) {
        expect(MINUS);
        return OPERATOR_MINUS;
    } else if (first(MULT)) {
        expect(MULT);
        return OPERATOR_MULT;
    } else if (first(DIV)) {
        expect(DIV);
        return OPERATOR_DIV;
    } else {
        printf("OPERATOR ERROR!!!");
        syntax_error();
    }
}

int Parser::parse_primary() {
    if (first(ID)) {
        return location(expect(ID).lexeme);
    } else if (first(NUM)) {
        //TODO
        auto cid = expect(NUM).lexeme;
        storeConstant(cid, stoi(cid));

//        printf("lexeme: %s value: %d location: %d value at location: %d\n", cid.c_str(), stoi(cid), location(cid), mem[location(cid)]);

        return location(cid);
    } else {
        printf("EXPECTED NUM OR ID!!! INSTEAD GOT %s\n", tokenName(lexer.peek(1).token_type));
        syntax_error();
    }
}

void Parser::parse_expr(struct InstructionNode* &inst) {
    inst->assign_inst.operand1_index = parse_primary();
    inst->assign_inst.op = parse_op();
    inst->assign_inst.operand2_index = parse_primary();
}

struct InstructionNode* Parser::parse_assign_stmt() {
    auto* inst = new InstructionNode;
    inst->next = nullptr;
    inst->type = ASSIGN;

    auto id = expect(ID).lexeme;
    inst->assign_inst.left_hand_side_index = location(id);

    expect(EQUAL);

    if (firstOp(2)) {
        parse_expr(inst);
    } else if (firstPrimary()) {   //ID || NUM
        int val = parse_primary();

        inst->assign_inst.op = OPERATOR_NONE;
        inst->assign_inst.operand1_index = val;
    }
    expect(SEMICOLON);
    return inst;
}

void Parser::parse_condition(struct InstructionNode* &inst) {
    inst->cjmp_inst.operand1_index = parse_primary();
    inst->cjmp_inst.condition_op = parse_relop();
    inst->cjmp_inst.operand2_index = parse_primary();
}

struct InstructionNode* Parser::parse_if_stmt() {
    auto* inst = new InstructionNode;
    inst->next = nullptr;
    inst->type = CJMP; // handling WHILE using if and goto nodes

    expect(IF);
    parse_condition(inst);
    inst->next = parse_body();

//      create no-op node and attach it to the list of instruction after the jmp node
    auto* noop = new InstructionNode;
    noop->type = NOOP;
    noop->next = nullptr;
    append(inst, noop);
//    set inst->cjmp_target.target to point to no-op node
    inst->cjmp_inst.target = noop;
    return inst;
}

struct InstructionNode* Parser::parse_while_stmt() {
    auto* inst = new InstructionNode;
    inst->next = nullptr;

    expect(WHILE);

    inst->type = CJMP; // handling WHILE using if and goto nodes

//    parse the condition and set inst->cjmp_inst.condition_op, inst->cjmp_inst.operand1 and inst->cjmp_inst.condition_operand2
    parse_condition(inst);

    inst->next = parse_body(); // when condition is true the next instruction is the first instruction of the body of while

//    create jmp node of type JMP // do not forget to set next field to nullptr
    auto* jmp = new InstructionNode;
    jmp->type = JMP;
    jmp->next = nullptr;
//    set jmp->jmp_inst.target to inst
    jmp->jmp_inst.target = inst;
//    append jmp node to end of body of while
    append(inst, jmp);
//      create no-op node and attach it to the list of instruction after the jmp node
    auto* noop = new InstructionNode;
    noop->type = NOOP;
    noop->next = nullptr;
    append(inst, noop);
//    set inst->cjmp_target.target to point to no-op node
    inst->cjmp_inst.target = noop;
//    return inst;
}

struct InstructionNode* Parser::parse_stmt() {
    auto* inst = new InstructionNode;
    inst->next = nullptr;

    if(firstAssignStmt()) {
        inst = parse_assign_stmt();
    }
    else if(firstWhileStmt()) {
        inst = parse_while_stmt();
    }
    else if(firstIfStmt()) {
        inst = parse_if_stmt();
    }
    else if(firstSwitchStmt()) {
        inst = parse_switch_stmt();
    }
    else if(firstForStmt()) {
        inst = parse_for_stmt();
    }
    else if(firstOutputStmt()) {
        inst = parse_output_stmt();
    }
    else if(firstInputStmt()) {
        inst = parse_input_stmt();
    } else {
        printf("UNRECOGNIZED STATEMENT!!!");
        syntax_error();
    }
    return inst;
}

struct InstructionNode* Parser::parse_stmt_list() {
    struct InstructionNode* instList1; // instruction for one statement
    struct InstructionNode* instList2; // instruction list for statement list
    instList1 = parse_stmt();
    if (firstStmtList()) {
        instList2 = parse_stmt_list();

        //TODO
        append(instList1, instList2);

        return instList1;
    } else if (followStmtList()) { //RBRACE
        return instList1;
    } else {
        printf("ERROR WITH STATEMENT LIST!!! Expecting Statement List!!! Instead Got %s\n", tokenName(lexer.peek(1).token_type).c_str());
        syntax_error();
    }
}

struct InstructionNode* Parser::parse_body() {
    struct InstructionNode* instList;

    expect(LBRACE);
    instList = parse_stmt_list();
    expect(RBRACE);

    return instList;
}

void Parser::parse_id_list() {
    allocateLocation(expect(ID).lexeme);
    if (first(COMMA)) {
        expect(COMMA);
        parse_id_list();
    }
}

void Parser::parse_var_section() {
    parse_id_list();
    expect(SEMICOLON);
}

struct InstructionNode* Parser::parse_program() {
    parse_var_section();
    auto inst = parse_body();
    parse_inputs();
    expect(END_OF_FILE);

    return inst;
}

void Parser::append(struct InstructionNode* &inst, struct InstructionNode* inst2) {
    if (inst == nullptr) {
        inst = inst2;
        return;
    }
    InstructionNode* node = inst;
    while (node->next != nullptr) { node = node->next; }
    node->next = inst2;
}

void Parser::store(const std::string& id, const int& val) {
    mem[location(id)] = val;
}

void Parser::storeConstant(const std::string& id, const int& val) {
    allocateLocation(id);
    mem[location(id)] = val;
}

int Parser::location(const std::string& id) const {
    return locationMap->at(id);
}

int Parser::allocateLocation(const std::string& id) const {
    locationMap->insert({id, next_available});

    next_available++;
    return next_available - 1;
}

struct InstructionNode* parse_generate_intermediate_representation() {
    Parser parser;
    return parser.parse_program();
}