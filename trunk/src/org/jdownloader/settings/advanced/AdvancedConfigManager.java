package org.jdownloader.settings.advanced;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import jd.controlling.linkchecker.LinkCheckerConfig;
import jd.controlling.linkcollector.LinkCollectorConfig;
import jd.controlling.linkcrawler.LinkCrawlerConfig;
import jd.controlling.reconnect.ReconnectConfig;

import org.appwork.storage.config.ConfigInterface;
import org.appwork.storage.config.ConfigUtils;
import org.appwork.storage.config.JsonConfig;
import org.appwork.storage.config.annotations.AboutConfig;
import org.appwork.storage.config.handler.KeyHandler;
import org.appwork.utils.logging.Log;
import org.jdownloader.api.RemoteAPIConfig;
import org.jdownloader.controlling.filter.LinkFilterSettings;
import org.jdownloader.controlling.packagizer.PackagizerSettings;
import org.jdownloader.gui.views.linkgrabber.addlinksdialog.LinkgrabberSettings;
import org.jdownloader.settings.AccountSettings;
import org.jdownloader.settings.GeneralSettings;
import org.jdownloader.settings.GraphicalUserInterfaceSettings;
import org.jdownloader.settings.InternetConnectionSettings;
import org.jdownloader.update.WebupdateSettings;

public class AdvancedConfigManager {
    private static final AdvancedConfigManager INSTANCE = new AdvancedConfigManager();

    public static AdvancedConfigManager getInstance() {
        return AdvancedConfigManager.INSTANCE;
    }

    private ArrayList<AdvancedConfigEntry> configInterfaces;
    private AdvancedConfigEventSender      eventSender;

    private AdvancedConfigManager() {
        configInterfaces = new ArrayList<AdvancedConfigEntry>();
        eventSender = new AdvancedConfigEventSender();
        this.register(JsonConfig.create(GeneralSettings.class));
        register(JsonConfig.create(LinkFilterSettings.class));
        register(JsonConfig.create(InternetConnectionSettings.class));
        register(JsonConfig.create(AccountSettings.class));
        register(JsonConfig.create(GraphicalUserInterfaceSettings.class));
        register(JsonConfig.create(LinkCheckerConfig.class));
        register(JsonConfig.create(LinkCrawlerConfig.class));
        register(JsonConfig.create(LinkgrabberSettings.class));
        register(JsonConfig.create(LinkCollectorConfig.class));
        register(JsonConfig.create(ReconnectConfig.class));
        register(JsonConfig.create(RemoteAPIConfig.class));
        register(JsonConfig.create(PackagizerSettings.class));
        register(JsonConfig.create(WebupdateSettings.class));

    }

    public static void main(String[] args) {
        Log.L.setLevel(Level.OFF);
        ConfigUtils.printStaticMappings(LinkgrabberSettings.class);
    }

    public AdvancedConfigEventSender getEventSender() {
        return eventSender;
    }

    public void register(ConfigInterface cf) {

        HashMap<KeyHandler, Boolean> map = new HashMap<KeyHandler, Boolean>();

        for (KeyHandler m : cf.getStorageHandler().getMap().values()) {

            if (map.containsKey(m)) continue;

            if (m.getAnnotation(AboutConfig.class) != null) {
                if (m.getSetter() == null) {
                    throw new RuntimeException("Setter for " + m.getGetter().getMethod() + " missing");
                } else if (m.getGetter() == null) {
                    throw new RuntimeException("Getter for " + m.getSetter().getMethod() + " missing");
                } else {
                    synchronized (configInterfaces) {
                        configInterfaces.add(new AdvancedConfigInterfaceEntry(cf, m));
                    }
                    map.put(m, true);
                }
            }

        }

        eventSender.fireEvent(new AdvancedConfigEvent(this, AdvancedConfigEvent.Types.UPDATED, cf));
    }

    public ArrayList<AdvancedConfigEntry> list() {
        synchronized (configInterfaces) {
            return new ArrayList<AdvancedConfigEntry>(configInterfaces);
        }
    }

}
