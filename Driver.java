
public class Driver
{
    public static void main(String[]args)
    {
        Function f = new Function("ln(x)", "x");

        try {
            System.out.println(Calculate.fnInt(f, 0, 1, 1E-11));
        } catch (StackOverflowError e) {
            System.err.println("ERR: TOL NOT MET");
        }
    }
}

/*Test Functions
abs(2*x-10)
(x^3-x)/(x^4+1)
e^((0-1)*(x^2))
 */


/* Sources
TI-Basic Dev Website
http://tibasicdev.wikidot.com/fnint

Gaussian Quadrature Wikipedia articles
https://en.wikipedia.org/wiki/Gauss%E2%80%93Kronrod_quadrature_formula
https://en.wikipedia.org/wiki/Gaussian_quadrature

Other
http://terpconnect.umd.edu/~petersd/460/html/adapt_test.html
https://openturns.github.io/openturns/latest/user_manual/_generated/openturns.GaussKronrod.html
https://docs.roguewave.com/imsl/java/5.0.1/api/com/imsl/math/Quadrature.html

 */
