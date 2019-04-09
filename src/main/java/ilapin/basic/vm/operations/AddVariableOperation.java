package ilapin.basic.vm.operations;

import ilapin.basic.vm.BasicError;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class AddVariableOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    private final String variableName;
    private final int index;

    private AddVariableOperation(final BasicVirtualMachineImpl vm, final String variableName) {
        this(vm, variableName, 0);
    }

    private AddVariableOperation(
            final BasicVirtualMachineImpl vm,
            final String variableName,
            final int index
    ) {
        this.vm = vm;
        this.variableName = variableName;
        this.index = index;
    }

    @Override
    public void execute() throws BasicError {
        final Integer value = vm.getNumberVariables().get(variableName)[index];
        vm.setAccumulator(vm.getAccumulator() + (value != null ? value : 0));
        vm.checkIntegerRange(vm.getAccumulator());
        vm.incrementOperationIndex();
    }
}
