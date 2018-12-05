#include <iostream>   // for I/O facilities
#include "btree_set.h" // 
#include <algorithm> // Includes sort()
#include <ctime> // access to the system clock
#include <time.h> // access to "wall clock"
using namespace std;



int main (int argc, char * const argv[]) {
    cout << "\n\nbtree_set Class Test Program 1 - START\n\n";
	
	// Make a btree_set, and verify that empty() says it is empty. 

	btree::btree_set<int,std::less<int>,std::allocator<int>,16> S1 ;
	cout << "S1.empty() = " << S1.empty() << "\n";
       
	// Put something in it, and verify that it is no longer empty.
	cout << "S1: inserting 10" << "\n";
        S1.insert(10);
	cout << "S1.empty() = " << S1.empty() << "\n";

	cout << "\n\nbtree_set Class Test Program 1 - DONE\n\n";
    return 0;
}



