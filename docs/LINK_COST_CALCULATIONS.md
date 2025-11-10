# Link Cost Calculations Documentation

This document provides comprehensive documentation on how various costs are calculated for flight links in the Airline simulation game.

## Overview

The total cost of operating a flight link consists of several components that are calculated weekly during the simulation cycle. These costs are computed in `LinkSimulation.scala` in the `computeLinkAndLoungeConsumptionDetail` method.

## Cost Components

### 1. Fuel Cost

**Location:** `LinkSimulation.computeFuelCost()`

Fuel cost is calculated based on distance, fuel burn rate of the aircraft model, and load factor.

**Formula:**
```
Fuel Cost = Fuel Burn Calculation × FUEL_UNIT_COST × Active Flights × Load Factor Adjustment
```

**Details:**
- **FUEL_UNIT_COST:** 0.0043 (per unit)
- **Active Flights:** `frequency - cancellationCount`
- **Load Factor Adjustment:** `0.7 + 0.3 × loadFactor`
  - At 0% load factor: 70% of base fuel cost
  - At 100% load factor: 100% of base fuel cost
  - This reflects that empty planes still need fuel for the aircraft weight

**Distance-Based Fuel Burn:**

The fuel burn varies significantly based on flight distance due to ascent and descent phases:

- **Short flights (≤ 360 km):**
  - Constant high burn throughout due to continuous ascent/descent
  - Multiplier: 32× for ascent, 1× for descent

- **Medium flights (361-500 km):**
  - Two ascent phases with different burn rates
  - Phase 1 (0-180 km): 32× multiplier
  - Phase 2 (180-250 km): 13× multiplier
  - Cruise and descent: 1× multiplier

- **Long flights (501-2000 km):**
  - Three ascent phases
  - Phase 1 (0-180 km): 32× multiplier
  - Phase 2 (180-250 km): 13× multiplier
  - Phase 3 (250-1000 km): 2× multiplier
  - Cruise and descent: 1× multiplier

- **Ultra-long flights (> 2000 km):**
  - All three ascent phases (180 km + 70 km + 750 km)
  - Extended cruise phase at 1× multiplier

**Key Constants:**
```scala
MAX_ASCEND_DISTANCE_1 = 180 km
MAX_ASCEND_DISTANCE_2 = 250 km
MAX_ASCEND_DISTANCE_3 = 1000 km
ASCEND_FUEL_BURN_MULTIPLIER_1 = 32
ASCEND_FUEL_BURN_MULTIPLIER_2 = 13
ASCEND_FUEL_BURN_MULTIPLIER_3 = 2
```

---

### 2. Crew Cost

**Location:** `LinkSimulation.computeLinkAndLoungeConsumptionDetail()`

Crew cost is calculated per seat class based on capacity, flight duration, and class service level.

**Formula:**
```
Crew Cost per Class = Class Resource Multiplier × Capacity × Duration (hours) × CREW_UNIT_COST
Total Crew Cost = Sum of all classes
```

**Details:**
- **CREW_UNIT_COST:** 12 (per hour per seat)
- **Class Resource Multipliers:**
  - Economy: Lower multiplier (crew-to-passenger ratio)
  - Business: Medium multiplier
  - First Class: Higher multiplier
- **Duration:** Converted from minutes to hours (`duration / 60`)

**Calculation per class:**
```scala
crewCost += (linkClass.resourceMultiplier × capacity × duration / 60 × 12).toInt
```

---

### 3. Airport Fees

**Location:** `LinkSimulation.computeLinkAndLoungeConsumptionDetail()` and `Airport.scala`

Airport fees consist of two components: slot fees and landing fees, charged at both departure and arrival airports.

**Formula:**
```
Airport Fees = (FromAirport.slotFee + ToAirport.slotFee + 
                FromAirport.landingFee + ToAirport.landingFee) × Frequency
```

#### 3.1 Slot Fee

**Location:** `Airport.slotFee()`

Slot fees vary by airport size and aircraft type, with discounts for airline bases.

**Base Slot Fee by Airport Size:**
- Size 1 (Small): $50
- Size 2 (Medium): $50
- Size 3 (Large): $80
- Size 4 (International): $150
- Size 5: $250
- Size 6: $350
- Size 7+ (Mega): $500

**Aircraft Type Multipliers:**
- Light: 1×
- Small: 1×
- Regional: 3×
- Medium: 8×
- Large: 12×
- Extra Large: 15×
- Jumbo: 18×
- Supersonic: 12×

**Base Discounts:**
- Headquarters: 50% discount
- Regular Base: 20% discount
- No Base: No discount

**Final Formula:**
```
Slot Fee = Base Slot Fee × Aircraft Multiplier × Discount Factor
```

#### 3.2 Landing Fee

**Location:** `Airport.landingFee()`

Landing fees are capacity-based and vary by airport size.

**Per-Seat Fee by Airport Size:**
- Size 1-3: $3 per seat
- Size 4+: $size per seat (e.g., Size 5 = $5/seat)

**Formula:**
```
Landing Fee = Aircraft Capacity × Per-Seat Fee
```

---

### 4. Depreciation

**Location:** `LinkSimulation.computeLinkAndLoungeConsumptionDetail()`

Depreciation represents the weekly value loss of aircraft assigned to the link.

**Formula:**
```
Depreciation = Sum(Airplane.depreciationRate × Assignment Weight)
```

**Details:**
- **depreciationRate:** Stored per airplane, represents weekly value decrease
- **Assignment Weight:** Proportion of airplane's time spent on this link (0 to 1)
  - Calculated as: `flightMinutes on this link / total flightMinutes for airplane`
- If an airplane is assigned to multiple links, depreciation is proportionally split

**Example:**
- Airplane depreciation rate: $10,000/week
- Assigned to 2 links: Link A (60% of time), Link B (40% of time)
- Link A depreciation: $10,000 × 0.6 = $6,000
- Link B depreciation: $10,000 × 0.4 = $4,000

---

### 5. Delay Compensation

**Location:** `Computation.computeCompensation()` in `Computation.scala`

Compensation paid to passengers for delays and cancellations.

**Formula:**
```
Compensation = Cancellation Comp + Major Delay Comp + Minor Delay Comp
```

**Base Calculation:**
```
Affected Seats = max(Sold Seats per Flight, 50% of Capacity per Flight)

Cancellation Compensation = Affected Seats × Cancellation Count × 0.5 × Ticket Price
Major Delay Compensation   = Affected Seats × Major Delay Count × 0.3 × Ticket Price
Minor Delay Compensation   = Affected Seats × Minor Delay Count × 0.05 × Ticket Price
```

**Compensation Rates:**
- Cancellation: 50% of ticket price
- Major Delay: 30% of ticket price
- Minor Delay: 5% of ticket price

**Service Quality Adjustment:**

Airlines with low service quality pay reduced compensation:

```
if Service Quality < 40:
    Adjustment Ratio = 0.2 + 0.8 × (Service Quality / 40)
    Final Compensation = Base Compensation × Adjustment Ratio
```

- Service Quality 0: Pay only 20% of base compensation
- Service Quality 40+: Pay full compensation

**Delay Probability:**

Delays and cancellations are simulated based on aircraft condition:

```
Condition Multiplier = (MAX_CONDITION - Current Condition) / MAX_CONDITION
```

**Normal Condition (> 20% condition):**
- Cancellation: 3% × condition multiplier
- Major Delay: 10% × condition multiplier
- Minor Delay: 30% × condition multiplier

**Critical Condition (≤ 20% condition):**
- Cancellation: 5% × condition multiplier
- Major Delay: 20% × condition multiplier
- Minor Delay: 50% × condition multiplier

---

### 6. Lounge Cost

**Location:** `LinkSimulation.computeLinkAndLoungeConsumptionDetail()`

Lounge cost is charged when premium passengers use lounges at departure or arrival airports.

**Formula:**
```
Lounge Cost per Airport = Premium Passengers × PER_VISITOR_CHARGE
Premium Passengers = Business Class Sold + First Class Sold
Total Lounge Cost = From Airport Lounge + To Airport Lounge
```

**Details:**
- **PER_VISITOR_CHARGE:** $100 per premium passenger
- Applies to both airline's own lounges and alliance partner lounges
- Only active lounges are counted
- Cost is charged to the operating airline, revenue goes to lounge owner
- If lounge belongs to same airline, it's internal cost/revenue
- If lounge belongs to alliance partner, it's a payment between airlines

**Example:**
- Flight with 50 Business + 10 First Class passengers = 60 premium passengers
- Lounges at both airports
- Lounge cost = 60 × $100 × 2 airports = $12,000

---

### 7. Service Supplies (Inflight Cost)

**Location:** `LinkSimulation.computeInflightCost()`

Inflight cost represents the cost of meals, entertainment, and amenities provided during the flight.

**Formula:**
```
Cost per Passenger = BASE_INFLIGHT_COST + (Duration Cost per Hour × Flight Duration in Hours)
Inflight Cost = Cost per Passenger × Sold Seats × 2
```

**Details:**
- **BASE_INFLIGHT_COST:** $20 per passenger
- **Multiplier:** ×2 for roundtrip
- **Duration Cost varies by Link Quality (Star Rating):**

**Star Rating to Duration Cost Mapping:**
| Stars | Quality Range | Duration Cost/Hour |
|-------|---------------|-------------------|
| ★     | 1-20         | $1                |
| ★★    | 21-40        | $4                |
| ★★★   | 41-60        | $8                |
| ★★★★  | 61-80        | $13               |
| ★★★★★ | 81-100       | $20               |

**Example:**
- 4-star service (rawQuality = 75)
- Flight duration: 180 minutes (3 hours)
- 100 sold seats
- Cost per passenger = $20 + ($13 × 3) = $59
- Total inflight cost = $59 × 100 × 2 = $11,800

**Rationale:**
- Base cost covers minimal service regardless of duration
- Higher quality services require more expensive amenities
- Duration cost reflects that longer flights require more meals/refreshments
- Roundtrip multiplier accounts for return journey supplies

---

### 8. Maintenance Cost

**Location:** `LinkSimulation.computeLinkAndLoungeConsumptionDetail()`

Maintenance cost is the weekly maintenance expense for aircraft assigned to the link.

**Formula:**
```
Base Maintenance per Airplane = Model.baseMaintenanceCost × Assignment Weight × 
                                 Airline Maintenance Quality / MAX_MAINTENANCE_QUALITY

Adjusted Maintenance = Base Maintenance × Maintenance Factor

Total Maintenance = Sum of all assigned airplanes
```

**Details:**
- **baseMaintenanceCost (Model):** `capacity × 150`
  - Example: 200-seat plane = $30,000 base weekly maintenance
  
- **Assignment Weight:** Proportion of airplane time on this link (0 to 1)

- **Maintenance Quality Factor:**
  - Airlines can set maintenance quality from 0 to MAX (typically 100)
  - Higher quality = higher cost but better aircraft condition
  - Formula: `cost × quality / MAX_MAINTENANCE_QUALITY`
  - At 100% quality: Full base cost
  - At 50% quality: Half base cost

- **Maintenance Factor (Regional adjustment):**
  - `AirplaneMaintenanceUtil.getMaintenanceFactor(airlineId)`
  - Adjusts costs based on airline's maintenance facilities/agreements
  - Typically near 1.0, can vary by airline

**Example:**
- 300-seat Boeing 777: Base = 300 × 150 = $45,000/week
- Airline maintenance quality: 80/100
- Assignment weight: 0.7 (70% utilization on this link)
- Maintenance factor: 1.0
- Cost = $45,000 × 0.7 × 0.8 × 1.0 = $25,200/week

**Impact of Maintenance Quality:**

Lower maintenance quality reduces costs but increases risks:
- Aircraft condition deteriorates faster
- Higher chance of delays and cancellations
- Potential safety issues and reputation damage

---

## Profit Calculation

The final profit for a link is calculated as:

```
Profit = Revenue - Fuel Cost - Crew Cost - Airport Fees - Inflight Cost - 
         Delay Compensation - Maintenance Cost - Depreciation - Lounge Cost
```

Where:
- **Revenue** = Sum of (Sold Seats × Ticket Price) for each class

---

## Cost Optimization Strategies

### 1. Fuel Cost Optimization
- Use fuel-efficient aircraft models
- Maintain high load factors (aim for 80%+)
- Consider distance when selecting aircraft (fuel burn varies by flight phase)
- Short routes have proportionally higher fuel costs due to ascent/descent

### 2. Crew Cost Optimization
- Match aircraft capacity to demand (don't use oversized planes)
- Higher-capacity planes have lower per-passenger crew costs
- Consider flight duration impact on crew costs

### 3. Airport Fee Optimization
- Establish headquarters for 50% slot fee discount
- Establish bases for 20% slot fee discount
- Use smaller aircraft at mega airports if slot fees are prohibitive
- Landing fees are capacity-based, so right-sizing matters

### 4. Depreciation Management
- Use newer aircraft with slower depreciation rates
- Maximize aircraft utilization across multiple profitable links
- Consider aircraft age when planning long-term routes

### 5. Delay Compensation Minimization
- Maintain aircraft condition above 40% (CRITICAL threshold = 20%)
- Invest in higher maintenance quality
- Monitor aircraft age and condition
- Budget for 2-5% compensation costs on average routes

### 6. Lounge Cost Management
- Lounge costs are fixed per premium passenger
- Consider premium cabin mix carefully
- Own lounges generate revenue from other airlines
- Alliance lounges share costs but extend network value

### 7. Inflight Cost Optimization
- Balance service quality with cost (higher stars = higher cost)
- Inflight costs scale with duration and passengers
- Don't over-invest in service quality for price-sensitive routes
- Short flights have proportionally lower inflight costs

### 8. Maintenance Cost Strategy
- Balance maintenance quality with cost and reliability
- Higher quality (80-100%) recommended for long-haul and premium routes
- Medium quality (60-80%) acceptable for short-haul routes
- Never drop below 50% quality due to safety and reliability risks
- Maintenance costs are proportional to aircraft size

---

## Simulation Implementation Notes

### Frequency and Timing
- All costs are calculated per weekly cycle
- Frequency indicates flights per week
- Cancellations reduce frequency for fuel cost but generate compensation costs

### Load Factor Impact
- Load factor affects fuel cost calculation (70% at 0 LF, 100% at full LF)
- Load factor is calculated as: `Total Sold Seats / Total Capacity`
- Higher load factor improves profitability significantly

### Aircraft Assignment
- Aircraft can be assigned to multiple links
- Costs (depreciation, maintenance) are split proportionally by time spent
- Assignment weight = flight minutes on link / total flight minutes for aircraft

### Quality and Satisfaction
- Service quality affects delay compensation rates
- Passenger satisfaction is tracked separately from costs
- Low-cost strategies may reduce passenger satisfaction

---

## Related Code Files

- **LinkSimulation.scala**: Main cost calculation logic
- **Computation.scala**: Compensation and utility calculations
- **Airport.scala**: Airport fee calculations (slot and landing fees)
- **Model.scala**: Aircraft model specifications (maintenance cost, fuel burn)
- **Airplane.scala**: Individual aircraft properties (depreciation rate)
- **Lounge.scala**: Lounge pricing constants

---

## Version Information

This documentation is based on the codebase as of version 2.1.

For questions or updates, please refer to the source code or submit an issue on the project repository.
