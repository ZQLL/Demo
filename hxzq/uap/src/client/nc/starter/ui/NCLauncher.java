package nc.starter.ui;

import java.awt.Toolkit;

import javax.swing.JFrame;

public class NCLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args != null && args.length == 1) {
			System.setProperty("nc.jstart.server", args[0]);
		} else if (args != null && args.length == 2) {
			System.setProperty("nc.jstart.server", args[0]);
			System.setProperty("nc.jstart.port", args[1]);
		} else if (args != null && args.length == 3) {
			System.setProperty("nc.jstart.protocol", args[0]);
			System.setProperty("nc.jstart.server", args[1]);
			System.setProperty("nc.jstart.port", args[2]);

		}
		String protocol = System.getProperty("nc.jstart.protocol");
		String server = System.getProperty("nc.jstart.server");
		String port = System.getProperty("nc.jstart.port");
		if (isNullString(protocol)) {
			protocol = "http";
		}
		if (isNullString(server)) {
			server = "localhost";
		}
		if (isNullString(port)) {
			port = "80";
		}

		startNC(protocol, server, port);
	}

	private static boolean isNullString(String str) {
		return str == null || str.trim().length() == 0;
	}

	private static void startNC(String protocol, String server, String portStr) {
		/*
		 * int port = Integer.parseInt(portStr); GraphicsDevice dev =
		 * GraphicsEnvironment
		 * .getLocalGraphicsEnvironment().getDefaultScreenDevice(); DisplayMode
		 * mode = new DisplayMode(1366, 768, 32,
		 * DisplayMode.REFRESH_RATE_UNKNOWN);
		 * 
		 * AppletViewer viewer = new AppletViewer(protocol, server, port);
		 * 
		 * viewer.setSize((int)
		 * Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int)
		 * Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
		 * 
		 * viewer.setState(JFrame.NORMAL);
		 * 
		 * dev.setFullScreenWindow(viewer); dev.setDisplayMode(mode);
		 */

		int port = Integer.parseInt(portStr);
		AppletViewer viewer = new AppletViewer(protocol, server, port);
		viewer.setSize((int) Toolkit.getDefaultToolkit().getScreenSize()
				.getWidth() / 2, (int) Toolkit.getDefaultToolkit()
				.getScreenSize().getHeight() / 2);

		viewer.setState(JFrame.NORMAL);
		if (Toolkit.getDefaultToolkit().isFrameStateSupported(
				JFrame.MAXIMIZED_BOTH)) {
			viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		viewer.setVisible(true);

	}

}
