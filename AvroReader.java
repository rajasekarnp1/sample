import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.FileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import com.example.User;  // Import the generated class

import java.io.File;
import java.io.IOException;

public class AvroReader {
    public static void main(String[] args) {
        File avroFile = new File("path/to/user.avro");
        DatumReader<User> userDatumReader = new GenericDatumReader<>(User.getClassSchema());
        try (FileReader<User> fileReader = DataFileReader.openReader(avroFile, userDatumReader)) {
            while (fileReader.hasNext()) {
                User user = fileReader.next();
                
                // Assign the values to a Java record
                UserRecord userRecord = new UserRecord(user.getName().toString(), user.getAge(), user.getEmail() != null ? user.getEmail().toString() : null);
                
                // Output or use the record
                System.out.println(userRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
