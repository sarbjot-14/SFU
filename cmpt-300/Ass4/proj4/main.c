#include <stdio.h>
#include "kallocator.h"
//#include "list_sol.h"
void test1(enum allocation_algorithm _aalgorithm);
void test2(enum allocation_algorithm _aalgorithm);
void test3(enum allocation_algorithm _aalgorithm);
void test4(enum allocation_algorithm _aalgorithm);
void test5(enum allocation_algorithm _aalgorithm);



void test1(enum allocation_algorithm _aalgorithm){
    printf("\t test1*****allocate 0,1, remove 0,1\n");

    initialize_allocator(100, _aalgorithm);    
    int* p[50] = {NULL};
    p[0] = kalloc(sizeof(int));
    *(p[0]) = 0;
    p[1] = kalloc(sizeof(int));
    *(p[1]) = 1;
    print_statistics();
    kfree(p[0]);
    kfree(p[1]);
    print_statistics();
    destroy_allocator();

}
void test2(enum allocation_algorithm _aalgorithm){
    printf("\t test2****allocate 0,1,2, remove 1,0,2\n");
    initialize_allocator(100, _aalgorithm);    
    int* p[50] = {NULL};

    p[0] = kalloc(sizeof(int));
    *(p[0]) = 0;
    p[1] = kalloc(sizeof(int));
    *(p[1]) = 1;
    p[2] = kalloc(sizeof(int));
    *(p[2]) = 2;
    print_statistics();
    kfree(p[1]);
    kfree(p[0]);
    kfree(p[2]);
    print_statistics();
    destroy_allocator();

}
void test3(enum allocation_algorithm _aalgorithm){
    printf("\t test3***allocate 0,1,2,3, remove 0,2,1,3\n");
    initialize_allocator(100, _aalgorithm);    
    int* p[50] = {NULL};

    p[0] = kalloc(sizeof(int));
    *(p[0]) = 0;
    p[1] = kalloc(sizeof(int));
    *(p[1]) = 1;
    p[2] = kalloc(sizeof(int));
    *(p[2]) = 2;
    p[3] = kalloc(sizeof(int));
    *(p[3]) = 3;
    print_statistics();
    kfree(p[0]);
    kfree(p[2]);
    kfree(p[1]);
    kfree(p[3]);
    print_statistics();
    destroy_allocator();
}
void test4(enum allocation_algorithm _aalgorithm){
    printf("\t test4***start with 20 spaces, add 2,3,4 remove 4,2,3\n");
    initialize_allocator(20, _aalgorithm);
    int* p1[5] = {NULL};
    p1[2] = kalloc(sizeof(int));
    *(p1[2]) = 2;
    p1[3] = kalloc(sizeof(int));
    *(p1[3]) = 3;
    p1[4] = kalloc(sizeof(int));
    *(p1[4]) = 4;
    print_statistics();
    kfree(p1[4]);
    kfree(p1[2]);
    kfree(p1[3]);
    print_statistics();
    destroy_allocator();
    
    
}
void test5(enum allocation_algorithm _aalgorithm){
    printf("\t test5*** fill it then empty\n");
    initialize_allocator(20, _aalgorithm);
    int* p2[5] = {NULL};
    for(int i=0; i<5; ++i) {
        p2[i] = kalloc(sizeof(int));
        if(p2[i] == NULL) {
            printf("Allocation failed\n");
            continue;
        }
        *(p2[i]) = i;
        printf("p2[%d] = %p ; *p2[%d] = %d\n", i, p2[i], i, *(p2[i]));
    }
   
    print_statistics();

    print_statistics();
    printf("\n\n\n");
    for(int i=0; i<5; ++i) {
       
        printf("Freeing p2[%d]\n", i);
        kfree(p2[i]);
        p2[i] = NULL;
    }

    print_statistics();
    destroy_allocator();
}
void best_worst_fit1(enum allocation_algorithm _aalgorithm){
    initialize_allocator(100, _aalgorithm);    
    int* p[50] = {NULL};

    p[0] = kalloc(sizeof(int));
    *(p[0]) = 0;
    p[1] = kalloc(sizeof(int));
    *(p[1]) = 1;
    p[2] = kalloc(sizeof(int));
    *(p[2]) = 2;
    p[3] = kalloc(sizeof(int));
    *(p[3]) = 3;
    p[4] = kalloc(sizeof(int));
    *(p[4]) = 4;
    p[5] = kalloc(sizeof(int));
    *(p[5]) = 5;
    p[6] = kalloc(sizeof(int));
    *(p[6]) = 6;
    p[7] = kalloc(sizeof(int));
    *(p[7]) = 7;
    print_statistics();
    kfree(p[1]);
    kfree(p[2]);
    kfree(p[4]);
    kfree(p[5]);
    kfree(p[6]);
  


    p[8] = kalloc(sizeof(int));
    *(p[8]) = 8;

    //kfree(p[2]);
    kfree(p[0]);
    kfree(p[3]);
    kfree(p[7]);
    kfree(p[8]);
    print_statistics();

    destroy_allocator();
    
}



int main(int argc, char* argv[]) {
    ////////////TESTING BEST FIT (Prof's)////////////
     
    initialize_allocator(100, FIRST_FIT);
    printf("Using first fit algorithm on memory size 100\n");

    int* p[50] = {NULL};
    for(int i=0; i<10; ++i) {
        p[i] = kalloc(sizeof(int));
        if(p[i] == NULL) {
            printf("Allocation failed\n");
            continue;
        }
        *(p[i]) = i;
        printf("p[%d] = %p ; *p[%d] = %d\n", i, p[i], i, *(p[i]));
    }
   

    print_statistics();
    printf("\n\n\n");


    for(int i=0; i<10; ++i) {
        if(i%2 == 0)
            continue;

        printf("Freeing p[%d]\n", i);
        kfree(p[i]);
        p[i] = NULL;
    }
    printf("available_memory %d\n", available_memory());
            //kfree(p[i]);
    printf("adding 24\n");
    p[24] = kalloc(sizeof(int));
    *(p[24]) = 24;
    p[5] = kalloc(sizeof(int));
    *(p[5]) = 5;

    print_statistics();

    //printContig();
    void* after[100];
    //printf("after, after[0] is %p\n",after[0] );
    int compact_size = compact_allocation((void*)p, (void*)after);
    //printContig();
    
    printf("in after should be 0 %d\n", *((int*)after[0]));
    printf("in after should be 2 %d\n", *((int*)after[2]));

    printf("in after should be 4 %d\n", *((int*)after[3]));
    //printf("in after should be 9 %d\n", *((int*)after[9]));
    printf("in after should be 5 %d\n", *((int*)after[3]));

    printf("in after should be 6 %d\n", *((int*)after[5]));
    printf("in after should be 8 %d\n", *((int*)after[6]));
    printf("in after should be 24 %d\n", *((int*)after[1]));
    //printf("in after should be 15 %d\n", *((int*)after[15]));



    // You can assume that the destroy_allocator will always be the 
    // last funciton call of main function to avoid memory leak 
    // before exit
    printf("compact size is %d\n", compact_size);
    destroy_allocator();
    
    ////////////TESTING (Mine's)////////////

    printf("first fit:\n");
    //test1(FIRST_FIT);
    //test2(FIRST_FIT);
    //test3(FIRST_FIT);
    //test4(FIRST_FIT);
    //test5(FIRST_FIT);

    printf("best fit\n");
    //test1(BEST_FIT);
    //test2(BEST_FIT);
    //test3(BEST_FIT);
    //test4(BEST_FIT);
    //test5(BEST_FIT);
    //best_worst_fit1(BEST_FIT);
    printf("worst fit\n");
    //test1(WORST_FIT);
    //test2(WORST_FIT);
    //test3(WORST_FIT);
    //test4(WORST_FIT);
    //test5(WORST_FIT);
    //best_worst_fit1(WORST_FIT);

    return 0;
}
