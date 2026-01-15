package se.lexicon;

import java.util.List;

public class Rules {

    public static SubscriberFilter activeSubscriber = Subscriber::isActive; // Active Subscriber
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

    public static void executeAll(List<Subscriber> subscribers) {
        System.out.println("Active: " + subscribers.stream().filter(activeSubscriber::matches).count());
        System.out.println("Expiring: " + subscribers.stream().filter(expiringSubscription::matches).count());
        System.out.println("Active and Expiring: " + subscribers.stream().filter(activeAndExpiringSubscriber::matches).count());
        System.out.println("Paying: " + subscribers.stream().filter(payingSubscriber::matches).count());
        System.out.println("Free: " + subscribers.stream().filter(subscriberByPlan(Plan.FREE)::matches).count());
        System.out.println("Basic: " + subscribers.stream().filter(subscriberByPlan(Plan.BASIC)::matches).count());
        System.out.println("Pro: " + subscribers.stream().filter(subscriberByPlan(Plan.PRO)::matches).count());
        System.out.println("Paying:" + subscribers.stream().filter(payingSubscriber::matches).count());
        
        SubscriberProcessor processor = new SubscriberProcessor();
        processor.applyToMatching(subscribers, expiringSubscription, extendSubscription(3));
        processor.applyToMatching(subscribers, subscriberByPlan(Plan.FREE), deactivateSubscriber);
    }
}