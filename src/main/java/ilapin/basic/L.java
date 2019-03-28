package ilapin.basic;

public class L {

	public static void d(final String tag, final String format, final Object... args) {
		System.out.print(tag);
		System.out.print(": ");
		System.out.println(String.format(format, args));
	}
}
