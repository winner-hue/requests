package icu.fanjie;

public class Test {
    @org.junit.Test
    public void test() {
        Params params = new Params("value:123;;;params:345");
        System.out.println(params.toString());
    }
}
