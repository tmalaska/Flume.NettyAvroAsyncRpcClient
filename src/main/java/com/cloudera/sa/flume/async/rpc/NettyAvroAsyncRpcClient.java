package com.cloudera.sa.flume.async.rpc;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.FlumeException;
import org.apache.flume.api.NettyAvroRpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyAvroAsyncRpcClient implements AsyncRpcClient {

  ArrayBlockingQueue<NettyAvroRpcClient> clientQueue;
  
  public static final String ASYNC_MAX_THREADS = "async-mx-threads";

  ExecutorService executorService;

  private static final Logger logger = LoggerFactory.getLogger(NettyAvroAsyncRpcClient.class);

  public NettyAvroAsyncRpcClient(Properties starterProp) {
    
    int numberOfClientThreads = Integer.parseInt(starterProp.getProperty(ASYNC_MAX_THREADS));
    
    clientQueue = new ArrayBlockingQueue<NettyAvroRpcClient>(numberOfClientThreads);

    for (int i = 0 ; i < numberOfClientThreads; i++) {
      NettyAvroRpcClient client = (NettyAvroRpcClient)RpcClientFactory.getInstance(starterProp);
      clientQueue.add(client);
    }

    logger.info("Number of Threads:" + numberOfClientThreads);
    executorService = Executors.newFixedThreadPool(numberOfClientThreads);
  }

  public Future<AppendAsyncResultPojo> appendAsync(final Event event) throws EventDeliveryException {

    Future<AppendAsyncResultPojo> future = executorService.submit(new Callable<AppendAsyncResultPojo>() {
      public AppendAsyncResultPojo call() throws Exception {
        try {
          NettyAvroRpcClient client = clientQueue.poll();
          client.append(event);
          clientQueue.add(client);
          return new AppendAsyncResultPojo(true, event);
        } catch (Exception e) {
          NettyAvroAsyncRpcClient.logger.error("Unable to append Event", e);
          return new AppendAsyncResultPojo(false, event);
        }
      }
    });
    return future;
  }

  public Future<AppendBatchAsyncResultPojo> appendBatchAsync(final List<Event> events) throws EventDeliveryException {
    Future<AppendBatchAsyncResultPojo> future = executorService.submit(new Callable<AppendBatchAsyncResultPojo>() {
      public AppendBatchAsyncResultPojo call() throws Exception {
        try {
          NettyAvroRpcClient client = clientQueue.poll();
          client.appendBatch(events);
          clientQueue.add(client);
          return new AppendBatchAsyncResultPojo(true, events);
        } catch (Exception e) {
          NettyAvroAsyncRpcClient.logger.error("Unable to append Event batch", e);
          return new AppendBatchAsyncResultPojo(false, events);
        }
      }
    });

    return future;

  }

  @Override
  public int getBatchSize() {
    
    return 0;
  }

  @Override
  public void append(Event event) throws EventDeliveryException {
    try {
      this.appendAsync(event).get();
    } catch (Exception e) {
      throw new EventDeliveryException(e);
    }
  }

  @Override
  public void appendBatch(List<Event> events) throws EventDeliveryException {
    try {
      this.appendBatchAsync(events).get();
    } catch (Exception e) {
      throw new EventDeliveryException(e);
    }
  }

  @Override
  public boolean isActive() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public void close() throws FlumeException {
    
    
  }

}
