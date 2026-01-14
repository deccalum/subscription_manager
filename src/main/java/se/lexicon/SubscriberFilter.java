package se.lexicon;

@FunctionalInterface
public interface SubscriberFilter {
    boolean matches(Subscriber subscriber);
}