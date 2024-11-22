package tmrv.dev.blogsystem.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;


@org.springframework.context.annotation.Configuration
public class SocketIOConfig {


    @Bean
    public SocketIOServer socketIOServer() {
        Configuration configuration = new Configuration();
        configuration.setHostname("localhost");
        configuration.setPort(9090);
        configuration.setAllowCustomRequests(true);
        configuration.setUpgradeTimeout(10000);
        configuration.setPingTimeout(60000);
        configuration.setPingInterval(25000);
        return new SocketIOServer(configuration);
    }
}
