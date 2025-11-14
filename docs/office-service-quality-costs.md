# Office Service Quality Cost Calculation Documentation

## Overview

This document provides a detailed explanation of how office service quality costs are calculated in the airline simulation. These costs are a critical component of airline operations and consist of three main components:

1. **Service Investment (Service Funding)**
2. **Office Staff Requirements and Capacity**
3. **Overtime Compensation**

All calculations are performed weekly during the airline simulation cycle in `AirlineSimulation.scala`.

---

## 1. Service Investment (Service Funding)

### Purpose
Service investment represents the cost an airline pays to maintain or improve their overall service quality level. This directly impacts passenger satisfaction and affects the computed quality of all flights operated by the airline.

### Location in Code
- Function: `AirlineSimulation.getServiceFunding(targetQuality: Int, links: List[Link])`
- File: `/airline-data/src/main/scala/com/patson/AirlineSimulation.scala` (lines 575-586)

### Calculation Formula

```scala
val getServiceFunding : (Int, Long) => Long = (targetQuality : Int, totalPassengerMileCapacity : Long) => {
  val MIN_PASSENGER_MILE_CAPACITY = 1000 * 1000
  val passengerMileCapacity = Math.max(totalPassengerMileCapacity, MIN_PASSENGER_MILE_CAPACITY).toDouble
  
  val funding = Math.pow(targetQuality.toDouble / 40, 2.5) * (passengerMileCapacity / 4000) * 30
  funding.toLong
}
```

### Step-by-Step Breakdown

1. **Calculate Total Passenger-Mile Capacity**
   - For each link: `frequency × airplane_capacity × distance`
   - Sum across all links operated by the airline
   - Apply minimum threshold of 1,000,000 passenger-miles

2. **Apply Quality Cost Formula**
   - The formula has an **exponential growth** (power of 2.5) based on target quality
   - Formula: `(targetQuality / 40)^2.5 × (passengerMileCapacity / 4000) × 30`

3. **Key Observations**
   - Cost grows **exponentially** with quality target (power of 2.5)
   - Doubling the quality target increases cost by approximately 2^2.5 = 5.66×
   - Cost scales linearly with passenger-mile capacity
   - Even with zero capacity, maintaining quality still has a base cost

### Examples from Tests

```scala
// No capacity
getServiceFunding(50, 0) == 13,101
getServiceFunding(100, 0) == 74,115  // Doubling quality ≈ 5.66× cost

// Medium airline (2000 × 2000 = 4,000,000 passenger-miles)
getServiceFunding(50, 4,000,000) == 52,407
getServiceFunding(100, 4,000,000) == 296,463

// Large airline (4 billion passenger-miles)
getServiceFunding(50, 4,000,000,000) == 52,407,843
getServiceFunding(100, 4,000,000,000) == 296,463,530
```

### Special Cases

- **Negative Balance**: If airline balance < 0, service funding is set to 0 and target quality becomes 0
- **Maximum Quality**: Quality is capped at `Airline.MAX_SERVICE_QUALITY = 100`

### How Service Quality Changes Over Time

Service quality doesn't jump immediately to the target. It adjusts gradually:

```scala
val getNewQuality : (Double, Double) => Double = (currentQuality, targetQuality) => {
  val delta = targetQuality - currentQuality
  val adjustment = 
    if (delta >= 0) { // Increasing quality - slower when already high
      MAX_SERVICE_QUALITY_INCREMENT * (1 - (currentQuality / MAX_SERVICE_QUALITY * 0.9))
      // At quality 0: multiplier = 1.0×
      // At quality 100: multiplier = 0.1×
    } else { // Decreasing quality - faster when high
      -1 * MAX_SERVICE_QUALITY_DECREMENT * (0.1 + (currentQuality / MAX_SERVICE_QUALITY * 0.9))
      // At quality 0: multiplier = 0.1×
      // At quality 100: multiplier = 1.0×
    }
  // Apply adjustment but don't overshoot target
  ...
}
```

**Constants:**
- `MAX_SERVICE_QUALITY_INCREMENT = 0.5` per week
- `MAX_SERVICE_QUALITY_DECREMENT = 10` per week

**Key Points:**
- Quality **increases slowly** when already high (diminishing returns)
- Quality **decreases quickly** when neglected, especially from high levels
- Maximum increase: 0.5 per week (at quality 0)
- Maximum decrease: 10 per week (at quality 100)

---

## 2. Office Staff Requirements

### Purpose
Each flight link requires office staff at the departure airport to handle operations like check-in, boarding, customer service, and administrative tasks. The requirements vary based on flight type, frequency, and passenger capacity.

### Location in Code
- Function: `Link.getOfficeStaffRequired()` and `Link.getOfficeStaffBreakdown()`
- File: `/airline-data/src/main/scala/com/patson/model/Link.scala` (lines 162-176)

### Calculation Formula

```scala
val getOfficeStaffBreakdown = (from : Airport, to : Airport, frequency : Int, capacity : LinkClassValues) => {
  val flightType = Computation.getFlightType(from, to)
  val airlineBaseModifier = from.getAirlineBase(airline.id).map(_.getStaffModifier(flightCategory)).getOrElse(1)
  
  if (frequency == 0) {
    StaffBreakdown(0, 0, 0, airlineBaseModifier)
  } else {
    val StaffSchemeBreakdown(basicStaff, perFrequencyStaff, per1000PaxStaff) = Link.staffScheme(flightType)
    StaffBreakdown(basicStaff, perFrequencyStaff * frequency, per1000PaxStaff * capacity.total / 1000, airlineBaseModifier)
  }
}

// Total staff = (basicStaff + frequencyStaff + capacityStaff) * modifier
```

### Staff Scheme by Flight Type

The base requirements depend on the flight type:

#### Basic Staff (per route regardless of frequency)

| Flight Type | Basic Staff |
|------------|------------|
| Short Haul Domestic | 8 |
| Medium Haul Domestic | 10 |
| Long Haul Domestic | 12 |
| Short Haul International | 10 |
| Medium Haul International | 15 |
| Long Haul International | 20 |
| Short Haul Intercontinental | 15 |
| Medium Haul Intercontinental | 25 |
| Long Haul Intercontinental | 30 |
| Ultra Long Haul Intercontinental | 30 |

#### Per-Frequency Staff Multipliers

The `perFrequency` coefficient is: `(2.0 / 5) × multiplyFactor`

| Flight Category | Multiply Factor | Per Frequency Staff |
|----------------|----------------|-------------------|
| Domestic | 2 | 0.8 |
| International | 2 | 0.8 |
| Short Haul Intercontinental | 3 | 1.2 |
| Long/Ultra Long Intercontinental | 4 | 1.6 |

#### Per 1000 Passengers Staff

Staff required per 1000 passenger capacity:

| Flight Category | Staff per 1000 Pax |
|----------------|-------------------|
| Domestic | 2 |
| International | 2 |
| Short Haul Intercontinental | 3 |
| Long/Ultra Long Intercontinental | 4 |

### Complete Formula

For each link at a departure airport:

```
Total Staff Required = (Basic + (PerFrequency × Frequency) + (Per1000Pax × Capacity / 1000)) × AirlineBaseModifier
```

### Example Calculation

**Scenario:** Long Haul International route with:
- Frequency: 7 flights/week
- Capacity: 300 passengers per flight = 2,100 total/week
- No base specialization (modifier = 1.0)

**Calculation:**
- Basic Staff: 20
- Frequency Staff: 0.8 × 7 = 5.6
- Capacity Staff: 2 × (2,100 / 1000) = 4.2
- **Total: (20 + 5.6 + 4.2) × 1.0 = 29.8 → 29 staff**

### Airline Base Modifier

Airlines can reduce staff requirements through base specializations. The modifier typically:
- Default value: 1.0 (no reduction)
- Can be reduced through base specializations for specific flight categories
- Applies multiplicatively to the total staff requirement

---

## 3. Office Staff Capacity

### Purpose
Airline bases provide office staff capacity to handle flight operations. The capacity depends on the base scale and whether it's a headquarters.

### Location in Code
- Function: `AirlineBase.getOfficeStaffCapacity(scale: Int, isHeadquarters: Boolean)`
- File: `/airline-data/src/main/scala/com/patson/model/AirlineBase.scala` (lines 131-146)

### Calculation Formula

```scala
def getOfficeStaffCapacity(scale : Int, isHeadquarters : Boolean) = {
  val base = if (isHeadquarters) 60 else 0
  val scaleBonus = if (isHeadquarters) 80 * scale else 60 * scale
  base + scaleBonus
}
```

### Capacity by Scale

| Base Scale | Headquarters Capacity | Regular Base Capacity |
|-----------|----------------------|---------------------|
| 1 | 60 + 80 = 140 | 60 |
| 2 | 60 + 160 = 220 | 120 |
| 3 | 60 + 240 = 300 | 180 |
| 4 | 60 + 320 = 380 | 240 |
| 5 | 60 + 400 = 460 | 300 |
| 10 | 60 + 800 = 860 | 600 |

### Key Observations

- **Headquarters** start with a base capacity of 60 staff (even at scale 1)
- **Regular bases** have no base capacity (start at 0)
- **Headquarters** gain 80 staff per scale level
- **Regular bases** gain 60 staff per scale level
- Capacity is **per base**, aggregated across all links departing from that airport

---

## 4. Overtime Compensation

### Purpose
When required office staff exceeds the available capacity at a base, the airline must pay overtime compensation to handle the excess workload.

### Location in Code
- Function: `AirlineBase.getOvertimeCompensation(staffRequired: Int)`
- File: `/airline-data/src/main/scala/com/patson/model/AirlineBase.scala` (lines 69-80)
- Applied in: `AirlineSimulation.scala` (lines 144-166)

### Calculation Formula

```scala
def getOvertimeCompensation(staffRequired : Int) = {
  if (getOfficeStaffCapacity >= staffRequired) {
    0
  } else {
    val delta = staffRequired - getOfficeStaffCapacity
    val income = CountrySource.loadCountryByCode(countryCode).map(_.income).getOrElse(0)
    val compensation = delta * (50000 + income) / 52 * 10
    compensation
  }
}
```

### Step-by-Step Breakdown

1. **Check if overtime is needed**
   - If `capacity >= required`: No overtime compensation (return 0)
   - Otherwise, calculate excess staff needed

2. **Calculate compensation per excess staff member**
   - Base weekly wage: (50,000 + country_income) / 52
   - Overtime multiplier: 10×
   - Formula: `excess_staff × (50,000 + country_income) / 52 × 10`

3. **Country Income Factor**
   - Higher income countries have higher labor costs
   - Base cost: $50,000/year
   - Additional: country's income level
   - Divided by 52 weeks to get weekly wage
   - Multiplied by 10 for overtime premium

### Example Calculations

**Scenario 1:** Low-income country (income = 10,000)
- Base capacity: 140 (HQ scale 1)
- Staff required: 150
- Excess staff: 10

**Calculation:**
```
Weekly wage per staff = (50,000 + 10,000) / 52 = 1,154
Overtime cost = 10 × 1,154 × 10 = 115,400 per week
```

**Scenario 2:** High-income country (income = 50,000)
- Base capacity: 140 (HQ scale 1)  
- Staff required: 200
- Excess staff: 60

**Calculation:**
```
Weekly wage per staff = (50,000 + 50,000) / 52 = 1,923
Overtime cost = 60 × 1,923 × 10 = 1,153,800 per week
```

### When Overtime Compensation is Applied

During weekly simulation (`AirlineSimulation.airlineSimulation`):

1. For each airline base:
   - Calculate total staff required from all departing links
   - Compare to base's office staff capacity
   - Calculate overtime compensation if capacity is exceeded

2. Overtime costs are:
   - Added to `OtherIncomeItemType.OVERTIME_COMPENSATION` (as negative)
   - Included in total cash expenses
   - Displayed in the airline's weekly expense report

---

## 5. Impact on Flight Quality

Service quality directly affects passenger experience through the flight's computed quality score:

```scala
// From Link.scala (lines 71-75)
val airplaneConditionQuality = ... // 20% weight
computedQualityStore = (
  rawQuality / Link.MAX_QUALITY * 30 +                                    // 30% from link quality
  airline.currentServiceQuality / Airline.MAX_SERVICE_QUALITY * 50 +      // 50% from service quality
  airplaneConditionQuality                                                 // 20% from plane condition
).toInt
```

**Service quality contributes 50% of the total flight quality score**, making it the single most important factor in passenger satisfaction.

---

## 6. Cost Optimization Strategies

### For Service Investment
1. **Scale appropriately**: Service costs scale with passenger-mile capacity
2. **Avoid over-targeting**: Each additional quality point becomes exponentially more expensive
3. **Balance is key**: Quality 50 costs 1/5.66 of quality 100 but still provides good service
4. **Plan for growth**: Quality increases slowly (max 0.5/week), so start early

### For Office Staff Costs
1. **Right-size bases**: Upgrade base scale to match staff requirements
2. **Avoid overtime**: Overtime costs 10× normal wages
3. **Consolidate operations**: Operating many flights from one base is more efficient
4. **Base specializations**: Use specializations to reduce staff requirements for specific flight types
5. **Headquarters advantage**: HQ bases have higher base capacity and better scaling

### For Overtime Compensation
1. **Monitor requirements**: Check staff requirements in advance before adding flights
2. **Upgrade proactively**: It's cheaper to upgrade bases than pay ongoing overtime
3. **Consider base location**: Low-income countries have lower overtime costs
4. **Capacity planning**: Headquarters provide better capacity scaling (80 vs 60 per level)

---

## 7. Summary of Weekly Costs

For each airline, the following costs are calculated and deducted weekly:

| Cost Component | Formula | Income Statement Category |
|---------------|---------|--------------------------|
| Service Investment | `(targetQuality/40)^2.5 × (pax-miles/4000) × 30` | Others Income - Service Investment |
| Base Upkeep | Separate calculation per base | Others Income - Base Upkeep |
| Overtime Compensation | `excess_staff × (50,000 + country_income)/52 × 10` | Others Income - Overtime Compensation |

**Total Office Service Quality Costs = Service Investment + Overtime Compensation**

Note: Base upkeep is a separate fixed cost, while service investment and overtime compensation are the variable components directly related to service quality and operational scale.

---

## 8. References

### Source Code Locations

1. **AirlineSimulation.scala**
   - `getServiceFunding()`: Lines 575-586
   - `getNewQuality()`: Lines 588-609
   - Service investment application: Lines 122-134
   - Overtime compensation calculation: Lines 144-166

2. **Link.scala**
   - `getOfficeStaffRequired()`: Line 162
   - `getOfficeStaffBreakdown()`: Lines 166-176
   - `staffScheme`: Lines 209-245
   - `StaffBreakdown`: Line 248
   - `StaffSchemeBreakdown`: Line 251

3. **AirlineBase.scala**
   - `getOfficeStaffCapacity()`: Lines 131-146
   - `getOvertimeCompensation()`: Lines 69-80

4. **Airline.scala**
   - `MAX_SERVICE_QUALITY`: Constant = 100
   - Service quality getters/setters

### Test References

1. **AirlineSimulationSpec.scala**
   - Service funding tests: Lines 8-28
   - Quality adjustment tests: Lines 30-46

---

## Version History

- **Version 1.0** (2025-11-10): Initial comprehensive documentation
