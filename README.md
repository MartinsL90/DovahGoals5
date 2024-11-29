API 26 ("Oreo; Android 8.0)

RPG Leveling and Achievement System
Overview
This project is an Android RPG-themed leveling and achievement system designed for my college project. It features a dynamic leveling system, an achievement system tied to player levels, and an tavern menu for tips on dealing with adhd and task management. The project aims to provide a user-friendly and minimalist interface that enhances player engagement through rewards and progression tracking.
____________________________________________________________________________________________________
Features:
- Leveling System
- Players earn XP by completing tasks and progress through various tasks.
- XP Progress Bar dynamically updates and tracks progress.
- Display of current level.
- Achievements menu.
- Predefined achievements unlocked at levels 1, 5, 10, 25, 50, 80, 100 and a platinum trophy.
- Each achievement has a custom name, description, and art.
- Achievements are dynamically displayed when the player meets the level requirement.
- Checkbox to indicate completion of the achievement.
- Tavern Menu with my guy Yarl, the tavern keeper.
- Access to tasks, levels, and achievements.
- Refreshes to check player level and update achievements when opened.

____________________________________________________________________________________________________
Project Structure

📂 project_root
├── 📂 app
│   └── 📂 src
│       └── 📂 main
│           ├── 📂 java
│           │   └── com.example.dovahgoals5  # Core project code
│           ├── 📂 res
│           │   ├── 📂 drawable  # Achievement images
│           │   ├── 📂 layout    # XML layouts for UI
│           │   └── 📂 values    # Colors, strings, and themes
│           └── AndroidManifest.xml
└── README.md

____________________________________________________________________________________________________
Setup and Installation
Prerequisites
Android Studio (Recommended)
Android SDK
Steps
Clone the repository:

git clone https://github.com/username/rpg-achievement-system.git
cd rpg-achievement-system

Open the project in Android Studio.
Sync the Gradle files.
Run the app on an emulator or a physical device.

____________________________________________________________________________________________________
Usage Instructions
Accessing the Tavern Menu:
Tap the Tavern button to open the menu.
The tavern menu displays some tips on how to deal with the tasks ahead.

Viewing Achievements:
Open the Achievements Menu.
Achievements are dynamically unlocked based on the player's level.

Completed achievements are marked with a checkbox.

____________________________________________________________________________________________________
Customization

Adding New Achievements:
Add new images to the res/drawable folder.
Update the achievement logic in your Java  files.
Adjust the activity_achievement.xml layout for new items.

Modifying Progress Bar:
The progress bar can be adjusted in the activity_home.xml by changing the android:max and android:progress attributes.

Contact
For any questions, feel free to reach out at:

Author: Lucas Martins
Email: lucasmmorais29@gmail.com
LinkedIn: [LinkedIn](https://www.linkedin.com/in/lucas-martins-b8a1371a5/)
