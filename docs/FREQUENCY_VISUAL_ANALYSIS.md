# Visual Analysis: Frequency Impact Curves

## Passenger Appeal vs. Frequency

This document provides visual representations of how frequency affects passenger perception across different preference types.

### 1. Perceived Cost Multiplier by Frequency

The following shows how frequency affects the cost multiplier applied to standard ticket prices:

```
Cost Multiplier (lower is better for passengers)
1.20 |                                    Simple Preference (2% sensitivity)
     |                                    
1.15 | S                                  Speed Preference (15% sensitivity)
     | | S                                
1.10 | | |A                               Appeal Preference (5% sensitivity)
     | | | A                              
1.05 | | | |A
     | | | | A
1.00 | ======A================================ Neutral (standard price)
     | | | | | A
0.95 | | | | |  A
     | | | | |   A
0.90 | | | |A|    A
     | | | A |     A
0.85 | | | | |      SSSSSSSSSSSSSSSSSSSSSS Speed (max benefit at 28)
     | | | | |                   S
0.80 | S | | |                    S  Simple (max benefit at 6)
     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--
     0  3  6  9  12 15 18 21 24 27 30 33 36 39 42 45
                    Frequency (flights per week)

Legend:
S = Simple Preference (Budget/Carefree passengers)
| = Speed Preference (Time-sensitive passengers) 
A = Appeal Preference (Quality-conscious passengers)
= = Neutral threshold
```

### 2. Benefit Curves by Passenger Type

#### Simple Preference (Budget Travelers)
```
Benefit (% reduction in perceived cost)
 0% +-----+-----+-----+-----+-----+-----+-----+
    |     |     |     |     |     |     |     |
-1% |     |  ***************************  | MAX BENEFIT
    |    *|*    |     |     |     |     |     |
-2% |   * |     |     |     |     |     |     |
    |  *  |     |     |     |     |     |     |
-3% | *   |     |     |     |     |     |     |
    +-----+-----+-----+-----+-----+-----+-----+
    0     3     6     9    12    15    18    21
              Frequency (flights/week)

Threshold: 3 flights/week
Max Benefit: -2% at 6+ flights/week
Sensitivity: 2%
```

#### Speed Preference (Business Travelers)
```
Benefit (% reduction in perceived cost)
 +5% +-----+-----+-----+-----+-----+-----+-----+
     |     |     |     |     |     |     |     |
  0% |     |     |     *     |     |     |     | NEUTRAL
     |     |     |   * |     |     |     |     |
 -5% |     |     |  *  |     |     |     |     |
     |     |     | *   |     |     |     |     |
-10% |     |    *|     |     |     |     |     |
     |     |   * |     |     |     |     |     |
-15% |  *  | *   |     |     *****************| MAX BENEFIT
     +-----+-----+-----+-----+-----+-----+-----+
     0     7    14    21    28    35    42    49
                  Frequency (flights/week)

Threshold: 14 flights/week
Max Benefit: -15% at 28+ flights/week
Sensitivity: 15%
```

#### Appeal Preference (Quality-Conscious Travelers)
```
Benefit (% reduction in perceived cost)
 +2% +-----+-----+-----+-----+-----+-----+-----+
     |     |     |     |     |     |     |     |
  0% |     |     |     *     |     |     |     | NEUTRAL
     |     |     |   * |     |     |     |     |
 -2% |     |     | *   |     |     |     |     |
     |     |     |*    |     |     |     |     |
 -4% |     |    *|     |     |     |     |     |
     |     |   * |     |     |     |     |     |
 -6% |  *  | *   |     |     *****************| MAX BENEFIT
     +-----+-----+-----+-----+-----+-----+-----+
     0     7    14    21    28    35    42    49
                  Frequency (flights/week)

Threshold: 14 flights/week
Max Benefit: -5% at 28+ flights/week
Sensitivity: 5%
```

### 3. Combined Staff Cost by Route Type

```
Staff per Flight (higher is worse)
2.0 |                                         Ultra Long Intercontinental (1.6)
    |                                         Long Intercontinental (1.6)
1.6 |                     ********************
    |                     |
    |                     |
1.2 |         ************
    |         |                               Short/Med Intercontinental (1.2)
    |         |
0.8 | ********
    | |                                       All Domestic Routes (0.8)
    | |                                       Short/Med/Long International (0.8)
0.4 | |
    +----+----+----+----+----+----+----+----+----+----+
      Dom  Dom  Dom  Int  Int  Int  Cont Cont Cont UL
      S    M    L    S    M    L    S    M    L    Cont

Legend:
S = Short-haul
M = Medium-haul  
L = Long-haul
Cont = Intercontinental
UL = Ultra Long-haul
```

### 4. Cost vs. Revenue Optimization Curve

Theoretical model showing the relationship between frequency and profitability:

```
Profitability ($)
     |                 ** Optimal Zone **
     |              *   |   |   |   *
High |           *      |   |   |      *  
     |         *        |   |   |        *
     |      *           |   |   |          *
     |    *   Revenue   |   |   |  Costs    *
Med  |  *     Curve     |   |   |  Exceed    *
     | *                |   |   |  Revenue    *
     |*                 |   |   |              *
Low  *                  |   |   |               **
     +----+----+----+----+----+----+----+----+----+----
     0    7   14   21   28   35   42   49   56   63
              Frequency (flights per week)

Key Points:
- Below 14: Revenue increasing faster than costs
- 14-28: Optimal zone, diminishing returns begin
- Above 28: Costs increase but revenue plateaus
- Above 42: Likely unprofitable (costs exceed revenue)

Actual optimal point depends on:
- Route type (intercontinental costs higher)
- Demand level (capacity utilization)
- Competition (load factor risks)
- Airplane availability (opportunity costs)
```

### 5. Break-Even Frequency by Route Type

Estimated break-even frequency where costs equal revenue for a typical route:

```
Frequency (flights/week)
50 |
   |                                   
45 |
   |                               X   Ultra Long (staff cost 1.6x)
40 |
   |                           X       Long Intercont (staff cost 1.6x)
35 |
   |                   X   X           Med Intercont (staff cost 1.2x)
30 |               X                   Short Intercont (staff cost 1.2x)
   |                                   
25 |
   |       X   X   X                   All International (staff cost 0.8x)
20 |
   |   X   X   X                       All Domestic (staff cost 0.8x)
15 |
   +---+---+---+---+---+---+---+---+---+
   Optimal          Break-Even

Route Categories (left to right):
1. Short/Med/Long Domestic
2. Short/Med/Long International  
3. Short Intercontinental
4. Medium Intercontinental
5. Long Intercontinental
6. Ultra Long Intercontinental

Note: This is illustrative. Actual break-even depends on:
- Ticket pricing
- Load factors
- Competition
- Airplane efficiency
```

### 6. Decision Matrix: Frequency Adjustment

```
Current Load Factor
     ^
100% |  +=====+=====+=====+=====+
     |  | OK  | INC | INC | INC |  Increase frequency
 90% |  +-----+-----+-----+-----+
     |  | OK  | OK  | INC | INC |  
 80% |  +-----+-----+-----+-----+
     |  | DEC | OK  | OK  | INC |  
 70% |  +-----+-----+-----+-----+
     |  | DEC | DEC | OK  | OK  |  
 60% |  +-----+-----+-----+-----+
     |  | DEC | DEC | DEC | OK  |  
 50% |  +-----+-----+-----+-----+
     |  | DEC!| DEC!| DEC!| DEC |  Decrease urgently
 40% |  +=====+=====+=====+=====+  (risk of license loss)
     +------------------------>
        0-6   7-13  14-27  28+
              Current Frequency

Legend:
INC  = Consider increasing frequency
OK   = Maintain current frequency
DEC  = Consider decreasing frequency
DEC! = Urgently decrease frequency (risk zone)

Additional Factors:
- Add +1 action (e.g., OK → INC) if route is domestic
- Subtract -1 action (e.g., INC → OK) if intercontinental
- Subtract -2 actions (e.g., INC → DEC) if ultra long-haul
```

### 7. Marginal Benefit Analysis

The marginal benefit of adding one more flight per week:

```
Marginal Passenger Appeal Benefit
10% |
    | *
    | |*
 8% | | *
    | | |*
 6% | | | *
    | | | |*
 4% | | | | *
    | | | | |*
 2% | | | | | *
    | | | | | |*___________________
 0% +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    0 2 4 6 8 10 12 14 16 18 20 22 24 26 28 30
              Frequency (flights/week)

Key Insights:
- Highest marginal benefit: 0-7 flights
- Moderate benefit: 7-14 flights
- Declining benefit: 14-28 flights  
- Zero additional benefit: 28+ flights

For Speed preference passengers (15% sensitivity),
each additional flight up to 14 provides ~1% benefit,
then declining to 0.5% from 14-28,
then 0% beyond 28.
```

### 8. Total Cost Comparison: Frequency vs. Airplane Size

Comparing 14 flights with small planes vs. 7 flights with large planes (same total capacity):

```
Total Weekly Cost (normalized)
120 |                Small Plane Option (14x per week)
    |                **********
110 |                *        *
    |                *        *
100 |     ***********         **********  Large Plane Option (7x per week)
    |     *          |        *        *
 90 |     *          |        *        *
    |     *          |        *        *
 80 |     *          |        *        *
    +-----+----------+--------+--------+
         Domestic    Intl    Intercont

Cost Components:
- Small plane, high frequency:
  * More staff (linear with frequency)
  * More airport fees (linear with frequency)
  * Same total fuel (capacity is equal)
  * Same total crew (capacity is equal)
  
- Large plane, low frequency:
  * Less staff (fewer flights)
  * Fewer airport fees (fewer flights)
  * Same total fuel (capacity is equal)
  * Same total crew (capacity is equal)
  * BETTER passenger appeal if reaches 14+ flights

Conclusion: 
- Large planes advantageous on intercontinental (due to staff costs)
- Small planes viable on domestic if reaching 14+ frequency
- The choice depends on demand patterns and slot availability
```

### 9. Summary Tables

#### Frequency Sweet Spots

| Passenger Type | Min Acceptable | Target | Optimal Max | Hard Cap (no benefit) |
|----------------|:--------------:|:------:|:-----------:|:---------------------:|
| Budget/Carefree | 1 | 3 | 6 | 6 |
| Time-Sensitive | 7 | 14 | 28 | 28 |
| Quality-Focused | 7 | 14 | 28 | 28 |

#### Cost Multipliers by Route Type

| Route Type | Staff Multiplier | Economic Pressure on High Frequency |
|------------|:----------------:|:----------------------------------:|
| Domestic | 1.0× | Low |
| International | 1.0× | Low |
| Short/Med Intercontinental | 1.5× | Moderate |
| Long/Ultra Long Intercontinental | 2.0× | High |

#### Load Factor Action Thresholds

| Load Factor | Competitive Market | Non-Competitive Market |
|:-----------:|:------------------:|:----------------------:|
| 80%+ | Excellent, can increase frequency | Excellent, can increase frequency |
| 70-80% | Good, maintain or cautiously increase | Good, maintain or increase |
| 60-70% | Adequate, maintain frequency | Adequate, monitor |
| 50-60% | Warning, consider reducing | Acceptable, monitor |
| Below 50% | Urgent action needed, reduce frequency | Concerning, consider adjustments |

---

## Mathematical Formulas

### Frequency Ratio Delta
```
frequencyRatioDelta = max(-1, (threshold - frequency) / threshold) × sensitivity

Where:
- threshold: 3 (Simple), 14 (Speed/Appeal)
- sensitivity: 0.02 (Simple), 0.15 (Speed), 0.05 (Appeal)
- Result is capped at -1 (full penalty) and has ceiling based on 2×threshold
```

### Total Trip Duration Adjustment
```
tripDurationAdjust = 1 + max(-0.75, frequencyRatioDelta + flightDurationRatioDelta)

- Minimum multiplier: 0.25 (75% discount)
- Maximum multiplier: depends on flight duration penalty
- Applied to standard price to get perceived cost
```

### Staff Cost
```
staffCost = (basic + perFrequency × frequency + per1000Pax × capacity/1000) × baseModifier

Where:
- basic: 8-30 depending on route type
- perFrequency: 0.8-1.6 depending on route type
- per1000Pax: 2-4 depending on route type
- baseModifier: depends on airline base specialization
```

---

## Conclusion

The visual analysis confirms:

1. **Passenger appeal plateaus at 28 flights/week** for most passengers
2. **Diminishing returns begin at 14 flights/week**
3. **No benefit beyond 28 flights/week** for passenger attraction
4. **Intercontinental routes have 50-100% higher staff costs** creating natural economic pressure against very high frequencies
5. **Optimal frequencies vary by route type**: domestic routes can sustain higher frequencies more economically than intercontinental routes

The game design elegantly balances frequency benefits through diminishing returns rather than explicit penalties.

---

*For detailed mechanics, see ROUTE_FREQUENCY_ANALYSIS.md*
*For practical guidelines, see FREQUENCY_QUICK_REFERENCE.md*
