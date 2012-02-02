package org.webguitoolkit.tools.document;

public interface ISubscription {
	public String getName();
	public String getId();
	public String getFrequency();
	public String getEvents();
	public String getRecursion();
	public String[] getRecipients();
	public String getNotificationText();
	public String getOwner();
	public String[] getSubscribedResources();
}
