package event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Event {
    @Getter
    private final Instant instant;
}
