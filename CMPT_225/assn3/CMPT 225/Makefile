all: assn3

assn3: assn3.o 
	g++ -std=c++11 -o assn3 assn3.o 

assn3.o: assn3.cpp btree_set.h 
	g++ -std=c++11 -c assn3.cpp

clean:
	rm -f assn3 *.o


