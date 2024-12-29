package kr.co.lionkorea.config;

import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class OracleCloudConfig {

    @Bean
    public ObjectStorageClient objectStorageClient() throws IOException {
        ConfigFileAuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider("~/.oci/config", "DEFAULT");
        return ObjectStorageClient.builder().build(provider);
    }
}
