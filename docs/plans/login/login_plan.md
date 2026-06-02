# Login & Register Feature Plan

**Version 1.0 — May 2026**

---

## 1. Feature Scope

Implement the 6 priority screens of the Login & Register flow:

| # | Screen | Purpose |
|---|--------|---------|
| 1 | **Login Page** | Email + password authentication |
| 2 | **Verify Phone Number** | Phone number entry + send OTP (mocked) |
| 3 | **SMS Verification** | 6-digit OTP code input (mocked) |
| 4 | **Profile Detail Form** | Personal data: name, DOB, address, city, postal code, phone |
| 5 | **Create Password** | Password creation with validation rules |
| 6 | **Done Page** | Registration success confirmation → navigate to Home |

---

## 2. Navigation Flow

```
From Onboarding:
  "Log In"          → LoginScreen → (success) → Home
  "Sign up for free" → VerifyPhoneScreen → SmsVerificationScreen
                        → ProfileDetailScreen → CreatePasswordScreen
                        → DoneScreen → Home

From Login:
  "Register" link   → VerifyPhoneScreen (same register flow)
```

### New NavKey routes needed

| Route | NavKey object |
|-------|---------------|
| `VerifyPhoneRoute` | Registration step 1 |
| `SmsVerificationRoute` | Registration step 2 |
| `ProfileDetailRoute` | Registration step 3 |
| `CreatePasswordRoute` | Registration step 4 |
| `DoneRoute` | Registration step 5 |

---

## 3. Reusable Components

### 3.1 `LendlyTopAppBar`
- **Location**: `presentation/components/LendlyTopAppBar.kt`
- Back arrow (24×24, fill `#171D1E`) + optional info icon
- Used in: Screens 2–5 (all registration steps)
- Parameters: `onBackClick: () -> Unit`, `showInfoIcon: Boolean = true`

### 3.2 `LendlyTextField`
- **Location**: `presentation/components/LendlyTextField.kt`
- Label (14sp, fw=500, `FormLabel`) above a rounded input field (56px height)
- Green border on focus, gray `#6A6C6A` placeholder
- Optional trailing icon (e.g., visibility toggle)
- Parameters: `label: String`, `value: String`, `onValueChange`, `placeholder`, `trailingIcon`, `isPassword: Boolean`

### 3.3 `LendlyPhoneInput`
- **Location**: `presentation/components/LendlyPhoneInput.kt`
- Split field: country code (80dp) + phone number (remaining width)
- Used in: Verify Phone Number, Profile Detail Form

### 3.4 `LendlyBottomBar`
- **Location**: `presentation/components/LendlyBottomBar.kt`
- Green pill button (full-width, 48dp height, `FigmaNeonGreen`, rounded 100dp)
- Home indicator bar below (134×5, centered)
- Parameters: `buttonText: String`, `onClick: () -> Unit`, `enabled: Boolean`

### 3.5 `OtpInputRow`
- **Location**: `presentation/components/OtpInputRow.kt`
- 6 equal-width boxes (53×56 each), centered single digit
- Auto-advance focus on digit entry

### 3.6 `LendlyLogo`
- Already exists in `presentation/auth/SplashScreen.kt` as `LendlyLogo3D`
- Will extract to `presentation/components/LendlyLogo.kt` for reuse in Login + Done pages

---

## 4. Screen Files & Architecture

### 4.1 File Structure

```
presentation/
├── components/
│   ├── LendlyTopAppBar.kt
│   ├── LendlyTextField.kt
│   ├── LendlyPhoneInput.kt
│   ├── LendlyBottomBar.kt
│   ├── OtpInputRow.kt
│   └── LendlyLogo.kt
├── auth/
│   ├── LoginScreen.kt
│   ├── LoginViewModel.kt
│   ├── SplashScreen.kt          (existing)
│   └── SplashViewModel.kt       (existing)
├── register/
│   ├── VerifyPhoneScreen.kt
│   ├── SmsVerificationScreen.kt
│   ├── ProfileDetailScreen.kt
│   ├── CreatePasswordScreen.kt
│   ├── DoneScreen.kt
│   └── RegisterViewModel.kt     (shared across register steps)
├── navigation/
│   └── AppNavigation.kt         (modify — add new routes)
└── onboarding/                   (existing)
```

### 4.2 ViewModel Architecture

#### `LoginViewModel`
```kotlin
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
```
- Validates email format + non-empty password
- Calls `POST /auth/login` (mocked: any valid input → success)
- Saves token via `UserPreferences.saveAuthToken()`

#### `RegisterViewModel`
```kotlin
data class RegisterState(
    val phone: String = "",
    val countryCode: String = "+65",
    val otpCode: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dobDay: String = "",
    val dobMonth: String = "",
    val dobYear: String = "",
    val address: String = "",
    val city: String = "",
    val postalCode: String = "",
    val password: String = "",
    val currentStep: RegisterStep = RegisterStep.VERIFY_PHONE,
)

enum class RegisterStep {
    VERIFY_PHONE, SMS_VERIFICATION, PROFILE_DETAIL, CREATE_PASSWORD, DONE
}
```
- Single ViewModel shared across all 5 registration steps
- Each screen reads/writes the relevant fields
- `completeRegistration()` calls `POST /auth/create` (mocked) + saves token

---

## 5. State Handling

### Login
| State | UI Behavior |
|-------|-------------|
| Idle | Form shown, button enabled |
| Loading | Button shows loading indicator, fields disabled |
| Success | Navigate to Home |
| Error | Show error message (Snackbar or inline) |

### Register (per-step validation)
| Step | Validation |
|------|-----------|
| Verify Phone | Phone number non-empty, valid format |
| SMS | All 6 digits filled |
| Profile Detail | First name, last name non-empty; DOB valid |
| Create Password | ≥ 9 chars, contains letter + number |
| Done | No validation — just "Done" button → Home |

---

## 6. Assets Required

| Asset | imageRef | Source |
|-------|----------|--------|
| `check_mark.png` | `d0ee99f6e871...` | Download from Figma API → `assets/` |

The shield decoration image on Login Page is mostly off-screen/decorative and can be omitted without visual impact.

---

## 7. Color Tokens to Add

| Token | Hex | Usage |
|-------|-----|-------|
| `DoneHeadlineGreen` | `#BDF0B3` | "All done!" text |
| `CloseButtonBg` | `#0B390F` | Close button background on Done page |
| `OtpLinkTeal` | `#005046` | "Didn't received a code?" link |

---

## 8. Implementation Phases

### Phase 1: Foundation (Components + Navigation)
1. Extract `LendlyLogo` from SplashScreen
2. Create `LendlyTopAppBar`
3. Create `LendlyTextField`
4. Create `LendlyBottomBar`
5. Add new NavKey routes to `NavigationKeys.kt`
6. Add new color tokens to `Color.kt`

### Phase 2: Login Screen
1. Create `LoginScreen.kt` + `LoginViewModel.kt`
2. Wire into `AppNavigation.kt`
3. Implement email + password form with validation
4. Mock API call → navigate to Home on success

### Phase 3: Register Flow — Screens 1–3
1. Create `RegisterViewModel.kt` (shared state)
2. Create `LendlyPhoneInput` component
3. Create `VerifyPhoneScreen.kt`
4. Create `OtpInputRow` component
5. Create `SmsVerificationScreen.kt`

### Phase 4: Register Flow — Screens 4–6
1. Create `ProfileDetailScreen.kt` (scrollable form)
2. Create `CreatePasswordScreen.kt`
3. Download `check_mark.png` asset
4. Create `DoneScreen.kt`

### Phase 5: Integration & Polish
1. Wire all register screens into `AppNavigation.kt`
2. Connect Onboarding → Login / Register flows
3. Test back-stack clearing
4. Verify visual fidelity against Figma

---

## 9. Risks & Ambiguities

| Risk | Mitigation |
|------|-----------|
| Login page Figma shows phone-based login, spec says email | Use email+password as per spec API, follow Figma layout structure |
| SMS/OTP is out of scope for real implementation | Mock with hardcoded success after delay |
| `check_mark.png` needs Figma API token to download | Can generate a similar asset or request token from user |
| Register flow has 5 screens sharing state | Single `RegisterViewModel` with `@HiltViewModel` avoids state loss |
| Profile form is long — may need scrolling | Use `verticalScroll` modifier |

---

## 10. Validation Checklist

- [ ] Login: email validation, password validation, error states
- [ ] Login: successful auth → Home with back-stack cleared
- [ ] Verify Phone: phone input with country code
- [ ] SMS: 6-digit OTP boxes with auto-advance
- [ ] Profile: all fields render, scrollable, DOB split into 3 columns
- [ ] Create Password: visibility toggle, validation hint
- [ ] Done: dark theme, checkmark image, "Done" → Home
- [ ] All screens: correct typography (sizes, weights, colors)
- [ ] All screens: correct spacing and padding (16dp horizontal)
- [ ] All screens: green pill button at bottom
- [ ] Back navigation works correctly between register steps
- [ ] Back-stack cleared when completing auth flows

---

## 11. Pending Questions

None — all open questions resolved with user confirmation.
