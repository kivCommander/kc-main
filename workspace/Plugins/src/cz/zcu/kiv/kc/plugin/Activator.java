package cz.zcu.kiv.kc.plugin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Activator implements BundleActivator {
	private ClassPathXmlApplicationContext appContext;

	@Override
	public void start(BundleContext arg0) throws Exception {
//		appContext = new ClassPathXmlApplicationContext(
//		        new String[] {"bundle-context.xml", "bundle-context-osgi.xml"});
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
	}

}
