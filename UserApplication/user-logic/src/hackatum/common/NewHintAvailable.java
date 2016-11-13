package hackatum.common;

/**
 * is a new hint available?
 *
 * @author Matthias Kammueller
 */
public class NewHintAvailable {

    private boolean hintAvailable;

    public NewHintAvailable(boolean hintAvailable) {
        this.hintAvailable = hintAvailable;
    }

    public boolean isHintAvailable() {
        return hintAvailable;
    }

}
