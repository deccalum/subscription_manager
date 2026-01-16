package se.lexicon;

public class Rules {

    // Filter Rules
    public static final SubscriberFilter activeSubscriber = Subscriber::isActive;

    public static final SubscriberFilter expiringSubscription = subscriber -> subscriber.getMonthsRemaining() <= 1;

    public static final SubscriberFilter activeAndExpiringSubscriber = subscriber ->
            subscriber.isActive() && subscriber.getMonthsRemaining() <= 1;

    public static final SubscriberFilter payingSubscriber = subscriber ->
            subscriber.getPlan() == Plan.BASIC || subscriber.getPlan() == Plan.PRO;

    public static SubscriberFilter subscriberByPlan(Plan plan) {
        return subscriber -> subscriber.getPlan() == plan;
    }

    // Action Rules
    public static SubscriberAction extendSubscription(int months) {
        return subscriber -> subscriber.setMonthsRemaining(subscriber.getMonthsRemaining() + months);
    }

    public static final SubscriberAction deactivateSubscriber = subscriber -> subscriber.setActive(false);
}
