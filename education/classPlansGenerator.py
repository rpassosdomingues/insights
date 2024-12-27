import random

class ClassPlanGenerator:
    def __init__(self):
        self.subjects = {
            "math": ["Algebra", "Geometry", "Calculus", "Statistics", "Trigonometry"],
            "science": ["Biology", "Chemistry", "Physics", "Environmental Science"],
            "history": ["World History", "US History", "Ancient History", "Modern History"],
            "language": ["English", "French", "Spanish", "German", "Chinese"],
            "literature": ["Classic Literature", "Contemporary Literature", "Poetry"],
            # Add more subjects and topics
        }

    def generate_class_plan(self, subject, topic, duration):
        if subject not in self.subjects:
            return "Invalid subject. Choose from " + ", ".join(self.subjects.keys())

        if topic not in self.subjects[subject]:
            return "Invalid topic for this subject."

        # Generate a random class plan
        class_plan = {
            "subject": subject,
            "topic": topic,
            "duration": duration,
            "objectives": self.generate_objectives(),
            "activities": self.generate_activities(),
            "homework": self.generate_homework()
        }

        return class_plan

    def generate_objectives(self):
        objectives = [
            "Understand key concepts related to the topic.",
            "Demonstrate problem-solving skills.",
            "Apply critical thinking to real-world scenarios.",
            "Analyze and interpret relevant data or texts.",
        ]
        return random.sample(objectives, random.randint(2, 4))

    def generate_activities(self):
        activities = [
            "Interactive group discussion on the topic.",
            "Hands-on experiment or practical exercise.",
            "Quiz or class poll to assess understanding.",
            "Debate or presentation on a related issue.",
        ]
        return random.sample(activities, random.randint(2, 4))

    def generate_homework(self):
        homework = [
            "Read a specific chapter or article related to the topic.",
            "Solve practice problems or exercises.",
            "Write a short essay or response paper.",
            "Prepare a presentation for the next class.",
        ]
        return random.sample(homework, random.randint(1, 2))

def main():
    generator = ClassPlanGenerator()

    subject = "science"  # Replace with the desired subject
    topic = "Physics"  # Replace with the desired topic
    duration = 60  # Duration of the class in minutes

    class_plan = generator.generate_class_plan(subject, topic, duration)

    if isinstance(class_plan, dict):
        print("Generated Class Plan:")
        print(f"Subject: {class_plan['subject']}")
        print(f"Topic: {class_plan['topic']}")
        print(f"Duration: {class_plan['duration']} minutes")
        print("\nObjectives:")
        for objective in class_plan['objectives']:
            print(f"- {objective}")
        print("\nActivities:")
        for activity in class_plan['activities']:
            print(f"- {activity}")
        print("\nHomework:")
        for hw in class_plan['homework']:
            print(f"- {hw}")
    else:
        print(class_plan)

if __name__ == "__main__":
    main()
