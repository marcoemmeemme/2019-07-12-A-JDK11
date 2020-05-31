package it.polito.tdp.food.model;
public class FoodCouple implements Comparable<FoodCouple>{
	private Food f1;
	private Food f2;
	private Double mediaCalories;
	public FoodCouple(Food f1, Food f2, Double mediaCalories) {
		super();
		this.f1 = f1;
		this.f2 = f2;
		this.mediaCalories = mediaCalories;
	}
	public Food getF1() {
		return f1;
	}
	public void setF1(Food f1) {
		this.f1 = f1;
	}
	public Food getF2() {
		return f2;
	}
	public void setF2(Food f2) {
		this.f2 = f2;
	}
	public Double getMediaCalories() {
		return mediaCalories;
	}
	public void setMediaCalories(Double mediaCalories) {
		this.mediaCalories = mediaCalories;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.f1.getDisplay_name()+"("+this.f1.getFood_code()+") e "+this.getF2().getDisplay_name()+"("+this.getF2().getFood_code()+"): "+this.mediaCalories+" calorie";
	}
	
	@Override
	public int compareTo(FoodCouple o) {
		return -(this.mediaCalories.compareTo(o.getMediaCalories()));
	}
	
}
