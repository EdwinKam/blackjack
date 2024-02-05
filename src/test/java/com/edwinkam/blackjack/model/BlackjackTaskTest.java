import com.edwinkam.blackjack.repository.BlackjackTaskRepository;
import com.edwinkam.blackjack.model.BlackjackTask;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BlackjackTaskTest {

    @Autowired
    private final BlackjackTaskRepository repository;

    public BlackjackTaskTest(BlackjackTaskRepository repository) {
        this.repository = repository;
    }

    String trackingUuid = UUID.randomUUID().toString();

    @Test
    public void testAdd() {
        BlackjackTask savedTask = repository.save(new BlackjackTask(1, trackingUuid));
        assertNotNull(savedTask);
    }

    @Test
    public void testGet() {
        Optional<BlackjackTask> taskOptional = repository.findByUuid(trackingUuid);
        assertTrue(taskOptional.isPresent());
        BlackjackTask task = taskOptional.get();
        assertNotNull(task);
    }

    @Test
    public void testDelete() {
        repository.deleteByUuid(trackingUuid);
        Optional<BlackjackTask> deletedTaskOptional = repository.findByUuid(trackingUuid);
        assertFalse(deletedTaskOptional.isPresent());
    }
}
