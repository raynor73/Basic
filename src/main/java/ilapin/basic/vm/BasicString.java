package ilapin.basic.vm;

import ilapin.basic.StringError;
import ilapin.basic.StringOverflowError;

public class BasicString {

	private final int maxLength;
	private int currentLength;
	private final Character[] characters;

	public BasicString(final String string) {
		characters = new Character[string.length()];
		for (int i = 0; i < characters.length; i++) {
			characters[i] = string.charAt(i);
		}
		maxLength = characters.length;
		currentLength = maxLength;
	}

	public BasicString(final Character[] characters) {
		maxLength = characters.length;
		currentLength = maxLength;
		this.characters = new Character[maxLength];
		System.arraycopy(characters, 0, this.characters, 0, this.characters.length);
	}

	public BasicString(final int maxLength) throws BasicError {
		if (maxLength < 0 || maxLength > 255) {
			throw new Range255OverflowError();
		}
		this.maxLength = maxLength;
		currentLength = 0;
		characters = new Character[maxLength];
	}

	public int getMaxLength() {
		return maxLength;
	}

	public int getCurrentLength() {
		return currentLength;
	}

	public Character[] getCharacters() {
		return characters;
	}

	public void setCharacters(
			final BasicString sourceString,
			final int destinationStart,
			final int sourceStart,
			final int sourceEnd
	) throws BasicError {
		int i = 0;
		final int sourceLength = sourceEnd - sourceStart + 1;
		final Character[] source = sourceString.getCharacters();
		while (i < sourceLength) {
			final int sourceIndex = i + sourceStart;
			final int destinationIndex = i + destinationStart;
			if (sourceIndex >= sourceString.maxLength) {
				throw new StringError();
			}
			if (destinationIndex >= maxLength) {
				throw new StringOverflowError();
			}
			characters[destinationIndex] = source[sourceIndex];
			i++;
		}
		currentLength = sourceLength;
	}
}
