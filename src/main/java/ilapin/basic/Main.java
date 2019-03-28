package ilapin.basic;

public class Main {

	private final BasicVirtualMachine vm = new BasicVirtualMachine(new StdoutStringPrinter());

	public static void main(final String[] args) {
		new Main().start();
	}

	private void start() {
		vm.addOperation(vm.createPrintStringOperation("Hello world\n"), 10);

		while (!vm.isFinished()) {
			try {
				vm.step();
			} catch (final BasicError e) {
				basicError.printStackTrace();
			}
		}
	}
}
