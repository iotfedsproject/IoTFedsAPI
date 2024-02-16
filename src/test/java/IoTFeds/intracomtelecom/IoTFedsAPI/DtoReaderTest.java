//package IoTFeds.intracomtelecom.IoTFedsAPI;
//
//import IoTFeds.intracomtelecom.IoTFedsAPI.models.baas.ProductBaas;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.nio.file.Paths;
//
////package IoTFeds.intracomtelecom.IoTFedsAPI;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
////import org.junit.jupiter.api.MethodOrderer;
////import org.junit.jupiter.api.TestMethodOrder;
////import org.springframework.boot.test.context.SpringBootTest;
////import org.springframework.test.context.ActiveProfiles;
////import org.springframework.test.context.TestPropertySource;
////import org.springframework.test.context.junit4.SpringRunner;
////import org.springframework.test.context.web.WebAppConfiguration;
//
////@ActiveProfiles("test")
////@WebAppConfiguration
//@RunWith(SpringRunner.class)
//@SpringBootTest()
//////@Testcontainers
////@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//////@TestPropertySourceySource("classpath:application-test.yaml")
//public class DtoReaderTest {
//
//    ProductBaas productBaas;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        //    ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule()).registerModule(new JavaTimeModule());
//
//        productBaas = mapper.readValue(Paths.get("src/test/resources/flex-cepa.json").toFile(), ProductBaas.class);
//
//    }
//
//    @Test
//    public void setProductBaas(){
//        System.out.println(productBaas);
//    }
//}
