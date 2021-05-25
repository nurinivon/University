#include "./set.h"

/*
this is secondary program to myset.c, that contains all sets related methods
*/

/*
original defintion of sets that are used in the program
*/
set* SETA;
set* SETB;
set* SETC;
set* SETD;
set* SETE;
set* SETF;

/*
newSet function create new memory allocation for set object and return is address
*/
set* newSet()
{
	set* temp = NULL;
	temp = (set *) malloc(sizeof(set));
	if(temp != NULL)
	{
		/*initialize set content*/
		temp->content[0] = 0;
		temp->content[1] = 0;
		temp->content[2] = 0;
		temp->content[3] = 0;
	}
	return temp;
} /*end of method newSet*/

/*
getAddressBySetName is a function that get a set name as a string and returns its address.
*/
set* getAddressBySetName(char *setName)
{
	if(strcmp(setName,"SETA") == 0)
	{
		return SETA;
	}else if(strcmp(setName,"SETB") == 0)
	{
		return SETB;
	}else if(strcmp(setName,"SETC") == 0)
	{
		return SETC;
	}else if(strcmp(setName,"SETD") == 0)
	{
		return SETD;
	}else if(strcmp(setName,"SETE") == 0)
	{
		return SETE;
	}else if(strcmp(setName,"SETF") == 0)
	{
		return SETF;
	}else{
		return NULL;
	}
}/*end of method getAddressBySetName*/

/*
read_set is function that sets a set content to a list of numbers.
the function gets as parameters the currents set to be use and the pointer to a numbers list which contain the numbers for the set.
the function creates new set with the numbers -> free the original set -> and returns the set address.
*/
set* read_set(set* currentSet, NumberNode* numbersHead)
{
	set* temp = newSet();/*create new set*/
	free(currentSet); /*free original set*/
	if(temp != NULL) /*check allocation*/
	{
		while(numbersHead != NULL && numbersHead->number != -1) /*iterate the numbers list*/
		{
			/*
			set appropriate bit in the set content to '1'
			BITS_IN_INT = 32
			3 >= (numbersHead->number / BITS_IN_INT) >= 0
			31 >= (numbersHead->number % BITS_IN_INT) >= 0
			*/
			temp->content[(int) (numbersHead->number / BITS_IN_INT)] |= (1u << (numbersHead->number % BITS_IN_INT));
			numbersHead = numbersHead->next;
		}
	}
	return temp;
}/*end of method read_set*/

/*
print_set is function that prints to out put the set content.
the function gets as parameters the currents set to be printed and print the numbers in the set.
the numbers will be printed in lines with max size of 16
*/
void print_set(set* currentSet)
{
	Boolean setIsEmpty = TRUE; /*boolean to check if set is empty*/
	int setIndex = 0; /*[0,3]*/
	int bitsIndex = 0; /*[0,31]*/
	int numbersInSet = 0; /*[0,128]*/
	while(setIndex < SET_SIZE) /*iterate the set content*/
	{
		if(currentSet->content[setIndex] != 0) /*if int is 0 so all of its bits are 0*/
		{
			setIsEmpty = FALSE; /*if int is not 0 so the set is not empty*/
			bitsIndex = 0; 
			while(bitsIndex < BITS_IN_INT) /*iterate the bits in the current int*/
			{
				if((currentSet->content[setIndex] & (1u << bitsIndex)) != 0) /*check if the current bit is not 0*/
				{
					if(numbersInSet % MAX_NUMBER_IN_ROW == 0) /*check if need new numbers line*/
					{
						printf("\n|");
					}
					printf(" %d |",((setIndex * BITS_IN_INT) + bitsIndex)); /*the number is the bit index in the array [0,127]*/
					numbersInSet++;
				}
				bitsIndex++;
			}/*end of while*/
		}
		setIndex++;
	}/*end of while*/
	if(setIsEmpty == TRUE)
	{
		printf("\n| The set is empty |");
	}
}/*end of method print_set*/

/*
union_set function is a function that gets 3 sets. it makes union out of the first 2 sets and returns new set with the united content.
the function gets as parameter sets array (size of 3).
the function returns an address to the new created united set.
*/
set* union_set(set* setsArr[])
{
	int setIndex = 0; /*[0,4]*/
	int bitsIndex = 0; /*[0,31]*/
	set* temp = NULL;
	temp = newSet(); /*create new set*/
	if(temp != NULL) /*check allocation*/
	{
		while(setIndex < SET_SIZE) /*iterate the set content*/
		{
			bitsIndex = 0;
			while(bitsIndex < BITS_IN_INT) /*iterate the bits in the current int*/
			{
				/*for each bit check if it defined in 1 out of the first 2 sets (OR)*/
				if(((setsArr[0]->content[setIndex] & (1u << bitsIndex)) != 0) || ((setsArr[1]->content[setIndex] & (1u << bitsIndex)) != 0))
				{
					temp->content[setIndex] |= (1u << bitsIndex); /*if defined in 1 of them it will be in the union*/
				}
				bitsIndex++;
			} /*end of while*/
			setIndex++;
		} /*end of while*/
	}
	free(setsArr[2]); /*free the third set, it will be set with the new created set*/
	return temp;
}/*end of method union_set*/

/*
intersect_set function is a function that gets 3 sets. it makes intersect out of the first 2 sets and returns new set with the intersected content.
the function gets as parameter sets array (size of 3).
the function returns an address to the new created intersected set.
*/
set* intersect_set(set* setsArr[])
{
	int setIndex = 0; /*[0,4]*/
	int bitsIndex = 0; /*[0,31]*/
	set* temp = NULL;
	temp = newSet(); /*create new set*/
	if(temp != NULL) /*check allocation*/
	{
		while(setIndex < SET_SIZE) /*iterate the set content*/
		{
			bitsIndex = 0;
			while(bitsIndex < BITS_IN_INT) /*iterate the bits in the current int*/
			{
				/*for each bit check if it defined in the first 2 sets (AND)*/
				if(((setsArr[0]->content[setIndex] & (1u << bitsIndex)) != 0) && ((setsArr[1]->content[setIndex] & (1u << bitsIndex)) != 0))
				{
					temp->content[setIndex] |= (1u << bitsIndex); /*if defined in both of them it will be in the intersect*/
				}
				bitsIndex++;
			} /*end of while*/
			setIndex++;
		} /*end of while*/
	}
	free(setsArr[2]); /*free the third set, it will be set with the new created set*/
	return temp;
}/*end of method intersect_set*/

/*
sub_set function is a function that gets 3 sets. it subtracts the the second set content from the first and returns new set with the subtracted content.
the function gets as parameter sets array (size of 3).
the function returns an address to the new created subtracted set.
*/
set* sub_set(set* setsArr[])
{
	int setIndex = 0; /*[0,4]*/
	int bitsIndex = 0; /*[0,31]*/
	set* temp = NULL;
	temp = newSet(); /*create new set*/
	if(temp != NULL) /*check allocation*/
	{
		while(setIndex < SET_SIZE) /*iterate the set content*/
		{
			bitsIndex = 0;
			while(bitsIndex < BITS_IN_INT) /*iterate the bits in the current int*/
			{
				/*for each bit check if it defined in the first set and NOT defined in the second*/
				if(((setsArr[0]->content[setIndex] & (1u << bitsIndex)) != 0) && ((setsArr[1]->content[setIndex] & (1u << bitsIndex)) == 0))
				{
					temp->content[setIndex] |= (1u << bitsIndex); /*create the substracted content*/
				}
				bitsIndex++;
			} /*end of while*/
			setIndex++;
		} /*end of while*/
	}
	free(setsArr[2]); /*free the third set, it will be set with the new created set*/
	return temp;
}/*end of method sub_set*/

/*
sub_set function is a function that gets 3 sets. the function creates a symetric diffrence between the first 2 sets and returns the new set address.
the function gets as parameter sets array (size of 3).
the function returns an address to the new created symetric diffrence set.
*/
set* symdiff_set(set* setsArr[])
{
	set* temp = NULL;
	set* intersectSets = NULL;
	set* tempSetsArr[SET_ARRAY_SIZE] = {NULL,NULL,NULL}; /*temp sets array for inner function use*/
	temp = newSet(); /*create a new set*/
	if(temp != NULL) /*check allocation*/
	{
		tempSetsArr[0] = setsArr[0];
		tempSetsArr[1] = setsArr[1];
		tempSetsArr[2] = temp;
		temp = union_set(tempSetsArr); /*create a union from the first 2 sets*/
		intersectSets = newSet(); /*create new set that will hold the intersect*/
		if(intersectSets != NULL) /*check allocation*/
		{
			tempSetsArr[0] = setsArr[0];
			tempSetsArr[1] = setsArr[1];
			tempSetsArr[2] = intersectSets;
			intersectSets = intersect_set(tempSetsArr); /*create a intersect from the first 2 sets*/
			tempSetsArr[0] = temp;
			tempSetsArr[1] = intersectSets;
			tempSetsArr[2] = temp;
			temp = sub_set(tempSetsArr);/*substract the intersect from the union - this is the symetric diffrence*/
			free(intersectSets); /*free inner used set*/
		}
	}
	free(setsArr[2]); /*free the third set, it will be set with the new created set*/
	return temp;
}/*end of method symdiff_set*/

/*
isValidSetName is a function that gets set name as string and check if it is a valid set name.
the ufncton returns the boolean result
*/
Boolean isValidSetName(char *setName)
{
	/*check all the sets names*/
	if(strcmp(setName,"SETA") != 0 && strcmp(setName,"SETB") != 0 && strcmp(setName,"SETC") != 0 && strcmp(setName,"SETD") != 0 && strcmp(setName,"SETE") != 0 && strcmp(setName,"SETF") != 0)
	{
		return FALSE;
	}
	return TRUE;
}/*end of method isValidSetName*/

/*
isValidCommand is a function that gets set name as string and check if it is a valid set name.
the ufncton returns the boolean result
*/
Boolean isValidCommand(char *commandName)
{
	/*check all commands names*/
	if(strcmp(commandName,"read_set") != 0 && strcmp(commandName,"print_set") != 0 && strcmp(commandName,"union_set") != 0 && strcmp(commandName,"intersect_set") != 0 && strcmp(commandName,"sub_set") != 0 && strcmp(commandName,"symdiff_set") != 0)
	{
		return FALSE;
	}
	return TRUE;
}/*end of method isValidCommand*/

/*
executeCommand is a function that gets a command as a string and execute it.
for each command there are expected parameters/syntax.
the function returns int which is the response index for response dictionary defined in myset.c
*/
int executeCommand(char command[])
{
	char* setName; /*string that holds set name*/
	char* commandName; /*string that holds command name*/
	NumberNode* numberListHead = NULL; /*head of number list if needed for read_set*/
	int numbersIndex;  /*numbers list index*/
	int i; /*command string index*/
	char *currentContent; /*string that will hold partial string from the original command*/
	set* setsArr[SET_ARRAY_SIZE] = {NULL,NULL,NULL}; /*set array to  be used in the methods with set array parameter*/
	/*boolean flags*/
	Boolean expectedComma = FALSE;
	Boolean expectedNumber = FALSE;
	Boolean foundTerminator = FALSE;
	Boolean numberListInit = FALSE;
	/*the original command will be turn into string linked list with the commandhead as the list head*/
	Node *commandHead = newNode();
	if(commandHead != NULL) /*check allocation*/
	{
		splitStr(commandHead,command);/*split the original command to string linked list*/
		commandName = getContent(commandHead, COMMAND_INDEX); /*the first node should be the command name*/
		if(isValidCommand(commandName) == FALSE) /*check if valid command*/
		{
			destroyList(commandHead);
			return 2;
		}
		if(strchr(getContent(commandHead, 1), ',') != NULL) /*check for Illegal commas right after the command (should be white space seprate)*/
		{
			destroyList(commandHead);
			return 10;
		}
		if(strcmp(commandName,"read_set") == 0)
		{
			/*in read set the second content should be the set name*/
			setName = getContent(commandHead, 1);
			if(isValidSetName(setName) == FALSE) /*check set name*/
			{
				destroyList(commandHead);
				return 1;
			}
			/*in read set we need to use numbers list - initialized here*/
			numberListHead = newNumberNode();
			if(numberListHead != NULL) /*check allocation*/
			{
				numbersIndex = 2;/*the numbers will start from the third content in the split string*/
				foundTerminator = FALSE;				
				expectedComma = TRUE; /*in read set after the set name - we expect comma*/
				expectedNumber = FALSE;
				numberListInit = FALSE;
				currentContent = getContent(commandHead, numbersIndex);
				while((currentContent != '\0') != 0 && (strcmp(currentContent,"") != 0)) /*while there is still content in the string list*/
				{
					if(expectedNumber == TRUE) /*if we expect number*/
					{
						if(currentContent[0] == '-') /*if its negative*/
						{
							if(currentContent[1] != '1') /*if its not -1*/
							{
								destroyList(commandHead);
								destroyNumberList(numberListHead);
								return 3;
							}
							/*if we have further content after -1*/
							if(currentContent[2] != '\0' || (getContent(commandHead, (numbersIndex + 1)) != '\0') != 0)
							{
								destroyList(commandHead);
								destroyNumberList(numberListHead);
								return 7;
							}
							/*handle flags*/
							expectedNumber = FALSE;
							foundTerminator = TRUE;
							expectedComma = TRUE;
							if(numberListInit == FALSE) /*first number in the list*/
							{
								numberListHead->number = -1;
								numberListInit = TRUE;
							}else{
								addNumberToList(numberListHead,-1);
							}
						}else{ /*not negative number*/
							i = 0;
							while(currentContent[i] != '\0') /*itrate the number chars*/
							{
								if(!isdigit(currentContent[i])) /*check all chars are digits*/
								{
									if(currentContent[i] == ',') /*if the char is comma*/
									{
										destroyList(commandHead);
										destroyNumberList(numberListHead);
										return 10;
									}
									destroyList(commandHead);
									destroyNumberList(numberListHead);
									return 5;									
								}
								i++;
							}/*end of chars while - if end while -> all chars are digits*/
							if(atoi(currentContent) < MAX_INT_VALUE)/*check number is less then 128*/
							{
								if(numberListInit == FALSE)/*first number in the list*/
								{
									numberListHead->number = atoi(currentContent);
									numberListInit = TRUE;
								}else{
									addNumberToList(numberListHead,atoi(currentContent));
								}
							}else{
								destroyList(commandHead);
								destroyNumberList(numberListHead);
								return 3;
							}
							/*handle flags*/
							expectedComma = TRUE;
							expectedNumber = FALSE;
						}
					}else{/*expected number is FALSE*/
						if(expectedComma == TRUE)/*if we expect a comma*/
						{
							if(currentContent[0] != ',')/*if the char is not a comma*/
							{
								destroyList(commandHead);
								destroyNumberList(numberListHead);
								return 9;
							}
							/*handle flags*/
							expectedComma = FALSE;
							expectedNumber = TRUE;
						}
					}
					/*get next content in the list*/
					numbersIndex++;
					currentContent = getContent(commandHead, numbersIndex);
				}
				if(foundTerminator == FALSE)/*check if numbers list terminated properly*/
				{
					destroyList(commandHead);
					destroyNumberList(numberListHead);
					return 4;					
				}
				/*check which of the sets need to be re-set*/
				if(strcmp(setName,"SETA") == 0)
				{
					SETA = read_set(SETA, numberListHead);
				}else if(strcmp(setName,"SETB") == 0)
				{
					SETB = read_set(SETB, numberListHead);
				}else if(strcmp(setName,"SETC") == 0)
				{
					SETC = read_set(SETC, numberListHead);
				}else if(strcmp(setName,"SETD") == 0)
				{
					SETD = read_set(SETD, numberListHead);
				}else if(strcmp(setName,"SETE") == 0)
				{
					SETE = read_set(SETE, numberListHead);
				}else if(strcmp(setName,"SETF") == 0)
				{
					SETF = read_set(SETF, numberListHead);
				}else
				{
					destroyList(commandHead);
					destroyNumberList(numberListHead);
					return 1;
				}
				destroyNumberList(numberListHead);
			}
		}else if(strcmp(commandName,"print_set") == 0)
		{
			/*in print set the second content should be the set name*/
			setName = getContent(commandHead, 1);
			if(isValidSetName(setName) == FALSE) /*check set name*/
			{
				destroyList(commandHead);
				return 1;
			}
			/*in print set - after set name there should be no further content*/
			if((getContent(commandHead, 2) != '\0') != 0)
			{
				destroyList(commandHead);
				return 7;
			}
			/*check which set needs to be printed*/
			if(strcmp(setName,"SETA") == 0)
			{
				print_set(SETA);
			}else if(strcmp(setName,"SETB") == 0)
			{
				print_set(SETB);
			}else if(strcmp(setName,"SETC") == 0)
			{
				print_set(SETC);
			}else if(strcmp(setName,"SETD") == 0)
			{
				print_set(SETD);
			}else if(strcmp(setName,"SETE") == 0)
			{
				print_set(SETE);
			}else if(strcmp(setName,"SETF") == 0)
			{
				print_set(SETF);
			}else
			{
				destroyList(commandHead);
				return 1;
			}
		}else if(strcmp(commandName,"union_set") == 0)
		{
			/*init sets array*/
			setsArr[0] = NULL;
			setsArr[1] = NULL;
			setsArr[2] = NULL;
			i = 1; /*init content index to 1 (0 is command name)*/
			expectedComma = FALSE; /*in union set we do not expect for comma on start*/
			currentContent = getContent(commandHead, i);
			while((currentContent != '\0') != 0 && (strcmp(currentContent,"") != 0))/*iterate content in the string list*/
			{
				if(expectedComma == FALSE)/*if we dont expect comma*/
				{
					if(currentContent[0] == ',')/*if the content is comma*/
					{
						destroyList(commandHead);
						return 8;
					}
					setName = currentContent;/*in union set we expect only sets names*/
					if(isValidSetName(setName) == FALSE)/*check set name*/
					{
						destroyList(commandHead);
						return 1;
					}
					/*check where to store the current set address*/
					if(setsArr[0] == NULL)
					{
						setsArr[0] = getAddressBySetName(setName); /*get the address of the set*/
						expectedComma = TRUE; /*expecting comma after first set name*/
					}else if(setsArr[1] == NULL)
					{
						setsArr[1] = getAddressBySetName(setName); /*get the address of the set*/
						expectedComma = TRUE; /*expecting comma after first set name*/
					}else if(setsArr[2] == NULL)
					{
						setsArr[2] = getAddressBySetName(setName); /*get the address of the set*/
						if((getContent(commandHead, (i + 1)) != '\0') != 0) /*if there is further content after third set name*/
						{
							destroyList(commandHead);
							return 7;
						}
						break;
					}
				}else{ /*if we expect a comma*/
					if(currentContent[0] != ',') /*if the string is not a comma*/
					{
						destroyList(commandHead);
						return 9;
					}
					expectedComma = FALSE; /*after a comma we do not expect another one*/
				}
				/*get next string in the list*/
				i++;
				currentContent = getContent(commandHead, i);
			}
			/*check we fill in the three sets*/
			if(setsArr[0] == NULL || setsArr[1] == NULL || setsArr[2] == NULL)
			{
				destroyList(commandHead);
				return 7;
			}
			/*setName now holds the third set name which is the set needs to re-set*/
			if(strcmp(setName,"SETA") == 0)
			{
				SETA = union_set(setsArr);
			}else if(strcmp(setName,"SETB") == 0)
			{
				SETB = union_set(setsArr);
			}else if(strcmp(setName,"SETC") == 0)
			{
				SETC = union_set(setsArr);
			}else if(strcmp(setName,"SETD") == 0)
			{
				SETD = union_set(setsArr);
			}else if(strcmp(setName,"SETE") == 0)
			{
				SETE = union_set(setsArr);
			}else if(strcmp(setName,"SETF") == 0)
			{
				SETF = union_set(setsArr);
			}else
			{
				destroyList(commandHead);
				return 1;
			}
		}else if(strcmp(commandName,"intersect_set") == 0)
		{
			/*init sets array*/
			setsArr[0] = NULL;
			setsArr[1] = NULL;
			setsArr[2] = NULL;
			i = 1; /*init content index to 1 (0 is command name)*/
			expectedComma = FALSE; /*in intersect set we do not expect for comma on start*/
			currentContent = getContent(commandHead, i);
			while((currentContent != '\0') != 0 && (strcmp(currentContent,"") != 0))/*iterate content in the string list*/
			{
				if(expectedComma == FALSE)/*if we dont expect comma*/
				{
					if(currentContent[0] == ',')/*if the content is comma*/
					{
						destroyList(commandHead);
						return 8;
					}
					setName = currentContent;/*in intersect set we expect only sets names*/
					if(isValidSetName(setName) == FALSE)/*check set name*/
					{
						destroyList(commandHead);
						return 1;
					}
					/*check where to store the current set address*/
					if(setsArr[0] == NULL)
					{
						setsArr[0] = getAddressBySetName(setName); /*get the address of the set*/
						expectedComma = TRUE; /*expecting comma after first set name*/
					}else if(setsArr[1] == NULL)
					{
						setsArr[1] = getAddressBySetName(setName); /*get the address of the set*/
						expectedComma = TRUE; /*expecting comma after first set name*/
					}else if(setsArr[2] == NULL)
					{
						setsArr[2] = getAddressBySetName(setName); /*get the address of the set*/
						if((getContent(commandHead, (i + 1)) != '\0') != 0)/*if there is further content after third set name*/
						{
							destroyList(commandHead);
							return 7;
						}
						break;
					}
				}else{ /*if we expect a comma*/
					if(currentContent[0] != ',') /*if the string is not a comma*/
					{
						destroyList(commandHead);
						return 9;
					}
					expectedComma = FALSE; /*after a comma we do not expect another one*/
				}
				/*get next string in the list*/
				i++;
				currentContent = getContent(commandHead, i);
			}
			/*check we fill in the three sets*/
			if(setsArr[0] == NULL || setsArr[1] == NULL || setsArr[2] == NULL)
			{
				destroyList(commandHead);
				return 7;
			}
			/*setName now holds the third set name which is the set needs to re-set*/
			if(strcmp(setName,"SETA") == 0)
			{
				SETA = intersect_set(setsArr);
			}else if(strcmp(setName,"SETB") == 0)
			{
				SETB = intersect_set(setsArr);
			}else if(strcmp(setName,"SETC") == 0)
			{
				SETC = intersect_set(setsArr);
			}else if(strcmp(setName,"SETD") == 0)
			{
				SETD = intersect_set(setsArr);
			}else if(strcmp(setName,"SETE") == 0)
			{
				SETE = intersect_set(setsArr);
			}else if(strcmp(setName,"SETF") == 0)
			{
				SETF = intersect_set(setsArr);
			}else
			{
				destroyList(commandHead);
				return 1;
			}
		}else if(strcmp(commandName,"sub_set") == 0)
		{
			/*init sets array*/
			setsArr[0] = NULL;
			setsArr[1] = NULL;
			setsArr[2] = NULL;
			i = 1; /*init content index to 1 (0 is command name)*/
			expectedComma = FALSE; /*in sub set we do not expect for comma on start*/
			currentContent = getContent(commandHead, i);
			while((currentContent != '\0') != 0 && (strcmp(currentContent,"") != 0))/*iterate content in the string list*/
			{
				if(expectedComma == FALSE)/*if we dont expect comma*/
				{
					if(currentContent[0] == ',')/*if the content is comma*/
					{
						destroyList(commandHead);
						return 8;
					}
					setName = currentContent;/*in sub set we expect only sets names*/
					if(isValidSetName(setName) == FALSE)/*check set name*/
					{
						destroyList(commandHead);
						return 1;
					}
					/*check where to store the current set address*/
					if(setsArr[0] == NULL)
					{
						setsArr[0] = getAddressBySetName(setName); /*get the address of the set*/
						expectedComma = TRUE; /*expecting comma after first set name*/
					}else if(setsArr[1] == NULL)
					{
						setsArr[1] = getAddressBySetName(setName); /*get the address of the set*/
						expectedComma = TRUE; /*expecting comma after first set name*/
					}else if(setsArr[2] == NULL)
					{
						setsArr[2] = getAddressBySetName(setName); /*get the address of the set*/
						if((getContent(commandHead, (i + 1)) != '\0') != 0) /*if there is further content after third set name*/
						{
							destroyList(commandHead);
							return 7;
						}
						break;
					}
				}else{ /*if we expect a comma*/
					if(currentContent[0] != ',') /*if the string is not a comma*/
					{
						destroyList(commandHead);
						return 9;
					}
					expectedComma = FALSE; /*after a comma we do not expect another one*/
				}
				/*get next string in the list*/
				i++;
				currentContent = getContent(commandHead, i);
			}
			/*check we fill in the three sets*/
			if(setsArr[0] == NULL || setsArr[1] == NULL || setsArr[2] == NULL)
			{
				destroyList(commandHead);
				return 7;
			}
			/*setName now holds the third set name which is the set needs to re-set*/
			if(strcmp(setName,"SETA") == 0)
			{
				SETA = sub_set(setsArr);
			}else if(strcmp(setName,"SETB") == 0)
			{
				SETB = sub_set(setsArr);
			}else if(strcmp(setName,"SETC") == 0)
			{
				SETC = sub_set(setsArr);
			}else if(strcmp(setName,"SETD") == 0)
			{
				SETD = sub_set(setsArr);
			}else if(strcmp(setName,"SETE") == 0)
			{
				SETE = sub_set(setsArr);
			}else if(strcmp(setName,"SETF") == 0)
			{
				SETF = sub_set(setsArr);
			}else
			{
				destroyList(commandHead);
				return 1;
			}
		}else if(strcmp(commandName,"symdiff_set") == 0)
		{
			/*init sets array*/
			setsArr[0] = NULL;
			setsArr[1] = NULL;
			setsArr[2] = NULL;
			i = 1; /*init content index to 1 (0 is command name)*/
			expectedComma = FALSE; /*in symdiff set we do not expect for comma on start*/
			currentContent = getContent(commandHead, i);
			while((currentContent != '\0') != 0 && (strcmp(currentContent,"") != 0))/*iterate content in the string list*/
			{
				if(expectedComma == FALSE)/*if we dont expect comma*/
				{
					if(currentContent[0] == ',')/*if the content is comma*/
					{
						destroyList(commandHead);
						return 8;
					}
					setName = currentContent; /*in symdiff set we expect only sets names*/
					if(isValidSetName(setName) == FALSE) /*check set name*/
					{
						destroyList(commandHead);
						return 1;
					}
					/*check where to store the current set address*/
					if(setsArr[0] == NULL)
					{
						setsArr[0] = getAddressBySetName(setName); /*get the address of the set*/
						expectedComma = TRUE; /*expecting comma after first set name*/
					}else if(setsArr[1] == NULL)
					{
						setsArr[1] = getAddressBySetName(setName); /*get the address of the set*/
						expectedComma = TRUE; /*expecting comma after first set name*/
					}else if(setsArr[2] == NULL)
					{
						setsArr[2] = getAddressBySetName(setName); /*get the address of the set*/
						if((getContent(commandHead, (i + 1)) != '\0') != 0) /*if there is further content after third set name*/
						{
							destroyList(commandHead);
							return 7;
						}
						break;
					}
				}else{ /*if we expect a comma*/
					if(currentContent[0] != ',') /*if the string is not a comma*/
					{
						destroyList(commandHead);
						return 9;
					}
					expectedComma = FALSE; /*after a comma we do not expect another one*/
				}
				/*get next string in the list*/
				i++;
				currentContent = getContent(commandHead, i);
			}
			/*check we fill in the three sets*/
			if(setsArr[0] == NULL || setsArr[1] == NULL || setsArr[2] == NULL)
			{
				destroyList(commandHead);
				return 7;
			}
			/*setName now holds the third set name which is the set needs to re-set*/
			if(strcmp(setName,"SETA") == 0)
			{
				SETA = symdiff_set(setsArr);
			}else if(strcmp(setName,"SETB") == 0)
			{
				SETB = symdiff_set(setsArr);
			}else if(strcmp(setName,"SETC") == 0)
			{
				SETC = symdiff_set(setsArr);
			}else if(strcmp(setName,"SETD") == 0)
			{
				SETD = symdiff_set(setsArr);
			}else if(strcmp(setName,"SETE") == 0)
			{
				SETE = symdiff_set(setsArr);
			}else if(strcmp(setName,"SETF") == 0)
			{
				SETF = symdiff_set(setsArr);
			}else
			{
				destroyList(commandHead);
				return 1;
			}
		}else
		{
			destroyList(commandHead);
			return 2;
		}
		destroyList(commandHead);
	}
	/*if we got to this point with no return - the command executed successfully*/
	return 0;
}/*end of method executeCommand*/
