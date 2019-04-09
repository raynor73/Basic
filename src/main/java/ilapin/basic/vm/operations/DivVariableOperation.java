package ilapin.basic.vm.operations;

import ilapin.basic.vm.BasicError;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class DivVariableOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    private final String variableName;
    private final int index;

    private DivVariableOperation(
            final BasicVirtualMachineImpl vm,
            final String variableName,
            final int index
    ) {
        this.vm = vm;
        this.variableName = variableName;
        this.index = index;
    }

    private DivVariableOperation(final BasicVirtualMachineImpl vm, final String variableName) {
        this.vm = vm;
        this.variableName = variableName;
        this.index = 0;
    }

    @Override
    public void execute() throws BasicError {
        final Integer value = vm.getNumberVariables().get(variableName)[index];
        vm.checkNotZero(value);
        vm.setAccumulator(vm.getAccumulator() / value);
        vm.checkIntegerRange(vm.getAccumulator());
        vm.incrementOperationIndex();
    }
}
