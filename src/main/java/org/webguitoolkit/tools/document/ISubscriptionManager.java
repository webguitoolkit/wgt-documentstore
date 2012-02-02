package org.webguitoolkit.tools.document;

import java.util.List;
import java.util.Locale;

import org.webguitoolkit.tools.document.impl.DocumentRepositoryException;

public interface ISubscriptionManager {
	public void subscribe(IResource[] resources, String[] recipients, String subscriptionName,
			String subscriptionFrequency, String subscriptionEvent, String subscriptionRecursion, String notificationText)
			throws DocumentRepositoryException;

	public List<ISubscription> getSubscriptions() throws DocumentRepositoryException;

	public void modifySubscription(ISubscription subscription, String[] recipients, String subscriptionName,
			String subscriptionFrequency, String subscriptionEvent, String subscriptionRecursion, String notificationText)
			throws DocumentRepositoryException;

	public void deleteSubscription(String subscriptionId) throws DocumentRepositoryException;

	public ISubscriptionProperties getSubscriptionProperties(Locale locale) throws DocumentRepositoryException;
}