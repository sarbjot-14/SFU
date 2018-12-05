#include <iostream> // I/O library.
#include <algorithm> // Includes sort()
#include <ctime> // access to the system clock
#include <time.h> // access to "wall clock"
using namespace std;

double elapsed_time( clock_t start, clock_t finish){
   // returns elapsed time in milliseconds
   return (finish - start)/(double)(CLOCKS_PER_SEC/1000);
}
   

int main (int argc, char * const argv[]) {

	int SIZE = 500000; // size of array to work on.
	int TIMES = 10 ; // number of times to run - mean is reported.

	srand( time(NULL) ); // seed the random number generator.

	clock_t start, finish ;// used to time function calls.


	// Make a btree_set, and verify that empty() says it is empty. 

	btree::btree_set<int,std::less<int>,std::allocator<int>,16> S1 ;
	cout << "S1.empty() = " << S1.empty() << "\n";	
	//double sort_time = 0;
	//int *A1 = new int[SIZE];

	for( int t = 0 ; t < TIMES ; t++ ){
		start = clock();
		for(int i = 0 ; i < SIZE ; i++ ){
			int x = rand() % SIZE*10;
			//A1[i] = x ;
			S1.insert(10);
		}
		//start = clock();
		//sort(A1,A1+SIZE);
		finish = clock();
		sort_time += elapsed_time(start,finish);

   }

	cout << "C++ sort time: " << sort_time / TIMES << endl ;

	return 0;
}
