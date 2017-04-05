
public class Classifier implements Comparable<Classifier> {
	private Class name;
	private double euclideanDistance;
	
	public Classifier(Class name, double euclideanDistance){
		this.name = name;
		this.euclideanDistance = euclideanDistance;
	}
	
	public double getDistance(){
		return this.euclideanDistance;
	}
	
	public String getName(){
		return this.name.getName();
	}
	
	public String toString(){
		String s = this.name + " " + this.euclideanDistance;
		return s;
	}

	@Override
	public int compareTo(Classifier o) {
		// TODO Auto-generated method stub
		return Double.compare(this.getDistance(), o.getDistance());
	}
}
