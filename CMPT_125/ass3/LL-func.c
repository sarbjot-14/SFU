#include <stdlib.h>
#include <stdio.h>
#include "LL.h"
// 	QUESTION 3
unsigned int removeNumber(LL_t * L, int target){
	int removed = 1;// keep track if number was removed
	//empty list
	if(L->head == NULL  && L->tail == NULL){ 
		printf("return 1\n");
		return 1;
	}
	//one number in list
	if(L->head->next==NULL){   
		if(L->head->data == target){
			free(L->head);
			L->head =NULL;
			L->tail=NULL;
			return 0; // removed
		}
		else{
			return 1; // could not remove
		}
	}
	node_t * prev= malloc(sizeof(node_t));
	prev= L->head;
	node_t* current = malloc(sizeof(node_t));
	for ( current = L->head; current != NULL ; current = current->next ) {
		if(current == L-> head){
			if(L->head->data == target){ //keep removing head if new head has target
				free(L->head);
				L->head = L->head->next;
				removed =0;
			}
			else{
				prev= L->head; // this will only happen once
				
			}
		}
		else if(current->next ==NULL){ // removing last element
			if(current->data == target){
				free(current);
				prev->next = NULL;
				L->tail = prev;
				removed =0;
				break;
			}
		}
		else if(current->data == target){ //typical case
			node_t * var = prev->next;
			prev->next = current -> next;
			free(var);
			removed =0;
		}
		else{
			prev= prev->next;
		}
	}
	
	if(removed==0){  //only return 0 if removed number
		return 0;
	}
	else{
		printf("returning 1\n");
		return 1;
	}
printf("returning 1\n");
return 1;  
}
void spliceinto(LL_t * L, int x, int i) {
	node_t * newNode = malloc(sizeof(node_t));
	newNode->data= x;
	if(L->head==NULL){
		 //for empty list
		L->head = newNode;
		L->tail =newNode;
		newNode->next = NULL; 
	}
	else if(i<=0){
		//new head
		newNode->next=L->head;
		L->head=newNode;
	}
	else{
		//typical case
		node_t * prev= L->head;
		node_t * current = L->head->next;
		int index=0;
		
		while (index != i-1) {
			
			index++;
			prev= prev->next;
        		current = current->next;
			if(current == NULL){
				break;
			}
		}
		newNode->data= x;  // Update nodes
		prev->next= newNode;
		newNode->next=current;

		if (current == NULL) {
        	     //new tail
        	    L->tail = newNode;
        	}
	}
}

void reverse(LL_t * L) {
	if(L->head == NULL && L-> tail == NULL){ // if empty list
		return;
	}
	if(L->head->next == L->tail){ // base case if 2 numbers left
		int temP;
		temP = L->tail->data;
		L->tail->data= L->head->data;
		L->head->data= temP;
		return;
	}
	if(L->head== L->tail){ // if 1 number
		return;
	}

	LL_t* front = LLcreate();
	node_t* tempF= malloc(sizeof(node_t));
	front->head= tempF;
	front->tail= tempF;
	front->head->data= L->tail->data; //allocate the data from last node to a temporary list of one node called front
	
	LL_t* back = LLcreate();

	node_t* tempB= malloc(sizeof(node_t));
	back->head = tempB;
	back->tail = tempB;
	back->head->data= L->head->data; //allocate the data from first node to a temporary list of one node called back

	
	node_t * prev= L->head;
	node_t * current = L->head->next;
	// use while loop to find new tail in order to send smaller list back recursively
	while (current->next != NULL) { 
			prev= prev->next;
        		current = current->next;
	}
	// recursively send back a smaller version of the list
	L->head = L->head->next;
	L->tail= prev; //use prev from while loop to update tail
	L->tail->next=NULL;
	reverse(L);

	// join the lists
	LLcatenate(front, L);
	LLcatenate(front, back);
	*L= *front;  //return contents to original pointer
return;
}


unsigned int numberOfEvens (LL_t * L){
	if(L->head ==NULL && L->tail == NULL){
		//empty list
		printf("returning 0\n");
		return 0;
	}
	int remainder;
	unsigned int evenCount=0;
	for ( node_t * current = L->head; current != NULL ; current = current->next ) {
		remainder = current->data % 2;
		if(remainder ==0){
			evenCount++; //count if even number found
		}
		
	}	
	return evenCount;
}


