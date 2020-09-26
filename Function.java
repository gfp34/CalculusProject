import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Function {
	
	String function;
	String inputVar;
	//Stack<String> operator;
	Queue<String> infix;
	Queue<String> postfix;
	
	public Function (String function, String inputVar){
		this.function = function;
		this.inputVar = inputVar; 
		
		infix = separateInfix();
		//System.err.println(infix);
		
		postfix = toPostfix(infix);
		//System.err.println(postfix);
	}
	
	//sparates the function string into a list of tokens in infix order
	private Queue<String> separateInfix(){
		Queue<String> tokens = new LinkedList<String>();
		
		int i=0;
		while(i<function.length()){
			int k=i+1;
			//add the numbers to the tokens list
			if(function.charAt(i)>=48 && function.charAt(i)<=57){			 
				while(k<function.length() && function.charAt(k)>=48 && function.charAt(k)<=57)
					k++;
				tokens.add(function.substring(i, k));
			}
			//add the variables/functions
			else{ 
				if(function.charAt(i)=='e') {
					tokens.add("" + Math.E);
				}
				else if(i+4<function.length() && function.substring(i, i+4).equals("sqrt")) {
					tokens.add("sqrt");
					k=i+4;
				}
				else if(i+2<function.length() && function.substring(i, i+2).equals("ln")) {
					tokens.add("ln");
					k=i+2;
				}
				else if(i+3<function.length() && function.substring(i, i+3).equals("abs")) {
					tokens.add("abs");
					k=i+3;
				}
				else if(i+3<function.length() && function.substring(i, i+3).equals("sin")) {
					tokens.add("sin");
					k=i+3;
				}
				else if(i+3<function.length() && function.substring(i, i+3).equals("cos")) {
					tokens.add("cos");
					k=i+3;
				}
				else if(i+3<function.length() && function.substring(i, i+3).equals("tan")) {
					tokens.add("tan");
					k=i+3;
				}
				else if(i+2<=function.length() && (function.substring(i, i+2).toLowerCase().equals("pi"))){
					tokens.add("" + Math.PI);
					k=i+2;
				}
				else
					tokens.add(function.substring(i, k));
			}	
			i=k; 
		}
		//System.err.println(tokens);
		return tokens;
	}
	
	private Queue<String> toPostfix(Queue<String> infix){
		Stack<String> operator = new Stack<String>();
		Queue<String> output = new LinkedList<String>();
		
		//shunting yard algorithm, see wikipedia page
		while(!infix.isEmpty()){
			if(isNumber(infix.peek()))
				output.add(infix.remove());
			else if(precedence(infix.peek())>=0){
				while(!operator.isEmpty() && (precedence(operator.peek())>precedence(infix.peek()) || precedence(operator.peek())==precedence(infix.peek()) && !operator.peek().equals("(")))
					output.add(operator.pop());
				operator.push(infix.remove());
			}
			else if(infix.peek().equals("("))
				operator.push(infix.remove());
			else if(infix.peek().equals(")")){
				while(!operator.peek().equals("("))
					output.add(operator.pop());
				operator.pop();
				infix.remove();
			}
			//System.err.println(output);
			//System.err.println(operator);
		}
		while(!operator.isEmpty())
			output.add(operator.pop());
		
		//System.err.println(output);
		return output;
	}
	
	private boolean isNumber(String str){
		if(str.length()==1 && ((str.charAt(0)>=65 && str.charAt(0)<=90) || (str.charAt(0)>=97 && str.charAt(0)<=122)))
			return true; 
		try{
			Double.parseDouble(str);
		}catch(NumberFormatException e){return false;}
		return true; 
	}
	
	private int precedence(String str){
		if(str.equals("^"))
			return 4;
		else if(str.equals("sqrt"))
			return 5;
		else if(str.equals("ln"))
			return 5;
		else if(str.equals("abs"))
			return 5;
		else if(str.equals("sin"))
			return 5; 
		else if(str.equals("cos"))
			return 5; 
		else if(str.equals("tan"))
			return 5; 
		else if(str.equals("*"))
			return 3;
		else if(str.equals("/"))
			return 3;
		else if(str.equals("+"))
			return 2;
		else if(str.equals("-"))
			return 2;
		else
			return -1; 
	}
	
	public double evaluate(){
		Queue<String> function = new LinkedList<String>(postfix); 
		return doMath(function);
	}
	
	public double evaluate(Double input){
		Queue<String> function = new LinkedList<String>(postfix);
		for(int i=0; i<function.size(); i++){
			if(!function.peek().equals(inputVar)){
				function.add(function.remove());
			}
			else{
				function.remove();
				function.add(input.toString());
			}		
		}
		
		//System.err.println(function);
		return doMath(function);
	}
	
	private double doMath(Queue<String> function) {
		Stack<Double> stack = new Stack<Double>();
		double left, right; 
		
		while(!function.isEmpty()){
			if(isNumber(function.peek()))
				stack.push(Double.parseDouble(function.remove()));
			else if(precedence(function.peek())>=5) {
				right = stack.pop();
				if(function.peek().equals("sqrt"))
					stack.push(Math.sqrt(right));
				else if(function.peek().equals("ln"))
					stack.push(Math.log(right));
				else if(function.peek().equals("abs"))
					stack.push(Math.abs(right));
				else if(function.peek().equals("sin"))
					stack.push(Math.sin(right)); 
				else if(function.peek().equals("cos"))
					stack.push(Math.cos(right)); 
				else if(function.peek().equals("tan"))
					stack.push(Math.tan(right)); 
				function.remove(); 
			}
			else if(precedence(function.peek())>-1) {
				right = stack.pop();
				left = stack.pop();
				if(function.peek().equals("^"))
					stack.push(Math.pow(left,right));
				else if(function.peek().equals("*"))
					stack.push(left*right);
				else if(function.peek().equals("/"))
					stack.push(left/right);
				else if(function.peek().equals("+"))
					stack.push(left+right);
				else if(function.peek().equals("-"))
					stack.push(left-right);
				function.remove();
			}
		}
		return stack.pop(); 
	}

}
