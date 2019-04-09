package ilapin.basic.vm.operations;

import ilapin.basic.vm.BasicError;
import ilapin.basic.vm.BasicString;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

import java.util.Map;

public class LoadStringToStringOperation implements Operation {

    private final BasicVirtualMachineImpl vm;

    private final String destinationVariableName;
    private final int destinationStart;
    private final String sourceVariableName;
    private final int sourceStart;
    private final int sourceEnd;

    private LoadStringToStringOperation(
            final BasicVirtualMachineImpl vm,
            final String destinationVariableName,
            final int destinationStart,
            final String sourceVariableName,
            final int sourceStart,
            final int sourceEnd
    ) {
        this.vm = vm;
        this.destinationVariableName = destinationVariableName;
        this.destinationStart = destinationStart;
        this.sourceVariableName = sourceVariableName;
        this.sourceStart = sourceStart;
        this.sourceEnd = sourceEnd;
    }

    @Override
    public void execute() throws BasicError {
        final Map<String, BasicString> stringVariables = vm.getStringVariables();

        final BasicString sourceString = stringVariables.get(sourceVariableName);
        final BasicString destinationString = stringVariables.get(destinationVariableName);
        if (sourceString != null && sourceString.getCurrentLength() > 0) {
            if (destinationString != null) {
                destinationString.setCharacters(sourceString, destinationStart, sourceStart, sourceEnd);
            } else {
                final BasicString newDestinationString = new BasicString(1);
                stringVariables.put(destinationVariableName, newDestinationString);
                newDestinationString.setCharacters(sourceString, destinationStart, sourceStart, sourceEnd);
            }
        } else {
            if (destinationString != null) {
                stringVariables.put(destinationVariableName, new BasicString(destinationString.getMaxLength()));
            } else {
                stringVariables.put(destinationVariableName, new BasicString(0));
            }
        }
    }
}
