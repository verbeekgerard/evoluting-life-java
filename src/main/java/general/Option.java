package general;

public class Option {

	public double defaultValue;
	public double internalValue;
	
	public Option(double value){
		defaultValue = value;
		internalValue = value;
	}
	
	public void reset(){
		internalValue = defaultValue;
	}
	
	public double get(){
		return internalValue;
	}
	
	public void set(double value){
		internalValue = value;
	}
}
