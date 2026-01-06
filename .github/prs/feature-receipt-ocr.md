# PR: feat(ocr): CameraX scanner + ML Kit text recognition + parsing + manual edit

## Summary
This PR implements the first iteration of receipt capture, on-device OCR (ML Kit), parsing heuristics, a manual review/edit screen, and saving receipts to Firestore.

Branches:
- Feature branch: `feature/receipt-ocr`

## What I executed (commit summary)
- Added CameraX dependencies and ML Kit text-recognition dependency.
- Added CAMERA permission to the Android manifest.
- Implemented `ReceiptScanScreen` (CameraX preview, capture, ML Kit processing).
- Implemented `ReceiptParser` (merchant/date/amount heuristic extraction).
- Implemented `ManualEditScreen` for review/edit and saving to Firestore.
- Implemented `ReceiptRepository` for Firestore writes (coroutines-based).
- Added `Receipt` data model and basic `PermissionUtils`.
- Updated `MainActivity` Compose flow to include sign-in → scan → review → save.
- Added `google-services.json.template` and updated `.gitignore` to ignore the real `google-services.json`.

## Files changed / added (high-level)
- android-app/app/src/main/java/com/deducto/app/receipts/* (new)
  - Receipt.kt, ReceiptParser.kt, ReceiptRepository.kt
  - ReceiptScanScreen.kt, ManualEditScreen.kt
- android-app/app/src/main/java/com/deducto/app/firebase/FirebaseModule.kt
- android-app/app/src/main/AndroidManifest.xml (camera permission)
- android-app/app/google-services.json.template
- build.gradle updates (dependencies for CameraX / ML Kit)

## How to test locally
1. Add Firebase `google-services.json` for project `ai-tax-manager-b1e34` to `android-app/app/` (do not commit).
2. Open `android-app` in Android Studio and run on a device/emulator with camera access.
3. Sign in (email/password — dev flow auto-creates accounts if needed).
4. Grant camera permission when prompted and capture a receipt.
5. After capture, the app runs ML Kit and shows parsed fields. Edit if required and press Save.
6. Check Firestore `receipts` collection to confirm saved document.

## Checklist
- [x] Add CameraX and ML Kit dependencies
- [x] Add CAMERA permission
- [x] Implement camera preview and capture
- [x] Run on-device text recognition (ML Kit)
- [x] Implement parsing heuristics (merchant/date/amount)
- [x] Add manual edit + save flow
- [x] Save receipts to Firestore
- [ ] Add unit tests for parsing heuristics
- [ ] Add UI tests for capture + review flow (emulator)
- [ ] Improve permission UX (Compose-based permission request)
- [ ] Add analytics / error logging (Crashlytics)
- [ ] Add cloud OCR fallback (Cloud Function + Vision API) for low-accuracy receipts

## Notes & considerations
- Parsing heuristics are intentionally conservative (largest numeric value considered total).
- For better results, we'll collect data samples and improve parsing rules or train a small model.
- Firestore writes assume Firestore is enabled in the Firebase project and that security rules permit writes from authenticated users.

## Next steps (recommended)
1. Add unit tests and UI tests for the scanner and parser.
2. Introduce tolerance and more robust parsing (currency detection, multi-line totals, decimal parsing with commas).
3. Implement cloud OCR fallback via Cloud Functions for receipts that fail on-device recognition.
4. Implement analytics and connection to a staging Firebase project for testing.

---

_Assignee: @KshitijShingane_
_Please review and merge when ready._
