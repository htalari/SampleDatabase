import java.io.IOException;
public class Driver{

	public static void main(String[] args) throws IOException {
		try {
			Algebra algebra = new Algebra();
			Ttable cars = algebra.load("cars.txt");
			Ttable res = algebra.Restrict(cars, "'Sedan' = Type");
			res.Display();
			Ttable r = algebra.Restrict(cars, "price != '21000'");
			r.Display();
			Ttable proj = algebra.Project(cars, "Price, Make");
			proj.Display();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}	
}
