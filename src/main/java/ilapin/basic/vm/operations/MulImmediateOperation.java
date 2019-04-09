package ilapin.basic.vm.operations;

import ilapin.basic.vm.BasicError;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class MulImmediateOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    private final int value;

    private MulImmediateOperation(final BasicVirtualMachineImpl vm, final int value) {
        this.vm = vm;
        this.value = value;
    }

    @Override
    public void execute() throws BasicError {
        vm.setAccumulator(vm.getAccumulator() * value);
        vm.checkIntegerRange(vm.getAccumulator());
        vm.incrementOperationIndex();
    }
}
