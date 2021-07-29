#if !defined(__FILES_UTILS___)
#define __FILES_UTILS___

/*
this is a header file for files.c
this header includes specific includes for files.c
*/

#include "../structures/structures.h"

/*function signatures for files.c*/
Boolean validInputFileName(char* fileName);
int getCodeLines(SourceCodeNode* source);
int getDataLines(SourceCodeNode* source);
void generateObjectFile(char* fileName, SourceCodeNode* source);
void generateEntriesFile(char* fileName, SymbolNode* symbolsHead);
void generateExternsFile(char* fileName, ExternNode* externsHead);
char binaryToHex(char bits[]);
void charToHex(char* hexChars, char c);

#endif
