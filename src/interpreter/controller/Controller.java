package interpreter.controller;

import interpreter.model.programstate.ProgramState;
import interpreter.repository.RepositoryException;

import java.util.List;

public interface Controller {

    void takeOneStepForAll(List<ProgramState> inputList);

    String takeAllSteps();

    List<ProgramState> removeCompletedPrograms(List<ProgramState> input);

    void removeCompletedPrograms();

    ProgramState getProgram();

    void collectGarbage();

    List<ProgramState> programs();
    public void log(ProgramState toLog) throws RepositoryException;
}

