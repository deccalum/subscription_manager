package se.lexicon;

import java.util.function.Predicate;

public class SubscriberFilters {

    public static Predicate<Subscriber> active() {
        return Subscriber::isActive;
    }

    public static Predicate<Subscriber> totalMonths(int months) {
        return s -> s.getMonthsRemaining() == months;
    }

    public static Predicate<Subscriber> isExpiringWithin(int months) {
        return s -> s.getMonthsRemaining() <= months;
    }
    
    public static Predicate<Subscriber> withPlan(Plan plan) {
        return s -> s.getPlan() == plan;
    }
    
    public static Predicate<Subscriber> isPaying() {
        return s -> s.getPlan() != Plan.FREE;
    }
}
