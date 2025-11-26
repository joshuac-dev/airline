# Country Openness Analysis: Drawbacks of Bases in Low Openness Countries

## Executive Summary

This document provides a comprehensive analysis of the drawbacks associated with establishing an airline base in a country with a market openness rating below 7 (on a scale of 1-10). The analysis is based on an extensive review of the airline game simulation codebase.

Countries with openness below 7 are classified as having "No International Connection" in the game, as opposed to countries with openness ≥ 7 which have an "Opened Market". This classification has significant implications for airline operations, passenger routing, and overall business viability.

---

## Table of Contents

1. [Understanding Country Openness](#understanding-country-openness)
2. [Key Openness Thresholds](#key-openness-thresholds)
3. [Drawbacks Analysis](#drawbacks-analysis)
   - [Sixth Freedom Restrictions](#1-sixth-freedom-restrictions)
   - [Negotiation Requirements](#2-negotiation-requirements)
   - [Gateway Airport Considerations](#3-gateway-airport-considerations)
   - [Transfer and Transit Passengers](#4-transfer-and-transit-passengers)
   - [Route Planning Limitations](#5-route-planning-limitations)
4. [Code References](#code-references)
5. [Practical Implications](#practical-implications)
6. [Recommendations](#recommendations)

---

## Understanding Country Openness

Country openness is a metric representing how accessible a country's aviation market is to foreign airlines. The value ranges from 0 to 10, where:

- **10**: Most open/business-friendly market
- **7+**: "Opened Market" - Full international connection capabilities
- **< 7**: "No International Connection" - Restricted sixth freedom rights

The openness value is derived from the World Bank's "Ease of Doing Business" index, translated into a game-appropriate scale using the following formula:

```
opennessValue = (200 - opennessRanking) / 20 + 1
```

Where countries with rankings above 200 receive an openness value of 0.

**Source**: `airline-data/openness.csv`, `airline-data/src/main/scala/com/patson/init/GeoDataGenerator.scala` (lines 488-540)

---

## Key Openness Thresholds

The game defines several critical openness thresholds in `Country.scala`:

| Constant | Value | Description |
|----------|-------|-------------|
| `MAX_OPENNESS` | 10 | Maximum possible openness rating |
| `SIXTH_FREEDOM_MIN_OPENNESS` | 7 | Minimum openness for sixth freedom traffic rights |
| `OPEN_DOMESTIC_MARKET_MIN_OPENNESS` | 4 | Minimum for open domestic market |
| `INTERNATIONAL_INBOUND_MIN_OPENNESS` | 2 | Minimum for international inbound flights |

**Source**: `airline-data/src/main/scala/com/patson/model/Country.scala` (lines 21-25)

---

## Drawbacks Analysis

### 1. Sixth Freedom Restrictions

**This is the PRIMARY and MOST SIGNIFICANT drawback of operating in a country with openness below 7.**

#### What is Sixth Freedom?

Sixth freedom rights allow an airline to carry passengers between two foreign countries via a stopover in the airline's home country. For example, a Singapore Airlines flight carrying passengers from Australia to Europe via Singapore.

#### How It Works in the Game

The `hasFreedom` function in `PassengerSimulation.scala` determines whether a flight segment can carry international transfer passengers:

```scala
def hasFreedom(linkConsideration : LinkConsideration, originatingAirport : Airport, countryOpenness : Map[String, Int]) : Boolean = {
  if (linkConsideration.from.countryCode == linkConsideration.to.countryCode) { // domestic flight is always ok
    true
  } else if (linkConsideration.from.countryCode == originatingAirport.countryCode) { // always ok if flying out from same country as origin
    true
  } else { // a foreign airline flying out carrying passengers originating from a foreign airport
    countryOpenness(linkConsideration.from.countryCode) >= Country.SIXTH_FREEDOM_MIN_OPENNESS
  }
}
```

#### Specific Impacts

**Impact on Transfer Passengers:**
- Airlines based in countries with openness < 7 **CANNOT** act as hubs for international-to-international transfer traffic
- Passengers originating from Country A cannot use Country B (openness < 7) as a connection point to reach Country C
- This fundamentally limits the airline's ability to build a hub-and-spoke network for intercontinental traffic

**Example Scenario:**
- Country C1 (openness 5) → Country C2 (openness 5) → Country C3 (openness 10)
- Passengers from C1 can travel to C3 only if C2 has openness ≥ 7
- If C2's openness is 5 (< 7), the route C1 → C2 → C3 is BLOCKED for passengers originating in C1

**Route Finding Impact:**
The route-finding algorithm explicitly checks sixth freedom rights, meaning routes through low-openness countries are excluded from consideration entirely during the shortest-path computation.

**Source**: `airline-data/src/main/scala/com/patson/PassengerSimulation.scala` (lines 472-479)

**Verified in Tests**: `PassengerSimulationSpec.scala` includes specific tests verifying this behavior:
- "find routes with low country openness if the original passenger is domestic or it's a domestic connection flight" (line 400)
- Tests confirm that C2 with openness 5 blocks sixth freedom traffic (lines 446, 495)

---

### 2. Negotiation Requirements

Airlines face increased difficulty when negotiating for routes in and out of low-openness countries.

#### Foreign Airline Penalty

When a foreign airline negotiates for flights to/from a closed market country, there is an additional negotiation requirement:

```scala
if (homeCountryCode != airport.countryCode) { // closed countries are anti foreign airlines
  var baseForeignAirline = (14 - country.openness) * 0.5
  if (existingLinkOption.isDefined) { // cheaper if it's already established
    baseForeignAirline = baseForeignAirline * 0.5
  }
  requirements.append(NegotiationRequirement(FOREIGN_AIRLINE, baseForeignAirline, "Foreign Airline"))
}
```

**Calculation Examples:**

| Country Openness | Foreign Airline Penalty (New Link) | Foreign Airline Penalty (Existing Link) |
|-----------------|-----------------------------------|----------------------------------------|
| 10 | (14-10) × 0.5 = 2.0 | 1.0 |
| 7 | (14-7) × 0.5 = 3.5 | 1.75 |
| 5 | (14-5) × 0.5 = 4.5 | 2.25 |
| 3 | (14-3) × 0.5 = 5.5 | 2.75 |
| 1 | (14-1) × 0.5 = 6.5 | 3.25 |

**Impact:** Lower openness = Higher negotiation difficulty = More delegates required = Longer time to establish routes

**Source**: `airline-web/app/controllers/NegotiationUtil.scala` (lines 219-225)

---

### 3. Gateway Airport Considerations

For NEW international routes to non-gateway airports in low-openness countries, there is an additional penalty:

```scala
if (flightCategory != FlightCategory.DOMESTIC) {
  airport.getFeatures().find(_.featureType == AirportFeatureType.GATEWAY_AIRPORT) match {
    case Some(_) => // OK - no penalty
    case None =>
      val nonGatewayCost = (14 - country.openness) * 0.15 * flightTypeMultiplier
      requirements.append(NegotiationRequirement(NON_GATEWAY, nonGatewayCost, "International flight to non-gateway"))
  }
}
```

**Flight Type Multipliers:**

| Flight Type | Multiplier |
|-------------|-----------|
| Short Haul Domestic | 1 |
| Medium/Long Haul Domestic | 1.5 |
| Short Haul International | 2 |
| Medium/Long Haul International | 2-2.5 |
| Intercontinental | 4-5 |

**Example Non-Gateway Penalties (for Medium Haul International, multiplier 2):**

| Country Openness | Non-Gateway Penalty |
|-----------------|---------------------|
| 10 | (14-10) × 0.15 × 2 = 1.2 |
| 7 | (14-7) × 0.15 × 2 = 2.1 |
| 5 | (14-5) × 0.15 × 2 = 2.7 |
| 3 | (14-3) × 0.15 × 2 = 3.3 |

**Note:** This penalty ONLY applies to new links, not existing ones.

**Source**: `airline-web/app/controllers/NegotiationUtil.scala` (lines 246-253)

---

### 4. Transfer and Transit Passengers

The impact on transfer passengers is multi-faceted:

#### Direct Impact: No International Transfers
- As detailed in Section 1, countries with openness < 7 cannot serve as international transfer hubs
- This eliminates a significant revenue stream for hub operations

#### Indirect Impact: Reduced Demand
- The route-finding algorithm excludes low-openness country routes for international transfers
- This means fewer potential passengers will even consider routes via these countries
- Reduced passenger demand = Lower load factors = Lower profitability

#### Domestic Connections Exception
The code does allow domestic connection flights through low-openness countries:
- If passengers are traveling within the same country as the low-openness transfer point, transfers ARE allowed
- This is specifically tested and confirmed in the codebase

**Test Reference:** "find routes with low country openness if the original passenger is domestic or it's a domestic connection flight" - `PassengerSimulationSpec.scala`

---

### 5. Route Planning Limitations

#### Hub-and-Spoke Model Restrictions
Airlines in low-openness countries face severe limitations in building efficient hub-and-spoke networks:

1. **Cannot aggregate international traffic**: International passengers cannot use the hub for connections to third countries
2. **Limited to point-to-point operations**: Primary revenue must come from origin-destination passengers, not transfer traffic
3. **Reduced network effects**: Traditional hub benefits (connection multiplier effect) don't apply for international routes

#### Geographic Positioning Becomes Less Valuable
- Even if a low-openness country is geographically well-positioned (e.g., between two major markets), it cannot leverage this advantage for sixth freedom operations
- This neutralizes one of the key competitive advantages of strategic hub locations

---

## Code References

### Primary Files

| File | Lines | Purpose |
|------|-------|---------|
| `Country.scala` | 21-25 | Openness threshold constants |
| `PassengerSimulation.scala` | 26, 472-479 | Openness loading and freedom checking |
| `NegotiationUtil.scala` | 219-225, 246-253 | Foreign airline and non-gateway penalties |
| `gadgets.js` | 381-413 | UI display for openness status |

### Test Coverage

| File | Lines | Description |
|------|-------|-------------|
| `PassengerSimulationSpec.scala` | 298-352 | Routes with sufficient openness |
| `PassengerSimulationSpec.scala` | 354-398 | Routes blocked by insufficient openness |
| `PassengerSimulationSpec.scala` | 400-447 | Domestic connection exception |
| `PassengerSimulationSpec.scala` | 449-496 | Inverted link direction tests |

---

## Practical Implications

### For New Players
1. **Avoid starting in low-openness countries** if your goal is to build an international hub carrier
2. If you must start in a low-openness country, focus on:
   - Building domestic network strength
   - Point-to-point international routes
   - Accumulating capital to eventually establish bases in open markets

### For Experienced Players
1. **Hub Strategy**: Do not invest heavily in hub infrastructure in countries with openness < 7
2. **Secondary Bases**: Use low-openness countries for:
   - Local/regional point-to-point services
   - Specialized markets (tourism, business travel)
   - Not for intercontinental connecting traffic

### Business Model Considerations
1. **Point-to-Point Carrier Model**: Works reasonably well in low-openness countries
2. **Hub Carrier Model**: Significantly impaired, should be avoided
3. **Hybrid Approach**: Use open-market hubs for international connecting traffic; low-openness bases for regional operations

---

## Recommendations

### If Operating in a Low-Openness Country

1. **Focus on Domestic Market First**
   - Build strong domestic route network
   - Domestic transfers are NOT affected by openness restrictions
   - Use this to build revenue base and airline reputation

2. **Target Gateway Airports for International Expansion**
   - Gateway airports have lower negotiation penalties
   - Prioritize gateway-to-gateway international routes

3. **Consider Establishing Foreign Hubs**
   - For true hub-and-spoke operations, establish bases in high-openness countries
   - Use your home country base for local operations only

4. **Monitor Country Relationship**
   - Build relationship with your home country to offset some negotiation penalties
   - Relationship bonuses can partially compensate for openness penalties

### Strategic Planning

1. **Long-Term Hub Selection**: Choose hub locations with openness ≥ 7 for any serious international connecting traffic strategy

2. **Route Portfolio Balance**: Maintain a mix of:
   - High-margin point-to-point international routes
   - Domestic routes for base load
   - Avoid relying on transfer traffic in low-openness markets

3. **Market Analysis**: When evaluating new markets, factor in openness as a key strategic consideration alongside demand, income, and competition

---

## Appendix A: Summary Comparison

| Aspect | Openness ≥ 7 | Openness < 7 |
|--------|--------------|--------------|
| Sixth Freedom Rights | ✅ Allowed | ❌ Blocked |
| International Transfer Hub | ✅ Viable | ❌ Not Viable |
| Foreign Airline Penalty | Lower | Higher |
| Non-Gateway Penalty | Lower | Higher |
| Domestic Transfers | ✅ Works | ✅ Works |
| Point-to-Point International | ✅ Works | ✅ Works (with penalties) |
| Hub-and-Spoke Model | ✅ Optimal | ⚠️ Limited to domestic |
| Geographic Advantage | ✅ Leverageable | ❌ Cannot be leveraged |

---

## Appendix B: Openness Values by Example Countries

From `openness.csv`, showing the calculated openness based on ease of doing business rankings:

| Country | Ranking | Openness Value |
|---------|---------|---------------|
| Singapore | 1 | 10+ (capped at 10) |
| New Zealand | 2 | 10 |
| Denmark | 3 | 10 |
| South Korea | 4 | 10 |
| Hong Kong | 5 | 10 |
| United Kingdom | 6 | 10 |
| United States | 7 | 10 |
| Sweden | 8-9 | 10 |
| Norway | 8-9 | 10 |
| Finland | 10 | 10 |
| ... | ... | ... |
| Countries ranked ~120-140 | | 4-5 |
| Countries ranked ~160-180 | | 2-3 |
| Countries ranked 180+ | | 1-0 |

Countries below rank ~140 typically have openness values below 7, making them unsuitable for international hub operations.

---

## Conclusion

The primary drawback of having a base in a country with openness below 7 is the **complete inability to handle international-to-international transfer traffic** (sixth freedom restrictions). This is not just about difficulty—it's a hard block in the passenger routing algorithm.

Secondary drawbacks include:
- Higher negotiation requirements for foreign airlines
- Additional penalties for non-gateway airport international routes
- Fundamental limitations on hub-and-spoke network viability

While point-to-point operations remain viable, the strategic value of such bases is significantly reduced compared to bases in countries with openness ≥ 7. Players should carefully consider these limitations when planning their airline network strategy.

---

*Document generated based on code analysis of the Airline Club game repository.*
*Last updated: 2025*
