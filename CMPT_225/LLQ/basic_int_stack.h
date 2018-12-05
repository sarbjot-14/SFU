/* File: basic_int_stack.h       

  Header file for a very basic array-based implementation of a stack.
  This is the dynamic version - implemented with a linked list.

*/
#include "int_node.h"

class basic_int_stack
{
  public:
    // This part should be implementation independent.
    basic_int_stack(); // constructor
    void push( int item );
    int pop(); 
    int top();
    bool empty();
    int size();

  private:
    // This part is implementation-dependant.
    int_node * top_ptr ; // pointer to the top Node.
    int current_size ; // number of stack elements.
};
