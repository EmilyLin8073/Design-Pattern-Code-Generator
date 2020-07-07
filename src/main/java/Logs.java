import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Logs {
    default Logger logger(){
        return LoggerFactory.getLogger(MyToolWindowFactory.class);
    }
}
