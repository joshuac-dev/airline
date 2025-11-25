# Service Quality Optimization Guide

## Executive Summary

This document provides a comprehensive analysis of the service quality system in the Airline game and offers strategic guidance on the most cost-effective methods to improve a route's quality above 0.6 (60%). The key finding is that **using the airline-wide "Target Service Quality" setting is generally more cost-effective than setting individual route quality levels**, especially for airlines with larger networks.

---

## Table of Contents

1. [Understanding the Quality System](#understanding-the-quality-system)
2. [Computed Quality Formula](#computed-quality-formula)
3. [Service Funding Cost Analysis](#service-funding-cost-analysis)
4. [Route-Specific Quality (rawQuality)](#route-specific-quality-rawquality)
5. [Strategic Recommendations](#strategic-recommendations)
6. [Cost-Effectiveness Analysis](#cost-effectiveness-analysis)
7. [Quality Expectations by Route Type](#quality-expectations-by-route-type)
8. [Implementation Scenarios](#implementation-scenarios)

---

## Understanding the Quality System

The game has two distinct quality settings that players can adjust:

### 1. Target Service Quality (Airline-Wide)
- **Location**: Main office / Airline settings
- **Range**: 0-100
- **Effect**: Sets a global quality baseline that affects ALL routes operated by the airline
- **Cost**: Weekly service investment that scales with total passenger-mile capacity
- **Benefit**: Contributes up to **50 points** to the computed quality of every route

### 2. Raw Quality / Service Quality (Route-Specific)
- **Location**: Individual route/link settings
- **Range**: 0-100
- **Effect**: Sets quality for a specific route only
- **Cost**: Impacts inflight costs per passenger (see inflight cost section)
- **Benefit**: Contributes up to **30 points** to the computed quality of that specific route

### 3. Airplane Condition Quality
- **Range**: 0-100 (based on airplane condition)
- **Effect**: Automatic based on airplane maintenance and age
- **Cost**: Maintenance costs (separate from service quality costs)
- **Benefit**: Contributes up to **20 points** to computed quality

---

## Computed Quality Formula

The final **computed quality** for any route is calculated as:

```
computedQuality = (rawQuality / 100 * 30) + (currentServiceQuality / 100 * 50) + (airplaneConditionQuality * 20)
```

Breaking this down:
- **Raw Quality Component (max 30 points)**: `rawQuality / 100 * 30`
- **Service Quality Component (max 50 points)**: `currentServiceQuality / 100 * 50`  
- **Airplane Condition Component (max 20 points)**: Based on airplane condition divided by max condition, multiplied by frequency-weighted average

### Example Calculations

| rawQuality | Service Quality | Airplane Condition | Computed Quality |
|------------|-----------------|-------------------|------------------|
| 0 | 0 | 100% | 20 |
| 0 | 60 | 100% | 50 (0 + 30 + 20) |
| 50 | 60 | 100% | 65 (15 + 30 + 20) |
| 100 | 60 | 100% | 80 (30 + 30 + 20) |
| 0 | 100 | 100% | 70 (0 + 50 + 20) |
| 100 | 100 | 100% | 100 (30 + 50 + 20) |

**To achieve a computed quality of 60:**
- With 100% airplane condition (20 points), you need 40 more points
- Option A: Service Quality at 80 = 40 points → Total: 60
- Option B: Service Quality at 60 (30 pts) + rawQuality at 33 (10 pts) → Total: 60
- Option C: rawQuality at 100 (30 pts) + Service Quality at 20 (10 pts) → Total: 60

---

## Service Funding Cost Analysis

The weekly cost of maintaining a target service quality is calculated using this formula:

```scala
funding = Math.pow(targetQuality / 40, 2.5) * (passengerMileCapacity / 4000) * 30
```

Where:
- `targetQuality`: The airline-wide target service quality setting (0-100)
- `passengerMileCapacity`: Total of (frequency × airplane capacity × distance) across all routes
- Minimum passenger-mile capacity is capped at 1,000,000

### Cost Scaling Analysis

The cost scales **exponentially** (power of 2.5) with quality level:

| Target Quality | Relative Cost Multiplier |
|---------------|-------------------------|
| 0 | 0 |
| 20 | 0.09x |
| 40 | 1x (baseline) |
| 50 | 1.75x |
| 60 | 2.76x |
| 70 | 4.08x |
| 80 | 5.66x |
| 90 | 7.52x |
| 100 | 9.68x |

### Cost Examples

For an airline with **10 million passenger-mile capacity**:

| Target Quality | Weekly Cost |
|---------------|-------------|
| 20 | $6,750 |
| 40 | $75,000 |
| 50 | $131,250 |
| 60 | $207,000 |
| 70 | $306,000 |
| 80 | $424,500 |
| 100 | $726,000 |

---

## Route-Specific Quality (rawQuality)

Setting the route-specific quality (rawQuality) affects **inflight costs** rather than a direct weekly fee.

### Inflight Cost Calculation

```scala
val star = rawQuality / 20  // 0-5 stars based on rawQuality
val durationCostPerHour = star match {
  case 0 | 1 => 1
  case 2 => 4
  case 3 => 8
  case 4 => 13
  case 5 => 20  // rawQuality 80-100
}
costPerPassenger = (20 + durationCostPerHour * flightDuration / 60) * 2  // Roundtrip
```

### Inflight Cost Examples (per passenger, roundtrip)

For a 3-hour flight:

| rawQuality | Star Rating | Cost per Passenger |
|------------|-------------|-------------------|
| 0-19 | 0-1 stars | $46 |
| 20-39 | 2 stars | $64 |
| 40-59 | 3 stars | $88 |
| 60-79 | 4 stars | $118 |
| 80-100 | 5 stars | $160 |

### Key Insight: Route-Specific Quality is Per-Passenger

Unlike the airline-wide service quality which is a flat weekly cost, **rawQuality costs scale linearly with passengers**. This means:
- High-volume routes will cost more to maintain at high rawQuality
- Low-volume routes are cheaper to boost individually

---

## Strategic Recommendations

### Primary Recommendation: Use Target Service Quality

**For most airlines, setting the airline-wide "Target Service Quality" to 60-80 is the most cost-effective approach to achieving quality above 60 on all routes.**

#### Reasons:

1. **Service Quality contributes MORE to computed quality (50 points max vs 30 for rawQuality)**
2. **Flat weekly cost** - Doesn't increase with more passengers, only with capacity
3. **Affects ALL routes** - One setting benefits your entire network
4. **Quality transition is smooth** - Current service quality gradually approaches target

### When to Cherry-Pick Routes with Higher rawQuality

Setting individual routes to higher rawQuality makes sense when:

1. **Premium routes**: Long-haul intercontinental routes where passengers expect higher quality
2. **Competition**: Routes with strong competitors where quality differentiation matters
3. **Low-volume routes**: Routes with few passengers where per-passenger costs remain low
4. **Targeted marketing**: Routes where you want to establish premium positioning

### When NOT to Use High rawQuality on Individual Routes

1. **High-volume short-haul routes**: The per-passenger costs add up quickly
2. **Budget-focused routes**: Where price sensitivity is high (economy-heavy)
3. **Routes where airline-wide quality is already sufficient**

---

## Cost-Effectiveness Analysis

### Scenario: Achieving Quality ≥ 60

**Assumption**: 100% airplane condition provides 20 points. Need 40 more points.

#### Option 1: Service Quality Only
- Set Target Service Quality to 80%
- Provides: 80 × 50/100 = 40 points
- **Total computed quality: 60**
- Cost: Scales with total network capacity (fixed weekly)

#### Option 2: Mix of Both
- Set Target Service Quality to 60%
- Provides: 60 × 50/100 = 30 points
- Set rawQuality to 33% on specific routes
- Provides: 33 × 30/100 = 10 points
- **Total computed quality: 60**
- Cost: Lower base cost + per-passenger inflight costs on selected routes

#### Option 3: rawQuality Heavy (NOT Recommended)
- Keep Target Service Quality low (20%)
- Provides: 20 × 50/100 = 10 points
- Need rawQuality at 100% on routes
- Provides: 100 × 30/100 = 30 points
- **Total computed quality: 60**
- Cost: VERY HIGH inflight costs due to 5-star service

### Recommendation Matrix

| Network Size | Passenger Volume | Recommended Strategy |
|--------------|------------------|---------------------|
| Small (<50 routes) | Low | Service Quality 60-70, cherry-pick premium routes |
| Small (<50 routes) | High | Service Quality 70-80 |
| Medium (50-200 routes) | Any | Service Quality 70-80 |
| Large (>200 routes) | Any | Service Quality 80+ (economy of scale) |

---

## Quality Expectations by Route Type

Passengers have different quality expectations based on route type and class:

### Quality Expectations Table

| Flight Type | Economy Adj | Business Adj | First Adj |
|------------|-------------|--------------|-----------|
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

The expected quality is calculated as:
```
expectedQuality = min(incomeLevel, 50) + flightTypeAdjustment
```

### Practical Implications

- **Short-haul domestic economy**: Passengers tolerate lower quality (expectation reduced by 15)
- **Long-haul intercontinental first class**: Passengers demand high quality (expectation increased by 20)
- **Higher-income airports**: Have higher base expectations

---

## Implementation Scenarios

### Scenario 1: New Airline Starting Out

**Recommended Approach:**
1. Set Target Service Quality to **50-60** initially
2. Focus on airplane condition (maintain above 80%)
3. Expected computed quality: ~55-65 on most routes
4. Invest savings in network expansion

### Scenario 2: Growing Airline (Moderate Network)

**Recommended Approach:**
1. Set Target Service Quality to **70**
2. Keep airplane condition above 70%
3. Cherry-pick 5-10 premium routes, set rawQuality to **60-80**
4. Expected computed quality: 60-75 on most routes, 70-85 on premium routes

### Scenario 3: Large Established Airline

**Recommended Approach:**
1. Set Target Service Quality to **80-90**
2. Maintain airplane condition above 60% (larger fleets have older planes)
3. Set rawQuality to **80-100** on flagship intercontinental routes
4. Expected computed quality: 70-85 on most routes, 85-100 on flagship routes

### Scenario 4: Budget Carrier Strategy

**Recommended Approach:**
1. Set Target Service Quality to **40-50**
2. Keep rawQuality at **0-20** on all routes
3. Focus on airplane efficiency over condition (accept 50-60% condition)
4. Expected computed quality: 40-50 (acceptable for price-sensitive passengers)
5. Compensate with **lower prices**

---

## Key Takeaways

1. **Service Quality (airline-wide) is the primary lever** - It provides up to 50 points vs 30 for rawQuality

2. **The cost curve is exponential** - Going from 60 to 80 service quality costs ~2x more than 40 to 60

3. **rawQuality is best for targeted improvements** - Use it to boost specific premium routes

4. **Airplane condition matters** - Free quality points from maintaining your fleet

5. **Don't over-invest in rawQuality on high-volume routes** - The per-passenger costs accumulate

6. **Match quality to passenger expectations** - Short-haul economy doesn't need 100 quality

7. **For quality ≥60 goal**: Target Service Quality of 80 + good airplane condition is usually sufficient

---

## Formulas Quick Reference

### Computed Quality
```
computedQuality = (rawQuality / 100 * 30) + (serviceQuality / 100 * 50) + (airplaneConditionRatio * 20)

Where:
- rawQuality: Route-specific quality setting (0-100)
- serviceQuality: Airline's current service quality (0-100)  
- airplaneConditionRatio: Weighted average airplane condition (0.0-1.0)
```

### Service Funding Weekly Cost
```
cost = pow(targetQuality / 40, 2.5) * (passengerMileCapacity / 4000) * 30

Where:
- targetQuality: Target service quality setting (0-100)
- passengerMileCapacity: Sum of (frequency * planeCapacity * distance) for all routes
- Minimum passengerMileCapacity is 1,000,000
```

### Inflight Cost per Passenger
```
star = floor(rawQuality / 20)   // Results in 0-5 stars
durationCostPerHour = lookup[star]  // [1, 1, 4, 8, 13, 20]
costPerPax = (20 + durationCostPerHour * flightDurationHours) * 2   // Roundtrip

Where:
- rawQuality: Route-specific quality setting (0-100)
- flightDurationHours: Flight duration in hours
```

### Quality Expectation
```
expected = min(airportIncomeLevel, 50) + flightTypeAdjustment(linkClass)

Where:
- airportIncomeLevel: Income level of departure airport
- flightTypeAdjustment: Adjustment based on flight type and cabin class (see table above)
```
