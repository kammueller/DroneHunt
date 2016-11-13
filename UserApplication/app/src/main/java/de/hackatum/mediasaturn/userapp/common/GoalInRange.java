package de.hackatum.mediasaturn.userapp.common;

/**
 * is the user in goal range?
 *
 * @author Matthias Kammueller
 */
public class GoalInRange {
    private boolean inGoalRange;

    public GoalInRange(boolean inGoalRange) {
        this.inGoalRange = inGoalRange;
    }

    public boolean isInGoalRange() {
        return inGoalRange;
    }
}
