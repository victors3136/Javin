package interpreter.view.commands;

import interpreter.controller.Controller;
import interpreter.controller.ControllerException;
import interpreter.model.exceptions.*;
import interpreter.repository.RepositoryException;
import interpreter.view.Command;

public class RunProgramCommand extends Command {
    private final Controller controller;

    public RunProgramCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.takeAllSteps();
        } catch (RepositoryException | SymbolTableException | ControllerException | StatementException |
                 ValueException | ExpressionException | TypeException | HeapException | ProgramStateException e) {
            System.err.println(e.getMessage());
        }
    }
}
