# Project 5: Memory Management - CMPT 300

Simulated Memory Management with C



## Design Decisions & Project Issues:

### 1. Implementing First Fit, Best Fit, and Worst Fit

#### Design Decisions:
1. First Fit
    * Simply find the first node in free linked list and allocate it

2. Best Fit and Worst Fit
    * Iterate over free linked list and keep updating if new best fit is found
    * similar but only update when even worse worst fit is found

### 2. Compacting Memory

#### Design Decisions:

* Once first free block is found, find next alloc block to move to free block. Then repeat until all allcs are compacted.



### 5. Clean up
    
#### Design Decisions:
1. used destroy allocator function
    * freed initial code block
    * freed both linked lists

### Testing:

	* tested using sample output files provided by prof

	* using valgrind command provided by prof:
    valgrind --leak-check=full --show-leak-kinds=all --num-callers=20 ./kallocation


### Project Issues:
   * Some issues keeping track of both free and alloc linked lists. 
   * Some issues with compacting function. 

## How to run tests

	* simply issue commands 'make' 
	* then issue command './kallocation'


## Authors

    Sarbjot Singh - sarbjot-14{301313399}




