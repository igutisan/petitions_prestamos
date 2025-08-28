package co.com.pragma.loggeradapter;

import co.com.pragma.model.petition.gateways.LoggerGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggerAdapter implements LoggerGateway {

    @Override
    public void logInfo(String message) {
        log.info(message);
    }

    @Override
    public void logWarn(String message) {
        log.warn(message);
    }

    @Override
    public void logError(String message) {
        log.error(message);
    }
}
