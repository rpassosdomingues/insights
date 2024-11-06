import random
import string

class ExamGenerator:
    def __init__(self, num_questions, num_choices, max_question_length, max_choice_length):
        self.num_questions = num_questions
        self.num_choices = num_choices
        self.max_question_length = max_question_length
        self.max_choice_length = max_choice_length

    def generate_exam(self):
        exam = []
        for _ in range(self.num_questions):
            question = self.generate_question()
            choices = self.generate_choices()
            correct_answer = random.choice(choices)
            exam.append((question, choices, correct_answer))
        return exam

    def generate_question(self):
        question = ''.join(random.choice(string.ascii_letters) for _ in range(self.max_question_length))
        return f"Question: {question}"

    def generate_choices(self):
        choices = []
        for _ in range(self.num_choices):
            choice = ''.join(random.choice(string.ascii_letters) for _ in range(self.max_choice_length))
            choices.append(choice)
        return choices

def main():
    num_questions = 10  # Number of questions in the exam
    num_choices = 4    # Number of choices for each question
    max_question_length = 100  # Maximum length of each question
    max_choice_length = 20     # Maximum length of each choice

    generator = ExamGenerator(num_questions, num_choices, max_question_length, max_choice_length)
    exam = generator.generate_exam()

    print("Generated Exam:")
    for i, (question, choices, correct_answer) in enumerate(exam, start=1):
        print(f"Q{i}: {question}")
        for j, choice in enumerate(choices, start=1):
            print(f"  {j}. {choice}")
        print(f"Correct Answer: {correct_answer}")
        print()

if __name__ == "__main__":
    main()
