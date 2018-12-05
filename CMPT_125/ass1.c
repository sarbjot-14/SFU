#include <stdio.h>

int i;
int j;
int k;
int n=15;
int s=0;


int main(){
	i=1;
	while(i<=n) {
		printf("the variable n is : %d \n", n);
		j=n-i;
		while (j>=2) {
			printf("the variable j is : %d \n", j);
			for (k=1; k<= j; k++) {
			s = s + 1;
			printf("the variable s is : %d \n", s);
		}
		j=j-2;
	}
	i=i+1;
}

	

/*
	int n =5;
	for(int i =1; i <=n; i++){
		printf("%d\n", i);
		for(int j=i; j<= n; j++){
			puts("hello");
		}
	}
*/


}
