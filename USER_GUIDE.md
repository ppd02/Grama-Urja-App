# Grama-Urja User Guide

## What Is Grama-Urja?

Grama-Urja is a rural electricity status monitoring app made for farmers in Karnataka. It helps users check and report whether power is currently ON or OFF in their village, transformer zone, or feeder area.

The app is designed for simple community use. Farmers can select their zone, report the latest power status, view alerts, use a pump timer, and read irrigation tips for Karnataka crops.

## Why Is It Used?

In many rural areas, electricity supply can be irregular. Farmers often need to know whether power is available before starting irrigation pumps. Grama-Urja helps reduce confusion by allowing people in the same area to share power status quickly.

Instead of calling multiple people or going to check the pump unnecessarily, farmers can open the app and see the latest reported status for their zone.

## Main Benefits

- Helps farmers know whether power is ON or OFF.
- Saves time before going to the pump set.
- Helps plan irrigation better.
- Reduces unnecessary phone calls between farmers.
- Supports English and Kannada.
- Uses Karnataka-based regions and crop choices.
- Does not require login, phone number, or email.
- Works simply for local reporting on the phone.
- Can support wider community sync when Firebase is configured.

## Possible Disadvantages

- The status depends on user reports, so wrong reports can mislead others.
- If nobody reports recently, the status may not be accurate.
- Live sharing between different phones needs internet and Firebase setup.
- The pump timer is an estimate, not an exact agricultural calculation.
- Crop tips are general guidance and not a replacement for expert advice.
- Notifications inside the app depend on reports being made.

## Why Login Is Not Included

Login is not included because the app is meant to be simple and farmer-friendly. Users do not need to enter phone numbers, email addresses, or passwords.

Instead, the Profile screen only asks for a name. That name is used as the reporter name when the user reports POWER ON or POWER OFF. The user can change the name anytime.

This keeps the app easier to use and avoids account setup problems.

## Why API/Gemini Is Not Included

The Gemini/API feature is not included because it requires an API key, internet access, billing or setup steps, and extra configuration.

For this version, crop tips are fixed and manual. This makes the app easier to run, easier to test, and less likely to fail during demonstration.

## Why Firebase Is Still Included

Firebase is included for future community syncing. When Firebase is properly configured, reports can be shared through the database so other devices can see the updated status.

The app also keeps local reporting simple, so the main experience can still be tested on one phone or emulator.

## Why Kannada And English Are Included

The app is meant for Karnataka farmers, so Kannada support is important. English is also included for wider understanding, project review, and demonstration.

Users can switch language from the app settings.

## Main Screens

### Splash Screen

Checks the app setup and opens the correct starting screen.

### Onboarding Screen

Used for first-time setup. The user selects language and chooses village or transformer zones to follow.

### Home Screen

Shows the current power status for the selected zone. The user can report POWER ON or POWER OFF from this screen.

### Power Report Screen

Provides a larger, focused screen for reporting power status.

### Alerts Screen

Shows recent power reports from followed zones.

### Pump Timer Screen

Helps estimate irrigation time based on crop, field size, and water intensity.

### Crop Tips Screen

Shows fixed irrigation suggestions and water-saving tips for supported Karnataka crops.

### Profile Screen

Lets the user enter or change their reporter name. No account details are required.

### Settings Screen

Allows changing language, theme, followed zones, and notification preferences.

## How To Use The App

1. Open Grama-Urja.
2. Choose English or Kannada.
3. Select your village, feeder, or transformer zone.
4. Go to the Home screen.
5. Check the selected zone name.
6. Tap POWER ON if electricity is available.
7. Tap POWER OFF if electricity is not available.
8. Open Alerts to view recent reports.
9. Use Pump Timer to estimate irrigation duration.
10. Use Crop Tips for general crop-based irrigation guidance.
11. Go to Profile to change your reporter name anytime.
12. Go to Settings to change language, theme, and followed zones.

## How To Report Correctly

Before reporting, make sure the selected zone is correct. Report POWER ON only when supply is actually available in that zone. Report POWER OFF when supply is not available or is not usable.

Good ways to confirm status include checking the pump starter, checking nearby supply, or asking another farmer in the same transformer zone.

## Supported Karnataka Zones

The app includes Karnataka-focused rural zones such as Hubli, Mandya, Mysuru, Belagavi, Raichur, Hassan, Shivamogga, and Kalaburagi regions.

These zones are used as sample transformer or feeder areas for the project.

## Supported Crops

The app focuses on crops commonly relevant to Karnataka farming, such as paddy, sugarcane, vegetables, and groundnut.

These crops are used in the pump timer and crop tips sections.

## Summary

Grama-Urja is a simple community power-status app for farmers. It helps users report power availability, view recent alerts, plan irrigation, and get basic crop guidance without needing login or external AI APIs.
