package stat;

import clock.SettableClock;
import java.time.Instant;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EventsStatisticTest {
    public static final int HOUR_IN_MINUTES = 60;
    public static final int HOUR_IN_SECONDS = 3600;

    private SettableClock settableClock;
    private EventsStatistic eventsStatistic;

    @BeforeMethod
    public void setUp() {
        settableClock = new SettableClock(Instant.now());
        eventsStatistic = new RpmStatistic(settableClock);
    }

    @Test
    public void testStatByNameEmpty() {
        settableClock.setNow(Instant.now());
        String name = "event1";
        double result = eventsStatistic.getEventStatisticByName(name);
        Assertions.assertThat(result).isEqualTo(0);
    }

    @Test
    public void testAllStatEmpty() {
        settableClock.setNow(Instant.now());
        Assertions.assertThat(eventsStatistic.getAllEventStatistic()).hasSize(0);
    }

    @Test
    public void testByName() {
        settableClock.setNow(Instant.now());
        String name = "event1";
        eventsStatistic.incEvent(name);
        double result = eventsStatistic.getEventStatisticByName(name);
        Assertions.assertThat(result).isEqualTo(1 / (double) HOUR_IN_MINUTES);
    }

    @Test
    public void testByNameInc() {
        settableClock.setNow(Instant.now());
        String name = "event1";
        eventsStatistic.incEvent(name);
        eventsStatistic.incEvent(name);
        eventsStatistic.incEvent(name);
        double result = eventsStatistic.getEventStatisticByName(name);
        Assertions.assertThat(result).isEqualTo(3 / (double) HOUR_IN_MINUTES);
    }

    @Test
    public void testMultipleTimes() {
        settableClock.setNow(Instant.now());
        SoftAssertions softAssertions = new SoftAssertions();

        String name1 = "event1";
        String name2 = "event2";
        String name3 = "event3";

        //Step1
        eventsStatistic.incEvent(name1);
        eventsStatistic.incEvent(name2);
        eventsStatistic.incEvent(name3);

        Map<String, Double> stat = eventsStatistic.getAllEventStatistic();
        softAssertions.assertThat(stat)
                      .containsEntry(name1, (double) 1 / HOUR_IN_MINUTES)
                      .containsEntry(name2, (double) 1 / HOUR_IN_MINUTES)
                      .containsEntry(name3, (double) 1 / HOUR_IN_MINUTES);

        //Step2
        settableClock.setNow(settableClock.now().plusSeconds(HOUR_IN_SECONDS / 2));
        eventsStatistic.incEvent(name1);
        eventsStatistic.incEvent(name2);

        stat = eventsStatistic.getAllEventStatistic();
        softAssertions.assertThat(stat)
                      .containsEntry(name1, (double) 2 / HOUR_IN_MINUTES)
                      .containsEntry(name2, (double) 2 / HOUR_IN_MINUTES)
                      .containsEntry(name3, (double) 1 / HOUR_IN_MINUTES);

        //Step3
        settableClock.setNow(settableClock.now().plusSeconds(HOUR_IN_SECONDS / 2));
        eventsStatistic.incEvent(name1);
        eventsStatistic.incEvent(name2);

        stat = eventsStatistic.getAllEventStatistic();
        softAssertions.assertThat(stat)
                      .containsEntry(name1, (double) 2 / HOUR_IN_MINUTES)
                      .containsEntry(name2, (double) 2 / HOUR_IN_MINUTES)
                      .doesNotContainKey(name3);

        softAssertions.assertAll();
    }
}
