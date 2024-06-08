class LocalSolutions:
    def __init__(self):
        self.coffee_plants = []
    
    def add_coffee_plant(self, name, location, quantity, price_per_kg):
        coffee_plant = CoffeePlant(name, location, quantity, price_per_kg)
        self.coffee_plants.append(coffee_plant)
    
    def find_coffee_plants(self, location):
        matching_plants = [plant for plant in self.coffee_plants if plant.location == location]
        return matching_plants

class CoffeePlant:
    def __init__(self, name, location, quantity, price_per_kg):
        self.name = name
        self.location = location
        self.quantity = quantity
        self.price_per_kg = price_per_kg

if __name__ == "__main__":
    local_solutions = LocalSolutions()

    # Adding coffee plant data
    local_solutions.add_coffee_plant("Arabica", "City A", 500, 10.5)
    local_solutions.add_coffee_plant("Robusta", "City B", 750, 9.0)
    local_solutions.add_coffee_plant("Arabica", "City C", 300, 11.0)

    # Find coffee plants in a specific location
    city_a_coffee_plants = local_solutions.find_coffee_plants("City A")
    for plant in city_a_coffee_plants:
        print(f"Coffee Plant in City A: {plant.name}, Quantity: {plant.quantity}, Price per kg: ${plant.price_per_kg}")
