package ilapin.basic.vm;

public interface BasicVirtualMachine {

    void step() throws BasicError;

    void reset();

    void clear();

    State getState();

    BasicError getLastError();

    void addPrintStringOperation(final BasicString string, final int lineNumber);

    void addPrintStringVariableOperation(
            final String variableName,
            final int start,
            final int end,
            final int lineNumber
    );

    void addEndOperation(int lineNumber);

    enum State {
        IDLE, RUNNING, FINISHED, ERROR_DETECTED
    }
}