package ABCalgorithm;

import java.util.HashMap;

public class Distribution {
	Long id;
	Division[] divisions;
	Double fitness;
	Integer trailCounter = 0;//The trail counter of the unmodified distribution indicates how much failed attempts have been made to improve the fitness of the unmodified distribution.
	
	public Distribution(int numOfDivisions){
		this.divisions = new Division[numOfDivisions];
	}
	
	/*public Distribution(){
		factors.put("UrgencyFactor", 0.0);
		factors.put("DistanceFactor", 0.0);
		factors.put("LoadFactor", 0.0);
	}*/
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Division[] getDivisions() {
		return this.divisions;
	}
	public void setDivisions(Division[] divisions) {
		this.divisions = divisions;
	}

	public Double getFitness() {
		return fitness;
	}

	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}

	public Integer getTrailCounter() {
		return trailCounter;
	}

	public void setTrailCounter(Integer trailCounter) {
		this.trailCounter += trailCounter;
	}
	
}
