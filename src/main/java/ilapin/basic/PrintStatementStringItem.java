package ilapin.basic;

import ilapin.basic.vm.BasicString;

public class PrintStatementStringItem extends PrintStatementItem {

	private final BasicString string;

	protected PrintStatementStringItem(final boolean shouldBeAligned, final BasicString string) {
		super(shouldBeAligned);
		this.string = string;
	}

	public BasicString getBasicString() {
		return string;
	}
}
