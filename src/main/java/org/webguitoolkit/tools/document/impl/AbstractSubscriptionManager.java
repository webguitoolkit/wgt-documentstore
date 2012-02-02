package org.webguitoolkit.tools.document.impl;

import org.webguitoolkit.tools.document.IDocumentRepository;
import org.webguitoolkit.tools.document.ISubscriptionManager;

public abstract class AbstractSubscriptionManager implements
		ISubscriptionManager {
	protected IDocumentRepository documentRepository;
	
	public AbstractSubscriptionManager(IDocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}
}
