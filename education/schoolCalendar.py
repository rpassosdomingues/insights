from datetime import date, timedelta

class SchoolCalendarGenerator:
    def __init__(self, start_date, terms):
        self.start_date = start_date
        self.terms = terms

    def generate_calendar(self):
        calendar = []

        current_date = self.start_date
        for term, days in self.terms:
            term_dates = [current_date + timedelta(days=i) for i in range(days)]
            calendar.append((term, term_dates))
            current_date += timedelta(days=days)

        return calendar

def main():
    start_date = date(2023, 9, 1)  # Start date of the school year
    terms = [
        ("Fall Term", 90),      # Example: Fall term with 90 days
        ("Winter Term", 75),   # Example: Winter term with 75 days
        ("Spring Term", 80)    # Example: Spring term with 80 days
    ]

    generator = SchoolCalendarGenerator(start_date, terms)
    calendar = generator.generate_calendar()

    print("Generated School Calendar:")
    for term, term_dates in calendar:
        print(f"{term}:")
        for term_date in term_dates:
            print(f"  {term_date.strftime('%Y-%m-%d')}")

if __name__ == "__main__":
    main()
