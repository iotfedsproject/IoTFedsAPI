package IoTFeds.intracomtelecom.IoTFedsAPI.Utilities.Login;

import IoTFeds.intracomtelecom.IoTFedsAPI.Models.PlatformCredentials;
import eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory;
import eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory.Type;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory.HomePlatformCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import static eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory.getFactory;

@Configuration
public class SymbIoTeLogin {
    @Value("${configuration.keystorePath}")
    private String keystorePath;
    @Value("${configuration.keystorePassword}")
    private String keystorePassword;
    @Value("${configuration.coreAAMAddress}")
    private String coreAAMAddress;

    Logger logger = LoggerFactory.getLogger(SymbIoTeLogin.class);
    public AbstractSymbIoTeClientFactory GetSymbIoTeFactory(PlatformCredentials credentials) throws SecurityHandlerException , NoSuchAlgorithmException{
        Type type = Type.FEIGN;
        // Get the configuration
        AbstractSymbIoTeClientFactory.Config config = new AbstractSymbIoTeClientFactory.Config(coreAAMAddress, keystorePath, keystorePassword, type);

        AbstractSymbIoTeClientFactory factory;
        File ksFile = new File(keystorePath);
        if (ksFile.exists()) ksFile.delete();
        factory = getFactory(config);

        // OPTIONAL section... needs to be run only once
        // - per new platform
        // and/or after revoking client certificate in an already initialized platform


        // ATTENTION: This MUST be an interactive procedure to avoid persisting credentials (password)
        // Here, you can add credentials FOR MORE THAN 1 platforms
        Set<HomePlatformCredentials> platformCredentials = new HashSet<>();

        HomePlatformCredentials exampleHomePlatformCredentials = new HomePlatformCredentials(
                credentials.getLocalPlatformId(),
                credentials.getUsername(),
                credentials.getPassword(),
                credentials.getClientId());
        platformCredentials.add(exampleHomePlatformCredentials);


        // Get Certificates for the specified platforms
        factory.initializeInHomePlatforms(platformCredentials);
        return factory;
    }

    public AbstractSymbIoTeClientFactory GetSymbIoTeCoreFactory() throws SecurityHandlerException , NoSuchAlgorithmException{
        Type type = Type.FEIGN;
        // Get the configuration
        AbstractSymbIoTeClientFactory.Config config = new AbstractSymbIoTeClientFactory.Config(coreAAMAddress, keystorePath, keystorePassword, type);

        AbstractSymbIoTeClientFactory factory;
        File ksFile = new File(keystorePath);
        if (ksFile.exists()) ksFile.delete();
        factory = getFactory(config);
        return factory;
    }
}
