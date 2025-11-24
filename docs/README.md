# Documentation Index: Route Frequency Analysis

This directory contains comprehensive documentation on route frequency mechanics in the Airline Tycoon game.

## Quick Start

**New to frequency optimization?** Start here:
1. Read [FREQUENCY_QUICK_REFERENCE.md](FREQUENCY_QUICK_REFERENCE.md) for practical guidelines (7KB, 5 min read)
2. Use the decision flowcharts to adjust your routes
3. Refer to the numerical thresholds table for quick decisions

**Want deeper understanding?** Continue with:
1. [ROUTE_FREQUENCY_ANALYSIS.md](ROUTE_FREQUENCY_ANALYSIS.md) for technical details (18KB, 15 min read)
2. [FREQUENCY_VISUAL_ANALYSIS.md](FREQUENCY_VISUAL_ANALYSIS.md) for visual analysis (13KB, 10 min read)

## Documents Overview

### 1. FREQUENCY_QUICK_REFERENCE.md
**Purpose**: Practical, actionable reference for route frequency optimization  
**Size**: 7KB  
**Reading Time**: ~5 minutes  
**Best For**: Quick decisions, in-game reference

**Contains**:
- ✅ Frequency threshold tables by passenger type
- ✅ Staff cost comparison by route type
- ✅ Decision flowcharts (when to increase/decrease frequency)
- ✅ Load factor warning thresholds
- ✅ Recommended frequencies by route type
- ✅ Example cost calculations

**Use This When**:
- Planning a new route
- Deciding whether to add/remove flights
- Troubleshooting low profitability
- Managing load factor warnings

---

### 2. ROUTE_FREQUENCY_ANALYSIS.md
**Purpose**: Comprehensive technical analysis of frequency mechanics  
**Size**: 18KB  
**Reading Time**: ~15 minutes  
**Best For**: Understanding the underlying game mechanics

**Contains**:
- ✅ Complete frequency mechanics breakdown
- ✅ Passenger preference system deep dive
- ✅ Operational cost formulas and calculations
- ✅ Route type classification (10 types)
- ✅ Code references with exact line numbers
- ✅ Mathematical formulas with explanations
- ✅ Optimization strategies by route type
- ✅ Critical numerical thresholds reference

**Use This When**:
- Building a route optimization tool
- Developing advanced strategies
- Understanding code implementation
- Analyzing edge cases

---

### 3. FREQUENCY_VISUAL_ANALYSIS.md
**Purpose**: Visual representations of frequency relationships  
**Size**: 13KB  
**Reading Time**: ~10 minutes  
**Best For**: Understanding trends and relationships visually

**Contains**:
- ✅ ASCII charts showing benefit curves
- ✅ Passenger appeal vs. frequency graphs
- ✅ Cost vs. revenue optimization curves
- ✅ Break-even frequency analysis
- ✅ Decision matrices with visual guides
- ✅ Marginal benefit charts
- ✅ Comparative analysis (frequency vs. airplane size)

**Use This When**:
- Comparing different frequency strategies
- Understanding diminishing returns visually
- Explaining concepts to others
- Analyzing trade-offs

---

## Key Findings Summary

### ✅ Diminishing Returns: YES
Passenger appeal benefits **plateau at 28 flights/week** with no additional gains beyond this threshold.

### ❌ Penalties: NO
No explicit code penalties exist for high frequency, but implicit economic pressures create natural limits.

### 📊 Critical Thresholds

| Threshold | Value | Impact |
|-----------|------:|--------|
| Budget neutral | 3 flights/week | Baseline for price-sensitive passengers |
| Standard neutral | 14 flights/week | Baseline for most passengers |
| Maximum benefit | 28 flights/week | Absolute ceiling, no gains beyond |
| License risk | <50% LF for 52 weeks | Route can be revoked |

### 💰 Cost Multipliers by Route

| Route Type | Staff Cost Multiplier |
|------------|:---------------------:|
| Domestic | 1.0× (baseline) |
| International (same zone) | 1.0× (baseline) |
| Short/Med Intercontinental | 1.5× (+50%) |
| Long/Ultra Intercontinental | 2.0× (+100%) |

### 🎯 Optimal Frequencies

| Route Type | Target |
|------------|:------:|
| Domestic/International | 21-28 /week |
| Short/Med Intercontinental | 14-21 /week |
| Long/Ultra Intercontinental | 7-14 /week |

---

## Code References

All documentation is based on analysis of the following source files:

### Core Files
- `airline-data/src/main/scala/com/patson/model/Link.scala`
  - Lines 15-185: Link model and frequency handling
  - Lines 187-245: Staff cost schemes by route type

- `airline-data/src/main/scala/com/patson/model/FlightPreference.scala`
  - Lines 14-223: Preference abstract class
  - Lines 252-284: SimplePreference (budget travelers)
  - Lines 286-311: SpeedPreference (time-sensitive travelers)
  - Lines 313-367: AppealPreference (quality-conscious travelers)

- `airline-data/src/main/scala/com/patson/LinkSimulation.scala`
  - Lines 221-376: Cost calculations
  - Lines 416-483: Load factor monitoring

- `airline-data/src/main/scala/com/patson/model/Computation.scala`
  - Lines 100-135: Route type classification

### Test Files
- `airline-data/src/test/scala/com/patson/model/FlightPreferenceSpec.scala`
  - Validates frequency behavior

---

## Formulas Quick Reference

### Frequency Cost Adjustment
```
frequencyRatioDelta = max(-1, (threshold - frequency) / threshold) × sensitivity
finalAdjustment = 1 + max(-0.75, frequencyRatioDelta)
perceivedCost = standardPrice × finalAdjustment × otherFactors
```

### Staff Requirements
```
totalStaff = (basic + perFrequency × frequency + per1000Pax × capacity/1000) × modifier
```

### Load Factor
```
loadFactor = soldSeats / totalCapacity
```

---

## Document History

- **v1.0** (2025-11-24): Initial comprehensive analysis
  - Created ROUTE_FREQUENCY_ANALYSIS.md
  - Created FREQUENCY_QUICK_REFERENCE.md
  - Created FREQUENCY_VISUAL_ANALYSIS.md
  
- **v1.1** (2025-11-24): Code review improvements
  - Added numerical thresholds quick reference
  - Improved ASCII chart symbols for clarity
  - Added visual decision flowcharts
  - Enhanced conclusion sections

---

## How to Contribute

If you find errors or have suggestions for improvements:

1. **Documentation errors**: Please open an issue describing the error
2. **Game mechanics changes**: If the game code changes, documentation may need updates
3. **Additional analysis**: Suggest new analysis topics or visualizations

---

## License

This documentation is part of the Airline Tycoon project. Refer to the main LICENSE file for details.

---

## Questions?

For questions about:
- **Game mechanics**: Refer to ROUTE_FREQUENCY_ANALYSIS.md
- **Practical usage**: Refer to FREQUENCY_QUICK_REFERENCE.md
- **Visual understanding**: Refer to FREQUENCY_VISUAL_ANALYSIS.md
- **Code implementation**: Check the code references sections

---

**Total Documentation Size**: 38KB across 3 documents  
**Total Reading Time**: ~30 minutes for complete understanding  
**Code Coverage**: 5 core files, 1 test file, 100% of frequency-related mechanics

Last Updated: 2025-11-24
