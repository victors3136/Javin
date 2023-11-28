import inputmanager.StringToStatementConverter;
import inputmanager.InputManager;
import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.controller.ControllerImplementation;
import interpreter.model.programstate.ProgramState;
import interpreter.model.programstate.ProgramStateImplementation;
import interpreter.model.statements.*;
import interpreter.model.exceptions.ExpressionException;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryVector;
import interpreter.view.TextMenu;
import interpreter.view.commands.ExitCommand;
import interpreter.view.commands.RunProgramCommand;

import java.util.Scanner;

public class Application {
    private static final StringToStatementConverter inputManager = new InputManager();

    private static String programFromSysIn() {
        StringBuilder buff = new StringBuilder();
        System.out.print("> ");
        Scanner source = new Scanner(System.in);
        do buff.append(source.nextLine());
        while (!(buff.length() < 2) && (buff.charAt(buff.length() - 1) == ';'));
        return buff.toString();
    }

    public static void main(String[] args) {
        String arithmeticAndConditionalSource =
                """
                        int a;
                        str b;
                        a<-100/10^2;
                        if(a==1)((
                        \t  b<-"order of operations is respected"
                        )else(
                        \t  b<-"order of operation is not respected"
                        ));
                        print(b)
                        """;
        String stringConcatSource =
                """
                        str b;
                        b<-"b";
                        str bb;
                        bb<-b+b;
                        str b3;
                        b3<-bb+b;
                        print(b3)
                        """;
        String fileOperationSource =
                """
                        fopen("in.txt");
                        \t  fopen("in2.txt");
                        \t  \t  int a;
                        \t  \t  fread("in.txt", a);
                        \t  \t  \t  print(a);
                        \t  \t  \t  fread("in2.txt", a);
                        \t  \t  \t  \t  print(a);
                        \t  fclose("in2.txt");
                        fclose("in.txt")
                        """;
        String repetitiveInstructionSource =
                """
                        int counter;
                        counter <- 0;
                        int i;
                        int j;
                        i <- 1;
                        while(i < 5) (
                        \t  j<-i+1;
                        \t  int a;
                        \t  while(j<5) (
                        \t  \t  bool b;
                        \t  \t  counter<-counter+1;
                        \t  \t  j<-j+1
                        \t  );
                        \t  str c;
                        \t  i<-i+1
                        );
                        print(counter)
                        """;
        String heapAllocationSource =
                """
                        ref int a;
                        ref ref int b;
                        ref ref ref int c;
                        ref int d;
                        ref int e;
                        heap_alloc(a, 10);
                        heap_alloc(b, a);
                        heap_alloc(c, b);
                        heap_alloc(d, 10);
                        heap_alloc(e, 10);
                        print("a");
                        print(heap_read(a));
                        heap_write(a, 100);
                        heap_write(b, d);
                        print(heap_read(a));
                        print("b");
                        print(heap_read(b));
                        print(heap_read(heap_read(b)));
                        print("c");
                        print(heap_read(c));
                        print(heap_read(heap_read(c)));
                        print(heap_read(heap_read(heap_read(c))))
                        """;
        String garbageCollectSource =
                """
                    int a;
                    str b;
                    ref int c;
                    heap_alloc(c, 10);
                    heap_write(c, 100);
                    ref ref int d;
                    heap_alloc(d, c);
                    b <- "in4.txt";
                    fopen( b );
                    fread( b, a );
                    if(a == 0 )((
                    \t  ref int e;
                    \t  heap_alloc(e,a)
                    )else(
                    \t  ref int f;
                    \t  heap_alloc(f, 10)
                    ));
                    print(a);
                    print(b);
                    fclose(b)
                    """;
        Statement arithmeticAndConditionalTest = null;
        Statement stringConcatTest = null;
        Statement fileOperationTest = null;
        Statement repetitiveInstructionTest = null;
        Statement heapAllocationTest = null;
        Statement garbageCollectTest = null;
        try {
            arithmeticAndConditionalTest = inputManager.program(arithmeticAndConditionalSource);
            stringConcatTest = inputManager.program(stringConcatSource);
            fileOperationTest = inputManager.program(fileOperationSource);
            repetitiveInstructionTest = inputManager.program(repetitiveInstructionSource);
            heapAllocationTest = inputManager.program(heapAllocationSource);
            garbageCollectTest = inputManager.program(garbageCollectSource);
        } catch (TokenizerException | ParseException | ExpressionException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        ProgramState programState1 = new ProgramStateImplementation(arithmeticAndConditionalTest);
        ProgramState programState2 = new ProgramStateImplementation(stringConcatTest);
        ProgramState programState3 = new ProgramStateImplementation(fileOperationTest);
        ProgramState programState4 = new ProgramStateImplementation(repetitiveInstructionTest);
        ProgramState programState5 = new ProgramStateImplementation(heapAllocationTest);
        ProgramState programState6 = new ProgramStateImplementation(garbageCollectTest);
        Repository repositoryVector1 = new RepositoryVector("logs/log1.txt");
        Repository repositoryVector2 = new RepositoryVector("logs/log2.txt");
        Repository repositoryVector3 = new RepositoryVector("logs/log3.txt");
        Repository repositoryVector4 = new RepositoryVector("logs/log4.txt");
        Repository repositoryVector5 = new RepositoryVector("logs/log5.txt");
        Repository repositoryVector6 = new RepositoryVector("logs/log6.txt");
        Controller controller1 = new ControllerImplementation(arithmeticAndConditionalSource, programState1, repositoryVector1);
        Controller controller2 = new ControllerImplementation(stringConcatSource, programState2, repositoryVector2);
        Controller controller3 = new ControllerImplementation(fileOperationSource, programState3, repositoryVector3);
        Controller controller4 = new ControllerImplementation(repetitiveInstructionSource, programState4, repositoryVector4);
        Controller controller5 = new ControllerImplementation(heapAllocationSource, programState5, repositoryVector5);
        Controller controller6 = new ControllerImplementation(garbageCollectSource, programState6, repositoryVector6);

        TextMenu mainMenu = new TextMenu();
        mainMenu.addCommand("exit", new ExitCommand("exit", "exit"));
        mainMenu.addCommand("1", new RunProgramCommand("1", arithmeticAndConditionalSource, controller1));
        mainMenu.addCommand("2", new RunProgramCommand("2", stringConcatSource, controller2));
        mainMenu.addCommand("3", new RunProgramCommand("3", fileOperationSource, controller3));
        mainMenu.addCommand("4", new RunProgramCommand("4", repetitiveInstructionSource, controller4));
        mainMenu.addCommand("5", new RunProgramCommand("5", heapAllocationSource, controller5));
        mainMenu.addCommand("6", new RunProgramCommand("6", garbageCollectSource, controller6));
        mainMenu.show();
    }
}
