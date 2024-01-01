package interpreter.controller;

import interpreter.model.programstate.ProgramState;

import java.util.List;

public interface Controller {
    String takeAllSteps();
    List<ProgramState> removeCompletedPrograms(List<ProgramState> input);
}
