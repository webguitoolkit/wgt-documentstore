package org.webguitoolkit.tools.document.impl;

import org.webguitoolkit.tools.document.ISubscription;

public abstract class AbstractSubscription implements ISubscription {
	protected String name;
	protected String id;
	protected String frequency;
	protected String events;
	protected String recursion;
	protected String[] recipients;
	protected String notificationText;
	protected String owner;
	protected String[] subscribedResources;
	
	public AbstractSubscription(String name, String id, String frequency, String events, String recursion, String[] recipients, String notificationText, String owner, String[] subscribedResources) {
		this.name = name;
		this.id = id;
		this.frequency = frequency;
		this.recursion = recursion;
		this.events = events;
		this.recipients = recipients;
		this.notificationText = notificationText;
		this.owner = owner;
		this.subscribedResources = subscribedResources;
	}
}