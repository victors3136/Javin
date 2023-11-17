package interpreter.controller;

import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.programstate.ProgramState;
import interpreter.model.statements.Statement;
import interpreter.model.exceptions.*;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryException;

public class ControllerImplementation implements Controller {
    Repository repository;
    public ControllerImplementation(ProgramState programState, Repository repository) {
        this.repository = repository;
        this.repository.add(programState);
    }
    @Override
    public void setCurrentProgramInRepo(int position) throws RepositoryException {
        this.repository.setCurrentProgram(position);
    }

    @Override
    public ProgramState takeOneStep(ProgramState programState) throws ControllerException, SymbolTableException, StatementException, ValueException, ExpressionException, TypeException {
        ExecutionStack<Statement> executionStack = programState.getExecutionStack();
        if (executionStack.empty()) {
            throw new ControllerException("Empty Stack when trying to take another step inside the program");
        }
        Statement currentStatement = executionStack.pop();
        return currentStatement.execute(programState);
    }

    @Override
    public void takeAllSteps() throws SymbolTableException, ControllerException, StatementException, ValueException, ExpressionException, RepositoryException, TypeException {
        ProgramState programState = repository.getCurrentProgram();

        System.out.println("Execution starting for current loaded program ...\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println(programState.getExecutionStack());
        repository.logProgramStateExecution();
        while (!programState.getExecutionStack().empty()) {
            ProgramState captureResultSoIntelliJWontComplain = takeOneStep(programState);
            repository.logProgramStateExecution();
        }
        System.out.println("Final Program Output\n");
        System.out.println(repository.getCurrentProgram().getOutputList().toString());
    }

    @Override
    public Repository getRepository() {
        return repository;
    }
}
