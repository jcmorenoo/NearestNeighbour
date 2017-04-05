
public class ClassResult {
	private Class c;
	private String result;
	
	public ClassResult(Class c, String result){
		this.c = c;
		this.result = result;
	}
	
	public boolean isCorrect(){
		return (c.getName().equals(result));
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Expected Result: " + this.c.getName());
		sb.append(" Got: " + this.result);
		return sb.toString();
	}
	
	
}
