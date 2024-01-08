package interpreter.controller;

import interpreter.model.programstate.ProgramState;

import java.util.List;

public interface Controller {

    void takeOneStepForAll(List<ProgramState> inputList);

    String takeAllSteps();

    List<ProgramState> removeCompletedPrograms(List<ProgramState> input);

    void removeCompletedPrograms();

    ProgramState getProgram();

    void collectGarbage();

    List<ProgramState> getPrograms();
}

