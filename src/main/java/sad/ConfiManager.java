package sad;

public class ConfiManager {

	public static void main(String[] args) {
		Config cf = new Config();
		cf.load();
		
		//cf.set("test", "msg");
		cf.store();
		System.out.println(cf.get("test"));

	}

}
