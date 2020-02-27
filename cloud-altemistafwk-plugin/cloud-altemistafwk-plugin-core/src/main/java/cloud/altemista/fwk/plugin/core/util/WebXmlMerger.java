package cloud.altemista.fwk.plugin.core.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import cloud.altemista.fwk.plugin.core.exception.PluginException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.XmlStreamReader;
import org.apache.commons.io.output.XmlStreamWriter;

/**
 * Utilit class to write the contents of a {@code web.xml} file
 * read from a Web Application Deployment Descriptor file (web.xml)
 * and a Servlet 3.0 web fragment (web-fragment.xml) as if they were merged.
 * <br>
 * Please note this implementation does not properly handle
 * neither the {@code <absolute-ordering>} nor the {@code <ordering>} tags 
 * @author NTT DATA
 */
public class WebXmlMerger {
	
	/**
	 * Writes the contents of a {@code web.xml} file read from a Web Application Deployment Descriptor file (web.xml)
	 * and a Servlet 3.0 web fragment (web-fragment.xml) as if they were merged.
	 * @param webXmlFile The web.xml file
	 * @param webFragmentXmlFile The web-fragment.xml file
	 * @param outputStream OutputStream to write the merged web.xml file
	 * @throws IOException if there is a problem reading one of the files
	 * @throws XMLStreamException if an unexpected processing error arises
	 */
	public static void merge(final File webXmlFile, final File webFragmentXmlFile, final OutputStream outputStream)
			throws IOException, XMLStreamException {
		
		XmlStreamWriter outputXmlStream = null;
		XMLEventWriter outputXmlWriter = null;
		MergedReader reader = null;
		try {

			// Initializes the XML writer
			outputXmlStream = new XmlStreamWriter(outputStream);
			outputXmlWriter = XMLOutputFactory.newFactory().createXMLEventWriter(outputXmlStream);
			
			// Reads from the web.xml and the web-fragment.xml as if they were merged
			// and writes in the outputStream
			for (reader = new MergedReader(webXmlFile, webFragmentXmlFile); reader.hasNext(); ) {
				outputXmlWriter.add(reader.next());
			}
			
		} finally {
			IOUtils.closeQuietly(reader);
			closeQuietly(outputXmlWriter);
			IOUtils.closeQuietly(outputXmlStream);
		}
	}
	
	/**
	 * Convenience method
	 * @param writer XMLEventWriter to close
	 */
	private static void closeQuietly(XMLEventWriter writer) {
		
		if (writer != null) {
			try {
				writer.close();
			} catch (XMLStreamException ignored) {
				// (ignored)
			}
		}
	}
	
	/**
	 * Private default constructor (non-instanceable class)
	 */
	private WebXmlMerger() {
		super();
	}
	
	/**
	 * Helper class to actually read a Web Application Deployment Descriptor file (web.xml)
	 * and a Servlet 3.0 web fragment (web-fragment.xml) as if they were merged.
	 * @author NTT DATA
	 */
	private static class MergedReader implements Iterator<XMLEvent>, Closeable {
		
		/** Factory for XML parser elements */
		private final XMLInputFactory xmlInputFactory;
		
		/** Character stream to read the web.xml file */
		private XmlStreamReader webXmlStream = null;
		
		/** XML parser to read the web.xml file */
		private XMLEventReader webXmlReader = null;
		
		/** Character stream to read the web-fragment.xml file */
		private XmlStreamReader webFragmentXmlStream = null;
		
		/** XML parser to read the web-fragment.xml file */
		private XMLEventReader webFragmentXmlReader = null;
	
		/**
		 * Constructor
		 * @param webXmlFile The web.xml file
		 * @param webFragmentXmlFile The web-fragment.xml file
		 * @throws IOException if there is a problem reading one of the files
		 * @throws XMLStreamException if an unexpected processing error arises
		 */
		private MergedReader(final File webXmlFile, final File webFragmentXmlFile) throws IOException, XMLStreamException {
			super();
			
			this.xmlInputFactory = XMLInputFactory.newFactory();
			this.initWebXmlReader(webXmlFile);
			this.initWebFragmentXmlReader(webFragmentXmlFile);
		}
	
		/**
		 * Initializes the character stream and the XML parser for the web.xml file
		 * @param webXmlFile The web.xml file
		 * @throws IOException if there is a problem reading the file
		 * @throws XMLStreamException if an unexpected processing error arises
		 */
		private void initWebXmlReader(final File webXmlFile) throws IOException, XMLStreamException {
			
			// Initializes the XML parser at the beginning of the web.xml file
			this.webXmlStream = new XmlStreamReader(webXmlFile);
			this.webXmlReader = xmlInputFactory.createXMLEventReader(this.webXmlStream);
		}
	
		/**
		 * Initializes the character stream and the XML parser for the web-fragment.xml file
		 * @param webFragmentXmlFile The web-fragment.xml file
		 * @throws IOException if there is a problem reading the file
		 * @throws XMLStreamException if an unexpected processing error arises
		 */
		private void initWebFragmentXmlReader(final File webFragmentXmlFile) throws IOException, XMLStreamException {
			
			// Initializes the XML parser at the start of the web-fragment tag contents
			this.webFragmentXmlStream = new XmlStreamReader(webFragmentXmlFile);
			for (this.webFragmentXmlReader = xmlInputFactory.createXMLEventReader(this.webFragmentXmlStream);
					this.webFragmentXmlReader.hasNext(); ) {
				
				// Reads up to the web-fragment start tag
				final XMLEvent webFragmentEvent = this.webFragmentXmlReader.nextEvent();
				if (webFragmentEvent.isStartElement()
						&& webFragmentEvent.asStartElement().getName().getLocalPart().equals("web-fragment")) {
					break;
				}
			}
		}
	
		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			
			// This merger has more elements while the web.xml file is not completely read
			return this.webXmlReader.hasNext();
		}
	
		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public XMLEvent next() {
			
			try {
				// If the next element to be read from the web.xml is the web-app end tag...
				XMLEvent nextWebEvent = this.webXmlReader.peek();
				if (nextWebEvent.isEndElement()
						&& nextWebEvent.asEndElement().getName().getLocalPart().equals("web-app")
						&& this.webFragmentHasNext()) {
					
					// ...then returns the next element found in the web-fragment.xml file
					return this.webFragmentNext();
				}
				
				// Otherwise, returns the element read from the web.xml file
				return this.webXmlReader.nextEvent();
				
			} catch (XMLStreamException e) {
				throw new PluginException(e,  "",  "",  "");
			}
		}
		
		/**
		 * Checks if there are more events to be read from the web-fragment.xml file
		 * @return if there are more events to be read
		 * @throws XMLStreamException if an unexpected processing error arises
		 */
		private boolean webFragmentHasNext() throws XMLStreamException {
			
			// (sanity check)
			if (!this.webFragmentXmlReader.hasNext()) {
				return false;
			}
			
			// Checks if the next event is the web-fragment end tag
			final XMLEvent nextWebFragmentEvent = this.webFragmentXmlReader.peek();
			final boolean isWebFragmentEndElement = nextWebFragmentEvent.isEndElement()
					&& nextWebFragmentEvent.asEndElement().getName().getLocalPart().equals("web-fragment");
			return !isWebFragmentEndElement;
		}
	
		/**
		 * Reads the next event from the web-fragment.xml file
		 * @return the next event from the web-fragment.xml file
		 * @throws XMLStreamException if an unexpected processing error arises
		 */
		private XMLEvent webFragmentNext() throws XMLStreamException {
			
			return this.webFragmentXmlReader.nextEvent();
		}
	
		/* (non-Javadoc)
		 * @see java.io.Closeable#close()
		 */
		@Override
		public void close() throws IOException {
			
			closeQuietly(this.webXmlReader);
			IOUtils.closeQuietly(this.webXmlStream);
			
			closeQuietly(this.webFragmentXmlReader);
			IOUtils.closeQuietly(this.webFragmentXmlStream);
		}
		
		/**
		 * Convenience method
		 * @param reader XMLEventReader to close
		 */
		private static void closeQuietly(XMLEventReader reader) {
			
			if (reader != null) {
				try {
					reader.close();
				} catch (XMLStreamException ignored) {
					// (ignored)
				}
			}
		}
	}
}
