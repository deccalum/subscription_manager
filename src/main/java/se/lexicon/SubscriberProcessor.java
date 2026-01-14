package se.lexicon;

import java.util.List;
import java.util.stream.Collectors;

class SubscriberProcessor {

    // List<Subscriber> subscribers = List.of(Subscribers...);

    // +findSubscribers(list: List~Subscriber~, filter: SubscriberFilter): List~Subscriber~
    public List<Subscriber> findSubscribers(List<Subscriber> list, SubscriberFilter filter) {
        return list.stream()
                .filter(filter::matches)
                .collect(Collectors.toList());
    }

    // +applyToMatching(list: List~Subscriber~, filter: SubscriberFilter, action: SubscriberAction): List~Subscriber~
    public List<Subscriber> applyToMatching(List<Subscriber> list, SubscriberFilter filter, SubscriberAction action) {
        list.stream()
                .filter(filter::matches)
                .forEach(action::run);
        return list;
    }
}

class Rules {
    public static SubscriberFilter activeSubscriber = subscriber -> subscriber.isActive(); // Active Subscriber
    public static SubscriberFilter expiringSubscription = subscriber -> subscriber.getMonthsRemaining() <= 1; // Expiring Subscription
    public static SubscriberFilter activeAndExpiringSubscriber = subscriber ->
            subscriber.isActive() && subscriber.getMonthsRemaining() <= 1; // Active and Expiring Subscriber
    public static SubscriberFilter subscriberByPlan(Plan plan) {
        return subscriber -> subscriber.getPlan() == plan; // Subscriber by Plan
    }
    public static SubscriberFilter payingSubscriber = subscriber ->
            subscriber.getPlan() == Plan.BASIC || subscriber.getPlan() == Plan.PRO; // Paying Subscriber
    public static SubscriberAction extendSubscription(int months) {
        return subscriber -> subscriber.setMonthsRemaining(subscriber.getMonthsRemaining() + months); // Extend Subscription
    }
    public static SubscriberAction deactivateSubscriber = subscriber -> subscriber.setActive(false); // Deactivate Subscriber
}

@FunctionalInterface
interface SubscriberAction {
    void run(Subscriber subscriber);
}

@FunctionalInterface
interface SubscriberFilter {
    boolean matches(Subscriber subscriber);
}