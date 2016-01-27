package com.tisl.mpl.cockpits.cmscockpit.editor;

import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.HtmlBasedComponent;



import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.WysiwygUIEditor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

public class MplWysiwygUIEditor extends WysiwygUIEditor {

	private static final Logger LOG = Logger.getLogger(MplWysiwygUIEditor.class);
	@Override
	public HtmlBasedComponent createViewComponent(final Object initialValue,
			final Map<String, ? extends Object> parameters,
			final EditorListener listener) {
		String aspell_prog = null;
		String langIso = (String) parameters.get("languageIso");
		String osName = System.getProperty("os.name");
		LOG.debug("Detected OS is :::::"+osName);
		final ConfigurationService configService = (ConfigurationService) Registry
				.getApplicationContext().getBean("configurationService");
		Configuration config = configService.getConfiguration();
		// Take the path of aspell from local.properties as this will be
		// configurable
		if (osName.toLowerCase().contains("win")) {
			
			
			aspell_prog = config.getString("win.aspell.path");
			LOG.debug("Aspell Prog for windows:::::"+aspell_prog);

		}
		
		else{
			aspell_prog = config.getString("linux.aspell.path","/usr/bin/aspell");
			LOG.debug("Aspell Prog for linux:::::"+aspell_prog);
		}
		if (langIso == null) {
			langIso = UISessionUtils.getCurrentSession().getLanguageIso();
		}
		UISessionUtils.getCurrentSession().setSessionAttribute(
				"cockpit.current_editor_language", langIso);

		UISessionUtils.getCurrentSession().setSessionAttribute(
				"cockpit.aspell_prog", aspell_prog);
		if (inline) {
			return createWysiwygInline((String) initialValue, listener,
					parameters);
		}
		return super.createViewComponent(initialValue, parameters, listener);

	}

}
