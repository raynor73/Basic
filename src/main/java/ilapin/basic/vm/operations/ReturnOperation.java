package ilapin.basic.vm.operations;

import ilapin.basic.vm.BadReturnError;
import ilapin.basic.vm.BasicError;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

import java.util.LinkedList;

public class ReturnOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    public ReturnOperation(final BasicVirtualMachineImpl vm) {
        this.vm = vm;
    }

    @Override
    public void execute() throws BasicError {
        final LinkedList<Integer> subroutineReturnStack = vm.getSubroutineReturnStack();
        if (subroutineReturnStack.size() > 0) {
            //noinspection ConstantConditions
            vm.setCurrentOperationIndex(subroutineReturnStack.pollLast());
        } else {
            vm.emitError(new BadReturnError());
        }
    }
}
