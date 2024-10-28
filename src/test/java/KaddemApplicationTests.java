import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.KaddemApplication;

@SpringBootTest(classes = KaddemApplication.class)  // Specify your main application class
public class KaddemApplicationTests {

    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
    }
}