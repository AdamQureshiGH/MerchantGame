public class FoodItem extends Item {
    private int nutritionValue;

    public FoodItem(String n, int p, int w, int nutrition, String rumor) {
        super(n, p, w, rumor);
        this.nutritionValue = nutrition;
    }

    public int getNutrition() { return nutritionValue; }
}
