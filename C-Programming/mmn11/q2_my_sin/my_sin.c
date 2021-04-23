#include <stdio.h>
#include <math.h>
#include "./utils/utils.h"

#define PI 3.14159265

/*
Nur Nivon
this program gets a number (double) from the user (standard input) and calculated the sin of this number using selfmade methods defined in the utils directory, made to meet  the requirements from assignment 11 -> q2. also the program will print the result of sending this number to the standard sin method.
*/
int main ()
{
	double input; /*this variable will store the user's input*/
	printf("Hi there,\nPlease enter a number (double) between 25.0 and -25.0 and i will calculate its sin.\n\n");
	scanf("%lf",&input); /*get the unput from the user*/
	printf("\nyour input was: %f\n",input);
	if(input > 25 || input < -25) /*check that the input is range is in range as defined in the requirements*/
	{
		printf("\nthis is not a valid input.\nPlease enter a number (double) between 25.0 and -25.0 and i will calculate its sin.\n");
	}
	else
	{
		printf("\nthe standard c library sin is: %f\n",sin(input)); /*call to standard sin method*/
		printf("\nmy library sin is:             %f\n",my_sin(input)); /*call to my self made sin method*/
	}
	return 0;
} /*end of method main*/
