package nc.starter.ui;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.*;
import javax.swing.*;
import java.awt.*;


public class AppletViewer extends JFrame {
	private static final long serialVersionUID = 6195829231526672595L;
	private String protocol = "http";
	private String server = null;
	private int port = 80;
	private String file = "/service/ncstarterservlet";
	private JApplet applet = null;
	private AppletViewerProgressPanel progressPanel = null;

	public AppletViewer(String protocol, String server, int port)
			throws HeadlessException {
		super();
		this.protocol = protocol;
		this.server = server;
		this.port = port;
		initialize();
	}

	public AppletViewer(String protocol, String server, int port, String file)
			throws HeadlessException {
		super();
		this.protocol = protocol;
		this.server = server;
		this.port = port;
		this.file = file;
		initialize();
	}

	private void initialize() {
		getContentPane().setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}

		});
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = -6581125447348608532L;

			@Override
			public void doLayout() {
				Dimension size = this.getSize();
				Dimension progSize = progressPanel.getPreferredSize();
				progressPanel.setBounds((size.width - progSize.width) / 2,
						(size.height - progSize.height) / 2, progSize.width,
						progSize.height);
			}

		};
		panel.setBackground(Color.white);
		URL url = null;
		try {
			url = new URL(protocol, server, port, "/logo/images/logo.gif");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		progressPanel = new AppletViewerProgressPanel(url);
		progressPanel.setOpaque(false);
		progressPanel.setPreferredSize(new Dimension(300, 300));
		panel.add(progressPanel);
		getContentPane().add(panel, BorderLayout.CENTER);
		SwingWorker<Object, Applet> worker = new SwingWorker<Object, Applet>() {
			@Override
			protected Object doInBackground() throws Exception {
				Class<?> objCls = Class.forName("nc.sfbase.applet.NCApplet");
				applet = (JApplet) objCls.newInstance();
				applet.setPreferredSize(new Dimension(0, 0));
				getContentPane().add(applet, BorderLayout.NORTH);
				try {
					AppletStub stub = getAppletStub();
					if (stub != null) {
						applet.setStub(stub);
					}
					applet.init();

				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage(), e);
				}
				return null;
			}

			@Override
			protected void done() {
				getContentPane().removeAll();
				getContentPane().add(applet, BorderLayout.CENTER);
				applet.start();
				getContentPane().invalidate();
				getContentPane().validate();
				getContentPane().repaint();
			}

		};
		worker.execute();
	}

	private AppletStub getAppletStub() throws Exception {
		// String urlstr =
		// protocol+"://"+server+":"+port+"/service/ncstarterservlet";
		URL url = new URL(protocol, server, port, file);
		System.out.println(url);
		URLConnection con = url.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		PrintWriter pw = null;
		ObjectInputStream ois = null;
		AppletStub stub = null;
		try {
			pw = new PrintWriter(con.getOutputStream());
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			pw.print("width=" + size.width + "&height=" + size.height);
			pw.close();
			pw = null;
			ois = new ObjectInputStream(con.getInputStream());
			stub = (AppletStub) ois.readObject();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (ois != null) {
				ois.close();
			}
		}
		return stub;

	}

}
