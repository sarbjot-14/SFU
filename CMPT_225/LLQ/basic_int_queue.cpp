/* File: basic_int_queue.cpp

  Implementation of functions for basic simple array-based implementation of a stack.
  Dynamic version.

*/
#include <cstring> // For declaration of NULL
#include "basic_int_stack.h"// contains the declarations of the variables and functions.

basic_int_stack::basic_int_stack(){
  q_size = 0 ;
  front = NULL ;
  rear =NULL;
} 

void basic_int_queue::enqueue( int item ){
  front = new int_node( item, front );
  q_size++;
}

int basic_int_queue::dequeue(){
  int_node * old_top = top_ptr ;
  int temp = old_top->data ; 
  top_ptr = top_ptr->next ;
  delete old_top;
  current_size-- ;
  return temp;
}

bool basic_int_stack::empty(){
  return top_ptr == NULL ;
}

int basic_int_stack::top(){
   return top_ptr->data;
}

int basic_int_stack::size(){
   return current_size ;
}
