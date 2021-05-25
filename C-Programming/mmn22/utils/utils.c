#include "./utils.h"

/*
splitStr function gets a command as string and split it into a strings linked list. the function will remove white spaces from the strings.
the function gets the address of the head of the list and the command as a string.
the function doesnt return anything
*/
void splitStr(Node *head,char command[])
{
	char tempStr[MAX_COMMAND_SIZE] = ""; /*string that will hold the temp partial string*/
	int commandIndex = 0; /*the command index*/
	int strIndex = 0; /*temp str index*/
	Node* tempNode = NULL; /*temp node to be set*/
	Node* tempNewNode = NULL;  /*temp new node if needed*/
	if(head != NULL)
	{
		tempNode = head; /*first init temp node to head of the list*/
		while(commandIndex < strlen(command)) /*iterate the chars in the command*/
		{
			strcpy(tempStr,""); /*empty the current temp string*/
			strIndex = 0; /*init string index*/
			/*itrate the command until white space OR comma*/
			while(commandIndex < strlen(command) && strIndex < MAX_COMMAND_SIZE && isspace(command[commandIndex]) == 0 && command[commandIndex] != ',')
			{
				/*create partial string*/
				tempStr[strIndex] = command[commandIndex];
				strIndex++;
				commandIndex++;
			}
			tempStr[strIndex] = '\0'; /*close string properly*/
			strcpy(tempNode->str,tempStr); /*set the node->str to the current partial string*/
			if(commandIndex >= strlen(command) || strIndex >= MAX_COMMAND_SIZE) /*check if there is more content*/
			{
			}else{
				if(isspace(command[commandIndex]) != 0) /*if its a white space*/
				{
					commandIndex++; /*check if there is content after the white space*/
					if(commandIndex < strlen(command) && strIndex > 0)
					{
						/*create new node*/
						tempNewNode = newNode();
						if(tempNewNode != NULL) /*check allocation*/
						{
							tempNode->next = tempNewNode;
							tempNode = tempNode->next;
						}else{
							break;
						}
					}
				}else{ /*if its not a white space - > it is a comma*/
					if(strIndex == 0) /*if its only a comma with no other chars*/
					{
						strcpy(tempNode->str,",");
						commandIndex++;
					}
					if(commandIndex < strlen(command))/*if there is still content after the comma*/
					{
						/*create new node*/
						tempNewNode = newNode();
						if(tempNewNode != NULL)/*check allocation*/
						{
							tempNode->next = tempNewNode;
							tempNode = tempNode->next;
						}else{
							break;
						}
					}
				}
			}
		}
	}
}/*end of method splitStr*/

/*
newNode is function that creates a new string list node and reurns its address
*/
Node* newNode()
{
	Node* temp = NULL;
	temp = (Node *) malloc(sizeof(Node)); /*allocate memory*/
	if(temp != NULL)/*check allocation*/
	{
		strcpy(temp->str,""); /*init str to empty string*/
		temp->next = NULL; /*init next pointer*/
	}
	return temp;
} /*end of method newNode*/

/*
destroyList is a function that gets a linked list head and free all used memory by the list
*/
void destroyList(Node* head)
{
	if(head == NULL) /*stop condition for recursive calls*/
	{
		return;
	}
	destroyList(head->next); /*recursive call with next node*/
	free(head); /*free memory from last node to first*/
} /*end of method destroyList*/

/*
getContent is function that gets linked list head and the index of the node we would like to know is content, and returns the content in the node.
*/
char* getContent(Node* head, int index)
{
	int i = 0; /*iteration index*/
	while(head != NULL && i < index) /*iterate the list until requested index or end of list*/
	{
		head = head->next;
		i++;
	}
	/*return the appropriate content*/
	if(head != NULL)
	{
		return head->str;
	}else {
		return NULL;
	}
}/*end of method getContent*/

/*
newNumberNode is function that creates a new numbes list node and reurns its address
*/
NumberNode *newNumberNode()
{
	NumberNode* temp = NULL;
	temp = (NumberNode *) malloc(sizeof(NumberNode)); /*allocate new node*/
	if(temp != NULL)/*check allocation*/
	{
		temp->number = -1; /*init number to -1*/
		temp->next = NULL; /*init next*/
	}
	return temp;
}/*end of method newNumberNode*/

/*
destroyNumberList is a function that gets a numbers linked list head and free all used memory by the list
*/
void destroyNumberList(NumberNode* head)
{
	if(head == NULL) /*stop condition for recursive calls*/
	{
		return;
	}
	destroyNumberList(head->next); /*recursive call with next node*/
	free(head); /*free memory from last node to first*/
}/*end of method destroyNumberList*/

/*
getNumberContent is function that gets numbers linked list head and the index of the node we would like to know is content, and returns the content in the node.
*/
int getNumberContent(NumberNode* head, int index)
{
	int i = 0; /*iteration index*/
	while(head != NULL && i < index) /*iterate the list until requested index or end of list*/
	{
		head = head->next;
		i++;
	}
	/*return the appropriate number*/
	if(head != NULL)
	{
		return head->number;
	}else {
		return -1;
	}
}/*end of method getNumberContent*/

/*
addNumberToList is a function that add a number to numbers linked list.
it gets the list head address and the current number to be added and create a new node to the list with the number.
*/
void addNumberToList(NumberNode* head, int currentNumber)
{
	NumberNode* temp = NULL;
	while(head->next != NULL) /*iterate the list until the last node*/
	{
		head = head->next;
	}
	temp = newNumberNode(); /*create new node*/
	if(temp != NULL)/*check allocation*/
	{
		temp->number = currentNumber; /*put the number to add*/
		head->next = temp; /*connect new node to the list*/
	}
}/*end of method addNumberToList*/
