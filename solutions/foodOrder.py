class FoodItem:
    def __init__(self, name, price):
        self.name = name
        self.price = price

class Order:
    def __init__(self):
        self.items = []

    def add_item(self, item, quantity=1):
        self.items.append((item, quantity))

    def calculate_total(self):
        total = 0
        for item, quantity in self.items:
            total += item.price * quantity
        return total

class Customer:
    def __init__(self, name, phone):
        self.name = name
        self.phone = phone

class SalesAutomation:
    def __init__(self):
        self.orders = []
        self.customers = []

    def create_customer(self, name, phone):
        customer = Customer(name, phone)
        self.customers.append(customer)
        return customer

    def create_order(self, customer):
        order = Order()
        self.orders.append((customer, order))
        return order

    def print_invoice(self, customer, order):
        print(f'Invoice for {customer.name} ({customer.phone}):')
        total = order.calculate_total()
        for item, quantity in order.items:
            print(f'{item.name} x{quantity}: ${item.price * quantity}')
        print(f'Total: ${total}')
        print()

if __name__ == "__main__":
    food_menu = {
        "Burger": 5.99,
        "Pizza": 7.99,
        "Salad": 3.99,
        "Soda": 1.99
    }

    automation = SalesAutomation()

    # Create customers
    customer1 = automation.create_customer("John Doe", "123-456-7890")
    customer2 = automation.create_customer("Alice Smith", "987-654-3210")

    # Create orders
    order1 = automation.create_order(customer1)
    order1.add_item(FoodItem("Burger", food_menu["Burger"]))
    order1.add_item(FoodItem("Soda", food_menu["Soda"]))

    order2 = automation.create_order(customer2)
    order2.add_item(FoodItem("Pizza", food_menu["Pizza"]))
    order2.add_item(FoodItem("Salad", food_menu["Salad"]))

    # Print invoices
    for customer, order in automation.orders:
        automation.print_invoice(customer, order)
