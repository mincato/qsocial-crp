package com.qsocialnow.elasticsearch.services.cases;

import java.util.ArrayList;
import java.util.List;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.queues.Consumer;

public class CaseConsumer extends Consumer<Case> {
	
	private List<Case> bulkDocuments = new ArrayList<Case>();
	 
	private CaseService service = new CaseService();
	
	@Override
	public void addDocument(Case caseDoc) {
		bulkDocuments.add(caseDoc);
	}

	@Override
	public void saveDocuments() {
		service.indexBulkCases(bulkDocuments);
		bulkDocuments.clear();
	}
	
}
