class Student:
    def __init__(self, student_id, name):
        self.student_id = student_id
        self.name = name
        self.discipline_records = []

    def add_discipline_record(self, record):
        self.discipline_records.append(record)

class DisciplineRecord:
    def __init__(self, date, description):
        self.date = date
        self.description = description

class Parent:
    def __init__(self, parent_id, name, email):
        self.parent_id = parent_id
        self.name = name
        self.email = email

class SchoolManagementSystem:
    def __init__(self):
        self.students = []
        self.parents = []

    def add_student(self, student):
        self.students.append(student)

    def add_parent(self, parent):
        self.parents.append(parent)

    def record_discipline_issue(self, student, description):
        if student in self.students:
            record = DisciplineRecord(date="2023-10-25", description=description)
            student.add_discipline_record(record)
        else:
            print("Student not found.")

    def notify_parent(self, parent, student, discipline_record):
        if parent in self.parents and student in self.students:
            print(f"Notifying {parent.name} about discipline issue for {student.name} on {discipline_record.date}: {discipline_record.description}")
        else:
            print("Parent or student not found.")

if __name__ == "__main__":
    school = SchoolManagementSystem()

    student1 = Student(101, "Alice Johnson")
    student2 = Student(102, "Bob Smith")

    parent1 = Parent(201, "John Johnson", "john.johnson@example.com")
    parent2 = Parent(202, "Sara Smith", "sara.smith@example.com")

    school.add_student(student1)
    school.add_student(student2)
    school.add_parent(parent1)
    school.add_parent(parent2)

    school.record_discipline_issue(student1, "Late to school.")
    school.record_discipline_issue(student2, "Disruptive behavior in class.")

    discipline_record = student1.discipline_records[0]

    school.notify_parent(parent1, student1, discipline_record)
