package chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollBar;

public class Server extends JFrame {

	private JPanel contentPane;
	static JTextArea msgArea;
	private static JTextArea msgSend;
	static DataOutputStream dataOut;
	static DataInputStream dataIn;
	static ServerSocket ss;
	static Socket s;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try{
			ss = new ServerSocket(1384);
			s = ss.accept();
			dataIn = new DataInputStream(s.getInputStream());
			dataOut = new DataOutputStream(s.getOutputStream());
			String msgIn;
			while(true){
				msgIn = dataIn.readUTF().trim();
				if(msgIn.length() > 45){
					int counter = 0;
					StringBuilder sb = new StringBuilder();
					for(int x = 0; x < msgIn.length(); x++){
						if(counter == 45){
							sb.append("\n     ");
							counter = 0;
						}
						sb.append(msgIn.charAt(x));
						counter += 1;	
					}
					msgIn = sb.toString();
				}
				msgArea.setText(msgArea.getText().trim() + "\nIn: " + msgIn);
				
			}
			
		}catch(Exception e){
		}
		
		
	}
	
	class sendDispatcher implements KeyEventDispatcher{

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if(e.getID() == KeyEvent.KEY_TYPED){
				if(Character.toString((e.getKeyChar())).equals("\n")){
					try{
						String msgOut;
						msgOut = msgSend.getText().trim();
						dataOut.writeUTF(msgOut);
						if(msgOut.length() > 44){
							int counter = 0;
							StringBuilder sb = new StringBuilder();
							for(int x = 0; x < msgOut.length(); x++){
								if(counter == 44){
									sb.append("\n        ");
									counter = 0;
								}
								sb.append(msgOut.charAt(x));
								counter += 1;
							}
							msgOut = sb.toString();
						}
						msgArea.setText(msgArea.getText().trim() + "\nOut: " + msgOut);
						msgSend.setText("");
					}catch(Exception e1){
						e1.printStackTrace();
					}
				}
			}
			return false;
		}
		
	}

	/**
	 * Create the frame.
	 */
	public Server() {
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		KeyboardFocusManager m = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		m.addKeyEventDispatcher(new sendDispatcher());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(29, 6, 393, 193);
		contentPane.add(scrollPane);
		
		msgArea = new JTextArea();
		scrollPane.setRowHeaderView(msgArea);
		msgArea.setEditable(false);
		
		JButton sendBtn = new JButton("Send");
		sendBtn.setBounds(327, 202, 98, 70);
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String msgOut;
					msgOut = msgSend.getText().trim();
					dataOut.writeUTF(msgOut);
					if(msgOut.length() > 44){
						int counter = 0;
						StringBuilder sb = new StringBuilder();
						for(int x = 0; x < msgOut.length(); x++){
							if(counter == 44){
								sb.append("\n        ");
								counter = 0;
							}
							sb.append(msgOut.charAt(x));
							counter += 1;
						}
						msgOut = sb.toString();
					}
					msgArea.setText(msgArea.getText().trim() + "\nOut: " + msgOut);
					msgSend.setText("");
				}catch(Exception e1){
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(sendBtn);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(26, 202, 287, 70);
		contentPane.add(scrollPane_1);
		
		msgSend = new JTextArea();
		scrollPane_1.setViewportView(msgSend);
		msgSend.setColumns(10);
	}

}
