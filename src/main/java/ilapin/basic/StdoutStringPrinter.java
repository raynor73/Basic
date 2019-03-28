package ilapin.basic;

public class StdoutStringPrinter implements StringPrinter {

	@Override
	public void print(final String string) {
		System.out.print(string);
	}
}
