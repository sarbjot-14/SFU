#include "process_ancestors.h"
#include <linux/uaccess.h>
#include <linux/kernel.h>
#include <linux/slab.h>
#include <linux/sched.h>
#include <linux/string.h>
#include <linux/cred.h>
#include <linux/uidgid.h>
#include <linux/types.h>


#include <linux/uaccess.h>
#include <linux/slab.h>

//REMOVE ALL PRINT STATEMENTS WHEN CLEANING


asmlinkage long sys_process_ancestors(struct process_info info_array[],long size,long *num_filled);

asmlinkage long sys_process_ancestors(struct process_info info_array[],long size,long *num_filled){

	struct process_info info_array_kern[size]; 

	struct task_struct* curr_task = current;

	long num_filled_kern = 0;

	int errorCheck=1;
	int errorCheck_num = 1;
	long count = 0;
	int sibling_flag = 0;
	char* p_name;
	int32_t user_id = 0;
	struct list_head* start_node;
	struct list_head* curr_node; 
	


	if(size <= 0){
		
		printk("returning size is < 0 -EINVAL");
		return -EINVAL;
	}

	
	while(curr_task->parent != curr_task){
		

		info_array_kern[count].pid = curr_task->pid;
		p_name = curr_task->comm;
		strcpy(info_array_kern[count].name, p_name);  	
		info_array_kern[count].state = curr_task->state;
		user_id = current_uid().val;
		info_array[count].uid = user_id;
		info_array_kern[count].nvcsw = curr_task->nvcsw;
		info_array_kern[count].nivcsw = curr_task->nivcsw;
		
		info_array_kern[count].num_children = 0;
		start_node = &(curr_task->children);
		curr_node = start_node->next;

		while(curr_node != start_node){
			
			curr_node = curr_node->next;
			info_array_kern[count].num_children++;
		}

		info_array_kern[count].num_siblings = 0;
		sibling_flag = 1;
		start_node = &(curr_task->sibling);
		curr_node = start_node->next;

		while(curr_node != start_node){
			
			if(sibling_flag){
				curr_node = curr_node->next;
			}

			else{
				curr_node = curr_node->next;
				info_array_kern[count].num_siblings++;
			}

			sibling_flag = 0;

		}
			
		//printk("\t parent is:  \"%s\" (pid: %ld) (state: %ld) (uid: %ld) (context switches: %ld) (inv. context switches: %ld) (#children: %ld) (#siblings: %ld)\n", info_array_kern[count].name, info_array_kern[count].pid, info_array_kern[count].state, info_array_kern[count].uid, info_array_kern[count].nvcsw, info_array_kern[count].nivcsw, info_array_kern[count].num_children, info_array_kern[count].num_siblings);
		
		curr_task = curr_task->parent;
		count++;		
	}

	num_filled_kern = count;


	errorCheck = copy_to_user(info_array, info_array_kern, size*sizeof(struct process_info ) );
	if(errorCheck != 0){
		printk("copy stats structure failed\n");
		return -EFAULT;
	}

	errorCheck_num = copy_to_user(num_filled, &num_filled_kern, sizeof(long));
	if(errorCheck_num != 0){
		printk("copy num_filled failed\n");
		return -EFAULT;
	}
	
 return 0;
}
