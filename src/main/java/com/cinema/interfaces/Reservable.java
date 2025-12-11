package com.cinema.interfaces;

import com.cinema.enums.SeatType;

public interface Reservable {
    boolean reserve(int[] seat, SeatType type);
    boolean cancel(int[] seat, SeatType type);
}