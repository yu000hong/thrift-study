namespace java com.github.yu000hong.demo.thrift

const i32 BRAND_ALL = 0
const i32 BRAND_VW = 1
const i32 BRAND_BMW = 2
const i32 BRAND_BENZ = 3
const i32 BRAND_AUDI = 4

struct Car{
    1:i64 id,//ID
    2:i32 brand,//品牌
    3:string model//型号
}

exception DumplicateError{

}

exception ParameterError{

}

service CarService{

    oneway void beep()

    void add(1:i64 id, 2:i32 brand, 3:string model) throws (1:ParameterError pe, 2:DumplicateError de)

    Car get(1:i64 id) throws (1:ParameterError pe)

    list<Car> getList(1:list<i64> ids)

    set<Car> getSet(1:list<i64> ids)

    map<i64,Car> getMap(1:list<i64> ids)

}