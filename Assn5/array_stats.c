#include "array_stats.h"
#include <linux/uaccess.h>
#include <linux/kernel.h>
#include <linux/slab.h>



asmlinkage long sys_array_stats(struct array_stats *stats,long data[],long size);

asmlinkage long sys_array_stats(struct array_stats *stats,long data[],long size){
	printk("im in the SYSTEM CALL NOW\n");
        //copy one value at a time from the user’s data array into this variable
	long temp = 0; 
	int errorCheck=1;
	int i = 0;

	// Compute the correct values in this struct first, 
	//then at the very end use copy_to_use() to copy the contents to user’s pointer.
	struct array_stats* stats_kern = kmalloc(sizeof(struct array_stats),GFP_KERNEL) ; 


	printk("size is %ld\n", size);
	if(size <= 0){
		printk("returning size is < 0 -EINVAL");
		return -EINVAL;
	}
	printk("initializing temp\n");
	//to initialize stats_kern
	errorCheck = copy_from_user(&temp, &data[0], sizeof(long) );
	if(errorCheck != 0){
		printk("copy to temp for init failed\n");
		return -EFAULT;
	}
	printk("initializing stats_kern\n");
	stats_kern->min = temp;
	stats_kern->max = temp;
	stats_kern->sum = 0;

	printk("beginning the for loop\n");
	for(i = 0; i<size; i++){
		errorCheck = copy_from_user(&temp, &data[i], sizeof(long) );
		if(errorCheck != 0){
			printk("copy to temp failed\n");
			return -EFAULT;
		}
		//printk("copied to temp %ld\n", temp);

		if(temp < stats_kern->min){
			stats_kern->min = temp;
		}

		if(temp > stats_kern->max){
			stats_kern->max = temp;
		}
		stats_kern->sum = stats_kern->sum + temp;
	}
	printk("min: %ld, max: %ld, sum: %ld\n", stats_kern->min, stats_kern->max, stats_kern->sum);
	printk("finally copying back to user struc\n");
	//use copy_to_user() to copy the contents to user’s pointer.
	errorCheck = copy_to_user(stats, stats_kern, sizeof(struct array_stats) );
	if(errorCheck != 0){
		printk("copy stats structure failed\n");
		return -EFAULT;
	}
	return 0;		
}
