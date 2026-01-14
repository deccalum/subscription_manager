package se.lexicon;

@FunctionalInterface
public interface SubscriberAction {
    void run(Subscriber subscriber);
}