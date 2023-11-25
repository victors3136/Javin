import com.sun.jdi.IntegerType;
import inputmanager.InputManager;
import inputmanager.InputMuncher;
import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.controller.ControllerImplementation;
import interpreter.model.expressions.ArithmeticExpression;
import interpreter.model.expressions.RelationalExpression;
import interpreter.model.expressions.ValueExpression;
import interpreter.model.expressions.VariableExpression;
import interpreter.model.operands.Operand;
import interpreter.model.programstate.ProgramState;
import interpreter.model.programstate.ProgramStateImplementation;
import interpreter.model.statements.*;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.type.IntType;
import interpreter.model.values.IntValue;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryVector;
import interpreter.view.TextMenu;
import interpreter.view.commands.ExitCommand;
import interpreter.view.commands.RunProgramCommand;

import java.util.Scanner;

public class Application {
    private static final InputManager muncher = new InputMuncher();

    private static String readInput() {
        StringBuilder buff = new StringBuilder();
        System.out.print("> ");
        Scanner source = new Scanner(System.in);

        do buff.append(source.nextLine());
        while (!(buff.length() < 2) && (buff.charAt(buff.length() - 1) == ';'));

        return buff.toString();
    }

    public static void main(String[] args) {
        Statement example1 = null, example2 = null, example3 = null, example4 = null;
        try {
            example1 = muncher.eatStringCreateProgram(
                    "int a;" +
                            "str b;" +
                            "a<-100/10^2;" +
                            "if(a==1)" +
                            "((b<-\"order of operations is respected\")" +
                            "else" +
                            "(b<-\"order of operation is not respected\"));" +
                            "print(b)");
            System.out.println("1");
            example2 = muncher.eatStringCreateProgram(
                    "str b;" +
                            "b<-\"b\";" +
                            "str bb;" +
                            "bb<-b+b;" +
                            "str b3;" +
                            "b3<-bb+b;" +
                            "print(b3)");
            System.out.println("2");
            example3 = muncher.eatStringCreateProgram(
                    "fopen(\"in.txt\");" +
                            "fopen(\"in2.txt\");" +
                            "int a; " +
                            "fread(\"in.txt\" a);" +
                            "print(a);" +
                            "fread(\"in2.txt\" a);" +
                            "print(a);" +
                            "fclose(\"in.txt\");" +
                            "fclose(\"in2.txt\")");
            System.out.println("3");
            example4 = muncher.eatStringCreateProgram(
                    "int a;" +
                            "a <- 1;" +
                            "while(a<5)(" +
                            "int b;" +
                            "a<-a+1" +
                            ")");
        } catch (TokenizerException | ParseException | ExpressionException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        ProgramState programState1 = new ProgramStateImplementation(example1);
        ProgramState programState2 = new ProgramStateImplementation(example2);
        ProgramState programState3 = new ProgramStateImplementation(example3);
        ProgramState programState4 = new ProgramStateImplementation(example4);
        Repository repositoryVector1 = new RepositoryVector("logs/log1.txt");
        Repository repositoryVector2 = new RepositoryVector("logs/log2.txt");
        Repository repositoryVector3 = new RepositoryVector("logs/log3.txt");
        Repository repositoryVector4 = new RepositoryVector("logs/log4.txt");
        Controller controller1 = new ControllerImplementation(programState1, repositoryVector1);
        Controller controller2 = new ControllerImplementation(programState2, repositoryVector2);
        Controller controller3 = new ControllerImplementation(programState3, repositoryVector3);
        Controller controller4 = new ControllerImplementation(programState4, repositoryVector4);


        TextMenu mainMenu = new TextMenu();
        mainMenu.addCommand("exit", new ExitCommand("exit", "exit"));
        mainMenu.addCommand("1", new RunProgramCommand("1", example1.toString(), controller1));
        mainMenu.addCommand("2", new RunProgramCommand("2", example2.toString(), controller2));
        mainMenu.addCommand("3", new RunProgramCommand("3", example3.toString(), controller3));
        mainMenu.addCommand("4", new RunProgramCommand("4", example4.toString(), controller4));
        mainMenu.show();
    }
}
