class Animal:
    def __init__(self, name, species, age, weight):
        self.name = name
        self.species = species
        self.age = age
        self.weight = weight
        self.diet = []

    def add_to_diet(self, food_item):
        self.diet.append(food_item)

    def calculate_daily_nutrition(self):
        total_protein = 0
        total_fat = 0
        total_carbohydrates = 0
        total_fiber = 0
        total_vitamins = {}

        for food_item in self.diet:
            total_protein += food_item.protein
            total_fat += food_item.fat
            total_carbohydrates += food_item.carbohydrates
            total_fiber += food_item.fiber

            for vitamin, amount in food_item.vitamins.items():
                if vitamin in total_vitamins:
                    total_vitamins[vitamin] += amount
                else:
                    total_vitamins[vitamin] = amount

        return {
            "protein": total_protein,
            "fat": total_fat,
            "carbohydrates": total_carbohydrates,
            "fiber": total_fiber,
            "vitamins": total_vitamins
        }

class FoodItem:
    def __init__(self, name, protein, fat, carbohydrates, fiber, vitamins):
        self.name = name
        self.protein = protein
        self.fat = fat
        self.carbohydrates = carbohydrates
        self.fiber = fiber
        self.vitamins = vitamins

if __name__ == "__main__":
    # Create animal
    cow = Animal("Bessie", "Cow", 3, 500)

    # Create food items
    grass = FoodItem("Grass", 10, 2, 30, 15, {"Vitamin A": 1000, "Vitamin D": 200})
    hay = FoodItem("Hay", 15, 3, 40, 10, {"Vitamin D": 500, "Vitamin E": 50})

    # Add food items to the cow's diet
    cow.add_to_diet(grass)
    cow.add_to_diet(hay)

    # Calculate daily nutrition for the cow
    nutrition = cow.calculate_daily_nutrition()

    print(f"{cow.name}'s Daily Nutrition:")
    print(f"Protein: {nutrition['protein']} grams")
    print(f"Fat: {nutrition['fat']} grams")
    print(f"Carbohydrates: {nutrition['carbohydrates']} grams")
    print(f"Fiber: {nutrition['fiber']} grams")
    print("Vitamins:")
    for vitamin, amount in nutrition['vitamins'].items():
        print(f"{vitamin}: {amount} IU/mg")
