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

public class ErrorGenerationSimProbe extends ConcernAbstractProbe {

	public ErrorGenerationSimProbe(Properties settings) {
		super(settings);
	}
	
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		//creating a probe
		ErrorGenerationSimProbe aGenericProbe = new ErrorGenerationSimProbe(
				ConnectionManager.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						"tcp://146.48.81.16761616","system", "manager",
						"TopicCF","DROOLS-InstanceOne", false, "ErrorGenerationSimProbe",	
						"it.cnr.isti.labsedc.concern,java.lang,javax.security,java.util",
						"vera", "griselda"));
		//sending events
		try {
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), ErrorGenerationSimProbe.class.getSimpleName(),"Sending ICTGateway messages");
			
			ErrorGenerationSimProbe.sendErrorMessage(aGenericProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"ErrorGenerationSimProbe", "Monitoring", "sessionID", "noChecksum",
					"ERROR_NAME", "ERROR_PAYLOAD", CepType.DROOLS, false, "ERROR_CLASS")
					);
						
		} catch (IndexOutOfBoundsException | NamingException e) {} catch (JMSException e) {
			e.printStackTrace();
		} 
	}
	
	
	protected static void sendErrorMessage(ErrorGenerationSimProbe aGenericProbe, ConcernBaseEvent<String> message) throws JMSException,NamingException {

		DebugMessages.print(
				System.currentTimeMillis(), 
				ErrorGenerationSimProbe.class.getSimpleName(),
				"Creating Message ");
		try
		{
			ObjectMessage messageToSend = publishSession.createObjectMessage();
			messageToSend.setJMSMessageID(String.valueOf(MESSAGEID++));
			messageToSend.setObject(message);
			DebugMessages.ok();
			DebugMessages.print(System.currentTimeMillis(), ErrorGenerationSimProbe.class.getSimpleName(),"Publishing message  ");
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