//
//  Test Program for Basic Int Queue Class
//
#include <iostream>   // for I/O facilities
#include "basic_int_queue.h" //  basic_int_queue declarations
using namespace std;

int main (int argc, char * const argv[]) {
    cout << "\n\nBasic_int_queue Class Test Procedure - START\n\n";
	
	// Make a queue, and verify that empty() says it is empty. 

	Basic_int_queue q1 ;
	cout << "empty() = " << q1.empty() << "\n";

        // Put some things on it. 
	cout << "enqueue( " << 1 << " )\n";
	q1.enqueue(1);
	cout << "enqueue( " << 2 << " )\n";
	q1.enqueue(2);

	cout << "enqueue( " << 3 << " )\n";
	q1.enqueue(3);
	cout << "enqueue( " << 4 << " )\n";
	q1.enqueue(4);
	cout << "enqueue( " << 5 << " )\n";
	q1.enqueue(5);
	cout << "enqueue( " << 6 << " )\n";
	q1.enqueue(6);
	cout << "enqueue( " << 7 << " )\n";
	q1.enqueue(7);
	cout << "enqueue( " << 8 << " )\n";
	q1.enqueue(8);
	cout << "enqueue( " << 9 << " )\n";
	q1.enqueue(9);
	cout << "enqueue( " << 10 << " )\n";
	q1.enqueue(10);

        // Verify that empty() reports it is not empty, 
        // and that the right thing is at the front.
	cout << "empty() = " << q1.empty() << "\n";
	cout << "front() = " << q1.front() << "\n";
	
        // Empty it, and verify that empty() again reports it is empty.
	while( ! q1.empty() ){
		cout << "dequeue() = " << q1.dequeue() << "\n";
	}
	cout << "empty() = " << q1.empty() << "\n";
	
	cout << "\n\nBasic_int_queue Class Test Procedure - DONE\n\n";
    return 0;
}
