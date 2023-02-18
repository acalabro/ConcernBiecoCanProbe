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

public class SevenBullsErrorSimProbe extends ConcernAbstractProbe {

	static SevenBullsErrorSimProbe errorProbe;
	
	public SevenBullsErrorSimProbe(Properties settings) {
		super(settings);
	}
	
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		//creating a probe
		errorProbe = new SevenBullsErrorSimProbe(
				ConnectionManager.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						args[0],"system", "manager",
						"TopicCF","DROOLS-InstanceOne", false, "SevenBullsErrorSimProbe",	
						"it.cnr.isti.labsedc.concern,java.lang,javax.security,java.util",
						"vera", "griselda"));
		//sending events
		try {
			
			simMessage();
			
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsErrorSimProbe.class.getSimpleName(),"Sending ERROR MESSAGE ONE");
			
			SevenBullsErrorSimProbe.sendErrorMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsErrorSimProbe", "Monitoring", "sessionID", "noChecksum",
					"ERROR_NAME", "ERROR_PAYLOAD", CepType.DROOLS, false, "ERROR_CLASS")
					);
						
			simMessage();
						
			injectingFailure(null);
			
		} catch (IndexOutOfBoundsException | NamingException e) {} catch (JMSException e) {
			e.printStackTrace();
		} 
	}
	
	
	private static void sendErrorMessage(SevenBullsErrorSimProbe aGenericProbe, ConcernBaseEvent<String> message) throws JMSException,NamingException {

		DebugMessages.print(
				System.currentTimeMillis(), 
				SevenBullsErrorSimProbe.class.getSimpleName(),
				"Creating Message ");
		try
		{
			ObjectMessage messageToSend = publishSession.createObjectMessage();
			messageToSend.setJMSMessageID(String.valueOf(MESSAGEID++));
			messageToSend.setObject(message);
			DebugMessages.ok();
			DebugMessages.print(System.currentTimeMillis(), SevenBullsErrorSimProbe.class.getSimpleName(),"Publishing message  ");
			mProducer.send(messageToSend);
			DebugMessages.ok();
			DebugMessages.line();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
		
	private static void simMessage() throws InterruptedException {

		for (int i = 0; i< 4; i++) {
			Thread.sleep(1300);
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsErrorSimProbe.class.getSimpleName(),"A non relevant method is executed");
			Thread.sleep(500);
			DebugMessages.println(System.currentTimeMillis(), SevenBullsErrorSimProbe.class.getSimpleName(),"Generic operations");			
		}
		
	}
	
	private static void injectingFailure(SevenBullsErrorSimProbe aGenericProbe) {

		try {
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsErrorSimProbe.class.getSimpleName(),"INJECTING DEVIATION IN 3 SECONDS");
			DebugMessages.line();
			Thread.sleep(1000);
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsErrorSimProbe.class.getSimpleName(),"INJECTING DEVIATION IN 2 SECONDS");
			DebugMessages.line();
			Thread.sleep(1000);
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsErrorSimProbe.class.getSimpleName(),"INJECTING DEVIATION IN 1 SECONDS");
			Thread.sleep(1000);
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsErrorSimProbe.class.getSimpleName(),"Injecting failure");
			DebugMessages.line();

			SevenBullsErrorSimProbe.sendErrorMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsErrorSimProbe", "Monitoring", "sessionID", "noChecksum",
					"ERROR_NAME", "ERROR_PAYLOAD", CepType.DROOLS, false, "ERROR_CLASS")
					);
			
			SevenBullsErrorSimProbe.sendErrorMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsErrorSimProbe", "Monitoring", "sessionID", "noChecksum",
					"ERROR_NAME", "ERROR_PAYLOAD", CepType.DROOLS, false, "ERROR_CLASS")
					);
			
		} catch (JMSException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	
	@Override
	public void sendMessage(ConcernBaseEvent<?> event, boolean debug) {
		// TODO Auto-generated method stub
		
	}
}