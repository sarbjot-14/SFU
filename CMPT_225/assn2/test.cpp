#include <iostream>
#include <string>

#include "Deque.h"
using namespace std;

/*
cursor stays in middle if push left righ
*/

Deque* d= new Deque;


main(){	
	
	if(d->empty()==true){
		cout<<"it is empty"<<endl;
	}
	d->push_left(3);
	d->push_left(2);
	d->push_right(4);
	d->push_right(5);
	//d->push_right(6);

	d->display();
	
	
	
	int popped= d->pop_right();
	cout<< "okay popping this"<<popped<<endl;



	d->display();
	
	//d->display();
	
	



/*
	bool right= d-> cursor_right();
	int m= d->get_cursor();
	cout<< "the cursor is "<<m<<endl;


	int popped= d->pop_right();

	m= d->get_cursor();
	cout<< "should be 3 "<<m<<endl;


	popped= d->pop_right();

	m= d->get_cursor();
	cout<< "should be 2 "<<m<<endl;

	d->display();
	popped= d->pop_right();






	bool right= d-> cursor_right();
	right= d-> cursor_right();
	right= d-> cursor_right();
	right= d-> cursor_right();

	m= d->get_cursor();
	cout<< "before left "<<m<<endl;

	bool left= d-> cursor_left();
	left= d-> cursor_left();
	left= d-> cursor_left();
	left= d-> cursor_left();
	left= d-> cursor_left();

	//right= d-> cursor_left();
	if(left==true){
		m= d->get_cursor();
		cout<< "true and new curs "<<m<<endl;
	}
	else if(left== false){
		cout<<"lord its false"<<endl;
	}
		

	d->set_cursor(20);

	m= d->get_cursor();
	cout<< "the cursor is after set "<<m<<endl;

	cout<<"size is"<<d->size()<<endl;
	cout<<"address of d:  "<< d<<endl;
	//cout<<"address of d:  "<< *d<<endl;
	
	
	d->display();
	//delete d;
	
	cout<<"kraaaa"<<endl;
	Deque* copy_d = new Deque(*d);    ///Do I have to pass in d like this 
	cout<<"left most is "<< copy_d->peek_left()<<endl;
	cout<<"right most is "<< copy_d->peek_right()<<endl;

	copy_d->display();
	
	int leftData =copy_d->pop_left();
	cout<<"this should be two "<< leftData<<endl;

	int rearData =copy_d->pop_right();
	cout<<"this should be six "<< rearData<<endl;
	
	 rearData =copy_d->pop_right();
	cout<<"this should be five "<< rearData<<endl;
	
	copy_d->display();
	cout<<"left most is "<< copy_d->peek_left()<<endl;

*/

 
return 0;
}
