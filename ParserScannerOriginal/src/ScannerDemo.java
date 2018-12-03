/**
 * @author Christelle
 * 
 */
public class ScannerDemo {

	private static String file1 = "C:\\Users\\dinom\\OneDrive\\Documents\\PaceMastersCompSci\\Fall_2018\\prnc_of_prgrm\\Assignments\\ParserScannerOriginal\\ParserScannerOriginal\\src\\prog2.jay";
	private static int counter = 1;

	public static void main(String args[]) {

		TokenStream ts = new TokenStream(file1);

		System.out.println(file1);


		while (!ts.isEndofFile()) {
			// TO BE COMPLETEDs everytime 
			Token t = ts.nextToken();
			System.out.println(t.toString());

		}
	}
}
