package ilapin.basic;

import java.util.*;

public class BasicVirtualMachine {

	private final StringPrinter stringPrinter;

	private final Map<String, Integer[]> numberVariables = new HashMap<>();
	private final Map<String, BasicString> stringVariables = new HashMap<>();

	private int accumulator;

	private final LinkedList<Integer> subroutineReturnStack = new LinkedList<>();

	private final List<Operation> operations = new ArrayList<>();
	private final Map<Integer, Integer> lineToIndexMap = new HashMap<>();

	private int currentOperationIndex;
	private State state;
	private BasicError lastError;

	public BasicVirtualMachine(final StringPrinter stringPrinter) {
		this.stringPrinter = stringPrinter;
	}

	public void step() throws BasicError {
		switch (state) {
			case IDLE:
				state = State.RUNNING;
			case RUNNING:
				if (currentOperationIndex >= operations.size()) {
					emitError(new EndError());
				}
				operations.get(currentOperationIndex).execute();
				break;
		}
	}

	public void reset() {
		state = State.IDLE;
		lastError = null;
		currentOperationIndex = 0;
		accumulator = 0;
		numberVariables.clear();
		stringVariables.clear();
		subroutineReturnStack.clear();
	}

	public void clear() {
		reset();
		operations.clear();
	}

	public State getState() {
		return state;
	}

	public BasicError getLastError() {
		return lastError;
	}

	public void addOperation(final Operation operation, final int lineNumber) {
		if (state != State.IDLE) {
			return;
		}

		operations.add(operation);
		if (!lineToIndexMap.containsKey(lineNumber)) {
			lineToIndexMap.put(lineNumber, operations.size() - 1);
		}
	}

	public PrintStringOperation createPrintStringOperation(final BasicString string) {
		return new PrintStringOperation(string);
	}

	public PrintStringVariableOperation createPrintStringVariableOperation(
			final String variableName,
			final int start,
			final int end
	) {
		return new PrintStringVariableOperation(variableName, start, end);
	}

	private void emitError(final BasicError error) throws BasicError {
		state = State.ERROR_DETECTED;
		lastError = error;
		throw error;
	}

	private void checkNotZero(final int value) throws BasicError {
		if (value == 0) {
			emitError(new IntegerOverflowError());
		}
	}

	private void checkIntegerRange(final int value) throws BasicError {
		if (value < -32767 || value > 32767) {
			emitError(new IntegerOverflowError());
		}
	}

	private class PrintStringOperation implements Operation {

		private final BasicString string;

		private PrintStringOperation(final BasicString string) {
			this.string = string;
		}

		@Override
		public void execute() {
			stringPrinter.print(string.getCharacters());
			currentOperationIndex++;
		}
	}

	private class PrintStringVariableOperation implements Operation {

		private final String variableName;
		private final int start;
		private final int end;

		private PrintStringVariableOperation(final String variableName, final int start, final int end) {
			this.variableName = variableName;
			this.start = start;
			this.end = end;
		}

		@Override
		public void execute() {
			final BasicString string = stringVariables.get(variableName);
			if (string != null) {
				stringPrinter.print(string.getCharacters(), start, end);
			} else {
				stringPrinter.print(new Character[]{});
			}
			currentOperationIndex++;
		}
	}

	private class AddImmediateOperation implements Operation {

		private final int value;

		private AddImmediateOperation(final int value) {
			this.value = value;
		}

		@Override
		public void execute() throws BasicError {
			accumulator += value;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class AddVariableOperation implements Operation {

		private final String variableName;
		private final int index;

		private AddVariableOperation(final String variableName) {
			this(variableName, 0);
		}

		private AddVariableOperation(final String variableName, final int index) {
			this.variableName = variableName;
			this.index = index;
		}

		@Override
		public void execute() throws BasicError {
			final Integer value = numberVariables.get(variableName)[index];
			accumulator += value != null ? value : 0;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class SubImmediateOperation implements Operation {

		private final int value;

		private SubImmediateOperation(final int value) {
			this.value = value;
		}

		@Override
		public void execute() throws BasicError {
			accumulator -= value;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class SubVariableOperation implements Operation {

		private final String variableName;
		private final int index;

		public SubVariableOperation(final String variableName) {
			this.variableName = variableName;
			this.index = 0;
		}

		private SubVariableOperation(final String variableName, final int index) {
			this.variableName = variableName;
			this.index = index;
		}

		@Override
		public void execute() throws BasicError {
			final Integer value = numberVariables.get(variableName)[index];
			accumulator -= value != null ? value : 0;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class MulImmediateOperation implements Operation {

		private final int value;

		private MulImmediateOperation(final int value) {
			this.value = value;
		}

		@Override
		public void execute() throws BasicError {
			accumulator *= value;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class MulVariableOperation implements Operation {

		private final String variableName;

		private MulVariableOperation(final String variableName) {
			this.variableName = variableName;
		}

		@Override
		public void execute() throws BasicError {
			final Integer value = numberVariables.get(variableName);
			accumulator *= value != null ? value : 0;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class DivImmediateOperation implements Operation {

		private final int value;

		private DivImmediateOperation(final int value) {
			this.value = value;
		}

		@Override
		public void execute() throws BasicError {
			checkNotZero(accumulator);
			accumulator /= value;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class DivVariableOperation implements Operation {

		private final String variableName;

		private DivVariableOperation(final String variableName) {
			this.variableName = variableName;
		}

		@Override
		public void execute() throws BasicError {
			final Integer value = numberVariables.get(variableName);
			checkNotZero(value);
			accumulator /= value;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class ModImmediateOperation implements Operation {

		private final int value;

		private ModImmediateOperation(final int value) {
			this.value = value;
		}

		@Override
		public void execute() throws BasicError {
			accumulator %= value;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class ModVariableOperation implements Operation {

		private final String variableName;

		private ModVariableOperation(final String variableName) {
			this.variableName = variableName;
		}

		@Override
		public void execute() throws BasicError {
			final Integer value = numberVariables.get(variableName);
			accumulator %= value != null ? value : 0;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class LoadAccumulatorImmediateOperation implements Operation {

		private final int value;

		private LoadAccumulatorImmediateOperation(final int value) {
			this.value = value;
		}

		@Override
		public void execute() throws BasicError {
			accumulator = value;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class LoadAccumulatorVariableOperation implements Operation {

		private final String variableName;

		private LoadAccumulatorVariableOperation(final String variableName) {
			this.variableName = variableName;
		}

		@Override
		public void execute() throws BasicError {
			final Integer value = numberVariables.get(variableName);
			accumulator = value != null ? value : 0;
			checkIntegerRange(accumulator);
			currentOperationIndex++;
		}
	}

	private class LoadStringToStringOperation implements Operation {

		private final String destinationVariableName;
		private final int destinationStart;
		private final String sourceVariableName;
		private final int sourceStart;
		private final int sourceEnd;

		private LoadStringToStringOperation(
				final String destinationVariableName,
				final int destinationStart,
				final String sourceVariableName,
				final int sourceStart,
				final int sourceEnd
		) {
			this.destinationVariableName = destinationVariableName;
			this.destinationStart = destinationStart;
			this.sourceVariableName = sourceVariableName;
			this.sourceStart = sourceStart;
			this.sourceEnd = sourceEnd;
		}

		@Override
		public void execute() throws BasicError {
			final BasicString sourceString = stringVariables.get(sourceVariableName);
			final BasicString destinationString = stringVariables.get(destinationVariableName);
			if (sourceString != null && sourceString.getCurrentLength() > 0) {
				if (destinationString != null) {
					destinationString.setCharacters(sourceString, destinationStart, sourceStart, sourceEnd);
				} else {
					final BasicString newDestinationString = new BasicString(1);
					stringVariables.put(destinationVariableName, newDestinationString);
					newDestinationString.setCharacters(sourceString, destinationStart, sourceStart, sourceEnd);
				}
			} else {
				if (destinationString != null) {
					stringVariables.put(destinationVariableName, new BasicString(destinationString.getMaxLength()));
				} else {
					stringVariables.put(destinationVariableName, new BasicString(0));
				}
			}
		}
	}

	private class LoadStringImmediate implements Operation {

		private final String string;
		final String variableName;

		private LoadStringImmediate(final String variableName, final String string) {
			this.variableName = variableName;
			this.string = string;
		}

		@Override
		public void execute() throws BasicError {
			final BasicString destination = stringVariables.get(variableName);
			if (string.length() > 0) {
				final Character[] characters = new Character[string.length()];
				for (int i = 0; i < characters.length; i++) {
					characters[i] = string.charAt(i);
				}
				final BasicString sourceBasicString = new BasicString(characters);
				if (destination != null) {
					destination.setCharacters(sourceBasicString, 0, 0, characters.length - 1);
				} else {
					final BasicString newDestination = new BasicString(1);
					stringVariables.put(variableName, newDestination);
					newDestination.setCharacters(sourceBasicString, 0, 0, characters.length - 1);
				}
			} else {
				if (destination != null) {
					stringVariables.put(variableName, new BasicString(destination.getMaxLength()));
				} else {
					stringVariables.put(variableName, new BasicString(1));
				}
			}
		}
	}

	private class GoToImmediateOperation implements Operation {

		private final int lineNumber;

		private GoToImmediateOperation(final int lineNumber) {
			this.lineNumber = lineNumber;
		}

		@Override
		public void execute() throws BadBranchError {
			final Integer goToOperationIndex = lineToIndexMap.get(lineNumber);
			if (goToOperationIndex != null) {
				currentOperationIndex = goToOperationIndex;
			} else {
				throw new BadBranchError();
			}
		}
	}

	private class GoToAccumulatorOperation implements Operation {

		@Override
		public void execute() throws BadBranchError {
			final Integer goToOperationIndex = lineToIndexMap.get(accumulator);
			if (goToOperationIndex != null) {
				currentOperationIndex = goToOperationIndex;
			} else {
				throw new BadBranchError();
			}
		}
	}

	private class GoSubImmediateOperation implements Operation {

		private final int lineNumber;

		private GoSubImmediateOperation(final int lineNumber) {
			this.lineNumber = lineNumber;
		}

		@Override
		public void execute() throws BasicError {
			final Integer goToOperationIndex = lineToIndexMap.get(lineNumber);
			if (goToOperationIndex != null) {
				subroutineReturnStack.add(currentOperationIndex);
				currentOperationIndex = goToOperationIndex;
			} else {
				throw new BadBranchError();
			}
		}
	}

	private class GoSubAccumulatorOperation implements Operation {

		@Override
		public void execute() throws BasicError {
			final Integer goToOperationIndex = lineToIndexMap.get(accumulator);
			if (goToOperationIndex != null) {
				subroutineReturnStack.add(currentOperationIndex);
				currentOperationIndex = goToOperationIndex;
			} else {
				throw new BadBranchError();
			}
		}
	}

	private class ReturnOperation implements Operation {

		@Override
		public void execute() throws BasicError {
			if (subroutineReturnStack.size() > 0) {
				currentOperationIndex = subroutineReturnStack.pollLast();
			} else {
				emitError(new BadReturnError());
			}
		}
	}

	private class EndOperation implements Operation {

		@Override
		public void execute() throws BasicError {
			state = State.FINISHED;
		}
	}

	public enum State {
		IDLE, RUNNING, FINISHED, ERROR_DETECTED
	}
}
