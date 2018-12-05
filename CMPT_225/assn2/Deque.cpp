#include <iostream>

#include "Deque.h"
using namespace std;

/*


/* Constructors and Destructor */

Deque:: Deque(){// Constructs a new empty deque.
	//cout<<"creating deque"<<endl;
	
	left_p= NULL ; // Points to left end node, or NULL if Deque is empty.
        right_p =NULL; // Points to right end node, or NULL if Deque is empty.
        current_size=0 ; // current number of elements in the queue.
        cursor=NULL ; // pointer to "current" node (or NULL if deque is empty).
	
	
}


Deque::Deque( const Deque& dq ){// Copy constructor.                         
	//cout<<"check again "<<&dq<<endl;
	//cout<<"plzzz    "<<(dq.left_p)->data<<endl;

	left_p= NULL ; // Points to left end node, or NULL if Deque is empty.
        right_p =NULL; // Points to right end node, or NULL if Deque is empty.
        current_size=0 ; // current number of elements in the queue.
        cursor= NULL; // pointer to "current" node (or NULL if deque is empty).


	if(dq.left_p!= NULL){
		node* current_node= dq.left_p;
		int item= current_node->data;
		node* new_node = new node(item,NULL,NULL);
		left_p= new_node; 
		right_p =new_node;
		current_size=1;
		cursor= left_p;

		

		current_node = current_node->next;
		while( current_node!= NULL){
			
			int item= current_node->data;
			node* new_node = new node(item,right_p,NULL);
			if(current_node==dq.cursor){
				cursor= new_node;
			}

			right_p->next=new_node;
			right_p= new_node;
			current_size++;
			current_node = current_node->next;
		}
			
	}


	
}




Deque::~Deque(){// Destructor.
	node* current = left_p;
	while(current!= NULL){
		node* temp_node = current->next;
		delete current;
		current= temp_node;
		
	}
	
	
}

/* Insertion operators */

void Deque::push_left( int item ){ // Inserts <item> at left end.
	if(left_p==NULL && right_p==NULL  ){
		//cout<<"pushed "<<item<<" left in empty list"<<endl;
		node* new_node = new node(item,NULL,NULL);
		left_p= new_node; 
		right_p =new_node;
		current_size=1;
		cursor= left_p;
		
	}
	else{
		//cout<<"pushed "<<item<<" left in non empty list"<<endl;
		node* new_node = new node(item,NULL,left_p);
		left_p->prev=new_node;
		left_p= new_node;
		
		current_size++;
	}

}
void Deque :: push_right( int item ){ // Inserts <item> at right end.
	if(left_p==NULL && right_p==NULL  ){
		//cout<<"pushed "<<item<<" right in empty list"<<endl;
		node* new_node = new node(item,NULL,NULL);
		left_p= new_node; 
		right_p =new_node;
		current_size=1;
		cursor= left_p;
		
		
	}
	else{
		//cout<<"pushed "<<item<<" right in non empty list"<<endl;
		
		
		node* new_node = new node(item,right_p,NULL);
		right_p->next=new_node;
		right_p= new_node;
		current_size++;
		
		
		
	}
}

/* Removal operators */
int Deque::pop_left(){  // removes item on left end.
	
	if(left_p==cursor){ //update cursor first
		if(cursor->next != NULL){
			cursor= cursor->next;
		}
		else{
			cursor=NULL;
		}

	}
	
	int temp_num= left_p->data;
	
	if(left_p->next==NULL){
		delete left_p;
		right_p= NULL;
		left_p=NULL;
		current_size=0;
		return temp_num;
	}
	else{
		node* temp_node= left_p->next;
		delete left_p;
		left_p= temp_node;
		left_p->prev= NULL;
		current_size--;
		return temp_num;
	}
}


// Pre: Deque is not empty.
int  Deque::pop_right(){  // removes item on right end.
	
	
	
	if(right_p==cursor){ //update cursor first
		if(cursor->prev != NULL){
			cursor= cursor->prev;
		}
		else{
			cursor=NULL;
		}
	}
	

	int temp_num= right_p->data;
	if(right_p->prev==NULL){
		delete right_p;
		right_p= NULL;
		left_p=NULL;
		current_size=0;
		return temp_num;
	}
	else{
		node* temp_node= right_p->prev;
		delete right_p;
		right_p= temp_node;
		right_p->next= NULL;
		current_size--;
		
		return temp_num;
	}
	

}

// Pre: Deque is not empty.

/* Size functions */
bool  Deque::empty(){ // Returns true iff the Deque contains no items.
	
	return current_size==0;
	
}



int  Deque::size(){  // Returns the current number of items in the deque.
	return current_size;
}
	

/* Cursor movement operators
*  These move the cursor one node to the left or right and then 
*  return true if such a move is possible.  If the move is not 
*  possible (i.e., the cursor is already at the extreme end
*  or the Deque is emty), no change occurs and false is returned. 
*/
bool  Deque::cursor_left(){ // Move cursor left.
	if(cursor->prev==NULL){
		return false;
	}
	else{
		cursor= cursor->prev;
		return true;
	}
}
bool  Deque::cursor_right(){ // Move cursor right.

	if(cursor->next==NULL){
		return false;
	}
	else{
		cursor= cursor->next;
		return true;
	}
}

/* Accessor functions */
int  Deque::peek_left(){  // Returns the left-most item without removing it.
// Pre: Deque is not empty.
	return left_p->data;
}
int  Deque::peek_right(){  // Returns the right-most item without removing it.
// Pre: Deque is not empty.
	return right_p->data;
}

// Return value the cursor currently is at, 
int  Deque::get_cursor(){
	return cursor->data;
}
 
// Pre: Deque is not empty.

void Deque:: set_cursor(int i){// Pre: Deque is not empty.
  cursor->data= i;
}
        
/* Utility functions */
void  Deque::display(){ // prints a depiction of the deque contents to standard output.   
	cout<<"[";
	
	for( node * current = left_p; current!= NULL; current = current->next){
		int number =current->data;
		cout<<number<<";";
	}
	cout<<"] size="<<current_size;
	if(cursor==NULL){
		cout<<", cursor=NULL."<<endl;
	}
	else{
		cout<< ", cursor="<<cursor->data<<"."<<endl;
	}
	//<<", cursor="<<cursor<<"."<<endl;
			
}
void Deque:: verbose_display(){ // like display, but includes node addresses and pointer values.
	cout<<"[";
	
	for( node * current = left_p; current!= NULL; current = current->next){
		
		cout<<current->data<<",";
		cout<<" "<<current<<"; ";
		
	}
	cout<<"] size="<<current_size;
	if(cursor==NULL){
		cout<<",cursor=NULL."<<endl;
	}
	else{
		cout<< ", cursor="<<cursor->data<<", ";
	}
	cout<<cursor<<"."<<endl;
	
}
