package it.cnr.isti.labsedc.concern.example;

import java.net.UnknownHostException;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;
import it.cnr.isti.labsedc.concern.event.ConcernICTGatewayEvent;
import it.cnr.isti.labsedc.concern.probe.ConcernAbstractProbe;
import it.cnr.isti.labsedc.concern.utils.ConnectionManager;
import it.cnr.isti.labsedc.concern.utils.DebugMessages;

public class ICTGatewayProbe extends ConcernAbstractProbe {

	public ICTGatewayProbe(Properties settings) {
		super(settings);
	}
	
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		//creating a probe
		ICTGatewayProbe aGenericProbe = new ICTGatewayProbe(
				ConnectionManager.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						"tcp://127.0.0.1:61616","system", "manager",
						"TopicCF","DROOLS-InstanceOne", false, "SUA_probe",	
						"it.cnr.isti.labsedc.concern,java.lang,javax.security,java.util",
						"vera", "griselda"));
		//sending events
		try {
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), ICTGatewayProbe.class.getSimpleName(),"Sending ICTGateway messages");
			
			ICTGatewayProbe.sendICTMessage(aGenericProbe, new ConcernICTGatewayEvent<String>(
					System.currentTimeMillis(),
					"ICTGW_Probe", "Monitoring", "sessionID", "noChecksum",
					"AUTHENTICATION_REQUEST", "ICTMessagePayload0", CepType.DROOLS, false, 
					"AUTHENTICATION_REQUEST", "AUTHENTICATION")
			);
		
			Thread.sleep(1000);
					
			ICTGatewayProbe.sendICTMessage(aGenericProbe, new ConcernICTGatewayEvent<String>(
					System.currentTimeMillis(),
					"ICTGW_Probe", "Monitoring", "sessionID", "noChecksum",
					"REGISTRATION_RESPONSE", "ICTMessagePayload1", CepType.DROOLS, false, 
					"REGISTRATION_RESPONSE", "REGISTRATION")
			);
			
			Thread.sleep(1000);
			
			ICTGatewayProbe.sendICTMessage(aGenericProbe, new ConcernICTGatewayEvent<String>(
					System.currentTimeMillis(),
					"ICTGW_Probe", "Monitoring", "sessionID", "noChecksum",
					"TOPOLOGY_ELEMENTS_RESPONSE", "ICTMessagePayload2", CepType.DROOLS, false, 
					"TOPOLOGY_ELEMENTS_RESPONSE", "DATA")
			);
			
			Thread.sleep(1000);
			
			ICTGatewayProbe.sendICTMessage(aGenericProbe, new ConcernICTGatewayEvent<String>(
					System.currentTimeMillis(),
					"ICTGW_Probe", "Monitoring", "sessionID", "noChecksum",
					"TOPOLOGY_ELEMENT_DETAILS_RESPONSE", "ICTMessagePayload3", CepType.DROOLS, false, 
					"TOPOLOGY_ELEMENT_DETAILS_RESPONSE", "DATA")
			);
						
		} catch (IndexOutOfBoundsException | NamingException e) {} catch (JMSException e) {
			e.printStackTrace();
		} 
	}
	
	
	protected static void sendICTMessage(ICTGatewayProbe aGenericProbe, ConcernICTGatewayEvent<String> message) throws JMSException,NamingException {

		DebugMessages.print(
				System.currentTimeMillis(), 
				DTProbe.class.getSimpleName(),
				"Creating Message ");
		try
		{
			ObjectMessage messageToSend = publishSession.createObjectMessage();
			messageToSend.setJMSMessageID(String.valueOf(MESSAGEID++));
			messageToSend.setObject(message);
			DebugMessages.ok();
			DebugMessages.print(System.currentTimeMillis(), DTProbe.class.getSimpleName(),"Publishing message  ");
			mProducer.send(messageToSend);
			DebugMessages.ok();
			DebugMessages.line();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
		
	
//	protected static void sendRegistrationICTMessage();
//	protected static void sendDataICTMessage();
//	


	@Override
	public void sendMessage(ConcernBaseEvent<?> event, boolean debug) {
		// TODO Auto-generated method stub
		
	}
}