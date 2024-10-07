import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.FileReader;
import org.apache.avro.reflect.ReflectDatumReader;
import java.io.File;
import java.io.IOException;

public class AvroReflectReader {
    public static void main(String[] args) {
        File avroFile = new File("path/to/user.avro");
        ReflectDatumReader<UserRecord> datumReader = new ReflectDatumReader<>(UserRecord.class);

        try (FileReader<UserRecord> fileReader = DataFileReader.openReader(avroFile, datumReader)) {
            while (fileReader.hasNext()) {
                UserRecord user = fileReader.next();
                System.out.println(user); // Output the UserRecord
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
