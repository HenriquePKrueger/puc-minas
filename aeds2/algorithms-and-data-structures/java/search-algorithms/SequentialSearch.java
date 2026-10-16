public class SequentialSearch{
    public static boolean sequentialSearch(int[] array, int x){
        for(int i = 0; i < array.length; i++){
            if(array[i] == x){
                return true;
            }
        }

        return false;
    }
}