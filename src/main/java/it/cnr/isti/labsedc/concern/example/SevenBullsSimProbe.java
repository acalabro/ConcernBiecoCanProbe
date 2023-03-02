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

public class SevenBullsSimProbe extends ConcernAbstractProbe {

	static SevenBullsSimProbe errorProbe;
	
	public SevenBullsSimProbe(Properties settings) {
		super(settings);
	}
	
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		//creating a probe
		errorProbe = new SevenBullsSimProbe(
				ConnectionManager.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						args[0],"system", "manager",
						"TopicCF","DROOLS-InstanceOne", false, "SevenBullsErrorSimProbe",	
						"it.cnr.isti.labsedc.concern,java.lang,javax.security,java.util",
						"vera", "griselda"));
		//sending events
		try {
					
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"Testing Rule 0\nSending ERROR MESSAGE ONE");
			
			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"ERROR_NAME", "ERROR_PAYLOAD", CepType.DROOLS, false, "ERROR_CLASS")
					);
												
			injectionTime();
			
			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"ERROR_NAME", "ERROR_PAYLOAD", CepType.DROOLS, false, "ERROR_CLASS")
					);
			
			testSeparator();

			
			
			
			
			
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"Testing Rule 1\nSending 101 Transaction messages");
			
			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"TransactionStart", "Transaction_Payload", CepType.DROOLS, false, "TransactionStart")
					);
			
			injectionTime();
			
			for (int i = 0; i<100;i++) {
				SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
						System.currentTimeMillis(),
						"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
						"TransactionStart", "Transaction_Payload", CepType.DROOLS, false, "TransactionStart")
						);
			}
			
			
			testSeparator();

			
			
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"Testing Rule 2\nSending 10 quotation per minute");
			
			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"QuotationFromBroker", "QuotationFromBroker_Payload", CepType.DROOLS, false, "QuotationFromBroker")
					);
			
			injectionTime();			
			
			for (int i = 0; i<10;i++) {
				SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
						System.currentTimeMillis(),
						"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
						"QuotationFromBroker", "QuotationFromBroker_Payload", CepType.DROOLS, false, "QuotationFromBroker")
						);
			}

			
			
			testSeparator();
			
			
			
			
			
			
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"Testing Rule 3\nViolating training queue threshold");		

			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"Training_Queue_Size", "40" , CepType.DROOLS, false, "Training_Queue_Size")
					);
			

			injectionTime();
			
			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"Training_Queue_Size", "120" , CepType.DROOLS, false, "Training_Queue_Size")
					);

			
			
			
			
			
			
			testSeparator();
			
			
			
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"Testing Rule 4\nViolating optimization request queue threshold");		

			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"Optimization_Queue_Size", "20" , CepType.DROOLS, false, "Optimization_Queue_Size")
					);
			

			injectionTime();
			
			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"Optimization_Queue_Size", "51" , CepType.DROOLS, false, "Optimization_Queue_Size")
					);

			
			
			

			testSeparator();
			
			
			
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"Testing Rule 5\nViolating optimization request queue threshold");		

			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"Errors_in_DLQ", "800" , CepType.DROOLS, false, "Errors_in_DLQ")
					);
			

			injectionTime();
			
			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"Errors_in_DLQ", "1001" , CepType.DROOLS, false, "Errors_in_DLQ")
					);	
			
		} catch (IndexOutOfBoundsException | NamingException e) {} catch (JMSException e) {
			e.printStackTrace();
		} 
	}
	
	
	private static void testSeparator() {
		DebugMessages.line();
		DebugMessages.line();
		DebugMessages.line();
		DebugMessages.line();
		try {
			Thread.sleep(2000);
		} catch(InterruptedException asd) {
			
		}
	}

	private static void sendMessage(SevenBullsSimProbe aGenericProbe, ConcernBaseEvent<?> message) throws JMSException,NamingException {

		DebugMessages.print(
				System.currentTimeMillis(), 
				SevenBullsSimProbe.class.getSimpleName(),
				"Creating Message ");
		try
		{
			ObjectMessage messageToSend = publishSession.createObjectMessage();
			messageToSend.setJMSMessageID(String.valueOf(MESSAGEID++));
			messageToSend.setObject(message);
			DebugMessages.ok();
			DebugMessages.print(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"Publishing message  ");
			mProducer.send(messageToSend);
			DebugMessages.ok();
			DebugMessages.line();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
		
	
	private static void injectingFailure() {

		try {

			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"ERROR_NAME", "ERROR_PAYLOAD", CepType.DROOLS, false, "ERROR_CLASS")
					);
			
			SevenBullsSimProbe.sendMessage(errorProbe, new ConcernBaseEvent<String>(
					System.currentTimeMillis(),
					"SevenBullsSimProbe", "Monitoring", "sessionID", "noChecksum",
					"ERROR_NAME", "ERROR_PAYLOAD", CepType.DROOLS, false, "ERROR_CLASS")
					);
			
		} catch (JMSException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void injectionTime() {
		try {
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"INJECTING DEVIATION IN 3 SECONDS");
			DebugMessages.line();
			Thread.sleep(1000);
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"INJECTING DEVIATION IN 2 SECONDS");
			DebugMessages.line();
			Thread.sleep(1000);
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"INJECTING DEVIATION IN 1 SECONDS");
			Thread.sleep(1000);
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SevenBullsSimProbe.class.getSimpleName(),"Injecting failure");
			DebugMessages.line();
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