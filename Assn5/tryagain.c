#include "process_ancestors.h"
//#include <linux/uaccess.h>
#include <linux/kernel.h>
#include <linux/slab.h>
#include <linux/sched.h>
#include <linux/string.h>
#include <linux/cred.h>
#include <linux/uidgid.h>


#include <linux/uaccess.h>
#include <linux/slab.h>


asmlinkage long sys_process_ancestors(struct process_info info_array[],long size,long *num_filled);

asmlinkage long sys_process_ancestors(struct process_info info_array[],long size,long *num_filled){
	struct process_info info_array_kern[size]; 
	struct task_struct* curr_task = current;
	int errorCheck=1;
	long temp = 0;

	if(size <= 0){
		printk("returning size is < 0 -EINVAL");
		return -EINVAL;
	}
	printk("INSIDE SYS_PROCESS_ANCESTORS\n");
	printk("size var is %ld\n", size);
	printk("The process is \"%s\" (pid %i)\n",current->comm, current->pid);

    temp = curr_task->pid;
    /* 
	errorCheck = copy_from_user(&temp, &(curr_task->pid), sizeof(long) );
	if(errorCheck != 0){
		printk("copy to temp for init failed\n");
		return -EFAULT;
	}
    */
	
	errorCheck = copy_to_user(&(info_array[0].pid), &temp, sizeof(long) );
	if(errorCheck != 0){
		printk("copy stats structure failed\n");
		return -EFAULT;
	}
	
	printk("this is the copied value %d\n", info_array[0].pid);


    return 0;
}
