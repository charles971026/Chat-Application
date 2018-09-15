package chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class Client extends JFrame {

	private JPanel contentPane;
	private static JTextArea msgSend;
	private static JTextArea msgArea;
	static Socket s;
	static DataInputStream dataIn;
	static DataOutputStream dataOut;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try{
			s = new Socket("192.168.1.68", 1384);
			dataIn = new DataInputStream(s.getInputStream());
			dataOut = new DataOutputStream(s.getOutputStream());
			String msgIn = "";
			while(true){
				msgIn = dataIn.readUTF();
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
				if(Character.toString(e.getKeyChar()).equals("\n")){
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
	public Client() {
		setTitle("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		KeyboardFocusManager m = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		m.addKeyEventDispatcher(new sendDispatcher());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(29, 205, 279, 66);
		contentPane.add(scrollPane_1);
		
		msgSend = new JTextArea();
		scrollPane_1.setViewportView(msgSend);
		msgSend.setColumns(10);
		
		JButton sendBtn = new JButton("Send");
		sendBtn.setBounds(318, 206, 104, 66);
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
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 6, 390, 193);
		contentPane.add(scrollPane);
		
		msgArea = new JTextArea();
		scrollPane.setColumnHeaderView(msgArea);
		msgArea.setEditable(false);
	}
}
