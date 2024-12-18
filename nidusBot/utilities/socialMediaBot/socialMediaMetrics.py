import matplotlib.pyplot as plt
import numpy as np

class Engagement:
    def __init__(self, views, likes, shares, comments, saves, reactions):
        self.views = views
        self.likes = likes
        self.shares = shares
        self.comments = comments
        self.saves = saves
        self.reactions = reactions

    def get_metrics_as_list(self):
        return [getattr(self, metric) for metric in sorted(vars(self))]

class Team:
    def __init__(self, team_name, post_title, date, time):
        self.team_name = team_name
        self.post_title = post_title
        self.date = date
        self.time = time
        self.engagement = None

# Function to collect reach and engagement metrics
def collect_metrics():
    views = int(input("Enter the number of views: "))
    likes = int(input("Enter the number of likes: "))
    shares = int(input("Enter the number of shares: "))
    comments = int(input("Enter the number of comments: "))
    saves = int(input("Enter the number of saves: "))
    reactions = int(input("Enter the number of reactions: "))

    return Engagement(views, likes, shares, comments, saves, reactions)

# Function to create a team with fixed values
def create_team(team_number):
    team_name = f"Team {team_number}"
    post_title = "Metrics: February 2024"
    date = "2024-01-09"
    time = "10:00"

    return Team(team_name, post_title, date, time)

# Function to plot engagement metrics on a radar chart
def plot_radar(teams):
    metrics = Engagement("views", "likes", "shares", "comments", "saves", "reactions").get_metrics_as_list()
    angles = np.linspace(0, 2 * np.pi, len(metrics), endpoint=False)

    fig, ax = plt.subplots(figsize=(8, 8), subplot_kw=dict(polar=True))
    fig.suptitle("Engagement Metrics by Team (Radar Chart)", fontsize=16)

    for i, team in enumerate(teams):
        values = team.engagement.get_metrics_as_list()
        ax.plot(angles, values, label=team.team_name, linewidth=2, linestyle='solid', marker='o')
        ax.fill(angles, values, alpha=0.25)

    ax.set_thetagrids(np.degrees(angles), metrics)
    ax.set_theta_offset(np.pi / 2)
    ax.set_theta_direction(-1)
    ax.legend()

    plt.show()

# Creating a list of teams
teams = []
num_teams = 6

for i in range(num_teams):
    print(f"\n----- Filling information for Team {i+1} -----")
    team = create_team(i+1)
    engagement = collect_metrics()
    team.engagement = engagement
    teams.append(team)

# Plotting engagement metrics on a radar chart
plot_radar(teams)
