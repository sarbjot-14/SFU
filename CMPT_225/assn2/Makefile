all: dqtest1 dqtest2 dqtest3 dqtest4 dqtest5 dqtest6

dqtest1: deque.o dqtest1.o 
	g++ -o dqtest1 dqtest1.o Deque.o 

dqtest2: deque.o dqtest2.o
	g++ -o dqtest2 dqtest2.o Deque.o

dqtest3: deque.o dqtest3.o
	g++ -o dqtest3 dqtest3.o Deque.o

dqtest4: deque.o dqtest4.o
	g++ -o dqtest4 dqtest4.o Deque.o

dqtest5: deque.o dqtest5.o
	g++ -o dqtest5 dqtest5.o Deque.o

dqtest6: deque.o dqtest6.o
	g++ -o dqtest6 dqtest6.o Deque.o

dqtest1.o: dqtest1.cpp Deque.o Deque.h
	g++ -c dqtest1.cpp

dqtest2.o: dqtest2.cpp Deque.o Deque.h
	g++ -c dqtest2.cpp

dqtest3.o: dqtest3.cpp Deque.o Deque.h
	g++ -c dqtest3.cpp

dqtest4.o: dqtest4.cpp Deque.o Deque.h
	g++ -c dqtest4.cpp

dqtest5.o: dqtest5.cpp Deque.o Deque.h
	g++ -c dqtest5.cpp

dqtest6.o: dqtest6.cpp Deque.o Deque.h
	g++ -c dqtest6.cpp

deque.o: Deque.cpp Deque.h
	g++ -c Deque.cpp

clean:
	rm -f dqtest1 dqtest2 dqtest3 dqtest4 dqtest5 dqtest6 *.o
