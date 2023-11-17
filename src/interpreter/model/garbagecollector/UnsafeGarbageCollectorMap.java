package interpreter.model.garbagecollector;

import interpreter.model.values.Value;

import java.util.Map;

public class UnsafeGarbageCollectorMap implements GarbageCollector {
    Map<Integer,Value> storage;

}
