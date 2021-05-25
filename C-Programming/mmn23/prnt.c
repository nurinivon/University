#include <stdio.h>

/*
Nuri Nivon
this program is printing to the output the file name of the source code file (.c) which compiled to create the executable image.
this program will work under 2 assumptions:
1. the executable image has the same name as the source code file only just with no extention
2. the source code file is in the same folder with the executable image
*/
int main(int argc, char** args)
{
	char* fileName = args[0];
	int i = 2;
	while(fileName[i] != '\0')
	{
		printf("%c",fileName[i]);
		i++;
	}
	printf(".c\n");
	return 0;
}
