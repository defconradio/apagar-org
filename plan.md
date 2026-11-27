# Implementation Plan: `apagar.org` (KMP + Wasm)

## 1. Architecture Overview
This project leverages the existing **Clean Architecture + MVVM** structure in `defconradio/apagar-org`:
* **`:shared` (`commonMain`):** Contains 100% of the domain models, quiz question bank, calculation protocol, and ViewModel state management. No external backend is required for v1 (client-side execution in Wasm).
* **`:composeApp` (`wasmJsMain`):** Renders the responsive Compose Multiplatform web UI, handles browser URL/routing, and bundles the Wasm distribution.

---

## 2. Module & Package Structure (`shared`)

Target package: `com.example.shared` (or refactor to `org.apagar.shared`).

```text
shared/src/commonMain/kotlin/com/example/shared/
├── di/
│   └── AppModule.kt                 # Koin bindings for UseCases & ViewModels
├── domain/
│   ├── model/
│   │   ├── CountryDebt.kt           # Enum/Data class (VE, AR, US, ES)
│   │   ├── QuizAnswer.kt            # User inputs (birthYear, children, sector, stealthAnswer)
│   │   ├── StealthQuestion.kt       # Question + Options + IdeologicalWeight
│   │   └── DebtInvoice.kt           # Final calculated total + itemized list of surcharges/discounts
│   ├── repository/
│   │   └── QuestionBank.kt          # Static localized Spanish question & country data
│   └── usecase/
│       ├── GetRandomStealthQuestionUseCase.kt
│       └── CalculateDebtInvoiceUseCase.kt
└── presentation/
    ├── QuizState.kt                 # Sealed interface (Intro, InProgress, Calculating, Result)
    └── DebtQuizViewModel.kt         # StateFlow management & user event handling
```

---

## 3. Step-by-Step Implementation Roadmap

### Phase 1: Domain Layer & Calculation Engine (`:shared`)
1. **Define Models (`domain/model/`):**
   * Create `CountryDebt(val code: String, val nameEs: String, val baseAmountUsd: Double)`.
   * Create `InvoiceLineItem(val descriptionEs: String, val amountUsd: Double, val percentageLabel: String)`.
   * Create `DebtInvoice(val country: CountryDebt, val items: List<InvoiceLineItem>, val totalUsd: Double)`.
2. **Implement Question Bank (`domain/repository/QuestionBank.kt`):**
   * Hardcode the Spanish UI strings and the 3+ stealth ideological questions to avoid network latency and ensure offline/Wasm reliability.
3. **Implement Calculation Protocol (`CalculateDebtInvoiceUseCase.kt`):**
   * Pure Kotlin function taking `QuizAnswer` and returning `DebtInvoice`.
   * Calculate age relative to `2026`.
   * Apply modifiers sequentially against `baseAmountUsd` and generate human-readable Spanish `InvoiceLineItem` entries for every non-zero modifier.

### Phase 2: Presentation & State Management (`:shared`)
1. **Setup `DebtQuizViewModel`:**
   * Expose `val uiState: StateFlow<QuizState>`.
   * On init, invoke `GetRandomStealthQuestionUseCase` so the user gets a randomized stealth question for that session.
   * Add a simulated 1.5-second delay (`kotlinx.coroutines.delay`) between quiz submission and `Result` state to display satirical loading messages (e.g., `"Consultando al Banco Central..."`, `"Calculando inflación acumulada..."`).
2. **Register in Koin (`di/AppModule.kt`):**
   * Register `CalculateDebtInvoiceUseCase`, `GetRandomStealthQuestionUseCase`, and `DebtQuizViewModel`.

### Phase 3: Compose Multiplatform Web UI (`:composeApp` / `:shared` UI)
1. **Theme & Typography:**
   * Design a "Government Receipt / Thermal Paper" aesthetic (monospaced fonts for numbers/invoices, off-white/paper background `#F9F8F4`, stark black borders, stamp-red accents `#D32F2F` for surcharges, and forest-green `#2E7D32` for discounts).
2. **Build Screens:**
   * `IntroScreen`: Big bold hook (`"¡Felicidades! Usted debe $8,300 USD."`) + Start button.
   * `QuizStepperScreen`: Progress bar (`Paso 1 de 5`), clean single-card question layout, radio buttons/chips for options.
   * `InvoiceReceiptScreen`: Thermal-receipt style card with dashed dividers, itemized breakdown, total amount, and a `"Copiar resumen para compartir"` button using Compose `LocalClipboardManager`.

### Phase 4: Wasm Build & Deployment Verification
1. **Local Development Run:**
   ```bash
   ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
   ```
2. **Unit Testing (`:shared:commonTest`):**
   * Write unit tests for `CalculateDebtInvoiceUseCase` verifying edge cases (e.g., young left-leaning crypto user with 0 kids vs. older right-leaning private sector parent).
3. **Production Wasm Bundle:**
   ```bash
   ./gradlew :composeApp:wasmJsBrowserDistribution
   python3 scripts/serve_wasm.py --prod
   ```
   * Verify Cross-Origin Isolation headers (`COOP`/`COEP`) work properly in target hosting (e.g., Cloudflare Pages, Vercel, or GitHub Pages with service worker headers).
