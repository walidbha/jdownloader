package org.jdownloader.extensions.captchapush;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import jd.config.Configuration;
import jd.config.SubConfiguration;
import jd.controlling.captcha.CaptchaEventSender;
import jd.gui.swing.jdgui.menu.MenuAction;
import jd.plugins.AddonPanel;

import org.appwork.storage.config.JsonConfig;
import org.jdownloader.extensions.AbstractConfigPanel;
import org.jdownloader.extensions.AbstractExtension;
import org.jdownloader.extensions.StartException;
import org.jdownloader.extensions.StopException;

public class CaptchaPushExtension extends AbstractExtension {

    private CaptchaPushConfig      config;
    private CaptchaPushConfigPanel configPanel;
    private MenuAction             activateAction;

    private CaptchaPushService     service;

    private int                    oldValue;

    public CaptchaPushExtension() {
        super("Captcha Push");
    }

    @Override
    public String getIconKey() {
        return "gui.images.config.ocr";
    }

    @Override
    protected void stop() throws StopException {
        if (activateAction.isSelected()) stopService();
    }

    @Override
    protected void start() throws StartException {
        if (activateAction.isSelected()) startService();
    }

    private void startService() {
        logger.info("Start the MQTT Service ...");
        logger.info("Broker " + config.getBrokerHost() + ":" + config.getBrokerPort() + " on Topic " + config.getBrokerTopic());

        service.connect();

        CaptchaEventSender.getInstance().addListener(service);

        oldValue = SubConfiguration.getConfig("JAC").getIntegerProperty(Configuration.JAC_SHOW_TIMEOUT);
        if (oldValue < config.getTimeout()) {
            SubConfiguration.getConfig("JAC").setProperty(Configuration.JAC_SHOW_TIMEOUT, config.getTimeout());
            SubConfiguration.getConfig("JAC").save();
        } else {
            oldValue = -1;
        }
    }

    private void stopService() {
        logger.info("Stop the MQTT Service ...");

        service.disconnect();

        CaptchaEventSender.getInstance().removeListener(service);

        if (oldValue != -1) {
            SubConfiguration.getConfig("JAC").setProperty(Configuration.JAC_SHOW_TIMEOUT, oldValue);
            SubConfiguration.getConfig("JAC").save();
        }
    }

    @Override
    protected void initExtension() throws StartException {
        config = JsonConfig.create(CaptchaPushConfig.class);
        configPanel = new CaptchaPushConfigPanel(this, config);

        activateAction = new MenuAction("captchapush", 0) {
            private static final long serialVersionUID = 3252473048646596851L;

            @Override
            public void onAction(ActionEvent e) {
                if (isSelected()) {
                    startService();
                } else {
                    stopService();
                }
                config.setSelected(isSelected());
            }
        };
        activateAction.setIcon(this.getIconKey());
        activateAction.setSelected(config.isSelected());

        service = new CaptchaPushService(this);

        logger.info("CaptchaPush: OK");
    }

    @Override
    public AbstractConfigPanel getConfigPanel() {
        return configPanel;
    }

    public CaptchaPushConfig getConfig() {
        return config;
    }

    @Override
    public boolean hasConfigPanel() {
        return true;
    }

    @Override
    public String getConfigID() {
        return "captchapush";
    }

    @Override
    public String getAuthor() {
        return "Greeny";
    }

    @Override
    public String getDescription() {
        return "This plugin can push any Captcha request to your Android or WebOS Smartphone";
    }

    @Override
    public AddonPanel getGUI() {
        return null;
    }

    @Override
    public ArrayList<MenuAction> getMenuAction() {
        ArrayList<MenuAction> menu = new ArrayList<MenuAction>();

        menu.add(activateAction);

        return menu;
    }

}