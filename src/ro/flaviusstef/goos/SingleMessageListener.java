package ro.flaviusstef.goos;

import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class SingleMessageListener implements MessageListener {

	private ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

	public void processMessage(Chat chat, Message message) {
		messages.add(message);
	}
	
	public void receivesAMessage() throws InterruptedException {
		assertThat("Message", messages.poll(3, SECONDS), is(notNullValue()));
	}

}
