# Smart Utility Toolkit

A beautifully designed Android utility app built with Jetpack Compose, featuring a task manager, unit/currency converter, and BMI calculator with an intuitive onboarding experience.

![App Screenshot](screenshots/app_banner.png)

## ✨ Features

### 📋 Tasks Manager
- Create, complete, and delete tasks with ease
- Long-press to enter delete mode for batch operations
- Completed tasks automatically move to the bottom with strikethrough
- Persistent storage using Room Database
- Empty state with helpful guidance

### 🔄 Smart Converter
- Convert between multiple units (kg, lb, g, oz)
- Real-time currency conversion (USD, EUR, GBP, JPY, CAD, AUD)
- Instant conversion as you type
- Quick swap button to reverse conversion direction
- Clean, intuitive interface

### 💪 BMI Calculator
- Calculate Body Mass Index with age and gender consideration
- Visual category indicator (Underweight, Normal, Overweight, Obese)
- Suggested weight range based on your height
- Personalized health tips for each BMI category
- Form validation with helpful error messages

### 🎓 Interactive Tutorials
- First-time user onboarding for each screen
- Step-by-step guidance highlighting key features
- Skip option for experienced users
- Remembers tutorial completion status

### 🌙 Dark & Light Themes
- Beautiful maple leaf-inspired color scheme
- Red (primary), Gold (secondary), and Green (tertiary)
- Full dark mode support
- Smooth theme transitions

## 🛠️ Tech Stack

- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with ViewModel
- **Navigation**: Jetpack Navigation Compose
- **Database**: Room Database
- **Dependency Injection**: Dagger Hilt
- **State Management**: Kotlin StateFlow
- **Preferences**: DataStore Preferences
- **Language**: Kotlin


## Getting Started
### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17
- Android SDK 34

### Installation

1. Clone the repository
```bash
git clone https://github.com/0L47UNB05UN/smart-utility-toolkit.git
```
Open the project in Android Studio
Build and run
