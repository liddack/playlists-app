package com.liddack.playlistsapp.ui;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.SpringLayout;

import com.liddack.playlistsapp.dominio.User;
import com.liddack.playlistsapp.persistencia.UserDB;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JLabel lblToggleFunction;
	private char status;
	private final String TOGGLE_CREATE_TEXT = "Ainda não criou uma conta?";
	private final String TOGGLE_LOGIN_TEXT = "Já possui uma conta?";
	private String currentToggleText;
	private JButton btnLogin;
	private JLabel lblErrorMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			private Login frame;

			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/res/favicon.png")));
		setTitle("Login - PlaylistsApp");
		setResizable(false);
		this.status = 1;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 451, 394);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panelLogin = new JPanel();
		panelLogin.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		panelLogin.setBackground(Color.WHITE);
		
		JLabel lblTitle = new JLabel("PlaylistsApp");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 30));
		
		JLabel lblSplashIcon = new JLabel("");
		lblSplashIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblSplashIcon.setIcon(new ImageIcon(Login.class.getResource("/res/splash-icon.png")));
		
		lblErrorMessage = new JLabel("");
		lblErrorMessage.setForeground(new Color(229, 49, 45));
		lblErrorMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblErrorMessage.setBackground(Color.WHITE);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelLogin, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
						.addComponent(lblSplashIcon, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
						.addComponent(lblTitle, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
						.addComponent(lblErrorMessage, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(9)
					.addComponent(lblSplashIcon)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTitle)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblErrorMessage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelLogin, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		SpringLayout sl_panelLogin = new SpringLayout();
		panelLogin.setLayout(sl_panelLogin);
		
		txtUsername = new JTextField();
		txtUsername.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginFlow();
			}
		});
		txtUsername.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				txtUsername.selectAll();
			}
		});
		sl_panelLogin.putConstraint(SpringLayout.NORTH, txtUsername, 21, SpringLayout.NORTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.WEST, txtUsername, 109, SpringLayout.WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.EAST, txtUsername, -112, SpringLayout.EAST, panelLogin);
		panelLogin.add(txtUsername);
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loginFlow();
			}
		});
		txtPassword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtPassword.selectAll();
			}
		});
		sl_panelLogin.putConstraint(SpringLayout.NORTH, txtPassword, 73, SpringLayout.NORTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.WEST, txtPassword, 0, SpringLayout.WEST, txtUsername);
		sl_panelLogin.putConstraint(SpringLayout.EAST, txtPassword, 0, SpringLayout.EAST, txtUsername);
		panelLogin.add(txtPassword);
		txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtPassword.setColumns(10);
		
		btnLogin = new JButton("Entrar");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loginFlow();
			}
		});
		sl_panelLogin.putConstraint(SpringLayout.WEST, btnLogin, 159, SpringLayout.WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, btnLogin, 0, SpringLayout.SOUTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.EAST, btnLogin, -152, SpringLayout.EAST, panelLogin);
		panelLogin.add(btnLogin);
		btnLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		JLabel lblUsername = new JLabel("Nome de usu\u00E1rio:");
		sl_panelLogin.putConstraint(SpringLayout.WEST, lblUsername, 0, SpringLayout.WEST, txtUsername);
		lblUsername.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		sl_panelLogin.putConstraint(SpringLayout.NORTH, lblUsername, 0, SpringLayout.NORTH, panelLogin);
		panelLogin.add(lblUsername);
		lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		JLabel lblPassword = new JLabel("Senha:");
		sl_panelLogin.putConstraint(SpringLayout.WEST, lblPassword, 0, SpringLayout.WEST, txtUsername);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, lblPassword, -6, SpringLayout.NORTH, txtPassword);
		panelLogin.add(lblPassword);
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		lblToggleFunction = new JLabel(TOGGLE_CREATE_TEXT);
		sl_panelLogin.putConstraint(SpringLayout.NORTH, lblToggleFunction, 3, SpringLayout.SOUTH, txtPassword);
		sl_panelLogin.putConstraint(SpringLayout.WEST, lblToggleFunction, 26, SpringLayout.WEST, txtUsername);
		sl_panelLogin.putConstraint(SpringLayout.EAST, lblToggleFunction, -131, SpringLayout.EAST, panelLogin);
		lblToggleFunction.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				currentToggleText = lblToggleFunction.getText();
				Login.this.lblToggleFunction.setText("<html><u>" + currentToggleText + "</u></html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Login.this.lblToggleFunction.setText(currentToggleText);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Login.this.status == 1) {
					Login.this.status = 2;
					Login.this.setTitle("Criar conta - PlaylistsApp");
					lblToggleFunction.setText(TOGGLE_LOGIN_TEXT);
					currentToggleText = TOGGLE_LOGIN_TEXT;
					Login.this.btnLogin.setText("Criar conta");
				}
				else if (Login.this.status == 2) {
					Login.this.status = 1;
					Login.this.setTitle("Login - PlaylistsApp");
					lblToggleFunction.setText(TOGGLE_CREATE_TEXT);
					currentToggleText = TOGGLE_CREATE_TEXT;
					Login.this.btnLogin.setText("Entrar");
				}
			}
		});
		lblToggleFunction.setForeground(new Color(0, 51, 153));
		lblToggleFunction.setHorizontalAlignment(SwingConstants.CENTER);
		lblToggleFunction.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblToggleFunction.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		panelLogin.add(lblToggleFunction);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void initializeMainUI(User user) {
		new MainUI(user).setVisible(true);
		Login.this.dispose();
	}
	
	private void showError(String message) {
		Login.this.lblErrorMessage.setText(message);
	}
	
	private void loginFlow() {
		showError("");
		String username = txtUsername.getText();
		String password = String.valueOf(txtPassword.getPassword());
		
		if (status == 1) /* login */ {
			if (!username.equals("")) {
				UserDB userDB = null;
				User user = null;
				try {
					userDB = new UserDB();
				} catch (SQLException e) {
					e.printStackTrace();
					if (e instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException) {
						showError("Houve um erro na conexão com o servidor");
					}
				}
				if (userDB != null) {
					user = userDB.buscarPorID(username);
					if (user == null) {
						showError("Este usuário não existe");
					} else {
						if (password.equals("")) showError("Informe a senha");
						else if (!user.getPassword().equals(password)) {
							showError("A senha está incorreta");
						} else {
							initializeMainUI(user);
						}
					}
				}
			}
		}
		else if (status == 2) /* Criar conta */ {
			if (username.equals("")) {
				showError("Digite um nome de usuário!");
			} else {
				UserDB userDB = null;
				User user = null;
				try {
					userDB = new UserDB();
				} catch (SQLException e) {
					e.printStackTrace();
					if (e instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException) {
						showError("Houve um erro na conexão com o servidor");
					}
				}
				if (userDB != null) {
					user = userDB.buscarPorID(username);
					if (user != null) showError("Este nome de usuário já existe");
					else {
						if (password.equals("")) {
							showError("Digite uma senha de no mínimo 4 caracteres");
						} else if (password.length() < 4) showError("A senha deve ter no mínimo 4 caracteres");
						else {
							user = new User(username, password);
							userDB.inserir(user);
							initializeMainUI(user);
						}
					}
				}
			}
		}
	}
}
