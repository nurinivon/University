#if !defined(__RESOLVER_UTILS___)
#define __RESOLVER_UTILS___

/*
this is a header file for resolver.c
this header includes the includes and signatures for resolver.c
*/

#include "../structures/structures.h"

/*function signatures for resolver.c*/
SourceCodeNode* mergeSourceCode(SourceCodeNode* firstSource, SourceCodeNode* secondSource);
LineType getLineType(StringNode* stringList);
void getCommandFromLine(StringNode* stringListHead, LineType type, char dest[]);
int addData(StringNode* stringListHead, SourceCodeNode* dataListHead, int* DCAdress, char* currentCommand, char* lineStr);
void instructionToMachineCode(char* currentMachineCode, int commandIndex, char commandType, int rs, int rt, int rd, int immed, int address, char reg);
int addInstruction(StringNode* stringList, SourceCodeNode* instructionsHead, int* ICAddress, char* currentCommand, char* currentLine, SymbolNode* symbolsHead, UnfinishedLineNode* unFinishedHead, int lineNumber);
void updateDataList(SourceCodeNode* dataHead, SymbolNode* symbolsHead, int IC);
void resolveUnfinishedLines(UnfinishedLineNode* unFinishedHead, SymbolNode* symbolsHead, SourceCodeNode* instructionsHead, ErrorNode* errorsHead, int* ECAdress);

#endif
