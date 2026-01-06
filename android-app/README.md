Android App (Kotlin + Jetpack Compose)

This module contains the Deducto Android app skeleton. Open `android-app` in Android Studio to work on the project.

Key areas to implement:
- Receipt capture & OCR (ML Kit) — CameraX-based scanner and on-device text recognition
- Firebase Auth & Firestore integration
- Expense categorization and summaries
- India tax estimator
- Export PDF/CSV

OCR & Scanner
- The app includes a camera-based scanner using CameraX and ML Kit on-device text recognition.
- After capturing a receipt, parsed fields (merchant, date, amount) are shown in a review screen and can be edited before saving to Firestore.

Dev dataset (opt-in)
- Deducto can optionally collect anonymized parsed receipts from consenting users to improve parsing ML models.
- Data collected is anonymized: merchant names are hashed (SHA-256), amounts are rounded & bucketed, dates are stored as year-month only, and no raw text or images are sent.
- Opt-in is requested after the first save and can be toggled in-app (settings). Data is uploaded to Firestore collection `dev_receipts` in project `ai-tax-manager-b1e34`.
- If you enable this during development, ensure your Firebase rules for `dev_receipts` permit writes from authenticated users only.


Run locally
1. Open the repo and checkout `scaffold/android-skeleton` or the latest feature branch.
2. Open `android-app` in Android Studio
3. Add your Firebase `google-services.json` to `android-app/app/` — **do not commit**. A template is provided as `google-services.json.template`.
4. Run `./gradlew assembleDebug` to build and `./gradlew test` to run unit tests.

Firebase setup (dev)
- Project ID: `ai-tax-manager-b1e34` (we will use this project for Auth/Firestore/Storage)
- To enable local testing, create an Android app in your Firebase project with package name `com.deducto.app` and download `google-services.json`.
- The app currently implements a simple Email sign-in flow (auto-creates accounts on sign-in failure) for development convenience.
