all:	driver

driver:	driver.o LL.o
	gcc -std=c99 -Wall -o driver driver.o LL.o

LL.o:	LL.h LL.c LL-node.h
	gcc -std=c99 -Wall -c LL.c

driver.o:	driver.c LL.h
	gcc -std=c99 -Wall -c driver.c

clean:	
	rm -f driver *.o *.gch
