#include <stdio.h>

int main(){

int array[10]={1,10,1,10,1,10,1,10,1,10};
int length=10;
int frontOG=1;
int backOG=length-2;
int tmp;
int i;
int front=frontOG;
int back= backOG;

int j=0;
printf("front %d back %d\n", front, back);



for(j=2; j<length; j=j+4){

	printf("%d\n", j);
	for(int i=0; i< (length/2 -1);i++){
		
		
		tmp= array[front];  //swap front end
		array[front] = array[front+1];
		array[front +1]=tmp;
		if(front+1 != back){
			tmp= array[back];   // swap back end
			array[back] = array[back-1];
			array[back-1]= tmp;
		}
                for(int k=0; k< length; k++){
			printf("%d ", array[k]);
		}
		printf("\n");
		front++;
		back--;

	}
	
	frontOG++;
	backOG--;
	front=frontOG;
	back= backOG;
	printf("front %d back %d\n", front, back);
	
}

return 0;
}
	
		
	
	



