package creatures.utils;

public class Formulas {
	
	static public double scaleEvols(double a)
	{
		return 10*sigmoidP(a-5, 0.8);		
	}

	
	static public double sigmoidP(double a, double lambda)
	{
		return 1-(1/(1+Math.exp(-lambda*a)));
	}
		
	public static double getDistance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
	}

	
	public static int random(int max)
	{
		return (int)(max*Math.random()+1);
	}
}
