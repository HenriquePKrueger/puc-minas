#include <stdbool.h>

bool sequential_search(int array[], int size, int x){
    for(int i = 0; i < size; i++){
        if(array[i] == x){
            return true;
        }
    }

    return false;
}