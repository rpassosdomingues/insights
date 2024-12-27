import numpy as np
import matplotlib.pyplot as plt

# Sample data: Student names and their exam scores (can be loaded from a database)
students = ["Alice", "Bob", "Charlie", "David", "Eve"]
scores = [85, 92, 78, 89, 95]

# Define a function to map exam scores and visualize capabilities
def map_capabilities(students, scores):
    # Create a horizontal bar chart to display scores
    plt.figure(figsize=(10, 6))
    plt.barh(students, scores, color='skyblue')
    
    # Add labels and title
    plt.xlabel('Exam Scores')
    plt.title('Individual Exam Capabilities')
    
    # Display the chart
    plt.show()

# Call the function to generate the capability mapping
map_capabilities(students, scores)
