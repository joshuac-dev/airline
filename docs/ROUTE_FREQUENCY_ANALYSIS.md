# Route Frequency Analysis: Diminishing Returns and Penalties

## Executive Summary

This document provides a comprehensive analysis of how route frequency affects various aspects of airline operations in the Airline Tycoon game. The analysis covers passenger demand, operational costs, and the differences across route types.

**Key Finding**: The system implements **diminishing returns** for high frequency routes through the passenger preference system, but does **NOT implement penalties** for excessive frequency. Higher frequencies always provide benefits, but those benefits decrease as frequency increases beyond certain thresholds.

---

## 1. Overview of Frequency Mechanics

### 1.1 High Frequency Threshold
- **Constant**: `Link.HIGH_FREQUENCY_THRESHOLD = 14`
- **Location**: `airline-data/src/main/scala/com/patson/model/Link.scala` (line 189)
- **Purpose**: This threshold is used in various calculations and passenger preferences to distinguish between low and high-frequency routes.

### 1.2 Frequency Definition
Frequency represents the number of flights per week on a route. It is calculated as the sum of all assigned airplane frequencies on that link:
```scala
frequency = assignedAirplanes.values.map(_.frequency).sum
```

---

## 2. Diminishing Returns on Passenger Demand

The primary mechanism for diminishing returns occurs in the **passenger preference system**, where different passenger types react differently to route frequency.

### 2.1 Passenger Preference Types

There are three main passenger preference types, each with different frequency sensitivities:

#### 2.1.1 SimplePreference (Budget and Carefree Passengers)
**Location**: `FlightPreference.scala` lines 252-284

**Characteristics**:
- **Frequency Threshold**: 3 flights per week
- **Frequency Sensitivity**: 0.02 (2%)
- **Target Audience**: Price-conscious passengers with minimal schedule requirements

**Frequency Impact**:
```scala
frequencyRatioDelta = max(-1, (3 - frequency) / 3) * 0.02
```

**Analysis**:
- At **0 frequency**: penalty of +2% to perceived cost
- At **3 frequency**: neutral (no adjustment)
- At **6+ frequency**: bonus of -2% to perceived cost (capped)
- **Diminishing Returns**: Benefits cap at 2x threshold (6 flights), providing only 2% cost reduction maximum

#### 2.1.2 SpeedPreference (Swift Passengers)
**Location**: `FlightPreference.scala` lines 286-311

**Characteristics**:
- **Frequency Threshold**: 14 flights per week (HIGH_FREQUENCY_THRESHOLD)
- **Frequency Sensitivity**: 0.15 (15%)
- **Target Audience**: Time-sensitive business travelers who value convenience

**Frequency Impact**:
```scala
frequencyRatioDelta = max(-1, (14 - frequency) / 14) * 0.15
```

**Analysis**:
- At **0 frequency**: penalty of +15% to perceived cost
- At **7 frequency**: penalty of +7.5% to perceived cost
- At **14 frequency**: neutral (no adjustment)
- At **28+ frequency**: bonus of -15% to perceived cost (capped)
- **Diminishing Returns**: Benefits cap at 2x threshold (28 flights), providing 15% maximum cost reduction

#### 2.1.3 AppealPreference (Comprehensive, Brand Conscious, and Elite Passengers)
**Location**: `FlightPreference.scala` lines 313-367

**Characteristics**:
- **Frequency Threshold**: 14 flights per week (HIGH_FREQUENCY_THRESHOLD)
- **Frequency Sensitivity**: 0.05 (5%)
- **Target Audience**: Quality-focused passengers considering multiple factors

**Frequency Impact**:
```scala
frequencyRatioDelta = max(-1, (14 - frequency) / 14) * 0.05
```

**Analysis**:
- At **0 frequency**: penalty of +5% to perceived cost
- At **7 frequency**: penalty of +2.5% to perceived cost
- At **14 frequency**: neutral (no adjustment)
- At **28+ frequency**: bonus of -5% to perceived cost (capped)
- **Diminishing Returns**: Benefits cap at 2x threshold (28 flights), providing 5% maximum cost reduction

### 2.2 Overall Frequency Cost Adjustment Formula

The frequency adjustment is applied as part of the `tripDurationAdjustRatio` calculation:

```scala
val frequencyRatioDelta = max(-1, (frequencyThreshold - link.frequencyByClass(linkClass)) / frequencyThreshold) * frequencySensitivity

val finalDelta = max(-0.75, frequencyRatioDelta + flightDurationRatioDelta)

costMultiplier = 1 + finalDelta
```

**Key Points**:
1. **Cap at -75%**: The total trip duration adjustment (frequency + flight duration) is capped at -75%, preventing extreme discounts
2. **Diminishing Returns**: Once frequency reaches 2x the threshold, additional flights provide NO additional benefit to passenger appeal
3. **No Penalties for High Frequency**: There is no penalty for having extremely high frequency (e.g., 50+ flights per week)

---

## 3. Operational Costs Related to Frequency

### 3.1 Staff Requirements (Office Staff)

Staff requirements scale **linearly** with frequency, with no diminishing returns or penalties.

**Formula** (Location: `Link.scala` lines 162-176):
```scala
StaffBreakdown = (basicStaff + perFrequencyStaff * frequency + per1000PaxStaff * capacity/1000) * airlineBaseModifier
```

#### 3.1.1 Staff Requirements by Route Type

| Flight Type | Basic Staff | Per Frequency | Multiply Factor | Total Per Freq |
|-------------|-------------|---------------|-----------------|----------------|
| Short-haul Domestic | 8 | 2/5 | 2 | 0.8 |
| Medium-haul Domestic | 10 | 2/5 | 2 | 0.8 |
| Long-haul Domestic | 12 | 2/5 | 2 | 0.8 |
| Short-haul International | 10 | 2/5 | 2 | 0.8 |
| Medium-haul International | 15 | 2/5 | 2 | 0.8 |
| Long-haul International | 20 | 2/5 | 2 | 0.8 |
| Short-haul Intercontinental | 15 | 2/5 | 3 | 1.2 |
| Medium-haul Intercontinental | 25 | 2/5 | 3 | 1.2 |
| Long-haul Intercontinental | 30 | 2/5 | 4 | 1.6 |
| Ultra Long-haul Intercontinental | 30 | 2/5 | 4 | 1.6 |

**Analysis**:
- Staff costs scale **linearly** with frequency
- No diminishing returns or economies of scale
- Intercontinental routes require more staff per frequency
- Ultra long-haul routes require the most staff per frequency (1.6 staff/freq vs 0.8 for domestic)

### 3.2 Crew Costs

Crew costs scale **linearly** with capacity and duration:

**Formula** (Location: `LinkSimulation.scala` lines 319-323):
```scala
crewCost = linkClass.resourceMultiplier * capacity * duration / 60 * CREW_UNIT_COST
```

Where:
- `CREW_UNIT_COST = 12`
- `resourceMultiplier`: Economy=1, Business=2, First=3

**Analysis**:
- Crew costs depend on **total capacity** (frequency × seats per flight), not directly on frequency
- No diminishing returns or penalties based on frequency alone
- Higher frequency with smaller planes vs. lower frequency with larger planes: **Cost is identical** if total capacity is the same

### 3.3 Fuel Costs

Fuel costs scale **linearly** with frequency:

**Formula** (Location: `LinkSimulation.scala` lines 238-265):
```scala
fuelCost = baseFuelBurn * distance_multipliers * FUEL_UNIT_COST * (frequency - cancellationCount) * (0.7 + 0.3 * loadFactor)
```

Where:
- `FUEL_UNIT_COST = 0.0043`
- Load factor adjustment: 70% at 0% load, 100% at 100% load

**Analysis**:
- Fuel costs scale **linearly** with frequency
- No diminishing returns or penalties for high frequency
- Lower load factors reduce fuel consumption proportionally

### 3.4 Airport Fees

Airport fees scale **linearly** with frequency:

**Formula** (Location: `LinkSimulation.scala` lines 301-306):
```scala
airportFees = (fromAirport.slotFee + toAirport.slotFee + fromAirport.landingFee + toAirport.landingFee) * frequency
```

**Analysis**:
- Airport fees scale **linearly** with frequency
- No diminishing returns or economies of scale
- No penalties for high frequency

### 3.5 Maintenance and Depreciation

Maintenance and depreciation are based on **airplane utilization**, not route frequency directly:

**Maintenance** (Location: `LinkSimulation.scala` lines 293-299):
```scala
maintenanceCost = airplane.model.baseMaintenanceCost * assignmentWeight * airlineMaintenanceQuality / MAX_MAINTENANCE_QUALITY
```

**Depreciation** (Location: `LinkSimulation.scala` lines 308-313):
```scala
depreciation = airplane.depreciationRate * assignmentWeight
```

Where `assignmentWeight` is the proportion of airplane time assigned to this link.

**Analysis**:
- Costs depend on **airplane utilization across all routes**, not individual route frequency
- No specific penalties for high frequency on individual routes
- Higher frequency requires more airplanes, which increases total maintenance and depreciation

---

## 4. Route Type Differences

### 4.1 Route Type Classifications

Routes are classified into 10 types based on distance and geographical relationship:

**Location**: `Computation.scala` lines 100-135

#### Domestic Routes (Same Country)
1. **Short-haul Domestic**: ≤ 1,000 km
2. **Medium-haul Domestic**: 1,001 - 3,000 km
3. **Long-haul Domestic**: > 3,000 km

#### International Routes (Same Zone/Continent)
4. **Short-haul International**: ≤ 2,000 km
5. **Medium-haul International**: 2,001 - 4,000 km
6. **Long-haul International**: > 4,000 km

#### Intercontinental Routes (Different Zones)
7. **Short-haul Intercontinental**: ≤ 2,000 km
8. **Medium-haul Intercontinental**: 2,001 - 5,000 km
9. **Long-haul Intercontinental**: 5,001 - 12,000 km
10. **Ultra Long-haul Intercontinental**: > 12,000 km

### 4.2 Frequency Impact Differences by Route Type

**Passenger Preferences**: 
- Frequency thresholds are **independent of route type**
- All route types use the same threshold (3 for SimplePreference, 14 for others)
- However, passenger type distribution may vary by route type (not analyzed in detail here)

**Operational Costs**:
- Staff requirements per frequency vary by route type (see section 3.1.1)
- Intercontinental routes require **50-100% more staff per frequency** than domestic routes
- This creates an **implicit penalty** for high frequency on long intercontinental routes

**Link Creation Costs**:
- International routes cost **3x more** to establish than domestic routes
- Longer distances increase establishment costs linearly
- This is a one-time cost, not recurring based on frequency

---

## 5. Diminishing Returns Analysis

### 5.1 Summary of Diminishing Returns

| Aspect | Diminishing Returns? | Cap/Threshold | Notes |
|--------|---------------------|---------------|-------|
| **Passenger Appeal (Simple)** | Yes | 6 flights (2% benefit) | Minimal impact |
| **Passenger Appeal (Speed)** | Yes | 28 flights (15% benefit) | Significant for time-sensitive pax |
| **Passenger Appeal (Appeal)** | Yes | 28 flights (5% benefit) | Moderate impact |
| **Staff Costs** | No | None | Linear scaling |
| **Crew Costs** | No | None | Linear scaling with capacity |
| **Fuel Costs** | No | None | Linear scaling |
| **Airport Fees** | No | None | Linear scaling |
| **Maintenance** | No | None | Based on airplane utilization |
| **Depreciation** | No | None | Based on airplane utilization |

### 5.2 Key Insights

1. **Passenger Appeal Shows Diminishing Returns**:
   - Benefits from frequency **plateau at 2x the threshold**
   - For most passengers (Speed/Appeal types): **14 flights is neutral, 28+ flights is optimal**
   - Additional flights beyond 28/week provide **NO additional passenger appeal benefits**

2. **Operational Costs Show NO Diminishing Returns**:
   - All operational costs scale **linearly** with frequency
   - **No economies of scale** for high-frequency routes
   - **No penalties** for extremely high frequency

3. **Net Effect - Diminishing Returns**:
   - Revenue potential plateaus after reaching 2x frequency threshold (28 flights for most passengers)
   - Costs continue to increase linearly
   - Result: **Diminishing returns on profitability** for frequencies > 28 flights/week

---

## 6. Penalty Analysis

### 6.1 Explicit Penalties

**Finding**: There are **NO explicit penalties** for having high frequency routes in the codebase.

- No code that reduces revenue or increases costs for frequency > threshold
- No "over-scheduling" penalty
- No slot congestion or capacity constraints (beyond physical airplane availability)

### 6.2 Implicit Penalties

While there are no explicit penalties, several implicit factors create effective penalties:

1. **Opportunity Cost**:
   - Airplanes assigned to high-frequency routes cannot serve other routes
   - Capital tied up in more airplanes for marginal benefit

2. **Staff Costs**:
   - Continue to increase linearly with no cap
   - Intercontinental routes particularly expensive (1.6-2x domestic staff requirements)

3. **Load Factor Risk**:
   - Higher frequency spreads passengers across more flights
   - Risk of lower load factors, reducing fuel efficiency and revenue per flight
   - Load factor below 50% on competitive routes triggers **cancellation warnings** (Location: `LinkSimulation.scala` lines 416-483)

4. **Competition Effects**:
   - On routes with 3+ airlines, sustained load factor < 50% for 52 weeks results in **route license revocation**
   - High frequency doesn't directly cause this, but can contribute to market oversupply

---

## 7. Frequency Optimization Recommendations

Based on this analysis, optimal frequency strategies are:

### 7.1 For Passenger Appeal
- **Minimum Target**: 14 flights/week (neutral perception for Speed/Appeal passengers)
- **Optimal Target**: 28 flights/week (maximum appeal benefit)
- **Beyond 28**: No additional passenger appeal benefits; only worthwhile if demand justifies the capacity

### 7.2 By Route Type

#### Domestic Routes
- Lower staff costs per frequency (0.8 staff/freq)
- Can justify higher frequencies more easily
- **Recommended**: 14-28 flights/week for competitive routes

#### International Routes
- Moderate staff costs (0.8 staff/freq for short/medium, same as domestic)
- Similar to domestic considerations
- **Recommended**: 14-28 flights/week for major markets

#### Intercontinental Routes
- **Higher staff costs** (1.2-1.6 staff/freq)
- Long distances reduce airplane utilization (fewer frequencies possible per plane)
- **Recommended**: 7-14 flights/week for most routes, up to 28 for major hubs
- Beyond 28 flights rarely justified due to high staff costs

#### Ultra Long-haul Intercontinental
- **Highest staff costs** (1.6 staff/freq)
- Very limited airplane utilization (long flight times)
- **Recommended**: 7-14 flights/week maximum
- Focus on quality and pricing rather than frequency

### 7.3 General Strategy

1. **Start with 14 flights/week** on any route to achieve neutral passenger perception
2. **Scale to 28 flights/week** if demand and load factors justify it (target 70%+ load factor)
3. **Avoid exceeding 28 flights/week** unless:
   - Demand significantly exceeds capacity
   - Load factors remain above 70%
   - Competition requires it
4. **Monitor load factors**: Below 50% on competitive routes risks license revocation
5. **Consider airplane size**: Sometimes larger, less frequent flights are more profitable than many small flights

---

## 8. Code References

### Primary Files
- **Link.scala**: Core link model, staff calculations, frequency definitions
  - Lines 15-185: Link model and frequency handling
  - Lines 187-245: Staff scheme by route type
  
- **FlightPreference.scala**: Passenger preference calculations
  - Lines 14-223: Preference abstract class and frequency adjustments
  - Lines 252-284: SimplePreference implementation
  - Lines 286-311: SpeedPreference implementation
  - Lines 313-367: AppealPreference implementation

- **LinkSimulation.scala**: Operational cost calculations
  - Lines 221-376: Link consumption and cost calculations
  - Lines 416-483: Load factor alerts and route cancellation

- **Computation.scala**: Route type definitions and calculations
  - Lines 100-135: Flight type classification by distance

### Key Constants
```scala
Link.HIGH_FREQUENCY_THRESHOLD = 14                // Link.scala:189
CREW_UNIT_COST = 12                              // LinkSimulation.scala:33
FUEL_UNIT_COST = 0.0043                          // LinkSimulation.scala:23
LOAD_FACTOR_ALERT_THRESHOLD = 0.5                // LinkSimulation.scala:399
LOAD_FACTOR_ALERT_DURATION = 52                  // LinkSimulation.scala:400
```

---

## 9. Conclusion

The airline game implements a sophisticated frequency system with the following characteristics:

### Diminishing Returns: **YES**
- Passenger appeal benefits **plateau at 28 flights/week**
- No additional passenger attraction beyond this threshold
- This creates natural diminishing returns on revenue

### Penalties: **NO (with caveats)**
- **No explicit penalties** for high frequency
- **Implicit penalties** through:
  - Linear cost scaling (no economies of scale)
  - Opportunity costs (capital and airplanes)
  - Load factor risks on oversupplied routes
  - Route license revocation risk if load factors drop too low

### Optimal Strategy
- Target **14-28 flights/week** for most routes
- Higher staff costs on intercontinental routes justify lower frequencies
- Monitor load factors and adjust frequency to maintain profitability
- Focus on route types with favorable staff cost ratios

### Route Type Variations
- **Domestic**: Most cost-effective for high frequency
- **International**: Similar to domestic
- **Intercontinental**: 50-100% higher staff costs limit high-frequency viability
- **Ultra Long-haul**: Highest costs, lowest optimal frequency

### Critical Numerical Thresholds (Quick Reference)
- **3 flights/week**: Budget passenger neutral point (SimplePreference)
- **6 flights/week**: Budget passenger max benefit threshold
- **14 flights/week**: Time-sensitive/quality passenger neutral point (HIGH_FREQUENCY_THRESHOLD)
- **28 flights/week**: Absolute maximum passenger appeal benefit (2× threshold)
- **50% load factor**: Warning threshold for competitive routes (3+ airlines)
- **52 weeks**: Duration of low load factor before route license revocation
- **0.8 staff/flight**: Domestic and international route staff requirement
- **1.2 staff/flight**: Short/medium intercontinental staff requirement (+50%)
- **1.6 staff/flight**: Long/ultra long intercontinental staff requirement (+100%)

This analysis confirms that the game design creates natural incentives to avoid excessive frequency through diminishing returns rather than explicit penalties.
