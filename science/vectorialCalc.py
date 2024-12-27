import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

class VectorField:
    def __init__(self, x, y, z):
        self.x = x
        self.y = y
        self.z = z

class GravitationalField(VectorField):
    def __init__(self, x, y, z):
        super().__init__(x, y, z)

    def gravitational_gradient(self):
        return VectorField(0, 0, -1)

    def gravitational_divergence(self):
        return 0

    def gravitational_laplacian(self):
        return 0

class MagneticField(VectorField):
    def __init__(self, x, y, z):
        super().__init__(x, y, z)

    def magnetic_gradient(self):
        return VectorField(0, 1, 0)

    def magnetic_divergence(self):
        return 0

    def magnetic_laplacian(self):
        return 0

class ElectricField(VectorField):
    def __init__(self, x, y, z):
        super().__init__(x, y, z)

    def electric_gradient(self):
        return VectorField(1, 0, 0)

    def electric_divergence(self):
        return 0

    def electric_laplacian(self):
        return 0

def visualize_vector_field(field, title):
    fig = plt.figure(figsize=(10, 8))
    ax = fig.add_subplot(111, projection='3d')
    ax.quiver(field.x, field.y, field.z, field.x, field.y, field.z, length=0.1, normalize=True, color='b')
    ax.set_title(title)
    plt.show()

# Create a grid of points
x = np.linspace(-5, 5, 20)
y = np.linspace(-5, 5, 20)
z = np.linspace(-5, 5, 20)
x, y, z = np.meshgrid(x, y, z)

# Create instances of vector fields
gravitational_field = GravitationalField(x, y, z)
magnetic_field = MagneticField(x, y, z)
electric_field = ElectricField(x, y, z)

# Visualize vector fields
visualize_vector_field(gravitational_field, "Gravitational Field")
visualize_vector_field(magnetic_field, "Magnetic Field")
visualize_vector_field(electric_field, "Electric Field")
