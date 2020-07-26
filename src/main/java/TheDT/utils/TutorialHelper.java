package TheDT.utils;

import TheDT.DTModMain;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.ui.FtueTip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class TutorialHelper {
	private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(DTModMain.makeID("Solo"));
	public static final String[] TEXT = tutorialStrings.TEXT;
	public static final String[] LABEL = tutorialStrings.LABEL;

	public static final Logger logger = LogManager.getLogger(TutorialHelper.class.getName());
	public static SpireConfig config;
	private static String tutorialName = "DTModTutorial";
	private static final Properties DEFAULTS;
	private static final String KEY = "tutorialEnabled";
	private static boolean tutorialEnabled;

	static {
		DEFAULTS = new Properties();
		DEFAULTS.setProperty(KEY, "true");
	}

	public static void init() {
		try {
			config = new SpireConfig("DTMod", tutorialName, DEFAULTS);
			tutorialEnabled = config.getBool(KEY);
		} catch (IOException e) {
			logger.warn(e);
		}
	}

	private static void save() {
		try {
			config.save();
		} catch (IOException e) {
			logger.warn(e);
		}
	}

	public static void showTutorial() {
		if (tutorialEnabled) {
			AbstractDungeon.ftue = new FtueTip(LABEL[0], TEXT[0], (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, FtueTip.TipType.CARD_REWARD);
			setTutorialEnabled(false);
		}
	}

	public static void setTutorialEnabled(boolean value) {
		tutorialEnabled = value;
		config.setBool(KEY, tutorialEnabled);
		save();
	}
}
