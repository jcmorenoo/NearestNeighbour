
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Class which implements k-nearest neighbour algorithm
 * @author julian
 *
 */
public class NearestNeighbour {
	//3 is the default number of closes classes we consider.
	private final int k = 3;
	
	private List<Class> training;
	private List<Class> test;
	private List<ClassResult> result;
	private List<ClassResult> resultK3;
	
	private double minPetalLength;
	private double maxPetalLength;
	
	private double minPetalWidth;
	private double maxPetalWidth;
	
	private double minSepalLength;
	private double maxSepalLength;
	
	private double minSepalWidth;
	private double maxSepalWidth;
	
	
	
	
	
	public NearestNeighbour(File training, File test) {
		this.training = new ArrayList<Class>();
		this.test = new ArrayList<Class>();
		this.result = new ArrayList<ClassResult>();
		this.resultK3 = new ArrayList<ClassResult>();
		this.minPetalLength = Double.POSITIVE_INFINITY;
		this.maxPetalLength = Double.NEGATIVE_INFINITY;
		this.minPetalWidth = Double.POSITIVE_INFINITY;
		this.maxPetalWidth = Double.NEGATIVE_INFINITY;
		
		this.minSepalLength = Double.POSITIVE_INFINITY;
		this.maxSepalLength = Double.NEGATIVE_INFINITY;
		this.minSepalWidth = Double.POSITIVE_INFINITY;
		this.maxSepalWidth = Double.NEGATIVE_INFINITY;
		
		readFile(training, test);
		runNearestNeighbour();

		
	}

	private void readFile(File training, File test){
		try {
			BufferedReader brTraining = new BufferedReader(new FileReader(training));
			String line = null;
			while((line=brTraining.readLine()) != null){
				//setup training set
				Class c = null;
				String[] values = line.split("  ");

				if(values.length < 5){
					break;
				}
				double sepalLength = Double.parseDouble(values[0]);
				double sepalWidth = Double.parseDouble(values[1]) ;
				double petalLength = Double.parseDouble(values[2]);
				double petalWidth = Double.parseDouble(values[3]);
				String name = values[4];
				
				c = new Class(sepalLength, sepalWidth, petalLength, petalWidth, name);
				
				// set minimum and maximum
				//this is used to have a range
				if(this.minPetalLength > petalLength){
					this.minPetalLength = petalLength;
				}
				if(this.maxPetalLength < petalLength){
					this.maxPetalLength = petalLength;
				}
				if(this.minSepalLength > sepalLength){
					this.minSepalLength = sepalLength;
				}
				if(this.maxSepalLength < sepalLength){
					this.maxSepalLength = sepalLength;
				}
				if(this.minPetalWidth > petalWidth){
					this.minPetalWidth = petalWidth;
				}
				if(this.maxPetalWidth < petalWidth){
					this.maxPetalWidth = petalWidth;
				}
				if(this.minSepalWidth > sepalWidth){
					this.maxSepalWidth = sepalWidth;
				}
				if(this.maxSepalWidth < sepalWidth){
					this.maxSepalWidth = sepalWidth;
				}
				this.training.add(c);
			}
			
			brTraining.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			BufferedReader brTest = new BufferedReader(new FileReader(test));
			String line = null;
			try {
				while((line = brTest.readLine()) != null){
					Class c = null;
					
					String[] values = line.split("  ");
					
					//if the line has less than 5 values.
					//this is the end of the file.. it has a line but nothing in it
					if(values.length < 5){
						
						break;
					}
					double sepalLength = Double.parseDouble(values[0]);
					double sepalWidth = Double.parseDouble(values[1]) ;
					double petalLength = Double.parseDouble(values[2]);
					double petalWidth = Double.parseDouble(values[3]);
					String name = values[4];
					
					c = new Class(sepalLength, sepalWidth, petalLength, petalWidth, name);
					this.test.add(c);
					
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//runs the nearest neighbour test
	//finds whatever classes are closest to the test
	private void runNearestNeighbour(){
		int i = 0;
		while(i<this.test.size()){
			findNearest(this.test.get(i));
			i = i + 1;
		}
//		for(Class val : this.test){
//			findNearest(val);
//		}
		
		double count = 0;
		double total = 0;
		double accuracy = 0;
		double countK3 = 0;
		double totalK3 = 0;
		double accuracyK3 = 0;
		System.out.println("K-1 Results: ");
		for(ClassResult result : this.result){
			count = count + 1;
			if(result.isCorrect()){
				total = total + 1;
			}
			System.out.println(result.toString());
		}
		
		accuracy = total / count;
		
		
		countK3 = 0;
		totalK3 = 0;
		accuracyK3 = 0;
		System.out.println("\nK-3 Results: ");
		for(ClassResult result : this.resultK3){
			countK3 = countK3 + 1;
			if(result.isCorrect()){
				totalK3 = totalK3 + 1;
			}
			System.out.println(result.toString());
		}
		accuracyK3 = totalK3 / countK3;
		
		System.out.println("\nAccuracy when k = 1: " + accuracy);
		System.out.println("\nAccuracy when k = 3: " + accuracyK3);
	}
	
	/**
	 * This method checks classes which are the closest to the test.
	 * After the test, they are put into the fields.
	 * @param val
	 */
	private void findNearest(Class val){
		List<Classifier> classifiers = new ArrayList<Classifier>();
		double petalLengthRange = this.maxPetalLength - this.minPetalLength;
		double petalWidthRange = this.maxPetalWidth - this.minPetalWidth;
		double sepalLengthRange = this.maxSepalLength - this.minSepalLength;
		double sepalWidthRange = this.maxSepalLength - this.minSepalLength;
		
		for(Class train : this.training){

			
			double diffPetalLength = Math.pow(val.getPetalLength() - train.getPetalLength(), 2);
			double diffPetalWidth = Math.pow(val.getPetalWidth() - train.getPetalWidth(), 2);
			double diffSepalLength = Math.pow(val.getSepalLength() - train.getSepalLength(), 2);
			double diffSepalWidth = Math.pow(val.getSepalWidth() - train.getSepalWidth(), 2);
			
			double distance = Math.sqrt((diffPetalLength/Math.pow(petalLengthRange, 2)) + (diffPetalWidth/Math.pow(petalWidthRange, 2)) + (diffSepalLength/Math.pow(sepalLengthRange, 2)) + (diffSepalWidth/Math.pow(sepalWidthRange, 2)));
			
			//System.out.println(distance); 
			
			Classifier cl = new Classifier(train, distance);
			
			classifiers.add(cl);
			
		}
		
		//this sorts out the collection based on the distances
		Collections.sort(classifiers);
		
		this.result.add(new ClassResult(val, classifiers.get(0).getName()));
		
		int count = 0;
		int countVer = 0;
		int countVir = 0;
		int countSet = 0;
		while(count < this.k){
			//System.out.println(classifiers.get(count).getName() + " : " + val.getName());
			String name = classifiers.get(count).getName();
			if(name.equalsIgnoreCase("Iris-virginica")){
				countVir = countVir + 1;
			}
			if(name.equalsIgnoreCase("Iris-setosa")){
				countSet = countSet + 1;
			}
			if(name.equalsIgnoreCase("Iris-versicolor")){
				countVer = countVer + 1;
			}
			count = count + 1;
		}
		
		//select the one that has most count.
		if(countVer > countSet && countVer > countVir){
			this.resultK3.add(new ClassResult(val,"Iris-versicolor"));
		}
		
		if(countSet > countVer && countSet > countVir){
			this.resultK3.add(new ClassResult(val, "Iris-setosa"));
		}
		if(countVir > countSet && countVir > countVer){
			this.resultK3.add(new ClassResult(val, "Iris-virginica"));
		}
		
		
		
	}
	
	
	/*
	 * Start point of the program
	 */
	public static void main(String[] args){
		
		
		File training = null;
		File test = null;
		if(args.length == 2){
			String trainingSet = args[0];
			String testSet = args[1];
			training = new File( trainingSet);
			test = new File( testSet);
			new NearestNeighbour(training, test);
			
		}
		else{
			String trainingSet = "iris-training.txt"; 
			String testSet = "iris-test.txt";
			training = new File(  trainingSet);
			test = new File( testSet);
			new NearestNeighbour(training, test);
		}
		
	}
	
	
}
