# Transfer Costs and Player Strategy: Comprehensive Analysis

## Executive Summary

This document provides an extensive analysis of how transfer costs affect player strategy in the Airline simulation game. Transfer costs (also known as connection costs) are a critical mechanism that influences passenger route selection when connecting flights are involved. Understanding these mechanics is essential for players to optimize their network design, pricing strategies, and competitive positioning.

**Key Finding**: Transfer costs create significant strategic depth by penalizing connections while rewarding direct flights, high-frequency service, airline loyalty/alliances, and airport infrastructure investments. Players must balance network connectivity against the economic penalties of layovers to maximize passenger uptake and profitability.

---

## Table of Contents

1. [Overview of Transfer Cost Mechanics](#overview-of-transfer-cost-mechanics)
2. [Base Connection Cost Components](#base-connection-cost-components)
3. [Passenger Preference Variations](#passenger-preference-variations)
4. [Airport Infrastructure Impact](#airport-infrastructure-impact)
5. [Alliance and Airline Effects](#alliance-and-airline-effects)
6. [Route Selection Algorithm](#route-selection-algorithm)
7. [Strategic Implications for Players](#strategic-implications-for-players)
8. [Code Architecture and Implementation](#code-architecture-and-implementation)
9. [Examples and Case Studies](#examples-and-case-studies)
10. [Recommendations](#recommendations)

---

## Overview of Transfer Cost Mechanics

### What Are Transfer Costs?

Transfer costs (connection costs) are additional perceived costs added to a route when passengers must change flights at intermediate airports. These costs do not represent actual ticket prices but rather the passenger's perceived inconvenience, time, and discomfort associated with making connections.

### Where They Apply

Transfer costs are calculated during the **route-finding algorithm** in `PassengerSimulation.scala`, specifically in the `findShortestRoute` method (lines 560-721). The algorithm uses a modified Dijkstra's shortest path algorithm where the "cost" of a route includes:

1. The base ticket price of each flight segment
2. Quality adjustments based on airline service
3. **Connection costs at transfer airports**
4. Frequency penalties for infrequent service
5. Asset discounts for airport facilities

### Core Implementation Location

The primary calculation occurs in `PassengerSimulation.scala` at lines 615-651:

```scala
var connectionCost = 0.0
if (predecessorLinkConsideration != null) { // Connection flight detected
  // Base cost, frequency penalties, airline switching penalties calculated here
  connectionCost = 25 // Base connection cost
  // ... additional calculations
  connectionCost *= passengerGroup.preference.connectionCostRatio
}
```

---

## Base Connection Cost Components

### 1. Base Connection Cost (Line 630)

**Value**: 25 cost units (baseline)

Every connection incurs a minimum base cost of 25, representing the fundamental inconvenience of changing planes. This applies to all flight-to-flight connections regardless of airlines or frequency.

**Special Case - Generic Transit**: When connecting through ground transportation (generic transit), the base cost is also 25 (line 628). This treats ground connections similarly to flight connections.

### 2. Frequency-Based Wait Time Penalty (Lines 631-636)

**Formula**:
```scala
val frequency = Math.max(predecessorLink.frequency, currentLink.frequency)
if (frequency < 42) {
  connectionCost += (3.5 * 24 * 5) / frequency  // = 420 / frequency
}
```

**Explanation**:
- **Frequency Threshold**: 42 flights per week (approximately 6 per day)
- **Rationale**: If either the arriving or departing flight has frequency < 42, passengers face longer wait times
- **Penalty Calculation**: `420 / frequency`
  - At frequency 1: penalty = 420 (extremely punitive)
  - At frequency 7: penalty = 60
  - At frequency 14: penalty = 30
  - At frequency 21: penalty = 20
  - At frequency 42+: penalty = 0

**Interpretation**: Each unit represents roughly $1 in perceived cost. The formula simulates wait time where `(3.5 * 24) = 84 hours` of potential wait per week, divided by frequency, multiplied by $5/hour perceived cost.

**Strategic Impact**: Players are heavily incentivized to maintain high frequency on hub routes to minimize connection penalties. Low-frequency connections are severely penalized.

### 3. Airline Switching Penalty (Lines 638-640)

**Value**: 75 cost units

**Conditions for Application**:
- Previous flight and current flight are from different airlines, AND
- Airlines are not in the same alliance (or either has no alliance)

**Code**:
```scala
if (previousLinkAirlineId != currentLinkAirlineId && 
    (allianceIdByAirlineId.get(previousLinkAirlineId) == null || 
     allianceIdByAirlineId.get(previousLinkAirlineId) != allianceIdByAirlineId.get(currentLinkAirlineId))) {
  connectionCost += 75
}
```

**Interpretation**: 
- Represents the hassle of:
  - Collecting and re-checking baggage
  - Different check-in procedures
  - Terminal changes
  - Reduced coordination between carriers
  - Loss of loyalty benefits

**Strategic Impact**: 
- **Massive advantage for single-airline networks** or **alliance partners**
- Players should prioritize building hub-and-spoke networks under single airline control
- Joining alliances becomes extremely valuable for enabling seamless connections

---

## Passenger Preference Variations

Different passenger types have vastly different tolerances for connections, controlled by the `connectionCostRatio` parameter in `FlightPreference.scala`.

### Base Connection Cost Ratio (Line 163)

**Default Value**: 1.0 (100% of calculated connection cost applies)

### Passenger Type Multipliers

#### 1. Simple/Carefree Preference (Line 281)

**connectionCostRatio**: 0.5

**Characteristics**:
- Applies to budget-conscious and simple preference passengers
- **50% discount on all connection costs**
- These passengers are more tolerant of connections
- Willing to take inconvenient routes to save money

**Example**:
- Base connection: 25 → 12.5
- Frequency penalty (freq=14): 30 → 15
- Airline switch: 75 → 37.5
- Total connection cost: 130 → 65

#### 2. Speed Preference (Line 309)

**connectionCostRatio**: 2.0

**Characteristics**:
- Applies to business travelers and time-sensitive passengers
- **200% connection cost (double penalty)**
- Strongly prefer direct flights
- Will pay significantly more to avoid connections

**Example**:
- Base connection: 25 → 50
- Frequency penalty (freq=14): 30 → 60
- Airline switch: 75 → 150
- Total connection cost: 130 → 260

#### 3. Appeal/Loyal/Elite Preferences (Implicit Default)

**connectionCostRatio**: 1.0

**Characteristics**:
- Standard connection cost tolerance
- Balanced between price and convenience
- Includes loyalty-conscious passengers

### Link Class Multiplier (Line 643)

Connection costs are further multiplied by the passenger's preferred link class price multiplier:

```scala
connectionCost *= passengerGroup.preference.preferredLinkClass.priceMultiplier
```

**Link Class Multipliers** (from `Link.scala` lines 322-330):
- **Economy**: 1.0x
- **Business**: 3.0x  
- **First**: 9.0x

**Combined Effect Example** (Speed preference + Business class):
- Base calculation: 130 cost units
- After preference ratio: 130 × 2.0 = 260
- After class multiplier: 260 × 3.0 = **780 cost units**

This means a Business class speed-preference passenger sees connection costs nearly **20x higher** than an Economy simple-preference passenger (780 vs 42.25).

---

## Airport Infrastructure Impact

Airports can build infrastructure that reduces connection costs through two mechanisms:

### 1. Transit Wait Time Modifiers (Lines 645-651)

Certain airport assets implement the `TransitWaitTimeModifier` trait and provide discounts on connection costs during flight transits.

#### Airport Hotel Asset

**Implementation** (`AirportAsset.scala` lines 728-738):

```scala
case class AirportHotelAsset extends HotelAsset with TransitWaitTimeModifier {
  override def computeTransitFreqDiscount(arrivalLinkFreq, departureLinkFreq, paxGroup) = {
    var discountPercentage = 0.1 + level * 1.0 / MAX_LEVEL * 0.1  // 10-20%
    
    if (paxGroup.preference.preferredLinkClass.level >= BUSINESS.level) {
      discountPercentage += (level * 1.0 / MAX_LEVEL) * 0.2  // Extra 0-20%
    }
    discountPercentage
  }
}
```

**Discount Ranges**:
- **Economy passengers**: 10% (level 1) to 20% (max level)
- **Business/First passengers**: 10% (level 1) to 40% (max level)

**Calculation** (`PassengerSimulation.scala` lines 645-651):
```scala
val waitTimeDiscount = Math.min(0.5, airport.computeTransitDiscount(...))
connectionCost *= (1 - waitTimeDiscount)  // Max 50% reduction
```

**Maximum Discount**: Capped at 50% of connection cost

**Example**:
- Original connection cost: 200
- Max-level airport hotel (40% discount)
- Actual discount applied: 40% (not capped)
- Final cost: 200 × (1 - 0.40) = **120**

#### Strategic Value for Players

**Hub Development**: 
- Players operating major connection hubs should prioritize building Airport Hotels
- Maximum benefit for premium cabin passengers (Business/First)
- Becomes competitive necessity in hub-vs-hub competition

**ROI Considerations**:
- Cost: $200M base (scales with airport income/population)
- Construction: 2 × 52 = 104 weeks (~2 years)
- Benefit: Increases passenger uptake on connection routes by reducing perceived cost
- Most valuable at high-frequency hubs serving premium passengers

### 2. Passenger Cost Asset Modifiers (Lines 660-666)

Separate from connection-specific benefits, certain assets provide general cost reductions for passengers stopping over:

#### Assets with PassengerCostAssetModifier Trait

**Examples** (from `AirportAsset.scala`):

1. **Museum** (lines 628-634)
   - Tourist discount: 12%
   - Business discount: 5%
   - Probability: 30%

2. **Convention Center** (lines 623-627)
   - Tourist discount: 8%
   - Business discount: 15%
   - Probability: 25%

3. **Amusement Park** (lines 652-658)
   - Tourist discount: 15%
   - Business discount: 3%
   - Probability: 30%

4. **Beach Resort / Ski Resort** (lines 617-622, 611-616)
   - Tourist discount: 20%
   - Business discount: 10%
   - Probability: 40%

5. **Stadium** (lines 671-677)
   - Tourist discount: 10%
   - Business discount: 8%
   - Probability: 25%

6. **Landmark** (lines 681-687)
   - Tourist discount: 15%
   - Business discount: 12%
   - Probability: 35%

7. **Golf Course** (lines 701-707)
   - Tourist discount: 7%
   - Business discount: 12%
   - Probability: 20%

#### How These Work

**Probabilistic Application** (`AirportAsset.scala` lines 380-388):

```scala
if (ThreadLocalRandom.current().nextInt(100) < 
    Math.max(1, probability * level / MAX_LEVEL)) {
  Some(computeAssetDiscount(paxGroup))
}
```

**Discount Calculation** (lines 391-397):
```scala
if (paxGroup.passengerType == PassengerType.BUSINESS) {
  businessDiscount * (0.5 + 0.5 * level / MAX_LEVEL)
} else {
  touristDiscount * (0.5 + 0.5 * level / MAX_LEVEL)
}
```

**Application in Route Finding** (`PassengerSimulation.scala` lines 660-666):
```scala
assetDiscountByAirportId.getOrElseUpdate(linkConsideration.to.id, 
  linkConsideration.to.computePassengerCostAssetDiscount(linkConsideration, passengerGroup))
  .foreach { case(discount, _) =>
    val costDiscount = Math.min(cost * 0.5, newCost * discount)
    newCost -= costDiscount
  }
```

**Scaling by Level**:
- Level 1: Discount is 50% of stated value
- Max level (10): Discount is 100% of stated value

**Example - Beach Resort Max Level**:
- Tourist discount: 20%
- Business discount: 10%
- Probability of applying: 40%
- When triggered, reduces total route cost by up to 20% (tourist) or 10% (business)
- Capped at 50% of individual segment cost

#### Strategic Implications

**Stopover Hub Strategy**:
- Building entertainment/tourism assets makes your airport attractive as a stopover point
- Passengers perceive routes through your hub as cheaper
- Particularly effective for tourist destinations
- Creates "magnetic" hubs that draw traffic even on longer routes

**Not Specific to Connections**:
- These discounts apply to the destination airport, not specifically to connections
- However, they make multi-stop routes more competitive against direct routes
- Most valuable for tourist/leisure passengers (not applicable to Speed preference passengers per line 381)

---

## Alliance and Airline Effects

### Alliance Connection Benefits

**Code Implementation** (`PassengerSimulation.scala` lines 638-640):

```scala
if (previousLinkAirlineId != currentLinkAirlineId && 
    (allianceIdByAirlineId.get(previousLinkAirlineId) == null || 
     allianceIdByAirlineId.get(previousLinkAirlineId) != allianceIdByAirlineId.get(currentLinkAirlineId))) {
  connectionCost += 75
}
```

**Note**: The actual code uses Java's `HashMap` where `null` is a valid value. The `.asInstanceOf[Int]` cast is used in the original Scala code to handle Java null semantics.

**Logic**:
- If airlines are different BUT belong to the same **established alliance**, the 75-point airline switching penalty is waived
- Alliance must be in "ESTABLISHED" status (loaded at lines 63-70)
- Members must not be in APPLICANT role

**Economic Value of Alliances**:

Consider a 2-leg connection route:
- Base connection cost: 25
- Frequency adequate (no penalty): 0
- **Without alliance**: +75 airline switch penalty = 100 total
- **With alliance**: 0 airline switch penalty = 25 total

**Savings**: 75 cost units per connection (300% reduction in connection cost)

For a Speed preference Business class passenger:
- Connection cost multiplier: 2.0 × 3.0 = 6.0
- Savings: 75 × 6.0 = **450 cost units per connection**

**Strategic Value**:
- Alliances become extremely valuable for hub operators
- Enables "virtual single-airline network" for passengers
- Competitive necessity for airlines without comprehensive route networks
- Critical for competing against larger carriers with direct flights

### Single-Airline Network Advantage

**No Penalty**: When all flights in a connection are operated by the same airline, no switching penalty applies regardless of alliance membership.

**Strategic Advantage**:
- Encourages hub-and-spoke network design
- Justifies operating thin routes from hubs (feeder routes)
- Makes airline mergers/acquisitions valuable
- Rewards network planning and consistency

---

## Route Selection Algorithm

### Algorithm Overview

The route selection uses a **modified Dijkstra's shortest path algorithm** with weighted costs instead of simple distances.

**Location**: `PassengerSimulation.scala`, `findShortestRoute` method (lines 560-721)

### Cost Components in Priority Order

For each route segment, the total cost is calculated as:

```
Total Segment Cost = Base Link Cost + Connection Cost (if applicable) - Asset Discounts
```

Where:
- **Base Link Cost** = Passenger preference-adjusted ticket price (accounts for quality, price, loyalty, frequency, flight duration)
- **Connection Cost** = Calculated only at connection points (not origin/destination)
- **Asset Discounts** = Applied at destination airports

### Algorithm Iteration Strategy

**Iteration Count** (lines 113-116):
```scala
val iterationCount = 
  if (consumptionCycleCount < 3) 4
  else if (consumptionCycleCount < 6) 5
  else 6
```

**Purpose**: Multiple iterations of Dijkstra's algorithm allow finding routes through progressively more hops. Early cycles find short routes, later cycles find longer alternative routes as capacity is consumed.

**Max Hops**: 
- First 3 consumption cycles: 4 hops maximum
- Cycles 3-6: 5 hops maximum
- After cycle 6: 6 hops maximum

### Route Validation and Rejection

After finding a route, it must pass validation checks (`getRouteRejection`, lines 268-305):

#### 1. Distance Tolerance (Lines 273-275)

```scala
val routeDistance = route.links.foldLeft(0)(_ + _.link.distance)
if (routeDistance > routeDisplacement * ROUTE_DISTANCE_TOLERANCE_FACTOR) {
  return Some(DISTANCE)  // Rejected
}
```

**ROUTE_DISTANCE_TOLERANCE_FACTOR**: 2.5 (line 259)

**Meaning**: The actual flown distance cannot exceed 2.5× the great circle distance between origin and destination.

**Example**:
- Origin to destination: 1000 km direct
- Maximum acceptable route: 2500 km
- A route with 2-3 connections totaling 2600 km would be rejected

**Strategic Implication**: Extremely circuitous routes are automatically rejected, limiting the value of poorly planned hub networks.

#### 2. Total Cost Affordability (Lines 284-288)

```scala
val routeAffordableCost = Pricing.computeStandardPrice(routeDisplacement, flightType, linkClass) 
                          * ROUTE_COST_TOLERANCE_FACTOR * incomeAdjustedFactor
if (route.totalCost > routeAffordableCost) {
  return Some(TOTAL_COST)  // Rejected
}
```

**ROUTE_COST_TOLERANCE_FACTOR**: 1.5 (line 256)

**Income Adjustment** (lines 277-282):
```scala
val incomeAdjustedFactor = 
  if (fromAirport.income < Country.LOW_INCOME_THRESHOLD) {
    1 - (Country.LOW_INCOME_THRESHOLD - fromAirport.income) / LOW_INCOME_THRESHOLD * 0.2
  } else { 1 }
```

**Meaning**: 
- Total route cost cannot exceed 1.5× the standard price for a direct flight
- Lower-income airports have up to 20% reduced tolerance (0.8× to 1.0×)

**Example**:
- Direct flight standard price: $1000
- Economy passengers from wealthy airport: reject routes costing > $1500
- Economy passengers from poor airport: reject routes costing > $1200

**Strategic Implication**: 
- Connection routes must remain price-competitive with direct alternatives
- High connection costs can make connecting routes unviable
- Low-income markets are particularly sensitive to connection penalties

#### 3. Individual Link Affordability (Lines 291-302)

```scala
val linkAffordableCost = link.standardPrice(preferredLinkClass) 
                        * LINK_COST_TOLERANCE_FACTOR * incomeAdjustedFactor
if (linkConsideration.cost > linkAffordableCost) {
  return Some(LINK_COST)  // Rejected
}
```

**LINK_COST_TOLERANCE_FACTOR**: 0.9 (line 257)

**Meaning**: Each individual segment cannot exceed 0.9× the standard price for that segment distance (slightly more strict than direct flights).

**Strategic Implication**: Even if total cost is acceptable, individual overpriced segments will cause rejection. Players cannot hide expensive connections within cheap overall routes.

---

## Strategic Implications for Players

### 1. Direct vs. Connecting Flight Strategy

**Direct Flight Advantages**:
- Zero connection costs (baseline advantage of 25-150+ cost units per connection)
- No frequency penalties
- No airline switching issues
- Simpler operations

**Connecting Flight Advantages**:
- Can serve thin markets without dedicated aircraft
- Leverages hub capacity more efficiently
- Can offer more destination pairs with fewer routes

**Break-Even Analysis**:

For a connection to be competitive with a direct flight:
```
Direct Flight Cost ≥ Connection Flight Cost + Connection Penalties
```

**Example Scenario**:
- Direct flight: 3000 km, $1500 standard price
- Via hub: 1500 km + 1500 km, $750 + $750 = $1500 base
- Connection penalties: 25 (base) + 30 (frequency) + 0 (same airline) = 55
- Passenger type: Economy standard (1.0× ratio, 1.0× class)
- **Connection total perceived cost**: $1555

**Conclusion**: Connection is 3.7% more expensive; passengers will prefer direct flight.

**To Make Connection Competitive**:
- Increase direct flight price to $1600+ OR
- Reduce connection flight prices to $700 each OR
- Improve frequency to eliminate 30-point penalty OR
- Build airport hotel to reduce connection cost by 20% (11 points)

### 2. Hub Design Strategy

**Optimal Hub Characteristics**:

1. **High Frequency on All Spokes**
   - Target: 42+ flights/week (6/day) on all routes
   - Below this threshold, massive penalties apply
   - Justifies smaller aircraft on frequent schedules over large aircraft on infrequent schedules

2. **Single-Airline or Alliance-Based**
   - Eliminate the 75-point airline switching penalty
   - Critical for competitiveness
   - If multi-airline, ensure all are in same alliance

3. **Airport Infrastructure Investment**
   - **Must-Build**: Airport Hotel (especially for premium traffic)
   - **High Value**: Tourist attractions (Museum, Landmark) for leisure hubs
   - **Business Hubs**: Convention Center for business traffic

4. **Geographic Positioning**
   - Keep total route distance < 2.5× direct distance
   - Minimize circuitousness
   - Position hubs to serve multiple regions efficiently

5. **Passenger Type Focus**
   - **Budget/Leisure Hubs**: Can tolerate lower frequency, benefit from Simple preference (0.5× connection cost)
   - **Business Hubs**: Must have very high frequency and infrastructure, face Speed preference (2.0× connection cost)

### 3. Network Planning Considerations

**Hub-and-Spoke vs. Point-to-Point**:

**Hub-and-Spoke Advantages**:
- Fewer routes to manage
- Higher frequency possible on each route
- Better aircraft utilization
- Can serve more city pairs

**Hub-and-Spoke Disadvantages**:
- Connection penalties reduce competitiveness
- Requires significant infrastructure investment
- Vulnerable to disruption
- Higher operational complexity

**Point-to-Point Advantages**:
- No connection penalties
- Simpler operations
- More attractive to time-sensitive passengers

**Point-to-Point Disadvantages**:
- Many thin routes with low frequency
- Poor aircraft utilization
- Cannot serve as many markets profitably

**Optimal Strategy**: Hybrid approach
- Direct flights on dense routes
- Hub connections for thin routes
- Focus hub on passengers tolerant of connections (leisure travelers)

### 4. Competitive Positioning

**Against Larger Competitors with Direct Flights**:

**Your Disadvantages**:
- Connection penalty of 25+ points per stop
- Possible frequency penalties if routes are thin
- Complexity penalty

**Your Advantages**:
- Can price more aggressively (lower total ticket price)
- Can serve more destinations from hub
- Loyalty programs can reduce perceived costs
- Airport infrastructure investments pay dividends

**Required Cost Advantage**: 
- Must price connecting routes at least 5-10% below direct competitors to overcome connection penalties
- Higher for Business class (requires 15-20% discount)

**Against Other Hub Operators**:

**Key Competitive Factors**:
1. **Frequency**: Higher frequency dramatically reduces penalties
2. **Infrastructure**: Airport hotels and attractions provide 10-40% cost advantages
3. **Alliance Membership**: Eliminates 75-point penalty when connecting with partners
4. **Geographic Position**: More central hubs have lower distance penalties
5. **Single-Airline Control**: Eliminates switching penalty

### 5. Pricing Strategy

**Connection Route Pricing**:

**Recommended Approach**:
```
Connection Route Price = Direct Route Price - Connection Penalties - Competitive Margin
```

**Example Calculation**:
- Direct competitor price: $1500
- Your connection penalties: 55 (base + frequency)
- Target competitive margin: 5% ($75)
- **Your price**: $1500 - $55 - $75 = **$1370**

**This provides**:
- Equal perceived cost to passengers
- 5% revenue sacrifice for connection disadvantage
- Competitive positioning

**Premium Segment Pricing**:

For Business/First class on connections:
- Connection penalties can be 6-9× higher (class multiplier)
- May need to discount 15-25% below direct flights
- Infrastructure investments become critical for profitability

### 6. Alliance Strategy

**Value Calculation**:

**Per Connection Savings**: 75 base points
**For Business Class Speed Passengers**: 75 × 2.0 × 3.0 = **450 points**

**Economic Impact**:
- If you operate 10,000 connecting passengers/week
- Average 1.5 connections per passenger
- Without alliance: 15,000 connection instances with +75 penalty
- **Total penalty**: 1,125,000 cost units
- **Revenue impact**: Passengers reject routes or require deep discounts

**With alliance**: Penalty eliminated, routes become viable without discounting

**ROI on Alliance Membership**:
- Joining cost: Varies by game mechanics
- Benefit: Massive increase in connection route viability
- **Critical** for hub operators
- Less important for point-to-point carriers

### 7. Market Segmentation

**Target Passenger Types for Connection Routes**:

1. **Best**: Simple Preference (Budget Travelers)
   - 50% connection cost discount
   - Price sensitive, willing to connect
   - Leisure travelers

2. **Good**: Standard Preferences
   - Normal connection costs
   - Balanced approach
   - Most passenger types

3. **Difficult**: Speed Preference (Business Travelers)
   - 200% connection penalty
   - Require excellent frequency and infrastructure
   - Premium pricing opportunity but high competition

**Market Strategy**:
- **Leisure/Tourist Hubs**: Focus on Simple preference passengers, build tourist attractions
- **Business Hubs**: Must invest heavily in frequency + infrastructure, target loyal passengers
- **Mixed Hubs**: Segment by time of day/day of week to optimize for different passenger types

---

## Code Architecture and Implementation

### Key Files and Their Roles

#### 1. PassengerSimulation.scala

**Location**: `/airline-data/src/main/scala/com/patson/PassengerSimulation.scala`

**Primary Functions**:

**`findShortestRoute`** (lines 560-721):
- Implements modified Dijkstra's algorithm
- Calculates connection costs
- Applies frequency penalties
- Checks alliance membership
- Integrates airport asset discounts

**`getRouteRejection`** (lines 268-305):
- Validates route distance tolerance
- Validates total cost affordability
- Validates individual link costs
- Returns rejection reasons or None

**`passengerConsume`** (lines 30-254):
- Main passenger simulation entry point
- Orchestrates route finding across all passenger groups
- Handles capacity constraints
- Manages multiple consumption cycles

#### 2. FlightPreference.scala

**Location**: `/airline-data/src/main/scala/com/patson/model/FlightPreference.scala`

**Key Classes**:

**`SimplePreference`** (lines 252-284):
- `priceSensitivity`: Variable (0-2.0)
- `connectionCostRatio`: 0.5 (50% discount on connections)
- Target: Budget/leisure travelers

**`SpeedPreference`** (lines 286-311):
- `priceSensitivity`: 0.9
- `connectionCostRatio`: 2.0 (200% penalty on connections)
- `frequencyThreshold`: 14 (higher standard)
- Target: Business travelers, time-sensitive passengers

**`AppealPreference`** (lines 313-367):
- `connectionCostRatio`: 1.0 (default, from parent class)
- `loyaltySensitivity`: Variable (0-2.0)
- Target: Brand-conscious, loyal passengers

**Methods**:
- `computeCost`: Main cost calculation including all adjustments
- `priceAdjustRatio`: Price sensitivity adjustment
- `loyaltyAdjustRatio`: Loyalty-based cost reduction
- `qualityAdjustRatio`: Service quality adjustment
- `tripDurationAdjustRatio`: Frequency and flight duration adjustment

#### 3. Airport.scala

**Location**: `/airline-data/src/main/scala/com/patson/model/Airport.scala`

**Key Methods**:

**`computeTransitDiscount`** (lines 372-380):
```scala
def computeTransitDiscount(fromLinkConsideration, toLinkConsideration, paxGroup): Double = {
  if (transitModifiers.isEmpty) { 0 }
  else { transitModifiers.map(_.computeTransitDiscount(...)).sum }
}
```

- Aggregates discounts from all airport assets with `TransitModifier` trait
- Returns total discount percentage (0.0 to 0.5+)

**`computePassengerCostAssetDiscount`** (lines 360-371):
- Evaluates airport assets with `PassengerCostModifier` trait
- Probabilistically applies discounts
- Returns discount and list of assets used

#### 4. AirportAsset.scala

**Location**: `/airline-data/src/main/scala/com/patson/model/AirportAsset.scala`

**Key Traits**:

**`TransitWaitTimeModifier`** (lines 364-376):
- Interface for connection-specific discounts
- Implemented by: Airport Hotel
- `computeTransitFreqDiscount`: Returns discount percentage based on flight frequencies

**`PassengerCostAssetModifier`** (lines 379-405):
- Interface for general passenger cost reductions
- Implemented by: Museums, Convention Centers, Resorts, Stadiums, Landmarks, etc.
- `computeDiscount`: Returns optional discount based on probability and passenger type

**Key Asset Types**:
- **AirportHotelAssetType** (lines 343-353): 10-40% connection discount
- **MuseumAssetType** (lines 232-241): 5-12% general discount
- **ConventionCenterAssetType** (lines 72-82): 8-15% general discount
- **BeachResortAssetType** / **SkiResortAssetType** (lines 122-141): 10-20% general discount

#### 5. Link.scala

**Location**: `/airline-data/src/main/scala/com/patson/model/Link.scala`

**Key Classes**:

**`LinkConsideration`** (lines 272-294):
- Wrapper around Transport (Link/GenericTransit) with cost calculation
- Stores passenger group preference
- Handles cost modifiers
- Lazy evaluation of cost with caching (`CostStoreProvider`)

**`Transport`** (parent abstract class in Transport.scala):
- Base class for all transportation types
- `frequency`: Flights per week
- `frequencyByClass`: Per-class frequency function
- `transportType`: FLIGHT or GENERIC_TRANSIT

#### 6. GenericTransit.scala

**Location**: `/airline-data/src/main/scala/com/patson/model/GenericTransit.scala`

**Purpose**: Represents ground transportation connections

**Key Characteristics**:
- `frequency`: 24 × 7 = 168 (constant, very high)
- `cost`: 25% of standard flight price
- `duration`: Based on 30 km/h speed
- `quality`: 35 (constant, modest)

**Usage**: Automatically generated for short-distance connections to enable ground transfer routes

### Data Flow

1. **Passenger Group Creation**: Demand generator creates passenger groups with specific preferences
2. **Route Finding**: `findShortestRoute` is called for each passenger group
3. **Link Evaluation**: Each link is evaluated with passenger preference costs
4. **Connection Detection**: Algorithm detects connections and calculates penalties
5. **Asset Integration**: Airport assets modify connection costs
6. **Route Validation**: Routes are checked against tolerance thresholds
7. **Passenger Assignment**: Valid routes receive passenger allocations
8. **Capacity Consumption**: Seats are consumed, algorithm reruns for unsatisfied demand

---

## Examples and Case Studies

### Example 1: Simple Economy Connection

**Scenario**:
- Route: New York (JFK) → Los Angeles (LAX) via Chicago (ORD)
- Passenger: Economy class, Simple preference (budget traveler)
- Airline: Single airline (no switching)

**Route Details**:
- JFK → ORD: 1200 km, $300, frequency 42
- ORD → LAX: 2800 km, $700, frequency 42
- Direct alternative: 4000 km, $1200

**Connection Cost Calculation**:

1. **Base connection cost**: 25
2. **Frequency penalty**: 0 (both legs have frequency 42)
3. **Airline switching**: 0 (same airline)
4. **Subtotal**: 25

5. **Passenger preference modifier**: 0.5 (Simple preference)
   - 25 × 0.5 = 12.5

6. **Link class modifier**: 1.0 (Economy)
   - 12.5 × 1.0 = 12.5

7. **Airport assets**: Assume ORD has level 5 Airport Hotel
   - Discount: 15% (0.15)
   - Applied: 12.5 × (1 - 0.15) = 10.6

**Total perceived connection cost**: 10.6

**Route Total Cost**:
- Segment costs: $300 + $700 = $1000 (base)
- Connection cost: $10.60
- **Total**: $1010.60

**vs. Direct Flight**: $1200

**Conclusion**: Connection route is 15.7% cheaper perceived cost. **Passenger will choose connection**.

### Example 2: Business Class Connecting on Different Airlines

**Scenario**:
- Route: London (LHR) → Singapore (SIN) via Dubai (DXB)
- Passenger: Business class, Speed preference (business traveler)
- Airlines: British Airways (BA) → Emirates (EK), no alliance

**Route Details**:
- LHR → DXB: 5500 km, $2000, frequency 14
- DXB → SIN: 6000 km, $2200, frequency 21
- Direct alternative: 10800 km, $6000

**Connection Cost Calculation**:

1. **Base connection cost**: 25
2. **Frequency penalty**: Max(14, 21) = 21, still < 42
   - 420 / 21 = 20
3. **Airline switching**: 75 (different airlines, no alliance)
4. **Subtotal**: 25 + 20 + 75 = 120

5. **Passenger preference modifier**: 2.0 (Speed preference)
   - 120 × 2.0 = 240

6. **Link class modifier**: 3.0 (Business)
   - 240 × 3.0 = 720

7. **Airport assets**: Assume DXB has max-level Airport Hotel
   - Discount: 40% (0.4) for Business class
   - Applied: 720 × (1 - 0.4) = 432

**Total perceived connection cost**: 432

**Route Total Cost**:
- Segment costs: $2000 + $2200 = $4200 (base, before preference adjustments)
- After quality/frequency/other adjustments: ~$4800 (estimated with average adjustments)
- Connection cost: $432
- **Total**: ~$5232

**vs. Direct Flight**: $6000

**Conclusion**: Connection route is 12.8% cheaper. **Marginally competitive**, but close. If direct flight prices slightly lower or connection routes have any quality issues, passengers will prefer direct.

**If No Airport Hotel at DXB**:
- Connection cost: 720 (no discount)
- Total: ~$5520
- vs. Direct: $6000
- Only 8% cheaper; very marginal competitiveness

**If Airlines Were in Alliance**:
- Connection cost base: 25 + 20 = 45 (no airline switch penalty)
- After modifiers: 45 × 2.0 × 3.0 = 270
- After hotel discount: 270 × 0.6 = 162
- Total route cost: ~$4962
- **17% cheaper than direct**; clearly competitive

**Insight**: Alliance membership is worth **270 cost units** for this passenger, equivalent to ~$270 in pricing advantage.

### Example 3: Low-Frequency Hub Problem

**Scenario**:
- Route: Small City A → Small City B via Regional Hub H
- Passenger: Economy class, Standard preference
- Airline: Same airline throughout

**Route Details**:
- A → H: 800 km, $200, frequency 7 (once per day)
- H → B: 900 km, $220, frequency 7 (once per day)
- No direct alternative available

**Connection Cost Calculation**:

1. **Base connection cost**: 25
2. **Frequency penalty**: Max(7, 7) = 7, << 42
   - 420 / 7 = 60 (severe penalty)
3. **Airline switching**: 0 (same airline)
4. **Subtotal**: 25 + 60 = 85

5. **Passenger preference modifier**: 1.0 (standard)
   - 85 × 1.0 = 85

6. **Link class modifier**: 1.0 (Economy)
   - 85 × 1.0 = 85

7. **Airport assets**: Assume small regional hub, no relevant assets
   - Discount: 0
   - Final: 85

**Total perceived connection cost**: 85

**Route Total Cost**:
- Segment costs: $200 + $220 = $420
- Connection cost: $85
- **Total**: $505

**Standard Price for Combined Distance**: 1700 km → ~$550

**Route Validation**:
- Affordability threshold: $550 × 1.5 = $825
- Actual cost: $505
- **Route passes validation**

**Problem**: Connection cost is **20.2% of base ticket cost**—very significant. Passengers will be reluctant to take this route unless no alternatives exist.

**Solutions**:
1. **Increase frequency to 14**: Penalty drops to 30, connection cost becomes 55, total $475 (saving $30)
2. **Increase frequency to 42+**: Penalty becomes 0, connection cost becomes 25, total $445 (saving $60)
3. **Build airport infrastructure**: 20% discount → connection cost 68, total $488 (saving $17)
4. **All of the above**: Connection cost 20, total $440 (saving $65, 12.9% cost reduction)

**Strategic Lesson**: For thin routes through regional hubs, frequency is paramount. Even doubling from 7 to 14 flights/week cuts the connection penalty in half.

### Example 4: Tourist Hub with Attractions

**Scenario**:
- Route: Toronto (YYZ) → Honolulu (HNL) via hub
- Passenger: Economy class, Simple preference (leisure tourist)
- Focus: Airport asset impact

**Assume Hub has**:
- Beach Resort (max level): 20% tourist discount, 40% probability
- Museum (max level): 12% tourist discount, 30% probability
- Landmark (max level): 15% tourist discount, 35% probability

**Probability One Asset Triggers**:
- Beach Resort: 40%
- Museum: 30%
- Landmark: 35%

**Expected Discount** (assuming one applies, rough approximation):
- 0.4 × 20% + 0.3 × 12% + 0.35 × 15% = 8% + 3.6% + 5.25% = **16.85% average**

**Route Cost Before Assets**:
- Segments: $600 + $800 = $1400
- Connection cost: ~30 (after simple preference adjustment)
- **Total**: $1430

**After Asset Discount**:
- Discount: 16.85% of $1430 = $241
- **Final**: $1189

**Savings**: $241 (16.9% reduction)

**Strategic Value**:
- Makes hub extremely competitive for tourist traffic
- Justifies higher-frequency service
- Creates "destination hub" rather than pure transfer point
- Attracts passengers who might otherwise prefer direct routes

**ROI**:
- Beach Resort cost: ~$600M (varies by location)
- If hub handles 50,000 tourists/year attracted by discount
- Average revenue per passenger: $50 (airline profit margin)
- Annual benefit: $2.5M
- Payback: ~240 years

**However**:
- Primary value is competitive positioning, not direct profit
- Without assets, hub may not be competitive at all
- Also provides population/income boosts increasing overall airport value
- Actual ROI calculation must include all secondary effects

---

## Recommendations

### For Players

#### 1. Network Design

**DO**:
- Build hubs in geographically central locations to minimize circuitousness
- Maintain 42+ weekly frequency on all hub spokes to eliminate frequency penalties
- Use single-airline or alliance-partner airlines for all connections
- Invest in Airport Hotels at major connection hubs (especially for premium traffic)
- Build tourist attractions at leisure-destination hubs
- Focus hub strategies on passenger types tolerant of connections (Simple preference)

**DON'T**:
- Operate low-frequency connections (< 14 flights/week) unless absolutely necessary
- Connect through multiple airlines without alliance coverage
- Build hubs in geographic extremes (high circuitousness)
- Neglect infrastructure investments at competitive hubs
- Compete for Business/Speed preference passengers on connections without excellent frequency and infrastructure

#### 2. Competitive Strategy

**Against Direct Flight Competitors**:
- Price connecting routes 10-20% below direct flights to compensate for connection penalties
- Emphasize frequency and infrastructure advantages
- Target price-sensitive passengers (Simple preference)
- Build loyalty programs to reduce perceived costs

**Against Other Hub Operators**:
- Compete on frequency (reduce frequency penalties below competitors)
- Invest in infrastructure (hotels, attractions) to gain 10-40% cost advantages
- Join alliances to eliminate switching penalties
- Position hubs geographically better (lower circuitousness)

#### 3. Alliance Membership

**Critical For**:
- Hub operators
- Airlines with limited direct route coverage
- Carriers competing against larger airlines

**Value Proposition**:
- Eliminates 75-base-point switching penalty (equivalent to $75-$675 depending on passenger class)
- Makes connection routes competitive
- Enables virtual network expansion without operating all routes

**Decision Framework**:
- If >20% of your traffic involves connections: **Join an alliance**
- If mostly direct point-to-point: Alliance less critical, evaluate other factors

#### 4. Infrastructure Investment Priority

**High Priority** (Build First):
1. **Airport Hotel** at major connection hubs (10-40% connection discount)
2. **Increase frequency** to 42+ on all hub spokes (eliminates frequency penalty)

**Medium Priority**:
3. **Tourist attractions** (Museum, Landmark, Resorts) at leisure hubs (10-20% general discount)
4. **Convention Centers** at business hubs (8-15% business discount)

**Lower Priority**:
5. Other asset types that don't affect passenger routing

**ROI Timeframe**:
- Airport Hotel: 2 years construction, 5-10 years payback
- Tourist attractions: 4-5 years construction, 10-20 years payback
- Value includes competitive positioning (hard to quantify but critical)

#### 5. Passenger Segmentation

**Target Segments for Connection Hubs**:
1. **Primary**: Simple preference (budget/leisure) - 50% connection discount
2. **Secondary**: Standard preferences - manageable connection costs
3. **Tertiary**: Appeal/Loyal with loyalty benefits - loyalty reduces costs
4. **Avoid**: Speed preference (business) - 200% connection penalty unless excellent infrastructure

**Hub Specialization**:
- **Leisure/Tourist Hubs**: Focus on Simple preference, build attractions, lower frequency acceptable
- **Business Hubs**: Focus on loyal passengers, maximum frequency, Airport Hotel mandatory, Convention Center valuable
- **Mixed Hubs**: Difficult to optimize; generally less efficient than specialized hubs

### For Game Developers

#### Balance Considerations

**Current State**:
- Connection penalties are substantial (25-150+ base cost)
- Frequency penalties are severe for low-frequency routes (60-420 points)
- Airline switching penalty is significant (75 points)
- Infrastructure investments provide meaningful but not overwhelming benefits (10-40% discount)

**Potential Improvements**:

1. **Transparency**: Add in-game visualization of connection costs for players
2. **Asset Value**: Consider increasing ROI or decreasing costs of airport infrastructure to encourage investment
3. **Alliance Balance**: The 75-point penalty may be too high; consider reducing to 50 for better balance
4. **Frequency Curve**: The sharp penalty below 42 flights/week creates discontinuity; consider smoothing
5. **Documentation**: Add in-game help explaining connection cost mechanics

#### Future Feature Ideas

1. **Transit Visa Costs**: Additional penalties for countries requiring transit visas
2. **Minimum Connection Times**: Vary by airport size (currently implicit in frequency)
3. **Terminal Changes**: Extra penalty if connection requires terminal change
4. **Baggage Policies**: Different penalties for baggage vs. no-baggage passengers
5. **Weather Delays**: Random connection cost increases during poor weather at hubs
6. **Hub Congestion**: Dynamic connection cost increases at over-capacity hubs

---

## Conclusion

Transfer costs are a sophisticated and well-balanced game mechanic that creates strategic depth in network planning. They:

1. **Reward Direct Flights**: By imposing penalties on connections
2. **Encourage High Frequency**: Through severe penalties on low-frequency connections
3. **Promote Airline Loyalty and Alliances**: By penalizing airline switches
4. **Create Infrastructure Investment Opportunities**: Through airport assets that reduce penalties
5. **Enable Market Segmentation**: Different passenger types have vastly different tolerances

**For Players**: Success requires understanding these mechanics and optimizing network design, pricing, alliances, and infrastructure investments to minimize connection penalties and maximize passenger uptake.

**Key Success Factors**:
- High frequency on all routes (42+ flights/week target)
- Single-airline or alliance-based hub operations
- Strategic infrastructure investments (especially Airport Hotels)
- Appropriate pricing to compensate for connection disadvantages
- Focus on passenger types tolerant of connections

**Final Thought**: The connection cost system successfully balances the advantages of hub-and-spoke networks against the inherent efficiency of direct flights, creating interesting strategic choices for players and realistic passenger behavior simulations.

---

## Appendix: Quick Reference Tables

### Connection Cost Components

| Component | Value | Conditions |
|-----------|-------|------------|
| Base Connection Cost | 25 | Always applies to flight connections |
| Generic Transit Base | 25 | Ground transportation connections |
| Frequency Penalty | 420 / frequency | Only if frequency < 42 |
| Airline Switch Penalty | 75 | Different airlines, not in same alliance |

### Passenger Preference Multipliers

| Preference Type | Connection Cost Ratio | Target Segment |
|----------------|----------------------|----------------|
| Simple (Budget) | 0.5× | Leisure, price-sensitive |
| Speed | 2.0× | Business, time-sensitive |
| Appeal/Loyal/Elite | 1.0× | Standard passengers |

### Link Class Multipliers

| Class | Price Multiplier | Connection Cost Impact |
|-------|-----------------|----------------------|
| Economy | 1.0× | Baseline |
| Business | 3.0× | 3× connection cost |
| First | 9.0× | 9× connection cost |

### Airport Asset Connection Benefits

| Asset Type | Connection Discount | Target Segment | Probability |
|-----------|-------------------|----------------|-------------|
| Airport Hotel | 10-20% (Economy)<br>10-40% (Business/First) | All (especially premium) | Always applies |

### Airport Asset General Benefits

| Asset Type | Tourist Discount | Business Discount | Probability |
|-----------|-----------------|-------------------|-------------|
| Beach/Ski Resort | 20% | 10% | 40% |
| Museum | 12% | 5% | 30% |
| Landmark | 15% | 12% | 35% |
| Convention Center | 8% | 15% | 25% |
| Amusement Park | 15% | 3% | 30% |
| Stadium | 10% | 8% | 25% |
| Golf Course | 7% | 12% | 20% |

### Route Rejection Thresholds

| Criterion | Threshold | Formula |
|-----------|-----------|---------|
| Distance | 2.5× direct distance | `routeDistance ≤ routeDisplacement × 2.5` |
| Total Cost | 1.5× standard price | `routeCost ≤ standardPrice × 1.5 × incomeAdjust` |
| Link Cost | 0.9× standard price | `linkCost ≤ standardPrice × 0.9 × incomeAdjust` |

### Frequency Penalty Table

| Weekly Frequency | Flights/Day | Penalty | Impact |
|-----------------|-------------|---------|--------|
| 1 | 0.14 | 420 | Extreme |
| 7 | 1 | 60 | Very High |
| 14 | 2 | 30 | High |
| 21 | 3 | 20 | Moderate |
| 42+ | 6+ | 0 | None |

---

*Document Version 1.0*  
*Date: 2025-11-24*  
*Analysis based on commit: 6160503*
