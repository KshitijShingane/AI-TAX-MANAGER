Android App (Kotlin + Jetpack Compose)

This module contains the Deducto Android app skeleton. Open `android-app` in Android Studio to work on the project.

Key areas to implement:
- Receipt capture & OCR (ML Kit)
- Firebase Auth & Firestore integration
- Expense categorization and summaries
- India tax estimator
- Export PDF/CSV

Run locally
1. Open the repo and checkout `scaffold/android-skeleton` or the latest feature branch.
2. Open `android-app` in Android Studio
3. Add your Firebase `google-services.json` to `android-app/app/` — **do not commit**. A template is provided as `google-services.json.template`.
4. Run `./gradlew assembleDebug` to build and `./gradlew test` to run unit tests.

Firebase setup (dev)
- Project ID: `ai-tax-manager-b1e34` (we will use this project for Auth/Firestore/Storage)
- To enable local testing, create an Android app in your Firebase project with package name `com.deducto.app` and download `google-services.json`.
- The app currently implements a simple Email sign-in flow (auto-creates accounts on sign-in failure) for development convenience.
