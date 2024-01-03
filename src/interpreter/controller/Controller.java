package interpreter.controller;

import interpreter.model.programstate.ProgramState;
import interpreter.repository.Repository;

import java.util.List;

public interface Controller {
    String getRepoRepresentation();

    Repository takeOneStepForAll(List<ProgramState> inputList);

    String takeAllSteps();
    List<ProgramState> removeCompletedPrograms(List<ProgramState> input);
    void removeCompletedPrograms();
    ProgramState getProgram();

    void collectGarbage();

    List<ProgramState> getPrograms();
}

