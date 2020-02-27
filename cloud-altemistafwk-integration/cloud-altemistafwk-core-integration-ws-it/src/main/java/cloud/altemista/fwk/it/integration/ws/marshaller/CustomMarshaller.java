package cloud.altemista.fwk.it.integration.ws.marshaller;

import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.xml.transform.StringSource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CustomMarshaller  implements Marshaller, Unmarshaller {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void marshal(Object graph, Result result) throws IOException, XmlMappingException {

		final String param = (String) graph;

		final String xmlString = 
				"<soap:testWebserviceMethod xmlns:soap=\"http://soap.ws.integration.it.cloud.altemista.org/\">"
						+ "<param>"+param+"</param>"
						+ "</soap:testWebserviceMethod>";

		try {
			TransformerFactory.newInstance().newTransformer().transform(new StringSource(xmlString), result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String unmarshal(Source source) throws IOException, XmlMappingException {

		final NodeList nodeList = ((DOMSource)source).getNode().getChildNodes();

		String result = null;
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node returnNode = nodeList.item(i);
			if (returnNode!=null && returnNode.getNodeName().equals("return")){
				result = returnNode.getTextContent();
			}
		}
		
		logger.info("Unmarshall result: "+result);
		
		return result;
	}

}
