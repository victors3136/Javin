package interpreter.repository;

import interpreter.model.programstate.ProgramState;

import java.util.List;

public interface Repository {
//    void setCurrentProgram(int position) throws RepositoryException;
//    ProgramState getCurrentProgram();
    void add(ProgramState programState);
    int size();
    void logProgramStateExecution(ProgramState toLog)throws RepositoryException;

    List<ProgramState> getProgramList();
    void setProgramList(List<ProgramState> input);

}
