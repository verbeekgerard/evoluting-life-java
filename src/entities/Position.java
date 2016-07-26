package entities;

public class Position {

	public double x;
	public double y;
	public double a;
	
	public Position(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Position(double x, double y, double a){
		this.x = x;
		this.y = y;
		this.a = a;
	}
	
	public double calculateDistance(Position other)
	{
		return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
	}
}
