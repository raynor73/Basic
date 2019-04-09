package ilapin.basic.vm.operations;

import ilapin.basic.vm.BasicString;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class PrintStringOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    private final BasicString string;

    public PrintStringOperation(final BasicVirtualMachineImpl vm, final BasicString string) {
        this.vm = vm;
        this.string = string;
    }

    @Override
    public void execute() {
        vm.getStringPrinter().print(string.getCharacters());
        vm.incrementOperationIndex();
    }
}
