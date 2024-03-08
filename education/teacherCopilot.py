class Student:
    def __init__(self, student_id, name, grade):
        self.student_id = student_id
        self.name = name
        self.grade = grade
        self.attendance = {}

    def mark_attendance(self, date, status):
        self.attendance[date] = status

class Teacher:
    def __init__(self, teacher_id, name, subject):
        self.teacher_id = teacher_id
        self.name = name
        self.subject = subject

class Course:
    def __init__(self, course_id, name, teacher, students=[]):
        self.course_id = course_id
        self.name = name
        self.teacher = teacher
        self.students = students

    def enroll_student(self, student):
        self.students.append(student)

class School:
    def __init__(self):
        self.students = []
        self.teachers = []
        self.courses = []

    def add_student(self, student):
        self.students.append(student)

    def add_teacher(self, teacher):
        self.teachers.append(teacher)

    def add_course(self, course):
        self.courses.append(course)

    def list_students(self):
        for student in self.students:
            print(f"Student ID: {student.student_id}, Name: {student.name}, Grade: {student.grade}")

    def list_teachers(self):
        for teacher in self.teachers:
            print(f"Teacher ID: {teacher.teacher_id}, Name: {teacher.name}, Subject: {teacher.subject}")

    def list_courses(self):
        for course in self.courses:
            print(f"Course ID: {course.course_id}, Name: {course.name}, Teacher: {course.teacher.name}")

if __name__ == "__main__":
    school = School()

    teacher1 = Teacher(1, "John Smith", "Math")
    teacher2 = Teacher(2, "Alice Johnson", "Science")

    student1 = Student(101, "Bob Brown", "Grade 10")
    student2 = Student(102, "Sara Wilson", "Grade 9")

    math_course = Course(1, "Mathematics 101", teacher1, [student1, student2])
    science_course = Course(2, "Science 101", teacher2, [student2])

    school.add_student(student1)
    school.add_student(student2)
    school.add_teacher(teacher1)
    school.add_teacher(teacher2)
    school.add_course(math_course)
    school.add_course(science_course)

    student1.mark_attendance("2023-10-01", "Present")
    student1.mark_attendance("2023-10-02", "Present")
    student2.mark_attendance("2023-10-01", "Present")
    student2.mark_attendance("2023-10-02", "Absent")

    print("Students:")
    school.list_students()
    print("\nTeachers:")
    school.list_teachers()
    print("\nCourses:")
    school.list_courses()
