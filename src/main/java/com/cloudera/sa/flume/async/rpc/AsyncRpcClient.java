package com.cloudera.sa.flume.async.rpc;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;


public interface AsyncRpcClient extends RpcClient
{
  public Future<AppendAsyncResultPojo> appendAsync(Event event) throws EventDeliveryException;

  public Future<AppendBatchAsyncResultPojo> appendBatchAsync(List<Event> events) throws
      EventDeliveryException;

  
}
