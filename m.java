package calculator;

public class m{

	public static final double PI = Math.PI;
	
	public static final double e = Math.E;
	
	public static double abs(double d){
		return Math.abs(d);
	}
	
	public static double round(double n, int digit){
		return Math.round(n*Math.pow(10, digit))/Math.pow(10, digit);
	}
	
	public static String toString(double n){
		return new Double(n).toString();
	}
	
	public static int digitAt(int num, int digit){
		
		num /= Math.pow(10, digit-1);
		
		num = (int)Math.ceil(abs(num));
		
		return num%10;
		
	}
	
	public static double integrate(String str, String as, String bs){
		double count = 0, a = Fx.evaluate(as, 0),b = Fx.evaluate(bs, 0);
		for(double i=0;i<10000;i++)
			count += Fx.evaluate(str, a+(b-a)*i/10000);
		count *= (b-a)/10000;
		return count;
	}
	
}
