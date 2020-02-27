package cloud.altemista.fwk.it.integration.ws.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@WebService
public interface CustomWebservice {
	
	@WebMethod
	public String testWebserviceMethod(@WebParam( name = "param" ) @XmlElement(required = true) String param);

}
