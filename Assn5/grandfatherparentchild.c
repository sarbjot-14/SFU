#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "process_ancestors.h"
#include <assert.h>



int main(){


   printf("running test: one parent .....\n");
   char *tokens[]={"ps",NULL}; 

	pid_t childpid; /* variable to store the child's pid */	
   pid_t childpid2;
	long size = 5;
	long *num_filled = 0;
	int status;     /* parent process: child's exit status */
	struct process_info info_array[5]; 
	struct process_info info_array_test[5];
   int result=-1; // return from system call
	info_array[0].pid = 55555; // for testing....
   int level = 2;
   //fill test struct for testing
	printf("I grandparent pid:%d\n", getpid());
	info_array_test[2].pid = getpid();
   //info_array_test[2].name = 
   //info_array_test[2].state= 
   //printf("the user id of the parent is %d\n",getuid() );
   info_array_test[2].uid = getuid();
   info_array_test[2].num_children = 1;
   info_array_test[2].num_siblings = 0;

	childpid = fork();
			
		
		if(childpid == 0){
			//this is child of grandparent
         printf("pid of child of grandparent %d\n", getpid());

         //fill test struct for testing
         info_array_test[1].pid = getpid();
         //info_array_test[1].name = 
         //info_array_test[1].state= 
         //info_array_test[1].uid = getuid();
         //info_array_test[1].num_children = 1;
         //info_array_test[1].num_siblings = 0;

         childpid2 = fork();
         if(childpid2 ==0){
            printf("pid of grandchild %d\n", getpid());
            printf("Diving to kernel level\n");
            sleep(10); //replace this with following two line (syscall)
            //result = syscall(_PROCESS_ANCESTORS_, info_array, size, num_filled);
            //printf("..Rising to user level w/ result = %d \n", result);
            printf("exiting grandchild\n");
            exit(0);
         }
         else{
			//this is parent
			//printf("The parent is pid:%d\n", getpid());
			//wait for for child to end
			waitpid(childpid2,&status, 0); //option 0 waits fo any child process 
			
		}
            
         printf("exiting child of grandparent\n");
         exit(0); //only exit once the system call is done so that it is able to get process info before it dies
		}
      else{
			//this is parent
			//printf("The parent is pid:%d\n", getpid());
			//wait for for child to end
			waitpid(childpid,&status, 0); //option 0 waits fo any child process 
			
		}


	


	//printf("info array[0].pid %ld\n", info_array[0].pid); //for testing. should not be 55555
 
   //make sure everything is right now
   if(childpid == info_array[0].pid){
      for(int i = 0; i<2; i++){
         //TESTING
         printf("info_array_[%d].pid %ld\n",i, info_array[0].pid);

         //assert(info_array_test[i].pid == info_array[i].pid);
         //assert(info_array_test[i].name == info_array[i].name );
         //assert(info_array_test[i].state== info_array[i].state );
         //assert(info_array_test[i].uid == info_array[i].uid);
         //assert(info_array_test[i].num_children == info_array[i].num_children);
         //assert(info_array_test[i].num_siblings == info_array[i].num_siblings);
      }
   }
	
	return 0;
}