package com.tickets.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tickets.model.enums.SeatType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a seat in a wagon.
 */

@Getter
@Setter
@Entity(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long seatNumber;

    /**
     * The type of the seat.
     */
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wagon_id")
    @JsonIgnore
    private Wagon wagon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    @JsonIgnore
    private Reservation reservation;

}