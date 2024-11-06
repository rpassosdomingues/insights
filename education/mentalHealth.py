class Student:
    def __init__(self, student_id, name, age):
        self.student_id = student_id
        self.name = name
        self.age = age
        self.mental_health_resources = []

    def access_resource(self, resource):
        if resource not in self.mental_health_resources:
            self.mental_health_resources.append(resource)
            print(f"{self.name} has accessed the resource: {resource.name}")

class MentalHealthResource:
    def __init__(self, resource_id, name, description):
        self.resource_id = resource_id
        self.name = name
        self.description = description

class MentalHealthSupportSystem:
    def __init__(self):
        self.students = []
        self.resources = []

    def add_student(self, student):
        self.students.append(student)

    def add_resource(self, resource):
        self.resources.append(resource)

    def list_resources(self):
        for resource in self.resources:
            print(f"Resource: {resource.name}\nDescription: {resource.description}\n")

if __name__ == "__main__":
    student1 = Student(101, "Alice Johnson", 20)
    student2 = Student(102, "Bob Smith", 25)

    resource1 = MentalHealthResource(1, "Stress Management Guide", "A guide to managing stress and anxiety.")
    resource2 = MentalHealthResource(2, "Counseling Services", "Access to professional counseling services.")

    support_system = MentalHealthSupportSystem()

    support_system.add_student(student1)
    support_system.add_student(student2)
    support_system.add_resource(resource1)
    support_system.add_resource(resource2)

    print("Available Mental Health Resources:")
    support_system.list_resources()

    student1.access_resource(resource1)
    student2.access_resource(resource2)
