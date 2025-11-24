# Route Frequency Quick Reference Guide

## Frequency Thresholds by Passenger Type

| Passenger Type | Frequency Threshold | Sensitivity | Max Benefit | Optimal Frequency |
|----------------|--------------------:|------------:|------------:|------------------:|
| Budget/Carefree (Simple) | 3 | 2% | -2% cost | 6 flights/week |
| Swift (Speed) | 14 | 15% | -15% cost | 28 flights/week |
| Comprehensive/Brand/Elite (Appeal) | 14 | 5% | -5% cost | 28 flights/week |

## Staff Costs by Route Type

| Flight Type | Distance | Basic Staff | Staff per Flight | Staff per 1000 Pax | Total Factor |
|-------------|----------|------------:|------------------:|-------------------:|-------------:|
| **Domestic Routes** |
| Short-haul | ≤1,000 km | 8 | 0.8 | 2 | 2 |
| Medium-haul | 1,001-3,000 km | 10 | 0.8 | 2 | 2 |
| Long-haul | >3,000 km | 12 | 0.8 | 2 | 2 |
| **International Routes (Same Zone)** |
| Short-haul | ≤2,000 km | 10 | 0.8 | 2 | 2 |
| Medium-haul | 2,001-4,000 km | 15 | 0.8 | 2 | 2 |
| Long-haul | >4,000 km | 20 | 0.8 | 2 | 2 |
| **Intercontinental Routes (Different Zones)** |
| Short-haul | ≤2,000 km | 15 | 1.2 | 3 | 3 |
| Medium-haul | 2,001-5,000 km | 25 | 1.2 | 3 | 3 |
| Long-haul | 5,001-12,000 km | 30 | 1.6 | 4 | 4 |
| Ultra Long-haul | >12,000 km | 30 | 1.6 | 4 | 4 |

## Frequency Impact on Perceived Cost

### Example: Route with Standard Price of $1,000

| Frequency | Simple (2%) | Speed (15%) | Appeal (5%) |
|----------:|------------:|------------:|------------:|
| 0 | $1,020 | $1,150 | $1,050 |
| 3 | $1,000 | $1,075 | $1,025 |
| 7 | $980 | $1,075 | $1,018 |
| 14 | $980 | $1,000 | $1,000 |
| 21 | $980 | $963 | $983 |
| 28 | $980 | $850 | $950 |
| 35 | $980 | $850 | $950 |
| 50 | $980 | $850 | $950 |

*Note: Benefits cap at 2× threshold; no additional benefit beyond that point*

## Cost Scaling Summary

| Cost Type | Scaling | Diminishing Returns? | Penalties? |
|-----------|---------|:-------------------:|:----------:|
| Passenger Appeal | Non-linear (plateaus) | ✅ Yes | ❌ No |
| Staff Costs | Linear | ❌ No | ❌ No |
| Crew Costs | Linear (capacity) | ❌ No | ❌ No |
| Fuel Costs | Linear | ❌ No | ❌ No |
| Airport Fees | Linear | ❌ No | ❌ No |
| Maintenance | Utilization-based | ❌ No | ❌ No |
| Depreciation | Utilization-based | ❌ No | ❌ No |

## Recommended Frequency by Route Type

| Route Type | Min Frequency | Optimal Frequency | Max Recommended | Reason |
|------------|:-------------:|:-----------------:|:---------------:|--------|
| Short/Medium Domestic | 14 | 21-28 | 35 | Low staff costs, good profitability |
| Long Domestic | 14 | 21-28 | 35 | Manageable costs, high demand |
| Short/Medium International | 14 | 21-28 | 35 | Similar to domestic |
| Long International | 14 | 21 | 28 | Higher base staff costs |
| Short Intercontinental | 7-14 | 14-21 | 28 | 50% higher staff costs |
| Medium Intercontinental | 7-14 | 14-21 | 21 | 50% higher staff costs |
| Long Intercontinental | 7-14 | 14 | 21 | 100% higher staff costs |
| Ultra Long-haul | 7 | 7-14 | 14 | 100% higher staff costs, limited utilization |

## Load Factor Warnings

| Load Factor | Competitive Routes (3+ airlines) | Action Required |
|:-----------:|:--------------------------------:|:----------------|
| 70-100% | ✅ Safe | Continue operations |
| 50-70% | ⚠️ Warning | Monitor closely, consider reducing frequency |
| <50% | ❌ Alert Active | Route license at risk, reduce frequency or improve marketing |

**Important**: Routes with 3+ competing airlines and load factor <50% for 52 consecutive weeks will have their license **revoked by airport authorities**.

## Frequency Decision Framework

### Decision Flowchart for Increasing Frequency

```
                    START
                      |
                      v
           Is frequency < 14? -------YES-----> [INCREASE TO 14]
                      |                              |
                      NO                             v
                      |                          [SUCCESS]
                      v
      Is frequency 14-27 AND LF > 70%? --YES--> [INCREASE TOWARD 28]
                      |                              |
                      NO                             v
                      |                       [MONITOR LF]
                      v
      Is frequency 28+ AND LF > 80%? ---YES--> [CONSIDER CAREFULLY]
                      |                         (diminishing returns)
                      NO                             |
                      |                              v
                      v                     [EVALUATE PROFIT]
           Is route intercontinental                 |
           with frequency > 21? ----YES----> [CAUTION: High costs]
                      |                              |
                      NO                             v
                      |                      [MAYBE INCREASE]
                      v
             Is LF < 70%? ------YES-----> [DO NOT INCREASE]
                      |                   Focus on marketing
                      NO
                      |
                      v
               [MAINTAIN CURRENT]

Legend: LF = Load Factor
```

### Decision Flowchart for Decreasing Frequency

```
                    START
                      |
                      v
         Is LF < 50% for weeks? ---YES----> [DECREASE URGENTLY]
                      |                    (license risk!)
                      NO                         |
                      |                          v
                      v                    [REDUCE TO SAFE LF]
      Is frequency > 28 and profitable? --YES--> [TEST REDUCTION]
                      |                      (no appeal loss)
                      NO                         |
                      |                          v
                      v                    [MONITOR RESULTS]
      Is route intercontinental with              |
      frequency > 21 and low profit? ---YES---->[REDUCE FREQUENCY]
                      |                      (high staff costs)
                      NO                         |
                      |                          v
                      v                     [IMPROVE PROFIT]
      Better uses for airplanes? ---YES-----> [REDEPLOY ASSETS]
                      |                            |
                      NO                           v
                      |                     [REALLOCATE FLEET]
                      v
            [MAINTAIN CURRENT]

Legend: LF = Load Factor
```

## Key Takeaways

1. **14 flights/week is the neutral baseline** for most passenger types
2. **28 flights/week is the optimal maximum** for passenger appeal (no benefits beyond this)
3. **Intercontinental routes have 50-100% higher staff costs** per flight
4. **All operational costs scale linearly** with no economies of scale
5. **No explicit penalties exist** for high frequency, only diminishing returns
6. **Load factor <50% for extended periods** can result in route license revocation on competitive routes
7. **Focus on quality and pricing** rather than excessive frequency for profitability

## Formula Reference

### Frequency Cost Adjustment
```
frequencyRatioDelta = max(-1, (threshold - frequency) / threshold) × sensitivity
finalAdjustment = 1 + max(-0.75, frequencyRatioDelta)
perceivedCost = standardPrice × finalAdjustment × other_factors
```

### Staff Requirements
```
totalStaff = (basicStaff + perFrequency × frequency + per1000Pax × capacity/1000) × baseModifier
```

### Load Factor
```
loadFactor = soldSeats / totalCapacity
```

---

*For detailed analysis, see ROUTE_FREQUENCY_ANALYSIS.md*
