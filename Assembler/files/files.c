#include "./files.h"

/*
this file includes all files related functions.
*/

/*
validInputFileName is a function that check if a string valid as an input file name
a valid file name for input ends with ".as"

the function parameters are:
fileName (string)

the function returns TRUE or FALSE
*/
Boolean validInputFileName(char* fileName)
{
	Boolean foundDot = FALSE;
	int charIndex = 0;
	while(fileName[charIndex] != '\0')
	{
		if(fileName[charIndex] == '.')
		{
			foundDot = TRUE;
			if(charIndex == 0)
			{
				return FALSE;
			}
			if(fileName[charIndex + 1] != 'a')
			{
				return FALSE;
			}
			if(fileName[charIndex + 2] != 's')
			{
				return FALSE;
			}
			if(fileName[charIndex + 3] != '\0')
			{
				return FALSE;
			}
			break;
		}
		charIndex++;
	}
	return foundDot;
}/*end of method validInputFileName*/

/*
binaryToHex is a function that gets chars array[4]. each char is either '0' or '1' (similar to 4 bits) and returns a single char hexadecimal translation of those bits (0-F).
the function parameters are:
bits (char[])

the function returns a char (0-F)
*/
char binaryToHex(char bits[])
{
	char hexResult;
	if(bits[0] == '0' && bits[1] == '0' && bits[2] == '0' && bits[3] == '0')
	{
		hexResult = '0';
	}else if(bits[0] == '0' && bits[1] == '0' && bits[2] == '0' && bits[3] == '1')
	{
		hexResult = '1';
	}else if(bits[0] == '0' && bits[1] == '0' && bits[2] == '1' && bits[3] == '0')
	{
		hexResult = '2';
	}else if(bits[0] == '0' && bits[1] == '0' && bits[2] == '1' && bits[3] == '1')
	{
		hexResult = '3';
	}else if(bits[0] == '0' && bits[1] == '1' && bits[2] == '0' && bits[3] == '0')
	{
		hexResult = '4';
	}else if(bits[0] == '0' && bits[1] == '1' && bits[2] == '0' && bits[3] == '1')
	{
		hexResult = '5';
	}else if(bits[0] == '0' && bits[1] == '1' && bits[2] == '1' && bits[3] == '0')
	{
		hexResult = '6';
	}else if(bits[0] == '0' && bits[1] == '1' && bits[2] == '1' && bits[3] == '1')
	{
		hexResult = '7';
	}else if(bits[0] == '1' && bits[1] == '0' && bits[2] == '0' && bits[3] == '0')
	{
		hexResult = '8';
	}else if(bits[0] == '1' && bits[1] == '0' && bits[2] == '0' && bits[3] == '1')
	{
		hexResult = '9';
	}else if(bits[0] == '1' && bits[1] == '0' && bits[2] == '1' && bits[3] == '0')
	{
		hexResult = 'A';
	}else if(bits[0] == '1' && bits[1] == '0' && bits[2] == '1' && bits[3] == '1')
	{
		hexResult = 'B';
	}else if(bits[0] == '1' && bits[1] == '1' && bits[2] == '0' && bits[3] == '0')
	{
		hexResult = 'C';
	}else if(bits[0] == '1' && bits[1] == '1' && bits[2] == '0' && bits[3] == '1')
	{
		hexResult = 'D';
	}else if(bits[0] == '1' && bits[1] == '1' && bits[2] == '1' && bits[3] == '0')
	{
		hexResult = 'E';
	}else if(bits[0] == '1' && bits[1] == '1' && bits[2] == '1' && bits[3] == '1')
	{
		hexResult = 'F';
	}
	return hexResult;
}/*end of method binaryToHex*/

/*
charToHex is a function that gets a char and translate it to 2 hexadecimal chars.
a char is 8 bits in translation to hex each 4 bits translated to 1 hexa char.
the function parameters:
c (char) - the char to be translated
hexChars (char*) - the destination to store the translation
*/
void charToHex(char* hexChars, char c)
{
	char temp[4];/*a chars array to store 4 bits at a time*/
	int i = 7, j = 0, k = 0;
	while(i >= 0)/*i from 7 to 0*/
	{
		if((c & (1u << i)) != 0)
		{
			temp[j] = '1';
		}else{
			temp[j] = '0';
		}
		if(j == 3)
		{
			hexChars[k] = binaryToHex(temp);/*translate 4 chars ('0'/'1') to hexa char (0-F)*/
			k++;
			j = 0;
			i--;
			continue;
		}
		j++;
		i--;
	}
}/*end of method charToHex*/

/*
getCodeLines is a functon that gets a source code linked list and returns how many of the nodes are code related (not data related).
the address starts at 100, so the result will be the last node of code - 100.
the function parameters:
source (SourceCodeNode*) - the head of the source code linked list

the function returns number of code lines as integer.
*/
int getCodeLines(SourceCodeNode* source)
{
	int result = 0;
	while(source != NULL && source->isCode == TRUE)
	{
		result += 4;
		source = source->next;
	}
	return result;
}/*end of method getCodeLines*/

/*
getDataLines is a functon that gets a source code linked list and returns how many of the nodes are data related (not code related).
the linked list is ordered code first and data second. and for each node there is a size. the sum of it will be the number of data lines.
the function parameters:
source (SourceCodeNode*) - the head of the source code linked list

the function returns number of data lines as integer.
*/
int getDataLines(SourceCodeNode* source)
{
	int temp = 0;
	while(source != NULL)
	{
		if(source->isCode == FALSE)
		{
			temp += source->size;
		}
		source = source->next;
	}
	return temp;
}/*end of method getCodeLines*/

/*
generateObjectFile is a function that implements the last step in the assembler - translating from binary to hex and creates the ".ob" file with the output
the function gets the file name as a pramater inorder produce a output file with similar name but diffrent extenation.
the function gets a source code linked list, in each node there is a machineCode(binary representation of the line), and the binary will be translated to hex and printed to output file.
the function parameters:
source (SourceCodeNode*) - the head of the source code linked list
fileName (char*) - source file name
*/
void generateObjectFile(char* fileName, SourceCodeNode* source)
{
	FILE* objFilePointer;/*the new file pointer*/
	char newFileName[MAX_COMMAND_SIZE];/*the new name string*/
	char hexChars[2];/*the translation will be occurred for each 8 bits (2 hexa chars)*/
	int i = 0;/*internal source list content index*/
	int j = 0;/*internal output line index (the output will be printed in 4 pairs of 2 hexa chars)*/
	int currentAddress = START_ADDRESS;/*output address counter*/
	strcpy(newFileName,fileName);
	/*replace the ".as" extantion with ".ob"*/
	while(newFileName[i] != '.')
	{
		i++;
	}
	newFileName[++i] = 'o';
	newFileName[++i] = 'b';
	objFilePointer = fopen(newFileName, "w");/*create output file*/
	if(objFilePointer != NULL)
	{
		fprintf(objFilePointer,"     %d %d", getCodeLines(source), getDataLines(source));/*the top of the file will present the number of code and data lines*/
		j = 0;
		i = 0;
		while(source != NULL && source->size > 0)/*loop over source list*/
		{
			if(j % 4 == 0)/*each for pairs will create a new line in output file*/
			{
				fprintf(objFilePointer,"\n");
				if(currentAddress < 1000)/*add leading '0' if needed*/
				{
					fprintf(objFilePointer,"%c",'0');
				}
				fprintf(objFilePointer,"%d ",currentAddress);
			}
			/*in the following loop we will go over the current source node content and translate the binary to hex. until end of source content or until end of output line*/
			while((i < source->size) && (j < 4))
			{
				charToHex(hexChars, source->machineCode[i]);
				fprintf(objFilePointer,"%c%c ",hexChars[0], hexChars[1]);
				i++;
				j++;
			}
			if(i == source->size)/*if the loop stopped becuase end of source content*/
			{
				source = source->next;
				i = 0;
			}/*if the loop stopped becuase end of output line*/
			if(j == 4)
			{
				j = 0;
				currentAddress += 4;
			}
		}/*end of while over source list*/
		fclose(objFilePointer);/*close new file*/
	}
}/*end of method generateObjectFile*/

/*
generateEntriesFile is a function that creates a ".ent" file for the assembly. it gets a symbols list and for each symbol with "entry" attribute it will print it with its address.
the function parameters:
symbolsHead (SymbolNode*) - the head of the symbols linked list
fileName (char*) - source file name
*/
void generateEntriesFile(char* fileName, SymbolNode* symbolsHead)
{
	FILE* entFilePointer = NULL;/*the new file pointer*/
	char newFileName[MAX_COMMAND_SIZE];/*the new file string*/
	int i = 0;/*file name char index*/
	Boolean entryExist = FALSE;/*if there are no entries, a file wont be generated*/
	/*loop over the file name to change the extantion*/
	while(fileName[i] != '.')
	{
		newFileName[i] = fileName[i];
		i++;
	}
	newFileName[i] = '.';
	newFileName[++i] = 'e';
	newFileName[++i] = 'n';
	newFileName[++i] = 't';
	newFileName[++i] = '\0';
	while(symbolsHead != NULL)/*loop over the symbols list*/
	{
		if(symbolsHead->value != 0 && symbolsHead->attrs[0] == '1')/*check the symbols is an entry*/
		{
			if(entryExist == FALSE)/*if it is the first entry*/
			{
				entryExist = TRUE;
				entFilePointer = fopen(newFileName, "w");/*create the new file*/
			}
			if(entFilePointer != NULL)
			{
				fprintf(entFilePointer,"%s ",symbolsHead->name);/*print the symbol name*/
				if(symbolsHead->value < 1000)/*add leading '0' if needed for the address*/
				{
					fprintf(entFilePointer,"%c",'0');
				}
				fprintf(entFilePointer,"%d\n",symbolsHead->value);
			}
		}
		symbolsHead = symbolsHead->next;
	}
	if(entFilePointer != NULL)
	{
		fclose(entFilePointer);/*close the new file*/
	}
}/*end of method generateEntriesFile*/

/*
generateExternsFile is a function that creates a ".ext" file for the assembly. it gets an externs list and for each it will print its name and the address where it is used in the code.
the function parameters:
externsHead (ExternNode*) - the head of the externs linked list
fileName (char*) - source file name
*/
void generateExternsFile(char* fileName, ExternNode* externsHead)
{
	FILE* extFilePointer = NULL;/*new file pointer*/
	char newFileName[MAX_COMMAND_SIZE];/*new file string*/
	int i = 0;/*file name char index*/
	Boolean externExist = FALSE;/*if there are no externs, a file wont be generated*/
	/*loop over the file name to change its extantion*/
	while(fileName[i] != '.')
	{
		newFileName[i] = fileName[i];
		i++;
	}
	newFileName[i] = '.';
	newFileName[++i] = 'e';
	newFileName[++i] = 'x';
	newFileName[++i] = 't';
	newFileName[++i] = '\0';
	/*loop over the externs list*/
	while(externsHead != NULL)
	{
		if(externsHead->address != 0)/*check the extern is in use in the code*/
		{
			if(externExist == FALSE)/*if it is the first extern*/
			{
				externExist = TRUE;
				extFilePointer = fopen(newFileName, "w");/*create the new file*/
			}
			if(extFilePointer != NULL)
			{
				fprintf(extFilePointer,"%s ",externsHead->name);/*print the extern name*/
				if(externsHead->address < 1000)/*print leading '0' if needed for the address*/
				{
					fprintf(extFilePointer,"%c",'0');
				}
				fprintf(extFilePointer,"%d\n",externsHead->address);
			}
		}
		externsHead = externsHead->next;
	}
	if(extFilePointer != NULL)
	{
		fclose(extFilePointer);/*close the new file*/
	}
}/*end of method generateExternsFile*/
