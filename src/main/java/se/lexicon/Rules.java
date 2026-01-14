package se.lexicon;

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
}