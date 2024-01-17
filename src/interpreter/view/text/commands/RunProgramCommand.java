package interpreter.view.text.commands;

import interpreter.controller.Controller;

public class RunProgramCommand extends Command {
    private final Controller controller;
    public RunProgramCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }
    @Override
    public void execute() {
        controller.takeAllSteps();
    }
}
