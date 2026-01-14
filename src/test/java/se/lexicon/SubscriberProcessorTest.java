package se.lexicon;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SubscriberProcessor - Business Rules Tests")
class SubscriberProcessorTest {

    private SubscriberProcessor processor;
    private List<Subscriber> testSubscribers;

    @BeforeEach
    void setUp() {
        SubscriberDAO dao = new SubscriberDAO();
        processor = new SubscriberProcessor();
        dao.randomSeed(50);
        testSubscribers = dao.findAll();
    }

    // Rule 1: Active Subscriber
    @Test
    @DisplayName("Should find all active subscribers")
    void testActiveSubscriber() {
        List<Subscriber> active = processor.findSubscribers(testSubscribers, Rules.activeSubscriber);

        assertFalse(active.isEmpty(), "Should find at least one active subscriber");
        assertTrue(active.stream().allMatch(Subscriber::isActive), "All found subscribers should be active");
    }

    @Test
    @DisplayName("Active subscriber filter should exclude inactive subscribers")
    void testActiveSubscriberExcludesInactive() {
        List<Subscriber> active = processor.findSubscribers(testSubscribers, Rules.activeSubscriber);
        
        assertFalse(active.stream().anyMatch(s -> !s.isActive()), "No inactive subscribers should be present");
    }

    // Rule 2: Expiring Subscription
    @Test
    @DisplayName("Should find subscribers with 0 or 1 month remaining")
    void testExpiringSubscription() {
        List<Subscriber> expiring = processor.findSubscribers(testSubscribers, Rules.expiringSubscription);
        
        assertTrue(expiring.stream().allMatch(s -> s.getMonthsRemaining() <= 1), 
                "All found subscribers should have 0 or 1 month remaining");
    }

    @Test
    @DisplayName("Expiring subscription filter should exclude subscribers with 2+ months")
    void testExpiringSubscriptionExcludes() {
        List<Subscriber> expiring = processor.findSubscribers(testSubscribers, Rules.expiringSubscription);
        
        assertFalse(expiring.stream().anyMatch(s -> s.getMonthsRemaining() > 1), 
                "No subscriber with more than 1 month should be present");
    }

    // Rule 3: Active and Expiring Subscriber
    @Test
    @DisplayName("Should find subscribers who are active and expiring")
    void testActiveAndExpiringSubscriber() {
        List<Subscriber> activeExpiring = processor.findSubscribers(testSubscribers, Rules.activeAndExpiringSubscriber);
        
        assertTrue(activeExpiring.stream().allMatch(s -> s.isActive() && s.getMonthsRemaining() <= 1),
                "All found subscribers should be active AND expiring");
    }

    @Test
    @DisplayName("Active and expiring filter should exclude inactive or non-expiring subscribers")
    void testActiveAndExpiringExcludes() {
        List<Subscriber> activeExpiring = processor.findSubscribers(testSubscribers, Rules.activeAndExpiringSubscriber);
        
        assertFalse(activeExpiring.stream().anyMatch(s -> !s.isActive()), "No inactive subscribers");
        assertFalse(activeExpiring.stream().anyMatch(s -> s.getMonthsRemaining() > 1), "No non-expiring subscribers");
    }

    // Rule 4: Subscriber by Plan
    @Test
    @DisplayName("Should find all FREE plan subscribers")
    void testSubscriberByPlanFree() {
        List<Subscriber> free = processor.findSubscribers(testSubscribers, Rules.subscriberByPlan(Plan.FREE));
        
        assertTrue(free.stream().allMatch(s -> s.getPlan() == Plan.FREE), 
                "All found subscribers should be on FREE plan");
    }

    @Test
    @DisplayName("Should find all BASIC plan subscribers")
    void testSubscriberByPlanBasic() {
        List<Subscriber> basic = processor.findSubscribers(testSubscribers, Rules.subscriberByPlan(Plan.BASIC));
        
        assertTrue(basic.stream().allMatch(s -> s.getPlan() == Plan.BASIC), 
                "All found subscribers should be on BASIC plan");
    }

    @Test
    @DisplayName("Should find all PRO plan subscribers")
    void testSubscriberByPlanPro() {
        List<Subscriber> pro = processor.findSubscribers(testSubscribers, Rules.subscriberByPlan(Plan.PRO));
        
        assertTrue(pro.stream().allMatch(s -> s.getPlan() == Plan.PRO), 
                "All found subscribers should be on PRO plan");
    }

    @Test
    @DisplayName("Plan filters should be mutually exclusive")
    void testSubscriberByPlanExclusive() {
        List<Subscriber> free = processor.findSubscribers(testSubscribers, Rules.subscriberByPlan(Plan.FREE));
        List<Subscriber> basic = processor.findSubscribers(testSubscribers, Rules.subscriberByPlan(Plan.BASIC));
        List<Subscriber> pro = processor.findSubscribers(testSubscribers, Rules.subscriberByPlan(Plan.PRO));

        assertTrue(free.stream().noneMatch(basic::contains), "FREE and BASIC should be exclusive");
        assertTrue(basic.stream().noneMatch(pro::contains), "BASIC and PRO should be exclusive");
    }

    // Rule 5: Paying Subscriber
    @Test
    @DisplayName("Should find all paying subscribers (BASIC or PRO)")
    void testPayingSubscriber() {
        List<Subscriber> paying = processor.findSubscribers(testSubscribers, Rules.payingSubscriber);
        
        assertTrue(paying.stream().allMatch(s -> s.getPlan() == Plan.BASIC || s.getPlan() == Plan.PRO),
                "All found subscribers should be BASIC or PRO");
    }

    @Test
    @DisplayName("Paying subscriber filter should exclude FREE plan")
    void testPayingSubscriberExcludesFree() {
        List<Subscriber> paying = processor.findSubscribers(testSubscribers, Rules.payingSubscriber);
        
        assertFalse(paying.stream().anyMatch(s -> s.getPlan() == Plan.FREE), 
                "No FREE plan subscribers should be present");
    }

    // Rule 6: Extend Subscription
    @Test
    @DisplayName("Should extend subscription months for matching subscribers")
    void testExtendSubscription() {
        Subscriber testSub = new Subscriber(1, "test@example.com", Plan.PRO, true, 5);
        List<Subscriber> list = List.of(testSub);
        
        processor.applyToMatching(list, Rules.activeSubscriber, Rules.extendSubscription(6));
        
        assertEquals(11, testSub.getMonthsRemaining(), "Months should increase by 6");
    }

    @Test
    @DisplayName("Should extend only matching subscribers")
    void testExtendSubscriptionOnlyMatching() {
        Subscriber active = new Subscriber(1, "active@example.com", Plan.BASIC, true, 3);
        Subscriber inactive = new Subscriber(2, "inactive@example.com", Plan.PRO, false, 3);
        List<Subscriber> list = List.of(active, inactive);
        
        processor.applyToMatching(list, Rules.activeSubscriber, Rules.extendSubscription(5));
        
        assertEquals(8, active.getMonthsRemaining(), "Active subscriber should be extended");
        assertEquals(3, inactive.getMonthsRemaining(), "Inactive subscriber should NOT be extended");
    }

    @Test
    @DisplayName("Should extend to zero months if already negative")
    void testExtendSubscriptionNegative() {
        Subscriber testSub = new Subscriber(1, "test@example.com", Plan.FREE, true, -2);
        List<Subscriber> list = List.of(testSub);
        
        processor.applyToMatching(list, Rules.activeSubscriber, Rules.extendSubscription(3));
        
        assertEquals(1, testSub.getMonthsRemaining(), "Should handle negative months correctly");
    }

    // Rule 7: Deactivate Subscriber
    @Test
    @DisplayName("Should deactivate matching subscribers")
    void testDeactivateSubscriber() {
        Subscriber testSub = new Subscriber(1, "test@example.com", Plan.BASIC, true, 5);
        List<Subscriber> list = List.of(testSub);
        
        processor.applyToMatching(list, Rules.activeSubscriber, Rules.deactivateSubscriber);
        
        assertFalse(testSub.isActive(), "Subscriber should be deactivated");
    }

    @Test
    @DisplayName("Should deactivate only matching subscribers")
    void testDeactivateOnlyMatching() {
        Subscriber active = new Subscriber(1, "active@example.com", Plan.FREE, true, 0);
        Subscriber inactive = new Subscriber(2, "inactive@example.com", Plan.PRO, false, 5);
        List<Subscriber> list = List.of(active, inactive);
        
        processor.applyToMatching(list, Rules.activeSubscriber, Rules.deactivateSubscriber);
        
        assertFalse(active.isActive(), "Active subscriber should be deactivated");
        assertFalse(inactive.isActive(), "Already inactive subscriber stays inactive");
    }

    // Additional Tests
    @Test
    @DisplayName("Should find 0 subscribers with empty filter result")
    void testEmptyFilterResult() {
        SubscriberFilter noneMatch = subscriber -> false;
        List<Subscriber> result = processor.findSubscribers(testSubscribers, noneMatch);
        
        assertEquals(0, result.size(), "Should return empty list when no matches");
    }

    @Test
    @DisplayName("Should find all subscribers with universal filter")
    void testUniversalFilter() {
        SubscriberFilter allMatch = subscriber -> true;
        List<Subscriber> result = processor.findSubscribers(testSubscribers, allMatch);
        
        assertEquals(testSubscribers.size(), result.size(), "Should return all subscribers");
    }

    @Test
    @DisplayName("applyToMatching should return the same list")
    void testApplyToMatchingReturnsInput() {
        List<Subscriber> result = processor.applyToMatching(testSubscribers, Rules.activeSubscriber, Rules.deactivateSubscriber);
        
        assertEquals(testSubscribers, result, "Should return the same list instance");
    }
}