package se.lexicon;

import java.util.List;
import java.util.stream.Collectors;

public class SubscriberProcessor {

    List<Subscriber> findSubscribers(List<Subscriber> list, SubscriberFilter filter) {
        return list.stream()
                .filter(filter::matches)
                .collect(Collectors.toList());
    }

    List<Subscriber> applyToMatching(List<Subscriber> list, SubscriberFilter filter, SubscriberAction action) {
        list.stream()
                .filter(filter::matches)
                .forEach(action::run);
        return list;
    }
}