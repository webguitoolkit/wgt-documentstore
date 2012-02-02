package org.webguitoolkit.tools.document;

import java.util.List;

public interface ISubscriptionProperties {
	List<String[]> getSubscriptionEventConditions() throws Exception;
	List<String[]> getSubscriptionFrequencyConditions() throws Exception;
	List<String[]> getSubscriptionRecursionConditions() throws Exception;
}