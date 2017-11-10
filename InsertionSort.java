public class InsertionSort {

	public static void sort (TheoreticalIon [] array){
		for(int c=1;c<array.length;c++){
			TheoreticalIon value = array[c];
			int d = c-1;
			while(d>=0 && array[d].getIonRank()>value.getIonRank()){
				array[d+1] = array[d];
				d--;
			}
			array[d+1] = value;
		}
	}
}

/**    for i <- 1 to length[A]-1 do
		value <- A[i] 
		j <- i-1
		while j >= 0 and A[j] > value do 
         	A[j + 1] <- A[j]
         	j <- j-1
		A[j+1] <- value
*/