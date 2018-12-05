#include <stdio.h>
#include <stdlib.h>
//	QUESTION 1
typedef struct{
	int * data; // array of the data on the queue
	int len; //how many elements in list
	int N; // max amount of elements in the data
} queue_t;
//print will be O(n)
void print(queue_t* list){
	for(int i=0; i< list->len; i++){
		printf("%d ", list->data[i]); // print all elements: O(n)
	}
	printf("\n");
}
//enqueue will be O(1)
int enqueue(queue_t* list, int add){
	
	if(list->N == list -> len){ 
		printf("reached maximum length\n");
		return -1;
	}
	if(list==NULL){
		printf("failed to enqueue\n");
		return -1;
	}
	list->data[list->len] = add; // add element to end of list
	list->len++; 
	
return 0;
	
}
//dequeue will be O(1)
int* dequeue(queue_t* list){

	int* ret = malloc(sizeof(int));
	if(list->len == 0){
		return NULL;
	}
	if(list->len != 0){
		*ret=list->data[0]; // save the first element to dequeue  
		list->data[0]= list->data[list->len-1]; //replace first element with another element from list to avoid copying entire array
		list->len--;
	}
	return ret; //return dequeued element
}
//empty will be O(1)
int isEmpty(queue_t* list){
	if(list->len==0){
		return 1; //true
	}
	else{
		return 0; // false not empty
	}
}
unsigned int size(queue_t*list){
	unsigned int size = list->len;
	return size;
}
	

