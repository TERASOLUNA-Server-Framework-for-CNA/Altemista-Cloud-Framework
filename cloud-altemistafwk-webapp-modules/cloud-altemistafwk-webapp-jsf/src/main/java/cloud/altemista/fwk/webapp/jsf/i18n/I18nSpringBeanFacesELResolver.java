
package cloud.altemista.fwk.webapp.jsf.i18n;

/*
 * #%L
 * altemista-cloud presentation layer: JSF support (PrimeFaces)
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.el.ELContext;

import org.springframework.context.MessageSource;
import org.springframework.web.jsf.el.SpringBeanFacesELResolver;


/**
 * This SpringBeanFacesELResolver eases the access to MessageSource beans with the current locale.<br>
 * Being "msg" the MessageSource, this resolver allows expressions such as "msg.my.message.key"
 * that will return to the internationalized message for the key "my.message.key".<br>
 * In other case it delegates to SpringBeanFacesElResolver.<br>
 * Please note that {@code FlowResourceELResolver} tries to do the same thing,
 * but causes an error if part of a key already exists as a key
 * (e.g.: if both "msg.page" and "msg.page.title" exists, using <code>#{msg.page.title}</code> will fail).
 * @author NTT DATA
 */
public class I18nSpringBeanFacesELResolver extends SpringBeanFacesELResolver {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.access.el.SpringBeanELResolver#getValue(javax.el.ELContext, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object getValue(ELContext elContext, Object base, Object property) {

		// If the expression is trying to access a key (or a partial key) of the Message Source...
		if ((base instanceof MessageSource) && (property instanceof String)) {
			
			/*
			 * TODO: For performance reasons, future versions of this class
			 * should keep one unique MessageSourceMap instance that internally caches the accessed messages.
			 * The messages are a finite set so the cached map will not grow indefinitely
			 * (and, also, multiple messages sharing the same key parts won't occupy memory more than once)
			 */
			
			// ...returns a fake Map (MessageSourceMap) that allows accessing the internationalized messages
			Object ret = new MessageSourceMap((MessageSource) base, (String) property);
			elContext.setPropertyResolved(true);
			return ret;
		}

		return super.getValue(elContext, base, property);
	}

}
