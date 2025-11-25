# Route Stabilization Guide

## Overview

This comprehensive guide explains how route performance stabilizes over time in the airline simulation game. Understanding these mechanics will help players make informed decisions about when to evaluate and adjust their routes.

## Executive Summary

**Key Finding: Routes typically require 4-8 cycles (weeks) to fully stabilize after creation or significant changes.** However, the exact stabilization period varies based on the type of change made:

| Change Type | Initial Impact | Partial Stabilization | Full Stabilization |
|-------------|----------------|----------------------|-------------------|
| New Route Creation | Immediate passenger availability | 2-4 cycles | 6-8 cycles |
| Price Changes | Nearly immediate | 1-2 cycles | 2-4 cycles |
| Frequency Changes | Immediate capacity change | 1-2 cycles | 3-4 cycles |
| Capacity Changes | Immediate seat availability | 1-2 cycles | 3-4 cycles |

---

## Table of Contents

1. [Understanding the Simulation Cycle](#understanding-the-simulation-cycle)
2. [Passenger Demand Mechanics](#passenger-demand-mechanics)
3. [Loyalty and Loyalist System](#loyalty-and-loyalist-system)
4. [Route Cost Computation](#route-cost-computation)
5. [Stabilization Factors by Change Type](#stabilization-factors-by-change-type)
6. [Practical Recommendations](#practical-recommendations)
7. [Technical Deep Dive](#technical-deep-dive)

---

## Understanding the Simulation Cycle

### What is a Cycle?

Each simulation cycle represents one week of in-game time. The cycle duration is configured as 30 minutes of real-time (see `MainSimulation.CYCLE_DURATION = 30 * 60` seconds).

### What Happens Each Cycle?

During each cycle, the simulation performs the following key operations in order:

1. **Demand Generation** - Fresh passenger demand is computed between all airport pairs
2. **Route Finding** - Passengers find optimal routes using available links
3. **Passenger Consumption** - Passengers book and travel on selected routes
4. **Loyalist Computation** - Airport loyalty is updated based on passenger satisfaction
5. **Airline Statistics** - Revenue, costs, and reputation are calculated
6. **Link Refresh** - Capacity and frequency adjustments take effect

---

## Passenger Demand Mechanics

### Demand Generation

Passenger demand is generated fresh each cycle using the `DemandGenerator.computeDemand()` method. Key factors include:

- **Airport Power** - Population × Income of origin and destination
- **Distance** - Flight type bonuses/penalties based on route distance
- **Country Relationships** - International routes affected by diplomatic relations
- **Airport Features** - Hub status, tourism attractions, etc.

**Important:** Demand is computed independently each cycle and does not "remember" previous cycles. This means passengers don't inherently prefer routes they've used before - instead, they evaluate all available options fresh each time.

### Flight Type Multipliers

Demand varies significantly by flight type:

| Flight Type | Demand Multiplier |
|-------------|------------------|
| Short-haul Domestic | 10x |
| Short-haul International | 2.5x |
| Medium-haul Domestic | 8x |
| Medium-haul International | 1.0x |
| Long-haul Domestic | 6.5x |
| Long-haul International | 0.35x |
| Ultra Long-haul | 0.25x |

---

## Loyalty and Loyalist System

### How Loyalty Works

Loyalty is the primary mechanism that provides route stability over time. The loyalty system works through **loyalists** - virtual residents of an airport's catchment area who prefer your airline.

### Loyalist Accumulation

Loyalists are gained when passengers have a satisfactory experience:

```
Satisfaction >= 0.6 (NEUTRAL_SATISFACTION) → Potential loyalty gain
```

The conversion formula is:
- **Base Conversion Ratio** = `(satisfaction - 0.6) / 0.4 * loyaltySensitivity`
- **Maximum Flip Ratio** = 1.0 (100% of passengers can convert)

### Loyalist Decay

Loyalists naturally decay each cycle at a rate of **0.05% per week** (0.0005 per cycle):
- 1 loyalist disappears per 2,000 loyalists per week
- For airports with population ≥ 1,000,000, at least 1 loyalist is lost per cycle

### Loyalty Impact on Passenger Choice

Loyalty affects how passengers perceive your prices through the `loyaltyAdjustRatio`:

```
Base discount = 1 + (-0.1 + loyalty/100 / 2.25) * loyaltySensitivity
Effective cost = ticket_price / base_discount
```

At maximum loyalty (100), passengers perceive your prices as approximately **25-35% lower** than actual price.

### Loyalty-to-Loyalist Formula

Loyalty percentage is derived from loyalist count:
```
loyalistRatio = loyalist_count / airport_population
baseLoyalty = log10(1 + loyalistRatio * 9) * 100
```

This creates a logarithmic curve where:
- Small loyalist gains provide significant initial loyalty boost
- Additional loyalists have diminishing returns on loyalty percentage

---

## Route Cost Computation

### How Passengers Choose Routes

Passengers evaluate routes based on **perceived cost**, not just ticket price. The perceived cost includes:

1. **Base Price** - Your set ticket price
2. **Price Sensitivity Adjustment** - How much price deviation from standard affects choice
3. **Quality Adjustment** - Link quality vs. passenger expectations
4. **Trip Duration Adjustment** - Frequency and flight duration effects
5. **Loyalty Adjustment** - Loyalty discount (if passenger is loyalty-sensitive)
6. **Lounge Adjustment** - Lounge requirements for premium passengers

### Cost Tolerance Factors

Passengers will reject routes that exceed certain cost thresholds:

| Factor | Tolerance Multiplier |
|--------|---------------------|
| Total Route Cost | 1.5x standard price |
| Individual Link Cost | 0.9x standard price |
| Route Distance | 2.5x direct distance |

---

## Stabilization Factors by Change Type

### 1. New Route Creation

**Stabilization Period: 6-8 cycles**

When creating a new route, the following progression occurs:

**Cycle 1-2:**
- Route is immediately available for booking
- Passengers can find and use the route
- Initial load factors may be low or highly variable
- No existing loyalty to attract passengers

**Cycle 3-4:**
- Passenger patterns begin emerging
- Early loyalists start accumulating (if satisfaction ≥ 0.6)
- Load factors become more consistent
- Route profitability trends become visible

**Cycle 5-8:**
- Loyalty effects become noticeable
- Loyalist base reaches meaningful levels
- Route finds its natural demand level
- Competition dynamics fully factored in

**Key factors affecting new route stabilization:**
- Competition on the route (more competitors = longer stabilization)
- Your airline's existing reputation at the airports
- Pricing strategy (aggressive pricing attracts passengers faster)
- Route quality/frequency offered

### 2. Price Changes

**Stabilization Period: 2-4 cycles**

Price changes have nearly immediate effect because:
- Demand is recalculated fresh each cycle
- Passengers evaluate prices in real-time
- No "memory" of previous prices

**However, full stabilization takes longer due to:**
- Load factor smoothing (passengers don't instantly fill capacity)
- Route consumption occurs in 10 iterations per cycle with shrinking available seats
- Competition may react to your price changes

**Recommended waiting period:** 2-4 cycles before further price adjustments

### 3. Frequency Changes

**Stabilization Period: 3-4 cycles**

Frequency affects passenger choice through the `tripDurationAdjustRatio`:

```
frequencyRatioDelta = max(-1, (threshold - frequency) / threshold) * frequencySensitivity
```

Default frequency threshold is **3-14 flights per week** depending on preference type.

**Immediate effects:**
- Changed capacity per flight
- Changed connection wait time perception
- Changed schedule attractiveness

**Delayed effects:**
- Passenger preference patterns adjust
- Connection route viability changes
- Hub effectiveness changes

### 4. Capacity Changes (Aircraft Assignment)

**Stabilization Period: 2-4 cycles**

Capacity changes take effect through the `LinkSimulation.refreshLinksPostCycle()` method which reconciles link capacity with actual aircraft assignments.

**Immediate effects:**
- Seats become available/unavailable immediately
- Load factor changes immediately

**Stabilization effects:**
- Passenger route selection adjusts over 2-3 cycles
- Hub connection patterns adjust
- Revenue per flight stabilizes

---

## Practical Recommendations

### When to Evaluate New Routes

| Metric | Wait Period |
|--------|-------------|
| Initial load factor | 2-3 cycles |
| Consistent load factor | 4-6 cycles |
| Profitable operation | 6-8 cycles |
| Final route assessment | 8-12 cycles |

### Price Adjustment Strategy

1. **Initial Pricing:** Start at or slightly below standard price
2. **First Adjustment:** After 2-3 cycles with stable load factor
3. **Fine-tuning:** Adjust by small increments (5-10%) every 2-3 cycles
4. **Monitoring:** Watch for load factor response within 1-2 cycles

### Frequency Optimization

1. **New routes:** Start with moderate frequency (3-7 flights/week)
2. **Assessment:** Evaluate load factor after 2-3 cycles
3. **Adjustment:** Change frequency by 1-2 flights at a time
4. **Target:** Load factor 70-85% for optimal profitability

### Capacity Planning

1. **Initial deployment:** Match capacity to expected demand (use route planning tools)
2. **Assessment:** Monitor load factor for 2-3 cycles
3. **Adjustment:** Scale capacity up/down based on consistent load factor trends
4. **Caution:** Large capacity changes reset stabilization

### Warning Signs to Watch

- **Load factor < 50%** for 3+ cycles: Consider price reduction or route review
- **Load factor > 95%** for 2+ cycles: Add capacity to capture unmet demand
- **Declining load factor trend:** Check for new competition
- **Revenue declining despite stable load:** Check for price competition

---

## Technical Deep Dive

### Passenger Consumption Algorithm

The passenger simulation runs up to **10 consumption cycles** per game cycle (see `PassengerSimulation.consumptionCycleMax = 10`). This allows for route recalculation as seats fill up:

```scala
// Iteration count increases with consumption cycle
val iterationCount =
  if (consumptionCycleCount < 3) 4
  else if (consumptionCycleCount < 6) 5
  else 6
```

### Route Finding Algorithm

The simulation uses a modified Bellman-Ford algorithm with 4-6 iterations (see `PassengerSimulation.findShortestRoute`). This means:
- Maximum 4-6 connection flights are considered
- Routes are found independently for each passenger group
- Cost is the primary optimization target

### Loyalty Update Frequency

Loyalty is updated every cycle through `AirportSimulation.simulateLoyalists()`:
- Existing loyalists decay by 0.05%
- New loyalists are added based on passenger satisfaction
- Changes are cumulative cycle-over-cycle

### Load Factor Alert System

The simulation includes automatic route warnings:
- **Alert threshold:** Load factor < 50% on routes with 3+ competitors
- **Warning duration:** 52 cycles before route revocation
- **Constant:** `LOAD_FACTOR_ALERT_DURATION = 52`

This means struggling routes have up to **52 cycles (1 year)** to improve before being forcibly cancelled by airport authorities.

---

## Summary Table

| Change Type | Immediate Effect | Partial Stable | Fully Stable | Key Factor |
|-------------|-----------------|----------------|--------------|------------|
| New Route | Passengers can book | 3-4 cycles | 6-8 cycles | Loyalty buildup |
| Price ↑/↓ | Next cycle | 1-2 cycles | 2-4 cycles | Demand elasticity |
| Frequency ↑ | Next cycle | 2-3 cycles | 3-4 cycles | Schedule preference |
| Frequency ↓ | Next cycle | 2-3 cycles | 3-4 cycles | Connection viability |
| Capacity ↑ | Next cycle | 1-2 cycles | 2-3 cycles | Unmet demand |
| Capacity ↓ | Next cycle | 1-2 cycles | 2-3 cycles | Passenger spillover |

---

## Appendix: Key Constants

From the codebase analysis:

| Constant | Value | Location |
|----------|-------|----------|
| Cycle Duration | 30 minutes | `MainSimulation.CYCLE_DURATION` |
| Loyalty Decay Rate | 0.0005 (0.05%) | `AirportSimulation.DECAY_RATE` |
| Neutral Satisfaction | 0.6 | `AirportSimulation.NEUTRAL_SATISFACTION` |
| Max Loyalist Flip Ratio | 1.0 | `AirportSimulation.MAX_LOYALIST_FLIP_RATIO` |
| Route Cost Tolerance | 1.5x | `PassengerSimulation.ROUTE_COST_TOLERANCE_FACTOR` |
| Link Cost Tolerance | 0.9x | `PassengerSimulation.LINK_COST_TOLERANCE_FACTOR` |
| Route Distance Tolerance | 2.5x | `PassengerSimulation.ROUTE_DISTANCE_TOLERANCE_FACTOR` |
| Load Factor Alert Threshold | 50% | `LinkSimulation.LOAD_FACTOR_ALERT_THRESHOLD` |
| Alert Duration | 52 cycles | `LinkSimulation.LOAD_FACTOR_ALERT_DURATION` |
| Consumption Cycles | 10 max | `PassengerSimulation.consumptionCycleMax` |
| Base Demand Chunk Size | 10 pax | `DemandGenerator.baseDemandChunkSize` |

---

## Version History

- **v1.0** - Initial documentation based on codebase analysis
