import inputmanager.StringToStatementConverter;
import inputmanager.InputManager;
import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.controller.ControllerImplementation;
import interpreter.model.exceptions.TypecheckException;
import interpreter.model.programstate.ProgramState;
import interpreter.model.programstate.ProgramStateImplementation;
import interpreter.model.statements.*;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.symboltable.SymbolTableHashMap;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryVector;
import interpreter.view.TextMenu;
import interpreter.view.commands.ExitCommand;
import interpreter.view.commands.RunProgramCommand;

public class Application {
    private static final StringToStatementConverter inputManager = new InputManager();

    public static void main(String[] args) {
        TextMenu mainMenu = new TextMenu();
        mainMenu.addCommand("exit", new ExitCommand("exit", "exit"));
        int size = 7;
        String[] sources = new String[size];
        populateSources(sources);
        int counter = 1;
        for (String source : sources) {
            Statement program;
            try {
                program = inputManager.program(source);
            } catch (TokenizerException | ParseException | ExpressionException e) {
                System.err.println(e.getMessage());
                continue;
            }
            try {
                program.typecheck(new SymbolTableHashMap<>());
                continue;
            } catch (TypecheckException e) {
                System.err.println(e.getMessage());
            }
            ProgramState programState = new ProgramStateImplementation(program);
            Repository repository = new RepositoryVector("logs/log%d.txt".formatted(counter));
            Controller controller = new ControllerImplementation(programState, repository);
            mainMenu.addCommand(String.valueOf(counter), new RunProgramCommand(String.valueOf(counter), source, controller));
            counter ++;
        }
        mainMenu.show();
    }

    private static void populateSources(String[] sources) {
        sources[0] =
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
        sources[1] =
                """
                        str b;
                        b<-"b";
                        str bb;
                        bb<-b+b;
                        str b3;
                        b3<-bb+b;
                        print(b3)
                        """;
        sources[2] =
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
        sources[3] =
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
        sources[4] =
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
        sources[5] =
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
        sources[6] =
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

    }

}
