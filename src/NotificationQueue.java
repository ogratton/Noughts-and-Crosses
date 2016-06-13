


// We use https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A queue of notifications used by both clients and server
 * @author Oliver
 *
 */
public class NotificationQueue {

	// We choose the LinkedBlockingQueue implementation of BlockingQueue:
	private BlockingQueue<Notification> queue = new LinkedBlockingQueue<Notification>();

	/**
	 * Inserts the specified notification into the queue
	 * @param m
	 */
	public void offer(Notification m) {
		queue.offer(m);
	}

	/**
	 * Retrieves and removes the head of this queue, waiting if
	 * necessary until an element becomes available.
	 * @return
	 */
	public Notification take() {

		while (true) {
			try {
				return (queue.take());
			} catch (InterruptedException e) {
				// This can in principle be triggered by queue.take().
				// But this would only happen if we had interrupt() in our code,
				// which we don't.
				// In any case, if it could happen, we should do nothing here
				// and try again until we succeed without interruption.
			}

		}
	}
}
