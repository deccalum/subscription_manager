package se.lexicon;

public class App 
{
    public static void main( String[] args ){

       SubscriberDAO dao = new SubscriberDAO();
       dao.randomSeed(100);
       Rules.executeAll(dao.findAll());
    }
}
