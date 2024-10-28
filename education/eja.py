class Course:
    def __init__(self, course_id, course_name, duration, instructor):
        self.course_id = course_id
        self.course_name = course_name
        self.duration = duration
        self.instructor = instructor
        self.students = []

    def enroll_student(self, student):
        if student not in self.students:
            self.students.append(student)
            print(f"{student.name} has been enrolled in {self.course_name}")

    def list_students(self):
        for student in self.students:
            print(f"Student: {student.name}")

class Student:
    def __init__(self, student_id, name, age, email):
        self.student_id = student_id
        self.name = name
        self.age = age
        self.email = email
        self.courses_enrolled = []

    def enroll_in_course(self, course):
        course.enroll_student(self)
        self.courses_enrolled.append(course)

    def list_enrolled_courses(self):
        for course in self.courses_enrolled:
            print(f"Enrolled in: {course.course_name}")

class UpskillingProgram:
    def __init__(self, program_name, courses):
        self.program_name = program_name
        self.courses = courses

    def list_available_courses(self):
        print(f"Available Courses for {self.program_name}:")
        for course in self.courses:
            print(f"Course: {course.course_name}, Duration: {course.duration} weeks, Instructor: {course.instructor}")

if __name__ == "__main__":
    course1 = Course(1, "Web Development", 12, "John Doe")
    course2 = Course(2, "Data Science", 16, "Jane Smith")

    student1 = Student(101, "Alice Johnson", 25, "alice@example.com")
    student2 = Student(102, "Bob Smith", 30, "bob@example.com")

    upskilling_program = UpskillingProgram("Career Advancement", [course1, course2])

    print("Available Courses:")
    upskilling_program.list_available_courses()

    student1.enroll_in_course(course1)
    student2.enroll_in_course(course2)

    print("\nEnrolled Courses:")
    student1.list_enrolled_courses()
    student2.list_enrolled_courses()
