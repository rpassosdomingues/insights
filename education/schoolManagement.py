class Student:
    def __init__(self, student_id, name, age, grade):
        self.student_id = student_id
        self.name = name
        self.age = age
        self.grade = grade

class Teacher:
    def __init__(self, teacher_id, name, subject):
        self.teacher_id = teacher_id
        self.name = name
        self.subject = subject

class Course:
    def __init__(self, course_id, name, teacher):
        self.course_id = course_id
        self.name = name
        self.teacher = teacher

class SchoolManagementSystem:
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

    def assign_student_to_course(self, student, course):
        if student in self.students and course in self.courses:
            course.enroll_student(student)
            student.enroll_in_course(course)
        else:
            print("Student or course not found.")

    def list_students(self):
        for student in self.students:
            print(f"Student ID: {student.student_id}, Name: {student.name}, Age: {student.age}, Grade: {student.grade}")

    def list_teachers(self):
        for teacher in self.teachers:
            print(f"Teacher ID: {teacher.teacher_id}, Name: {teacher.name}, Subject: {teacher.subject}")

    def list_courses(self):
        for course in self.courses:
            print(f"Course ID: {course.course_id}, Name: {course.name}, Teacher: {course.teacher.name}")

class CourseEnrollment:
    def __init__(self, course):
        self.course = course
        self.students = []

    def enroll_student(self, student):
        self.students.append(student)

class CourseManagement:
    def __init__(self):
        self.course_enrollments = []

    def create_course_enrollment(self, course):
        enrollment = CourseEnrollment(course)
        self.course_enrollments.append(enrollment)
        return enrollment

if __name__ == "__main__":
    school = SchoolManagementSystem()

    math_teacher = Teacher(1, "John Smith", "Math")
    history_teacher = Teacher(2, "Alice Johnson", "History")

    math_course = Course(101, "Mathematics 101", math_teacher)
    history_course = Course(102, "World History", history_teacher)

    school.add_teacher(math_teacher)
    school.add_teacher(history_teacher)

    school.add_course(math_course)
    school.add_course(history_course)

    student1 = Student(101, "Bob Johnson", 16, "A")
    student2 = Student(102, "Sara Miller", 17, "B")

    school.add_student(student1)
    school.add_student(student2)

    school.assign_student_to_course(student1, math_course)
    school.assign_student_to_course(student2, history_course)

    print("Students:")
    school.list_students()
    print("\nTeachers:")
    school.list_teachers()
    print("\nCourses:")
    school.list_courses()
