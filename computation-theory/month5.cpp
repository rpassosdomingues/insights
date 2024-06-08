#include <iostream>

// Generic Model class template
template <typename T>
class Model {
public:
    Model() : data(T()) {}

    // Getter method to retrieve the data
    T getData() const {
        return data;
    }

    // Setter method to modify the data
    void setData(const T& newData) {
        data = newData;
    }

private:
    T data;
};

int main() {
    // Creating model instances for different data types
    Model<int> intModel;
    Model<double> doubleModel;
    Model<std::string> stringModel;

    // Setting data for each model
    intModel.setData(42);
    doubleModel.setData(3.14);
    stringModel.setData("Hello, world!");

    // Getting and printing data from each model
    std::cout << "Integer Model: " << intModel.getData() << std::endl;
    std::cout << "Double Model: " << doubleModel.getData() << std::endl;
    std::cout << "String Model: " << stringModel.getData() << std::endl;

    return 0;
}
