#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "process_ancestors.h"


  
  
  int main(){

   
    printf("running test: test one child.....\n");
	printf("My original pid:%d\n", getpid());

    char *tokens[]={"ps",NULL}; 
	pid_t childpid; /* variable to store the child's pid */	
	long size = 5;
	long *num_filled = 0;
	int status;     /* parent process: child's exit status */
	struct process_info info_array[5]; 

	info_array[0].pid = 55555; // for testing....

	childpid = fork();
			
	if(childpid >= 0){ //fork successful
		
		if(childpid == 0){
			//this is child
			printf("Child pid:%d\n", getpid());
			printf("Diving to kernel level\n");
			//int result = syscall(_PROCESS_ANCESTORS_, info_array, size, num_filled);

			//printf("..Rising to user level w/ result = %d \n", result);
            sleep(30);
            //execvp(*tokens,tokens );
			exit(0);
		}

		else{
			//this is parent

			printf("The parent is pid:%d\n", getpid());
			//wait for for child to end
  
			waitpid(childpid,&status, 0); //option 0 waits fo any child process 
			//sleep(1);
		}
	}


	printf("info array[0].pid %ld\n", info_array[0].pid); //for testing. should not be 55555
return 0;
  }	