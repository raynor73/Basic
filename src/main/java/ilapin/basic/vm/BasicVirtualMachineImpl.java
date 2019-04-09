package ilapin.basic.vm;

import ilapin.basic.vm.operations.EndOperation;
import ilapin.basic.vm.operations.PrintStringOperation;
import ilapin.basic.vm.operations.PrintStringVariableOperation;

import java.util.*;

public class BasicVirtualMachineImpl implements BasicVirtualMachine {

	private final StringPrinter stringPrinter;

	private final Map<String, Integer[]> numberVariables = new HashMap<>();
	private final Map<String, BasicString> stringVariables = new HashMap<>();

	private int accumulator;

	private final LinkedList<Integer> subroutineReturnStack = new LinkedList<>();

	private final List<Operation> operations = new ArrayList<>();
	private final Map<Integer, Integer> lineToIndexMap = new HashMap<>();

	private int currentOperationIndex;
	private BasicVirtualMachine.State state = State.IDLE;
	private BasicError lastError;

	public BasicVirtualMachineImpl(final StringPrinter stringPrinter) {
		this.stringPrinter = stringPrinter;
	}

	public int getAccumulator() {
		return accumulator;
	}

	public void setAccumulator(final int accumulator) {
		this.accumulator = accumulator;
	}

	public void incrementOperationIndex() {
		currentOperationIndex++;
	}

	public void setCurrentOperationIndex(final int currentOperationIndex) {
		this.currentOperationIndex = currentOperationIndex;
	}

	public int getCurrentOperationIndex() {
		return currentOperationIndex;
	}

	public StringPrinter getStringPrinter() {
		return stringPrinter;
	}

	public Map<String, Integer[]> getNumberVariables() {
		return numberVariables;
	}

	public Map<String, BasicString> getStringVariables() {
		return stringVariables;
	}

	public LinkedList<Integer> getSubroutineReturnStack() {
		return subroutineReturnStack;
	}

	public Map<Integer, Integer> getLineToIndexMap() {
		return lineToIndexMap;
	}

	@Override
	public void step() throws BasicError {
		switch (state) {
			case IDLE:
				state = BasicVirtualMachine.State.RUNNING;
			case RUNNING:
				if (currentOperationIndex >= operations.size()) {
					emitError(new EndError());
				}
				operations.get(currentOperationIndex).execute();
				break;
		}
	}

	@Override
	public void reset() {
		state = BasicVirtualMachine.State.IDLE;
		lastError = null;
		currentOperationIndex = 0;
		accumulator = 0;
		numberVariables.clear();
		stringVariables.clear();
		subroutineReturnStack.clear();
	}

	@Override
	public void clear() {
		reset();
		operations.clear();
	}

	@Override
	public BasicVirtualMachine.State getState() {
		return state;
	}

	public void setState(final State state) {
		this.state = state;
	}

	@Override
	public BasicError getLastError() {
		return lastError;
	}

	@Override
	public void addPrintStringOperation(final BasicString string, final int lineNumber) {
		addOperation(new PrintStringOperation(this, string), lineNumber);
	}

	@Override
	public void addPrintStringVariableOperation(
			final String variableName,
			final int start,
			final int end,
			final int lineNumber)
	{
		addOperation(new PrintStringVariableOperation(this,  variableName, start, end), lineNumber);
	}

	@Override
	public void addEndOperation(final int lineNumber) {
		addOperation(new EndOperation(this), lineNumber);
	}

	public void addOperation(final Operation operation, final int lineNumber) {
		if (state != BasicVirtualMachine.State.IDLE) {
			return;
		}

		operations.add(operation);
		if (!lineToIndexMap.containsKey(lineNumber)) {
			lineToIndexMap.put(lineNumber, operations.size() - 1);
		}
	}

	public void emitError(final BasicError error) throws BasicError {
		state = BasicVirtualMachine.State.ERROR_DETECTED;
		lastError = error;
		throw error;
	}

	public void checkNotZero(final int value) throws BasicError {
		if (value == 0) {
			emitError(new IntegerOverflowError());
		}
	}

	public void checkIntegerRange(final int value) throws BasicError {
		if (value < -32767 || value > 32767) {
			emitError(new IntegerOverflowError());
		}
	}
}
