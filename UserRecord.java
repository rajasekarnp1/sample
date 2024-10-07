import org.apache.avro.reflect.Nullable;

public record UserRecord(String name, int age, @Nullable String email) {}
