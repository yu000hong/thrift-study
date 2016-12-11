package com.github.yu000hong.demo.thrift

import org.apache.thrift.TMultiplexedProcessor
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.server.TServer
import org.apache.thrift.server.TThreadPoolServer
import org.apache.thrift.transport.TServerSocket
import org.apache.thrift.transport.TTransportException

/**
 * Thrift server
 */
class CarServer {

    private CarServiceImpl carService = new CarServiceImpl()

    private TServer server

    public void startService(int port) {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress('0.0.0.0', port)
            // 设置服务器端口
            TServerSocket serverTransport = new TServerSocket(socketAddress)
            // 设置二进制协议工厂
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory()
            //处理器关联业务实现
            //处理器关联业务实现
            def carProcessor = new CarService.Processor<CarService.Iface>(carService)
            TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor()
            multiplexedProcessor.registerProcessor('carService', carProcessor)
            // 使用线程池服务模型
            TThreadPoolServer.Args poolArgs = new TThreadPoolServer.Args(serverTransport)
            poolArgs.maxWorkerThreads(100)
            poolArgs.processor(multiplexedProcessor)
            poolArgs.protocolFactory(protocolFactory)
            server = new TThreadPoolServer(poolArgs)
            server.serve()
        } catch (TTransportException e) {
            e.printStackTrace()
        }
    }

    public void stopService() {
        if (server) {
            server.stop()
        }
    }

    public static void main(String[] args) {
        def server = new CarServer()
        server.startService(9999)

        //close server gracefully
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.stopService()
                println('Shutdown gracefully~')
            }
        })
    }

}
