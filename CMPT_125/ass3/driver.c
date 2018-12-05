#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include "LL-node.h"
#include "LL.h"

int main(){

LL_t* intlist = LLcreate();

LLappend(intlist, 5);
LLappend(intlist, 6);
LLappend(intlist, 7);

LLprint(intlist);

return 0;
}

