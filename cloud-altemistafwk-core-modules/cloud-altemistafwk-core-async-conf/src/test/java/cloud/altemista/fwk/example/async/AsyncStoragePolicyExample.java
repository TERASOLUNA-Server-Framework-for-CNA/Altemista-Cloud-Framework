package cloud.altemista.fwk.example.async;

/*
 * #%L
 * altemista-cloud asynchronous and scheduled executions CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cloud.altemista.fwk.core.async.AsyncStoragePolicy;
import cloud.altemista.fwk.test.async.service.AsyncService;

// tag::usage[]
@Service
public class AsyncStoragePolicyExample {
  
  @Autowired
  private AsyncService asyncService;
  
  @Autowired
  private AsyncStoragePolicy<Boolean> storagePolicy; // <1>
  
  public String startLongProcessingTask() {
    
    Future<Boolean> future = this.asyncService.longProcessingTask();
    String identifier = this.storagePolicy.put(future); // <2>
    return identifier; // <3>
  }
  
  public boolean checkStatusOfLongProcessingTask(String identifier) { // <3>
    
    if (!this.storagePolicy.containsId(identifier)) { // <4>
      return false;
    }
    Future<Boolean> future = this.storagePolicy.get(identifier); // <4>
    return future.isDone(); // <4>
  }
}
// end::usage[]
