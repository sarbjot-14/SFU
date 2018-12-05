all: a0b

a0b: basic_int_stack.o a0b.o
	g++ basic_int_stack.o a0b.o -o a0b

basic_int_stack.o: basic_int_stack.cpp
	g++ -c basic_int_stack.cpp

a0b.o: a0b.cpp
	g++ -c a0b.cpp

clean:
	rm -rf *o a0b
