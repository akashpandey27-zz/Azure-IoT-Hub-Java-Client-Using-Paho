package packagemain;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientAzure {

	public static void main(String[] args) {
		RunnableDemo R1;
		try {
			R1 = new RunnableDemo("Thread-1");
			R1.start();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class RunnableDemo implements Runnable {
	String topic = "devices/[device-id]/messages/events/";
	String content = "Message from MqttPublishSample";
	int qos = 0;
	String broker = "ssl://[IoT-hub-name].azure-devices.net:8883";
	String clientId = "Remote-MITCSY";

	MemoryPersistence persistence = new MemoryPersistence();
	private Thread t;
	private final String threadName;
	MqttClient sampleClient = null;

	RunnableDemo(final String name) throws MqttException {
		threadName = name;
		// System.out.println("Creating " + threadName );
		sampleClient = new MqttClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName("[IoT-hub-name].azure-devices.net/[device-id]");
		
		/**
		 * Shared Access Signature of you device 
		 * will look like 
		 * SharedAccessSignature=SharedAccessSignature sr=....................................................................&se=1517341345

		 */
		connOpts.setPassword("[Shared-Access-Signature]".toCharArray()); 

		// System.out.println("Connecting to broker: "+broker);
		sampleClient.connect(connOpts);
	}

	public void run() {
		System.out.println("Running " + threadName);
		try {
			int i = 0;
			while (true) {
				try {

					// System.out.println("Connected");
					// System.out.println("Publishing message: "+content);
					MqttMessage message = new MqttMessage(content.getBytes());
					message.setQos(qos);
					sampleClient.publish(topic, message);
					System.out.println("Message published");
					// sampleClient.disconnect();
					// System.out.println("Disconnected");

					// System.exit(0);
				} catch (MqttException me) {
					
					 System.out.println("reason "+me.getReasonCode());
					 System.out.println("msg "+me.getMessage());
					 System.out.println("loc "+me.getLocalizedMessage());
					 System.out.println("cause "+me.getCause()); System.out.println("excep "+me);
					 
					me.printStackTrace();
				}
				System.out.println("Thread: " + threadName + ", " + i++);
				// Let the thread sleep for a while.
				Thread.sleep(500);
			}
		} catch (final InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}
}
