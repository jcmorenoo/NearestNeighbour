/**
 * Represents a class which contains name, size of the plant
 * @author julian
 *
 */
public class Class {
	private String name;
	private double sepalLength;
	private double sepalWidth;
	private double petalLength;
	private double petalWidth;
	
	
	
	
	public Class(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String name){
		this.name = name;
		this.sepalLength = sepalLength;
		this.sepalWidth = sepalWidth;
		this.petalLength = petalLength;
		this.petalWidth = petalWidth;
	}
	
	public double calculateDistance(double sepalLength, double sepalWidth, double petalLength, double petalWidth){
		
		return 0;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.name);
		sb.append(" " + this.sepalLength);
		sb.append(" " + this.sepalWidth);
		sb.append(" " + this.petalLength);
		sb.append(" " + this.petalWidth);
		String s = sb.toString();
		return s;
	}
	
	public double getSepalLength(){
		return this.sepalLength;
	}
	
	public double getSepalWidth(){
		return this.sepalWidth;
	}
	
	public double getPetalLength(){
		return this.petalLength;
	}
	
	public double getPetalWidth(){
		return this.petalWidth;
	}
	
	public String getName(){
		return this.name;
	}
	
}
