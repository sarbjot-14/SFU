/* File: basic_int_queue.h       

  Header file for a very basic array-based implementation of a stack.
  This is the dynamic version - implemented with a linked list.

*/
#include "int_node.h"

class basic_int_stack
{
  public:
    // This part should be implementation independent.
    basic_int_queue(); // constructor
    void enqueue( int item );
    int dequeue(); 
    int top();
    bool empty();
    int size();

  private:
    // This part is implementation-dependant.
    int_node * front; //
    int_node * rear ; // 
    int q_size ; // number of stack elements.
};
