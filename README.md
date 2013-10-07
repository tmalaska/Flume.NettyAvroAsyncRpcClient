Flume.NettyAvroAsyncRpcClient
=================

A simple wrapper of the Flume's NettyAvroRpcClient to allow for asycn appends of events and event batches.

To make the NettyAvroAsyncRpcClient is as simple as this

NettyAvroAsyncRpcClient client = new NettyAvroAsyncRpcClient( starterProp);

Included in the properties must be "async-mx-threads".  This configure will define the number of threads the async client will support.  The number you pick will depend on the number of CPUs you have and the latency between the client and server.

