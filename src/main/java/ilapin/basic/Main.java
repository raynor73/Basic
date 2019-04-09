package ilapin.basic;

import ilapin.basic.vm.*;

public class Main {

	private final BasicVirtualMachine vm =
			BasicVirtualMachineFactory.createVm(new StdoutStringPrinter());

	public static void main(final String[] args) {
		new Main().start();
	}

	private void start() {
		vm.addPrintStringOperation(new BasicString("Hello world\n"), 10);
		vm.addEndOperation(20);

		BasicVirtualMachine.State vmState = vm.getState();
		while (vmState == BasicVirtualMachine.State.IDLE || vmState == BasicVirtualMachine.State.RUNNING) {
			try {
				vm.step();
			} catch (final BasicError e) {
				e.printStackTrace();
			}
			vmState = vm.getState();
		}
	}
}
