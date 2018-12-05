/* File: basic_int_queue.cpp

  Implementation of functions for basic "circular-array" implementation of a Queue.

*/
#include "basic_int_queue.h"// contains the declarations of the variables and functions.

Basic_int_queue::Basic_int_queue(){
  // the default constructor intitializes the private variables.
  capacity = 5;
  A = new int[capacity];
  front_index = 0 ;
  rear_index = 0 ;
  current_size = 0 ;
} 

void Basic_int_queue::enqueue( int item ){
  if(rear_index == front_index){
	cout<<"sorry Q full"<<endl;
  A[rear_index] = item ;
  rear_index = ( rear_index + 1 ) % capacity ;
  current_size += 1 ;
}

int Basic_int_queue::dequeue(){
  int item = A[front_index];
  front_index = ( front_index + 1 ) % capacity ;
  current_size -= 1 ;
  return item ;
}

bool Basic_int_queue::empty(){
  return current_size == 0 ;
}


int Basic_int_queue::front(){
  return A[front_index];
}

int Basic_int_queue::size(){
  return current_size;
}
