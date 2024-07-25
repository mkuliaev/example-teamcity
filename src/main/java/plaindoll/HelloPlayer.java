package plaindoll;

public class HelloPlayer{
	public static void main(String[] args) {
		Welcomer welcomer = new Welcomer();
		System.out.println(welcomer.SayHello());
		System.out.println(welcomer.sayFarewell());
	}
}
