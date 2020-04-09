package org.pdfsam;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.pdfsam.context.BooleanUserPreference;
import org.pdfsam.context.StringUserPreference;


public class PdfsamAppModTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private PdfsamAppMod victim;

	@Before
	public void setUp() {
		victim = new PdfsamAppMod();
	}

	@After
	public void tearDown() {
		victim.getUserContext().clear();
	}

	@Test
	public void userContextNonNull() {
		assertNotNull(victim.getUserContext());
	}

	@Test
	public void initActiveModuleBranch() {
		victim.getUserContext().setStringPreference(StringUserPreference.STARTUP_MODULE, "");
		victim.initActiveModule();
		victim.getUserContext().setStringPreference(StringUserPreference.STARTUP_MODULE, "ChuckNorris");
		victim.initActiveModule();
	}

	@Test
	public void saveWorkspaceIfRequiredBranch() throws IOException {
		victim.getUserContext().setBooleanPreference(BooleanUserPreference.SAVE_WORKSPACE_ON_EXIT, false);
		victim.saveWorkspaceIfRequired();
		victim.getUserContext().setBooleanPreference(BooleanUserPreference.SAVE_WORKSPACE_ON_EXIT, true);
		victim.saveWorkspaceIfRequired();
		victim.getUserContext().setStringPreference(StringUserPreference.WORKSPACE_PATH,
				folder.getRoot().getAbsolutePath());
		victim.saveWorkspaceIfRequired();
		folder.newFile("tempFile.txt");
		victim.getUserContext().setStringPreference(StringUserPreference.WORKSPACE_PATH,
				folder.getRoot().getAbsolutePath() + "/tempFile.txt");
		victim.saveWorkspaceIfRequired();
		assertTrue(new File(folder.getRoot().getAbsolutePath() + "/tempFile.txt").exists());
	}

}

