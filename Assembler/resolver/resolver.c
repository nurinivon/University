#include "./resolver.h" 

/*
this is file includes the function regarding resolving a line in the source file and resolving unfinishied lines and data updates.
*/

/* global variables (defined in ("app_utils.c"))*/
extern char* instructions[];
extern char commandTypes[];
extern int opcodes[];
extern int functs[];
extern int subCommandTypes[];
extern char* guidances[];

/*
mergeSourceCode is a function that get to source code lists and connect them. it returns the head of the first list that is now common head
the function parameters:
firstSource (SourceCodeNode*) - the head of the first list
secondSource (SourceCodeNode*) - the head of the second list

the function returns the head of the first list that is now common head
*/
SourceCodeNode* mergeSourceCode(SourceCodeNode* firstSource, SourceCodeNode* secondSource)
{
	SourceCodeNode* head = firstSource;
	while(firstSource->next != NULL)
	{
		firstSource = firstSource->next;
	}
	firstSource->next = secondSource;
	return head;
}/*end of method mergeSourceCode*/

/*
getLineType is a function that get a splited line and returns is type (COMMENT, UNDEFINED, GUIDANCE, INSTRUCTION).
the function parameters:
stringList (StringNode*) - the line splited to nodes

the function returns the line type
*/
LineType getLineType(StringNode* stringList)
{
	LineType temp = COMMENT;
	if(stringList->str[0] != ';')
	{
		temp = UNDEFINED;
		while(stringList != NULL && temp == UNDEFINED)
		{
			if(isInstructionWord(stringList->str) == TRUE)
			{
				return INSTRUCTION;
			}else if(isGuidanceWord(stringList->str) == TRUE)
			{
				return GUIDANCE;
			}
			stringList = stringList->next;
		}
	}
	return temp;
}/*end of method getLineType*/

/*
getCommandFromLine is a function that get a splited line and found the command in its nodes. the command string will be stored in the destination parameter.
the function parameters:
stringListHead (StringNode*) - the line splited to nodes
type (LineType)
dest (char[]) - where to store the command string
*/
void getCommandFromLine(StringNode* stringListHead, LineType type, char dest[])
{
	while(stringListHead != NULL)
	{
		if(type == GUIDANCE)
		{
			if(isGuidanceWord(stringListHead->str) == TRUE)
			{
				strcpy(dest, stringListHead->str);
				return;
			}
		}else if(type == INSTRUCTION){
			if(isInstructionWord(stringListHead->str) == TRUE)
			{
				strcpy(dest, stringListHead->str);
				return;
			}
		}
		stringListHead = stringListHead->next;
	}
	dest[0] = '\0';
}/*end of method getCommandFromLine*/

/*
addData is the function to resolve data related lines.
the function parameters:
stringListHead (StringNode*) - the line splited to nodes
dataListHead (SourceCodeNode*) - the data list head managed in the main program
DCAdress (int*) - the pointer to the data counter managed in the main program
currentCommand (char*) - the specific command string
lineStr (char*) - the line as string before splited (to add to the data list)

the function returns an integer. 0 - success, otherwish - error
*/
int addData(StringNode* stringListHead, SourceCodeNode* dataListHead, int* DCAdress, char* currentCommand, char* lineStr)
{
	int charIndex = 0;/*index to move over strings*/
	int currentOperand;/*current integer operands*/
	char currentMachineCode[4];/*the machine code (binary) that will be added to the data list*/
	/*flags to see that we got appropriate operands*/
	Boolean expectingComma = FALSE;
	Boolean firstOperand = TRUE;
	if(strcmp(currentCommand, ".asciz") == 0) {
		while(strcmp(stringListHead->str, ".asciz") != 0)/*loop to get to the command node (operands shold be after it)*/
		{
			stringListHead = stringListHead->next;
		}
		if(stringListHead->next == NULL)/*if no operands after command*/
		{
			return 8;
		}
		stringListHead = stringListHead->next;
		if(stringListHead->next != NULL)/*in ".asciz" the string should be the last operand*/
		{
			return 6;
		}
		if(stringListHead->str[0] != '\"')/*the string should start with doucle quotes (")*/
		{
			return 9;
		}
		/*the following loop checks that the string is also closed with double quotes (")*/
		charIndex = 1;
		while(charIndex < strlen(stringListHead->str) && stringListHead->str[charIndex] != '\"')
		{
			charIndex++;
		}
		if(stringListHead->str[charIndex] != '\"')
		{
			return 10;
		}
		if(stringListHead->str[charIndex + 1] != '\0')/*check there is no further content after string closed*/
		{
			return 11;
		}
		/*
		in the following loop we will go over the chars in the string and for each, we will add it to the data list. the data counter is growing in 1 each time.
		the first char, will hold the line string in the source code list
		*/
		charIndex = 1;/*[0] is double quote (")*/
		while(stringListHead->str[charIndex] != '\"')
		{
			currentMachineCode[0] = stringListHead->str[charIndex];
			if(charIndex == 1)
			{
				addSourceCodeNode(dataListHead, *DCAdress, lineStr, currentMachineCode ,1, FALSE);
			}else{
				addSourceCodeNode(dataListHead, *DCAdress, "", currentMachineCode ,1, FALSE);
			}
			charIndex++;
			(*DCAdress)++;
		}
		/*add string closer to the data list after end of string*/
		currentMachineCode[0] = '\0';
		addSourceCodeNode(dataListHead, *DCAdress, "", currentMachineCode ,1, FALSE);
		(*DCAdress)++;
	}else if(strcmp(currentCommand, ".dw") == 0) {
		while(strcmp(stringListHead->str, ".dw") != 0)/*loop to get to the command node (operands shold be after it)*/
		{
			stringListHead = stringListHead->next;
		}
		if(stringListHead->next == NULL)/*if there are no nodes after command*/
		{
			return 12;
		}
		stringListHead = stringListHead->next;
		expectingComma = FALSE;/*first operand shouldnt seperate with comma*/
		while(stringListHead != NULL)/*loop over the nodes of the string*/
		{
			if(expectingComma == TRUE)
			{
				if(stringListHead->str[0] != ',')/*expecting comma*/
				{
					return 13;
				}
				expectingComma = FALSE;/*switch flag*/
				if(stringListHead->next == NULL)/*a command could not end with comma*/
				{
					return 15;
				}
			}else{
				if(isNumber(stringListHead->str) == FALSE)/*if not expected comma - expected number*/
				{
					return 14;
				}
				currentOperand = atoi(stringListHead->str);/*string to int*/
				if(currentOperand > INT_MAX || currentOperand < INT_MIN)/*check number range*/
				{
					return 16;
				}
				intToChars(currentOperand, currentMachineCode);/*translate int to binary chars*/
				if(firstOperand == TRUE)/*first operand will be added with the line string to the data list*/
				{
					firstOperand = FALSE;
					addSourceCodeNode(dataListHead, *DCAdress, lineStr, currentMachineCode ,4, FALSE);
				}else{
					addSourceCodeNode(dataListHead, *DCAdress, "", currentMachineCode ,4, FALSE);
				}
				/*".dw" operands size is 4*/
				(*DCAdress) += 4;
				expectingComma = TRUE;/*switch flag*/
			}
			stringListHead = stringListHead->next;
		}
	}else if(strcmp(currentCommand, ".dh") == 0) {
		while(strcmp(stringListHead->str, ".dh") != 0)/*loop to get to the command node (operands shold be after it)*/
		{
			stringListHead = stringListHead->next;
		}
		if(stringListHead->next == NULL)/*if there are no nodes after command*/
		{
			return 12;
		}
		stringListHead = stringListHead->next;
		expectingComma = FALSE;/*first operand shouldnt seperate with comma*/
		while(stringListHead != NULL)/*loop over the nodes of the string*/
		{
			if(expectingComma == TRUE)
			{
				if(stringListHead->str[0] != ',')/*expecting comma*/
				{
					return 13;
				}
				expectingComma = FALSE;/*switch flag*/
				if(stringListHead->next == NULL)/*a command could not end with comma*/
				{
					return 15;
				}
			}else{
				if(isNumber(stringListHead->str) == FALSE)/*if not expected comma - expected number*/
				{
					return 14;
				}
				currentOperand = atoi(stringListHead->str);/*string to int*/
				if(currentOperand > MAX_HALF_WORD_NUMBER || currentOperand < MIN_HALF_WORD_NUMBER)/*check number range*/
				{
					return 16;
				}
				intToChars(currentOperand, currentMachineCode);/*translate int to binary chars*/
				if(firstOperand == TRUE)/*first operand will be added with the line string to the data list*/
				{
					firstOperand = FALSE;
					addSourceCodeNode(dataListHead, *DCAdress, lineStr, currentMachineCode ,2, FALSE);
				}else{
					addSourceCodeNode(dataListHead, *DCAdress, "", currentMachineCode ,2, FALSE);
				}
				/*".dh" operands size is 2*/
				(*DCAdress) += 2;
				expectingComma = TRUE;/*switch flag*/
			}
			stringListHead = stringListHead->next;
		}
	}else if(strcmp(currentCommand, ".db") == 0) {
		while(strcmp(stringListHead->str, ".db") != 0)/*loop to get to the command node (operands shold be after it)*/
		{
			stringListHead = stringListHead->next;
		}
		if(stringListHead->next == NULL)/*if there are no nodes after command*/
		{
			return 12;
		}
		stringListHead = stringListHead->next;
		expectingComma = FALSE;/*first operand shouldnt seperate with comma*/
		while(stringListHead != NULL)/*loop over the nodes of the string*/
		{
			if(expectingComma == TRUE)
			{
				if(stringListHead->str[0] != ',')/*expecting comma*/
				{
					return 13;
				}
				expectingComma = FALSE;/*switch flag*/
				if(stringListHead->next == NULL)/*a command could not end with comma*/
				{
					return 15;
				}
			}else{
				if(isNumber(stringListHead->str) == FALSE)/*if not expected comma - expected number*/
				{
					return 14;
				}
				currentOperand = atoi(stringListHead->str);/*string to int*/
				if(currentOperand > MAX_BYTE_NUMBER || currentOperand < MIN_BYTE_NUMBER)/*check number range*/
				{
					return 16;
				}
				intToChars(currentOperand, currentMachineCode);/*translate int to binary chars*/
				if(firstOperand == TRUE)/*first operand will be added with the line string to the data list*/
				{
					firstOperand = FALSE;
					addSourceCodeNode(dataListHead, *DCAdress, lineStr, currentMachineCode , 1, FALSE);
				}else{
					addSourceCodeNode(dataListHead, *DCAdress, "", currentMachineCode , 1, FALSE);
				}
				/*".dh" operands size is 2*/
				(*DCAdress) += 1;
				expectingComma = TRUE;/*switch flag*/
			}
			stringListHead = stringListHead->next;
		}
	}else{
		return ERRORS_NUMBER;/*unrecognized command*/
	}
	return 0;/*if got here - success*/
}/*end of method addData*/

/*
instructionToMachineCode is a function that get all the variables realted to instructions command and resolving the machine code according to the requirements. the result is stored int currentMachineCode (this is the destination).
the function parameters:
currentMachineCode (char*) - a char[4] this is the destination where the binary translation is stored
commandIndex (int) - the index of the specifc command in the commands global array
commandType (char) - 'R'/'I'/'J'
int variables: rs,rt,rd,immed,address
reg (char)
*/
void instructionToMachineCode(char* currentMachineCode, int commandIndex, char commandType, int rs, int rt, int rd, int immed, int address, char reg)
{
	int opcode;/*the command opcode*/
	int funct;/*the command funct number*/
	int i = 0;/*bits index*/
	/*init current machine code to 32 '0' bits*/
	currentMachineCode[0] = '\0';
	currentMachineCode[1] = '\0';
	currentMachineCode[2] = '\0';
	currentMachineCode[3] = '\0';
	opcode = opcodes[commandIndex];/*get opcode by index*/
	funct = functs[commandIndex];/*get funct by index*/
	/*
	the following loop is going over 32 bits
	for each command type there is predefined structure of the bits and how many bits each variable is allowed to use.
	so for each bit we will check the appropriate variable bit, if its not '0' we will init the current bit also.
			R
	0-6	6-10	11-15	16-20	21-25	26-31
	no use	funct	rd	rt	rs	opcode

			I
	0-15	16-20	21-25	26-30
	immed	rt	rs	opcode

			J
	0-24	25	26-31
	address	reg	opcode
	*/
	while(i < BITS_IN_WORD)
	{
		if(commandType == 'R')
		{
			if(i < 6)
			{
			}else if(i < 11){
				if((funct & (1u << (i - 6))) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}else if(i < 16){
				if((rd & (1u << (i - 11))) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}else if(i < 21){
				if((rt & (1u << (i - 16))) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}else if(i < 26){
				if((rs & (1u << (i - 21))) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}else{
				if((opcode & (1u << (i - 26))) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}
		}else if(commandType == 'I'){
			if(i < 16){
				if((immed & (1u << i)) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}else if(i < 21){
				if((rt & (1u << (i - 16))) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}else if(i < 26){
				if((rs & (1u << (i - 21))) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}else{
				if((opcode & (1u << (i - 26))) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}
		}else if(commandType == 'J'){
			if(i < 25)
			{
				if((address & (1u << i)) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}else if(i == 25){
				if(reg != '0')
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}else{
				if((opcode & (1u << (i - 26))) != 0)
				{
					currentMachineCode[(int) i / BITS_IN_BYTE] |= (1u << (i % BITS_IN_BYTE));
				}
			}
		}
		i++;
	}
}/*end of method instructionToMachineCode*/

/*
addInstruction is the function to resolve code related lines.
the function parameters:
stringListHead (StringNode*) - the line splited to nodes
instructionsHead (SourceCodeNode*) - the instructions list head managed in the main program
ICAddress (int*) - the pointer to the instructions counter managed in the main program
currentCommand (char*) - the specific command string
currentLine (char*) - the line as string before splited (to add to the data list)
symbolsHead (SymbolNode*) - the symbols list head from main program, instructions can use external/internal labels and they adressess needed to resolve the command
unFinishedHead (UnfinishedLineNode*) - the unfinished lines list head from main program, there sometimes when a command could not be fully resolved and will be handled later on.
lineNumber (int) - the current line number in the source file (needed for unfinished lines).

the function returns an integer. 0 - success, otherwish - error
*/
int addInstruction(StringNode* stringList, SourceCodeNode* instructionsHead, int* ICAddress, char* currentCommand, char* currentLine, SymbolNode* symbolsHead, UnfinishedLineNode* unFinishedHead, int lineNumber)
{
	/*the variables that will use for the machine code translation*/
	char commandType;
	int rs;
	int rt;
	int rd;
	int immed;
	int address;
	char reg;
	int subCommandType;
	int tempInteger;/*temp int for integers operands*/
	int instructionIndex = 0;
	/*flags for checking the validations*/
	Boolean expectingComma = FALSE;
	Boolean foundInstruction = FALSE;
	Boolean foundRs = FALSE;
	Boolean foundRt = FALSE;
	Boolean foundImmed = FALSE;
	Boolean foundAll = FALSE;
	char currentMachineCode[4];
	while(instructionIndex < INSTRUCTIONS_NUMBER)/*loop to find the command index*/
	{
		if(strcmp(currentCommand, instructions[instructionIndex]) == 0)
		{
			foundInstruction = TRUE;
			break;
		}
		instructionIndex++;
	}
	if(foundInstruction == FALSE)/*if not found command*/
	{
		return 5;
	}
	commandType = commandTypes[instructionIndex];/*get command type by index*/
	while(strcmp(stringList->str, currentCommand) != 0)/*loop until we get to the command node (operands should after it)*/
	{
		stringList = stringList->next;
	}
	if(strcmp(currentCommand, "stop") == 0)/*if the command is "stop" it should not have operands after*/
	{
		if(stringList->next != NULL)
		{
			return 6;
		}
		/*the variables for stop command*/
		rs = 0;
		rt = 0;
		rd = 0;
		immed = 0;
		address = 0;
		reg = '0';
		foundAll = TRUE;
	}else{
		if(stringList->next == NULL)/*if there are no operands after command*/
		{
			return 12;
		}
		stringList = stringList->next;
		expectingComma = FALSE;
		foundRs = FALSE;
		foundRt = FALSE;
		foundImmed = FALSE;
		foundAll = FALSE;
		rs = 0;
		rt = 0;
		rd = 0;
		immed = 0;
		address = 0;
		reg = '0';
		subCommandType = subCommandTypes[instructionIndex];/*get sub command type by index*/
		/*
		in the following loop we  will go over the string nodes. an instruction sould have some operands after it, the first one should be seperated with ',', the rest should. for each command type and sub command type there is a specific arguments we expect to get, and they should meet some requirements, if every thing is well we will mark - foundALL - TRUE. if there is an error the function will stop with the error type. if there are lines needed to resolve later, we will add unfinished line.
		*/
		while(stringList != NULL)/*loop over the string nodes*/
		{
			if(expectingComma == FALSE)
			{
				if(stringList->str[0] == ',')/*unexpected comma*/
				{
					return 19;
				}
				if(commandType == 'R')
				{
					/*in 'R' all operands should be registers*/
					if(isRegister(stringList->str) == FALSE)
					{
						return 20;
					}
					/*change '$' to '+' for int transition*/
					stringList->str[0] = '+';
					tempInteger = atoi(stringList->str);
					if(tempInteger > MAX_REGISTER_INDEX)/*MAX_REGISTER_INDEX = 31*/
					{
						return 21;
					}
					if(subCommandType == 1)
					{
						/*for sub 1 - the order is: rs, rt, rd*/
						if(foundRs == FALSE)
						{
							rs = tempInteger;
							foundRs = TRUE;
						}else if(foundRt == FALSE){
							rt = tempInteger;
							foundRt = TRUE;
						}else{
							rd = tempInteger;
							foundAll = TRUE;
							if(stringList->next != NULL)/*if there is more content after last operand*/
							{
								return 6;
							}
						}
					}else{
						/*for sub 2 - the order is: rd, rs*/
						if(foundRs == FALSE)
						{
							rs = tempInteger;
							foundRs = TRUE;
						}else{
							rd = tempInteger;
							foundAll = TRUE;
							if(stringList->next != NULL)/*if there is more content after last operand*/
							{
								return 6;
							}
						}
					}
				}else if(commandType == 'I'){
					if(subCommandType == 1)
					{
						/*for sub 1 - the order is: rs, immed, rt*/
						if(foundRs == FALSE)
						{
							if(isRegister(stringList->str) == FALSE)
							{
								return 20;
							}
							stringList->str[0] = '+';
							tempInteger = atoi(stringList->str);
							if(tempInteger > MAX_REGISTER_INDEX)
							{
								return 21;
							}
							rs = tempInteger;
							foundRs = TRUE;
						}else if(foundImmed == FALSE){
							if(isNumber(stringList->str) == FALSE)/*imeed should be an actual number*/
							{
								return 14;
							}
							tempInteger = atoi(stringList->str);
							if(tempInteger > MAX_HALF_WORD_NUMBER)/*immed is 16 bits*/
							{
								return 16;
							}
							immed = tempInteger;
							foundImmed = TRUE;
						}else{/*expecting rt*/
							if(isRegister(stringList->str) == FALSE)
							{
								return 20;
							}
							stringList->str[0] = '+';
							tempInteger = atoi(stringList->str);
							if(tempInteger > MAX_REGISTER_INDEX)
							{
								return 21;
							}
							rt = tempInteger;
							foundRt = TRUE;
							foundAll = TRUE;
							if(stringList->next != NULL)/*if there is more content after last operand*/
							{
								return 6;
							}
						}
					}else{
						/*for sub 2 the order is - rs, rt, immed (label). labels will resolved at the end of assembler*/
						if(foundRs == FALSE)
						{
							if(isRegister(stringList->str) == FALSE)
							{
								return 20;
							}
							stringList->str[0] = '+';
							tempInteger = atoi(stringList->str);
							if(tempInteger > MAX_REGISTER_INDEX)
							{
								return 21;
							}
							rs = tempInteger;
							foundRs = TRUE;
						}else if(foundRt == FALSE){
							if(isRegister(stringList->str) == FALSE)
							{
								return 20;
							}
							stringList->str[0] = '+';
							tempInteger = atoi(stringList->str);
							if(tempInteger > MAX_REGISTER_INDEX)
							{
								return 21;
							}
							rt = tempInteger;
							foundRt = TRUE;
						}else{
							/*should be a label, the label could be only inernal*/
							if(isValidLabel(stringList->str) == FALSE)
							{
								return 17;
							}
							/*if the label doesnt exist we will mark for later*/
							if(isSymbolExist(stringList->str, symbolsHead) == TRUE)
							{
								/*if the label exists and its extenal - error*/
								if(getSymbolAttr(symbolsHead, stringList->str, 1) == '1')
								{
									return 22;
								}else{
									/*mark for later*/
									addUnfinishedLine(unFinishedHead, (*ICAddress), lineNumber);
								}
							}else{
								/*mark for later*/
								addUnfinishedLine(unFinishedHead, (*ICAddress), lineNumber);
							}
							foundImmed = TRUE;
							foundAll = TRUE;
							if(stringList->next != NULL)
							{
								return 6;
							}
						}
					}
				}else{/*command type is J*/
					if(strcmp(currentCommand, "jmp") == 0)
					{
						/*for "jmp" the order is : label / register*/
						if(isRegister(stringList->str) == TRUE)
						{
							stringList->str[0] = '+';
							tempInteger = atoi(stringList->str);
							if(tempInteger > MAX_REGISTER_INDEX)
							{
								return 21;
							}
							address = tempInteger;
							reg = '1';/*if its a register so reg is '1'*/
							foundAll = TRUE;
							if(stringList->next != NULL)
							{
								return 6;
							}
						}else{
							/*if this is not a register and not a valid label - error*/
							if(isValidLabel(stringList->str) == FALSE)
							{
								return 17;
							}
							/*if the symbol doesnt exists we will mark for later*/
							if(isSymbolExist(stringList->str, symbolsHead) == TRUE)
							{
								/*if the symboll is external - is address is 0 - no need to mark for later*/
								if(getSymbolAttr(symbolsHead, stringList->str, 1) == '0')
								{
									/*mark for later*/
									addUnfinishedLine(unFinishedHead, (*ICAddress), lineNumber);
								}
							}else{
								/*mark for later*/
								addUnfinishedLine(unFinishedHead, (*ICAddress), lineNumber);
							}
							foundAll = TRUE;
							if(stringList->next != NULL)
							{
								return 6;
							}
						}
					}else{/*command is "la" or "call"*/
						/*for "la" or "call" the order is : label*/
						if(isValidLabel(stringList->str) == FALSE)
						{
							return 17;
						}
						if(isSymbolExist(stringList->str, symbolsHead) == TRUE)
						{
							if(getSymbolAttr(symbolsHead, stringList->str, 1) == '0')
							{
								/*mark for later*/
								addUnfinishedLine(unFinishedHead, (*ICAddress), lineNumber);
							}
						}else{
							/*mark for later*/
							addUnfinishedLine(unFinishedHead, (*ICAddress), lineNumber);
						}
						address = 0;
						reg = '0';/*in "la" or "call" reg is always '0'*/
						foundAll = TRUE;
						if(stringList->next != NULL)
						{
							return 6;
						}
					}
				}
				expectingComma = TRUE;/*switch flag*/
			}else{
				/*expecting comma*/
				if(stringList->str[0] != ',')
				{
					/*missing comma*/
					return 13;
				}
				if(stringList->next == NULL)/*command could not end with comma*/
				{
					return 12;
				}
				expectingComma = FALSE;/*switch flag*/
			}
			stringList = stringList->next;
		}
	}
	if(foundAll == TRUE)/*if found all we will translate the variables to machine code and add to the instructions list*/
	{
		instructionToMachineCode(currentMachineCode, instructionIndex, commandType, rs, rt, rd, immed, address, reg);
		addSourceCodeNode(instructionsHead, (*ICAddress), currentLine, currentMachineCode , 4, TRUE);
		(*ICAddress) += 4;/*instruction line size is always 4 byts*/
	}else{/*missing operands*/
		return 12;
	}
	return 0;/*if we got here with no errors - success*/
}/*end of method addInstruction*/

/*
updateDataList is a function to fix the data list address. the data will be stored after the code, so at the end of the 1st file loop we will update the data adresses with the bigest instruction address. also we will update the value in the symbols list.
the function parameters:
dataHead (SourceCodeNode*) - the data list head
symbolsHead (SymbolNode*) - the symbols list head
IC (int) - instructions counter
*/
void updateDataList(SourceCodeNode* dataHead, SymbolNode* symbolsHead, int IC)
{
	/*update addresses in all data list*/
	while(dataHead != NULL)
	{
		dataHead->address += IC;
		dataHead = dataHead->next;
	}
	/*update address in the symbols list where the symbol is data*/
	while(symbolsHead != NULL)
	{
		if(symbolsHead->attrs[2] == '1')
		{
			symbolsHead->value += IC;
		}
		symbolsHead = symbolsHead->next;
	}
}/*end of method updateDataList*/

/*
resolveUnfinishedLines is a function that going over unfinished lines list and resolve the missing data. in this assembler a labels could be used before they are defined so we need to resolve this after we collected all the code and the data.
the function parameters:
unFinishedHead (UnfinishedLineNode*) - the head of the unfinished lines list from main program
symbolsHead (SymbolNode*) - the head of the symbols list from main program
instructionsHead (SourceCodeNode*) - the head of the instructions list from main program
errorsHead (ErrorNode*) - the head of the errors list from main program
ECAdress (int*) - the address of the error counter from the main program
*/
void resolveUnfinishedLines(UnfinishedLineNode* unFinishedHead, SymbolNode* symbolsHead, SourceCodeNode* instructionsHead, ErrorNode* errorsHead, int* ECAdress)
{
	char currentCommand[MAX_COMMAND_SIZE];	
	StringNode* stringList = NULL;
	int instructionIndex = 0;
	int commandType;
	/*flags to check validations*/
	Boolean foundInstruction = FALSE;
	Boolean expectingComma = FALSE;
	Boolean foundRs = FALSE;
	Boolean foundRt = FALSE;
	Boolean foundAll = FALSE;
	int tempInteger;/*temp int for numbers operands*/
	/*int variables to be translated*/
	int rs = 0;
	int rt = 0;
	int rd = 0;
	int immed = 0;
	int address = 0;
	char reg = '0';/*reg variable for translation*/
	if(unFinishedHead != NULL && unFinishedHead->address > 0)/*if there are unfinished lines*/
	{
		while(unFinishedHead != NULL)/*loop over unfinished lines list*/
		{
			while(instructionsHead->address != unFinishedHead->address)/*loop to find the match instruction node*/
			{
				instructionsHead = instructionsHead->next;
			}
			stringList = newStringNode();/*create string list*/
			if(stringList == NULL)
			{
				(*ECAdress)++;
				addErrorToList(errorsHead, unFinishedHead->lineNumber, 25, instructionsHead->sourceCode);
				goto skipUnfinishedLine;
			}
			splitStr(stringList, instructionsHead->sourceCode);/*split the line to string nodes by white spaces and commas*/
			getCommandFromLine(stringList, INSTRUCTION, currentCommand);/*get the specific command string*/
			/*loop to find the instruction index in the instructions array*/
			instructionIndex = 0;
			while(instructionIndex < INSTRUCTIONS_NUMBER)
			{
				if(strcmp(currentCommand, instructions[instructionIndex]) == 0)
				{
					foundInstruction = TRUE;
					break;
				}
				instructionIndex++;
			}
			if(foundInstruction == FALSE)/*if not found*/
			{
				(*ECAdress)++;
				addErrorToList(errorsHead, unFinishedHead->lineNumber, 5, instructionsHead->sourceCode);
				goto skipUnfinishedLine;
			}
			commandType = commandTypes[instructionIndex];/*get command type by index*/
			while(strcmp(stringList->str, currentCommand) != 0)/*loop to get to the command node*/
			{
				stringList = stringList->next;
			}
			if(stringList->next == NULL)/*if there are no operands after the command*/
			{
				(*ECAdress)++;
				addErrorToList(errorsHead, unFinishedHead->lineNumber, 12, instructionsHead->sourceCode);
				goto skipUnfinishedLine;
			}
			stringList = stringList->next;
			expectingComma = FALSE;
			foundRs = FALSE;
			foundRt = FALSE;
			foundAll = FALSE;
			rs = 0;
			rt = 0;
			rd = 0;
			immed = 0;
			address = 0;
			reg = '0';
			while(stringList != NULL)/*loop over the string nodes*/
			{
				if(expectingComma == FALSE)
				{
					if(stringList->str[0] == ',')/*unexpected comma*/
					{
						(*ECAdress)++;
						addErrorToList(errorsHead, unFinishedHead->lineNumber, 19, instructionsHead->sourceCode);
						goto skipUnfinishedLine;
					}
					if(commandType == 'I')/*only I or J could be unfinshed*/
					{
						if(foundRs == FALSE)
						{
							if(isRegister(stringList->str) == FALSE)
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 20, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
							stringList->str[0] = '+';
							tempInteger = atoi(stringList->str);
							if(tempInteger > MAX_REGISTER_INDEX)
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 21, instructionsHead->sourceCode);
							}
							rs = tempInteger;
							foundRs = TRUE;
						}else if(foundRt == FALSE){
							if(isRegister(stringList->str) == FALSE)
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 20, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
							stringList->str[0] = '+';
							tempInteger = atoi(stringList->str);
							if(tempInteger > MAX_REGISTER_INDEX)
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 21, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
							rt = tempInteger;
							foundRt = TRUE;
						}else{
							/*check valid label*/
							if(isValidLabel(stringList->str) == FALSE)
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 17, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
							/*symbol have to be exist in this stage (after 2nd loop)*/
							if(isSymbolExist(stringList->str, symbolsHead) == FALSE)
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 23, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
							/*label could not be external in this type of command*/
							if(getSymbolAttr(symbolsHead, stringList->str, 1) == '1')
							{
									(*ECAdress)++;
									addErrorToList(errorsHead, unFinishedHead->lineNumber, 22, instructionsHead->sourceCode);
									goto skipUnfinishedLine;
							}
							/*calculate the distnace between addresses*/
							tempInteger = (getSymbolValue(symbolsHead, stringList->str)) - unFinishedHead->address;
							/*immed is 16 bit long*/
							if(tempInteger > MAX_HALF_WORD_NUMBER || tempInteger < MIN_HALF_WORD_NUMBER)
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 16, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
							immed = tempInteger;
							foundAll = TRUE;
							if(stringList->next != NULL)/*if there is still content after found all*/
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 6, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
						}
					}else{/*command type is J*/
						if(strcmp(currentCommand, "jmp") == 0)
						{
							if(isRegister(stringList->str) == TRUE)
							{
								stringList->str[0] = '+';
								tempInteger = atoi(stringList->str);
								if(tempInteger > MAX_REGISTER_INDEX)
								{
									(*ECAdress)++;
									addErrorToList(errorsHead, unFinishedHead->lineNumber, 21, instructionsHead->sourceCode);
									goto skipUnfinishedLine;
								}
								address = tempInteger;
								reg = '1';/*if it is a register so reg is '1'*/
								foundAll = TRUE;
								if(stringList->next != NULL)
								{
									(*ECAdress)++;
									addErrorToList(errorsHead, unFinishedHead->lineNumber, 6, instructionsHead->sourceCode);
									goto skipUnfinishedLine;
								}
							}else{
								/*check valid label*/
								if(isValidLabel(stringList->str) == FALSE)
								{
									(*ECAdress)++;
									addErrorToList(errorsHead, unFinishedHead->lineNumber, 17, instructionsHead->sourceCode);
									goto skipUnfinishedLine;
								}
								/*symbol have to be exist in this stage (after 2nd loop)*/
								if(isSymbolExist(stringList->str, symbolsHead) == TRUE)
								{
									/*if the symbol is internal the address will be its value, if external so stays 0*/
									if(getSymbolAttr(symbolsHead, stringList->str, 1) == '0')
									{
										address = getSymbolValue(symbolsHead, stringList->str);
									}
								}else{
									(*ECAdress)++;
									addErrorToList(errorsHead, unFinishedHead->lineNumber, 23, instructionsHead->sourceCode);
									goto skipUnfinishedLine;
								}
								foundAll = TRUE;
								if(stringList->next != NULL)/*if there is still content after found all*/
								{
									(*ECAdress)++;
									addErrorToList(errorsHead, unFinishedHead->lineNumber, 6, instructionsHead->sourceCode);
									goto skipUnfinishedLine;
								}
							}
						}else{/*command is 'la' or 'call'*/
							/*check valid label*/
							if(isValidLabel(stringList->str) == FALSE)
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 17, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
							/*symbol have to be exist in this stage (after 2nd loop)*/
							if(isSymbolExist(stringList->str, symbolsHead) == TRUE)
							{
								/*if the symbol is internal the address will be its value, if external so stays 0*/
								if(getSymbolAttr(symbolsHead, stringList->str, 1) == '0')
								{
									address = getSymbolValue(symbolsHead, stringList->str);
								}
							}else{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 23, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
							reg = '0';/*reg always 0 for those type of command*/
							foundAll = TRUE;
							if(stringList->next != NULL)/*if there is still content after found all*/
							{
								(*ECAdress)++;
								addErrorToList(errorsHead, unFinishedHead->lineNumber, 6, instructionsHead->sourceCode);
								goto skipUnfinishedLine;
							}
						}
					}
					expectingComma = TRUE;/*switch flag*/
				}else{/*expecting comma*/
					if(stringList->str[0] != ',')/*missing comma*/
					{
						(*ECAdress)++;
						addErrorToList(errorsHead, unFinishedHead->lineNumber, 13, instructionsHead->sourceCode);
						goto skipUnfinishedLine;
					}
					if(stringList->next == NULL)/*a command could end with a comma*/
					{
						(*ECAdress)++;
						addErrorToList(errorsHead, unFinishedHead->lineNumber, 12, instructionsHead->sourceCode);
						goto skipUnfinishedLine;
					}
					expectingComma = FALSE;/*switch flag*/
				}
				stringList = stringList->next;
			}
			/*if found all - we will change the machine code in the instructions list (the instruction list is itrated until correct address at the start of the function) else - missing operands*/
			if (foundAll == TRUE)
			{
				instructionToMachineCode(instructionsHead->machineCode, instructionIndex, commandType, rs, rt, rd, immed, address, reg);
			}
			else
			{/*missing operands*/
				(*ECAdress)++;
				addErrorToList(errorsHead, unFinishedHead->lineNumber, 12, instructionsHead->sourceCode);
				goto skipUnfinishedLine;
			}

			skipUnfinishedLine:;/*on error, the code will jump here and skip the line*/
			destroyStringList(stringList);/*free allocated memory*/
			unFinishedHead = unFinishedHead->next;
		}
	}
}/*end of method resolveUnfinishedLines*/
