package stat;

import clock.Clock;
import event.Event;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RpmStatistic implements EventsStatistic {
    public static final int HOUR_IN_SECONDS = 3600;
    public static final int HOUR_IN_MINUTES = 60;
    private final Map<String, List<Event>> eventsHistory = new HashMap<>();

    private final Clock clock;

    public RpmStatistic(Clock clock) {
        this.clock = clock;
    }

    private void clean(Instant now) {
        Instant hourAgo = now.minusSeconds(HOUR_IN_SECONDS).plusSeconds(1);
        eventsHistory.forEach((name, events) -> events.removeIf(event -> event.getInstant().isBefore(hourAgo)));
        eventsHistory.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    @Override
    public void incEvent(String name) {
        Instant now = clock.now();
        clean(now);
        eventsHistory.putIfAbsent(name, new ArrayList<>());
        eventsHistory.get(name).add(new Event(now));
    }

    @Override
    public double getEventStatisticByName(String name) {
        Instant now = clock.now();
        clean(now);
        double rpm =
            eventsHistory.getOrDefault(name, List.of()).size() / (double) HOUR_IN_MINUTES;
        return rpm;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        Instant now = clock.now();
        clean(now);
        Map<String, Double> stat = eventsHistory.keySet().stream()
                                                .collect(Collectors
                                                    .toMap(name -> name, this::getEventStatisticByName));
        return stat;
    }

    @Override
    public void printStatistic() {
        Instant now = clock.now();
        clean(now);
        Map<String, Double> allEventStatistic = getAllEventStatistic();
        allEventStatistic.forEach((name, rpm) -> System.out.println("Name: " + name + ", rpm: " + rpm));
    }
}
