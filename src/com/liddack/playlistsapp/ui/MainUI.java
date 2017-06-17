package com.liddack.playlistsapp.ui;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.liddack.playlistsapp.dominio.User;
import com.liddack.playlistsapp.dominio.Playlist;
import com.liddack.playlistsapp.dominio.Track;
import com.liddack.playlistsapp.persistencia.PlaylistDB;
import com.liddack.playlistsapp.persistencia.TrackDB;
import com.liddack.playlistsapp.spotifyAuth.SpotifyAuthorizationFlow;
import com.liddack.playlistsapp.util.Spotify;
import com.liddack.playlistsapp.util.threads.AudioPreviewConnectionThread;
import com.liddack.playlistsapp.util.threads.ImageConnectionThread;
import com.liddack.playlistsapp.util.threads.SearchConnectionThread;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;



public class MainUI extends JFrame implements ListSelectionListener {

	private static JButton btnAudioPreview_PlaylistDetails;
	private static JButton btnAudioPreview_Search;
	private static JButton btnBackToPlaylists_cS; 
	private static JButton btnPesquisar;
	private static JButton btnRemPlaylist;
	private static JPanel cardLayout;
	private static JPanel cardSearch;
	private static JPanel cardPlaylists;
	private static ImageIcon iconDefault;
	private static ImageIcon ICON_PLAY;
	private static ImageIcon ICON_STOP;
	private static Playlist detailed;
	private static JLabel lblAlbum;
	private static JLabel lblAlbumArt;
	private static JLabel lblAlbumImage;
	private static JLabel lblArtist;
	private static JLabel lblDuration; 
	private static JLabel lblDurationTip;
	private static JLabel lblNome;
	private static JLabel lblPlaylistCreationDate;
	private static JLabel lblPlaylistSize;
	private static JLabel lblTotalDuration;
	private static JTextPane txtpnInfo;
	private static final String NOT_PLAYING = "Tocar pr\u00E9via";
	private static final String PLAYING = "Parar";
	private static List<Playlist> userPlaylists;
	private static AdvancedPlayer previewPlayer = null;
	private static JList<Track> searchList;
	private static ArrayList<Track> searchResultsArray;
	private static Track selectedTrack;
	private static final long serialVersionUID = 1L;
	private static JTextField tfSearchQuery;
	
	private User currentUser;
	private JButton btnAddTrack;
	private JButton btnAdicFaixas;
	private JButton btnEditarNome;
	private JButton btnParaBaixo;
	private JButton btnParaCima;
	private JButton btnRemTrack;
	private JButton btnSeePlaylistDetails;
	private JPanel cardPlaylistDetails;
	private SearchConnectionThread connectionThread;
	private JPanel imagePanel;
	private JLabel lblPlaylistNameTitle;
	private JPanel panel;
	private JPanel Root;
	private JTable tblPlaylists;
	private JTable tblTracks;
	private DefaultTableModel tblPlaylistsModel;
	private DefaultTableModel tblTracksModel;
	//private UserDB userDB;
	//private TrackDB trackDB;
	private PlaylistDB playlistDB;
	private static SpotifyAuthorizationFlow spotifyApi;
	
	
	/**
	 * Launch the application.
	 */
	
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					MainUI frame = new MainUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} // main()*/
	
	public MainUI(User currentUser) {
		// Declarações iniciais
		this.currentUser = currentUser;
		spotifyApi = new SpotifyAuthorizationFlow();
		setTitle("PlaylistsApp - " + this.currentUser.getUsername());
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("res/favicon.png")).getImage());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 472);
		userPlaylists = new ArrayList<Playlist>();
		Root = new JPanel();
		Root.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(Root);
		Root.setLayout(null);
		
		// Definição dos caminhos dos ícones de Play e Stop no classpath
		ICON_PLAY = new ImageIcon(getClass().getClassLoader().getResource("res/play.png"));
		ICON_STOP = new ImageIcon(getClass().getClassLoader().getResource("res/stop.png"));
		
		// Definição do cardLayout raiz
		cardLayout = new JPanel();
		cardLayout.setBorder(new EmptyBorder(0, 0, 0, 0));
		cardLayout.setBounds(0, 0, 670, 511);
		Root.add(cardLayout);
		cardLayout.setLayout(new CardLayout(0, 0));
		
		
		/*
		 * ** cardPlaylists
		 */
		cardPlaylists = new JPanel();
		cardPlaylists.setForeground(Color.BLACK);
		cardPlaylists.setBackground(Color.getColor("24, 24, 24"));
		cardLayout.add(cardPlaylists, "name_12512336390572");
		cardPlaylists.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setToolTipText("");
		scrollPane_1.setBounds(27, 66, 619, 317);
		cardPlaylists.add(scrollPane_1);
		
		// Tabela que mostra as playlists do usuário
		tblPlaylists = new JTable();
		tblPlaylists.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {	// Clique de mouse
		    	// Identifica a linha clicada
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        // Verifica se o clique é duplo
		        if (me.getClickCount() == 2) {
		            // Detalha a playlist selecionada
		        	// Isso abre a tela de Detalhes da playlist (cardPlaylistDetails)
		        	detailed = (Playlist) tblPlaylists.getValueAt(row, 0);
					populatePlaylistDetails(detailed);
		        }
		    }
		});
		tblPlaylists.setRowHeight(20);
		tblPlaylists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblPlaylists.setShowHorizontalLines(false);
		tblPlaylists.setShowVerticalLines(false);
		tblPlaylistsModel = new DefaultTableModel(
				new Object[][] {
						{null, null, null, null},
					},
					new String[] {
						"Nome", "# de m\u00FAsicas", "Dura\u00E7\u00E3o total", "Data de cria\u00E7\u00E3o"
					}
				) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					boolean[] columnEditables = new boolean[] {
						false, false, false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				};
		tblPlaylists.setModel(tblPlaylistsModel);
		tblPlaylists.getColumnModel().getColumn(0).setPreferredWidth(196);
		tblPlaylists.getColumnModel().getColumn(2).setPreferredWidth(77);
		tblPlaylists.getColumnModel().getColumn(3).setPreferredWidth(85);
		tblPlaylists.setFillsViewportHeight(true);
		tblPlaylists.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tblPlaylists.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            if (tblPlaylists.getSelectedRow() > -1) {
	            	btnRemPlaylist.setEnabled(true);
	            	btnSeePlaylistDetails.setEnabled(true);
	            } else {
	            	btnRemPlaylist.setEnabled(false);
	            	btnSeePlaylistDetails.setEnabled(false);
	            }
	        }
	    });
		scrollPane_1.setViewportView(tblPlaylists);
		
		// Botão Nova playlist
		JButton btnNovaPlaylist = new JButton("Nova");
		btnNovaPlaylist.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/plus.png")));
		btnNovaPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Mostra um diálogo que recebe o nome da nova playlist
				String name = (String) JOptionPane.showInputDialog(
						cardPlaylists, 
						"Digite o nome da playlist:", 
						"Nova playlist", 
						JOptionPane.PLAIN_MESSAGE);
				if (name != null && !name.equals("")) {
					// Cria uma playlist e atualiza a tblPlaylists 
					addPlaylist(name);
					updateUserPlaylistsTable();
				}
			}
		});
		btnNovaPlaylist.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnNovaPlaylist.setBounds(153, 25, 80, 30);
		cardPlaylists.add(btnNovaPlaylist);
		
		// Botão excluir playlist 
		btnRemPlaylist = new JButton("Excluir");
		btnRemPlaylist.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/trash-can.png")));
		btnRemPlaylist.setEnabled(false);
		btnRemPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Identifica a playlist a ser excluída
				Playlist removing = (Playlist) tblPlaylists.getValueAt(tblPlaylists.getSelectedRow(), 0);
				if (JOptionPane.showConfirmDialog(cardPlaylists, "Tem certeza que deseja excluir \"" + removing +"\"?") == 0) {
					// Exclui a playlist
					removePlaylist((Playlist) tblPlaylists.getValueAt(tblPlaylists.getSelectedRow(), 0));
					updateUserPlaylistsTable();
				}
			}
		});
		btnRemPlaylist.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnRemPlaylist.setBounds(27, 394, 108, 30);
		cardPlaylists.add(btnRemPlaylist);
		
		// Botão de pesquisar músicas
		JButton btnOpenSearch_cP = new JButton("Pesquisar m\u00FAsicas");
		btnOpenSearch_cP.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/search-icon.png")));
		btnOpenSearch_cP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// muda para a tela de pesquisa (cardSearch)
				lblAlbumImage.setIcon(iconDefault);
				cardSearch.setVisible(true);
				btnBackToPlaylists_cS.setText("Playlists");
				cardPlaylists.setVisible(false);
				verifyAuthorization();
			}
		});
		btnOpenSearch_cP.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnOpenSearch_cP.setBounds(422, 25, 147, 30);
		cardPlaylists.add(btnOpenSearch_cP);
		
		// Título da tela de Playlists
		JLabel lblPlaylistsTitle = new JLabel("Playlists");
		lblPlaylistsTitle.setForeground(new Color(30, 30, 30));
		lblPlaylistsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 32));
		lblPlaylistsTitle.setBounds(27, 11, 129, 48);
		cardPlaylists.add(lblPlaylistsTitle);
		
		// Botão ver detalhes
		btnSeePlaylistDetails = new JButton("Ver detalhes");
		btnSeePlaylistDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Detalha a playlist selecionada
	        	// Isso abre a tela de Detalhes da playlist (cardPlaylistDetails)
				detailed = (Playlist) tblPlaylists.getValueAt(tblPlaylists.getSelectedRow(), 0);
				populatePlaylistDetails(detailed);
			}
		});
		btnSeePlaylistDetails.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnSeePlaylistDetails.setEnabled(false);
		btnSeePlaylistDetails.setBounds(145, 394, 108, 30);
		cardPlaylists.add(btnSeePlaylistDetails);
		
		JButton btnLogout = new JButton("Sair");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Login login = new Login();
				login.setVisible(true);
				MainUI.this.dispose();
			}
		});
		btnLogout.setIcon(new ImageIcon(MainUI.class.getResource("/res/logout.png")));
		btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnLogout.setBounds(579, 25, 67, 30);
		cardPlaylists.add(btnLogout);
		
		
		/*
		 * * * cardPlaylistDetails * *
		 */
		cardPlaylistDetails = new JPanel();
		cardLayout.add(cardPlaylistDetails, "name_16850112663675");
		cardPlaylistDetails.setLayout(null);
		
		JButton btnBackToPlaylists_cPD = new JButton("Playlists");
		btnBackToPlaylists_cPD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainUI.this.cardPlaylistDetails.setVisible(false);
				MainUI.this.updateUserPlaylistsTable();
				MainUI.detailed = null;
				MainUI.cardPlaylists.setVisible(true);
			}
		});
		btnBackToPlaylists_cPD.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/back.png")));
		btnBackToPlaylists_cPD.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnBackToPlaylists_cPD.setBounds(499, 25, 147, 30);
		cardPlaylistDetails.add(btnBackToPlaylists_cPD);
		
		JPanel playlistDetailsTitlePanel = new JPanel();
		playlistDetailsTitlePanel.setBounds(27, 11, 459, 48);
		cardPlaylistDetails.add(playlistDetailsTitlePanel);
		playlistDetailsTitlePanel.setLayout(new BoxLayout(playlistDetailsTitlePanel, BoxLayout.X_AXIS));
		//panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		lblPlaylistNameTitle = new JLabel("Nome da playlist");
		lblPlaylistNameTitle.setMinimumSize(new Dimension(100, 46));
		lblPlaylistNameTitle.setMaximumSize(new Dimension(340, 46));
		playlistDetailsTitlePanel.add(lblPlaylistNameTitle);
		lblPlaylistNameTitle.setForeground(new Color(30, 30, 30));
		lblPlaylistNameTitle.setFont(new Font("Segoe UI", Font.PLAIN, 32));
		
		JPanel editButtonPanel = new JPanel();
		editButtonPanel.setMaximumSize(new Dimension(10000, 46));
		editButtonPanel.setMinimumSize(new Dimension(140, 46));
		playlistDetailsTitlePanel.add(editButtonPanel);
		editButtonPanel.setLayout(new BoxLayout(editButtonPanel, BoxLayout.X_AXIS));
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(110, 46));
		panel.setMinimumSize(new Dimension(160, 46));
		editButtonPanel.add(panel);
		panel.setLayout(null);
		
		btnEditarNome = new JButton("Editar nome");
		btnEditarNome.setBounds(10, 13, 101, 30);
		panel.add(btnEditarNome);
		btnEditarNome.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnEditarNome.setBorder(new EmptyBorder(7, 10, 7, 10));
		btnEditarNome.setBorderPainted(false);
		btnEditarNome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = JOptionPane.showInputDialog(
						cardPlaylistDetails, 
						"Digite o novo nome", 
						MainUI.detailed.getName());
				if (input != null && !input.equals("")) {
					MainUI.detailed.setName(input);
					//userPlaylists.set(userPlaylists.indexOf(detailed), detailed);
					MainUI.this.modifyPlaylist(detailed);
					MainUI.this.populatePlaylistDetails(detailed);
				}
			}
		});
		btnEditarNome.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/edit.png")));
		btnEditarNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(null);
		scrollPane_2.setToolTipText("");
		scrollPane_2.setBounds(194, 66, 452, 317);
		cardPlaylistDetails.add(scrollPane_2);
		
		tblTracks = new JTable();
		tblTracks.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        /*JTable table = (JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);*/
		        if (me.getClickCount() == 2) {
					stopPreview();
					playPreview(selectedTrack);
				}
		    }
		});
		tblTracks.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTracksModel = new DefaultTableModel(
				new Object[][] {
						{null, null, null, null},
					},
					new String[] {
						"Nome", "Artista", "\u00C1lbum", "Dura\u00E7\u00E3o"
					}
				) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					boolean[] columnEditables = new boolean[] {
						false, false, false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				};
		tblTracks.setModel(tblTracksModel);
		tblTracks.getColumnModel().getColumn(0).setPreferredWidth(155);
		tblTracks.getColumnModel().getColumn(1).setPreferredWidth(118);
		tblTracks.getColumnModel().getColumn(2).setPreferredWidth(125);
		tblTracks.getColumnModel().getColumn(3).setPreferredWidth(52);
		tblTracks.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            if (tblTracks.getSelectedRow() > -1) {
	            	btnRemTrack.setEnabled(true);
	            	new Thread(new ImageConnectionThread(
	            			detailed.getTrackAt(tblTracks.getSelectedRow())
	            			.getAlbumImageUrl()))
	            	.start();
	            	btnAudioPreview_PlaylistDetails.setEnabled(true);
	            	selectedTrack = detailed.getTrackAt(tblTracks.getSelectedRow());
	            	if (tblTracks.getSelectedRow() > 0) btnParaCima.setEnabled(true);
	            	else btnParaCima.setEnabled(false);
	            	
	            	if (tblTracks.getSelectedRow() < (detailed.getSize() - 1) ) btnParaBaixo.setEnabled(true);
	            	else btnParaBaixo.setEnabled(false); 
	            } else {
	            	btnRemTrack.setEnabled(false);
	            	btnAudioPreview_PlaylistDetails.setEnabled(false);
	            	selectedTrack = null;
	            }
	        }
	    });
		tblTracks.setShowVerticalLines(false);
		tblTracks.setShowHorizontalLines(false);
		tblTracks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblTracks.setRowHeight(20);
		tblTracks.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tblTracks.setFillsViewportHeight(true);
		scrollPane_2.setViewportView(tblTracks);
		
		JPanel albumArtPanel = new JPanel();
		albumArtPanel.setLayout(null);
		albumArtPanel.setBounds(27, 233, 150, 150);
		cardPlaylistDetails.add(albumArtPanel);
		
		lblAlbumArt = new JLabel("");
		lblAlbumArt.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/no-image.png")));
		lblAlbumArt.setBounds(0, 0, 150, 150);
		albumArtPanel.add(lblAlbumArt);
		
		JPanel playlistInfoPanel = new JPanel();
		playlistInfoPanel.setBounds(27, 66, 150, 156);
		cardPlaylistDetails.add(playlistInfoPanel);
		playlistInfoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("2dlu"),
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("1dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("1dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("1dlu"),
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblCreationDateTip = new JLabel("Criada em");
		lblCreationDateTip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		playlistInfoPanel.add(lblCreationDateTip, "2, 2");
		
		lblPlaylistCreationDate = new JLabel("-");
		lblPlaylistCreationDate.setFont(new Font("Segoe UI", Font.BOLD, 14));
		playlistInfoPanel.add(lblPlaylistCreationDate, "2, 4");
		
		JLabel lblPlaylistSizeTip = new JLabel("# de faixas");
		lblPlaylistSizeTip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		playlistInfoPanel.add(lblPlaylistSizeTip, "2, 6");
		
		lblPlaylistSize = new JLabel("-");
		lblPlaylistSize.setFont(new Font("Segoe UI", Font.BOLD, 14));
		playlistInfoPanel.add(lblPlaylistSize, "2, 8");
		
		JLabel lblTotalDurationTip = new JLabel("Dura\u00E7\u00E3o total");
		lblTotalDurationTip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		playlistInfoPanel.add(lblTotalDurationTip, "2, 10");
		
		lblTotalDuration = new JLabel("-");
		lblTotalDuration.setFont(new Font("Segoe UI", Font.BOLD, 14));
		playlistInfoPanel.add(lblTotalDuration, "2, 12");
		
		btnRemTrack = new JButton("Remover");
		btnRemTrack.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/trash-can.png")));
		btnRemTrack.setEnabled(false);
		btnRemTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Track removing = detailed.getTrackAt(tblTracks.getSelectedRow());
				if (JOptionPane.showConfirmDialog(
						cardPlaylistDetails, "Tem certeza que deseja remover \"" + removing.getName() +"\"?") == 0) {
					detailed.removeTrack(removing);
					MainUI.this.modifyPlaylist(detailed);
					populatePlaylistDetails(detailed);
				}
			}
		});
		btnRemTrack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnRemTrack.setBounds(194, 394, 100, 30);
		cardPlaylistDetails.add(btnRemTrack);
		
		btnAdicFaixas = new JButton("Adic. faixas");
		btnAdicFaixas.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/plus.png")));
		btnAdicFaixas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardPlaylistDetails.setVisible(false);
				btnBackToPlaylists_cS.setText(detailed.getName());
				cardSearch.setVisible(true);
				verifyAuthorization();
			}
		});
		btnAdicFaixas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnAdicFaixas.setBounds(538, 394, 108, 30);
		cardPlaylistDetails.add(btnAdicFaixas);
		
		btnAudioPreview_PlaylistDetails = new JButton(NOT_PLAYING);
		btnAudioPreview_PlaylistDetails.setEnabled(false);
		btnAudioPreview_PlaylistDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isPlaying())
					stopPreview();
				else playPreview(selectedTrack);
			}
		});
		btnAudioPreview_PlaylistDetails.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/play.png")));
		btnAudioPreview_PlaylistDetails.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnAudioPreview_PlaylistDetails.setBounds(27, 394, 150, 30);
		cardPlaylistDetails.add(btnAudioPreview_PlaylistDetails);
		
		btnParaCima = new JButton("Para cima");
		btnParaCima.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/up.png")));
		btnParaCima.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Track movingUp = detailed.getTrackAt(tblTracks.getSelectedRow());
				detailed.moveTrackUp(movingUp);
				modifyPlaylist(detailed);
				//userPlaylists.set(userPlaylists.indexOf(detailed), detailed);
				populatePlaylistDetails(detailed);
				tblTracks.setRowSelectionInterval(detailed.indexOf(movingUp), detailed.indexOf(movingUp));
			}
		});
		btnParaCima.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnParaCima.setEnabled(false);
		btnParaCima.setBounds(304, 394, 108, 30);
		cardPlaylistDetails.add(btnParaCima);
		
		btnParaBaixo = new JButton("Para baixo");
		btnParaBaixo.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/down.png")));
		btnParaBaixo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Track movingDown = detailed.getTrackAt(tblTracks.getSelectedRow());
				detailed.moveTrackDown(movingDown);
				modifyPlaylist(detailed);
				//userPlaylists.set(userPlaylists.indexOf(detailed), detailed);
				populatePlaylistDetails(detailed);
				tblTracks.setRowSelectionInterval(detailed.indexOf(movingDown), detailed.indexOf(movingDown));
			}
		});
		btnParaBaixo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnParaBaixo.setEnabled(false);
		btnParaBaixo.setBounds(420, 394, 108, 30);
		cardPlaylistDetails.add(btnParaBaixo);
		
		/*JButton btnPlaylistDBTest = new JButton("");
		btnPlaylistDBTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TrackDB trackDB = new TrackDB();
				PlaylistDB playlistDB = new PlaylistDB();
				Iterator<Track> itr = detailed.getTracks().iterator();
				while (itr.hasNext()) {
					Track atual = itr.next();
					// Verifica se a faixa já está registrada no banco de dados
					if (trackDB.buscarPorID(atual.getId()) == null) {
						trackDB.inserir(atual);
					}
				}
				playlistDB.inserir(detailed);
			}
		})
		btnPlaylistDBTest.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/plus.png")));
		btnPlaylistDBTest.setToolTipText("Adicionar ao BD (Teste)");
		btnPlaylistDBTest.setBounds(143, 394, 34, 30);
		cardPlaylistDetails.add(btnPlaylistDBTest);*/
		
		cardSearch = new JPanel();
		cardLayout.add(cardSearch, "name_10654433773006");
		cardSearch.setLayout(null);
		
		JLabel lblPesquisarTitle = new JLabel("Pesquisar");
		lblPesquisarTitle.setForeground(new Color(30, 30, 30));
		lblPesquisarTitle.setBounds(27, 11, 156, 48);
		cardSearch.add(lblPesquisarTitle);
		lblPesquisarTitle.setFont(new Font("Segoe UI", Font.PLAIN, 32));
		
		tfSearchQuery = new JTextField();
		tfSearchQuery.setBounds(177, 28, 150, 25);
		cardSearch.add(tfSearchQuery);
		tfSearchQuery.setToolTipText("Digite sua pesquisa");
		tfSearchQuery.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tfSearchQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pesquisar();
			}
		});
		tfSearchQuery.setColumns(10);
		
		btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.setDisabledIcon(null);
		btnPesquisar.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/search-icon.png")));
		btnPesquisar.setBounds(337, 27, 125, 27);
		cardSearch.add(btnPesquisar);
		btnPesquisar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pesquisar();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 66, 300, 359);
		cardSearch.add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		searchList = new JList<Track>();
		searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		searchList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		searchList.addListSelectionListener(this);
		searchList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (previewPlayer != null) {
						previewPlayer.close();
						btnAudioPreview_Search.setText(NOT_PLAYING);
					}
					playPreview(searchList.getSelectedValue());
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (previewPlayer != null) {
						previewPlayer.close();
						btnAudioPreview_Search.setText(NOT_PLAYING);
					}
					playPreview(searchList.getSelectedValue());
				}
			}
		});
		scrollPane.setViewportView(searchList);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setForeground(SystemColor.activeCaptionBorder);
		infoPanel.setBorder(new LineBorder(null));
		infoPanel.setBounds(337, 231, 310, 194);
		cardSearch.add(infoPanel);
		infoPanel.setLayout(null);
		
		JLabel lblNomeTip = new JLabel("Nome:");
		lblNomeTip.setBounds(6, 6, 36, 16);
		lblNomeTip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		infoPanel.add(lblNomeTip);
		
		lblNome = new JLabel("-");
		lblNome.setBounds(6, 24, 303, 20);
		lblNome.setForeground(Color.DARK_GRAY);
		lblNome.setVerticalAlignment(SwingConstants.TOP);
		lblNome.setFont(new Font("Segoe UI", Font.BOLD, 14));
		infoPanel.add(lblNome);
		
		JLabel lblArtistTip = new JLabel("Artista:");
		lblArtistTip.setBounds(6, 52, 37, 16);
		lblArtistTip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		infoPanel.add(lblArtistTip);
		
		lblArtist = new JLabel("-");
		lblArtist.setFocusTraversalKeysEnabled(false);
		lblArtist.setBounds(6, 70, 303, 20);
		lblArtist.setForeground(Color.DARK_GRAY);
		lblArtist.setVerticalAlignment(SwingConstants.TOP);
		lblArtist.setFont(new Font("Segoe UI", Font.BOLD, 14));
		infoPanel.add(lblArtist);
		
		JLabel lblAlbumTip = new JLabel("Álbum:");
		lblAlbumTip.setBounds(6, 96, 39, 16);
		lblAlbumTip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		infoPanel.add(lblAlbumTip);
		
		lblAlbum = new JLabel("-");
		lblAlbum.setBounds(6, 112, 303, 20);
		lblAlbum.setForeground(Color.DARK_GRAY);
		lblAlbum.setVerticalAlignment(SwingConstants.TOP);
		lblAlbum.setFont(new Font("Segoe UI", Font.BOLD, 14));
		infoPanel.add(lblAlbum);
		
		lblDurationTip = new JLabel("Dura\u00E7\u00E3o:");
		lblDurationTip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblDurationTip.setBounds(6, 138, 57, 16);
		infoPanel.add(lblDurationTip);
		
		lblDuration = new JLabel("-");
		lblDuration.setVerticalAlignment(SwingConstants.TOP);
		lblDuration.setForeground(Color.DARK_GRAY);
		lblDuration.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblDuration.setBounds(6, 156, 57, 20);
		infoPanel.add(lblDuration);
		
		imagePanel = new JPanel();
		imagePanel.setBounds(337, 66, 150, 150);
		cardSearch.add(imagePanel);
		imagePanel.setLayout(null);
		
		lblAlbumImage = new JLabel("");
		lblAlbumImage.setBounds(0, 0, 150, 150);
		imagePanel.add(lblAlbumImage);
		lblAlbumImage.setIcon(iconDefault);
		
		JPanel actionsPanel = new JPanel();
		actionsPanel.setBounds(497, 66, 150, 150);
		cardSearch.add(actionsPanel);
		
		btnAudioPreview_Search = new JButton(NOT_PLAYING);
		btnAudioPreview_Search.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/play.png")));
		btnAudioPreview_Search.setBounds(5, 11, 140, 30);
		btnAudioPreview_Search.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnAudioPreview_Search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isPlaying())
					stopPreview();
				else if (searchList.getSelectedIndex() > -1) {
					playPreview(searchList.getSelectedValue());
				}
			}
		});
		btnAudioPreview_Search.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnAudioPreview_Search.setEnabled(false);
		
		btnAddTrack = new JButton("Adic. \u00E0 playlist");
		btnAddTrack.setBounds(5, 48, 140, 30);
		btnAddTrack.setEnabled(false);
		btnAddTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Track track = searchList.getSelectedValue();
				if (detailed == null) {
					System.out.println(track.toSimpleString());
					JDialog dialog = new AddTrackDialog(MainUI.this, MainUI.this.currentUser, userPlaylists, track);
					dialog.setVisible(true);
				} else {
					if (!detailed.addTrack(track)) {
						JOptionPane.showMessageDialog(cardSearch, "Esta faixa já existe na playlist "+ detailed.getName() +".");
					} else {
						addTrackToPlaylist(track, detailed);
						txtpnInfo.setText(track.getName() + " foi adicionada à \""+ detailed.getName() + "\"!");
					}
				}
			}
		});
		btnAddTrack.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/plus.png")));
		btnAddTrack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		actionsPanel.setLayout(null);
		actionsPanel.add(btnAudioPreview_Search);
		actionsPanel.add(btnAddTrack);
		
		txtpnInfo = new JTextPane();
		txtpnInfo.setBackground(SystemColor.menu);
		txtpnInfo.setBounds(5, 89, 135, 50);
		actionsPanel.add(txtpnInfo);
		
		btnBackToPlaylists_cS = new JButton("Playlists");
		btnBackToPlaylists_cS.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/back.png")));
		btnBackToPlaylists_cS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardSearch.setVisible(false);
				if (detailed == null) {
					updateUserPlaylistsTable();
					cardPlaylists.setVisible(true);
				} else {
					populatePlaylistDetails(detailed);
					cardPlaylistDetails.setVisible(true);
				}
			}
		});
		btnBackToPlaylists_cS.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnBackToPlaylists_cS.setBounds(499, 25, 147, 30);
		cardSearch.add(btnBackToPlaylists_cS);		
		iconDefault = new ImageIcon(getClass().getClassLoader().getResource("res/no-image.png"));
		playlistDB = new PlaylistDB();
		updateUserPlaylistsTable();	
	} // Fim do MainUI()
	
	
	/*
	 * Métodos receivers
	 */
	
	/**
	 * Responsável por receber de uma thread {@link ImageConnectionThread} 
	 * a {@link Image} da faixa selecionada e mostrá-la na UI.
	 * 
	 * @param albumImage
	 * 			A {@link Image} retornada por uma thread {@link ImageConnectionThread}
	 */
	public static void albumImageReceiver(Image albumImage) {
		MainUI.lblAlbumImage.setIcon(new ImageIcon(albumImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		MainUI.lblAlbumArt.setIcon(new ImageIcon(albumImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
	}
	
	/**
	 * Responsável por receber de uma thread {@link AudioPreviewConnectionThread} 
	 * o {@link AdvancedPlayer} da faixa a ser reproduzida e e executá-lo, fazendo 
	 * as devidas mudanças na UI.
	 * 
	 * @param player
	 * 			O {@link AdvancedPlayer} retornado por uma thread {@link AudioPreviewConnectionThread}
	 * 			contendo a prévia da faixa a ser reproduzida 
	 */
	public static void playerReceiver(AdvancedPlayer player) {
		previewPlayer = player;
		previewPlayer.setPlayBackListener(new PlaybackListener() {
			public void playbackFinished(PlaybackEvent e) {
				btnAudioPreview_Search.setText(NOT_PLAYING);
				btnAudioPreview_Search.setIcon(MainUI.ICON_PLAY);
				btnAudioPreview_PlaylistDetails.setText(NOT_PLAYING);
				btnAudioPreview_PlaylistDetails.setIcon(MainUI.ICON_PLAY);
			}
		});
		try {
			btnAudioPreview_Search.setEnabled(true);
			btnAudioPreview_Search.setText(PLAYING);
			btnAudioPreview_Search.setIcon(ICON_STOP);
			btnAudioPreview_PlaylistDetails.setEnabled(true);
			btnAudioPreview_PlaylistDetails.setText(PLAYING);
			btnAudioPreview_PlaylistDetails.setIcon(ICON_STOP);
			previewPlayer.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Responsável por receber de uma instância de {@link AddTrackDialog} a {@link Track}
	 * a ser adicionada e a {@link Playlist} para a qual a faixa será adicionada. Este
	 * método será chamado depois dousuário adicionar uma faixa a uma playlist usando o
	 * {@link AddTrackDialog}.
	 * 
	 * @param track
	 * 			A {@link Track} que será adicionada
	 * @param playlist
	 * 			A {@link Playlist} para a qual a faixa será adicionada
	 */
	public static void addTrackReceiver(Track track, Playlist playlist) {
		MainUI.addTrackToPlaylist(track, playlist);
		txtpnInfo.setText(track.getName() + " foi adicionada à \""+ playlist.getName() + "\"!");
	}
	
	/**
	 * Responsável por receber de uma thread {@link SearchConnectionThread} 
	 * uma {@link ArrayList} de {@link Track} contendo os resultados de 
	 * pesquisa, além de mostrá-los na UI.
	 *  
	 * @param searchResultsArrayList
	 * 			Uma {@link ArrayList} contendo as {@link Track}s retornadas pela pesquisa
	 */
	public static void searchResultsReceiver(ArrayList<Track> searchResultsArrayList) {
		searchResultsArray = searchResultsArrayList;
		if (searchResultsArray == null) {
			JOptionPane.showMessageDialog(cardSearch, "Houve um erro ao pesquisar. Verifique sua conexão.");
		} else {
			Track[] resultsArray = searchResultsArrayList.toArray(new Track[0]);
			DefaultListModel<Track> model = new DefaultListModel<Track>();
			int i = 0;
			for (Track linha : resultsArray) {
				model.add(i++, linha);
			}
			searchList.setModel(model);
		}
		searchList.setEnabled(true);
		tfSearchQuery.setEnabled(true);
		btnPesquisar.setEnabled(true);
		btnPesquisar.setText("Pesquisar");
		btnAudioPreview_Search.setEnabled(false);
	}
	
	
	/*
	 * Métodos de reprodução de áudio
	 */
	
	/**
	 * Este método inicia uma thread {@link AudioPreviewConnectionThread} em busca
	 * do aúdio de prévia da {@link Track} recebida pelo mesmo.
	 * O áudio será recebido pela MainUI através do método {@linkplain playerReceiver()}. 
	 * 
	 * @param track
	 * 			A faixa cuja prévia será acessada través da {@link AudioPreviewConnectionThread}
	 */
	private void playPreview(Track track) {
		URL previewUrl = track.getPreviewUrl();
		new Thread(new AudioPreviewConnectionThread(previewUrl)).start();
		btnAudioPreview_Search.setEnabled(false);
		btnAudioPreview_PlaylistDetails.setEnabled(false);
	}
	
	/**
	 * Retorna o estado de execução do player de prévia de faixas.
	 * 
	 * @return 	<code>true</code> se o player estiver sendo executado,
	 * 		   	<code>false</code> se o player estiver nulo ou não
	 * 			estiver sendo executado
	 */
	private boolean isPlaying() {
		if (btnAudioPreview_Search.getText().equals(PLAYING) || btnAudioPreview_PlaylistDetails.getText().equals(PLAYING)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Interrompe a execução do áudio de prévia da faixa sendo executada,
	 * realizando as devidas alterações na UI.
	 * 
	 * @return 	<code>true</code> se o áudio foi interrompido com sucesso,
	 * 			e <code>false</code> se não for o caso
	 */
	private boolean stopPreview() {
		if (previewPlayer != null) {
			if (isPlaying()) {
				previewPlayer.close();
				btnAudioPreview_Search.setText(NOT_PLAYING);
				btnAudioPreview_Search.setIcon(MainUI.ICON_PLAY);
				btnAudioPreview_PlaylistDetails.setText(NOT_PLAYING);
				btnAudioPreview_PlaylistDetails.setIcon(MainUI.ICON_PLAY);
			}
			return true;
		}
		return false;
	}
	
	
	/*
	 *  Métodos da tela de Detalhes da Playlist (cardPlaylistDetails)
	 */
	
	/**
	 * Responsável por atualizar a tela de detalhes da playlist 
	 * (<code>cardPlaylistDetails</code>) com as informações da
	 * {@link Playlist} recebida como parâmetro.
	 * 
	 * @param playlist
	 * 			- A playlist a ser detalhada
	 */
	private void populatePlaylistDetails(Playlist playlist) {
		lblPlaylistNameTitle.setText(playlist.getName());
		while (this.tblTracksModel.getRowCount() > 0) {
			this.tblTracksModel.removeRow(0);
		}
		for (int i = 0; i < playlist.getSize(); i++) {
			Track t = playlist.getTrackAt(i);
			tblTracksModel.addRow(new Object[] {
				t.getName(),
				t.getArtist(),
				t.getAlbum(),
				Spotify.msToMinSec(t.getDurationMs())
			});
			//tblTracks.setModel(modelo);
		}
		lblPlaylistCreationDate.setText(playlist.getCreationDateAndTime());
		lblPlaylistSize.setText(String.valueOf(playlist.getSize()));
		lblTotalDuration.setText(Spotify.msToMinSec(playlist.getTotalDuration()));
		lblAlbumArt.setIcon(iconDefault);
		
		cardPlaylists.setVisible(false);
		cardPlaylistDetails.setVisible(true);
	}
	
	
	/*
	 * Métodos de acesso ao DB
	 */
	
	/**
	 * Responsável por atualizar a tabela <code>tblPlaylists</code> da tela
	 * de playlists (<code>cardPlaylists</code>) com os dados do BD.
	 */
	private void updateUserPlaylistsTable() {
		userPlaylists = (ArrayList<Playlist>) playlistDB.buscarTodosDe(currentUser);
		/*while (tblPlaylistsModel.getRowCount() > 0) {
			tblPlaylistsModel.removeRow(0);
		}
		DefaultTableModel modelo = (DefaultTableModel) tblPlaylists.getModel();*/
		this.tblPlaylistsModel.setRowCount(0);
		if (userPlaylists != null) {
			Iterator<Playlist> itr = userPlaylists.iterator();
			while (itr.hasNext()) {
				Playlist p = itr.next();
				this.tblPlaylistsModel.addRow(new Object[] {
						p, 
						p.getSize(), 
						Spotify.msToMinSec(p.getTotalDuration()),
						p.getCreationDate()});
			}
			tblPlaylists.setModel(tblPlaylistsModel);
		}
	}
	
	/**
	 * Responsável por adicionar uma nova playlist ao BD contendo apenas
	 * o nome.
	 * @param name - o nome da nova playlist
	 */
	private void addPlaylist(String name) {
		new PlaylistDB().inserir(new Playlist(name, currentUser));
	}
	
	/**
	 * Responsável por excluir do DB a {@link Playlist} recebida como
	 * parâmetro.
	 * 
	 * @param playlist - a {@link Playlist} a ser removida do BD
	 */
	private void removePlaylist(Playlist playlist) {
		new PlaylistDB().excluir(playlist);
	}
	
	private void modifyPlaylist(Playlist playlist) {
		new PlaylistDB().modificar(playlist);
	}
	
	private static void addTrackToPlaylist(Track track, Playlist playlist) {
		TrackDB trackDB = new TrackDB();
		System.out.println(track.getPreviewUrl());
		if (!trackDB.jaExiste(track.getId())) {
			trackDB.inserir(track);
		}
		
		playlist.addTrack(track);
		PlaylistDB pdb = new PlaylistDB();
		if (pdb.jaExiste(playlist.getId())) {
			pdb.modificar(playlist);
		} else {
			pdb.inserir(playlist);
		}
		if (userPlaylists != null || userPlaylists.size() > 0) {
			userPlaylists.set(userPlaylists.indexOf(playlist), playlist);
		} else {
			if (userPlaylists == null) userPlaylists = new ArrayList<Playlist>();
			userPlaylists.add(playlist);
		}
	}
	
	/*public static void updatePlaylistDB(Playlist playlist) {
		TrackDB trackDB = new TrackDB();
		PlaylistDB playlistDB = new PlaylistDB();
		Iterator<Track> itr = playlist.getTracks().iterator();
		while (itr.hasNext()) {
			Track atual = itr.next();
			// Verifica se a faixa já está registrada no banco de dados
			if (trackDB.buscarPorID(atual.getId()) == null) {
				trackDB.inserir(atual);
			}
		}
		
		if (playlistDB.buscarPorID(playlist.getId()) == null) {
			playlistDB.inserir(playlist);
		} else {
			playlistDB.modificar(playlist);
		}
		
	}*/
	
	
	/* 
	 * Métodos para a tela de pesquisa
	 */
	
	/**
	 * Realiza a pesquisa online de faixas no Spotify usando o
	 * texto inserido na caixa de pesquisa <code>tfSearchQuery</code>
	 * e iniciando uma nova thread {@link SearchConnectionThread}. </br>
	 * Os resultados da pesquisa são recebidos pela MainUI através
	 * do método <code>searchResultsReceiver()</code>.
	 */
	private void pesquisar() {
		String searchQuery = tfSearchQuery.getText();
		if (!searchQuery.equals("")) {
			tfSearchQuery.setEnabled(false);
			searchList.setEnabled(false);
			btnPesquisar.setText("Pesquisando...");
			btnPesquisar.setEnabled(false);
			connectionThread = new SearchConnectionThread(searchQuery);
			new Thread(connectionThread).start();
		}
	}
	
	/**
	 * Este método é responsável por escutar mudanças de valor na
	 * {@link JList} <code>searchList</code>, realizando as devidas
	 * alterações na UI de acordo com o item selecionado.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			 
            if (searchList.getSelectedIndex() != -1) {
            	if (searchResultsArray != null) {
            		Track selected = searchResultsArray.get(searchList.getSelectedIndex());
            		lblNome.setText(selected.getName());
            		lblNome.setToolTipText(selected.getName());
            		lblArtist.setText(selected.getArtist());
            		lblArtist.setToolTipText(selected.getArtist());
            		lblAlbum.setText(selected.getAlbum());
            		lblAlbum.setToolTipText(selected.getAlbum());
            		lblDuration.setText(Spotify.msToMinSec(selected.getDurationMs()));
            		lblAlbumImage.setIcon(iconDefault);
            		new Thread(new ImageConnectionThread(selected.getAlbumImageUrl())).start();
            		if (selected.getPreviewUrl() != null) {
            			btnAudioPreview_Search.setEnabled(true);
            			btnAudioPreview_Search.setToolTipText(null);
            		}
            		else {
            			btnAudioPreview_Search.setEnabled(false);
            			btnAudioPreview_Search.setToolTipText("Prévia não disponível");
            		}
            		/*if (previewPlayer != null) {
            			previewPlayer.close();
            			previewPlayer = null;
            			btnAudioPreview_Search.setText(NOT_PLAYING);
            		}*/
            		btnAddTrack.setEnabled(true);
            		//btnTrackDBTest.setEnabled(true);
            	}
            	
            } else {
                lblNome.setText("-");
                lblAlbum.setText("-");
                lblArtist.setText("-");
                lblDuration.setText("-");
                lblAlbumImage.setIcon(iconDefault);
                btnAudioPreview_Search.setEnabled(false);
            }
        }
	}
	
	public static String requestAuthorizationCode() {
		String code = (String) JOptionPane.showInputDialog(
				cardSearch, 
				"Agora faça login na sua conta do Spotify pelo navegador. "
				+ "Ao final do processo, cole aqui o código de autorização:", 
				"Código de autorização", 
				JOptionPane.PLAIN_MESSAGE);
		return code;
	}
	
	public void verifyAuthorization() {
		if (!spotifyApi.isAuth()) {
			JOptionPane.showMessageDialog(
					cardSearch, 
					"Para pesquisar faixas no Spotify, você deve fazer login primeiro. "
					+ "Clique em OK para abrir a página de login.");
			spotifyApi.authorizeFlow();
		}
	}
	
	public static void showMessage(String message) {
		JOptionPane.showMessageDialog(cardSearch, message);
	}
	
	public static SpotifyAuthorizationFlow getSpotifyApi() {
		return spotifyApi;
	}
	
}


