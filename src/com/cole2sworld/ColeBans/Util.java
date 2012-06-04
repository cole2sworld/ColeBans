package com.cole2sworld.ColeBans;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;

import javax.management.InstanceAlreadyExistsException;

import com.cole2sworld.ColeBans.handlers.BanHandler;

public class Util {
	/**
	 * Check if the number "check" should be pluralized in speech with an s.
	 * Returns "s" if yes, "" if no when plural is true, "are" if yes or "is" if no when plural is false.
	 * <br/><b>Example:</b><br/>
	 * "There "+Util.getPlural(apples, false)+" "+apples+" apple"+Util.getPlural(apples, true)+" in the bowl."<br/>
	 * <br/>
	 * If 'apples' was 4, the string would be "There are 4 apples in the bowl."<br/>
	 * If 'apples' was 1, the string would be "There is 1 apple in the bowl."<br/>
	 * If 'apples' was 0, the string would be "There are 0 apples in the bowl."
	 * @param check The number to check
	 * @param plural Return s?
	 * @return Plural
	 */
	public static String getPlural(long check, boolean plural) {
		boolean isPlural = check <= 0 || check > 1;
		if (plural && isPlural) return "s";
		if (plural && !isPlural) return "";
		if (!plural && isPlural) return "are";
		return "is";
	}
	/**
	 * Lookup a banhandler by short name.
	 * @param shortName The short name of the handler
	 * @return The BanHandler
	 * @throws ClassNotFoundException If the handler does not exist
	 * @throws NoSuchMethodException If the handler is implemented wrong
	 * @throws InvocationTargetException If the handler does not implement onEnable
	 * @throws IllegalAccessException If the handler is implemented wrong
	 * @throws SecurityException If there is a plugin conflict
	 * @throws IllegalArgumentException If the handler is implemented wrong
	 * @throws InstanceAlreadyExistsException If the handler is already instantiated
	 * @throws ClassCastException If the handler is not actually a handler
	 */
	public static BanHandler lookupHandler(String shortName) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassCastException {
		return (BanHandler) Class.forName(GlobalConf.Advanced.pkg+"."+shortName+GlobalConf.Advanced.suffix).getDeclaredMethod("onEnable", new Class<?>[0]).invoke(null);
	}
	/**
	 * Turn a IP in format A.B.C.D into an array of bytes.
	 * @param str IP to process
	 * @return Processed IP
	 * @throws UnknownHostException if the IP has bad formatting
	 */
	public static byte[] processIp(String str) throws UnknownHostException {
		String[] ip = str.split("\\.");
		byte[] procIp = new byte[4];
		if (ip.length < 4) {
			throw new UnknownHostException();
		}
		try {
			procIp[0] = Byte.parseByte(ip[0]);
			procIp[1] = Byte.parseByte(ip[1]);
			procIp[2] = Byte.parseByte(ip[2]);
			procIp[3] = Byte.parseByte(ip[3]);
		} catch (NumberFormatException e) {
			throw new UnknownHostException();
		}
		return procIp;
	}
}
