# Staff Requirements by Route Type

This document outlines the office staff requirements for various route types in the airline simulation.

## Overview

Staff requirements are calculated based on three components:
1. **Basic Staff**: Fixed base requirement for the route type
2. **Frequency Staff**: Staff required per flight frequency (departures per week)
3. **Capacity Staff**: Staff required per 1,000 passengers carried

The total staff requirement is calculated as:
```
Total Staff = (Basic Staff + Frequency Staff × Frequency + Capacity Staff × Total Capacity / 1000) × Modifier
```

Where the modifier is based on the airline base efficiency (default: 1.0).

## Route Types and Staff Requirements

### Domestic Routes

#### Short-haul Domestic
- **Basic Staff**: 8
- **Staff per Frequency**: 0.8 (multiply factor: 2)
- **Staff per 1,000 Passengers**: 2
- **Description**: Domestic flights within the same country (short distance)

#### Medium-haul Domestic
- **Basic Staff**: 10
- **Staff per Frequency**: 0.8 (multiply factor: 2)
- **Staff per 1,000 Passengers**: 2
- **Description**: Domestic flights within the same country (medium distance)

#### Long-haul Domestic
- **Basic Staff**: 12
- **Staff per Frequency**: 0.8 (multiply factor: 2)
- **Staff per 1,000 Passengers**: 2
- **Description**: Domestic flights within the same country (long distance)

### International Routes

#### Short-haul International
- **Basic Staff**: 10
- **Staff per Frequency**: 0.8 (multiply factor: 2)
- **Staff per 1,000 Passengers**: 2
- **Description**: International flights to neighboring countries (short distance)

#### Medium-haul International
- **Basic Staff**: 15
- **Staff per Frequency**: 0.8 (multiply factor: 2)
- **Staff per 1,000 Passengers**: 2
- **Description**: International flights to nearby countries (medium distance)

#### Long-haul International
- **Basic Staff**: 20
- **Staff per Frequency**: 0.8 (multiply factor: 2)
- **Staff per 1,000 Passengers**: 2
- **Description**: International flights to distant countries (long distance)

### Intercontinental Routes

#### Short-haul Intercontinental
- **Basic Staff**: 15
- **Staff per Frequency**: 1.2 (multiply factor: 3)
- **Staff per 1,000 Passengers**: 3
- **Description**: Intercontinental flights between continents (short distance)

#### Medium-haul Intercontinental
- **Basic Staff**: 25
- **Staff per Frequency**: 1.2 (multiply factor: 3)
- **Staff per 1,000 Passengers**: 3
- **Description**: Intercontinental flights between continents (medium distance)

#### Long-haul Intercontinental
- **Basic Staff**: 30
- **Staff per Frequency**: 1.6 (multiply factor: 4)
- **Staff per 1,000 Passengers**: 4
- **Description**: Intercontinental flights between continents (long distance)

#### Ultra Long-haul Intercontinental
- **Basic Staff**: 30
- **Staff per Frequency**: 1.6 (multiply factor: 4)
- **Staff per 1,000 Passengers**: 4
- **Description**: Ultra long-haul intercontinental flights (very long distance)

## Staff Calculation Formula Details

The staff per frequency is calculated as:
```
Staff per Frequency = 2.0 / 5 × Multiply Factor = 0.4 × Multiply Factor
```

The staff per 1,000 passengers is:
```
Staff per 1,000 Pax = 1 × Multiply Factor
```

### Multiply Factors by Route Category:
- **Domestic & International**: 2
- **Short/Medium Intercontinental**: 3
- **Long/Ultra Long Intercontinental**: 4

## Examples

### Example 1: Short-haul Domestic Route
A short-haul domestic route with:
- Frequency: 7 flights per week
- Capacity: 1,400 passengers per week (200 per flight)

Staff Required:
```
= (8 + 0.8 × 7 + 2 × 1.4) × 1.0
= (8 + 5.6 + 2.8) × 1.0
= 16.4 × 1.0
= 16 staff (rounded)
```

### Example 2: Long-haul Intercontinental Route
A long-haul intercontinental route with:
- Frequency: 14 flights per week
- Capacity: 4,200 passengers per week (300 per flight)

Staff Required:
```
= (30 + 1.6 × 14 + 4 × 4.2) × 1.0
= (30 + 22.4 + 16.8) × 1.0
= 69.2 × 1.0
= 69 staff (rounded)
```

## Implementation Details

The staff requirements are implemented in:
- **File**: `airline-data/src/main/scala/com/patson/model/Link.scala`
- **Object**: `Link.staffScheme`
- **Data Structure**: `Map[FlightType.Value, StaffSchemeBreakdown]`
- **Calculation Method**: `getOfficeStaffBreakdown`

## Airline Base Modifiers

Airlines with bases at airports can benefit from staff efficiency modifiers that reduce the total staff requirement. The modifier is applied to the final calculated staff total:
- Default modifier: 1.0 (no efficiency gain)
- Base modifier: < 1.0 (efficiency gain from having a base)

These modifiers are retrieved from the airline's base at the departure airport and vary by flight category (Domestic, International, or Intercontinental).
