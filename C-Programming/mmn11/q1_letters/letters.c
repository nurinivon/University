#include <stdio.h>
#include <ctype.h>

/*
Nur Nivon
this program get ASCII code as input and printing the output according to the constraints in assignment 11 -> question 1.
*/

/*
switchFlag is a method that get a char as input and returns char also.
the assumption is the char contains '0' or '1', and the method switches the char value accordingly.
*/
char switchFlag (char flag)
{
	if(flag == '0')
	{
		return '1';
	}
	return '0';
}

int main ()
{
	int c; /*this int will hold the current input from the user until it will be printed*/
	char between_double_quotes = '0'; /*a flag that indicates if we are inside a quote*/
	char beginning_of_sentence = '1'; /*a flag indicates if we are looking for a a char to upper it according to requirements*/
	printf("\nHi there!\nPlease enter your text here. you can either write it manually or use input text files.\n");
	while ( (c = getchar()) && (c != EOF) )
	{
		if(!isdigit(c))
		/*digits dont need to be printed*/
		{
			c = (char)c; /*cast the int to char*/
			c = tolower(c); /*default is lower case*/
			if(beginning_of_sentence == '1' && !isspace(c))
			/*in case we started a new sentence*/
			{
				c = toupper(c);
				beginning_of_sentence = switchFlag(beginning_of_sentence);
			}
			if(c == '\"')
			/*we are starting or ending a quote*/
			{
				between_double_quotes = switchFlag(between_double_quotes);
			}
			else if(between_double_quotes == '1')
			/*if we are inside a quote*/
			{
				c = toupper(c);
			}
			else if(c == '.')
			/*dot indicates end of sentence (if we are not inside quote)*/
			{
				beginning_of_sentence = switchFlag(beginning_of_sentence);
			}
			printf("%c",c); /*print user input after manipulations*/
		}
	} /*end of while*/

	return 0;
} /*end of method main*/
