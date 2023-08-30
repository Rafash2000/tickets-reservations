package com.tickets.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tickets.model.enums.WagonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Represents a wagon in a train.
 */
@Getter
@Setter
@Entity(name = "wagons")
public class Wagon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int wagonNumber;

    /**
     * The type of the wagon.
     */
    @Enumerated(EnumType.STRING)
    private WagonType wagonType;

    @OneToMany(mappedBy = "wagon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Seat> seats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    @JsonIgnore
    private Train train;

}
