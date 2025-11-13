package com.patson.model

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.testkit.{ImplicitSender, TestKit}
import com.patson.Util
import com.patson.model.airplane.{Airplane, AirplaneConfiguration, LinkAssignment, Model}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpecLike}

import scala.collection.immutable.Map

class LinkSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {
 
  def this() = this(ActorSystem("MySpec"))

  val testAirline1 = Airline("airline 1", id = 1)
  val fromAirport = Airport("", "", "From Airport", 0, 0, "", "", "", 1, baseIncome = 40000, basePopulation = 1, 0, 0)
  val toAirport = Airport("", "", "To Airport", 0, 180, "", "", "", 1, baseIncome = 40000, basePopulation = 1, 0, 0)
  val distance = Util.calculateDistance(fromAirport.latitude, fromAirport.longitude, toAirport.latitude, toAirport.longitude).toInt
  val defaultPrice = Pricing.computeStandardPriceForAllClass(distance, fromAirport, toAirport)

  val flightType = Computation.getFlightType(fromAirport, toAirport, distance)
  val model = Model.modelByName("Boeing 737 MAX 9")




  "frequencyByClass".must {
    "compute correct frequency".in {

      val config1 = AirplaneConfiguration(100, 0, 0, testAirline1, model, false)
      val config2 = AirplaneConfiguration(50, 25, 0, testAirline1, model, false)
      val config3 = AirplaneConfiguration(50, 10, 5, testAirline1, model, false)
      val airline1Link = Link(fromAirport, toAirport, testAirline1, defaultPrice, distance = distance, LinkClassValues.getInstance(200, 35, 5) * 10, rawQuality = 0, 600, frequency = 30, flightType)

      airline1Link.setAssignedAirplanes(
        scala.collection.immutable.Map(
          Airplane(model, testAirline1, 0, purchasedCycle = 0, 100, 0, 0, configuration = config1) -> LinkAssignment(10, 6000)
        , Airplane(model, testAirline1, 0, purchasedCycle = 0, 100, 0, 0, configuration = config2) -> LinkAssignment(10, 6000)
        , Airplane(model, testAirline1, 0, purchasedCycle = 0, 100, 0, 0, configuration = config3) -> LinkAssignment(10, 6000)))

      assert(airline1Link.frequencyByClass(ECONOMY) == 30)
      assert(airline1Link.frequencyByClass(BUSINESS) == 20)
      assert(airline1Link.frequencyByClass(FIRST) == 10)
    }
  }

  "staffScheme".must {
    "have correct basic staff for SHORT_HAUL_DOMESTIC".in {
      val breakdown = Link.staffScheme(FlightType.SHORT_HAUL_DOMESTIC)
      assert(breakdown.basic == 8)
      assert(breakdown.perFrequency == 0.8)
      assert(breakdown.per1000Pax == 2)
    }

    "have correct basic staff for MEDIUM_HAUL_DOMESTIC".in {
      val breakdown = Link.staffScheme(FlightType.MEDIUM_HAUL_DOMESTIC)
      assert(breakdown.basic == 10)
      assert(breakdown.perFrequency == 0.8)
      assert(breakdown.per1000Pax == 2)
    }

    "have correct basic staff for LONG_HAUL_DOMESTIC".in {
      val breakdown = Link.staffScheme(FlightType.LONG_HAUL_DOMESTIC)
      assert(breakdown.basic == 12)
      assert(breakdown.perFrequency == 0.8)
      assert(breakdown.per1000Pax == 2)
    }

    "have correct basic staff for SHORT_HAUL_INTERNATIONAL".in {
      val breakdown = Link.staffScheme(FlightType.SHORT_HAUL_INTERNATIONAL)
      assert(breakdown.basic == 10)
      assert(breakdown.perFrequency == 0.8)
      assert(breakdown.per1000Pax == 2)
    }

    "have correct basic staff for MEDIUM_HAUL_INTERNATIONAL".in {
      val breakdown = Link.staffScheme(FlightType.MEDIUM_HAUL_INTERNATIONAL)
      assert(breakdown.basic == 15)
      assert(breakdown.perFrequency == 0.8)
      assert(breakdown.per1000Pax == 2)
    }

    "have correct basic staff for LONG_HAUL_INTERNATIONAL".in {
      val breakdown = Link.staffScheme(FlightType.LONG_HAUL_INTERNATIONAL)
      assert(breakdown.basic == 20)
      assert(breakdown.perFrequency == 0.8)
      assert(breakdown.per1000Pax == 2)
    }

    "have correct basic staff for SHORT_HAUL_INTERCONTINENTAL".in {
      val breakdown = Link.staffScheme(FlightType.SHORT_HAUL_INTERCONTINENTAL)
      assert(breakdown.basic == 15)
      assert(breakdown.perFrequency == 1.2)
      assert(breakdown.per1000Pax == 3)
    }

    "have correct basic staff for MEDIUM_HAUL_INTERCONTINENTAL".in {
      val breakdown = Link.staffScheme(FlightType.MEDIUM_HAUL_INTERCONTINENTAL)
      assert(breakdown.basic == 25)
      assert(breakdown.perFrequency == 1.2)
      assert(breakdown.per1000Pax == 3)
    }

    "have correct basic staff for LONG_HAUL_INTERCONTINENTAL".in {
      val breakdown = Link.staffScheme(FlightType.LONG_HAUL_INTERCONTINENTAL)
      assert(breakdown.basic == 30)
      assert(breakdown.perFrequency == 1.6)
      assert(breakdown.per1000Pax == 4)
    }

    "have correct basic staff for ULTRA_LONG_HAUL_INTERCONTINENTAL".in {
      val breakdown = Link.staffScheme(FlightType.ULTRA_LONG_HAUL_INTERCONTINENTAL)
      assert(breakdown.basic == 30)
      assert(breakdown.perFrequency == 1.6)
      assert(breakdown.per1000Pax == 4)
    }
  }

  "getOfficeStaffBreakdown".must {
    "calculate correct staff for short-haul domestic route".in {
      val frequency = 7
      val capacity = LinkClassValues.getInstance(1400, 0, 0)
      val link = Link(fromAirport, toAirport, testAirline1, defaultPrice, distance = 500, capacity, rawQuality = 0, 600, frequency, FlightType.SHORT_HAUL_DOMESTIC)
      
      val breakdown = link.getOfficeStaffBreakdown(fromAirport, toAirport, frequency, capacity)
      
      // Basic: 8, Frequency: 0.8 * 7 = 5.6, Capacity: 2 * 1.4 = 2.8
      // Total: (8 + 5.6 + 2.8) * 1.0 = 16.4 = 16
      assert(breakdown.basicStaff == 8)
      assert(breakdown.frequencyStaff == 5.6)
      assert(breakdown.capacityStaff == 2.8)
      assert(breakdown.total == 16)
    }

    "calculate correct staff for long-haul intercontinental route".in {
      val frequency = 14
      val capacity = LinkClassValues.getInstance(4200, 0, 0)
      val link = Link(fromAirport, toAirport, testAirline1, defaultPrice, distance = 10000, capacity, rawQuality = 0, 600, frequency, FlightType.LONG_HAUL_INTERCONTINENTAL)
      
      val breakdown = link.getOfficeStaffBreakdown(fromAirport, toAirport, frequency, capacity)
      
      // Basic: 30, Frequency: 1.6 * 14 = 22.4, Capacity: 4 * 4.2 = 16.8
      // Total: (30 + 22.4 + 16.8) * 1.0 = 69.2 = 69
      assert(breakdown.basicStaff == 30)
      assert(breakdown.frequencyStaff == 22.4)
      assert(breakdown.capacityStaff == 16.8)
      assert(breakdown.total == 69)
    }

    "return zero staff for zero frequency".in {
      val frequency = 0
      val capacity = LinkClassValues.getInstance(0, 0, 0)
      val link = Link(fromAirport, toAirport, testAirline1, defaultPrice, distance = 500, capacity, rawQuality = 0, 600, frequency, FlightType.SHORT_HAUL_DOMESTIC)
      
      val breakdown = link.getOfficeStaffBreakdown(fromAirport, toAirport, frequency, capacity)
      
      assert(breakdown.basicStaff == 0)
      assert(breakdown.frequencyStaff == 0)
      assert(breakdown.capacityStaff == 0)
      assert(breakdown.total == 0)
    }
  }
}
