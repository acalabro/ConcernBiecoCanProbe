package it.cnr.isti.labsedc.concern.example;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;
import it.cnr.isti.labsedc.concern.probe.ConcernAbstractProbe;
import it.cnr.isti.labsedc.concern.utils.ConnectionManager;
import it.cnr.isti.labsedc.concern.utils.DebugMessages;

public class iFEVS_Probe extends ConcernAbstractProbe {

	public iFEVS_Probe(Properties settings) {
		super(settings);
	}
	
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		//creating a probe
		if (args.length < 3) {
			System.out.println("USAGE: java -jar iFevs_Probe.jar #monitoringBroker #pathToCPUValueFile #pathToConnectionsValueFile"
					+ "\neg: java -jar iFevs_Probe.jar tcp://localhost:61616 /tmp/cpuvalue.tmp /tmp/connectionvalue.tmp");
			System.exit(0);
		}
		iFEVS_Probe aGenericProbe = new iFEVS_Probe(
				ConnectionManager.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						args[0],"system", "manager",
						"TopicCF","DROOLS-InstanceOne", false, "iFEVS_Probe",	
						"it.cnr.isti.labsedc.concern,java.lang,javax.security,java.util",
						"vera", "griselda"));
		//sending events
		while (true) {
			try {
				DebugMessages.line();
				DebugMessages.println(System.currentTimeMillis(), iFEVS_Probe.class.getSimpleName(),"Sending Primary Client messages");
				
				iFEVS_Probe.sendMessage(aGenericProbe, new ConcernBaseEvent<String>(
						System.currentTimeMillis(),
						"iFevs-PrimaryClient", "Monitoring", "sessionID", "noChecksum",
						"CPU", readCPUValue(args[1]), CepType.DROOLS, false, "CPU_Value")
						);
						
				iFEVS_Probe.sendMessage(aGenericProbe, new ConcernBaseEvent<String>(
						System.currentTimeMillis(),
						"iFevs-PrimaryClient", "Monitoring", "sessionID", "noChecksum",
						"CONNECTIONS", readConnections(args[2]), CepType.DROOLS, false, "Connections_Value")
						);
				Thread.sleep(5000);
	
			} catch (IndexOutOfBoundsException | NamingException e) {} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String readCPUValue(String cpuValuePath) {

		return readFromFile(cpuValuePath);
	}
	
	private static String readConnections(String connectionValuePath) {
		return readFromFile(connectionValuePath);
	}
	
	private static String readFromFile(String filePath) {
		String text="";
		
		try {
		Path fileName = Paths.get(filePath);

	    text = Files.readString(fileName, StandardCharsets.ISO_8859_1);
	    		} catch (IOException e) {
			e.printStackTrace();
		}
	    return text.trim();
	}

	protected static void sendMessage(iFEVS_Probe aGenericProbe, ConcernBaseEvent<String> message) throws JMSException,NamingException {

		DebugMessages.print(
				System.currentTimeMillis(), 
				iFEVS_Probe.class.getSimpleName(),
				"Creating Message ");
		try
		{
			ObjectMessage messageToSend = publishSession.createObjectMessage();
			messageToSend.setJMSMessageID(String.valueOf(MESSAGEID++));
			messageToSend.setObject(message);
			DebugMessages.ok();
			DebugMessages.print(System.currentTimeMillis(), iFEVS_Probe.class.getSimpleName(),"Publishing message  ");
			mProducer.send(messageToSend);
			DebugMessages.ok();
			DebugMessages.line();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
		

	@Override
	public void sendMessage(ConcernBaseEvent<?> event, boolean debug) {
		// TODO Auto-generated method stub
		
	}
}