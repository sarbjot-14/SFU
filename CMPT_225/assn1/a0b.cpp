//
//  a0b.cpp
//
#include <iostream>   // for I/O facilities
#include "basic_int_stack.h" //  basic_int_stack declarations
using namespace std;

int main (int argc, char * const argv[]) {
	int sum=0;
	int input=0;
	int popped=0;
	int size=0;
   
	cout<< "Sarbjot Singh\nSarbjots\n301313399"<<endl;

	Basic_int_stack s1 ;

	cin>> input;
	
	while(input>=0){
		//cout<<"pushing on stack\n";
		sum= sum + input;
		s1.push(input);
		cin>>input;
		//cout<<"new input is:"<<input<<endl;
	}
	
	size= s1.size();
	cout<<"\n"<<size<<endl;
	//sum
	
	
	cout<<sum<<"\n"<<endl;

	//reverse order
	for(int i=0; i<size;i++){
		cout<<s1.pop()<<endl;
	}
	
	cout<<"goodbye"<<endl;
	
    return 0;
}
