package ilapin.basic.basicparser;

import ilapin.basic.L;
import ilapin.basic.vm.BasicVirtualMachineImpl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class BasicParser {

	private final BasicVirtualMachineImpl vm;
	private final Reader reader;

	private State state = State.LINE_NUMBER_LOOKUP;

	private final StringBuilder sb = new StringBuilder();
	private int lineNumber;
	private final List<PrintStatementItem> printStatementItems = new ArrayList<>();

	public BasicParser(final BasicVirtualMachineImpl vm, final Reader reader) {
		this.vm = vm;
		this.reader = reader;
	}

	public State getState() {
		return state;
	}

	public void step() throws Throwable {
		if (state == State.PARSING_FINISHED) {
			return;
		}

		final int currentCharacterCode = reader.read();
		if (currentCharacterCode < 0) {
			changeState(State.PARSING_FINISHED);
			return;
		}

		final char currentCharacter = (char) currentCharacterCode;
		switch (state) {
			case LINE_NUMBER_LOOKUP:
				if (Character.isSpaceChar(currentCharacter)) {
					break;
				} else if (Character.isDigit(currentCharacter)) {
					sb.append(currentCharacter);
					changeState(State.LINE_NUMBER_PARSING);
					break;
				} else {
					emitError(new SyntaxError("Expecting begin of line number, but found: " + currentCharacter));
				}

			case LINE_NUMBER_PARSING:
				if (Character.isSpaceChar(currentCharacter)) {
					break;
				} else if (Character.isDigit(currentCharacter)) {
					sb.append(currentCharacter);
					break;
				} else if (Character.isAlphabetic(currentCharacter)) {
					lineNumber = Integer.valueOf(sb.toString());
					changeState(State.STATEMENT_LOOKUP);
					break;
				} else {
					emitError(new SyntaxError("Unexpected character in line number: " + currentCharacter));
				}

			case STATEMENT_LOOKUP:
				if (Character.isSpaceChar(currentCharacter)) {
					break;
				} else if (Character.isDigit(currentCharacter)) {
					emitError(new SyntaxError("Expecting begin of operator, but found: " + currentCharacter));
				} else if (Character.isAlphabetic(currentCharacter)) {
					sb.append(currentCharacter);
					changeState(State.STATEMENT_PARSING);
					break;
				} else {
					emitError(new SyntaxError("Expecting begin of operator, but found: " + currentCharacter));
				}

			case STATEMENT_PARSING:
				if (Character.isSpaceChar(currentCharacter)) {
					break;
				} else if (Character.isDigit(currentCharacter)) {
					emitError(new SyntaxError("Unexpected character in operator name: " + currentCharacter));
				} else if (Character.isAlphabetic(currentCharacter)) {
					sb.append(currentCharacter);
					checkForOperatorMatch();
					break;
				} else {
					emitError(new SyntaxError("Unexpected character in operator name: " + currentCharacter));
				}

			case PRINT_STATEMENT_ITEMS_LOOKUP:
				if (Character.isSpaceChar(currentCharacter)) {
					break;
				} else if (currentCharacter == '"') {
					changeState(State.PRINT_STATEMENT_STRING_ITEM_PARSING);
					break;
				} else if (currentCharacter == '\n') {
					changeState(State.LINE_NUMBER_LOOKUP);
					break;
				} else if (currentCharacter == ':') {
					changeState(State.STATEMENT_LOOKUP);
					break;
				}else if (Character.isDigit(currentCharacter) || Character.isAlphabetic(currentCharacter)) {
					sb.append(currentCharacter);
					changeState(State.PRINT_STATEMENT_EXPRESSION_ITEM_PARSING);
					break;
				} else {
					emitError(new SyntaxError("Unexpected character in operator name: " + currentCharacter));
				}

			case PRINT_STATEMENT_STRING_ITEM_PARSING:
				if (currentCharacter != '"') {
					sb.append(currentCharacter);
				} else {
					//printStatementItems.add(new PrintStatementStringItem())
					changeState(State.PRINT_STATEMENT_ITEMS_LOOKUP);
				}
				break;
		}
	}

	private void checkForOperatorMatch() {
		switch (sb.toString().toUpperCase()) {
			case "PRINT":
				changeState(State.PRINT_STATEMENT_ITEMS_LOOKUP);
				break;
		}
	}

	private void changeState(final State newState) {
		if (state == newState) {
			throw new RuntimeException("Already has state: " + state);
		}

		L.d("Basic", state + " -> " + newState);

		state = newState;

		switch (state) {
			case LINE_NUMBER_LOOKUP:
				sb.setLength(0);
				break;

			case STATEMENT_LOOKUP:
				sb.setLength(0);
				break;

			case PRINT_STATEMENT_ITEMS_LOOKUP:
				printStatementItems.clear();
				sb.setLength(0);
				break;
		}
	}

	private void emitError(final Throwable error) throws Throwable {
		state = State.ERROR_DETECTED;
		throw error;
	}

	public enum State {
		LINE_NUMBER_LOOKUP,
		LINE_NUMBER_PARSING,
		STATEMENT_LOOKUP,
		STATEMENT_PARSING,
		PRINT_STATEMENT_ITEMS_LOOKUP,
		PRINT_STATEMENT_STRING_ITEM_PARSING,
		PRINT_STATEMENT_EXPRESSION_ITEM_PARSING,
		PARSING_FINISHED,
		ERROR_DETECTED
	}
}
