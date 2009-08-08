//    jDownloader - Downloadmanager
//    Copyright (C) 2009  JD-Team support@jdownloader.org
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jd.gui.swing.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jd.gui.UserIO;
import jd.gui.swing.Factory;
import jd.gui.swing.components.linkbutton.JLink;
import jd.gui.swing.dialog.AbstractDialog;
import jd.gui.swing.jdgui.userio.UserIOGui;
import jd.nutils.JDImage;
import jd.nutils.io.JDIO;
import jd.utils.JDTheme;
import jd.utils.JDUtilities;
import jd.utils.locale.JDL;
import net.miginfocom.swing.MigLayout;

public class AboutDialog extends AbstractDialog {

    public static void main(String[] args) {
        UserIO.setInstance(UserIOGui.getInstance());
        new AboutDialog();
    }

    private static final String JDL_PREFIX = "jd.gui.swing.components.AboutDialog.";

    public AboutDialog() {
        super(UserIO.NO_COUNTDOWN | UserIO.NO_OK_OPTION | UserIO.NO_CANCEL_OPTION, JDL.L(JDL_PREFIX + "title", "About JDownloader"), null, null, null);

        init();
    }

    private static final long serialVersionUID = -7647771640756844691L;

    public JComponent contentInit() {
        JPanel cp = new JPanel(new MigLayout("ins 10 10 0 10, wrap 2"));

        cp.add(new JLabel(JDImage.getImageIcon("logo/jd_logo_128_128")), "spany 3, gapright 10");

        JLabel lbl;
        cp.add(lbl = new JLabel(JDL.L(JDL_PREFIX + "name", "JDownloader")), "gaptop 15");
        lbl.setFont(lbl.getFont().deriveFont(lbl.getFont().getSize() * 2.0f));

        cp.add(new JLabel(JDL.LF(JDL_PREFIX + "version", "Version %s", JDUtilities.getRevision())));

        cp.add(new JLabel("© AppWork UG (haftungsbeschränkt) 2007-2009"), "gaptop 5");

        JButton btn;
        cp.add(btn = Factory.createButton(JDL.L(JDL_PREFIX + "license", "Show license"), JDTheme.II("gui.images.premium", 16, 16), new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String license = JDIO.getLocalFile(JDUtilities.getResourceFile("licenses/jdownloader.license"));
                UserIO.getInstance().requestConfirmDialog(UserIO.NO_CANCEL_OPTION | UserIO.STYLE_LARGE | UserIO.NO_ICON, JDL.L(JDL_PREFIX + "license.title", "JDownloader License"), license, null, null, null);
            }

        }), "gaptop 15, spanx, split 4");
        btn.setBorder(null);

        try {
            cp.add(new JLink(JDL.L(JDL_PREFIX + "homepage", "Homepage"), JDTheme.II("gui.images.config.host", 16, 16), new URL("http://www.jdownloader.org/home?lng=en")), "gapleft 10");
            cp.add(new JLink(JDL.L(JDL_PREFIX + "forum", "Support board"), JDTheme.II("gui.images.list", 16, 16), new URL("http://board.jdownloader.org")), "gapleft 10");
            cp.add(new JLink(JDL.L(JDL_PREFIX + "contributers", "Contributers"), JDTheme.II("gui.images.accounts", 16, 16), new URL("http://jdownloader.org/knowledge/wiki/contributers")), "gapleft 10");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        return cp;
    }

    protected void packed() {
        this.remove(countDownLabel);
        this.pack();
        this.setDefaultCloseOperation(AbstractDialog.DISPOSE_ON_CLOSE);
    }

}
