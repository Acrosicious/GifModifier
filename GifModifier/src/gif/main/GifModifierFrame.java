package gif.main;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.JTextField;


/**
 * @author macros @ Imgur
 *
 */
public class GifModifierFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GifModifierPanel modPanel;
	private JButton saveBtn;

	public GifModifierFrame() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		modPanel = new GifModifierPanel();
		getContentPane().add(modPanel, BorderLayout.CENTER);
		
		JPanel toolBar = new JPanel();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton openBtn = new JButton("Open Gif");
		openBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showOpenDialogue();
			}
		});
		toolBar.add(openBtn);
		
		saveBtn = new JButton("Save Gif");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveDialogue();
			}
		});
		saveBtn.setEnabled(false);
		toolBar.add(saveBtn);
		
		JLabel fpsLabel = new JLabel("FPS:");
		toolBar.add(fpsLabel);
		
		JSlider fpsSlider = new JSlider();
		fpsSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				modPanel.setFPS(fpsSlider.getValue());
			}
		});
		fpsSlider.setValue(20);
		fpsSlider.setMaximum(60);
		fpsSlider.setMinimum(1);
		toolBar.add(fpsSlider);
		
		JLabel tileLabel = new JLabel("Tile size:");
		toolBar.add(tileLabel);
		
		JTextField tileField = new JTextField();		
		((AbstractDocument) tileField.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String s, AttributeSet a) throws BadLocationException
			{
				fb.insertString(offset, s.replaceAll("\\D++", ""), a);
			}

			@Override
			public void replace(FilterBypass fb, int offset, int l, String s, AttributeSet a) throws BadLocationException 
			{
				fb.replace(offset, l, s.replaceAll("\\D++", ""), a);
			}
		});		
		tileField.setColumns(3);
		tileField.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				int input = Integer.parseInt(tileField.getText());
				input = input < 10 ? 10 : input > 50 ? 50 : input;
				modPanel.setTiling(input);
			}
		});
		toolBar.add(tileField);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	private void showOpenDialogue() {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Gif Images", "gif");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			modPanel.loadGif(fc.getSelectedFile());
			saveBtn.setEnabled(true);
			pack();
		}
	}
	
	private void showSaveDialogue() {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Gif Images", "gif");
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File f;
			if(!fc.getSelectedFile().getName().endsWith(".gif")) {
				f = new File(fc.getSelectedFile() + ".gif");
			}else {
				f = fc.getSelectedFile();
			}
			modPanel.saveGif(f);
		}
	}
	
	
}
