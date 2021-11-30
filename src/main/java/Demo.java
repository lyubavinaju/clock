import clock.NormalClock;
import stat.EventsStatistic;
import stat.RpmStatistic;

public class Demo {
    public static void main(String[] args) {
        EventsStatistic eventsStatistic = new RpmStatistic(new NormalClock());
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event2");
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event2");
        eventsStatistic.printStatistic();
    }
}
