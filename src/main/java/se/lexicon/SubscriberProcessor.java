package se.lexicon;

import java.util.List;
import java.util.function.Predicate;

interface SubscriberProcessor {

    // List<Subscriber> subscribers = List.of(Subscribers...);

    Predicate<Subscriber> isActive = Subscriber::isActive;
    Predicate<Subscriber> isExpiring = Subscriber -> Subscriber.getMonthsRemaining() <= 1;
    Predicate<Subscriber> isActiveAndExpiring = isActive.and(isExpiring);

    // +findSubscribers(list: List~Subscriber~, filter: SubscriberFilter): List~Subscriber~
    List<Subscriber> findSubscribers(List<Subscriber> list, SubscriberFilter filter);

    // +applyToMatching(list: List~Subscriber~, filter: SubscriberFilter, action: SubscriberAction): List~Subscriber~
    List<Subscriber> applyToMatching(List<Subscriber> list, SubscriberFilter filter, SubscriberAction action);
}

@FunctionalInterface
interface SubscriberAction {
    // +run(subscriber: Subscriber): void
    void run(Subscriber subscriber);

}

@FunctionalInterface
interface SubscriberFilter {
    // +matches(subscriber: Subscriber): boolean
    boolean matches(Subscriber subscriber);
}