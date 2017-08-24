package com.rishiqing.mid.simulation

import edu.emory.mathcs.backport.java.util.LinkedList
import grails.test.mixin.TestFor
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserSimulationService)
class UserSimulationServiceSpec extends Specification {

    def setupSpec(){
    }

    def cleanupSpec(){
    }

    def setup() {
    }

    def cleanup() {
    }

    @Ignore
    @Unroll
    def "max of #a and #b is #c"() {
        expect:
        Math.max(a, b) == c

        where:
        a | b | c
        1 | 3 | 3
        5 | 2 | 5
//        8 | 7 | 7
    }

    @Ignore
    void "test truth value"(){
        expect:
        true
    }

    def "test something......"(){
        expect:
        1 == 1
    }

    @CompileStatic
    static void test(){
        def computer = new Computer()
        int len = computer.compute('foobar')
        String str = computer.compute(len)
        assert str == '6'
//        computer.with {
//            assert compute(compute('foobar')) == '6'
//        }
    }

    static def test(int a, int b){
        println "$a=====$b"
    }

    @CompileStatic
    static def showLength(String str){
        Computer c = new Computer()
        return c.compute(str)
    }

    public static void main(String[] args) {

//        String str = "asdf"
//
//        def map = [
//                a: 1,
//                1: 'fddd',
//                true: 333,
//                (true): 444,
//                str: 'qqqq',
//                (str): 'wwww'
//        ]
//
//        map << [a: 333]
//        map += [b: 444]
//        println "----${1}".hashCode() == "----1".hashCode()

//        println map['str']
//        println map[str]
//        println map.'str'
//        map['true'] == 333
//        map[true] == 444


//        def list = [222, 333] as int[]
//
//        list -= 222
//
//        assert list == [333]

//        python 猴子补丁
        Computer.metaClass.compute = {String str ->
            return 11111
        }

        println showLength('a')


//        def a = { -> println "----$it--"}
//
//        a()

//        def list = [1,2]
//        test(*list)
//
//        def list1 = [1, 2]
//
//        def list2 = [*list1, 3]
//
//        println list2

//        if(!null){
//            println "----"
//        }
//
//        int a = 1
//        int b = a ? a : 2



//        def clo = {
//            return 111;
//        }

//        String str = 'abcd'
//
//        test str

//        Map m = [
//                foo: { 11111 },
//                bar: { 22222 }
//        ]
//
//        FooBar f = m as FooBar
//
//        println f.foo()

//        try {
//
//        }catch(NumberFormatException | NullPointerException e){
//
//        }




//        int num = 1
//       def a =  "${num}"
//
//        test((String)"aaaa")
//
//        def n = 111111111111111111111144444444444444444433
//
//        def d = 1.1

    }
}
