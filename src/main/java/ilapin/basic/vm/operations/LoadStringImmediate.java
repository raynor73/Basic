package ilapin.basic.vm.operations;

import ilapin.basic.vm.BasicError;
import ilapin.basic.vm.BasicString;
import ilapin.basic.vm.BasicVirtualMachineImpl;
import ilapin.basic.vm.Operation;

import java.util.Map;

public class LoadStringImmediate implements Operation {

    private final BasicVirtualMachineImpl vm;

    private final String string;
    final String variableName;

    private LoadStringImmediate(
            final BasicVirtualMachineImpl vm,
            final String variableName,
            final String string
    ) {
        this.vm = vm;
        this.variableName = variableName;
        this.string = string;
    }

    @Override
    public void execute() throws BasicError {
        final Map<String, BasicString> stringVariables = vm.getStringVariables();
        final BasicString destination = stringVariables.get(variableName);
        if (string.length() > 0) {
            final Character[] characters = new Character[string.length()];
            for (int i = 0; i < characters.length; i++) {
                characters[i] = string.charAt(i);
            }
            final BasicString sourceBasicString = new BasicString(characters);
            if (destination != null) {
                destination.setCharacters(sourceBasicString, 0, 0, characters.length - 1);
            } else {
                final BasicString newDestination = new BasicString(1);
                stringVariables.put(variableName, newDestination);
                newDestination.setCharacters(sourceBasicString, 0, 0, characters.length - 1);
            }
        } else {
            if (destination != null) {
                stringVariables.put(variableName, new BasicString(destination.getMaxLength()));
            } else {
                stringVariables.put(variableName, new BasicString(1));
            }
        }
    }
}
