#if !defined(__STRUCTURES_UTILS___)
#define __STRUCTURES_UTILS___

/*
this file is a header file for structures.c.
it includes all the structures definitions for the program and all funcions signatures from signatures.c
*/

#include "../app_utils/app_utils.h"

/*
source code node in source code list
*/
typedef struct SourceCodeNode
{
	int address;
	char sourceCode[MAX_COMMAND_SIZE];
	int size;
	char machineCode[4];
	Boolean isCode;
	struct SourceCodeNode* next;
}SourceCodeNode;

/*
extern node in externs list
*/
typedef struct ExternNode
{
	int address;
	char name[MAX_COMMAND_SIZE];
	struct ExternNode* next;
}ExternNode;

/*
symbol node in symbols list
*/
typedef struct SymbolNode
{
	int value;
	char name[MAX_COMMAND_SIZE];
	char attrs[4];
	/*
	attrs[0] - entry
	attrs[1] - external
	attrs[2] - data
	attrs[3] - code
	*/
	struct SymbolNode* next;
}SymbolNode;

/*
error node in errors list
*/
typedef struct ErrorNode
{
	int lineNumber;
	int errorType;
	char lineTxt[MAX_COMMAND_SIZE];
	struct ErrorNode* next;
}ErrorNode;

/*
unfinished line node in unfinished lines list
*/
typedef struct UnfinishedLineNode
{
	int address;
	int lineNumber;
	struct UnfinishedLineNode* next;
}UnfinishedLineNode;

/*functions signature from structures.c*/
SourceCodeNode* newSourceCodeNode();
void destroySourceCodeList(SourceCodeNode* head);
ExternNode* newExternNode();
void destroyExternsList(ExternNode* head);
SymbolNode* newSymbolNode();
void destroySymbolsList(SymbolNode* head);
ErrorNode* newErrorNode();
void destroyErrorsList(ErrorNode* head);
UnfinishedLineNode* newUnfinishedLineNode();
void destroyUnFinishedLinesList(UnfinishedLineNode* head);
Boolean isSymbolExist(char* symbol, SymbolNode* symbolsHead);
char getSymbolAttr(SymbolNode* symbolsHead, char* symbol, int attrToCheck);
void addSymbol(int address, SymbolNode* symbolsListHead, char symbol[], int attrIndex);
void printSymbols(SymbolNode* symbolsHead);
void createExternsList(ExternNode* externsHead, SymbolNode* symbolsHead, SourceCodeNode* instructionsHead);
void addExternNode(ExternNode* externsHead, char name[], int address);
int addExtern(StringNode* stringList, SymbolNode* symbolsHead);
int getSymbolValue(SymbolNode* symbolsHead, char* symbol);
void addSourceCodeNode(SourceCodeNode* head, int tempAddress, char tempSourceCode[], char currentMachineCode[], int tempSize, Boolean tempIsCode);
char* getErrorType(int errorType);
void printErrors(ErrorNode* errorsHead, char* fileName);
void addErrorToList(ErrorNode* errorsHead, int lineNumber, int errorType, char* currentLine);
void printSource(SourceCodeNode* source);
void setSymbolAttr(SymbolNode* symbolsHead, char* symbol, int attr);
int addEntry(StringNode* stringList, SymbolNode* symbolsHead);
void addUnfinishedLine(UnfinishedLineNode* unFinishedHead, int address, int lineNumber);
#endif
