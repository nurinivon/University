#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include<ctype.h>

#define MAX_COMMAND_SIZE 1000

/*
enum definition for boolean
*/
typedef enum
{
	TRUE,
	FALSE
} Boolean;

/*
struct defintion for node.
this node is meant to use for string linked list.
*/
typedef struct Node
{
	char str[MAX_COMMAND_SIZE];
	struct Node* next;
}Node;

/*
struct defintion for NumberNode.
this node is meant to use for integers linked list.
*/
typedef struct NumberNode
{
	int number;
	struct NumberNode* next;
}NumberNode;

void splitStr(Node * head,char command[]);
Node *newNode();
void destroyList(Node* head);
char* getContent(Node* head, int index);
NumberNode *newNumberNode();
void destroyNumberList(NumberNode* head);
int getNumberContent(NumberNode* head, int index);
void addNumberToList(NumberNode* head, int currentNumber);
