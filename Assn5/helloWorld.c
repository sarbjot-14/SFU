#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>



int main(){


   printf("running test: one parent many children.....\n");
   char *tokens[]={"ps",NULL}; 

   pid_t parentpid;
   pid_t childpid_1;
	pid_t childpid; /* variable to store the child's pid */	
	long size = 5;
	long *num_filled = 0;
	int status;     /* parent process: child's exit status */
	//struct process_info info_array[5]; 
  
  // struct process_info info_array_test[5]; 
  

	//info_array[0].pid = 55555; // for testing....

   printf("I am the Big papa %d\n", getpid() );

   //info_array_test[0].pid = ge

   int num_of_children = 20;
 
      for(int i = 0 ; i< num_of_children; i++){
         childpid = fork();

         if(childpid == 0){
            printf("child's pid is %d, and it's parent pid is %d\n", getpid(), getppid());
            if(i== num_of_children-1){
               execvp(*tokens,tokens );
               printf("Diving to kernel level\n");
               //int result = syscall(_PROCESS_ANCESTORS_, info_array, size, num_filled);
               //printf("..Rising to user level w/ result = %d \n", result);
            }
            //exit(0);
            break; // we want kids to stop


         }
         else{
            continue; // we want parent to keep on forking kids
         }
      
      }
      
   sleep(1); // use the proper wait command to gather all the children
   // HAVE TO MAKE SURE NO KIDS DIES WHILE SYSTEM CALL IS COUNTING THE CHILDREN
   for(int i = 0; i< num_of_children; i++){
      wait(NULL);
   }
			

      //info_array_test[0].pid = getpid(); //this is to test if the returned one is the same number

	return 0;
}
