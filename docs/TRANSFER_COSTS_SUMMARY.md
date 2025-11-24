# Transfer Costs - Quick Summary

> For the full comprehensive analysis, see [TRANSFER_COSTS_ANALYSIS.md](../TRANSFER_COSTS_ANALYSIS.md) in the repository root.

## What Are Transfer Costs?

Transfer costs (connection costs) are penalties applied when passengers must change flights at intermediate airports. They represent the perceived inconvenience, wait time, and complexity of making connections.

## Key Components

### 1. Base Connection Cost: **25 points**
Applied to every connection between flights.

### 2. Frequency Penalty: **0 to 420 points**
- Formula: `420 / frequency` (only if frequency < 42 flights/week)
- At 7 flights/week: penalty = 60
- At 14 flights/week: penalty = 30
- At 42+ flights/week: penalty = 0

### 3. Airline Switching Penalty: **75 points**
Applied when connecting between different airlines that are NOT in the same alliance.

## Passenger Preference Multipliers

| Preference Type | Multiplier | Effect |
|----------------|-----------|---------|
| Simple (Budget) | 0.5× | Tolerates connections well |
| Standard | 1.0× | Normal tolerance |
| Speed (Business) | 2.0× | Strongly prefers direct flights |

## Link Class Multipliers

| Class | Multiplier |
|-------|-----------|
| Economy | 1.0× |
| Business | 3.0× |
| First | 9.0× |

## Airport Infrastructure Benefits

### Airport Hotel
- **Economy**: 10-20% connection cost discount
- **Business/First**: 10-40% connection cost discount
- Maximum discount capped at 50%

### Tourist Attractions
- Museums, Landmarks, Resorts, etc.
- Provide 5-20% general cost reductions
- Applied probabilistically based on asset type
- Make airports attractive as stopover points

## Strategic Implications

### To Minimize Connection Penalties:

1. **Increase Frequency**: Target 42+ flights/week on hub routes
2. **Use Same Airline**: Or ensure all connections are within same alliance
3. **Build Infrastructure**: Airport Hotels at major hubs (especially for premium traffic)
4. **Target Right Passengers**: Focus on budget/leisure travelers who tolerate connections better
5. **Geographic Positioning**: Central hubs minimize route circuitousness

### Connection vs. Direct Flight Economics

For a connection route to be competitive:
- **Economy passengers**: Must be ~5-10% cheaper than direct
- **Business passengers**: Must be ~15-20% cheaper than direct
- Higher percentages if frequency is low or no alliance

### Alliance Value

Eliminates the 75-point airline switching penalty, which equals:
- **Economy Standard**: $75 savings per connection
- **Business Speed**: $450 savings per connection

## Quick Reference: Connection Cost Examples

### Low-Cost Connection (Best Case)
- Same airline
- High frequency (42+)
- Economy Simple passenger
- Airport Hotel at hub
- **Total**: ~11 cost units

### High-Cost Connection (Worst Case)
- Different airlines, no alliance
- Low frequency (7/week)
- First class Speed passenger
- No infrastructure
- **Total**: ~1,440 cost units

## When to Build Connection Hubs

✅ **Build Connection Hubs When:**
- You can maintain 42+ flights/week on all spokes
- You operate single airline or have alliance partners
- You can invest in Airport Hotel infrastructure
- Target market is budget/leisure travelers
- Geographic position minimizes circuitousness

❌ **Avoid Connection Hubs When:**
- Cannot maintain adequate frequency
- Multiple airlines without alliance coverage
- Targeting premium business passengers
- Better served by direct flights
- Geographic position is suboptimal

## Further Reading

For detailed analysis including:
- Complete mathematical formulas
- Code implementation details
- 4 detailed worked examples
- Route selection algorithm explanation
- ROI calculations for infrastructure
- Developer recommendations

See the full document: [TRANSFER_COSTS_ANALYSIS.md](../TRANSFER_COSTS_ANALYSIS.md)
