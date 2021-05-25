#include "./utils/utils.h"

#define SET_SIZE 4
#define SET_NAME_SIZE 4
#define COMMAND_INDEX 0
#define MAX_INT_VALUE 128
#define MAX_NUMBER_IN_ROW 16
#define BITS_IN_INT 32
#define SET_ARRAY_SIZE 3

/*
set struct definition:
set has content which is int array size 4;
int size is 4 bytes, byte size is 8 bits.

so each set->content size is 128 bits.
using this structure we can indicate if a number between 0-127 is in the set (if the bit is 0 or not), not using an int array size 128 which is much larger.
*/
typedef struct
{
   unsigned int content[SET_SIZE];
}set;

set* newSet();
set* getAddressBySetName(char* setName);
set* read_set(set* currentSet, NumberNode* numbersHead);
void print_set(set* currentSet);
set* union_set(set* setsArr[]);
set* intersect_set(set* setsArr[]);
set* sub_set(set* setsArr[]);
set* symdiff_set(set* setsArr[]);
Boolean isValidSetName(char *setName);
Boolean isValidCommand(char *commandName);
int executeCommand(char command[]);
