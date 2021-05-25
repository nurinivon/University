#include <stdio.h>

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
