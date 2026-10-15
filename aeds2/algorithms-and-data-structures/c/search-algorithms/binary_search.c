#include <stdbool.h>

bool binary_search(int array[], int size, int x){
    int left = 0;
    int right = size - 1;

    while(left <= right){
        int mid = (left + right) / 2;
        int actual = array[mid];
        
        if(x == actual){
            return true;
        }
        else if(x > actual){
            left = mid + 1;
        }
        else{
            right = mid - 1;
        }
    }
    
    return false;
}

