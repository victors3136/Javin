package interpreter.view;

import java.util.ArrayList;
import java.util.List;

public class SourceGenerator {
    public static final int NAME = 0;
    public static final int CODE = 1;

    private SourceGenerator() {
    }


    static public List<String[]> makeList() {
        List<String[]> list = new ArrayList<>();
        list.add(new String[]
                {
                        "order of operations",
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
                                """
                }
        );

        list.add(new String[]
                {
                        "string concatenation"
                        ,
                        """
                                str b;
                                b<-"b";
                                str bb;
                                bb<-b+b;
                                str b3;
                                b3<-bb+b;
                                print(b3)
                                """
                }
        );
        list.add(new String[]
                {
                        "file operations"
                        ,
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
                                """
                }
        );
        list.add(new String[]
                {
                        "while statement"
                        ,
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
                                """
                }
        );
        list.add(new String[]
                {
                        "references"
                        ,
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
                                """
                }
        );
        list.add(new String[]
                {
                        "garbage collection"
                        ,
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
                                """
                }
        );
        list.add(new String[]
                {
                        "concurrency"
                        ,
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
                                """
                }
        );
        return list;
    }
}
