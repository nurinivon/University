#include "app_utils.h"

/*

this file includes global variables and methods for assembler program.
also include strings list instance method.

*/

/* global variables*/
char* instructions[] = {"add", "sub", "and", "or", "nor", "move", "mvhi", "mvlo", "addi", "subi", "andi", "ori", "nori", "bne", "beq", "blt", "bgt", "lb", "sb", "lw", "sw", "lh", "sh", "jmp", "la", "call", "stop"};
char commandTypes[] = {'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'J', 'J', 'J', 'J'};
int opcodes[] = {0, 0, 0, 0, 0, 1, 1, 1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 30, 31, 32, 63};
int functs[] = {1, 2, 3, 4, 5, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
int subCommandTypes[] = {1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1};
char* guidances[] = {".dh", ".dw", ".db", ".asciz", ".entry", ".extern"};
char* savedWords[] = {"dh", "dw", "db", "asciz", "entry", "extern"};

/*
newStringNode is function that creates a new string list node and reurns its address
*/
StringNode* newStringNode()
{
	StringNode* temp = NULL;
	temp = (StringNode *) malloc(sizeof(StringNode)); /*allocate memory*/
	if(temp != NULL)/*check allocation*/
	{
		strcpy(temp->str,""); /*init str to empty string*/
		temp->next = NULL; /*init next pointer*/
	}
	return temp;
} /*end of method newStringNode*/

/*
destroyStringList is a function that gets a linked list head and free all used memory by the list
*/
void destroyStringList(StringNode* head)
{
	if(head == NULL) /*stop condition for recursive calls*/
	{
		return;
	}
	destroyStringList(head->next); /*recursive call with next node*/
	free(head); /*free memory from last node to first*/
} /*end of method destroyStringList*/

/*
thereIsMoreContent is a function that checks if a string has more content (not a white space) after specific index.
the function parameters are:
command (string)
currentIndex (int)

the function returns TRUE or FALSE
*/
Boolean thereIsMoreContent(char *command, int currentIndex)
{
	Boolean result = FALSE;
	while(currentIndex < strlen(command) && result == FALSE)
	{
		if(isspace(command[currentIndex]) == 0)/*if not a white space*/
		{
			result = TRUE;
		}
		currentIndex++;
	}
	return result;
}/*end of method thereIsMoreContent*/

/*
splitStr function gets a command as string and split it into a strings linked list. the function will remove white spaces from the strings.
the function gets the address of the head of the list and the command as a string.
the function doesnt return anything
*/
void splitStr(StringNode *head,char command[])
{
	char tempStr[MAX_STRING_SIZE] = ""; /*string that will hold the temp partial string*/
	int commandIndex = 0; /*the command index*/
	int strIndex = 0; /*temp str index*/
	StringNode* tempNode = NULL; /*temp node to be set*/
	StringNode* tempNewNode = NULL;  /*temp new node if needed*/
	Boolean insideString = FALSE;
	if(head != NULL)
	{
		tempNode = head; /*first init temp node to head of the list*/
		while(commandIndex < strlen(command) && (thereIsMoreContent(command, commandIndex) == TRUE)) /*iterate the chars in the command*/
		{
			strcpy(tempStr,""); /*empty the current temp string*/
			strIndex = 0; /*init string index*/
			insideString = FALSE;
			/*itrate the command until white space OR comma*/
			while(commandIndex < strlen(command) && strIndex < MAX_STRING_SIZE)
			{
				if(command[commandIndex] == '\"')
				{
					insideString = (insideString == FALSE) ? TRUE : FALSE;
				}
				if(insideString == FALSE && (isspace(command[commandIndex]) != 0 || command[commandIndex] == ','))
				{
					break;
				}
				/*create partial string*/
				tempStr[strIndex] = command[commandIndex];
				strIndex++;
				commandIndex++;
			}
			tempStr[strIndex] = '\0'; /*close string properly*/
			strcpy(tempNode->str,tempStr); /*set the node->str to the current partial string*/
			if(commandIndex >= strlen(command) || strIndex >= MAX_STRING_SIZE) /*check if there is more content*/
			{
			}else{
				if(isspace(command[commandIndex]) != 0) /*if its a white space*/
				{
					commandIndex++; /*check if there is content after the white space*/
					if(commandIndex < strlen(command) && strIndex > 0 && (thereIsMoreContent(command, commandIndex) == TRUE))
					{
						/*create new node*/
						tempNewNode = newStringNode();
						if(tempNewNode != NULL) /*check allocation*/
						{
							tempNode->next = tempNewNode;
							tempNode = tempNode->next;
						}else{
							break;
						}
					}
				}else{ /*if its not a white space - > it is a comma*/
					if(strIndex == 0) /*if its only a comma with no other chars*/
					{
						strcpy(tempNode->str,",");
						commandIndex++;
					}
					if(commandIndex < strlen(command) && (thereIsMoreContent(command, commandIndex) == TRUE))/*if there is still content after the comma*/
					{
						/*create new node*/
						tempNewNode = newStringNode();
						if(tempNewNode != NULL)/*check allocation*/
						{
							tempNode->next = tempNewNode;
							tempNode = tempNode->next;
						}else{
							break;
						}
					}
				}
			}
		}
	}
}/*end of method splitStr*/

/*this function is is for debugging correct split str*/
void printStrList(StringNode* stringListHead)
{
	int index = 0;
	while(stringListHead != NULL)
	{
		printf("\nnode index %d is : %s",index,stringListHead->str);
		index++;
		stringListHead = stringListHead->next;
	}
}

/*
isInstructionWord is a function that check if a string is a saved instruction word
the function parameters are:
str (string)

the function returns TRUE or FALSE
*/
Boolean isInstructionWord(char* str)
{
	Boolean temp = FALSE;
	int i = 0;
	while(i < INSTRUCTIONS_NUMBER && temp == FALSE)/*go over instructions strings array*/
	{
		if(strcmp(str, instructions[i]) == 0)
		{
			temp = TRUE;
		}
		i++;
	}
	return temp;
}/*end of method isInstructionWord*/

/*
isGuidanceWord is a function that check if a string is a saved guidance word
the function parameters are:
str (string)

the function returns TRUE or FALSE
*/
Boolean isGuidanceWord(char* str)
{
	int i = 0;
	while(i < GUIDANCES_NUMBER)/*go over guidances strings array*/
	{
		if(strcmp(str, guidances[i]) == 0)
		{
			return TRUE;
		}
		i++;
	}
	return FALSE;
}/*end of method isGuidanceWord*/

/*
isLabel is a function that check if a string is a label (ends with ':')
the function parameters are:
str (string)

the function returns TRUE or FALSE
*/
Boolean isLabel(char* str)
{
	Boolean foundDots = FALSE;	
	int i = 0;
	while(i < strlen(str) && foundDots == FALSE)
	{
		if(str[i] == ':')
		{
			foundDots = TRUE;
			if(str[i + 1] != '\0')/*check this is the last char*/
			{
				return FALSE;
			}
		}
		i++;
	}
	return foundDots;
}/*end of method isLabel*/

/*
isCommand is a function that check if a string is a command (guidance / instruction)
the function parameters are:
str (string)

the function returns TRUE or FALSE
*/
Boolean isCommand(char* str)
{
	if(isGuidanceWord(str) == TRUE)
	{
		return TRUE;
	}
	if(isInstructionWord(str) == TRUE)
	{
		return TRUE;
	}
	return FALSE;
}/*end of method isCommand*/

/*
isSavedWord is a function that check if a string is a saved word
the function parameters are:
str (string)

the function returns TRUE or FALSE
*/
Boolean isSavedWord(char* str)
{
	int i = 0;
	while(i < SAVED_WORDS_NUMBER)
	{
		if(strcmp(str, savedWords[i]) == 0)
		{
			return TRUE;
		}
		i++;
	}
	return FALSE;
}/*end of method isSavedWord*/

/*
isValidLabel is a function that check if a string is a valid label
valid label is:
not a command
not a saved word
not a register
length <= 31

the function parameters are:
str (string)

the function returns TRUE or FALSE
*/
Boolean isValidLabel(char* str)
{
	if(strlen(str) > 31)
	{
		return FALSE;
	}
	if(isCommand(str) == TRUE)
	{
		return FALSE;
	}
	if(str[0] == '$')
	{
		return FALSE;
	}
	if(isSavedWord(str) == TRUE)
	{
		return FALSE;
	}
	return TRUE;
}/*end of method isValidLabel*/

/*
isNumber is a function that check if a string is a number
a number includes only digits and could include a sign ('-' / '+') only in the beginnig

the function parameters are:
str (string)

the function returns TRUE or FALSE
*/
Boolean isNumber(char* str)
{
	int charIndex = 0;
	while(charIndex < strlen(str))
	{
		if(isdigit(str[charIndex]) == 0)/*if not a digit*/
		{
			if(charIndex == 0)/*if it is the first char*/
			{
				if(str[charIndex] != '-' && str[charIndex] != '+')
				{
					return FALSE;
				}
			}else{
				return FALSE;
			}
		}
		charIndex++;
	}
	return TRUE;
}/*end of method isNumber*/

/*
intToChars is a function that gets an integer (32 bits) and translate it into for chars (8 X 4 = 32)
the function parameter are:
num (int) - the number to be translated
machineCode (char array[4]) - the destination for the translation
*/
void intToChars(int num, char machineCode[])
{
	int i = 0;
	machineCode[0] = '\0';
	machineCode[1] = '\0';
	machineCode[2] = '\0';
	machineCode[3] = '\0';
	while(i < BITS_IN_WORD)/*BITS_IN_WORD = 32*/
	{
		if((num & (1u << i)) != 0)/*if the current bit is not 0*/
		{
			machineCode[(int) (i / BITS_IN_BYTE)] |= (1u << (i % BITS_IN_BYTE));/*put bit '1' in the appropriate position*/
		}
		i++;
	}
}/*end of method intToChars*/

/*
isEmptyLine is a function that check if a string is only white spaces
a number includes only digits and could include a sign ('-' / '+') only in the beginnig

the function parameters are:
str (string)

the function returns TRUE or FALSE
*/
Boolean isEmptyLine(char *currentLine)
{
	int i = 0;
	Boolean result = TRUE;
	while(result == TRUE && i < strlen(currentLine))
	{
		if(isspace(currentLine[i]) == 0)
		{
			result = FALSE;
		}
		i++;
	}
	return result;
}/*end of method isEmptyLine*/

/*
isRegister is a function that check if a string is a register
a register starts with '$' and all other chars are digits

the function parameters are:
str (string)

the function returns TRUE or FALSE
*/
Boolean isRegister(char* str)
{
	int i = 0;
	while(i < strlen(str))
	{
		if(i == 0)
		{
			if(str[i] != '$')
			{
				return FALSE;
			}
		}else{
			if(isdigit(str[i]) == 0)
			{
				return FALSE;
			}
		}
		i++;
	}
	return TRUE;
}/*end of method isRegister*/
