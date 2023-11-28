package interpreter.controller;

import interpreter.model.exceptions.*;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.heaptable.HeapHashTable;
import interpreter.model.programstate.ProgramState;
import interpreter.model.statements.Statement;
import interpreter.model.values.ReferenceValue;
import interpreter.model.values.Value;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryException;

import java.util.*;
import java.util.stream.Collectors;

public class ControllerImplementation implements Controller {
    final Repository repository;
    final String input;

    public ControllerImplementation(String input, ProgramState programState, Repository repository) {
        this.input = input;
        this.repository = repository;
        this.repository.add(programState);
    }


    @Override
    public void setCurrentProgramInRepo(int position) throws RepositoryException {
        this.repository.setCurrentProgram(position);
    }

    @Override
    public ProgramState takeOneStep(ProgramState programState) throws ControllerException, SymbolTableException, StatementException, ValueException, ExpressionException, HeapException {
        ExecutionStack<Statement> executionStack = programState.getExecutionStack();
        if (executionStack.empty()) {
            throw new ControllerException("Empty Stack when trying to take another step inside the program");
        }
        Statement currentStatement = executionStack.pop();
        return currentStatement.execute(programState);
    }

    @Override
    public void takeAllSteps() throws SymbolTableException, ControllerException, StatementException, ValueException, ExpressionException, RepositoryException, HeapException {
        ProgramState programState = repository.getCurrentProgram();

        System.out.println("Execution starting for current loaded program ...\n");
        System.out.println(input);
        repository.logProgramStateExecution();
        while (!programState.getExecutionStack().empty()) {
            takeOneStep(programState);
            collectGarbage();
            repository.logProgramStateExecution();
        }
        System.out.println("Final Program Output: \n");
        System.out.println(repository.getCurrentProgram().getOutputList().toString());
        repository.getCurrentProgram().getFileTable().cleanup();
        repository.getCurrentProgram().getHeapTable().cleanup();
        repository.logProgramStateExecution();
    }

    @Override
    public Repository getRepository() {
        return repository;
    }

    private Set<Integer> getReachableAddresses(){
        Set<Integer> reachableAddresses = new HashSet<>();
        reachableAddresses.addAll(repository.getCurrentProgram().getSymbolTable()
                .getValues()
                .stream()
                .filter(x -> x instanceof ReferenceValue)
                .map(x -> ((ReferenceValue) x).getAddress())
                .collect(Collectors.toSet()));
        reachableAddresses.addAll(repository.getCurrentProgram().getHeapTable()
                .entriesStream()
                .filter(x -> x.getValue() instanceof ReferenceValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet()));
        return reachableAddresses;

    }
    private void collectGarbage() {
        HashMap<Integer, Value> newHeap = repository.getCurrentProgram().getHeapTable()
                .entriesStream()
                .filter(x -> getReachableAddresses().contains(x.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value, repeatValue) -> value,
                        HashMap::new));
        repository.getCurrentProgram().setHeapTable(new HeapHashTable(newHeap));
    }

    Collection<Integer> getReferencesFromSymbolTable() {
        return repository
                .getCurrentProgram()
                .getSymbolTable()
                .getValues()
                .stream()
                .filter(value -> value instanceof ReferenceValue)
                .map(refVal -> ((ReferenceValue) refVal).getAddress())
                .collect(Collectors.toList());
    }
}
