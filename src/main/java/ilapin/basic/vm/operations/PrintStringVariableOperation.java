package ilapin.basic.vm.operations;

import ilapin.basic.vm.BasicString;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class PrintStringVariableOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    private final String variableName;
    private final int start;
    private final int end;

    public PrintStringVariableOperation(
            final BasicVirtualMachineImpl vm,
            final String variableName,
            final int start,
            final int end
    ) {
        this.vm = vm;
        this.variableName = variableName;
        this.start = start;
        this.end = end;
    }

    @Override
    public void execute() {
        final BasicString string = vm.getStringVariables().get(variableName);
        if (string != null) {
            vm.getStringPrinter().print(string.getCharacters(), start, end);
        } else {
            vm.getStringPrinter().print(new Character[]{});
        }
        vm.incrementOperationIndex();
    }
}
