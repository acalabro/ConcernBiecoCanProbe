package it.cnr.isti.labsedc.concern.example;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernAnemometerEvent;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;
import it.cnr.isti.labsedc.concern.probe.ConcernAbstractProbe;
import it.cnr.isti.labsedc.concern.utils.ConnectionManager;
import it.cnr.isti.labsedc.concern.utils.DebugMessages;

public class CSVProbe extends ConcernAbstractProbe {

	static CSVProbe theProbe;
	
	
	public CSVProbe(Properties settings) {
		super(settings);
	}
	
	public static void main(String[] args) {
		
		theProbe = new CSVProbe(
				ConnectionManager.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						args[0],
						"system",
						"manager",
						"TopicCF",
						"DROOLS-InstanceOne",
						false,
						"Anemometer",	
						"it.cnr.isti.labsedc.concern,java.lang,javax.security,java.util",
						"vera",
						"griselda")
				);
		
		//sending events
		try {
		
			double latitude = 43.7061873;
			double longitude = 10.4319787;
			
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), CSVProbe.class.getSimpleName(),"Sending anemometerEvent");

			CSVReader reader = new CSVReader(new FileReader(args[1]));
			String[] lineInArray;
			while ((lineInArray = reader.readNext()) != null) {
				sendAnometerEventMessage(theProbe, new ConcernAnemometerEvent<String>(
						Long.parseLong(lineInArray[0]),
						"AnemometerSensor",
						"Monitoring",
						"noSession",
						"noChecksum",
						"windMeasure",
						lineInArray[1],
						CepType.DROOLS,
						false,
						Integer.parseInt(lineInArray[2]), //windangle
						Integer.parseInt(lineInArray[3]), //windstrength
						Integer.parseInt(lineInArray[4]), //gustangle
						Integer.parseInt(lineInArray[5]), //guststrength
						latitude, 
						longitude));
						
				System.out.println(
						"timestamp: " + lineInArray[0] + 
						" localDateTime: " + lineInArray[1] +
						" WindAngle: " + lineInArray[2] +
						" WindStrength: " + lineInArray[3] +
						" GustAngle: " + lineInArray[4] +
						" GustStrength: " + lineInArray[5]);
				
			}
		} catch (IndexOutOfBoundsException | IOException | CsvValidationException e) { 
		
			}
	}
	
	
	@Override
	public void sendMessage(ConcernBaseEvent<?> event, boolean debug) {
		// TODO Auto-generated method stub
		
	}
	
	protected static void sendAnometerEventMessage(CSVProbe theProbe, ConcernAnemometerEvent<String> event) {
		
		DebugMessages.print(
				System.currentTimeMillis(), 
				CSVProbe.class.getSimpleName(),
				"Creating Message ");
		try
		{
			ObjectMessage messageToSend = publishSession.createObjectMessage();
			messageToSend.setJMSMessageID(String.valueOf(MESSAGEID++));
			messageToSend.setObject(event);
			DebugMessages.ok();
			DebugMessages.print(System.currentTimeMillis(), CSVProbe.class.getSimpleName(),"Publishing message  ");
			mProducer.send(messageToSend);
			DebugMessages.ok();
			DebugMessages.line();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
