#include <stdlib.h>
#include <stdio.h>
#include "LL.h"



unsigned int removeNumber(LL_t * L, int target);




void spliceinto(LL_t * L, int x, int i) {

	node_t * newNode = malloc(sizeof(node_t));
	newNode->data= x;
	//list empty
	if(L->head==NULL){
		//assert(L->tail == NULL);
		L->head = newNode;
		L->tail =newNode;
		//L->head->next= NULL;
		newNode->next = NULL; // SEG FAULT???
		
	}
	else if(i<=0){
		//new head
		//printf("new head\n");
		newNode->next=L->head;
		
		L->head=newNode;
	}
	else{
		//typical case
		node_t * prev= L->head;
		node_t * current = L->head->next;
		//printf("prev %d and curr %d\n", prev->data, current->data);
		int index=0;
		
		while (index != i-1) {
			index++;
			prev= prev->next;
        		current = current->next;
	
			//printf("p %d ", prev->data);
			//printf("c %d ", current->data);
	
		}
		putchar('\n');
		
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
	if(L->head->next == L->tail){ // base case if 2 numbers left
		int temP;
		temP = L->tail->data;
		L->tail->data= L->head->data;
		L->head->data= temP;
		return;
	}
	if(L->head== L->tail){ // base case if 1 number left
		return;
	}
	LL_t* afterCat = L; // preserve L pointer for later
	LL_t* front = LLcreate();
	node_t* tempF= malloc(sizeof(node_t));
	front->head= tempF;
	front->tail= tempF;
	//printf("tail data %d \n", L->tail->data);
	
	front->head->data= L->tail->data; //switch back to front
	
	
	
	LL_t* back = LLcreate();

	node_t* tempB= malloc(sizeof(node_t));
	back->head = tempB;
	back->tail = tempB;
	back->head->data= L->head->data; //switch front to back

	//printf("back head %d\n", back->head->data);
	

	
	node_t * prev= L->head;
	node_t * current = L->head->next;
	while (current->next != NULL) {
			//index++;
			prev= prev->next;
        		current = current->next;
	
			//printf("p %d ", prev->data);
			//printf("c %d ", current->data);
	
	}
	printf("\n");
	L->head = L->head->next;
	L->tail= prev;
	L->tail->next=NULL;
	
	//LLprint(L);
	reverse(L);
	LLcatenate(front, L);
	LLcatenate(front, back);
	
	//LL_t* L= LLcreate();
	L= afterCat;
	
	*L= *front;
	
return;
}



unsigned int numberOfEvens (LL_t * L) ;

int main(){

LL_t* L = LLcreate();


LLappend(L, 5);	
LLappend(L, 6);
LLappend(L, 7);
LLappend(L, 8);
LLappend(L, 9);
LLappend(L, 10);

//spliceinto(L, 55, 5);

reverse(L);

LLprint(L);




return 0;
}
