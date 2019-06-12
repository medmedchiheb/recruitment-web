package fr.d2factory.libraryapp.member;

/**
 * 
 * @author benjemaam helper class to provide auto-genreted membershipId
 */
public class MembershipIdHelper {

	private static int nextId = 0;

	public static synchronized int nextId() {
		return nextId++;
	}

}
