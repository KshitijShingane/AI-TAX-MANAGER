 # Product Requirements Document (PRD)

## 1. Product Overview

**Product Name (Working):** **Deducto** (formerly LedgerAI)

**Branding:** Uses the provided logo assets: `assets/logo_light.png` and `assets/logo_dark.png` (see design folder).

**Product Vision:**
Build the simplest, smartest expense and tax-tracking app for freelancers and solo entrepreneurs. The app should feel like a calm, competent financial assistant that quietly handles the boring money work so users can focus on earning.

**One‑line Value Proposition:**
> “Track expenses, estimate taxes, and stay compliant — without spreadsheets or stress.”

---

## 2. Target Market (Primary Focus)

**Primary Users (Paying Customers):**
- Freelancers (designers, developers, writers)
- Solopreneurs
- Digital nomads
- Content creators (YouTubers, coaches, influencers)
- Small service business owners

**Key Characteristics:**
- Self‑employed or independent income
- Personally responsible for taxes
- Time‑poor, admin‑averse
- Willing to pay to reduce mistakes and mental load

**Out of Scope (Initial):**
- Large businesses
- Corporations with payroll & complex accounting
- Enterprise finance teams

---

## 3. Problem Statement

Freelancers struggle with:
- Manually tracking expenses across months
- Knowing which expenses are tax‑deductible
- Estimating taxes throughout the year
- Organizing receipts for accountants

Existing solutions fail because they are:
- Built for corporations, not individuals
- Overloaded with accounting jargon
- Expensive and bloated
- Poorly optimized for mobile‑first workflows

---

## 4. Goals & Success Metrics

### Business Goals
- Reach $10,000+ MRR within 6–9 months
- Achieve strong retention through recurring utility
- Build a niche‑first, expandable product

### Product Goals
- Reduce expense tracking time by 80%
- Eliminate uncertainty around tax estimates
- Become the default “financial memory” for freelancers

### Success Metrics (KPIs)
- Free → Paid conversion rate ≥ 5–8%
- Monthly churn ≤ 4%
- Avg. receipts scanned per user per month ≥ 15
- Tax estimate feature usage ≥ 60% of active users

---

## 5. Core MVP Features (Launch Fast)

### 5.1 Receipt Scanning & OCR
- Camera-based receipt capture
- OCR extraction of:
  - Merchant name
  - Date
  - Amount
  - Currency
- Manual edit option

**Acceptance Criteria:**
- Receipt processed in < 5 seconds
- ≥ 90% accuracy for common receipts

---

### 5.2 AI Expense Categorization
- Auto‑categorize expenses into:
  - Travel
  - Food
  - Software
  - Office
  - Marketing
  - Utilities
- Learns from user corrections

---

### 5.3 Expense Summaries
- Monthly summary
- Year‑to‑date summary
- Category breakdown charts

---

### 5.4 Simple Tax Estimation
- Country‑based tax rules (Phase 1: single country)
- Estimated tax owed (monthly & yearly)
- Conservative estimate with clear disclaimers

---

### 5.5 Export Reports
- PDF export (accountant‑friendly)
- CSV export (Excel/Sheets)
- Date range selection

---

## 6. Advanced & Scalable Features

### 6.1 Bank Account Sync
- Connect bank accounts via third‑party APIs
- Auto‑import transactions
- Match transactions with receipts

---

### 6.2 Subscription Detection
- Identify recurring charges
- Monthly subscription summary
- Alerts for unused or duplicate subscriptions

---

### 6.3 Tax Reminders
- Quarterly tax reminders
- Push notifications & email
- Country‑specific deadlines

---

### 6.4 Multi‑Currency Support
- Automatic FX conversion
- Base currency setting
- Ideal for digital nomads

---

### 6.5 Accountant Access
- Read‑only shared access
- Secure link or email invite
- Reduces back‑and‑forth during tax season

---

### 6.6 AI Finance Chat
- “Ask your finances” natural language chat
- Examples:
  - “How much can I spend this month?”
  - “What were my biggest deductions?”
  - “How much tax should I save?”

---

## 7. User Experience Principles

- Mobile‑first, thumb‑friendly design
- Minimal accounting terminology
- Clear confidence indicators (estimates vs exact)
- Calm, non‑judgmental tone
- Fast actions (scan → done)

---

## 8. Monetization Strategy

### Pricing Model
- Subscription‑only
- No ads

**Plans:**
- $15/month
- $120/year (20% discount)

### Revenue Targets
- 700 users × $15 = $10,500/month
- 1,000 users × $10 (discounts & promos) = $10,000/month

---

## 9. Growth Strategy (Android‑First)

- SEO landing pages:
  - “Best expense tracker for freelancers”
- Google Play ASO (low‑competition keywords)
- Reddit & Indie Hacker communities
- Partnerships with accountants & coaches
- Free trial → paid conversion funnel

---

## 10. Technical Architecture (Initial)

### Frontend
- Android (Kotlin + Jetpack Compose)

### Backend
- Firebase (Auth, Firestore, Storage) — **Project ID: `ai-tax-manager-b1e34`**
- Cloud Functions for OCR & AI processing

### Integrations
- OCR API
- Banking API (future)
- PDF generation service

---

## 11. Risks & Mitigations

| Risk | Mitigation |
|----|----|
| Tax accuracy liability | Clear disclaimers, conservative estimates |
| OCR inaccuracies | Manual edit + learning loop |
| User churn | High perceived daily value |
| Feature creep | Strict MVP scope |

---

## 12. Niche Expansion Strategy

Start with one focused niche:
- Freelancers in one country

Then expand to:
- Digital nomads
- Creators
- Region‑specific tax versions

---

## 13. Launch Milestones

1. MVP build (6–8 weeks)
2. Private beta (50–100 users)
3. Public Play Store launch
4. Paid conversion experiments
5. Niche expansion

---

## 14. Long‑Term Vision

Become the default financial OS for independent workers — not an accounting tool, but a daily companion that quietly keeps money, taxes, and compliance under control.

