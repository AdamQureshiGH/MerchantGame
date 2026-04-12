public class FoodItem extends Item {
    private int nutritionValue;

    public FoodItem(String n, int p, double w, int nutrition) {
        super(n, p, w);
        this.nutritionValue = nutrition;
    }

    public int getNutrition() { return nutritionValue; }
}
