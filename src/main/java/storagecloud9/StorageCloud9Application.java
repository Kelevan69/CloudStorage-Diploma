package storagecloud9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class StorageCloud9Application { // С большой "C"
    public static void main(String[] args) {
        SpringApplication.run(StorageCloud9Application.class, args);
    }
}