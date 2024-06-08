class Teacher:
    def __init__(self, teacher_id, name, subjects, max_daily_classes):
        self.teacher_id = teacher_id
        self.name = name
        self.subjects = subjects
        self.max_daily_classes = max_daily_classes
        self.classes = []

    def add_class(self, class_obj):
        if len(self.classes) < self.max_daily_classes:
            if class_obj.subject in self.subjects:
                self.classes.append(class_obj)
                class_obj.assign_teacher(self)
                print(f"{self.name} has been assigned to teach {class_obj.subject} class.")
            else:
                print(f"{self.name} is not authorized to teach {class_obj.subject}.")
        else:
            print(f"{self.name} has reached the maximum number of daily classes.")

    def list_classes(self):
        print(f"Classes assigned to {self.name}:")
        for class_obj in self.classes:
            print(class_obj.subject)

class Class:
    def __init__(self, class_id, subject, start_time, end_time):
        self.class_id = class_id
        self.subject = subject
        self.start_time = start_time
        self.end_time = end_time
        self.teacher = None

    def assign_teacher(self, teacher):
        self.teacher = teacher

class ClassReservationSystem:
    def __init__(self):
        self.teachers = []
        self.classes = []

    def add_teacher(self, teacher):
        self.teachers.append(teacher)

    def add_class(self, class_obj):
        self.classes.append(class_obj)

    def list_teachers(self):
        print("Available Teachers:")
        for teacher in self.teachers:
            print(f"Teacher ID: {teacher.teacher_id}, Name: {teacher.name}, Subjects: {', '.join(teacher.subjects)}")

    def list_classes(self):
        print("Available Classes:")
        for class_obj in self.classes:
            print(f"Class ID: {class_obj.class_id}, Subject: {class_obj.subject}, Time: {class_obj.start_time} - {class_obj.end_time}")

if __name__ == "__main__":
    teacher1 = Teacher(1, "John Doe", ["Math", "Science"], max_daily_classes=3)
    teacher2 = Teacher(2, "Jane Smith", ["English", "History"], max_daily_classes=2)

    class1 = Class(101, "Math", "9:00 AM", "10:30 AM")
    class2 = Class(102, "English", "10:45 AM", "12:15 PM")

    reservation_system = ClassReservationSystem()

    reservation_system.add_teacher(teacher1)
    reservation_system.add_teacher(teacher2)
    reservation_system.add_class(class1)
    reservation_system.add_class(class2)

    print("Available Teachers:")
    reservation_system.list_teachers()

    print("\nAvailable Classes:")
    reservation_system.list_classes()

    teacher1.add_class(class1)
    teacher2.add_class(class2)
