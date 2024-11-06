class AgriculturalInput:
    def __init__(self, name, category, quantity, price_per_unit):
        self.name = name
        self.category = category
        self.quantity = quantity
        self.price_per_unit = price_per_unit

    def __str__(self):
        return f"{self.name} ({self.category}) - {self.quantity} units - ${self.price_per_unit:.2f} per unit"

class Inventory:
    def __init__(self):
        self.inputs = []

    def add_input(self, input_item):
        self.inputs.append(input_item)

    def remove_input(self, input_name):
        for input_item in self.inputs:
            if input_item.name == input_name:
                self.inputs.remove(input_item)
                return

    def get_input_by_category(self, category):
        return [input_item for input_item in self.inputs if input_item.category == category]

    def get_total_cost(self):
        total_cost = 0
        for input_item in self.inputs:
            total_cost += input_item.quantity * input_item.price_per_unit
        return total_cost

if __name__ == "__main__":
    inventory = Inventory()

    seed1 = AgriculturalInput("Wheat Seeds", "Seeds", 500, 1.5)
    fertilizer1 = AgriculturalInput("Nitrogen Fertilizer", "Fertilizer", 100, 10.0)
    pesticide1 = AgriculturalInput("Insecticide", "Pesticide", 10, 25.0)

    inventory.add_input(seed1)
    inventory.add_input(fertilizer1)
    inventory.add_input(pesticide1)

    print("Agricultural Inputs in Inventory:")
    for input_item in inventory.inputs:
        print(input_item)

    total_cost = inventory.get_total_cost()
    print(f"Total Cost of Inputs: ${total_cost:.2f}")

    seeds = inventory.get_input_by_category("Seeds")
    print("Seeds in Inventory:")
    for seed in seeds:
        print(seed)

    inventory.remove_input("Insecticide")
    print("Removed Insecticide from Inventory.")

    print("Updated Agricultural Inputs in Inventory:")
    for input_item in inventory.inputs:
        print(input_item)
