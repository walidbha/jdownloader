package org.jdownloader.gui.views.linkgrabber.quickfilter;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;

import jd.controlling.packagecontroller.AbstractPackageChildrenNode;
import jd.controlling.packagecontroller.AbstractPackageNode;
import jd.gui.swing.laf.LookAndFeelController;

import org.appwork.swing.exttable.ExtTable;
import org.jdownloader.gui.views.components.packagetable.PackageControllerTableModelFilter;

public class FilterTable<E extends AbstractPackageNode<V, E>, V extends AbstractPackageChildrenNode<E>> extends ExtTable<Filter<E, V>> implements PackageControllerTableModelFilter<E, V> {

    /**
     * 
     */
    private static final long         serialVersionUID       = -5917220196056769905L;
    protected ArrayList<Filter<E, V>> filters                = new ArrayList<Filter<E, V>>();
    protected boolean                 filtersEnabledGlobally = false;

    public FilterTable() {
        super(new FilterTableModel<E, V>());

        this.setShowVerticalLines(false);
        this.setShowGrid(false);
        this.setShowHorizontalLines(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setRowHeight(22);

        int c = LookAndFeelController.getInstance().getLAFOptions().getPanelHeaderColor();
        Color b2;
        Color f2;
        if (c >= 0) {
            b2 = new Color(c);
            f2 = new Color(LookAndFeelController.getInstance().getLAFOptions().getPanelHeaderForegroundColor());
        } else {
            b2 = getForeground();
            f2 = getBackground();
        }
        this.setBackground(new Color(LookAndFeelController.getInstance().getLAFOptions().getPanelBackgroundColor()));
        // this.addRowHighlighter(new SelectionHighlighter(null, b2));
        // this.getExtTableModel().addExtComponentRowHighlighter(new
        // ExtComponentRowHighlighter<T>(f2, b2, null) {
        //
        // @Override
        // public boolean accept(ExtColumn<T> column, T value, boolean selected,
        // boolean focus, int row) {
        // return selected;
        // }
        //
        // });

        // this.addRowHighlighter(new AlternateHighlighter(null,
        // ColorUtils.getAlphaInstance(new JLabel().getForeground(), 6)));
        this.setIntercellSpacing(new Dimension(0, 0));
    }

    @Override
    public void setVisible(boolean aFlag) {
        filtersEnabledGlobally = aFlag;
        super.setVisible(aFlag);
    }

    public boolean isFiltered(E e) {
        if (filtersEnabledGlobally == false) return false;
        ArrayList<Filter<E, V>> lfilters = filters;
        for (Filter<E, V> filter : lfilters) {
            if (!filter.isEnabled()) continue;
            if (filter.isFiltered(e)) return true;
        }
        return false;
    }

    public boolean isFiltered(V v) {
        if (filtersEnabledGlobally == false) return false;
        ArrayList<Filter<E, V>> lfilters = filters;
        for (Filter<E, V> filter : lfilters) {
            if (!filter.isEnabled()) continue;
            if (filter.isFiltered(v)) return true;
        }
        return false;
    }

}