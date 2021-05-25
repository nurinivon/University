#include "./set.h"

/*
Nur Nivon
this program read pre defined commands from standard input and execute them, for each command it will print the appropriate response.
*/

/*
global variables for the sets that will be used in the program. the sets are defined in set.c.
*/
extern set* SETA;
extern set* SETB;
extern set* SETC;
extern set* SETD;
extern set* SETE;
extern set* SETF;

/*
global variable which holds the response options for command execution
*/
char *REPONSE_DICTIONARY[] = {
	"Command Executed Successfully",
	"Undefined set name",
	"Undefined command name",
	"Invalid set member – value out of range",
	"List of set members is not terminated correctly",
	"Invalid set member – not an integer",
	"Missing parameter",
	"Extraneous text after end of command",
	"Multiple consecutive commas",
	"Missing comma",
	"Illegal comma"
};

/*
destroySets is a function that free all the allocated memory we are using in the program.
the function has no parameters and it doesnt return anything
*/
void destroySets()
{
	free(SETA);
	free(SETB);
	free(SETC);
	free(SETD);
	free(SETE);
	free(SETF);
}/*end of method destroySets*/

/*
userWantToStop is a function that gets the command to be executed before it is executed and check if it is a 'stop' command.
the function  get the command as parameter. and returns Boolean TRUE / FALSE
*/
Boolean userWantToStop(char command[])
{
	Boolean result = TRUE; /*the result init with FALSE*/
	int i = 0; /*index for the string chars*/
	char c; /*will hold the current char*/
	while(result == TRUE && i < strlen(command)) /*iterate the string chars while command looks like 'stop'*/
	{
		c = command[i];
		if(i == 0 && c != 's')
		{
			result = FALSE;
		}else if(i == 1 && c != 't')
		{
			result = FALSE;
		}else if(i == 2 && c != 'o')
		{
			result = FALSE;
		}else if(i == 3 && c != 'p')
		{
			result = FALSE;
		}else if(i > 3 && isspace(c) == 0)/*white spaces are allowed after 'stop command', but not other chars*/
		{
			result = FALSE;
		}
		i++;
	}
	return result;
}/*end of method userWantToStop*/

/*
main method is the interface for the program which read the text from standard input and execute the commands.
*/
int main()
{
	int c; /*will hold the current char, is int in order to be able to identify EOF*/
	char currentCommand[MAX_COMMAND_SIZE] = ""; /*current command as string*/
	unsigned int commandSize = 0; /*the command chars index*/
	int currentResponse = 0; /*the response from command execution*/
	/*initialize sets*/
	SETA = newSet();
	SETB = newSet();
	SETC = newSet();
	SETD = newSet();
	SETE = newSet();
	SETF = newSet();
	printf("\nPlease Enter A Command: \n");
	while ( (c = getchar()) && (c != EOF) ) /*while there is input from standard input*/
	{
		c = (char) c; 
		if(c != '\n') /*'\n' is used to seperate commands*/
		{
			/*creation of command string*/
			currentCommand[commandSize] = c;
			commandSize++;
		}else{
			currentCommand[commandSize] = '\0'; /*close string properly*/
			commandSize++;
			printf("\nYour Command Is :\n%s",currentCommand); /*print the collected command to the output*/
			if(userWantToStop(currentCommand) == FALSE) /*check if user wants to stop*/
			{
				/*user doesnt want to stop - command will be executed*/
				currentResponse = executeCommand(currentCommand); /*save the response*/
				printf("\n%s\n\n",REPONSE_DICTIONARY[currentResponse]); /*print the response*/
				/*empty the command for next iteration*/
				strcpy(currentCommand, "");
				commandSize = 0;
				currentResponse = 0;
				printf("\nPlease Enter A Command: \n");
			}else{
				/*user wants to stop*/
				destroySets();
				printf("\nBye Bye !\n");
				return 0;
			}
		}
	}/*end of while*/
	/*if we exit the while - it means we got EOF - and error meesage will be printed*/
	destroySets();
	printf("\nNOTICE: The Program Stopped Unexpectedly, You Should Use 'stop' Command To Exit Properly\n");
	return 0;
}/*end of method main*/
