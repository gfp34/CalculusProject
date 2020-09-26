
public class Calculate {
	
	final static double H = .001;

	/**Derivative of a functions using the formula f(x+h)−f(x−h)/2h where h=.001
	 * The same derivative method as the ti-84
	 * http://tibasicdev.wikidot.com/nderiv
	 *
	 * @param f function f(x)
	 * @param value x value for f'(x)
	 * @return f'(x) where x=value
	 */
	public static double nDeriv(Function f, double value){
		return (f.evaluate(value+H) - f.evaluate(value-H))/ (2*H);
	}

	/**Newton's Method for finding the roots(zeros) of a function
	 *
	 * @param f function f(x)
	 * @param guess guess for an x value close to the x value where f(x)=0; starting point for Newton's Method
	 * @return The x value for which f(x)=0
	 */
	public static double zero(Function f, double guess){
		while(f.evaluate(guess)!=0){
			System.err.println(guess);
			guess = guess-(f.evaluate(guess)/nDeriv(f, guess)); 
		}
		return guess; 
	}

	/**Rectangular Approximation Method for integration
	 *
	 * @param f function f(x)
	 * @param a upperbound of integral approximation
	 * @param b lowerbound of integral approximation
	 * @param nSubInt number of subintervals for drawing rectangles
	 * @return double array with {Left, Middle, Right} rectangle approximation for integration
	 */
	public static double[] RAM(Function f, double a, double b, int nSubInt){
		double deltaX = (b-a)/nSubInt;
		double[]leftMidRight = new double[3];
		for(double i=a; i<b; i+=deltaX){
			leftMidRight[0] += f.evaluate(i);
			leftMidRight[1] += f.evaluate(i+deltaX/2);
			leftMidRight[2] += f.evaluate(i+deltaX);
		}
		for(int i=0; i<3; i++)
			leftMidRight[i] *= deltaX;
		return leftMidRight;
	}

	/**Trapezoidal Approximation method for integration
	 *
	 * @param f function f(x)
	 * @param a upperbound of integral approximation
	 * @param b lowerbound of integral approximation
	 * @param nSubInt number of subintervals for drawing trapezoids
	 * @return Integral is approximated using the trapezoidal approximation method
	 */
	public static double trapApprox(Function f, double a, double b, int nSubInt){
		double deltaX = (b-a)/nSubInt;
		double sum = 0; 
		for(double i=a; i<b; i+=deltaX){
			sum += ((f.evaluate(i)+f.evaluate(i+deltaX))/2)*deltaX;
		}
		return sum; 
	}

	public static double gauss(Function f, double a, double b) {
		double sum = 0;
		double[] points = {0.949107912342759, 0.741531185599394, 0.405845151377397, 0};
		double[] weights = {0.129484966168870, 0.279705391489277, 0.381830050505119, 0.417959183673469};

		for(int i=0; i<points.length; i++) {
			sum += (b-a)/2.0 * f.evaluate(((b-a)/2.0)*points[i] + (a+b)/2.0) * weights[i];
			if(points[i] != 0)
				sum += (b-a)/2.0 * f.evaluate(((b-a)/2.0)*(points[i]*-1) + (a+b)/2.0) * weights[i];
		}
		return sum; 
	}
	
	public static double gaussKronrod(Function f, double a, double b) {
		double sum = 0; 
		double[] points = {0.991455371120813, 0.949107912342759, 0.864864423359769, 0.741531185599394, 0.586087235467691, 0.405845151377397, 0.207784955007898, 0};
		double[] weights = {0.022935322010529, 0.063092092629979, 0.104790010322250, 0.140653259715525, 0.169004726639267, 0.190350578064785, 0.204432940075298, 0.209482141084728};

		for(int i=0; i<points.length; i++) {
			sum += (b-a)/2.0 * f.evaluate(((b-a)/2.0)*points[i] + (a+b)/2.0) * weights[i];
			if(points[i] != 0)
				sum += (b-a)/2.0 * f.evaluate(((b-a)/2.0)*(points[i]*-1) + (a+b)/2.0) * weights[i];
		}
		return sum; 
	}


	public static double fnInt(Function f, double a, double b, double tol) throws StackOverflowError {
		double g7 = gauss(f, a, b);
		double k15 = gaussKronrod(f, a, b);
		double error = Math.abs(g7 - k15);

		System.err.println("a = " + a);
		System.err.println("b = " + b);
		//System.err.println("g7 = " + g7);
		System.err.println("k15 = " + k15);
		System.err.println("Error = " + error);
		//System.err.println("Tol = " + tol);
		//System.err.println();
		if (error <= tol || Math.abs(a-b)<tol) {
			return k15;
		} else {
			double c = (a + b) / 2;
			double errAC = Math.abs(gauss(f, a, c) - gaussKronrod(f, a, c));
			System.err.println("errAC = " + errAC);
			double errCB = Math.abs(gauss(f, c, b) - gaussKronrod(f, c, b));
            System.err.println("errCB = " + errCB);
            System.err.println();
			if(errAC > errCB)
			    return gaussKronrod(f, c, b) + fnInt(f, a, c, tol);
			else if(errAC < errCB)
			    return gaussKronrod(f, a, c) + fnInt(f, c, b, tol);
			else
			    return fnInt(f, a, c, tol) + fnInt(f, c, b, tol);
		}
	}
	
	public static double rootBisectorMethod(Function f, double lower, double upper){
		double a = lower, b = upper, c = (upper-lower)/2; 
		
		while(Math.abs(f.evaluate(c)) < 1E-5) {
			double fa = f.evaluate(a); 
			double fb = f.evaluate(b);
			double fc = f.evaluate(c);
			
			if((fa<0 && fc<0) || (fa>0 && fc>0)){
				a = c; 
			}
			else if((fb<0 && fc<0) || (fb>0 && fc>0)){
				b = c; 
			} 
			c = (a-b)/2;
		}
		return c;
	}
}
