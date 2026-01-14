package se.lexicon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class SubscriberDAO {

    private final List<Subscriber> allSubscribers = new ArrayList<>();
    private final AtomicInteger idSeq = new AtomicInteger(1);

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

    public void seed(int count, IntFunction<Subscriber> generator) {
        IntStream.range(0, count).forEach(i -> save(generator.apply(idSeq.getAndIncrement())));
    }

    public void randomSeed(int count) {
        seed(count, id -> {
            Plan plan = Plan.values()[ThreadLocalRandom.current().nextInt(Plan.values().length)];
            boolean active = ThreadLocalRandom.current().nextBoolean();
            int months = ThreadLocalRandom.current().nextInt(0, 13);
            String email = "user" + id + "@example.com";
            return new Subscriber(id, email, plan, active, months);
        });
    }
}