# Concept Document: "Aquí Vamos Todos" (`apagar.org`)

## 1. Core Concept & Satirical Premise
**apagar.org** ("Aquí Vamos Todos a Pagar") is an interactive, satirical web application built on Kotlin Multiplatform (Wasm). It turns sovereign public debt figures into a personalized, gamified "invoice" for individual citizens. 

Using real-world macroeconomic data as a baseline—such as Venezuela's estimated $8,300 USD per capita debt projection (referenced from the *Plan Venezuela Renace* framework)—the app guides users through a quick, seemingly bureaucratic quiz to calculate their "fair share" of the national debt.

* **Tone:** Darkly humorous, bureaucratic, ironic, and shareable.
* **User Journey:** Landing Page -> 5-Step Satirical Quiz -> "Processing Debt" Loading Animation -> Personalized Itemized Invoice (Shareable Receipt).

---

## 2. User Flow & Spanish UI Copy

### Screen 1: Landing Page
* **Headline:** `"¡Felicidades! Usted debe $8,300 USD."`
* **Subheadline:** `"La deuda externa no se paga sola. Calcule su cuota patriótica en menos de 60 segundos y descubra cuánto le toca aportar para sacar al país adelante."`
* **CTA Button:** `"Calcular mi factura"`

### Screen 2: The Quiz (5 Steps)
1. **Country Selection (`País de residencia / origen`):**
   * *Options:* Venezuela (Base: $8,300), Argentina (Base: $9,100), EE.UU. (Base: $104,000), España (Base: $34,000). *(Defaults to Venezuela).*
2. **Age / Birth Year (`Año de nacimiento`):**
   * *Logic:* Determines "remaining productive years" to pay off the debt.
3. **Dependents (`¿Cuántos hijos tiene?`):**
   * *Options:* `0`, `1-2`, `3+`.
   * *Satirical Rationale:* Having children means creating "future taxpayers," earning a discount. Having `0` incurs a *"Tasa de Egoísmo Demográfico"* surcharge.
4. **Economic Sector (`¿En qué área se desempeña?`):**
   * *Options:* 
     * `"Sector Productivo / Privado / Tecnología"` (Discount: already carrying the economy)
     * `"Sector Público / Burocracia"` (Surcharge: *"Recargo por Nómina Estatal"*)
     * `"Estudiante / Sin empleo fijo"` (Neutral / Deferred interest)
     * `"Creador de contenido / Crypto / Trading"` (High Surcharge: *"Impuesto al Humo Digital"*)
5. **The "Stealth" Ideological Question (Randomized Pool):**
   * Presented as a casual personality/lifestyle question to mask ideological profiling. One question is randomly selected per session.

---

## 3. Stealth Ideological Question Bank (Spanish UI)

The app selects **one** question at random from this list to apply a hidden political/economic multiplier without explicitly asking *"Are you left-wing or right-wing?"*:

* **Q1 (Book/Media):** `"¿Qué libro prefieres llevar a una isla desierta?"`
  * *A:* `"El Capital o las venas abiertas de América Latina"` -> **Left-leaning (+35% Surcharge)**
  * *B:* `"La Riqueza de las Naciones o Padre Rico, Padre Pobre"` -> **Right-leaning (-10% Discount)**
  * *C:* `"El horóscopo o un manual de supervivencia"` -> **Neutral (0%)**
* **Q2 (Economic Role):** `"Si te encuentras $100 en la calle, ¿qué es lo más justo?"`
  * *A:* `"Repartirlo entre todos los que pasaban por ahí"` -> **Left-leaning (+35% Surcharge)**
  * *B:* `"Invertirlo y quedarte con la ganancia por haberlo encontrado"` -> **Right-leaning (-10% Discount)**
  * *C:* `"Gastarlo en cerveza antes de que se devalúe"` -> **Pragmatic (+5% Surcharge)**
* **Q3 (Crisis Blame):** `"Cuando sube el precio del pan, ¿quién tiene la culpa?"`
  * *A:* `"Los empresarios especuladores y el bloqueo"` -> **Left-leaning (+40% Surcharge)**
  * *B:* `"El Banco Central imprimiendo billetes sin respaldo"` -> **Right-leaning (-10% Discount)**
  * *C:* `"El panadero que madruga poco"` -> **Neutral (0%)**

---

## 4. Calculation Protocol (`DebtCalculatorProtocol`)

The final debt ($D_{final}$) is calculated deterministically in the `:shared` domain layer using a base debt ($D_{base}$) and additive/multiplicative modifiers:

$$D_{final} = D_{base} \times (1 + M_{age} + M_{children} + M_{sector} + M_{ideology})$$

### Modifier Rules

| Parameter | Condition | Modifier | Invoice Line Item (Spanish) |
| :--- | :--- | :--- | :--- |
| **Base Debt** | Venezuela | `$8,300.00` | `"Cuota Base Plan Venezuela Renace"` |
| **Age** | Age < 25 | `+0.15` (+15%) | `"Recargo por Juventud (Tiene tiempo de sobra para pagar)"` |
| **Age** | Age 25 - 55 | `0.00` (0%) | `"Tasa Estándar de Fuerza Laboral"` |
| **Age** | Age > 55 | `-0.10` (-10%) | `"Descuento por Desgaste Histórico"` |
| **Children** | 0 children | `+0.20` (+20%) | `"Multa por Falta de Relevo Generacional (0 hijos)"` |
| **Children** | 1 - 2 children | `-0.05` (-5%) | `"Bono por Aporte de Futuros Contribuyentes"` |
| **Children** | 3+ children | `-0.15` (-15%) | `"Subsidio Heroico de Natalidad"` |
| **Sector** | Private / Tech | `-0.10` (-10%) | `"Exención por Sostener el PIB"` |
| **Sector** | Public / Gov | `+0.25` (+25%) | `"Recargo por Eficiencia Burocrática"` |
| **Sector** | Influencer / Crypto | `+0.30` (+30%) | `"Impuesto Especial a la Especulación Digital"` |
| **Ideology** | Left-leaning answer | `+0.35` (+35%) | `"Aporte Solidario Voluntario-Obligatorio (Cláusula Socialista)"` |
| **Ideology** | Right-leaning answer | `-0.10` (-10%) | `"Descuento por Amor al Libre Mercado"` |

---

## 5. Screen 3: The Invoice Output (Spanish UI)
* **Header:** `"FACTURA OFICIAL DE DEUDA SOBERANA #VE-2026-8300"`
* **Summary Box:** `"Total a Pagar: $12,865.00 USD"` *(Example output)*
* **Payment Options (Satirical):**
  * `"Pagar en 480 cómodas cuotas mensuales"`
  * `"Pagar con un riñón (sujeto a evaluación médica)"`
  * `"Emigrar y fingir demencia"`
* **Viral Action:** `"Descargar Recibo / Compartir en X"` (Generates a clean summary card).
