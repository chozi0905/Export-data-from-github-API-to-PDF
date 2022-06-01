public class Percobaan{
	public static void main (String[] args){
		String test = "abca";
		String hasil = "";
		int a = 1;

		for (int i = 0; i < test.length(); i++) {
			for (; a < test.length(); a++) {
				// System.out.println(test.substring(i,i+1).equals(test.substring(a,a+1)));
				if(test.substring(i,i+1).equals(test.substring(a,a+1))){
					hasil = test.substring(i,i+1);
					break;
				}
			}
			if(!hasil.isEmpty())
				break;
			if(a >= test.length()){
				a = 1;
			}
			a++;
		}
		 System.out.print(hasil);
	}
}

