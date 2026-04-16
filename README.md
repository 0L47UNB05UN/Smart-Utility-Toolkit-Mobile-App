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

## 📸 Screenshots

| Tasks | Converter | BMI |
|-------|-----------|-----|
| ![Tasks](screenshots/tasks.png) | ![Converter](screenshots/converter.png) | ![BMI](screenshots/bmi.png) |

## 🛠️ Tech Stack

- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with ViewModel
- **Navigation**: Jetpack Navigation Compose
- **Database**: Room Database
- **Dependency Injection**: Dagger Hilt
- **State Management**: Kotlin StateFlow
- **Preferences**: DataStore Preferences
- **Language**: Kotlin

## 📁 Project Structure
com.example.smartutilitytoolkitmobileapp/
├── data/
│ ├── local/
│ │ ├── TaskDao.kt
│ │ ├── TaskDatabase.kt
│ │ └── TaskEntity.kt
│ ├── preferences/
│ │ └── PreferencesManager.kt
│ └── repository/
│ └── TaskRepository.kt
├── di/
│ └── DatabaseModule.kt
├── navigation/
│ ├── BottomNavItem.kt
│ ├── NavGraph.kt
│ └── NavigationRoutes.kt
├── ui/
│ ├── components/
│ │ └── TutorialOverlay.kt
│ ├── screens/
│ │ ├── BMICalculatorScreen.kt
│ │ ├── BMICalculatorViewModel.kt
│ │ ├── ConverterScreen.kt
│ │ ├── ConverterScreenViewModel.kt
│ │ ├── MainScreen.kt
│ │ ├── TasksScreen.kt
│ │ └── TasksViewModel.kt
│ └── theme/
│ ├── Color.kt
│ ├── Theme.kt
│ └── Type.kt
├── MainActivity.kt
└── SmartUtilityToolkitApplication.kt

text

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17
- Android SDK 34

### Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/smart-utility-toolkit.git
Open the project in Android Studio

Build and run

bash
./gradlew assembleDebug
📦 Dependencies
kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui:1.6.0")
implementation("androidx.compose.material3:material3:1.2.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")

// Hilt DI
implementation("com.google.dagger:hilt-android:2.51.1")
kapt("com.google.dagger:hilt-compiler:2.51.1")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
🎨 Theme Colors
Color	Light Mode	Dark Mode	Usage
Maple Red	#C62828	#EF5350	Primary actions, FAB
Maple Gold	#F9A825	#FFCA28	Secondary elements
Forest Green	#2E7D32	#66BB6A	Tertiary, success states
Underweight	#1976D2	#42A5F5	BMI Category
Normal	#2E7D32	#66BB6A	BMI Category
Overweight	#F9A825	#FFCA28	BMI Category
Obese	#C62828	#EF5350	BMI Category
🔧 Configuration
Enabling Hilt
The app uses Hilt for dependency injection. Ensure your Application class is annotated:

kotlin
@HiltAndroidApp
class SmartUtilityToolkitApplication : Application()
And your MainActivity:

kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity()
📝 License
This project is licensed under the MIT License - see the LICENSE file for details.

👤 Author
Your Name

GitHub: @yourusername

LinkedIn: Your Name

🙏 Acknowledgments
Material Design 3 for design guidelines

Jetpack Compose for modern UI toolkit

Android Open Source Project

Made with ❤️ using Kotlin and Jetpack Compose

text

## Additional Files

### .gitignore (Android specific)

```gitignore
*.iml
.gradle
/local.properties
/.idea/
.DS_Store
/build
/captures
.externalNativeBuild
.cxx
*.apk
*.ap_
*.dex
*.class
bin/
gen/
out/
.navigation/
captures/
output.json
LICENSE (MIT)
text
MIT License

Copyright (c) 2026 Your Name

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE
