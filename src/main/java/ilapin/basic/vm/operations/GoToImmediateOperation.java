package ilapin.basic.vm.operations;

import ilapin.basic.vm.BadBranchError;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class GoToImmediateOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    private final int lineNumber;

    private GoToImmediateOperation(final BasicVirtualMachineImpl vm, final int lineNumber) {
        this.vm = vm;
        this.lineNumber = lineNumber;
    }

    @Override
    public void execute() throws BadBranchError {
        final Integer goToOperationIndex = vm.getLineToIndexMap().get(lineNumber);
        if (goToOperationIndex != null) {
            vm.setCurrentOperationIndex(goToOperationIndex);
        } else {
            throw new BadBranchError();
        }
    }
}
