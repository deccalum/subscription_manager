package se.lexicon;

import java.util.List;

import static se.lexicon.SubscriberActions.deactivate;
import static se.lexicon.SubscriberActions.extendBy;

public class SubscriberReporter {

    public void printStats() {
        SubscriberProcessor processor = new SubscriberProcessor();
        SubscriberDAO dao = new SubscriberDAO();
        dao.randomSeed(100);
        List<Subscriber> subscribers = dao.findAll();


        // --- COLLECT STATS  ---

        List<Subscriber> activeResult = processor.findSubscribers(subscribers, s -> s.isActive());
        List<Subscriber> expiringResult = processor.findSubscribers(subscribers, s -> s.getMonthsRemaining() <= 1);
        
        List<Subscriber> activeAndExpiring = processor.findSubscribers(subscribers, 
            s -> s.isActive() && s.getMonthsRemaining() <= 1);
            
        List<Subscriber> freeResult = processor.findSubscribers(subscribers, s -> s.getPlan() == Plan.FREE);
        List<Subscriber> basicResult = processor.findSubscribers(subscribers, s -> s.getPlan() == Plan.BASIC);
        List<Subscriber> proResult = processor.findSubscribers(subscribers, s -> s.getPlan() == Plan.PRO);
        List<Subscriber> payingResult = processor.findSubscribers(subscribers, s -> s.getPlan() != Plan.FREE);

        // --- ID PICKER FOR ID RELATED ACTIONS ---

        Subscriber subToExtend = subscribers.get(
            java.util.concurrent.ThreadLocalRandom.current().nextInt(subscribers.size()));
        
        Subscriber subToDeactivate = activeResult.get(
            java.util.concurrent.ThreadLocalRandom.current().nextInt(activeResult.size()));

       
       // --- PERFORM ACTIONS ---

       // Extend the randomly selected subscriber
       int extendId = subToExtend.getId();
       int monthsBefore = subToExtend.getMonthsRemaining();
       processor.applyToMatching(subscribers, 
           s -> s.getId() == extendId, 
           extendBy(3)::accept);

       // Deactivate the randomly selected active subscriber
       int deactivateId = subToDeactivate.getId();
       boolean activeBefore = subToDeactivate.isActive();
       processor.applyToMatching(subscribers, 
           s -> s.getId() == deactivateId, 
           deactivate()::accept);

        processor.applyToMatching(subscribers, s -> s.isActive(), s -> s.setMonthsRemaining(1));


        // --- PRINT DATA ---

        // Print IDs of all active subscribers
        System.out.print("Active IDs: ");
        processor.applyToMatching(activeResult, 
            s -> true, 
            s -> System.out.print(s.getId() + " "));
        System.out.println();

        // Print IDs of expiring subscribers
        System.out.print("Expiring IDs: ");
        processor.applyToMatching(expiringResult, 
            s -> true, 
            s -> System.out.print(s.getId() + " "));
        System.out.println();

        // Print Statistics
        System.out.println("Active: " + activeResult.size());
        System.out.println("Expiring: " + expiringResult.size());
        System.out.println("Active and Expiring: " + activeAndExpiring.size());
        System.out.println("Free Plans: " + freeResult.size());
        System.out.println("Basic Plans: " + basicResult.size());
        System.out.println("Pro Plans: " + proResult.size());
        System.out.println("Paying subscribers: " + payingResult.size());

        String actionInfo = String.format("\nACTIONS PERFORMED: \n- Extended ID %d: %d -> %d months \n- Deactivated ID %d: %b -> %b", 
            extendId, monthsBefore, subToExtend.getMonthsRemaining(),
            deactivateId, activeBefore, subToDeactivate.isActive());
        
        System.out.println(actionInfo);
    }
} 
