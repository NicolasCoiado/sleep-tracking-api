package br.com.nvnk.SleepTracking.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDateTime;

@Document(collection = "sleep_attempts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SleepAttempt {
    @Id
    private String id;

    @Indexed
    private String userId;
    private boolean success;
    private LocalDateTime bedTime;
    private LocalDateTime wakeTime;
    private LocalDateTime sleepStartTime;
    private LocalDateTime sleepEndTime;

    private Duration totalTimeInBed;
    private Duration totalSleepTime;
}
