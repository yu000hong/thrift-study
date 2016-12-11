package com.github.yu000hong.demo.thrift

import org.apache.thrift.TException

class CarServiceImpl implements CarService.Iface {


    @Override
    void beep() throws TException {
        println('beep......')
    }

    @Override
    void add(long id, int brand, String model) throws ParameterError, DumplicateError, TException {
        if (id <= 0) {
            throw new ParameterError()
        }
        if (id == 1L) {
            throw new DumplicateError()
        }
        println("Adding: id($id), brand($brand), model($model)")
    }

    @Override
    Car get(long id) throws ParameterError, TException {
        if (id <= 0) {
            throw new ParameterError()
        }
        return internalGet(id)
    }

    @Override
    List<Car> getList(List<Long> ids) throws TException {
        return ids.collect { id ->
            internalGet(id)
        }
    }

    @Override
    Set<Car> getSet(List<Long> ids) throws TException {
        def set = new HashSet()
        ids.each { id ->
            set << internalGet(id)
        }
        return set
    }

    @Override
    Map<Long, Car> getMap(List<Long> ids) throws TException {
        def map = [:]
        ids.each { id ->
            map[id] = internalGet(id)
        }
        return map
    }

    private Car internalGet(long id) {
        if (id % 10 == 0L) {
            return null
        }
        return new Car(id: id, brand: CarServiceConstants.BRAND_BENZ, model: 'C300L')
    }

}
