#include "./structures.h"

/*
this file includes all structures related function. (create new node, destroy lists, add node to list...)
*/

/*a global variable with all availble errors*/
char* errors[] = {
	/*0*/"undefined line type",
	/*1*/"cannot resolve two labels in one line",
	/*2*/"label already exist",
	/*3*/"not a valid label",
	/*4*/"cannot resolve two commands in one line",
	/*5*/"missing command",
	/*6*/"extraneous operand",
	/*7*/"symbol already exist",
	/*8*/"missing string",
	/*9*/"missing string quote opener",
	/*10*/"missing string quote closer",
	/*11*/"extraneous text after string",
	/*12*/"missing operands",
	/*13*/"missing comma",
	/*14*/"not a number",
	/*15*/"extraneous comma after end of command",
	/*16*/"number out of range",
	/*17*/"invalid label",
	/*18*/"undefined operand",
	/*19*/"unexpected comma",
	/*20*/"not a register",
	/*21*/"register out of range",
	/*22*/"expected internal label but found extern",
	/*23*/"symbol doesnt exist",
	/*24*/"symbol already external",
	/*25*/"memory error",
	/*26*/"symbol is not an entry",
	/*27*/"symbol is not an entry / extern",
	/*28*/"unrecognized error"
};

/*
newSourceCodeNode is function that creates new source code node and returns its address
*/
SourceCodeNode* newSourceCodeNode()
{
	SourceCodeNode* temp;
	temp = (SourceCodeNode*)malloc(sizeof(SourceCodeNode));
	if(temp != NULL)
	{
		temp->address = 0;
		temp->size = 0;
		temp->next = NULL;
		temp->isCode = FALSE;
	}
	return temp;
}/*end of method newSourceCodeNode*/

/*
destroySourceCodeList is a function that gets an output linked list head and free all used memory by the list
*/
void destroySourceCodeList(SourceCodeNode* head)
{
	if(head == NULL) /*stop condition for recursive calls*/
	{
		return;
	}
	destroySourceCodeList(head->next); /*recursive call with next node*/
	free(head); /*free memory from last node to first*/
} /*end of method destroySourceCodeList*/

/*
newExternNode is function that creates new extern node and returns its address
*/
ExternNode* newExternNode()
{
	ExternNode* temp;
	temp = (ExternNode*)malloc(sizeof(ExternNode));
	if(temp != NULL)
	{
		temp->address = 0;
		temp->next = NULL;
	}
	return temp;
}/*end of method newExternNode*/

/*
destroyExternsList is a function that gets an output linked list head and free all used memory by the list
*/
void destroyExternsList(ExternNode* head)
{
	if(head == NULL) /*stop condition for recursive calls*/
	{
		return;
	}
	destroyExternsList(head->next); /*recursive call with next node*/
	free(head); /*free memory from last node to first*/
} /*end of method destroyExternsList*/

/*
newSymbolNode is function that creates new symbol node and returns its address
*/
SymbolNode* newSymbolNode()
{
	SymbolNode* temp;
	temp = (SymbolNode*)malloc(sizeof(SymbolNode));
	if(temp != NULL)
	{
		temp->value = 0;
		temp->attrs[0] = '0';
		temp->attrs[1] = '0';
		temp->attrs[2] = '0';
		temp->attrs[3] = '0';
		temp->next = NULL;
	}
	return temp;
}/*end of method newSymbolNode*/

/*
destroySymbolsList is a function that gets an output linked list head and free all used memory by the list
*/
void destroySymbolsList(SymbolNode* head)
{
	if(head == NULL) /*stop condition for recursive calls*/
	{
		return;
	}
	destroySymbolsList(head->next); /*recursive call with next node*/
	free(head); /*free memory from last node to first*/
} /*end of method destroySymbolsList*/

/*
newErrorNode is function that creates new error node and returns its address
*/
ErrorNode* newErrorNode()
{
	ErrorNode* temp;
	temp = (ErrorNode*)malloc(sizeof(ErrorNode));
	if(temp != NULL)
	{
		temp->lineNumber = 0;
		temp->errorType = 0;
		temp->next = NULL;
	}
	return temp;
}/*end of method newErrorNode*/

/*
destroyErrorsList is a function that gets an output linked list head and free all used memory by the list
*/
void destroyErrorsList(ErrorNode* head)
{
	if(head == NULL) /*stop condition for recursive calls*/
	{
		return;
	}
	destroyErrorsList(head->next); /*recursive call with next node*/
	free(head); /*free memory from last node to first*/
} /*end of method destroyErrorsList*/

/*
newUnfinishedLineNode is function that creates new unfinished line node and returns its address
*/
UnfinishedLineNode* newUnfinishedLineNode()
{
	UnfinishedLineNode* temp;
	temp = (UnfinishedLineNode*)malloc(sizeof(UnfinishedLineNode));
	if(temp != NULL)
	{
		temp->address = 0;
		temp->lineNumber = 0;
		temp->next = NULL;
	}
	return temp;
}/*end of method newUnfinishedLineNode*/

/*
destroyUnFinishedLinesList is a function that gets an output linked list head and free all used memory by the list
*/
void destroyUnFinishedLinesList(UnfinishedLineNode* head)
{
	if(head == NULL) /*stop condition for recursive calls*/
	{
		return;
	}
	destroyUnFinishedLinesList(head->next); /*recursive call with next node*/
	free(head); /*free memory from last node to first*/
} /*end of method destroyUnFinishedLinesList*/

/*
isSymbolExist is a function that gets a symbol name and a symbols list and check if the symbol is exists in it.
the function parameters:
symbol (char*) - the symbol name to check
symbolsHead (SymbolNode*) - the list head

the function return TRUE or FALSE
*/
Boolean isSymbolExist(char* symbol, SymbolNode* symbolsHead)
{
	Boolean result = FALSE;
	while(symbolsHead != NULL)
	{
		if(strcmp(symbolsHead->name, symbol) == 0)
		{
			result = TRUE;
		}
		symbolsHead = symbolsHead->next;
	}
	return result;
}/*end of method isSymbolExist*/

/*
getSymbolAttr is a function that gets a symbol name, a symbols list and a an attribute to check (code, data,entry,extern). it returns if it checked or not.
the function parameters:
symbol (char*) - the symbol name to check
symbolsHead (SymbolNode*) - the list head
attrToCheck (int) - the index of the attribute

the function return '0' or '1'
*/
char getSymbolAttr(SymbolNode* symbolsHead, char* symbol, int attrToCheck)
{
	while(symbolsHead != NULL)
	{
		if(strcmp(symbolsHead->name, symbol) == 0)
		{
			return symbolsHead->attrs[attrToCheck];
		}
		symbolsHead = symbolsHead->next;
	}
	return '0';
}/*end of method isSymbolExist*/

/*
addSymbol is a function that add a symbol node to a list
the function parameters:
symbol (char[]) - the symbol name
symbolsHead (SymbolNode*) - the list head
address (int) - address for new node
attrIndex (int) - the attribute index
*/
void addSymbol(int address, SymbolNode* symbolsListHead, char symbol[], int attrIndex)
{
	SymbolNode* tempSymbolNode; 
	/*if this is the first node*/
	if((symbolsListHead->attrs[0] == '0') && (symbolsListHead->attrs[1] == '0') && (symbolsListHead->attrs[2] == '0') && (symbolsListHead->attrs[3] == '0'))
	{
		strcpy(symbolsListHead->name, symbol);
		symbolsListHead->value = address;
		symbolsListHead->attrs[attrIndex] = '1';
	}else{
		while(symbolsListHead->next != NULL)
		{
			symbolsListHead = symbolsListHead->next;
		}
		tempSymbolNode = newSymbolNode();
		strcpy(tempSymbolNode->name, symbol);
		tempSymbolNode->value = address;
		tempSymbolNode->attrs[attrIndex] = '1';
		symbolsListHead->next = tempSymbolNode;
	}
}/*end of method addSymbol*/

/*
printSymbols is a function used for debug only - it prints a symbols list
*/
void printSymbols(SymbolNode* symbolsHead)
{
	if(symbolsHead != NULL)
	{
		printf("\nsymbols list:");
		while(symbolsHead != NULL)
		{
			printf("\n%s %d %c %c %c %c", symbolsHead->name, symbolsHead->value, symbolsHead->attrs[0], symbolsHead->attrs[1], symbolsHead->attrs[2], symbolsHead->attrs[3]);
			symbolsHead = symbolsHead->next;
		}
	}
}/*end of method printSymbols*/

/*
createExternsList is a function that gets a symbols list, for each extern symbol it will find in which address in the source code it is used, for each show, it will add an extern node to the extern list.
the function parameters:
externsHead (ExternNode*) - the externs list head
symbolsHead (SymbolNode*) - the symbols list head 
instructionsHead (SourceCodeNode*) - instructions list head 
*/
void createExternsList(ExternNode* externsHead, SymbolNode* symbolsHead, SourceCodeNode* instructionsHead)
{
	SourceCodeNode* tempSourceCodeHead = NULL;/*temp source code pointer to each time from start of list*/
	StringNode* stringList = NULL;/*string list head*/
	Boolean exist = FALSE;
	while(symbolsHead != NULL)/*go over the symbols list*/
	{
		tempSourceCodeHead = instructionsHead;/*start from head of instructions*/
		if(symbolsHead->attrs[1] == '1')/*if current symbol is extern*/
		{
			while(tempSourceCodeHead != NULL)/*go over the instructions list and search where he shows up*/
			{
				stringList = newStringNode();
				splitStr(stringList, tempSourceCodeHead->sourceCode);/*split the current line to string nodes by white spaces and commas*/
				exist = FALSE;/*for each line search until the symbol exists in the line once*/
				while(stringList != NULL && exist  == FALSE)
				{
					if(strcmp(stringList->str, symbolsHead->name) == 0)
					{
						exist = TRUE;
						addExternNode(externsHead, symbolsHead->name, tempSourceCodeHead->address);/*add extern node with the current address*/
					}
					stringList = stringList->next;
				}
				destroyStringList(stringList);/*free allocated memory*/
				tempSourceCodeHead = tempSourceCodeHead->next;
			}
		}
		symbolsHead = symbolsHead->next;
	}
}/*end of method createExternsList*/

/*
addExternNode is a function that create new extern node and add it to end of the list
the function parameter:
externsHead (ExternNode*) - head of the list 
name (char[]) - new extern name
address (int) - new extern address
*/
void addExternNode(ExternNode* externsHead, char name[], int address)
{
	ExternNode* temp = NULL;
	if(externsHead->address == 0)/*if this is the first node*/
	{
		externsHead->address = address;
		strcpy(externsHead->name, name);
	}else{
		while(externsHead->next != NULL)
		{
			externsHead = externsHead->next;
		}
		temp = newExternNode();
		if(temp != NULL)
		{
			temp->address = address;
			strcpy(temp->name, name);
			externsHead->next = temp;
		}
	}
}/*end of method addExternNode*/

/*
addExtern that gets a strings list with an extern command in it. and add the symbol to the symbols list with extern attribute.
the function does some validation if success returns 0, else its error.
the function parameters:
stringList (StringNode*) - strings list head
symbolsHead (SymbolNode*) - symbols list head
*/
int addExtern(StringNode* stringList, SymbolNode* symbolsHead)
{
	while(strcmp(stringList->str, ".extern") != 0)/*loop until extern command*/
	{
		stringList = stringList->next;
	}
	if(stringList->next == NULL)/*if no operands after extern command*/
	{
		return 12;
	}
	stringList = stringList->next;
	if(stringList->next != NULL)/*after the command there should be only 1 more operand and no more*/
	{
		return 6;
	}
	if(isValidLabel(stringList->str) == TRUE)/*check the label is valid*/
	{
		if(isSymbolExist(stringList->str, symbolsHead) == TRUE)
		{
			if(getSymbolAttr(symbolsHead, stringList->str, 1) == '0')/*if the label exist not as an extern its error*/
			{
				return 2;
			}
		}else{
			addSymbol(0, symbolsHead, stringList->str, 1);/*if not exist - add it*/
		}
	}else{
		return 17;
	}
	return 0;/*if we got here - success*/
}/*end of method addExtern*/

/*
getSymbolValue is a function that gets a symbols name and returns there value(address).
the function parameters:
symbolsHead (SymbolNode*) - the head of the symbols list 
symbol (char*) - the symbol name

the function return there value as integer.
*/
int getSymbolValue(SymbolNode* symbolsHead, char* symbol)
{
	while(symbolsHead != NULL)
	{
		if(strcmp(symbolsHead->name, symbol) == 0)
		{
			return symbolsHead->value;
		}
		symbolsHead = symbolsHead->next;
	}
	return 0;
}/*end of method getSymbolValue*/

/*
addSourceCodeNode is a function that create new source code node and add it to end of the list
the function parameter:
head (SourceCodeNode*) - head of the list 
tempAddress (int) - new node address
tempSourceCode (char[]) - the line as a string
currentMachineCode (char[4]) - the binary translated line
tempSize (int) - new node size
tempIsCode (Boolean) - is a code node or data node
*/
void addSourceCodeNode(SourceCodeNode* head, int tempAddress, char tempSourceCode[], char currentMachineCode[], int tempSize, Boolean tempIsCode)
{
	SourceCodeNode* tempSourceCodeNode;
	int i = 0;
	if(head->size != 0)/*if it is not the first node - create a new one*/
	{
		while(head->next != NULL)
		{
			head = head->next;
		}
		tempSourceCodeNode = newSourceCodeNode();
		head->next = tempSourceCodeNode;
		head = head->next;
	}
	strcpy(head->sourceCode, tempSourceCode);
	head->address = tempAddress;
	head->isCode = tempIsCode;
	head->size = tempSize;
	while(i < tempSize)/*copy machine code chars*/
	{
		head->machineCode[i] = currentMachineCode[i];
		i++;
	}
}/*end of method addSourceCodeNode*/

/*
getErrorType is a function that get error type as integer and returns the string for this error.
the errors are in a string array and the error type is the index, but using this loop we can avoid exceeding the array by mistake.
the function parameters:
errorType (int) - the error index in errors array

the function retruns the string for the error
*/
char* getErrorType(int errorType)
{
	int i = 0;
	while(i < ERRORS_NUMBER && i < errorType)
	{
		i++;
	}
	return errors[i];
}/*end of method getErrorType*/

/*
printErrors is function that gets a errors linked list and prints the errors in the list one by one
the function parameters:
errorsHead (ErrorNode*) - the errores linked list head
fileName (char*) - the file name
*/
void printErrors(ErrorNode* errorsHead, char* fileName)
{
	/*for each file it will print the file name and then the errors, line numbers and actual line text where the error occurred*/
	printf("\n\nerrors in file: %s", fileName);
	while(errorsHead != NULL)
	{
		if(errorsHead->lineNumber != 0)
		{
			printf("\nline %d: %s", errorsHead->lineNumber, getErrorType(errorsHead->errorType));
			printf("\n\t%s", errorsHead->lineTxt);
		}
		errorsHead = errorsHead->next;
	}
	printf("\n\n");
}/*end of method printErrors*/

/*
addErrorToList is a function that create new error node and add it to end of the list
the function parameter:
errorsHead (ErrorNode*) - head of the list 
lineNumber (int) - the file line number
errorType (int) - error index
currentLine (char*) - the actual line as a string
*/
void addErrorToList(ErrorNode* errorsHead, int lineNumber, int errorType, char* currentLine)
{
	ErrorNode* temp;
	if(errorsHead->lineNumber == 0)/*if this is the first node*/
	{
		errorsHead->lineNumber = lineNumber;
		errorsHead->errorType = errorType;
		errorsHead->next = NULL;
		strcpy(errorsHead->lineTxt, currentLine);
	}else {
		while(errorsHead->next != NULL)
		{
			errorsHead = errorsHead->next;
		}
		temp = newErrorNode();
		if(temp != NULL)
		{
			temp->lineNumber = lineNumber;
			temp->errorType = errorType;
			temp->next = NULL;
			strcpy(temp->lineTxt, currentLine);
			errorsHead->next = temp;
		}
	}
}/*end of method addErrorToList*/

/*
printSource is function used for debugging, it prints source code list nodes one by one
*/
void printSource(SourceCodeNode* source)
{
	int sizeIndex = 0;
	int i = 0;
	while(source != NULL)
	{
		printf("\n%d %d %s ", source->address, source->size, source->sourceCode);
		sizeIndex = 0;
		while(sizeIndex < source->size)
		{
			i = 0;
			while(i < BITS_IN_BYTE)
			{
				if((source->machineCode[sizeIndex] & (1u << i)) != 0)
				{
					printf("1");
				}else{
					printf("0");
				}
				i++;
			}
			sizeIndex++;
		}
		source = source->next;
	}
}/*end of method printSource*/

/*
setSymbolAttr is a function that gets a symbol name, symbols list head, and an attribute index and set the attribute for the symbol to '1'.
the function parameters:
symbolsHead (SymbolNode) - the head of the list
symbol (char*) - symbol name
attr (int) - attribute index
*/
void setSymbolAttr(SymbolNode* symbolsHead, char* symbol, int attr)
{
	while(strcmp(symbolsHead->name, symbol) != 0)
	{
		symbolsHead = symbolsHead->next;
	}
	symbolsHead->attrs[attr] = '1';
}/*end of method setSymbolAttr*/

/*
addEntry is a function that add an entry attribute for a symbol in a list (entry has to be exist as code or data first)
the function parameters:
stringList (StringNode*) - the split string where entry is one of them
symbolsHead (SymbolNode*) - the head of symbols list
*/
int addEntry(StringNode* stringList, SymbolNode* symbolsHead)
{
	if(stringList == NULL)/*check string is ok*/
	{
		return ERRORS_NUMBER;
	}
	while(strcmp(stringList->str, ".entry") != 0)/*go over the string nodes until entry*/
	{
		stringList = stringList->next;
	}
	if(stringList->next == NULL)/*if no content after the command*/
	{
		return 12;
	}
	stringList = stringList->next;
	if(stringList->next != NULL)/*if there is more content after the symbol*/
	{
		return 6;
	}
	if(isValidLabel(stringList->str) == FALSE)/*check validity*/
	{
		return 17;
	}
	if(isSymbolExist(stringList->str, symbolsHead) == FALSE)/*symbol has to be exists first for entry*/
	{
		return 23;
	}
	if(getSymbolAttr(symbolsHead, stringList->str, 1) == '1')/*check symbol is not extern*/
	{
		return 24;
	}
	setSymbolAttr(symbolsHead, stringList->str, 0);/*set symbol as entry*/
	return 0;
}/*end of method addEntry*/

/*
addUnfinishedLine is a function that creates new unfinished line node and add it to end of a list
the function parameters:
unFinishedHead (UnfinishedLineNode*) - the head of the list 
address (int) - address in the instructions list
lineNumber (int) - line number in the source file
*/
void addUnfinishedLine(UnfinishedLineNode* unFinishedHead, int address, int lineNumber)
{
	UnfinishedLineNode* temp;
	if(unFinishedHead->address == 0)/*if this is the first node*/
	{
		unFinishedHead->address = address;
		unFinishedHead->lineNumber = lineNumber;
	}else{
		while(unFinishedHead->next != NULL)
		{
			unFinishedHead = unFinishedHead->next;
		}
		temp = newUnfinishedLineNode();
		temp->address = address;
		temp->lineNumber = lineNumber;
		unFinishedHead->next = temp;
	}
}/*end of method addUnfinishedLine*/
