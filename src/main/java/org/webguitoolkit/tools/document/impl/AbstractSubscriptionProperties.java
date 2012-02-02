package org.webguitoolkit.tools.document.impl;

import org.webguitoolkit.tools.document.IDocumentRepository;
import org.webguitoolkit.tools.document.ISubscriptionProperties;

public abstract class AbstractSubscriptionProperties implements
		ISubscriptionProperties {
	protected IDocumentRepository documentRepository;

	public AbstractSubscriptionProperties(IDocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}
}
