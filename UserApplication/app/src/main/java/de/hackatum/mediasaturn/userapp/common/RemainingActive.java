package de.hackatum.mediasaturn.userapp.common;

/**
 * represents the remaining time in seconds
 *
 * @author Matthias Kammueller
 */
public class RemainingActive {

    private int secondsLeft;

    public RemainingActive(int secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }
}
