#include <stdio.h>
#include <stdlib.h>

#define MAX_NODE_SIZE 60

/*
Nur Nivon
this program read text from standard input and save it into a certin data structure: linked list or buffer.
after read the whole text it will be printed in lines with similar length of 60 chars.
*/


/*
enum defenition for the data structures types
*/
typedef enum
{
	BUFFER,
	LINKED_LIST
} DataStructureType;

/*
enum definitions for the reading status of the text
*/
typedef enum
{
	SUCCESS,
	ERROR
} ReadTextStatus;

/*
structure definitions of linked list single node
*/
typedef struct Node
{
	char line[MAX_NODE_SIZE];
	int charIndex;
	struct Node* next;
}Node;

/*
structure definition of buffer
text: the text stored in the buffer as string
charIndex: current last char in the text member
bufferIndex: the buffer will be increase in multiples of MAX_NODE_SIZE, this is the multipiler counter
*/
typedef struct Buffer
{
	char* text;
	int charIndex;
	int bufferIndex;
}Buffer;

/*
newBuffer is a method that craetes a new buffer instance in the memory, and returns its address.
no parameters needed.
returns address to the new buffer.
*/
Buffer* newBuffer()
{
	Buffer* temp = NULL;
	temp = (Buffer *) malloc(sizeof(Buffer));
	if(temp != NULL)
	{
		temp->charIndex = 0;
		temp->bufferIndex = 0;
		temp->text = NULL;
	}
	return temp;
} /*end of method newBuffer*/

/*
destroyBuffer is a method that free the memory used by a buffer.
buffer: is the address of the buffer needed to be detroyed
no return needed.
*/
void destroyBuffer(Buffer* buffer)
{
	free(buffer->text);
	free(buffer);
} /*end of method destroyBuffer*/

/*
newNode is a method that craetes a new linked list node instance in the memory, and returns its address.
no parameters needed.
returns address to the new node.
*/
Node* newNode()
{
	Node* temp = NULL;
	temp = (Node *) malloc(sizeof(Node));
	if(temp != NULL)
	{
		temp->charIndex = 0;
		temp->next = NULL;
	}
	return temp;
} /*end of method newNode*/

/*
destroyList is recursive method to free all the memory used by linked list.
head: the address of the first node in the list.
no return needed.
*/
void destroyList(Node* head)
{
	if(head == NULL)
	{
		return;
	}
	destroyList(head->next);
	free(head);
} /*end of method destroyList*/

/*
addCharToList is a recursive method that gets a single char, and address of linked list, and add the char to the appropriate node in the list under the limitations of MAX_NODE_SIZE of node's line length.
head: address of the first node in linked list.
c: the char to be added
return ReadTextStatus: (ERROR / SUCCESS)
*/
ReadTextStatus addCharToList(Node* head, char c)
{
	Node* tempNewNode = NULL; /*the address of a new node if will be needed.*/
	while(head->next != NULL) /*loop to find the last node*/
	{
		head = head->next;
	}
	if(head->charIndex < MAX_NODE_SIZE - 1) /*check the line length limitation*/
	{
		head->line[head->charIndex] = c; /*add the char to the last free spot*/
		head->charIndex++; /*increase the char index counter*/
		return SUCCESS;	
	}else { /*line length limitation reached*/
		head->line[head->charIndex] = '\0'; /*last char in array*/
		head->charIndex++; /*increase the char index counter*/
		tempNewNode = newNode(); /*create new node for the list*/
		if(tempNewNode != NULL) /*check allocation success*/
		{
			head->next = tempNewNode; /*add new node to list*/
			return addCharToList(head->next, c); /*recursive call to the method*/
		}else { /*allocation failed*/
			return ERROR;
		}
	}
} /*end of method addCharToList*/

/*
addCharToBuffer is a method that gets a single char, and address of a buffer, and add the char to the buffer, the buffer will be increased in MAX_NODE_SIZE every time it ends.
bufferPoiter: address of the buffer.
c: the char to be added
return ReadTextStatus: (ERROR / SUCCESS)
*/
ReadTextStatus addCharToBuffer(Buffer* bufferPoiter, char c)
{
	char* tempLine = NULL; /*the new buffer allocation if will be needed*/
	if(bufferPoiter->charIndex % MAX_NODE_SIZE == 0) /*check limitations*/
	{
		bufferPoiter->bufferIndex++; /*increase the buffer multipiler counter*/
		tempLine = (char *)realloc(bufferPoiter->text, (MAX_NODE_SIZE * bufferPoiter->bufferIndex)); /*reallocate the buffer with bigger size*/
		if(tempLine != NULL) /*check allocation status*/
		{
			bufferPoiter->text = tempLine; /*replace text address with new address*/
		}else {
			/*re allocation failed*/
			return ERROR;
		}
	}
	bufferPoiter->text[bufferPoiter->charIndex] = c; /*add the char to appropriate place*/
	bufferPoiter->charIndex++; /*increase char counter*/
	return SUCCESS;
} /*end of method addCharToBuffer*/

/*
readText text is a method that read text from standard input, char by char, and save it into the chosen data structure (buffer or linked list).
dsType: chosen data structure (buffer or linked list)
bufferPoiter: in case chosen data structure is buffer - this will contain the addres of the buffer otherwise it will be NULL
head: in case chosen data structure is linked list - this will contain the addres of the head of the list otherwise it will be NULL
return ReadTextStatus: (ERROR / SUCCESS)
*/
ReadTextStatus readText(DataStructureType dsType, Buffer* bufferPoiter, Node* head)
{
	int c; /*the char from the input*/
	ReadTextStatus currentStatus; /*the return status*/
	while ( (c = getchar()) && (c != EOF) ) /*standard input until EOF*/
	{
		c = (char) c; /*cast the int to char*/
		if(c != '\n') /*we will not include '\n' char in order to print the lines with similar length*/
		{
			if(dsType == LINKED_LIST) /*check where to save the char*/
			{
				currentStatus = addCharToList(head, c); /*if there is an error the read text funtion will stop here*/
				if(currentStatus == ERROR)
				{
					return ERROR;
				}
			}else { /*dsType is buffer*/
				currentStatus = addCharToBuffer(bufferPoiter, c); /*if there is an error the read text funtion will stop here*/
				if(currentStatus == ERROR)
				{
					return ERROR;
				}
			}
		}
	}
	return SUCCESS; /* if there where no errors until EOF*/
} /*end of method readText*/

/*
printText text is a method that print the stored text from the chosen data structure. the text will be printed with length of MAX_NODE_SIZE.
dsType: chosen data structure (buffer or linked list)
bufferPoiter: in case chosen data structure is buffer - this will contain the addres of the buffer otherwise it will be NULL
head: in case chosen data structure is linked list - this will contain the addres of the head of the list otherwise it will be NULL
*/
void printText(DataStructureType dsType, Buffer* bufferPoiter, Node* head)
{
	int bufferIndex = 0; /*buffer char counter*/
	if(dsType == LINKED_LIST) /*check where the text is stored*/
	{
		while(head != NULL) /*go over all nodes in the list*/
		{
			printf("%s",head->line); /*print node's line as string*/
			printf("\n"); /*seperate the lines*/
			head = head->next;
		}
	}else { /*text stored in a buffer*/
		while(bufferIndex < bufferPoiter->charIndex) /*go over all chars in in buffer*/
		{
			if(bufferIndex % MAX_NODE_SIZE == 0) /*seperate the lines*/
			{
				printf("\n");
			}
			printf("%c",bufferPoiter->text[bufferIndex]); /*print current char*/
			bufferIndex++; /*increase char counter*/
		}
	}
	printf("\n");
} /*end of method printText*/

/*
method main is the inteface for the following proccess:
1. scan data structure type from the user
2. scan text from the user and save it to the chosen data structure
3. print the stored text from the chosen data structure
4. free used dynamic memory
*/
int main()
{
	DataStructureType currentDS; /*user chosen data structure type*/
	Buffer* bufferPointer = NULL; /*the address of the buffer if will be used*/
	Node* listHead = NULL; /*the address of the head of the list if will be used*/
	ReadTextStatus readStatus; /*the status of read text (ERROR / SUCCESS)*/
	char userChoice; /*char to save user choice from input*/
	/*loop for the user to choose data structure type to store the data*/
	do
	{
		printf("\nHi There!\nPlease Choose Prefered Data Structure: (0 = Buffer, 1 Linked List)\n");
		scanf(" %c", &userChoice); /*scan user's choice from input*/
		if(userChoice == '0')
		{
			currentDS = BUFFER;
		}else {
			if(userChoice == '1')
			{
				currentDS = LINKED_LIST;
			}
		}
	}while(currentDS != BUFFER && currentDS != LINKED_LIST);
	/*initialize the appropriate data structure according to user choice*/
	if(currentDS == LINKED_LIST)
	{
		printf("Your Text Will Be Stored In: Linked List\n");
		listHead = newNode();
	}else {
		printf("Your Text Will Be Stored In: Buffer\n");
		bufferPointer = newBuffer();
	}
	printf("\nPlease Enter Text To Store\n");
	readStatus = readText(currentDS,bufferPointer,listHead); /*read text to store from the user, the response will be saved*/
	printf("\n\nThe Text You Stroed Is:\n");
	printText(currentDS,bufferPointer,listHead); /*print the stored text*/
	if(readStatus == ERROR) /*warning notice will be printed after the text*/
	{
		printf("\n******** WARNING: you are out of memory, not all of your text has been read ********");
	}
	/*free used memory of list or buffer*/
	if(currentDS == LINKED_LIST)
	{
		destroyList(listHead);
	}else {
		destroyBuffer(bufferPointer);
	}
	return 0;
} /*end of method main*/
