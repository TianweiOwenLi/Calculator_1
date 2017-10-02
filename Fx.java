package calculator;

public class Fx {
	
	//If +- operators that are out of any parenthesis exist, f is separated into 2 recursive actions.
	//Else, f is evaluated by mul().
	public static double evaluate(String f, double x){
		
		//Eliminate the negative sign, if there's any at the beginning.
		if(f.substring(0, 1).equals("-"))
			return -1*evaluate(f.substring(1), x);
		
		//If there's a number preceding x, a * is inserted.
		for(int i=1;i<f.length();i++)
			if(f.substring(i, i+1).equals("x")&&f.substring(i-1,i).matches("\\d{1}"))
				f = f.substring(0,i)+"*"+f.substring(i);
		
		//Split f at the location of valid +- and calculate both sections by recursion.
		for(int i=f.length();i>0;i--){
			if(pCount(f, i) == 0){
				if(f.substring(i-1, i).equals("+"))
					return evaluate(f.substring(0, i-1), x)+evaluate(f.substring(i), x);
				if(f.substring(i-1, i).equals("-") && !isOperator(f.substring(i-2, i-1)))
					return evaluate(f.substring(0, i-1), x)-evaluate(f.substring(i), x);
			}
		}
		//If no valid +- is found, f is evaluated by val().
		return mul(f, x);
	}
	
	//If */ operators that are out of any parenthesis exist, f is separated into 2 recursive actions.
	//Else, f is evaluated by pow().
	private static double mul(String f, double x){

		//Split f at the location of valid */ and calculate both sections by recursion.
		for(int i=f.length();i>0;i--){
			if(pCount(f, i) == 0){
				if(f.substring(i-1, i).equals("*"))
					return mul(f.substring(0, i-1), x)*mul(f.substring(i), x);
				if(f.substring(i-1, i).equals("/"))
					return mul(f.substring(0, i-1), x)/mul(f.substring(i), x);
			}
		}
		
		//If no valid */ is found, f is evaluated by val().
		return pow(f, x);
	}
	
	
	//If ^ that is out of any parenthesis exist, f is separated into 2 recursive actions.
	//Else, f is evaluated by val().
	private static double pow(String f, double x){
		
		//Split f at the location of valid ^ and calculate both sections by recursion.
		for(int i=f.length();i>0;i--)
			if(pCount(f, i) == 0 && f.substring(i-1, i).equals("^"))
				return Math.pow(pow(f.substring(0, i-1), x), pow(f.substring(i), x));
		
		//If no valid ^ is found, f is evaluated by val().
		return val(f, x);
		
	}
	
	//If f is within a pair of parenthesis, then no primitive operators exist out of parenthesis.
	//In this case, if there are parenthesis, anything out of parenthesis is removed. The inner part is evaluated by evaluate().
	//If no parenthesis exists, then no operators explicitly exist in f. In this case, f is evaluated by *.
	private static double val(String f, double x){
		
		//Remove empty spaces.
		f = f.trim();
		
		//Eliminate the negative sign, if there's any at the beginning.
		if(f.substring(0, 1).equals("-"))
			return -1*evaluate(f.substring(1), x);
		
		//Eliminate meaningless parenthesis that incorporates the entire f.
		if(f.substring(0, 1).equals("(") && f.substring(f.length()-1).equals(")"))
			return evaluate(f.substring(1, f.length()-1), x);
		
		//If ( exists, read the substring preceding ( and decide which function to use for evaluating f. Deals with abs(), sin(), etc.
		else if(f.indexOf("(") != -1){
			int i = f.indexOf("(");
			switch(f.substring(0, i)){
			
			//A set of possible functions.
			
			case "abs":
				return Math.abs(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "floor":
				return Math.floor(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "ceil":
				return Math.ceil(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "round":
				return Math.round(evaluate(f.substring(i+1, f.length()-1), x));
				
				
				
			case "sin":
				return Math.sin(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "cos":
				return Math.cos(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "tan":
				return Math.tan(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "cot":
				return 1/Math.tan(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "sec":
				return 1/Math.cos(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "csc":
				return 1/Math.sin(evaluate(f.substring(i+1, f.length()-1), x));
				
				
				
			case "asin":
				return Math.asin(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "acos":
				return Math.acos(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "atan":
				return Math.atan(evaluate(f.substring(i+1, f.length()-1), x));
				
				
				
			case "sinh":
				return Math.sinh(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "cosh":
				return Math.cosh(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "tanh":
				return Math.tanh(evaluate(f.substring(i+1, f.length()-1), x));
				
				
			
			case "ln":
				return Math.log(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "lg":
				return Math.log10(evaluate(f.substring(i+1, f.length()-1), x));
				
			case "int":
				String copy = new String(f.substring(i+1, f.length()-1));
				String func = new String(copy.substring(0, copy.indexOf(",")));
				copy = copy.substring(copy.indexOf(",")+1);
				String n1 = new String(copy.substring(0, copy.indexOf(",")));
				String n2 = copy.substring(copy.indexOf(",")+1);
				return m.integrate(func, n1, n2);
				
			default:
				return evaluate(f.substring(i+1, f.length()-1), x)*new Integer(f.substring(0, i)).intValue();
			}
		}
		
		//At this point, no explicit operators exist. f is evaluated by multiplication.
		if(f.equals("x"))
			return x;
		if(f.equals("pi"))
			return Math.PI;
		if(f.equals("e"))
			return Math.E;
		if(f.substring(f.length()-1).equals("x"))
			return x*mul(f.substring(0, f.length()-1), x);
		if(f.substring(Math.max(f.length()-2, 0)).equals("pi"))
			return Math.PI*mul(f.substring(0, f.length()-2), x);
		if(f.substring(f.length()-1).equals("e"))
			return Math.E*mul(f.substring(0, f.length()-1), x);
		return new Double(f).doubleValue();
	}
	
	//Count that in how many layers of parenthesis is a given place locating. If returns 0, the place is out of all parenthesis.
	private static int pCount(String str, int place){
		int count = 0;
		for(int i=0;i<place;i++){
			if(str.substring(i, i+1).equals("("))
				count++;
			if(str.substring(i, i+1).equals(")"))
				count--;
		}
		return count;
	}
	
	//Check if a string is an operator.
	public static boolean isOperator(String s){
		if(s.equals("+")||s.equals("-")||s.equals("*")||s.equals("/")||s.equals("^"))
			return true;
		return false;
	}
	
	//Rounds a double type data to certain digits after decimal point.
	public static double round(double n, int digit){
		return Math.round(n*Math.pow(10, digit))/Math.pow(10, digit);
	}
	/*
	public static void main(String[]args){
		System.out.println(round(evaluate("x^2+x-2", 1.5),4));
	}
	*/
}
