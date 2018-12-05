#include <iostream> // I/O library.
#include "btree_set.h" // 
#include "btree_container.h"
#include <algorithm> // Includes sort()
#include <ctime> // access to the system clock
#include <time.h> // access to "wall clock"
using namespace std;

double elapsed_time( clock_t start, clock_t finish){
   // returns elapsed time in milliseconds
   return (finish - start)/(double)(CLOCKS_PER_SEC/1000);
}
   

int main (int argc, char * const argv[]) {

	int SIZE = 10; // size of array to work on.
	int TIMES = 100; // number of times to run - mean is reported.

	srand( time(NULL) ); // seed the random number generator.

	clock_t start, finish ;// used to time function calls.
	
	double insert_time = 0;
	btree::btree_set<int,std::less<int>,std::allocator<int>,16> S1 ;

	for( int t = 0 ; t < TIMES ; t++ ){
		btree::btree_set<int,std::less<int>,std::allocator<int>,16> S1 ;
		
		start = clock();
		for(int i = 0 ; i < SIZE ; i++ ){
			int x = rand() % SIZE*10;
			
			S1.insert(x);
			
		}
		
		finish = clock();
		insert_time += elapsed_time(start,finish);

   	}

	cout << "C++ insert time: " << insert_time / TIMES << endl ;

		
	//test for height average bytes per value and number of nodes
	SIZE =10;
	btree::btree_set<int,std::less<int>,std::allocator<int>,16> S2 ;

	for(int i = 0 ; i < SIZE ; i++ ){
		int x = rand() % SIZE*10;
		
		S2.insert(x);
	
		
	}
	cout<<"the height is: "<<S2.height()<<endl;
	cout<<"the average_bytes_per_value: "<<S2.average_bytes_per_value()<<endl;
	cout<<"the nodes is: "<<S2.nodes()<<endl;

		

	return 0;
}
