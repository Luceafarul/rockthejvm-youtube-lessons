package tips.scala

object SelfType {

  trait Edible
  trait Person {
    def hasAllergiesTo(thing: Edible): Boolean
  }
  trait Child extends Person
  trait Adult extends Person

  trait Diet { self: Person =>
    def eat(thing: Edible): Boolean =
      self.hasAllergiesTo(thing)
  }
  trait Carnivore  extends Diet with Person
  trait Vegetarian extends Diet with Person

  // Problem: trait Diet must be applicable only for trait Person
  // We can should implement both method,
  // but what if we should know about Person inside Diet
  // class VegetarianAthlete extends Vegetarian with Adult

  // Option 1: enforce a subtype relationship
  //           extends Person directly and we got access to Person's method:
  //   trait Diet extends Person {
  //    def eat(thing: Edible): Boolean =
  //      if (this.hasAllergiesTo(thing)) false
  //      else true
  //  }

  // Option 2: add a type argument

  // Option 3: self type
  //   trait Diet { self: Person =>
  //    def eat(thing: Edible): Boolean =
  //      self.hasAllergiesTo(thing)
  //  }
  // This construction means that
  // whoever extends Diet must also extends Person
  // self: Person =>

  // now we can create VegetarianAthlete without any problem
  class VegetarianAthlete extends Vegetarian with Adult {
    def hasAllergiesTo(thing: Edible): Boolean = false
  }

  // Inheritance means IS AN relation
  // Self-type it's REQUIRE relation
}
