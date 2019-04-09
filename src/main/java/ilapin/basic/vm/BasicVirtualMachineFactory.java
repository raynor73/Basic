package ilapin.basic.vm;

public class BasicVirtualMachineFactory {

    public static BasicVirtualMachine createVm(final StringPrinter stringPrinter) {
        return new BasicVirtualMachineImpl(stringPrinter);
    }
}
