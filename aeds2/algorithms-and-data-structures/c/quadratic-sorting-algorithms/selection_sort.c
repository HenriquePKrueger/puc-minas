void selection_sort(int array[], int size){
    for(int i = 0; i < size - 1; i++){
        int min = i;

        for(int j = i + 1; j < size; j++){
            if(array[j] < array[min]){
                min = j;
            }
        }

        swap(array, i, min);
    }
}