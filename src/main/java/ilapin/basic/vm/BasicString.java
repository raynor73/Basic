package ilapin.basic.vm;

import ilapin.basic.basicparser.StringError;
import ilapin.basic.basicparser.StringOverflowError;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BasicString that = (BasicString) o;
        return currentLength == that.currentLength &&
                Arrays.equals(characters, that.characters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(currentLength);
        result = 31 * result + Arrays.hashCode(characters);
        return result;
    }

    @Override
    public String toString() {
        final char[] chars = new char[characters.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = characters[i];
        }
        return String.valueOf(chars) + "(" + currentLength + ", " + maxLength + ")";
    }
}
