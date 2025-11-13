# Staff Requirements Quick Reference

## Summary Table

| Route Type | Basic Staff | Staff/Freq | Staff/1000 Pax | Multiply Factor |
|------------|-------------|------------|----------------|-----------------|
| **Domestic Routes** |
| Short-haul Domestic | 8 | 0.8 | 2 | 2 |
| Medium-haul Domestic | 10 | 0.8 | 2 | 2 |
| Long-haul Domestic | 12 | 0.8 | 2 | 2 |
| **International Routes** |
| Short-haul International | 10 | 0.8 | 2 | 2 |
| Medium-haul International | 15 | 0.8 | 2 | 2 |
| Long-haul International | 20 | 0.8 | 2 | 2 |
| **Intercontinental Routes** |
| Short-haul Intercontinental | 15 | 1.2 | 3 | 3 |
| Medium-haul Intercontinental | 25 | 1.2 | 3 | 3 |
| Long-haul Intercontinental | 30 | 1.6 | 4 | 4 |
| Ultra Long-haul Intercontinental | 30 | 1.6 | 4 | 4 |

## Calculation Formula

```
Total Staff = (Basic Staff + Staff/Freq × Frequency + Staff/1000 Pax × Total Capacity / 1000) × Base Modifier
```

Where:
- **Frequency**: Number of flights per week
- **Total Capacity**: Total passenger capacity per week across all classes
- **Base Modifier**: Efficiency modifier from airline base (default: 1.0)

## Key Observations

1. **Domestic routes** require the least staff per frequency (0.8) and per 1000 passengers (2)
2. **International routes** have moderate requirements, same as domestic for frequency/capacity but higher base staff
3. **Intercontinental routes** require the most staff, with multiply factors of 3-4
4. **Basic staff** increases with route complexity:
   - Domestic: 8-12
   - International: 10-20
   - Intercontinental: 15-30

## Route Classification

Routes are classified based on:
- **Distance**: Short-haul, Medium-haul, Long-haul, Ultra Long-haul
- **Type**: Domestic (same country), International (same continent), Intercontinental (different continents)

For detailed documentation, see [STAFF_REQUIREMENTS.md](STAFF_REQUIREMENTS.md)
