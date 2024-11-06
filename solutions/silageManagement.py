class SilageManagement:
    def __init__(self):
        self.silage_pile = []  # Represents the silage pile
        self.moisture_level = 0.0  # Represents the current moisture level

    def add_forage(self, forage, weight):
        # Add forage to the silage pile
        self.silage_pile.append((forage, weight))
        self.moisture_level += forage.moisture_content * weight

    def compact_silage(self):
        # Simulate the compaction process by removing air pockets
        print("Compacting silage...")

    def measure_moisture(self):
        # Simulate measuring moisture level
        print(f"Current moisture level: {self.moisture_level}")

class Forage:
    def __init__(self, name, moisture_content):
        self.name = name
        self.moisture_content = moisture_content

if __name__ == "__main__":
    silage_management = SilageManagement()

    # Create forage types
    alfalfa = Forage("Alfalfa", 50)  # Example moisture content: 50%
    clover = Forage("Clover", 45)    # Example moisture content: 45%

    # Add forage to the silage pile
    silage_management.add_forage(alfalfa, 1000)  # 1000 kg of alfalfa
    silage_management.add_forage(clover, 800)    # 800 kg of clover

    # Compact the silage pile to remove air pockets
    silage_management.compact_silage()

    # Measure the moisture level
    silage_management.measure_moisture()
