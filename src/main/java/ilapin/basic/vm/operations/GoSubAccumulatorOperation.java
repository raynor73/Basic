package ilapin.basic.vm.operations;

import ilapin.basic.vm.BadBranchError;
import ilapin.basic.vm.BasicError;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class GoSubAccumulatorOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    public GoSubAccumulatorOperation(final BasicVirtualMachineImpl vm) {
        this.vm = vm;
    }

    @Override
    public void execute() throws BasicError {
        final Integer goToOperationIndex = vm.getLineToIndexMap().get(vm.getAccumulator());
        if (goToOperationIndex != null) {
            vm.getSubroutineReturnStack().add(vm.getCurrentOperationIndex());
            vm.setCurrentOperationIndex(goToOperationIndex);
        } else {
            throw new BadBranchError();
        }
    }
}
