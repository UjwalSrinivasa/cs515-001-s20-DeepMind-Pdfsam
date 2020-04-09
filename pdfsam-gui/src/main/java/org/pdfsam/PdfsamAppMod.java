package org.pdfsam;

import org.pdfsam.context.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.pdfsam.context.DefaultUserContext;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.pdfsam.ui.workspace.LoadWorkspaceEvent;
import org.pdfsam.ui.workspace.SaveWorkspaceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;

import java.io.File;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.sejda.eventstudio.StaticStudio.eventStudio;
import static org.pdfsam.ui.commons.SetActiveModuleRequest.activeteModule;

public class PdfsamAppMod {
	
	private static final Logger LOG = LoggerFactory.getLogger(PdfsamAppMod.class);
	
	private UserContext userContext = new DefaultUserContext();

	public UserContext getUserContext() {
		return userContext;
	}

	public void initActiveModule() {
		String startupModule = userContext.getStartupModule();
		if (isNotBlank(startupModule)) {
			PdfsamApp.LOG.trace("Activating startup module '{}'", startupModule);
			eventStudio().broadcast(activeteModule(startupModule));
		}
	}

	public void saveWorkspaceIfRequired() {
		if (userContext.isSaveWorkspaceOnExit()) {
			String workspace = userContext.getDefaultWorkspacePath();
			if (isNotBlank(workspace) && Files.exists(Paths.get(workspace))) {
				eventStudio().broadcast(new SaveWorkspaceEvent(new File(workspace), true));
			}
		}
	}
	public void loadWorkspaceIfRequired(Application app) {
		String workspace = ofNullable(app.getParameters().getNamed().get("workspace")).filter(StringUtils::isNotBlank)
				.orElseGet(userContext::getDefaultWorkspacePath);
		if (isNotBlank(workspace) && Files.exists(Paths.get(workspace))) {
			eventStudio().broadcast(new LoadWorkspaceEvent(new File(workspace)));
		}
	}
}