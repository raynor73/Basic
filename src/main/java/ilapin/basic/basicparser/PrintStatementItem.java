package ilapin.basic.basicparser;

public abstract class PrintStatementItem {

	private final boolean shouldBeAligned;

	protected PrintStatementItem(final boolean shouldBeAligned) {
		this.shouldBeAligned = shouldBeAligned;
	}
}
