import requests
from geopy.geocoders import Nominatim

class FuelPriceTracker:
    def __init__(self, fuel_api_url):
        self.fuel_api_url = fuel_api_url

    def get_fuel_prices(self, location, fuel_type):
        # Simulate API call to get fuel prices
        response = requests.get(f"{self.fuel_api_url}?location={location}&fuel_type={fuel_type}")
        if response.status_code == 200:
            return response.json()
        else:
            print("Failed to retrieve fuel prices.")
            return None

class GPS:
    def __init__(self):
        self.geolocator = Nominatim(user_agent="myGeocoder")

    def get_coordinates(self, address):
        try:
            location = self.geolocator.geocode(address)
            if location:
                return location.latitude, location.longitude
            else:
                return None
        except:
            print("Error occurred while getting coordinates.")
            return None

class TripPlanner:
    def __init__(self, start_address, end_address):
        self.start_address = start_address
        self.end_address = end_address
        self.gps = GPS()

    def plan_trip(self, fuel_type):
        start_coordinates = self.gps.get_coordinates(self.start_address)
        end_coordinates = self.gps.get_coordinates(self.end_address)

        if start_coordinates and end_coordinates:
            fuel_tracker = FuelPriceTracker("https://example.com/fuel-api")

            # Get fuel prices at the starting and ending locations
            start_fuel_prices = fuel_tracker.get_fuel_prices(self.start_address, fuel_type)
            end_fuel_prices = fuel_tracker.get_fuel_prices(self.end_address, fuel_type)

            if start_fuel_prices and end_fuel_prices:
                cheapest_start_station = min(start_fuel_prices, key=lambda x: x['price'])
                cheapest_end_station = min(end_fuel_prices, key=lambda x: x['price'])

                print(f"Starting Address: {self.start_address}")
                print(f"Cheapest Gas Station: {cheapest_start_station['name']} - Price: ${cheapest_start_station['price']} per gallon")
                print(f"Ending Address: {self.end_address}")
                print(f"Cheapest Gas Station: {cheapest_end_station['name']} - Price: ${cheapest_end_station['price']} per gallon")
            else:
                print("Failed to retrieve fuel prices for the trip locations.")
        else:
            print("Failed to retrieve coordinates for trip locations.")

if __name__ == "__main__":
    start_address = "123 Main St, City A"
    end_address = "456 Elm St, City B"
    fuel_type = "regular"  # Replace with the desired fuel type

    trip_planner = TripPlanner(start_address, end_address)
    trip_planner.plan_trip(fuel_type)
