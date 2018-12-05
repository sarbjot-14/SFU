/* Deque Test Program 5 */
#include <cstring>
#include <iostream>
#include "Deque.h"

using namespace std ;

int main (int argc, char * const argv[]) {
    cout << "\n\nDeque Class Test Program 5 - START\n\n";

    // Make a Deque
    Deque * dq1 = new Deque();

    for( int i = 0 ; i<1000 ; i++ ){
        dq1->push_left(1);
        // dq1->display();
    }
    cout << "Size=" << dq1->size() << endl ;
    for( int i = 0 ; i<1000 ; i++ ){
        dq1->pop_left();
        // dq1->display();
    }
    cout << "Size=" << dq1->size() << endl ;

    delete dq1 ;

    cout << "\n\nDeque Class Test Program 5 - DONE\n\n";
    return 0;
}

