/**
 * This code demonstrates how to work with spherical coordinates and create a 4-dimensional figure.
 * Author: Rafael Passos Domingues
*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// Define the dimensions of the 4D figure in spherical coordinates
#define DIM_R 3
#define DIM_THETA 3
#define DIM_PHI 3
#define DIM_T 3

#define PI 3.1415

// Convert spherical coordinates to Cartesian coordinates
void spherical_to_cartesian (double r, double theta, double phi, double *x, double *y, double *z) {
    *x = r * sin(theta) * cos(phi);
    *y = r * sin(theta) * sin(phi);
    *z = r * cos(theta);
}

// Convert Cartesian coordinates to spherical coordinates
void cartesian_to_spherical (double x, double y, double z, double *r, double *theta, double *phi) {
    *r = sqrt(x * x + y * y + z * z);
    *theta = acos(z / *r);
    *phi = atan2(y, x);
}

// Fill the dynamically allocated memory positions in spherical coordinates
void fill_values (int ****ptr, int dim_r, int dim_theta, int dim_phi, int dim_t) {
    // Allocate the first dimension (r)
    for (int i = 0; i < dim_r; i++) {
        double r = i + 1.0; // Arbitrary value for r

        // Allocate the second dimension (theta)
        for (int j = 0; j < dim_theta; j++) {
            double theta = j * PI / (dim_theta - 1); // Theta angle varies from 0 to pi

            // Allocate the third dimension (phi)
            for (int k = 0; k < dim_phi; k++) {
                double phi = k * 2 * PI / (dim_phi - 1); // Phi angle varies from 0 to 2*pi

                // Allocate the fourth dimension (time)
                for (int t = 0; t < dim_t; t++) {
                    double x, y, z;
                    spherical_to_cartesian(r, theta, phi, &x, &y, &z);
                    ptr[i][j][k][t] = (int)(x + y + z + t); // Using the x-coordinate as the value
                }
            }
        }
    }
}

// Print the values on the console
void print_values (int ****ptr, int dim_r, int dim_theta, int dim_phi, int dim_t) {
    for (int i = 0; i < dim_r; i++) {
        for (int j = 0; j < dim_theta; j++) {
            for (int k = 0; k < dim_phi; k++) {
                for (int t = 0; t < dim_t; t++) {
                    printf("%4d ", ptr[i][j][k][t]);
                }
                printf("\n");
            }
            printf("\n");
        }
        printf("\n");
    }
}

// Free the dynamically allocated memory
void deallocate_4D_figure (int ****ptr, int dim_r, int dim_theta, int dim_phi) {
    for (int i = 0; i < dim_r; i++) {
        for (int j = 0; j < dim_theta; j++) {
            for (int k = 0; k < dim_phi; k++) {
                free((ptr)[i][j][k]);
            }
            free((ptr)[i][j]);
        }
        free((ptr)[i]);
    }
    free(ptr);
}

int main () {
    int ****fourD_figure;

    // Dimensions of the 4D figure in spherical coordinates
    int dim_r = DIM_R;
    int dim_theta = DIM_THETA;
    int dim_phi = DIM_PHI;
    int dim_t = DIM_T;

    // Allocate the 4D figure
    fourD_figure = (int ****)malloc(dim_r * sizeof(int ***));
    for (int i = 0; i < dim_r; i++) {
        fourD_figure[i] = (int ***)malloc(dim_theta * sizeof(int **));
        for (int j = 0; j < dim_theta; j++) {
            fourD_figure[i][j] = (int **)malloc(dim_phi * sizeof(int *));
            for (int k = 0; k < dim_phi; k++) {
                fourD_figure[i][j][k] = (int *)malloc(dim_t * sizeof(int));
            }
        }
    }

    // Read values from an input file
    FILE *inputFile = fopen("input.txt", "r");
    if (inputFile == NULL) {
        fprintf(stderr, "Error: Cannot open input file\n");
        return 1;
    }

    for (int i = 0; i < dim_r; i++) {
        for (int j = 0; j < dim_theta; j++) {
            for (int k = 0; k < dim_phi; k++) {
                for (int t = 0; t < dim_t; t++) {
                    fscanf(inputFile, "%d", &fourD_figure[i][j][k][t]);
                }
            }
        }
    }

    fclose(inputFile);

    printf("4D Figure:\n");
    print_values(fourD_figure, dim_r, dim_theta, dim_phi, dim_t);
    deallocate_4D_figure(fourD_figure, dim_r, dim_theta, dim_phi);

    return 0;
}
