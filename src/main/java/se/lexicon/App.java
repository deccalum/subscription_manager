package se.lexicon;

public class App 
{
    public static void main( String[] args )
    {

        /*

        You will create core components such as subscribers, subscription plans, and a data access class to store and retrieve subscribers.  
        You will then work with lists of subscribers and apply different rules, such as identifying active users or updating subscriptions.

        */

        /*
                Core Rules to Implement

        Define and apply the following business rules using functional interfaces (SubscriberFilter and SubscriberAction).

        Active Subscriber
        Matches subscribers whose account is active.

        Expiring Subscription
        Matches subscribers with 0 or 1 month remaining.

        Active and Expiring Subscriber
        Matches subscribers who are active and whose subscription is about to expire.

        Subscriber by Plan
        Matches subscribers based on their subscription plan (FREE, BASIC, or PRO).

        Paying Subscriber
        Matches subscribers with a paid plan (BASIC or PRO).

        Extend Subscription
        Increases the remaining subscription period for a subscriber.

        Deactivate Subscriber
        Marks a subscriber as inactive.
        */

        /*
                Part 2 — (Optional / Challenge)

        Write JUnit tests for the core business logic.

        Scenario 1: Show Active Subscribers
        Display all subscribers that are active.

        Scenario 2: Show Expiring Subscriptions
        Display subscribers with 0 or 1 month remaining.

        Scenario 3: Show Active and Expiring Subscribers
        Display subscribers who are active and whose subscriptions are about to expire.

        Scenario 4: Extend Subscriptions for Paying Subscribers
        Extend subscriptions for active, paying subscribers with expiring subscriptions.

        Scenario 5: Deactivate Expired Free Subscribers
        Deactivate free subscribers whose subscriptions have expired.

        Scenario 6: Filter Subscribers by Plan
        Display subscribers based on their subscription plan (FREE, BASIC, or PRO).

        Add more scenarios as you see fit!
        */


        /*
        ADDITIONAL COMMANDS
        tick "time" by x months
         */
    }
}
