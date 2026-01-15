package se.lexicon;

import java.util.function.Consumer;

public class SubscriberActions {
    public static Consumer<Subscriber> deactivate() {
        return s -> s.setActive(false);
    }
    
    public static Consumer<Subscriber> extendBy(int months) {
        return s -> s.setMonthsRemaining(s.getMonthsRemaining() + months);
    }
}