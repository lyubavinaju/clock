package clock;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
public class SettableClock implements Clock {
    @Setter
    private Instant now;

    @Override
    public Instant now() {
        return now;
    }
}
