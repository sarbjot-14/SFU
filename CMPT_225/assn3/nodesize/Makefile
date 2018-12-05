all: nodesize

nodesize: nodesize.o 
	g++ -std=c++11 -o nodesize nodesize.o 

nodesize.o: nodesize.cpp btree_set.h 
	g++ -std=c++11 -c nodesize.cpp

clean:
	rm -f nodesize *.o


