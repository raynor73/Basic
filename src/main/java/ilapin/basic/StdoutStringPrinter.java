package ilapin.basic;

import ilapin.basic.vm.StringPrinter;

public class StdoutStringPrinter implements StringPrinter {

	@Override
	public void print(final Character[] string, final int start, final int end) {
		for (int i = start; i <= end; i++) {
			System.out.print(string[i]);
		}
	}

	@Override
	public void print(final Character[] string) {
		for (final Character character : string) {
			System.out.print(character);
		}
	}
}
