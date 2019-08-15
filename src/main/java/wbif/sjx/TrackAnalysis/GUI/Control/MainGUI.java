package wbif.sjx.TrackAnalysis.GUI.Control;

import ij.ImagePlus;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;

public class MainGUI
{
    public static int frameWidth = 300;
    public static int elementHeight = 30;

    private final JScrollPane modulePanel;
    private final JFrame frame;

    private final TrackCollection tracks;
    private final ImagePlus ipl;

    public MainGUI(TrackCollection tracks, ImagePlus ipl)
    {
        this.tracks = tracks;
        this.ipl = ipl;

        modulePanel = new JScrollPane();
        modulePanel.setBorder(null);
        frame = new JFrame();
        frame.setVisible(false);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        frame.setTitle("Track analysis (v" + getClass().getPackage().getImplementationVersion() + ")");

        JComboBox<Modules> comboBox = new JComboBox<>(Modules.getAvailableModules(ipl != null));
        comboBox.setPreferredSize(new Dimension(frameWidth, elementHeight));
        comboBox.addActionListener(e ->
        {
            Modules module = (Modules) comboBox.getSelectedItem();
            changeModule(module);
        });

        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,5,0,5);
        frame.add(comboBox, c);

        c.gridy++;
        frame.add(modulePanel, c);

        changeModule((Modules) comboBox.getSelectedItem());
        centre();
        move(300, 0);
    }

    private void changeModule(Modules module)
    {
        BasicModule control = module.create(tracks, ipl);

        modulePanel.setViewportView(control.getPanel());
        modulePanel.repaint();
        modulePanel.revalidate();

        frame.pack();
    }

    public boolean isVisible()
    {
        return frame.isVisible();
    }

    public void hide()
    {
        frame.setVisible(false);
    }

    public void show()
    {
        frame.setVisible(true);
        frame.toFront();
    }

    public void centre()
    {
        frame.setLocationRelativeTo(null);
    }

    public void setPosition(int x, int y)
    {
        frame.setLocation(x, y);
    }

    public void move(int dx, int dy)
    {
        frame.setLocation(frame.getX() + dx, frame.getY() + dy);
    }

    public void dispose()
    {
        this.hide();
        frame.dispose();
    }
}
