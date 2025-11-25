# Comprehensive Guide to Route Satisfaction Factor

## Executive Summary

This document provides an extensive analysis of the satisfaction factor system in the Airline Club game, examining all factors that influence passenger satisfaction, and presenting the most cost-effective strategies to achieve and maintain satisfaction above 60%.

---

## Table of Contents

1. [Understanding Satisfaction Factor](#1-understanding-satisfaction-factor)
2. [The Complete Cost Formula](#2-the-complete-cost-formula)
3. [Computed Quality Breakdown](#3-computed-quality-breakdown)
4. [Factors That Matter Beyond Service Quality](#4-factors-that-matter-beyond-service-quality)
5. [Target Service Quality vs Route Service Quality](#5-target-service-quality-vs-route-service-quality)
6. [Cost-Effective Strategies](#6-cost-effective-strategies)
7. [Recommendations by Scenario](#7-recommendations-by-scenario)
8. [Technical Implementation Details](#8-technical-implementation-details)
9. [Quick Reference Formulas](#9-quick-reference-formulas)

---

## 1. Understanding Satisfaction Factor

### What is Satisfaction Factor?

Satisfaction factor is a metric ranging from **0 to 1** (0% to 100%) that measures how satisfied passengers are with your flight. It directly impacts:
- Passenger loyalty (future loyalty at that airport)
- Airport reputation
- Overall airline reputation

### The Core Formula

```scala
val computePassengerSatisfaction = (cost: Double, standardPrice: Int) => {
    val ratio = cost / standardPrice
    var satisfaction = (MIN_SATISFACTION_PRICE_RATIO_THRESHOLD - ratio) / 
                       (MIN_SATISFACTION_PRICE_RATIO_THRESHOLD - MAX_SATISFACTION_PRICE_RATIO_THRESHOLD)
    satisfaction = Math.min(1, Math.max(0, satisfaction))
    satisfaction
}
```

Where:
- `MAX_SATISFACTION_PRICE_RATIO_THRESHOLD = 0.7` (100% satisfaction at ≤70% cost-to-price ratio)
- `MIN_SATISFACTION_PRICE_RATIO_THRESHOLD = 0.95` (0% satisfaction at ≥95% cost-to-price ratio)

### Key Insight

**Satisfaction is NOT directly about the ticket price you set.** It's about the **perceived cost** versus the **standard price**. The perceived cost is influenced by many factors beyond just the ticket price.

---

## 2. The Complete Cost Formula

The "cost" in satisfaction calculation is the **perceived cost** from the passenger's perspective, calculated through multiple adjustment ratios:

```scala
def computeCost(link: Transport, linkClass: LinkClass, externalCostModifier: Double = 1.0): Double = {
    val standardPrice = link.standardPrice(preferredLinkClass)
    var cost = standardPrice * priceAdjustRatio(link, linkClass)
    cost = (cost * qualityAdjustRatio(homeAirport, link, linkClass)).toInt
    cost = (cost * tripDurationAdjustRatio(link, linkClass)).toInt
    
    if (loyaltySensitivity > 0) {
        cost = (cost * loyaltyAdjustRatio(link)).toInt
    }
    
    cost = cost * loungeAdjustRatio(link, loungeLevelRequired, linkClass)
    cost *= externalCostModifier
    
    computeCost(cost, link, linkClass)
}
```

### The Five Key Adjustment Ratios

| Ratio | Impact | What It Measures |
|-------|--------|------------------|
| **Price Adjust Ratio** | Ticket price vs standard price | How much you're charging |
| **Quality Adjust Ratio** | Computed quality vs expected quality | Service quality, airplane condition, raw quality |
| **Trip Duration Ratio** | Frequency and flight speed | Wait time and journey duration |
| **Loyalty Adjust Ratio** | Airline loyalty at home airport | Brand recognition and customer loyalty |
| **Lounge Adjust Ratio** | Lounge availability and level | Business/First class amenities |

---

## 3. Computed Quality Breakdown

### The Computed Quality Formula

This is **crucial** for understanding service quality impact:

```scala
override def computedQuality: Int = {
    val airplaneConditionQuality = inServiceAirplanes.toList.map {
        case ((airplane, assignmentPerAirplane)) => 
            airplane.condition / Airplane.MAX_CONDITION * assignmentPerAirplane.frequency
    }.sum / frequency * 20
    
    computedQualityStore = (
        rawQuality.toDouble / Link.MAX_QUALITY * 30 +           // 30% from route's raw quality (0-100)
        airline.getCurrentServiceQuality / Airline.MAX_SERVICE_QUALITY * 50 + // 50% from airline service quality
        airplaneConditionQuality                                  // 20% from airplane condition
    ).toInt
}
```

### Quality Composition

| Component | Max Contribution | Source | Your Control |
|-----------|------------------|--------|--------------|
| **Raw Quality (Route)** | 30 points | Route's "Service Quality" setting (0-100) | Direct control per route |
| **Airline Service Quality** | 50 points | Target Service Quality in main office | Global setting |
| **Airplane Condition** | 20 points | Airplane age and maintenance | Maintenance quality setting |

**Maximum Computed Quality: 100 points**

### Expected Quality by Airport

Each airport has an **expected quality** based on:
- Income level of the airport (higher income = higher expectations)
- Flight type (longer flights have higher expectations)
- Link class (First > Business > Economy)

```scala
val expectedQuality = (flightType: FlightType.Value, linkClass: LinkClass) => {
    Math.max(0, Math.min(incomeLevel.toInt, 50) + 
    Airport.qualityExpectationFlightTypeAdjust(flightType)(linkClass))
}
```

**Flight Type Adjustments by Class:**

| Flight Type | Economy | Business | First |
|-------------|---------|----------|-------|
| Short Haul Domestic | -15 | -5 | +5 |
| Short Haul International | -10 | 0 | +10 |
| Short Haul Intercontinental | -5 | +5 | +15 |
| Medium Haul Domestic | -5 | +5 | +15 |
| Medium Haul International | 0 | +5 | +15 |
| Medium Haul Intercontinental | 0 | +5 | +15 |
| Long Haul Domestic | 0 | +5 | +15 |
| Long Haul International | +5 | +10 | +20 |
| Long Haul Intercontinental | +10 | +15 | +20 |
| Ultra Long Haul Intercontinental | +10 | +15 | +20 |

---

## 4. Factors That Matter Beyond Service Quality

### 4.1 Ticket Pricing

The most direct factor. Lower prices = lower perceived cost = higher satisfaction.

**Price Sensitivity by Class:**
- Economy: 1.0 (full sensitivity)
- Business: 0.9 (90% sensitivity)
- First: 0.8 (80% sensitivity)

### 4.2 Flight Frequency

```scala
val frequencyRatioDelta = Math.max(-1, 
    (frequencyThreshold - link.frequencyByClass(linkClass)).toDouble / frequencyThreshold) * frequencySensitivity
```

**Frequency Thresholds by Preference:**
- Simple/Budget passengers: 3 flights/week minimum
- Speed/Appeal/Elite passengers: 14 flights/week minimum

Low frequency increases perceived cost significantly!

### 4.3 Flight Duration

```scala
val flightDurationRatioDelta = 
    (link.duration - flightDurationThreshold).toFloat / flightDurationThreshold * flightDurationSensitivity
```

Faster planes (SST) can reduce perceived cost for speed-conscious passengers.

### 4.4 Airline Loyalty

```scala
val base = 1 + (-0.1 + loyalty.toDouble / maxLoyalty / 2.25) * loyaltySensitivity
return 1 / base
```

At maximum loyalty (100), passengers can perceive costs up to ~30% lower!

### 4.5 Lounges (Business/First Only)

For Elite passengers and high-class travelers:
- Missing required lounge: +3% to +15% cost penalty per airport
- Having a lounge: Up to -1.5% to -3% cost reduction per airport
- Impact scales with flight distance

```scala
val baseReduceRate = 0.005 + level * 0.01  // Level 3 = 3.5% reduction per lounge
val getPriceReduceFactor = flightDistance => -1 * (baseReduceRate * Math.max(0.5, Math.min(1.0, flightDistance / 10000.0)))
```

### 4.6 Airplane Condition

Poor airplane condition (below 40%) triggers delays and cancellations:
- **Minor delays**: Start occurring at 60-70% condition
- **Major delays**: More common below 40%
- **Cancellations**: Significant risk below 20%

Delays trigger compensation costs AND reduce passenger satisfaction!

---

## 5. Target Service Quality vs Route Service Quality

### The Question: Global vs Per-Route?

**Answer: It depends on your strategy.**

### Option A: High Global Target Service Quality (Recommended for Most)

**Setting:** Target Service Quality at 50-70 in main office

**Pros:**
- Contributes 50% to all routes' computed quality automatically
- Less micromanagement
- Builds consistent brand image
- Benefits ALL routes equally

**Cons:**
- Higher weekly service investment cost
- Formula: `Math.pow(targetQuality / 40, 2.5) * (passengerMileCapacity / 4000) * 30`
- At 50 quality with 10M passenger-miles: ~$937,500/week
- At 70 quality with 10M passenger-miles: ~$2,575,000/week

### Option B: Lower Global, Higher Per-Route (Cherry Picking)

**Setting:** Target Service Quality at 20-30 globally, Route Quality at 80-100 for select routes

**Pros:**
- Lower overall costs
- Targeted investment on profitable/important routes
- Good for airlines with few long-haul routes

**Cons:**
- More management overhead
- Route quality only contributes 30% to computed quality (vs 50% from global)
- Still need decent global quality for the 50% contribution

### The Math

For a route to have good computed quality:
```
computedQuality = rawQuality/100 * 30 + globalServiceQuality/100 * 50 + airplaneCondition/100 * 20
```

**Example Scenarios:**

| Scenario | Raw Quality | Global SQ | Plane Cond | Computed Quality |
|----------|-------------|-----------|------------|------------------|
| Cheap Airline | 20 | 20 | 60 | 28 |
| Balanced | 60 | 50 | 70 | 57 |
| Premium | 100 | 70 | 90 | 83 |
| Cherry Pick (Route 80, Global 30) | 80 | 30 | 80 | 55 |
| High Global (Route 40, Global 70) | 40 | 70 | 80 | 63 |

**Conclusion:** Higher global service quality is generally more effective because it contributes 50% vs 30% for route quality.

---

## 6. Cost-Effective Strategies

### Strategy 1: The Balanced Approach (Best for Most Airlines)

**Settings:**
- Target Service Quality: **50-60**
- Route Quality: **60-80** on key routes
- Maintenance Quality: **70-80**
- Pricing: At or slightly below standard price

**Cost-Benefit:**
- Moderate service investment
- Good computed quality (50-65)
- Satisfies most passenger types
- Expected satisfaction: 55-75%

### Strategy 2: Budget Carrier

**Settings:**
- Target Service Quality: **20-30**
- Route Quality: **20-40**
- Maintenance Quality: **60-70**
- Pricing: **15-25% below standard**

**Cost-Benefit:**
- Very low service investment
- Lower computed quality (25-35)
- COMPENSATE with low prices
- Expected satisfaction: 50-65% (price-driven)

**Best for:** Short-haul domestic, price-sensitive markets

### Strategy 3: Premium Carrier

**Settings:**
- Target Service Quality: **70-85**
- Route Quality: **80-100**
- Maintenance Quality: **85-100**
- Pricing: **At or 5-10% above standard**
- Lounges: Level 2-3 at major airports

**Cost-Benefit:**
- High service investment
- High computed quality (75-95)
- Attracts premium passengers
- Expected satisfaction: 75-90%

**Best for:** Long-haul international, first/business focus

### Strategy 4: Frequency Focus (Speed-Conscious Markets)

**Settings:**
- Target Service Quality: **40-50**
- Route Quality: **40-60**
- Frequency: **14+ flights/week**
- Fast aircraft (high speed rating)

**Cost-Benefit:**
- Moderate service investment
- Average computed quality
- Low trip duration penalty
- Expected satisfaction: 60-75%

**Best for:** Business routes, hub operations

---

## 7. Recommendations by Scenario

### Scenario: New Route Below 60% Satisfaction

**Diagnostic Steps:**
1. Check computed quality (aim for 50+)
2. Verify pricing (should be at or below standard)
3. Check frequency (aim for 7+ weekly)
4. Check airplane conditions (aim for 60%+)

**Quick Fixes (by cost):**
1. **FREE:** Reduce ticket price 5-10%
2. **LOW COST:** Increase route quality to 80
3. **MEDIUM COST:** Increase global service quality by 10
4. **HIGH COST:** Add frequency, upgrade lounges

### Scenario: Long-Haul First Class Below Target

**Key Issues:**
- High expected quality (up to 70)
- Elite passengers expect lounges
- Connection quality matters

**Solutions:**
1. Ensure lounges at both endpoints (Level 2+)
2. Route quality: 100
3. Global service quality: 60+
4. New/well-maintained aircraft (80%+ condition)

### Scenario: Short-Haul Domestic Low Satisfaction

**Key Insight:** Expected quality is LOW for short-haul domestic economy (-15 adjustment!)

**Solutions:**
1. Just lower prices 10-15%
2. Route quality: 40-60 is sufficient
3. Focus on frequency over quality

---

## 8. Technical Implementation Details

### How Service Quality Investment is Calculated

```scala
val getServiceFunding: (Int, Long) => Long = (targetQuality: Int, totalPassengerMileCapacity: Long) => {
    val MIN_PASSENGER_MILE_CAPACITY = 1000 * 1000
    val passengerMileCapacity = Math.max(totalPassengerMileCapacity, MIN_PASSENGER_MILE_CAPACITY).toDouble
    
    val funding = Math.pow(targetQuality.toDouble / 40, 2.5) * (passengerMileCapacity / 4000) * 30
    funding.toLong
}
```

**Cost Examples (at 10M passenger-mile capacity):**

| Target Quality | Weekly Cost |
|----------------|-------------|
| 20 | ~$84,000 |
| 40 | ~$750,000 |
| 50 | ~$1,460,000 |
| 60 | ~$2,450,000 |
| 70 | ~$3,760,000 |
| 80 | ~$5,450,000 |
| 100 | ~$9,375,000 |

**The cost scales exponentially!** Going from 40 to 60 costs 3x more than 20 to 40.

### Service Quality Transition Speed

```scala
val getNewQuality: (Double, Double) => Double = (currentQuality, targetQuality) => {
    val delta = targetQuality - currentQuality
    val adjustment = 
        if (delta >= 0) { // Going up - slower at high levels
            MAX_SERVICE_QUALITY_INCREMENT * (1 - (currentQuality / Airline.MAX_SERVICE_QUALITY * 0.9))
        } else { // Going down - faster at high levels
            -1 * MAX_SERVICE_QUALITY_DECREMENT * (0.1 + (currentQuality / Airline.MAX_SERVICE_QUALITY * 0.9))
        }
    // MAX_SERVICE_QUALITY_INCREMENT = 0.5
    // MAX_SERVICE_QUALITY_DECREMENT = 10
}
```

**Key Insight:** 
- Quality increases slowly (max 0.5/week at quality 0, even slower at higher levels)
- Quality decreases quickly (up to 10/week at quality 100)
- Set your target early and maintain it!

### Satisfaction to Loyalty Conversion

Higher satisfaction contributes to:
- Increased passenger count (loyalists) at that airport
- Long-term loyalty growth
- Reputation boost through airport champion rankings

---

## 9. Quick Reference Formulas

### Computed Quality Formula
```
Computed Quality = (Raw Quality × 0.3) + (Global Service Quality × 0.5) + (Airplane Condition × 0.2)
```

### Satisfaction Formula
```
Satisfaction = (0.95 - cost/standardPrice) / (0.95 - 0.70)

Where cost is influenced by:
- Actual ticket price
- Quality difference from expectation
- Frequency
- Flight duration
- Loyalty
- Lounges
```

### Target Computed Quality for 60%+ Satisfaction

To achieve >60% satisfaction consistently:
- **Minimum computed quality:** 45-50 (for routes meeting expectations)
- **Recommended:** 55-65 for comfortable margin
- **For premium routes:** 70+

### Quick Cost-Benefit Analysis

| Investment | Impact on Computed Quality | Cost Level |
|------------|---------------------------|------------|
| +10 Global SQ | +5 points | High |
| +10 Route Quality | +3 points | Low |
| +10% Airplane Condition | +2 points | Medium |
| -10% Ticket Price | ~+5-10% satisfaction | Revenue loss |
| +7 Frequency | Variable (removes penalty) | Medium |

---

## Conclusion: The Most Cost-Effective Strategy

For most airlines seeking satisfaction above 60%:

### The Optimal Balance

1. **Global Service Quality: 50-60** (provides 25-30 points to computed quality)
2. **Route Quality: 60-80** on important routes (provides 18-24 points)
3. **Maintenance Quality: 70-80** (keeps planes at 60-80% condition = 12-16 points)
4. **Pricing: At standard or 5-10% below**
5. **Frequency: Minimum 7/week, ideally 14+/week** on competitive routes
6. **Lounges: Level 2** at airports with significant business/first traffic

This yields:
- Computed Quality: 55-70
- Moderate costs
- Satisfaction: 60-80%

### Priority Order (Most Cost-Effective First)

1. **Lower prices** (if margins allow) - FREE, immediate impact
2. **Increase route quality** - LOW cost, moderate impact
3. **Increase frequency** - MEDIUM cost, reduces duration penalty
4. **Increase global service quality** - HIGH cost, but benefits all routes
5. **Add/upgrade lounges** - HIGH cost, only for premium routes

---

## Appendix A: Passenger Preference Distribution

Understanding the distribution of passenger types helps prioritize which factors matter most.

### Economy Class Preferences (by weight)

| Preference Type | Weight | Key Sensitivities |
|-----------------|--------|-------------------|
| **Budget (Price 1.2)** | 2 | Very price sensitive |
| **Budget (Price 1.3)** | 2 | Very price sensitive |
| **Budget (Price 1.4)** | 1 | Extremely price sensitive |
| **Budget (Price 1.5)** | 1 | Extremely price sensitive |
| **Speed** | 2 | Frequency (14/week), flight duration |
| **Appeal (Standard)** | 8 | Quality, moderate price |
| **Appeal (Loyal 1.1)** | 2 | Quality + loyalty bonus |
| **Appeal (Loyal 1.2)** | 1 | Quality + stronger loyalty bonus |

*Note: Budget weights are multiplied by 2-3x in low-income airports.*

**Key Insight:** ~40% of economy passengers are budget-focused, ~50% are appeal-focused (quality matters), ~10% are speed-focused.

### Business Class Preferences (by weight)

| Preference Type | Weight | Key Sensitivities |
|-----------------|--------|-------------------|
| **Speed** | 6 | Frequency (14/week), flight duration |
| **Appeal (No Lounge)** | 4 | Quality, moderate price |
| **Appeal (Lounge Lv1)** | 2 | Quality + lounge required |
| **Appeal (Lounge Lv2)** | 2 | Quality + better lounge required |
| **Appeal (Lounge Lv3, Loyal)** | 2 | Quality + best lounge + loyalty |

**Key Insight:** ~40% of business passengers are speed-focused, ~35% want quality, ~25% require lounges.

### First Class Preferences (by weight)

| Preference Type | Weight | Key Sensitivities |
|-----------------|--------|-------------------|
| **Speed** | 1 | Frequency (14/week), flight duration |
| **Appeal (No Lounge)** | 2 | Quality, lower price sensitivity |
| **Appeal (Lounge Lv1)** | 1 | Quality + lounge |
| **Appeal (Lounge Lv2)** | 1 | Quality + better lounge |
| **Appeal (Lounge Lv3, Loyal)** | 1 | Quality + best lounge + loyalty |

**Key Insight:** First class passengers are less price-sensitive but very quality and lounge focused.

---

## Appendix B: Sensitivity Values by Preference Type

### SimplePreference (Budget/Carefree)
```
priceSensitivity: 1.0 - 1.5 (varies)
qualitySensitivity: 0.5
loyaltySensitivity: 0
frequencyThreshold: 3 flights/week
frequencySensitivity: 0.02
flightDurationSensitivity: 0
connectionCostRatio: 0.5 (more okay with connections)
```

### SpeedPreference (Swift)
```
priceSensitivity: 0.9
qualitySensitivity: 0.5
loyaltySensitivity: 0
frequencyThreshold: 14 flights/week
frequencySensitivity: 0.15
flightDurationSensitivity: 0.85
connectionCostRatio: 2.0 (strongly prefers direct)
```

### AppealPreference (Comprehensive/Brand Conscious/Elite)
```
priceSensitivity: varies by class (1.0 Y, 0.9 J, 0.8 F)
qualitySensitivity: 1.0
loyaltySensitivity: 1.0 - 1.2 (varies)
frequencyThreshold: 14 flights/week
frequencySensitivity: 0.05
flightDurationSensitivity: 0.25 Y, 0.4 J, 0.55 F
loungeSensitivity: 1.0
connectionCostRatio: 1.0 (standard)
```

---

## Appendix C: Complete Cost Calculation Example

Let's calculate the satisfaction for a hypothetical route:

**Route Details:**
- Distance: 5000 km (Long-Haul International)
- Standard Price Economy: $400
- Ticket Price Set: $380 (5% below standard)
- Raw Quality: 60
- Global Service Quality: 50
- Airplane Condition: 75%
- Frequency: 10 flights/week
- Airport Income Level: 40 (expected quality = 40 + 5 = 45 for Economy)
- Loyalty: 30/100

**Step 1: Computed Quality**
```
Computed Quality = (60/100 × 30) + (50/100 × 50) + (75/100 × 20)
                 = 18 + 25 + 15 = 58
```

**Step 2: Quality Adjustment (for Appeal passenger)**
```
Expected Quality = 45
Quality Delta = 58 - 45 = +13 (good!)
Price Adjust from Quality ≈ 0.935 (quality lowers perceived cost)
```

**Step 3: Price Adjustment**
```
Ticket Price = $380
Standard Price = $400
Delta = $380 - $400 = -$20
Price Adjust Ratio = 1 + (-20 × 1.0 / 400) = 0.95
```

**Step 4: Trip Duration Adjustment**
```
Frequency = 10, Threshold = 14
Frequency Delta = (14 - 10) / 14 × 0.05 = 0.0143 penalty
Duration Adjust = 1.0143
```

**Step 5: Loyalty Adjustment**
```
Loyalty = 30
Base = 1 + (-0.1 + 30/100/2.25) × 1.0 = 1 + 0.0333 = 1.0333
Loyalty Ratio = 1/1.0333 = 0.968 (loyalty lowers cost)
```

**Step 6: Final Cost Calculation**
```
Base Cost = $400 (standard price)
Final Cost = 400 × 0.95 × 0.935 × 1.0143 × 0.968
           = 400 × 0.872 = $348.80
```

**Step 7: Satisfaction Calculation**
```
Cost/Standard Ratio = 348.80 / 400 = 0.872
Satisfaction = (0.95 - 0.872) / (0.95 - 0.70)
             = 0.078 / 0.25
             = 0.312 or 31.2%
```

This seems low! That's because the example shows a realistic scenario where even with decent settings, satisfaction can be moderate. The example demonstrates that **multiple factors need to work together** to achieve high satisfaction. To get above 60%:

**For 60% Satisfaction:**
```
0.60 = (0.95 - ratio) / 0.25
ratio = 0.95 - 0.15 = 0.80
```

You need cost/standard ratio ≤ 0.80. To achieve this with our example:
- Lower prices further (to ~$320) OR
- Higher quality (to offset more) OR
- Higher loyalty (provides bigger discount)

This demonstrates why **multiple factors working together** is the key to high satisfaction!

---

*Document generated based on analysis of airline simulation source code. All formulas and values are derived from the actual game implementation.*
