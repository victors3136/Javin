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

public class Application {
    private static final StringToStatementConverter inputManager = new InputManager();

    public static void main(String[] args) {
        String arithmeticAndConditionalSource =
                """
                        int a;
                        str b;
                        a<-100/10^2;
                        if(a==1)((
                            b<-"order of operations is respected"
                        )else(
                            b<-"order of operation is not respected"
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
                        fopen("in2.txt");
                        int a;
                        fread("in.txt", a);
                        print(a);
                        fread("in2.txt", a);
                        print(a);
                        fclose("in2.txt");
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
                            j<-i+1;
                            int a;
                            while(j<5) (
                                bool b;
                                counter<-counter+1;
                                j<-j+1
                            );
                            str c;
                            i<-i+1
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
                        b <- "in4.txt";
                        fopen( b );
                        fread( b, a );
                        if(a == 0 )((
                            ref int e;
                            heap_alloc(e,a)
                        )else(
                            ref int f;
                            heap_alloc(f, 10)
                        ));
                        print(a);
                        print(b);
                        fclose(b);
                        ref int v;
                        heap_alloc(v,20);
                        ref ref int alpha;
                        heap_alloc(v,30)
                        """;
        String forkSource =
                """
                        ref int counter;
                        heap_alloc(counter, 0);
                        while(heap_read(counter) < 10)(
                            fork(
                                print(heap_read(counter));
                                fork(
                                    print(heap_read(counter))
                                )
                            );
                            heap_write(counter, heap_read(counter) + 1)
                        );
                        print(heap_read(counter))
                        """;
        Statement arithmeticAndConditionalTest = null;
        Statement stringConcatTest = null;
        Statement fileOperationTest = null;
        Statement repetitiveInstructionTest = null;
        Statement heapAllocationTest = null;
        Statement garbageCollectTest = null;
        Statement forkTest = null;
        try {
            arithmeticAndConditionalTest = inputManager.program(arithmeticAndConditionalSource);
            stringConcatTest = inputManager.program(stringConcatSource);
            fileOperationTest = inputManager.program(fileOperationSource);
            repetitiveInstructionTest = inputManager.program(repetitiveInstructionSource);
            heapAllocationTest = inputManager.program(heapAllocationSource);
            garbageCollectTest = inputManager.program(garbageCollectSource);
            forkTest = inputManager.program(forkSource);
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
        ProgramState programState7 = new ProgramStateImplementation(forkTest);
        Repository repositoryVector1 = new RepositoryVector("logs/log1.txt");
        Repository repositoryVector2 = new RepositoryVector("logs/log2.txt");
        Repository repositoryVector3 = new RepositoryVector("logs/log3.txt");
        Repository repositoryVector4 = new RepositoryVector("logs/log4.txt");
        Repository repositoryVector5 = new RepositoryVector("logs/log5.txt");
        Repository repositoryVector6 = new RepositoryVector("logs/log6.txt");
        Repository repositoryVector7 = new RepositoryVector("logs/log7.txt");
        Controller controller1 = new ControllerImplementation(programState1, repositoryVector1);
        Controller controller2 = new ControllerImplementation(programState2, repositoryVector2);
        Controller controller3 = new ControllerImplementation(programState3, repositoryVector3);
        Controller controller4 = new ControllerImplementation(programState4, repositoryVector4);
        Controller controller5 = new ControllerImplementation(programState5, repositoryVector5);
        Controller controller6 = new ControllerImplementation(programState6, repositoryVector6);
        Controller controller7 = new ControllerImplementation(programState7, repositoryVector7);
        TextMenu mainMenu = new TextMenu();
        mainMenu.addCommand("exit", new ExitCommand("exit", "exit"));
        mainMenu.addCommand("1", new RunProgramCommand("1", arithmeticAndConditionalSource, controller1));
        mainMenu.addCommand("2", new RunProgramCommand("2", stringConcatSource, controller2));
        mainMenu.addCommand("3", new RunProgramCommand("3", fileOperationSource, controller3));
        mainMenu.addCommand("4", new RunProgramCommand("4", repetitiveInstructionSource, controller4));
        mainMenu.addCommand("5", new RunProgramCommand("5", heapAllocationSource, controller5));
        mainMenu.addCommand("6", new RunProgramCommand("6", garbageCollectSource, controller6));
        mainMenu.addCommand("7", new RunProgramCommand("7", forkSource, controller7));
        mainMenu.show();
    }
}
