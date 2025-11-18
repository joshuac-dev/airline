# Aircraft Seat Configuration Guide

## Overview

This document explains how different seat classes are configured in aircraft and the space requirements for each class type.

## Seat Class Space Multipliers

In the airline simulation, each seat class occupies a different amount of space on an aircraft. The space multiplier determines how much physical space each seat type requires relative to an economy seat.

### Space Multiplier Values

| Seat Class | Space Multiplier | Space Relative to Economy |
|------------|------------------|---------------------------|
| Economy    | 1.0              | 1x (baseline)             |
| Business   | 2.5              | 2.5x economy space        |
| First      | 6.0              | 6x economy space          |

## How Many Economy Seats Does Each Class Replace?

### Business Class
**1 Business class seat replaces 2.5 economy seats**

This means that for every business class seat you configure on an aircraft, you are using the same space that could accommodate 2.5 economy seats.

### First Class
**1 First class seat replaces 6 economy seats**

This means that for every first class seat you configure on an aircraft, you are using the same space that could accommodate 6 economy seats.

## Configuration Examples

### Example 1: All-Economy Configuration
- Aircraft Total Capacity: 300 space units
- Configuration: 300 economy seats (300 × 1.0 = 300 space units)

### Example 2: Mixed Configuration
- Aircraft Total Capacity: 300 space units
- Configuration:
  - 10 First class seats (10 × 6.0 = 60 space units)
  - 20 Business class seats (20 × 2.5 = 50 space units)  
  - 190 Economy seats (190 × 1.0 = 190 space units)
  - Total: 60 + 50 + 190 = 300 space units ✓

### Example 3: Maximum First Class
- Aircraft Total Capacity: 300 space units
- Configuration: 50 first class seats (50 × 6.0 = 300 space units)

## Other Class Multipliers

Beyond space, each seat class also has different multipliers for:

| Multiplier Type | Economy | Business | First |
|----------------|---------|----------|-------|
| **Space**      | 1.0     | 2.5      | 6.0   |
| **Resource**   | 1.0     | 2.0      | 3.0   |
| **Price**      | 1.0     | 3.0      | 9.0   |
| **Price Sensitivity** | 1.0 | 0.9   | 0.8   |

### Resource Multiplier
Indicates the operational resources (crew, amenities, service) required per seat.

### Price Multiplier
Indicates the typical pricing ratio compared to economy fares.

### Price Sensitivity
Indicates how sensitive passengers in each class are to price changes (lower = less sensitive).

## Code Reference

These multipliers are defined in the source code at:
- File: `airline-data/src/main/scala/com/patson/model/Link.scala`
- Lines: 322-333

```scala
case object FIRST extends LinkClass("F", spaceMultiplier = 6, resourceMultiplier = 3, priceMultiplier = 9, priceSensitivity = 0.8, level = 3)
case object BUSINESS extends LinkClass("J", spaceMultiplier = 2.5, resourceMultiplier = 2, priceMultiplier = 3, priceSensitivity = 0.9, level = 2)
case object ECONOMY extends LinkClass("Y", spaceMultiplier = 1, resourceMultiplier = 1, priceMultiplier = 1, priceSensitivity = 1, level = 1)
```

## Impact on Aircraft Configuration

When configuring an aircraft:
1. Each aircraft model has a fixed total capacity (space units)
2. The sum of (seats × spaceMultiplier) for all classes cannot exceed the aircraft's total capacity
3. Airlines can create custom configurations balancing premium and economy seating

## Business Implications

- **Premium Seating Trade-off**: Adding premium seats significantly reduces total passenger capacity
- **Revenue Optimization**: While first class uses 6× the space, it typically commands 9× the price
- **Operational Efficiency**: All-economy configurations maximize passenger throughput but may not maximize revenue
- **Market Positioning**: Seat configuration is a key strategic decision affecting brand positioning and profitability
