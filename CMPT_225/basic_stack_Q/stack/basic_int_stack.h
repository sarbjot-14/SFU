/* File: basic_int_stack.h       

  Header file for a very basic array-based implementation of a stack.

*/

class Basic_int_stack
{
  public:
    // This part should be implementation independent.
    Basic_int_stack(); // constructor
    void push( int item );
    int pop(); 
    int top();
    bool empty();

  private:
    // This part is implementation-dependant.
    static const int capacity = 10 ; // the array size 
    int A[capacity] ; // the array.
    int top_index ; // this will index the top of the stack in the array
};
