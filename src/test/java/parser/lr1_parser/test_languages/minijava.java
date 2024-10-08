class SubclassReturnValue
{
    public static void main(String[] args)
    {
        B b;
        A a;
        int[] asd;
        asd = new int[4+1];

        b = new B();

        a = b.foo();

        if(  (5 < 10) && (10 < 5)) {
            System.out.println(asd[5]);
        } else {
            System.out.println(asd[8]);
        }
    }
}

class A
{

}

class B extends A
{
    public A foo()
    {
        return new B();
    }
}