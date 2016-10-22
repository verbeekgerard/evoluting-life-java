package eu.luminis.general;

public class Option {

	private final double defaultValue;
	private double internalValue;
	
	public Option(double value) {
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
