package com.cinema.interfaces;

import com.cinema.enums.SeatType;
import com.cinema.models.Seance.SeatReservationException;
import com.cinema.models.Seance.SeatCancellationException;

public interface Reservable {
    /**
     * Réserve un siège pour un type donné.
     *
     * @param seat Coordonnées du siège.
     * @param type Type de siège.
     * @throws SeatReservationException Si la réservation échoue.
     * @throws IllegalArgumentException Si le siège est invalide.
     */
    void reserve(int[] seat, SeatType type) throws SeatReservationException;

    /**
     * Annule la réservation d'un siège.
     *
     * @param seat Coordonnées du siège.
     * @param type Type de siège.
     * @throws SeatCancellationException Si l'annulation échoue.
     * @throws IllegalArgumentException Si le siège est invalide.
     */
    void cancel(int[] seat, SeatType type) throws SeatCancellationException;
}
