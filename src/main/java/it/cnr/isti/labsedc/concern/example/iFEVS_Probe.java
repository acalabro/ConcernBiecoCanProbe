package it.cnr.isti.labsedc.concern.example;

import java.net.UnknownHostException;
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
		iFEVS_Probe aGenericProbe = new iFEVS_Probe(
				ConnectionManager.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						"tcp://146.48.81.167:61616","system", "manager",
						"TopicCF","DROOLS-InstanceOne", false, "iFEVS_Probe",	
						"it.cnr.isti.labsedc.concern,java.lang,javax.security,java.util",
						"vera", "griselda"));
		//sending events
		try {
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), iFEVS_Probe.class.getSimpleName(),"Sending ICTGateway messages");
			
			iFEVS_Probe.sendErrorMessage(aGenericProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"iFevs-ErrorMessage", "Monitoring", "sessionID", "noChecksum",
					"ERROR_NAME", "ERROR_PAYLOAD", CepType.DROOLS, false, "ERROR_CLASS")
					);
						
		} catch (IndexOutOfBoundsException | NamingException e) {} catch (JMSException e) {
			e.printStackTrace();
		} 
	}
	
	
	protected static void sendErrorMessage(iFEVS_Probe aGenericProbe, ConcernBaseEvent<String> message) throws JMSException,NamingException {

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