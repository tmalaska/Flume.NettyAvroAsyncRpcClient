package com.cloudera.sa.flume.async.rpc;

import java.util.List;

import org.apache.flume.Event;

public class AppendBatchAsyncResultPojo {
  public boolean isSuccessful;
  public List<Event> events;
  
  public AppendBatchAsyncResultPojo(boolean isSuccessful, List<Event> events) {
    super();
    this.isSuccessful = isSuccessful;
    this.events = events;
  }

  public boolean isSuccessful() {
    return isSuccessful;
  }

  public List<Event> getEvents() {
    return events;
  }
  
}
