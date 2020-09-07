package TheDT;

import TheDT.Interfaces.CreateBurnPower;
import TheDT.Interfaces.ShufflePower;
import TheDT.actions.FreezeAggroAction;
import TheDT.cards.AbstractDTCard;
import TheDT.cards.RepeatStrike;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.modules.TargetMarker;
import TheDT.crossovers.OptionalRelicHelper;
import TheDT.patches.*;
import TheDT.potions.BlazePotion;
import TheDT.powers.ResonanceFormPower;
import TheDT.relics.*;
import TheDT.screens.DragonStatusButton;
import TheDT.screens.DragonStatusScreen;
import TheDT.utils.TutorialHelper;
import TheDT.variables.DTDragonBlock;
import TheDT.variables.DTDragonDamage;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.MercuryHourglass;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class DTModMain
		implements EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber,
		EditCharactersSubscriber, PostInitializeSubscriber, OnStartBattleSubscriber,
		PostEnergyRechargeSubscriber, StartGameSubscriber, PostDungeonInitializeSubscriber {
	public static final Logger logger = LogManager.getLogger(DTModMain.class.getName());

	private static final String MODNAME = "The DT";
	private static final String AUTHOR = "Celicath";
	private static final String DESCRIPTION = "Adds a character called The DT.";

	public static final Color DT_ORANGE = CardHelper.getColor(216, 116, 24);

	private static final String DT_MOD_ASSETS_FOLDER = "DTMod/images";

	// Card backgrounds
	private static final String ATTACK_DT_GRAY = "512/bg_attack_dt_orange.png";
	private static final String POWER_DT_GRAY = "512/bg_power_dt_orange.png";
	private static final String SKILL_DT_GRAY = "512/bg_skill_dt_orange.png";
	private static final String ENERGY_ORB_DT_GRAY = "512/card_dt_orange_orb.png";
	private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";

	private static final String ATTACK_DT_GRAY_PORTRAIT = "1024/bg_attack_dt_orange.png";
	private static final String POWER_DT_GRAY_PORTRAIT = "1024/bg_power_dt_orange.png";
	private static final String SKILL_DT_GRAY_PORTRAIT = "1024/bg_skill_dt_orange.png";
	private static final String ENERGY_ORB_DT_GRAY_PORTRAIT = "1024/card_dt_orange_orb.png";

	// Character assets
	private static final String THE_DT_BUTTON = "charSelect/DTButton.png";
	private static final String THE_DT_PORTRAIT = "charSelect/DTPortraitBG.png";
	public static final String THE_DT_SHOULDER_1 = "char/TheDT/shoulder.png";
	public static final String THE_DT_SHOULDER_2 = "char/TheDT/shoulder2.png";
	public static final String THE_DT_CORPSE = "char/TheDT/corpse.png";

	// Mod Badge
	public static final String BADGE_IMAGE = "Badge.png";

	// Animations atlas and JSON files
	public static final String THE_DT_SKELETON_ATLAS = "char/TheDT/skeleton.atlas";
	public static final String THE_DT_SKELETON_JSON = "char/TheDT/skeleton.json";

	// Modules
	public static TargetMarker targetMarker;

	// Crossovers
	public static boolean isAspirationLoaded;
	public static boolean isGahtererLoaded;
	public static boolean isFriendlyMinionsLoaded;

	// Configs
	public static Properties dtDefaults = new Properties();
	public static String filename = "DTModSaveData";
	public static boolean enableEvent = false;
	ModLabeledToggleButton enableEventButton;

	// etc.
	public static Burn previewBurn = null;
	public static String MythicalSkillbookID = makeID("MythicalSkillbook");

	public static final String CHOICE_ID_YOU = "ChooseOptionYou";
	public static final String CHOICE_ID_DRAGON = "ChooseOptionDragon";

	public static String makePath(String resource) {
		return DT_MOD_ASSETS_FOLDER + "/" + resource;
	}

	// System
	public static int yourAggro = 0;
	public static int dragonAggro = 0;
	public static int burnGen = 0;
	public static int bondingGained = 0;

	// Screens
	public static DragonStatusScreen dragonStatusScreen;
	public static DragonStatusButton dragonStatusButton;
	boolean topPanelAdded = false;

	public DTModMain() {
		logger.info("Subscribe to basemod hooks");

		BaseMod.subscribe(this);

		logger.info("Done subscribing");

		logger.info("Creating the color " + CardColorEnum.DT_ORANGE.toString());

		BaseMod.addColor(CardColorEnum.DT_ORANGE, DT_ORANGE, DT_ORANGE, DT_ORANGE,
				DT_ORANGE, DT_ORANGE, DT_ORANGE, DT_ORANGE, makePath(ATTACK_DT_GRAY),
				makePath(SKILL_DT_GRAY), makePath(POWER_DT_GRAY),
				makePath(ENERGY_ORB_DT_GRAY), makePath(ATTACK_DT_GRAY_PORTRAIT),
				makePath(SKILL_DT_GRAY_PORTRAIT), makePath(POWER_DT_GRAY_PORTRAIT),
				makePath(ENERGY_ORB_DT_GRAY_PORTRAIT), makePath(CARD_ENERGY_ORB));

		logger.info("Done Creating the color");

		loadConfig();

		logger.debug("Constructor finished.");
	}

	@SuppressWarnings("unused")
	public static void initialize() {
		DTModMain mod = new DTModMain();
		isAspirationLoaded = Loader.isModLoaded("aspiration");
		isGahtererLoaded = Loader.isModLoaded("gatherermod");
		isFriendlyMinionsLoaded = Loader.isModLoaded("Friendly_Minions_0987678");
	}

	@Override
	public void receiveEditCharacters() {
		logger.info("begin editing characters. " + "Add " + TheDTEnum.THE_DT.toString());

		BaseMod.addCharacter(new DragonTamer(DragonTamer.charStrings.NAMES[1], TheDTEnum.THE_DT),
				makePath(THE_DT_BUTTON), makePath(THE_DT_PORTRAIT), TheDTEnum.THE_DT);

		receiveEditPotions();
		logger.info("done editing characters");
	}

	@Override
	public void receiveStartGame() {
		if (AbstractDungeon.player instanceof DragonTamer) {
			if (!topPanelAdded) {
				BaseMod.addTopPanelItem(dragonStatusButton);
				topPanelAdded = true;
			}
		} else if (topPanelAdded) {
			BaseMod.removeTopPanelItem(dragonStatusButton);
			topPanelAdded = false;
		}
	}

	public static void loadConfig() {
		logger.debug("loadConfig started.");
		try {
			SpireConfig config = new SpireConfig("DTMod", filename, dtDefaults);
			config.load();
			enableEvent = config.getBool("enableEvent");
		} catch (Exception e) {
			e.printStackTrace();
			enableEvent = false;
		}
		logger.debug("loadConfig finished.");
	}

	public static void saveConfig() {
		logger.debug("saveConfig started.");
		try {
			SpireConfig config = new SpireConfig("DTMod", filename, dtDefaults);
			config.setBool("enableEvent", enableEvent);
			config.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("saveConfig finished.");
	}

	@Override
	public void receivePostInitialize() {
		// Load the Mod Badge
		Texture badgeTexture = new Texture(makePath(BADGE_IMAGE));

		// Create the Mod Menu
		ModPanel settingsPanel = new ModPanel();

		BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

		TutorialHelper.init();

		targetMarker = new TargetMarker();
		previewBurn = new Burn();
		dragonStatusScreen = new DragonStatusScreen();
		dragonStatusButton = new DragonStatusButton();
	}

	public void receiveEditPotions() {
		logger.info("begin editing potions");

		BaseMod.addPotion(
				BlazePotion.class,
				Color.ORANGE.cpy(),
				Color.ORANGE.cpy(),
				null,
				BlazePotion.POTION_ID, TheDTEnum.THE_DT);

		logger.info("end editing potions");
	}


	@Override
	public void receiveEditRelics() {
		logger.info("Add relics");

		BaseMod.addRelicToCustomPool(new PactStone(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new MagicStone(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new BasicTextbook(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new AncientBone(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new DragonFood(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new TacticalNote(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new OddArmor(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new SwitchButton(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new BindingString(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new SolidMemories(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new TheChair(), CardColorEnum.DT_ORANGE);

		if (isAspirationLoaded) {
			OptionalRelicHelper.registerAspirationRelic();
		}

		logger.info("Done adding relics!");
	}


	@Override
	public void receiveEditCards() {
		// Add the Custom Dynamic Variables
		BaseMod.addDynamicVariable(new DTDragonDamage());
		BaseMod.addDynamicVariable(new DTDragonBlock());

		new AutoAdd("DTMod")
				.packageFilter(AbstractDTCard.class)
				.setDefaultSeen(true)
				.cards();
	}

	@Override
	public void receiveEditStrings() {
		logger.info("Begin editing strings");

		String loc = getLocCode();

		// RelicStrings
		String relicStrings = GetLocString(loc, "relics");
		BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
		// CardStrings
		String cardStrings = GetLocString(loc, "cards");
		BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
		// PotionStrings
		String potionStrings = GetLocString(loc, "potions");
		BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
		// PowerStrings
		String powerStrings = GetLocString(loc, "powers");
		BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
		// UIStrings
		String uiStrings = GetLocString(loc, "ui");
		BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
		// EventStrings
		String eventStrings = GetLocString(loc, "events");
		BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
		// CharacterStrings
		String characterStrings = GetLocString(loc, "characters");
		BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
		// TutorialStrings
		String tutorialStrings = GetLocString(loc, "tutorials");
		BaseMod.loadCustomStrings(TutorialStrings.class, tutorialStrings);

		logger.info("Done editing strings");
	}


	@Override
	public void receivePostDungeonInitialize() {
		if (AbstractDungeon.player.chosenClass == TheDTEnum.THE_DT) {
			AbstractDungeon.uncommonRelicPool.remove(MercuryHourglass.ID);
		}
	}

	public static void onShuffle() {
		if (AbstractDungeon.player == null) {
			return;
		}
		for (AbstractPower p : AbstractDungeon.player.powers) {
			if (p instanceof ShufflePower) {
				((ShufflePower) p).onShuffle();
			}
		}
	}

	public static void onBurnCreated() {
		if (AbstractDungeon.player == null) {
			return;
		}
		burnGen++;
		for (AbstractPower p : AbstractDungeon.player.powers) {
			if (p instanceof CreateBurnPower) {
				((CreateBurnPower) p).onBurnCreated();
			}
		}
		Dragon d = DragonTamer.getLivingDragon();
		if (d != null) {
			for (AbstractPower p : d.powers) {
				if (p instanceof CreateBurnPower) {
					((CreateBurnPower) p).onBurnCreated();
				}
			}
		}
	}

	public static String getLocCode() {
		switch (Settings.language) {
			case KOR:
				return "kor";
			default:
				return "eng";
		}
	}

	@Override
	public void receiveEditKeywords() {
		logger.debug("receiveEditKeywords started.");
		Gson gson = new Gson();
		String loc = getLocCode();

		String json = GetLocString(loc, "keywords");
		Keyword[] keywords = gson.fromJson(json, Keyword[].class);

		if (keywords != null) {
			for (Keyword keyword : keywords) {
				BaseMod.addKeyword("ragontaer", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
			}
		}
		logger.debug("receiveEditKeywords finished.");
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		SkillbookGameState.reset();
		MonsterTargetPatch.prevPlayer = null;

		DTModMain.dragonAggro = 0;
		DTModMain.yourAggro = 0;

		FreezeAggroAction.frozen = false;
		burnGen = 0;
		bondingGained = 0;
		ResonanceFormPower.disabledViaSelf = false;
		ResonanceFormPower.disabledViaCard = false;

		receivePostEnergyRecharge();
	}

	@Override
	public void receivePostEnergyRecharge() {
		for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
			if (c instanceof RepeatStrike) {
				AbstractDungeon.actionManager.addToTop(new DiscardToHandAction(c));
			}
		}
		DragonTamer.frontChangedThisTurn = false;
	}

	public static String makeID(String idText) {
		return "DTMod:" + idText;
	}

	public static String GetCardPath(String id) {
		return "DTMod/images/cards/" + id + ".png";
	}

	public static String GetPowerPath(String id, int size) {
		return "DTMod/images/powers/" + id + "_" + size + ".png";
	}

	public static String GetVfxPath(String id) {
		return "DTMod/images/vfx/" + id + ".png";
	}

	public static String GetRelicPath(String id) {
		return "DTMod/images/relics/" + id + ".png";
	}

	public static String GetRelicOutlinePath(String id) {
		return "DTMod/images/relics/outline/" + id + ".png";
	}

	public static String GetEventPath(String id) {
		return "DTMod/images/events/" + id + ".png";
	}

	private static String GetLocString(String locCode, String name) {
		return Gdx.files.internal("DTMod/localization/" + locCode + "/" + name + ".json").readString(
				String.valueOf(StandardCharsets.UTF_8));
	}
}
