// Test application for the process_ancestors syscall.

#include <stdio.h>
#include <unistd.h>
#include <sys/syscall.h>
#include <assert.h>
#include <stdlib.h>
#include <stdint.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>

#include "process_ancestors_tests.h"

// Sys-call number:
#define _PROCESS_ANCESTORS_ 342

//#define _ARRAY_STATS_ 341




/**
 * Prototypes
 */
//mine: 
void test_one_child(void);
void test_no_child_no_parent();
void test_one_parent();
void test_grandfather(); // this works. asserts for pid dont fail for child, parent, or grandparent
void test_manychildren(); 
void test_three_child();
//profs: 
/*
void test_positive_few(void);
void test_negative_few(void);
void test_many(void);
void test_bad_addr(void);
static void do_syscall_working(long data[], long size);
static void do_syscall_failing(struct array_stats *stats, long data[], long size, long ret_code);
static void test_internal(_Bool success, int lineNum, char* argStr);
static void test_print_summary(void);
*/



/***********************************************************
 * main()
 ***********************************************************/
int main(int argc, char *argv[])
{
	
	test_grandfather(); // this works. asserts for pid don't fail for child, parent, or grandparent
	//test_child();
	//test_three_child();  //this one work i think for children. read print statements and pid's very carefully
	//test_one_child();

	//test_no_child_no_parent();
	//test_one_parent();
	
	return 0;
}


/***********************************************************
 * Testing routines for specific test
 ***********************************************************/
void test_no_child_no_parent()
{
	printf("running test: no child no parent.....\n");
	printf("My original pid:%d\n", getpid());

	pid_t childpid; /* variable to store the child's pid */	
	long size = 2;
long *num_filled = malloc(sizeof(long));
	*num_filled = 0;	int status;     /* parent process: child's exit status */
	struct process_info info_array[2]; 

	info_array[0].pid = 55555; // for testing....

	printf("Diving to kernel level\n");
	int result = syscall(_PROCESS_ANCESTORS_, info_array, size, num_filled);

	printf("..Rising to user level w/ result = %d \n", result);
	
	printf("info array[0].pid %ld\n", info_array[0].pid);
	
	
	
	
	
}
void test_grandfather(){
	printf("running test: grandfather, father, child .....\n");
   char *tokens[]={"ps",NULL}; 

	pid_t childpid = 2; /* variable to store the child's pid */	
   pid_t childpid2;
   pid_t storing_parent;
	long size = 8;
	long *num_filled = malloc(sizeof(long));
	*num_filled = 0;
	int status;     /* parent process: child's exit status */
	struct process_info info_array[8]; 
	struct process_info info_array_test[8];
	int32_t user_id = 0;

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
   user_id = getuid();
   printf("the user id of the grandparent: %lu\n", user_id);

	childpid = fork();

	if(childpid >0){
		 storing_parent = childpid;

	}		
		
		if(childpid == 0){
				//this is child of grandparent
	         printf("pid of child of grandparent %d\n", getpid());

	         //fill test struct for testing
	         info_array_test[1].pid = getpid();
	         //info_array_test[1].name = 
	         //info_array_test[1].state= 
	         //info_array_test[1].uid = getuid();
	         info_array_test[1].num_children = 1;
	         info_array_test[1].num_siblings = 0;
	          user_id = getuid();
	         printf("the user id of the parent %d\n", user_id);

	         childpid2 = fork();
	         if(childpid2 ==0){
		            printf("pid of grandchild %d\n", getpid());
		            info_array_test[0].pid = getpid();
			         //info_array_test[1].name = 
			         //info_array_test[1].state= 
			         //info_array_test[1].uid = getuid();
			         info_array_test[0].num_children = 0;
			         info_array_test[0].num_siblings = 0;
		            printf("Diving to kernel level\n");
		             user_id = getuid();
		            printf("the user id of the child is %d\n", user_id);
		            //sleep(10); //replace this with following two line (syscall)
		            result = syscall(_PROCESS_ANCESTORS_, info_array, size, num_filled);
		            printf("..Rising to user level w/ result = %d \n", result);
		            printf("exiting grandchild\n");
		            //exit(0);
	         }
	         else{
				//this is parent
				//printf("The parent is pid:%d\n", getpid());
				//wait for for child to end
				waitpid(childpid2,&status, 0); //option 0 waits fo any child process 
				
			}
	            
	         printf("exiting child of grandparent\n");
	         //exit(0); //only exit once the system call is done so that it is able to get process info before it dies
		}
      else{
			//this is parent
			//printf("The parent is pid:%d\n", getpid());
			//wait for for child to end
			waitpid(childpid,&status, 0); //option 0 waits fo any child process 
			
		}

   //make sure everything is right now
   if(getpid() == info_array_test[0].pid){
      for(int i = 0; i< 3; i++){ // three cause start at grandchild then parent then grandparent
         //TESTING
         //printf("info_array_[%d].pid %ld\n",i, info_array[i].pid);
		printf("\t parent is:  \"%s\" (pid: %ld) (state: %ld) (uid: %hu) (context switches: %ld) (inv. context switches: %ld) (#children: %ld) (#siblings: %ld)\n",info_array[i].name, info_array[i].pid, info_array[i].state, info_array[i].uid, info_array[i].nvcsw, info_array[i].nivcsw, info_array[i].num_children, info_array[i].num_siblings);
         assert(info_array_test[i].pid == info_array[i].pid);
         //assert(info_array_test[i].name == info_array[i].name );
         //assert(info_array_test[i].state== info_array[i].state );
         //assert(info_array_test[i].uid == info_array[i].uid);
         assert(info_array_test[i].num_children == info_array[i].num_children);
         assert(info_array_test[i].num_siblings == info_array[i].num_siblings);
         assert(*num_filled == 6);
         printf("num_filled: %ld \n", *num_filled);
      }
   }
   printf("grandfather ^^^^ check if assertion passed\n");

}
void test_three_child(){
	printf("running test: three child .....\n");
   char *tokens[]={"ps",NULL}; 

   pid_t childpid, childpid2, childpid3; /* variacurr_task = curr_task->parent;

      count++;ble to store the child's pid */   
   long size = 5;
   long *num_filled = malloc(sizeof(long));
   *num_filled = 0;
   int status;     /* parent process: child's exit status */
   struct process_info info_array[5]; 
   struct process_info info_array_test[5];
   int result=-1; // return from system call
   //info_array[0].pid = 55555; // for testing....

   //fill test struct for testing
   printf("I am only parent pid:%d\n", getpid());
   info_array_test[1].pid = getpid();
   //info_array_test[1].name = 
   //info_array_test[1].state= 
   printf("the user id of the parent is %d\n",getuid() );
   info_array_test[1].uid = getuid();
   info_array_test[1].num_children = 3;
   info_array_test[1].num_siblings = 0;

   printf("the user id of the parent is %d\n",getuid() );
   childpid = fork();
         

   if(childpid == 0){
      //this is child

      //fill test struct for testing
      info_array_test[2].pid = getpid();
      printf("im child %d and my parent is %d\n", getpid(), getppid());
      //printf("infor array test pid[0] = %ld \n", info_array_test[0].pid);
      //info_array_test[0].name = 
      //info_array_test[0].state= 
      info_array_test[2].uid = getuid();
      info_array_test[2].num_children = 0;
      info_array_test[2].num_siblings = 2;
      

      
   }
   else{
      printf("I am only parent pid:%d\n", getpid());
      childpid2 = fork();
         

      if(childpid2 == 0){
         //fill test struct for testing
         info_array_test[3].pid = getpid();
         printf("im child %d and my parent is %d\n", getpid(), getppid());
         //printf("infor array test pid[0] = %ld \n", info_array_test[0].pid);
         //info_array_test[0].name = 
         //info_array_test[0].state= 
         info_array_test[3].uid = getuid();
         info_array_test[3].num_children = 0;
         info_array_test[3].num_siblings = 2;
         sleep(1);

      }
      else{
         printf("I am only parent pid:%d\n", getpid());
         //same parent again
         childpid3 = fork();
         

         if(childpid3 == 0){

            //fill test struct for testing
            info_array_test[0].pid = getpid();
            
            //printf("infor array test pid[0] = %ld \n", info_array_test[0].pid);
            //info_array_test[0].name = 
            //info_array_test[0].state= 
            info_array_test[0].uid = getuid();
            info_array_test[0].num_children = 0;
            info_array_test[0].num_siblings = 2;
            sleep(2);
            printf("before diving im child %d and my parent is %d\n", getpid(), getppid());
            printf("Diving to kernel level\n");
            //sleep(10); //replace this with following two line (syscall)
            result = syscall(_PROCESS_ANCESTORS_, info_array, size, num_filled);
            printf("..Rising to user level w/ result = %d \n", result);
            sleep(2);
		   if(info_array_test[0].pid = getpid()){

		   		printf("child: info_array[0].pid %ld and info_array_test[0].pid %ld\n", info_array[0].pid, info_array_test[0].pid);
		   		printf("parent should have 3 children info_array[1].children %ld\n", info_array[1].num_children);
		   		printf("parent is array[1].pid %ld and info_array_test[1].pid %ld\n", info_array[1].pid, info_array_test[1].pid);
		   		
		   		assert(info_array[1].num_children ==3);
		   		assert(info_array[0].num_children == 0);
		   		assert(info_array[0].num_siblings == 2);
		   		assert(info_array[1].num_siblings == 0);
		   }


         }
         else{
            printf("I am only parent pid:%d\n", getpid());
            //same parent
         }
      }

   }

   for(int count = 0; count < *num_filled; count++){

   		printf("\t parent is:  \"%s\" (pid: %ld) (state: %ld) (uid: %lu) (context switches: %ld) (inv. context switches: %ld) (#children: %ld) (#siblings: %ld)\n",info_array[count].name, info_array[count].pid, info_array[count].state, info_array[count].uid, info_array[count].nvcsw, info_array[count].nivcsw, info_array[count].num_children, info_array[count].num_siblings);
	}

   printf("three child passed. assert did not fail\n");

   sleep(10);

   
}
void test_one_parent(){

   printf("running test: one parent .....\n");
   char *tokens[]={"ps",NULL}; 

	pid_t childpid; /* variacurr_task = curr_task->parent;
		count++;ble to store the child's pid */	
	long size = 5;
	long *num_filled = malloc(sizeof(long));
	*num_filled = 0;
	int status;     /* parent process: child's exit status */
	struct process_info info_array[5]; 
	struct process_info info_array_test[5];
   int result=-1; // return from system call
	//info_array[0].pid = 55555; // for testing....

   //fill test struct for testing
	printf("I am only parent pid:%d\n", getpid());
	info_array_test[1].pid = getpid();
   //info_array_test[1].name = 
   //info_array_test[1].state= 
   printf("the user id of the parent is %d\n",getuid() );
   info_array_test[1].uid = getuid();
   info_array_test[1].num_children = 1;
   info_array_test[1].num_siblings = 0;

   printf("the user id of the parent is %d\n",getuid() );
	childpid = fork();
			
	if(childpid >= 0){ //fork successful
		
		if(childpid == 0){
			//this is child

         //fill test struct for testing
         info_array_test[0].pid = getpid();
         printf("infor array test pid[0] = %ld \n", info_array_test[0].pid);
         //info_array_test[0].name = 
         //info_array_test[0].state= 
         info_array_test[0].uid = getuid();
         info_array_test[0].num_children = 0;
         info_array_test[0].num_siblings = 0;
			printf("Diving to kernel level\n");


			result = syscall(_PROCESS_ANCESTORS_, info_array, size, num_filled);

			printf("..Rising to user level w/ result = %d \n", result);
			while(result!=0){
           	sleep(0.005);
         	}
         exit(0); //only exit once the system call is done so that it is able to get process info before it dies
         result = -1; //reset result
                  printf("infor array pid[0] = %ld \n", info_array[0].pid);

		}

		else{
			//this is parent
			printf("The parent is pid:%d\n", getpid());
			//wait for for child to end
			waitpid(childpid,&status, 0); //option 0 waits fo any child process 
			
		}
	}

	for(int i = 0; i < size; i++){
	printf("info array[%d].pid: %ld\n", i, info_array[i].pid); //for testing. should not be 55555
 	}
   //make sure everything is right now

   //for(int i = 0; i<1; i++){
      //TESTING
      //assert(info_array_test[0].pid == info_array[0].pid);
      //assert(info_array_test[i].name == info_array[i].name );
      //assert(info_array_test[i].state== info_array[i].state );
      //assert(info_array_test[i].uid == info_array[i].uid);
      //assert(info_array_test[i].num_children == info_array[i].num_children);
      //assert(info_array_test[i].num_siblings == info_array[i].num_siblings);
   //}
}

void test_one_child()
{
	printf("running test: test one child.....\n");
	printf("My original pid:%d\n", getpid());

	pid_t childpid; /* variable to store the child's pid */	
	long size = 5;
	long *num_filled = malloc(sizeof(long));
	*num_filled = 0;
	int status;     /* parent process: child's exit status */
	struct process_info info_array[5]; 



	info_array[0].pid = 55555; // for testing....

	info_array[1].pid = 66666;

	printf("the sicond onnnnn %ld\n\n\n\n\n", info_array[1].pid);



	childpid = fork();
			
	 //fork successful
		
		if(childpid == 0){
			//this is child
			printf("My original pid:%d\n", getpid());
			printf("My original uid: %d\n", getuid());
			printf("Diving to kernel level\n");
			int result = syscall(_PROCESS_ANCESTORS_, info_array, size, num_filled);

			printf("..Rising to user level w/ result = %d \n", result);
			sleep(1);
			//exit(0);
		}

		else{
			//this is parent

			sleep(2);
			
			printf("The parent is pid:%d\n", getpid());
			//wait for for child to end
			//waitpid(childpid,&status, 0); //option 0 waits fo any child process 
			
		}
	
		for(int i = 0; i < 4; i++){
			printf("info array[%d].pid: %ld\ninfo array[%d].name: %s\ninfo array[%d].state: %ld\ninfo array[%d].uid: %ld\ninfo array[%d].cntxt switch: %ld\ninfo array[%d].inv cntxt switch: %ld\ninfo array[%d].#childre: %ld\n", i, info_array[i].pid, i, info_array[i].name, i, info_array[i].state, i, info_array[i].uid, i, info_array[i].nvcsw, i, info_array[i].nivcsw, i, info_array[i].num_children);
			
		}	
}



//------------------




