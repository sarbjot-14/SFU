#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "process_ancestors.h"
#include <assert.h>

int main(){ 
    printf("running test: test one child.....\n");

	pid_t childpid; /* variable to store the child's pid */	
	long size = 5;
	long *num_filled = 0;
	int status;     /* parent process: child's exit status */
	struct process_info info_array[size]; 
    struct process_info info_array_test[size]; 


	printf("My parent pid:%d\n", getpid());

    info_array_test[1].pid = getpid();

	childpid = fork();
			
	if(childpid >= 0){ //fork successful
		
		if(childpid == 0){
			//this is child
            info_array_test[0].pid = getpid();
			printf("My child pid is:%d\n", getpid());
			printf("Diving to kernel level: both processes should be running...\n");
			//int result = syscall(_PROCESS_ANCESTORS_, info_array, size, num_filled);

			//printf("..Rising to user level w/ result = %d \n", result);
            sleep(10); // this is too imitate the system call
            printf("returninggggg\n");

			exit(0);
		}

		else{
			//this is parent
			printf("The parent is pid:%d\n", getpid());
			//wait for for child to end
			waitpid(childpid,&status, 0); //option 0 waits fo any child process 
			
		}
	}


	//printf("info array[0].pid %ld\n", info_array[0].pid); //for testing. should not be 
    //assert(info_array_test[0].pid == info_array[0].pid);
    //assert(info_array_test[1].pid == info_array[1].pid)
    

return 0;

}