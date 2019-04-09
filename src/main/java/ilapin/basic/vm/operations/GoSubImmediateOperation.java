package ilapin.basic.vm.operations;

import ilapin.basic.vm.BadBranchError;
import ilapin.basic.vm.BasicError;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class GoSubImmediateOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    private final int lineNumber;

    private GoSubImmediateOperation(final BasicVirtualMachineImpl vm, final int lineNumber) {
        this.vm = vm;
        this.lineNumber = lineNumber;
    }

    @Override
    public void execute() throws BasicError {
        final Integer goToOperationIndex = vm.getLineToIndexMap().get(lineNumber);
        if (goToOperationIndex != null) {
            vm.getSubroutineReturnStack().add(vm.getCurrentOperationIndex());
            vm.setCurrentOperationIndex(goToOperationIndex);
        } else {
            throw new BadBranchError();
        }
    }
}
