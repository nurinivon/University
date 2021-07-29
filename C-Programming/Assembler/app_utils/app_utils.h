#if !defined(__APP_UTILS___)
#define __APP_UTILS___

/*
this is a header file for app_utils.c
this header includes the global includes for all the program and structures definitions for global structures.
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <limits.h>

/*global definitions*/
#define START_ADDRESS 100
#define MAX_COMMAND_SIZE 200
#define MAX_STRING_SIZE 100
#define INSTRUCTIONS_NUMBER 27
#define GUIDANCES_NUMBER 6
#define SAVED_WORDS_NUMBER 6
#define MAX_HALF_WORD_NUMBER 32767
#define MIN_HALF_WORD_NUMBER -32768
#define MAX_BYTE_NUMBER 127
#define MIN_BYTE_NUMBER -128
#define BITS_IN_WORD 32
#define BITS_IN_HALF_WORD 16
#define BITS_IN_BYTE 8
#define MAX_REGISTER_INDEX 31
#define ERRORS_NUMBER 29


/*
enum definition for boolean
*/
typedef enum
{
	TRUE,
	FALSE
} Boolean;

/*
enum definition for line types
*/
typedef enum
{
	COMMENT,
	UNDEFINED,
	INSTRUCTION,
	GUIDANCE	
} LineType;

/*
struct defintion for node.
this node is meant to use for string linked list .
*/
typedef struct StringNode
{
	char str[MAX_STRING_SIZE];
	struct StringNode* next;
}StringNode;

/*function signatures for app_utils.c*/
StringNode* newStringNode();
void destroyStringList(StringNode* head);
Boolean thereIsMoreContent(char *command, int currentIndex);
void splitStr(StringNode *head,char command[]);
void printStrList(StringNode* stringListHead);
Boolean isInstructionWord(char* str);
Boolean isGuidanceWord(char* str);
Boolean isLabel(char* str);
Boolean isCommand(char* str);
Boolean isSavedWord(char* str);
Boolean isValidLabel(char* str);
Boolean isNumber(char* str);
void intToChars(int num, char machineCode[]);
Boolean isEmptyLine(char *currentLine);
Boolean isRegister(char* str);
#endif
