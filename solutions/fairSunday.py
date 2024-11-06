class Fair:
    def __init__(self, name, location, operating_hours):
        self.name = name
        self.location = location
        self.operating_hours = operating_hours
        self.sellers = []

    def add_seller(self, seller):
        self.sellers.append(seller)

class Seller:
    def __init__(self, name, products, contact_info, location):
        self.name = name
        self.products = products
        self.contact_info = contact_info
        self.location = location
        self.reviews = []

    def add_review(self, review):
        self.reviews.append(review)

class Review:
    def __init__(self, rating, comment):
        self.rating = rating
        self.comment = comment

class FairMap:
    def __init__(self):
        self.stalls = {}

    def add_stall(self, stall_name, location):
        self.stalls[stall_name] = location

class PriceComparator:
    def __init__(self):
        self.product_prices = {}

    def add_product_price(self, product, seller, price, availability):
        if product not in self.product_prices:
            self.product_prices[product] = []
        self.product_prices[product].append({"seller": seller, "price": price, "availability": availability})

class User:
    def __init__(self, username):
        self.username = username

    def leave_review(self, seller, rating, comment):
        review = Review(rating, comment)
        seller.add_review(review)

    def compare_prices(self, product, price_comparator):
        if product in price_comparator.product_prices:
            return price_comparator.product_prices[product]

# Example usage:
if __name__ == "__main__":
    fair = Fair("Alfenas Open Market", "Alfenas, MG", "Saturdays, 8:00 AM - 1:00 PM")
    seller1 = Seller("Fruit Paradise", ["Apples", "Bananas"], "fruitparadise@example.com", "Stall 1")
    seller2 = Seller("Crafty Creations", ["Handmade Crafts", "Jewelry"], "crafty@example.com", "Stall 2")
    
    fair.add_seller(seller1)
    fair.add_seller(seller2)
    
    user = User("JohnDoe")
    user.leave_review(seller1, 4.5, "Great selection of fruits!")
    
    fair_map = FairMap()
    fair_map.add_stall("Fruit Paradise", (44.655732, -90.324873))
    fair_map.add_stall("Crafty Creations", (44.656125, -90.325071))
    
    price_comparator = PriceComparator()
    price_comparator.add_product_price("Apples", "Fruit Paradise", 2.0, True)
    
    prices = user.compare_prices("Apples", price_comparator)
    if prices:
        for price in prices:
            print(f"Seller: {price['seller']}, Price: ${price['price']}, Availability: {price['availability']}")
