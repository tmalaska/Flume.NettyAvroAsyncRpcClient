package com.cloudera.sa.flume.async.rpc;

import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.DEFAULT_EXCLUDE_EVENTS;
import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.DEFAULT_REGEX;
import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.EXCLUDE_EVENTS;
import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.REGEX;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.flume.interceptor.RegexFilteringInterceptor;

public class SleepingInterceptor implements Interceptor{

  public void close() {
    // TODO Auto-generated method stub
    
  }

  public void initialize() {
    // TODO Auto-generated method stub
  }

  public Event intercept(Event events) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return events;
  }

  public List<Event> intercept(List<Event> events) {
    for (Event event : events) {
      intercept(event);
    }
    return events;
  }
  
  /**
   * Builder which builds new instance of the StaticInterceptor.
   */
  public static class Builder implements Interceptor.Builder {

    int sleepTime;
    
    @Override
    public void configure(Context context) {
      String sleepTime = context.getString("sleep-time", "1000");
      
    }

    @Override
    public Interceptor build() {
      return new SleepingInterceptor();
    }
  }

}
