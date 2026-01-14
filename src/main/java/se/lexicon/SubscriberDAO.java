package se.lexicon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubscriberDAO {

    private final List<Subscriber> allSubscribers = new ArrayList<>();

        public void save(Subscriber subscriber) {
            allSubscribers.add(subscriber);
        }
    
        public List<Subscriber> findAll() {
            return new ArrayList<>(allSubscribers);
        }

        public Optional<Subscriber> findById(int id) {
            return allSubscribers.stream()
                    .filter(subscriber -> subscriber.getId() == id)
                    .findFirst();
        }
}