package com.liddack.playlistsapp.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.liddack.playlistsapp.dominio.User;
import com.liddack.playlistsapp.dominio.Playlist;
import com.liddack.playlistsapp.dominio.Track;
import javax.swing.DefaultComboBoxModel;

public class AddTrackDialog extends JDialog {

	private JComboBox<Playlist> playlistsComboBox;
	private DefaultComboBoxModel<Playlist> model;
	private JDialog dialog;
	private List<Playlist> playlists;

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public AddTrackDialog(Frame owner, final User currentUser, List<Playlist> playlists, final Track track) {
		super(owner);
		this.dialog = this;
		setTitle("Adicionar faixa a uma playlist");
		setResizable(false);
		this.playlists = playlists;
		setModal(true);
		setBounds(100, 100, 213, 175);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblSelectPlaylist = new JLabel("Selecione a playlist:");
		lblSelectPlaylist.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSelectPlaylist.setBounds(10, 11, 157, 15);
		contentPanel.add(lblSelectPlaylist);
		
		final JPanel panel = new JPanel();
		panel.setBounds(10, 36, 187, 64);
		contentPanel.add(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("187px"),},
			new RowSpec[] {
				RowSpec.decode("22px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("30px"),}));
		
		playlistsComboBox = new JComboBox<Playlist>();
		panel.add(playlistsComboBox, "1, 1");
		playlistsComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		JButton btnNovaPlaylist = new JButton("Nova playlist");
		btnNovaPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = (String) JOptionPane.showInputDialog(
						panel, 
						"Digite o nome da playlist:", 
						"Escolha o nome", 
						JOptionPane.PLAIN_MESSAGE);
				if (name != null && !name.equals("")) {
					Playlist newPlaylist = new Playlist(name, currentUser);
					if (AddTrackDialog.this.playlists == null) {
						AddTrackDialog.this.playlists = new ArrayList<Playlist>();
					}
					AddTrackDialog.this.playlists.add(newPlaylist);
					populatePlaylistsComboBox();
				}
				
			}
		});
		btnNovaPlaylist.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnNovaPlaylist.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnNovaPlaylist.setIcon(new ImageIcon(getClass().getClassLoader().getResource("res/plus.png")));
		btnNovaPlaylist.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		panel.add(btnNovaPlaylist, "1, 3, center, fill");
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				/*
				 * Botão OK
				 */
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (playlistsComboBox.getSelectedIndex() == 0) {
							// Se a opção padrão foi selecionada, mostra um diálogo
							// pedindo pro usuário selecionar uma playlist
							JOptionPane.showMessageDialog(dialog, "Escolha uma playlist");
						} else {
							// Pega a playlist selecionada
							Playlist selected = (Playlist) playlistsComboBox.getSelectedItem();
							//Se a faixa já existir na playlist
							if (!selected.addTrack(track)) {
								JOptionPane.showMessageDialog(dialog, "Esta faixa já existe na playlist "+ selected.getName() +".");
							} else {
								// Retorna a playlist selecionada para a MainUI
								MainUI.addTrackReceiver(track, selected);
								dialog.dispose();
							}
						}
					}
				});
				okButton.setMaximumSize(new Dimension(47, 30));
				okButton.setPreferredSize(new Dimension(50, 30));
				okButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setMinimumSize(new Dimension(80, 23));
				cancelButton.setPreferredSize(new Dimension(80, 30));
				cancelButton.setMaximumSize(new Dimension(80, 30));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dialog.dispose();
					}
				});
				cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
				cancelButton.setActionCommand("Cancelar");
				buttonPane.add(cancelButton);
			}
		}
		populatePlaylistsComboBox();
		playlistsComboBox.setSelectedIndex(0);
	}
	
	private void populatePlaylistsComboBox() {
		/* Pega a lista de todas as playlists existentes
		 * no banco de dados. */
		playlistsComboBox.removeAllItems(); // Limpa a combobox
		model = new DefaultComboBoxModel<Playlist>();
		model.addElement(new Playlist("-- Selecione --", new User()));
		if (playlists != null && playlists.size() > 0) {
			Playlist[] playlistsArray = playlists.toArray(new Playlist[0]);
			for (Playlist atual : playlistsArray) {
				model.addElement(atual);
			}
		}
		playlistsComboBox.setModel(model);
		
	}
}
