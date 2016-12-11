import com.github.yu000hong.demo.thrift.CarService
import com.github.yu000hong.demo.thrift.DumplicateError
import com.github.yu000hong.demo.thrift.ParameterError
import org.apache.thrift.TApplicationException
import org.apache.thrift.TException
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.protocol.TMultiplexedProtocol
import org.apache.thrift.protocol.TProtocol
import org.apache.thrift.transport.TSocket
import org.apache.thrift.transport.TTransport
import org.apache.thrift.transport.TTransportException

def server = '127.0.0.1'
def port = 9999


def runner = { Closure closure ->
    try {
        // 设置调用的服务地址-端口
        TTransport transport = new TSocket(server, port)
        //  使用二进制协议
        TProtocol protocol = new TBinaryProtocol(transport)
        // 使用的接口
        CarService.Client client = new CarService.Client(new TMultiplexedProtocol(protocol, 'carService'))
        //打开socket
        transport.open()
        closure.call(client)
        transport.close()
    } catch (ParameterError pe) {
        println(pe.message)
        println(pe.stackTrace)
    } catch (DumplicateError de) {
        println(de.message)
        println(de.stackTrace)
    } catch (TTransportException tte) {
        println("Error type: ${tte.type}")
        println("Error cause: ${tte.cause}")
        tte.printStackTrace()
    } catch (TApplicationException tae) {
        println(tae.message)
        println(tae.type)
        tae.printStackTrace()
    } catch (TException te) {
        te.printStackTrace()
    }
}

def ids = [1L, 10L, 15L, 28L, 30L]

def testReturnNullClosure = { CarService.Client client ->
    client.get(10)
}

def testGetClosure = { CarService.Client client ->
    def car = client.get(6)
    println(car.toString())
}

def testParameterErrorClosure = { CarService.Client client ->
    client.get(-1)
}

def testOnewayBeep = { CarService.Client client ->
    client.beep()
}

def testGetList = { CarService.Client client ->
    def list = client.getList(ids)
    list.eachWithIndex { item, index ->
        println("$index: $item")
    }
}

def testGetSet = { CarService.Client client ->
    def set = client.getSet(ids)
}

def testGetMap = { CarService.Client client ->
    def map = client.getMap(ids)
}

runner.call(testGetSet)