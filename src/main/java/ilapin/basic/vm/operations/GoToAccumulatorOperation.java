package ilapin.basic.vm.operations;

import ilapin.basic.vm.BadBranchError;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class GoToAccumulatorOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    public GoToAccumulatorOperation(final BasicVirtualMachineImpl vm) {
        this.vm = vm;
    }

    @Override
    public void execute() throws BadBranchError {
        final Integer goToOperationIndex = vm.getLineToIndexMap().get(vm.getAccumulator());
        if (goToOperationIndex != null) {
            vm.setCurrentOperationIndex(goToOperationIndex);
        } else {
            throw new BadBranchError();
        }
    }
}
