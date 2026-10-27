# Rumble Racing Pocket — v0.3.0 Android starter

Dedicated Android UI for Rumble Racing PS2.

## Build an APK using only an Android phone
This project includes a GitHub Actions workflow at `.github/workflows/android-build.yml` so you do not need Android Studio or a local Android SDK.

1. Create a GitHub account if needed.
2. In the mobile browser, create a **new repository** and upload the contents of this ZIP (upload the files and folders, including the hidden `.github` folder if the interface allows it).
3. Open the repository's **Actions** tab and select **Build Rumble Racing Pocket APK**.
4. Tap **Run workflow** (or push to `main` to trigger the workflow).
5. When the workflow finishes successfully, open its run and download the `RumbleRacingPocket-debug-apk` artifact. GitHub may ask you to sign in.
6. Extract the downloaded artifact ZIP; it contains `app-debug.apk`. Install it on your Android phone, allowing installs from that browser/file manager if Android asks.

If uploading the `.github` folder from the mobile web UI is awkward, use GitHub's web editor to create `.github/workflows/android-build.yml` and paste in the workflow from this file's folder.

## Included
- Single-game themed launcher.
- Pick and remember ISO document URI.
- In-app list of built-in password cheats with copy buttons.
- Saved UI preferences for performance preset, internal-resolution target and FPS target.

## Important limits
This is source code, not a PS2 emulator. Password cheats are copied to the clipboard; enter them in-game through the game's password menu. Graphics/resolution/FPS preferences are saved by the launcher but do not yet change the emulator renderer or actual FPS. Direct ISO launch depends on the installed emulator accepting Android document intents. A built APK does not itself include an emulator, BIOS, or game.

Use your own legally obtained game ISO and PS2 BIOS.

