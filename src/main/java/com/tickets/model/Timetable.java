package com.tickets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tickets.model.enums.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a timetable for train departures and arrivals.
 */
@Getter
@Setter
@Entity(name = "timetables")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The station where the journey starts.
     */
    @Enumerated(EnumType.STRING)
    private Station startStation;

    /**
     * The station where the journey ends.
     */
    @Enumerated(EnumType.STRING)
    private Station endStation;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToOne(mappedBy = "timetable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Train train;

}
