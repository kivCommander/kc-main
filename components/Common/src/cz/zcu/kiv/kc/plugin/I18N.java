package cz.zcu.kiv.kc.plugin;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * translation getter
 * @author Michal
 *
 */
public class I18N {
	public static String getText(String key) {
		String ret = key;
		try
		{
			ret = ResourceBundle.getBundle("kivCmd", Locale.getDefault()).getString(key);
		} catch (java.util.MissingResourceException ex) {}
		return ret;
	}
	
	public static String getText(String key, Object param) {
		String text = getText(key);
		return text.replace("{0}", param.toString());
	}
}
