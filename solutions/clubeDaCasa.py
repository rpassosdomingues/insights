class ConstructionMaterial:
    def __init__(self, name, category, quantity, unit_price):
        self.name = name
        self.category = category
        self.quantity = quantity
        self.unit_price = unit_price

    def __str__(self):
        return f"{self.name} ({self.category}) - {self.quantity} units - ${self.unit_price:.2f} per unit"

class Inventory:
    def __init__(self):
        self.materials = []

    def add_material(self, material_item):
        self.materials.append(material_item)

    def remove_material(self, material_name):
        for material_item in self.materials:
            if material_item.name == material_name:
                self.materials.remove(material_item)
                return

    def get_materials_by_category(self, category):
        return [material_item for material_item in self.materials if material_item.category == category]

    def get_total_cost(self):
        total_cost = 0
        for material_item in self.materials:
            total_cost += material_item.quantity * material_item.unit_price
        return total_cost

if __name__ == "__main__":
    inventory = Inventory()

    cement = ConstructionMaterial("Cement", "Building Materials", 100, 10.0)
    steel = ConstructionMaterial("Steel Bars", "Structural", 500, 5.0)
    bricks = ConstructionMaterial("Bricks", "Building Materials", 2000, 0.2)

    inventory.add_material(cement)
    inventory.add_material(steel)
    inventory.add_material(bricks)

    print("Construction Materials in Inventory:")
    for material_item in inventory.materials:
        print(material_item)

    total_cost = inventory.get_total_cost()
    print(f"Total Cost of Materials: ${total_cost:.2f}")

    building_materials = inventory.get_materials_by_category("Building Materials")
    print("Building Materials in Inventory:")
    for material in building_materials:
        print(material)

    inventory.remove_material("Steel Bars")
    print("Removed Steel Bars from Inventory.")

    print("Updated Construction Materials in Inventory:")
    for material_item in inventory.materials:
        print(material_item)
