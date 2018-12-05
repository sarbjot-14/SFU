/* File: basic_int_queue.h       

  Header file for a very basic "circular array" implementation of a Queue.

*/

class Basic_int_queue
{
  public:
    // This part should be implementation independent.
    Basic_int_queue(); // constructor
    void enqueue( int item );
    int dequeue(); 
    int front();
    bool empty();
    int size();

  private:
    // This part is implementation-dependant.
    int capacity ; // this will be the size of the array.
    int *A ; // this is a pointer to the array.
    int front_index ; // index in A of current front item (if queue is not empty).
    int rear_index ;  // index in A where next item enqueued will go.
    int current_size ; // current number of elements in the queue.
};
