#include "./files/files.h"
#include "./resolver/resolver.h"

/*

Nur Nivon
this program is an assembler for a predefined unique assembly language.
the program gets a list of assembly files and for each it will make machine code files according to the grammer definitions.
if the assembly files contain any errors, the program will mark it.

("./assembler file1.as file2.as file3.as ...")

*/

int main(int argc, char** args)
{
	FILE* fPointer; /*a pointer to current assembly file*/
	/*pointers to linked lists of all the structures needed*/
	SourceCodeNode* instructionsHead = NULL;
	SourceCodeNode* dataHead = NULL;
	SourceCodeNode* sourceCodeHead = NULL;
	ExternNode* externsHead = NULL;
	SymbolNode* symbolsHead = NULL;
	StringNode* stringList = NULL;
	ErrorNode* errorsHead = NULL;
	UnfinishedLineNode* unFinishedHead = NULL;
	/*strings variables*/
	char currentLine[MAX_COMMAND_SIZE];
	char currentLabel[MAX_COMMAND_SIZE];
	char currentCommand[MAX_COMMAND_SIZE];
	int inputFilesCouter = 1;/*index of the file name in the command (index 0 is "./assembler")*/
	int charIndex = 0;
	int lineNumber = 1;
	int IC = START_ADDRESS;/*instructions counter (start address is 100)*/
	int DC = 0;/*data counter*/
	int lineHandler = 0;/*0 if success resolve line, otherwise error*/
	int errorsCounter = 0;/*errors counter per file*/
	LineType lineType = COMMENT;
	/*boolean variables*/
	Boolean labelFound = FALSE;
	Boolean isItLabel = FALSE;
	Boolean isItCommand = FALSE;
	Boolean mergedLists = FALSE;
	if(argc < 2)
	{
		/*args[0] is "./assembler" so needed one more arg at least*/
		printf("\nno input files specified\n");
		exit(0);
	}
	while(inputFilesCouter < argc)/*loop over args, start from 1*/
	{
		if(validInputFileName(args[inputFilesCouter]) == TRUE)/*valid file name ends with ".as"*/
		{
			fPointer = fopen(args[inputFilesCouter], "r");/*open file for reading*/
			if(fPointer == NULL)/*check file successfully open*/
			{
				printf("\ncannot open file: %s\n",args[inputFilesCouter]);
				inputFilesCouter++;
				continue;
			}
			instructionsHead = newSourceCodeNode();/*create instructions list head*/
			if(instructionsHead == NULL)
			{
				printf("\nerror allocate memory (instructions list) for file: %s\n",args[inputFilesCouter]);
				inputFilesCouter++;
				continue;
			}
			dataHead = newSourceCodeNode();/*create data list head*/
			if(dataHead == NULL)
			{
				printf("\nerror allocate memory (data list) for file: %s\n",args[inputFilesCouter]);
				inputFilesCouter++;
				destroySourceCodeList(instructionsHead);
				continue;
			}
			mergedLists = FALSE;/*init boolean in order to know if we need to free data list*/
			externsHead = newExternNode();/*create externs list head*/
			if(externsHead == NULL)
			{
				printf("\nerror allocate memory (externs list) for file: %s\n",args[inputFilesCouter]);
				inputFilesCouter++;
				destroySourceCodeList(instructionsHead);
				destroySourceCodeList(dataHead);
				continue;
			}
			symbolsHead = newSymbolNode();/*create symbols list head*/
			if(symbolsHead == NULL)
			{
				printf("\nerror allocate memory (symbols list) for file: %s\n",args[inputFilesCouter]);
				inputFilesCouter++;
				destroySourceCodeList(instructionsHead);
				destroySourceCodeList(dataHead);
				destroyExternsList(externsHead);
				continue;
			}
			errorsHead = newErrorNode();/*create errors list head*/
			if(errorsHead == NULL)
			{
				printf("\nerror allocate memory (errors list) for file: %s\n",args[inputFilesCouter]);
				inputFilesCouter++;
				destroySourceCodeList(instructionsHead);
				destroySourceCodeList(dataHead);
				destroyExternsList(externsHead);
				destroySymbolsList(symbolsHead);
				continue;
			}
			unFinishedHead = newUnfinishedLineNode();/*create unfinished lines list head*/
			if(unFinishedHead == NULL)
			{
				printf("\nerror allocate memory (unfinished lines list) for file: %s\n",args[inputFilesCouter]);
				inputFilesCouter++;
				destroySourceCodeList(instructionsHead);
				destroySourceCodeList(dataHead);
				destroyExternsList(externsHead);
				destroySymbolsList(symbolsHead);
				destroyErrorsList(errorsHead);
				continue;
			}
			/*init variables before going over the file*/
			lineNumber = 1;
			errorsCounter = 0;
			IC = 100;
			DC = 0;
			/*start 1st loop*/
			while(fgets(currentLine, MAX_COMMAND_SIZE, fPointer))/*while there is content in the file*/
			{
				stringList = newStringNode();/*create strings list head*/
				if(stringList == NULL)
				{
					goto skipFile;
				}
				if(isEmptyLine(currentLine) == TRUE)
				{
					goto skipLine;
				}
				/* fgets function get the line with '\n' at the end, this while will remove it*/
				charIndex = 0;
				while(charIndex < strlen(currentLine))
				{
					if(currentLine[charIndex] == '\n')
					{
						currentLine[charIndex] = '\0';
					}
					charIndex++;
				}
				splitStr(stringList, currentLine);/*split the line to list by white spaces and commas*/
				lineType = getLineType(stringList);/*find the line type (COMMENT,UNDEFINED,GUIDANCE,INSTRUCTION)*/
				labelFound = FALSE;
				isItLabel = FALSE;
				isItCommand = FALSE;
				lineHandler = 0;
				if(lineType == COMMENT)
				{					
				}else if(lineType == UNDEFINED) {
					errorsCounter++;
					addErrorToList(errorsHead, lineNumber, 0, currentLine);
				}else{
					isItLabel = isLabel(stringList->str);/*if first node in the line is label*/
					getCommandFromLine(stringList, GUIDANCE, currentCommand);/*try to find GUIDANCE command in the line*/
					if(isItLabel == TRUE && (strcmp(currentCommand, ".entry") != 0))/*entry will not be handled in the 1st loop on the file*/
					{
						labelFound = TRUE;
						strcpy(currentLabel, stringList->str);
						/* label will end with ':' char, the following loop will remove it */
						charIndex = 0;
						while(charIndex < strlen(currentLabel))
						{
							if(currentLabel[charIndex] == ':')
							{
								currentLabel[charIndex] = '\0';
							}
							charIndex++;
						}
					}else{
						isItCommand = isCommand(stringList->str);/*if first node in the line is a command*/
						if(isItCommand == FALSE)/*if it is not a command and not a label - error*/
						{
							errorsCounter++;
							addErrorToList(errorsHead, lineNumber, 18, currentLine);
							goto skipLine;
						}
					}
					if(lineType == GUIDANCE)
					{
						if((strcmp(currentCommand, "")) == 0)/*if not recognizing command*/
						{
							errorsCounter++;
							addErrorToList(errorsHead, lineNumber, 5, currentLine);
							goto skipLine;
						}
						if((strcmp(currentCommand, ".asciz") == 0) || (strcmp(currentCommand, ".dw") == 0) || (strcmp(currentCommand, ".dh") == 0) || (strcmp(currentCommand, ".db") == 0))/*check if it is a data line*/
						{
							if(labelFound == TRUE)
							{
								if(isValidLabel(currentLabel) == TRUE)
								{
									if(isSymbolExist(currentLabel, symbolsHead) == TRUE)
									{/*in this step if it is a data line the label shouldnt exist*/
										errorsCounter++;
										addErrorToList(errorsHead, lineNumber, 2, currentLine);
										goto skipLine;
									}else{
										addSymbol(DC, symbolsHead, currentLabel, 2);/*add the label to the symbols list*/
									}
								}else{/*not a valid label*/
									errorsCounter++;
									addErrorToList(errorsHead, lineNumber, 17, currentLine);
									goto skipLine;									
								}
							}
							lineHandler = addData(stringList, dataHead, &DC, currentCommand, currentLine);/*handle data*/
							if(lineHandler != 0)/*0 = success, otherwise - error*/
							{
								errorsCounter++;
								addErrorToList(errorsHead, lineNumber, lineHandler, currentLine);
								goto skipLine;
							}
						}else{/*this either an ".entry" or ".extern"*/
							if(strcmp(currentCommand, ".extern") == 0)/*if ".extern"*/
							{
								lineHandler = addExtern(stringList, symbolsHead);/*handle extern*/
								if(lineHandler != 0)/*0 = success, otherwise - error*/
								{
									errorsCounter++;
									addErrorToList(errorsHead, lineNumber, lineHandler, currentLine);
									goto skipLine;
								}
							}
						}
					}else if(lineType == INSTRUCTION){
						if(labelFound == TRUE)
						{
							/*handle label validations*/
							if(isValidLabel(currentLabel) == TRUE)
							{
								if(isSymbolExist(currentLabel, symbolsHead) == TRUE)
								{
									errorsCounter++;
									addErrorToList(errorsHead, lineNumber, 2, currentLine);
									goto skipLine;
								}else{
									addSymbol(IC, symbolsHead, currentLabel, 3);/*add the label to the symbols list*/
								}
							}else{
								errorsCounter++;
								addErrorToList(errorsHead, lineNumber, 17, currentLine);
								goto skipLine;									
							}
						}
						getCommandFromLine(stringList, INSTRUCTION, currentCommand);/*get INSTRUCTION command from the line*/
						if(strcmp(currentCommand, "") == 0)/*if not recognizig command*/
						{
							errorsCounter++;
							addErrorToList(errorsHead, lineNumber, 5, currentLine);
							goto skipLine;
						}
						lineHandler = addInstruction(stringList, instructionsHead, &IC, currentCommand, currentLine, symbolsHead, unFinishedHead, lineNumber);/*handle instruction*/
						if(lineHandler != 0)/*0 = success, otherwise - error*/
						{
							errorsCounter++;
							addErrorToList(errorsHead, lineNumber, lineHandler, currentLine);
							goto skipLine;
						}
					}
				}
				skipLine:;/*if there is an error with the line, skip line will go here*/
				destroyStringList(stringList);/*free the string list memory after finish handling the line*/
				lineNumber++;
			}/*end of while over the file content*/
			fclose(fPointer);/*close the file*/
			if(errorsCounter > 0)
			{
				/*if there were errors in the file, we will print them and not move on further*/
				printErrors(errorsHead, args[inputFilesCouter]);
				goto skipFile;
			}
			updateDataList(dataHead, symbolsHead, IC);/*update the data list address with IC counter*/
			/*start 2nd loop*/
			fPointer = fopen(args[inputFilesCouter], "r");/*open file 2nd time*/
			if(fPointer == NULL)
			{
				printf("\ncannot open file: %s\n",args[inputFilesCouter]);
				inputFilesCouter++;
				goto skipFile;
			}
			lineNumber = 1;/*init line number*/
			while(fgets(currentLine, MAX_COMMAND_SIZE, fPointer))/*while there is content in the file*/
			{
				stringList = newStringNode();/*create strings list for the line*/
				if(stringList == NULL)
				{
					goto skipFile;
				}
				if(isEmptyLine(currentLine) == TRUE)
				{
					goto skipLineSecondLoop;
				}
				/* fgets function get the line with '\n' at the end, this while will remove it*/
				charIndex = 0;
				while(charIndex < strlen(currentLine))
				{
					if(currentLine[charIndex] == '\n')
					{
						currentLine[charIndex] = '\0';
					}
					charIndex++;
				}
				splitStr(stringList, currentLine);/*split the line to strings list by white spaces and commas*/
				lineType = getLineType(stringList);
				getCommandFromLine(stringList, GUIDANCE, currentCommand);/*try to get a GUIDANCE command*/
				lineHandler = 0;/*init line handler*/
				if(lineType == COMMENT)
				{					
				}else if(lineType == GUIDANCE){
					/*in this step we will only need to take care of entry commands*/
					if(strcmp(currentCommand, ".entry") == 0)
					{
						lineHandler = addEntry(stringList, symbolsHead);/*handle entry*/
						if(lineHandler != 0)/*0 = success, otherwise - error*/
						{
							errorsCounter++;
							addErrorToList(errorsHead, lineNumber, lineHandler, currentLine);
							goto skipLineSecondLoop;
						}
					}
				}
				skipLineSecondLoop:;/*if there is an error in the line it will skip here*/
				destroyStringList(stringList);/*free strings list*/
				lineNumber++;
			}/*end of second while over the file*/
			fclose(fPointer);/*close file after 2nd loop*/
			if(errorsCounter > 0)
			{
				/*if there were errors in the file, we will print them and not move on further*/
				printErrors(errorsHead, args[inputFilesCouter]);
				goto skipFile;
			}
			/*handle missing adresses, missing symbols*/
			resolveUnfinishedLines(unFinishedHead, symbolsHead, instructionsHead, errorsHead, &errorsCounter);
			if(errorsCounter > 0)/*check for errors again*/
			{
				printErrors(errorsHead, args[inputFilesCouter]);
				goto skipFile;
			}

			/*if we got here, no errors occured in the file*/
			createExternsList(externsHead, symbolsHead, instructionsHead);/*creating externs list from symbols list and instructions list*/
			if(instructionsHead->size > 0 && dataHead->size > 0)/*if there is data and code*/
			{
				sourceCodeHead = mergeSourceCode(instructionsHead, dataHead);/*merge instructions list and data list, the result will be stored in source code list*/
				mergedLists = TRUE;/*mark that the lists has merged, now we need to free only the first one*/
			}else if(instructionsHead->size == 0) {
				sourceCodeHead = dataHead;
			}else{
				sourceCodeHead = instructionsHead;
			}
			
			generateObjectFile(args[inputFilesCouter], sourceCodeHead);/*creating the object (".ob") file (machine code)*/
			generateEntriesFile(args[inputFilesCouter], symbolsHead);/*creating the entries (".ent")*/
			generateExternsFile(args[inputFilesCouter], externsHead);/*creating the externs (".ext")*/

			skipFile:;/*if there where errors in the file it will skip here*/
			/*free all allocated memory*/
			destroySourceCodeList(instructionsHead);
			if(mergedLists == FALSE)
			{
				destroySourceCodeList(dataHead);
			}
			destroyExternsList(externsHead);
			destroySymbolsList(symbolsHead);
			destroyErrorsList(errorsHead);
			destroyUnFinishedLinesList(unFinishedHead);
		}else{
			printf("\ninvalid file name: %s\n",args[inputFilesCouter]);
		}
		inputFilesCouter++;
	}/*end of while over args*/
	printf("\n");
	return 0;
}/*end of method main*/
