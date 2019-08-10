#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "kallocator.h"
//#include "list_sol.c"
#include <string.h>

// list_sol.h header file:
struct nodeStruct {
    struct nodeStruct *next;
    int mem_size;
    int* mem_ptr;   
};

/*
 * Allocate memory for a node of type struct nodeStruct and initialize
 * it with the value item. Return a pointer to the new node.
 */
struct nodeStruct* List_createNode(int* mem_ptr);

/*
 * Insert node at the head of the list.
 */
void List_insertHead (struct nodeStruct **headRef, struct nodeStruct *node);

/*
 * Insert node after the tail of the list.
 */
void List_insertTail (struct nodeStruct **headRef, struct nodeStruct *node);

/*
 * Count number of nodes in the list.
 * Return 0 if the list is empty, i.e., head == NULL
 */
int List_countNodes (struct nodeStruct *head);

/*
 * Return the first node holding the value mem_ptr, return NULL if none found
 */
//struct nodeStruct* List_findNode(struct nodeStruct *head, int size);

/*
 * Delete node from the list and free memory allocated to it.
 * This function assumes that node has been properly set (by for example
 * calling List_findNode()) to a valid node in the list. If the list contains
 * only node, the head of the list should be set to NULL.
 */
void List_deleteNode (struct nodeStruct **headRef, struct nodeStruct *node);

/*
 * Sort the list in ascending order based on the item field.
 * Any sorting algorithm is fine.
 */
void List_sort (struct nodeStruct **headRef);

// list_sol.c file:
static _Bool doSinglePassOnSort(struct nodeStruct **headRef);
static void swapElements(struct nodeStruct **previous, struct nodeStruct *nodeA, struct nodeStruct *b);
static void print_mem(struct nodeStruct **headFree, struct nodeStruct **headAlloc);
void List_insertAfter (struct nodeStruct **nodeRef, struct nodeStruct *node);
void List_removeNode (struct nodeStruct **headRef, struct nodeStruct *node);
struct nodeStruct* List_first_fit(struct nodeStruct *head, int size);
struct nodeStruct* List_best_fit(struct nodeStruct *head, int size);
struct nodeStruct* List_worst_fit(struct nodeStruct *head, int size);
struct nodeStruct* List_findPtrNode(struct nodeStruct *head, int* int_ptr);


/*
 * Allocate memory for a node of type struct nodeStruct and initialize
 * it with the value mem_ptr. Return a pointer to the new node.
 */
struct nodeStruct* List_createNode(int* mem_ptr)
{
	struct nodeStruct *pNode = malloc(sizeof(struct nodeStruct));
	if (pNode != NULL) {
		pNode->mem_ptr = mem_ptr;
	}
	return pNode;
}

/*
 * Insert node at the head of the list.
 */
void List_insertHead (struct nodeStruct **headRef, struct nodeStruct *node)
{
	node->next = *headRef;
	*headRef = node;
}

/*
 * Insert node after the tail of the list.
 */
void List_insertTail (struct nodeStruct **headRef, struct nodeStruct *node)
{
	node->next = NULL;

	// Handle empty list
	if (*headRef == NULL) {
		*headRef = node;
	}
	else {
		// Find the tail and insert node
		struct nodeStruct *current = *headRef;
		while (current->next != NULL) {
			current = current->next;
		}
		current->next = node;
	}
}
//insert node after nodeRef provided
void List_insertAfter (struct nodeStruct **nodeRef, struct nodeStruct *node){
	// Handle empty list
	if (*nodeRef == NULL) {
		*nodeRef = node;
	}
	struct nodeStruct *current = *nodeRef;


	struct nodeStruct *nextNode = current->next;
	current->next = node;

	node->next = nextNode;


}

/*
	Print how memory looks like
 */
 static void print_mem(struct nodeStruct **headFree,struct nodeStruct **headAlloc ){
	 
	 //printf("free list looks like :\n");
	struct nodeStruct* current=  *headFree;
	while(current != NULL){
		//printf("item is %d, size is %d, ptr is %p \n",*(current->mem_ptr),current->mem_size, current->mem_ptr);
		current = current->next;
	}
	//printf("\nAlloc list looks like\n");
	current=  *headAlloc;
	while(current != NULL){
		//printf("size is %d contains %d,ptr is %p \n",current->mem_size,*(current->mem_ptr), current->mem_ptr);
		current = current->next;
	}
	//printf("\n\n");
 }

/*
 * Count number of nodes in the list.
 * Return 0 if the list is empty, i.e., head == NULL
 */
int List_countNodes (struct nodeStruct *head)
{
	int count = 0;
	struct nodeStruct *current = head;
	while (current != NULL) {
		current = current->next;
		count++;
	}
	return count;
}

/*
 * Return the first node holding the value mem_ptr, return NULL if none found
 */
struct nodeStruct* List_first_fit(struct nodeStruct *head, int size)
{
	struct nodeStruct *current = head;
	while (current != NULL) {
			if (current->mem_size >= size) {
				////printf("found item SPACE %p\n", *(current->mem_ptr));
			return current;
			}
		
		current = current->next;
	}
	return NULL;
}
struct nodeStruct* List_best_fit(struct nodeStruct *head, int size){
	struct nodeStruct *current = head;
	struct nodeStruct *bestFit = head;
	while (current != NULL) {
		////printf("looking for nodeNum %d\n", current->nodeNum);
			if (current->mem_size >= size) {
				////printf("found nodeNum SPACE %d\n", current->nodeNum);
				if(current->mem_size < bestFit->mem_size){
					bestFit= current;
				}
				
			}
		
		current = current->next;
	}
	return bestFit;
}
struct nodeStruct* List_worst_fit(struct nodeStruct *head, int size){
	struct nodeStruct *current = head;
	struct nodeStruct *worstFit = head;
	while (current != NULL) {
		//printf("looking for nodeNum %d\n", current->nodeNum);
			if (current->mem_size >= size) {
				//printf("found nodeNum SPACE %d\n", current->nodeNum);
				if(current->mem_size > worstFit->mem_size){
					worstFit= current;
				}
				
			}
		
		current = current->next;
	}
	return worstFit;
}
/*
 * Return the first node holding the value mem_ptr, return NULL if none found
 */
struct nodeStruct* List_findNode(struct nodeStruct *head, int* int_ptr)
{
	struct nodeStruct *current = head;
	while (current != NULL) {
		//printf("looking for nodeNum %d\n", current->nodeNum);
			if (current->mem_ptr >= int_ptr) {
				//printf("found item POINTER %d\n", *(current->mem_ptr));
				return current;
			}
		
		current = current->next;
	}
	return NULL;
}
/*
 * find for printContig
 */
struct nodeStruct* List_findPtrNode(struct nodeStruct *head, int* int_ptr)
{
	struct nodeStruct *current = head;
	while (current != NULL) {
		//printf("looking for nodeNum %d\n", current->nodeNum);
			if (current->mem_ptr == int_ptr) {
				//printf("found item POINTER %d\n", *(current->mem_ptr));
				return current;
			}
		
		current = current->next;
	}
	return NULL;
}

/*
 * Delete node from the list and free memory allocated to it.
 * This function assumes that node has been properly set (by for example
 * calling List_findNode()) to a valid node in the list. If the list contains
 * only node, the head of the list should be set to NULL.
 */
void List_deleteNode (struct nodeStruct **headRef, struct nodeStruct *node)
{
	assert(headRef != NULL);
	assert(*headRef != NULL);

	// Is it the first element?
	if (*headRef == node) {
		*headRef = node->next;
	}
	else {
		// Find the previous node:
		struct nodeStruct *previous = *headRef;
		while (previous->next != node) {
			previous = previous->next;
			assert(previous != NULL);
		}

		// Unlink node:
		assert(previous->next == node);
		previous->next = node->next;
	}

	// Free memory:
	free(node);
}
/*
like delete but dont free
 */
void List_removeNode (struct nodeStruct **headRef, struct nodeStruct *node)
{
	assert(headRef != NULL);
	assert(*headRef != NULL);

	// Is it the first element?
	if (*headRef == node) {
		*headRef = node->next;
	}
	else {
		// Find the previous node:
		struct nodeStruct *previous = *headRef;
		while (previous->next != node) {
			previous = previous->next;
			assert(previous != NULL);
		}

		// Unlink node:
		assert(previous->next == node);
		previous->next = node->next;
	}

	// Free memory:
	//free(node);
}



/*
 * Sort the list in ascending order based on the mem_ptr field.
 * Any sorting algorithm is fine.
 */
void List_sort (struct nodeStruct **headRef)
{
	while (doSinglePassOnSort(headRef)) {
		// Do nothing: work done in loop condition.
	}
}
static _Bool doSinglePassOnSort(struct nodeStruct **headRef)
{
	_Bool didSwap = false;
	while (*headRef != NULL) {
		struct nodeStruct *nodeA = *headRef;
		// If we don't have 2 remaining elements, nothing to swap.
		if (nodeA->next == NULL) {
			break;
		}
		struct nodeStruct *nodeB = nodeA->next;

		// Swap needed?
		if (nodeA->mem_ptr > nodeB->mem_ptr){
			swapElements(headRef, nodeA, nodeB);
			didSwap = true;
		}

		// Advance to next elements
		headRef = &((*headRef)->next);
	}
	return didSwap;
}
static void swapElements(struct nodeStruct **previous,
		struct nodeStruct *nodeA,
		struct nodeStruct *nodeB)
{
	*previous = nodeB;
	nodeA->next = nodeB->next;
	nodeB->next = nodeA;
}

struct KAllocator {
    enum allocation_algorithm aalgorithm;
    int size;
    void* memory;
    // Some other data members you want, 
    // such as lists to record allocated/free memory
    struct nodeStruct *headAlloc ;
    struct nodeStruct *headFree ;

};
int nodeCount = -1;
int* last_ptr = NULL;
struct KAllocator kallocator;

void printContig(); //for testing
void join_contig(struct nodeStruct **headFree);
////// ASSERT FUNCTIONS FOR TESTING //
void alwaysTrueAssertions();
////////// STATS HELPER FUNCTIONS //
int available_memory();
int alloc_size();
int alloc_chunks();
int count_free_chunks();
int find_smallest_free_chunk_size();
int find_largest_free_chunk_size();

// HELPER FUNCTIONS //
//traverse linked list to find memory
int* find_memory(int _size){
    //finding a node with enough space
    struct nodeStruct* avail_space = NULL;
    //printf("algorithm is %d\n",kallocator.aalgorithm);
    switch(kallocator.aalgorithm){
        case 0:
            //printf("first fit\n");
            avail_space = List_first_fit(kallocator.headFree, _size);
            break;
        case BEST_FIT: 
            //printf("best fit\n");
            avail_space = List_best_fit(kallocator.headFree, _size);
            break;
        case WORST_FIT:
            //printf("worse fit\n");
            avail_space = List_worst_fit(kallocator.headFree, _size);
            break;
        default: 
            //printf("choose correct algorithm please\n");
            assert(false);
            break;
    }
    assert(avail_space!=NULL);
    //printf("found free space %p\n", avail_space->mem_ptr);

    //create allocated node for allocated linked list
    struct nodeStruct* take_space = List_createNode(avail_space->mem_ptr);

    take_space->mem_size = _size;
    //insert node into allocated linked list
    List_insertTail (&(kallocator.headAlloc), take_space);

    if(avail_space->mem_size>sizeof(int)){
        //remove memory from free space
        avail_space->mem_size= avail_space->mem_size - _size;
        //update avail_space memory pointer
        avail_space->mem_ptr++;
    }
    else{
        List_deleteNode (&(kallocator.headFree), avail_space);
    }
    
    return take_space->mem_ptr;
}
//join contigous memory in free linked list
void join_contig(struct nodeStruct **headFree){
    //sort on ptrs
    //printf("sorting\n");
    List_sort (headFree);
    //print_mem(&(kallocator.headFree), &(kallocator.headAlloc));

    // join the contig memory sections
    int memSize=0;
    struct nodeStruct* current  = *headFree;
    struct nodeStruct* current_next= NULL;
    while(current != NULL){
        if(current->next!=NULL){
            current_next = current->next;
        }
        else{
            break;
        }
        memSize= current->mem_size;
        //see if two free memories are beside each other
        if(current->mem_ptr +memSize/4 == current_next->mem_ptr){
            //printf("\titem is %d with size %d and pointer %p should join\n\titem %d with size %d and pointer %p\n", *(current->mem_ptr), current->mem_size, current->mem_ptr, *(current_next->mem_ptr), current_next->mem_size, current_next->mem_ptr);
            //update the memory size
            current->mem_size= current->mem_size + current_next->mem_size;
            //delete the uneeeded node
            List_deleteNode (headFree, current_next);

        }
        else{
            //to check if to make sure current and new next are not next to each other
            current = current->next;
        }
		
	}


}
//prints all spaces contiguously, if free space print free space
void printContig(){
    //printf("\n\n PRINTING CONTIGUOUS \n");
    int count = kallocator.size;
    int* a_ptr = kallocator.memory;
    //printf("beginning of space starts at %p\n", a_ptr);

    for(int i = 0; i<count; i++){
        //printf("pointer is %p\n",a_ptr );
        if(List_findPtrNode(kallocator.headAlloc, a_ptr) !=NULL){ //means it is allocated
            //printf("contains %d\n", *a_ptr);
        }
        else{
            //printf("\nblock is free block\n");
        }
        a_ptr++;
        if(i ==15){
            break;
        }
    }

}

////// PROFS FUNCTIONS //
void initialize_allocator(int _size, enum allocation_algorithm _aalgorithm) {
    assert(_size > 0);
    kallocator.aalgorithm = _aalgorithm;
    kallocator.size = _size;
    kallocator.memory = malloc((size_t)kallocator.size);

    // Add some other initialization 
    kallocator.headAlloc = NULL;
    kallocator.headFree = NULL;
    //first node represents the entire empty memory
    struct nodeStruct* aNode = List_createNode(kallocator.memory);
    aNode->mem_size = kallocator.size;
    aNode->mem_ptr = kallocator.memory;
   
    List_insertHead(&(kallocator.headFree), aNode);

   

    print_mem(&(kallocator.headFree),&(kallocator.headAlloc));

}

void destroy_allocator() {
    free(kallocator.memory);
    // free other dynamic allocated memory to avoid memory leak

    //free free list
    struct nodeStruct *current = kallocator.headFree;
    struct nodeStruct* tmp = current;
	while (current != NULL) {
		//printf("looking for nodeNum %d\n", current->nodeNum);
        tmp = current;
		current = current->next;

        free(tmp);
		
	}
    current = kallocator.headAlloc;
    tmp = current;
	while (current != NULL) {
		//printf("looking for nodeNum %d\n", current->nodeNum);
        tmp = current;
		current = current->next;

        free(tmp);
		
	}
    
}

void* kalloc(int _size) {
    //printf("*kalloc called\n");
    if( available_memory()<_size){
        printf("sorry not enough memory\n");
        return NULL;
    }
   
    void* ptr = NULL;
    // make sure there is room in free linked list
    assert(find_largest_free_chunk_size()>= _size);
    // Allocate memory from kallocator.memory 
    // ptr = address of allocated memory
    // add meta-data to linked list
    ptr = find_memory(_size);
    print_mem(&(kallocator.headFree),&(kallocator.headAlloc));
    //printf("returning pointer %p, contains %d\n", (void*)ptr, *ptr);
    alwaysTrueAssertions(); //ensure obvious bugs free
    return ptr;
}

void kfree(void* _ptr) {
    //printf("\n*kfree called\n");
    assert(_ptr != NULL);
    //free(((int*)_ptr));

   struct nodeStruct* dealloc_space = List_findNode(kallocator.headAlloc, ((int*)_ptr));
   if(dealloc_space == NULL){
       printf("sorry could not find allocated space to free\n");
       return;
   }
   *(dealloc_space->mem_ptr) = 0;
    //printf("freeing ptr %p and item %d\n", dealloc_space->mem_ptr, *((int*)_ptr));
    List_removeNode (&(kallocator.headAlloc), dealloc_space);
    List_insertHead (&(kallocator.headFree), dealloc_space);
    //join any contigous blocks
    join_contig(&(kallocator.headFree));
    print_mem(&(kallocator.headFree),&(kallocator.headAlloc));
    alwaysTrueAssertions(); //ensure obvious bugs free

}

int compact_allocation(void** _before, void** _after) {
    //printf("\n\nCOMPPACTINGGGG\n");
    List_sort (&(kallocator.headFree));
    List_sort (&(kallocator.headAlloc));
    int j = 0;
   // print_mem(&(kallocator.headFree), &(kallocator.headAlloc));

    //coppy to before
    struct nodeStruct *current = kallocator.headAlloc;
	while (current != NULL) {
		_before[j] = current->mem_ptr;
        j++;
		current = current->next;
	}

    //do the compacting
    int temp_num=0 ;
    struct nodeStruct *first_free = kallocator.headFree;
    current = kallocator.headAlloc;
	while (current != NULL) {
        first_free = kallocator.headFree;
        //printf("comparing free %p and alloc %p\n",first_free->mem_ptr,current->mem_ptr );
        if(first_free->mem_ptr<current->mem_ptr){
            //printf("found free 20 %p\n", current->mem_ptr);
            //switching
            temp_num = *(current->mem_ptr);
            current->mem_ptr = first_free->mem_ptr;
            *(current->mem_ptr) = temp_num;
            //update pointer (move over)
            first_free->mem_ptr++;
            *(first_free->mem_ptr) = 0;
        
            List_sort (&(kallocator.headFree));
            List_sort (&(kallocator.headAlloc));
            join_contig(&(kallocator.headFree));
            //print_mem(&(kallocator.headFree), &(kallocator.headAlloc));
            current = kallocator.headAlloc;

            //break;
        }
    
		current = current->next;
	}
    //coppy to after
    current = kallocator.headAlloc;
    j=0;
	while (current != NULL) {
		_after[j] = current->mem_ptr;
        j++;
		current = current->next;
	}
   

    //print_mem(&(kallocator.headFree), &(kallocator.headAlloc));
    int compacted_size = alloc_chunks();
    return compacted_size;
}
////// ASSERT FUNCTIONS FOR TESTING //
void alwaysTrueAssertions(){
    //total memory managed should be equal to what was given initially
    assert(available_memory() + alloc_size() == kallocator.size);
    //assert contigous memory in free list is joined 
    //assume free list is sorted
    List_sort (&(kallocator.headFree));
    int memSize=0;
    struct nodeStruct* current  = kallocator.headFree;
    struct nodeStruct* current_next= NULL;
    while(current != NULL){
        if(current->next!=NULL){
            current_next = current->next;
        }
        else{
            break;
        }
        memSize= current->mem_size;
        //see if two free memories are beside each other
        if(current->mem_ptr +memSize/4 == current_next->mem_ptr){
            //printf("there is contigous memory that was not joined\n");
            assert(false);

        }
        else{
            //to check if to make sure current and new next are not next to each other
            current = current->next;
        }
	}
}
////////// STATS HELPER FUNCTIONS //
int available_memory() { //also calculate smallest and largest free chunks

    int available_memory_size = 0;
    // Calculate available memory size
    struct nodeStruct* current  = kallocator.headFree;
    while(current != NULL){
        //add up available mem
        available_memory_size = available_memory_size + current->mem_size;
        current = current->next;
	}
    return available_memory_size;
}
int alloc_size(){
    //printf("finding alloc size\n");
    int size = 0;
    // Calculate available memory size
    struct nodeStruct* current  = kallocator.headAlloc;
    while(current != NULL){
        size = size + current->mem_size;
        current = current->next;
	}
    return size;
}
int alloc_chunks(){
    int chunks = 0;
    // Calculate available memory size
    struct nodeStruct* current  = kallocator.headAlloc;
    while(current != NULL){
        chunks++;
        current = current->next;

	}
    return chunks;
}
int count_free_chunks(){ 
    int chunks = 0;
    // Calculate available memory size
    struct nodeStruct* current  = kallocator.headFree;
    while(current != NULL){
        chunks++;
        current = current->next;

	}
    return chunks;
}
int find_smallest_free_chunk_size(){
    int chunk_size = kallocator.size;
    // Calculate available memory size
    struct nodeStruct* current  = kallocator.headFree;
    while(current != NULL){
        if(current->mem_size< chunk_size){
            chunk_size = current->mem_size;
        }
        current = current->next;

	}
    return chunk_size;
}
int find_largest_free_chunk_size(){
    int chunk_size = 0;
    // Calculate available memory size
    struct nodeStruct* current  = kallocator.headFree;
    while(current != NULL){
        if(current->mem_size> chunk_size){
            chunk_size = current->mem_size;
        }
        current = current->next;

	}
    return chunk_size;
}


void print_statistics() {
    print_mem(&(kallocator.headFree), &(kallocator.headAlloc));
    //printf("Printing Statisitics\n");
    int allocated_size = alloc_size();
    int allocated_chunks = alloc_chunks();
    int free_size = available_memory();
    int free_chunks = count_free_chunks();
    int smallest_free_chunk_size = find_smallest_free_chunk_size(); 
    int largest_free_chunk_size = find_largest_free_chunk_size();               

    // Calculate the statistics

    printf("Allocated size = %d\n", allocated_size);
    printf("Allocated chunks = %d\n", allocated_chunks);
    printf("Free size = %d\n", free_size);
    printf("Free chunks = %d\n", free_chunks);
    printf("Largest free chunk size = %d\n", largest_free_chunk_size);
    printf("Smallest free chunk size = %d\n", smallest_free_chunk_size);
        
}



