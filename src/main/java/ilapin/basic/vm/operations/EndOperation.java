package ilapin.basic.vm.operations;

import ilapin.basic.vm.BasicError;
import ilapin.basic.vm.BasicVirtualMachine;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

public class EndOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    public EndOperation(final BasicVirtualMachineImpl vm) {
        this.vm = vm;
    }

    @Override
    public void execute() throws BasicError {
        vm.setState(BasicVirtualMachine.State.FINISHED);
    }
}
