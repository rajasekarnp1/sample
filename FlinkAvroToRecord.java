import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.avro.AvroInputFormat;

import java.io.IOException;

public class FlinkAvroToRecord {
    public static void main(String[] args) throws Exception {
        // Set up the Flink streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Define the Avro schema file path (or parse schema from .avsc file)
        String schemaFilePath = "path/to/user.avsc";
        Schema schema = new Schema.Parser().parse(new Path(schemaFilePath).toString());

        // Define the AvroInputFormat, which reads Avro files as GenericRecord
        AvroInputFormat<GenericRecord> avroInputFormat = new AvroInputFormat<>(new Path("path/to/user.avro"), GenericRecord.class);
        avroInputFormat.setSchema(schema);

        // Read Avro data into a DataStream
        DataStream<GenericRecord> avroStream = env.readFile(avroInputFormat, "path/to/user.avro");

        // Map the Avro GenericRecord to a Java record
        DataStream<UserRecord> userRecordStream = avroStream.map(new MapFunction<GenericRecord, UserRecord>() {
            @Override
            public UserRecord map(GenericRecord record) throws Exception {
                // Extract values from GenericRecord and map them to Java record
                String name = record.get("name").toString();
                int age = (int) record.get("age");
                String email = record.get("email") != null ? record.get("email").toString() : null;

                // Create and return a new UserRecord
                return new UserRecord(name, age, email);
            }
        });

        // Print the mapped Java record
        userRecordStream.print();

        // Execute the Flink job
        env.execute("Flink Avro to Java Record Example");
    }
}
