package TheDT;

import TheDT.cards.*;
import TheDT.characters.TheDT;
import TheDT.optionals.OptionalRelicHelper;
import TheDT.patches.CardColorEnum;
import TheDT.patches.TheDTEnum;
import TheDT.potions.LesserPlaceholderPotion;
import TheDT.relics.BigBag;
import TheDT.relics.DeckCase;
import TheDT.relics.EnergeticBat;
import TheDT.relics.PendantOfEscape;
import TheDT.variables.DTMythicalBlock;
import TheDT.variables.DTMythicalDamage;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomCard;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_gatherer.GathererMod;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@SpireInitializer
public class DTMod
		implements EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber,
		EditCharactersSubscriber, PostInitializeSubscriber, OnStartBattleSubscriber, PreMonsterTurnSubscriber {
	public static final Logger logger = LogManager.getLogger(DTMod.class.getName());

	//This is for the in-game mod settings pannel.
	private static final String MODNAME = "The DT";
	private static final String AUTHOR = "Celicath";
	private static final String DESCRIPTION = "Adds a character called The DT, which is not yet revealed.";

	// =============== IMPUT TEXTURE LOCATION =================

	// Colors (RGB)
	// Character Color
	public static final Color DT_ORANGE = CardHelper.getColor(216.0f, 116.0f, 24.0f);

	// Image folder name
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

	// Logics
	public static HashMap<AbstractCard, AbstractCard> shapeshiftReturns = new HashMap<>();
	public static int genCards;

	// Modules

	// Crossovers
	public static boolean isAspirationLoaded;
	public static boolean isGathererLoaded;
	public static boolean isFriendlyMinionsLoaded;

	// Configs
	public static Properties dtDefaults = new Properties();
	public static boolean enableEvent = false;
	ModLabeledToggleButton enableEventButton;

	// =============== /INPUT TEXTURE LOCATION/ =================

	/**
	 * Makes a full path for a resource path
	 *
	 * @param resource the resource, must *NOT* have a leading "/"
	 * @return the full path
	 */
	public static final String makePath(String resource) {
		return DT_MOD_ASSETS_FOLDER + "/" + resource;
	}

	// =============== SUBSCRIBE, CREATE THE COLOR, INITIALIZE =================

	public DTMod() {
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
		DTMod mod = new DTMod();
		isAspirationLoaded = Loader.isModLoaded("aspiration");
		isGathererLoaded = Loader.isModLoaded("gatherermod");
		isFriendlyMinionsLoaded = Loader.isModLoaded("Friendly_Minions_0987678");
	}

	// ============== /SUBSCRIBE, CREATE THE COLOR, INITIALIZE/ =================


	// =============== LOAD THE CHARACTER =================

	@Override
	public void receiveEditCharacters() {
		logger.info("begin editing characters. " + "Add " + TheDTEnum.THE_DT.toString());

		BaseMod.addCharacter(new TheDT(TheDT.charStrings.NAMES[1], TheDTEnum.THE_DT),
				makePath(THE_DT_BUTTON), makePath(THE_DT_PORTRAIT), TheDTEnum.THE_DT);

		receiveEditPotions();
		logger.info("done editing characters");
	}

	// =============== /LOAD THE CHARACTER/ =================


	// =============== POST-INITIALIZE =================

	public static void loadConfig() {
		logger.debug("loadConfig started.");
		try {
			SpireConfig config = new SpireConfig("DTMod", "DTModSaveData", dtDefaults);
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
			SpireConfig config = new SpireConfig("DTMod", "DTModSaveData", dtDefaults);
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

		enableEventButton = new ModLabeledToggleButton(
				CardCrawlGame.languagePack.getUIString(DTMod.makeID("EnableEventConfig")).TEXT[0],
				400.0f, 480.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
				enableEvent, settingsPanel, (label) -> {
		}, (button) -> {
			enableEvent = button.enabled;
			saveConfig();
		});

		settingsPanel.addUIElement(enableEventButton);

		BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

		if (isGathererLoaded) {
			GathererMod.lesserPotionPool.add(new LesserPlaceholderPotion());
		}
	}

	// =============== / POST-INITIALIZE/ =================


	// ================ ADD POTIONS ===================


	public void receiveEditPotions() {
		logger.info("begin editing potions");

		logger.info("end editing potions");
	}

	// ================ /ADD POTIONS/ ===================


	// ================ ADD RELICS ===================

	@Override
	public void receiveEditRelics() {
		logger.info("Add relics");

		BaseMod.addRelicToCustomPool(new DeckCase(), CardColorEnum.DT_ORANGE);

		BaseMod.addRelicToCustomPool(new EnergeticBat(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new PendantOfEscape(), CardColorEnum.DT_ORANGE);
		BaseMod.addRelicToCustomPool(new BigBag(), CardColorEnum.DT_ORANGE);

		if (isAspirationLoaded) {
			OptionalRelicHelper.registerAspirationRelic();
		}

		logger.info("Done adding relics!");
	}

	// ================ /ADD RELICS/ ===================


	// ================ ADD CARDS ===================

	@Override
	public void receiveEditCards() {
		// Add the Custom Dynamic Variables
		BaseMod.addDynamicVariable(new DTMythicalDamage());
		BaseMod.addDynamicVariable(new DTMythicalBlock());

		List<CustomCard> cards = new ArrayList<>();

		cards.add(new Strike());
		cards.add(new Defend());
		cards.add(new DisturbingTactics());
		cards.add(new BadJoker());
		cards.add(new CardBurn());
		cards.add(new OpeningTactics());
		cards.add(new BronzeWave());
		cards.add(new Configure());
		cards.add(new DartThrow());
		cards.add(new FlyingCard());
		cards.add(new Fold());
		cards.add(new ForbiddenStrike());
		cards.add(new HiddenCard());
		cards.add(new JackOfSpades());
		cards.add(new MagicTrick());
		cards.add(new Mulligan());
		cards.add(new SecondChance());
		cards.add(new RefreshTactics());
		cards.add(new RunningTactics());
		cards.add(new StackedDeck());

		cards.add(new SuperBite());

		for (CustomCard card : cards) {
			BaseMod.addCard(card);
			UnlockTracker.unlockCard(card.cardID);
		}
	}

	// ================ /ADD CARDS/ ===================


	// ================ LOAD THE TEXT ===================

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

		logger.info("Done editing strings");
	}

	// ================ /LOAD THE TEXT/ ===================

	public static String getLocCode() {
		if (Settings.language == Settings.GameLanguage.KOR) {
			return "kor";
		} else {
			return "eng";
		}
	}

	// ================ LOAD THE KEYWORDS ===================

	@Override
	public void receiveEditKeywords() {
		logger.debug("receiveEditKeywords started.");
		Gson gson = new Gson();
		String loc = getLocCode();

		String json = GetLocString(loc, "keywords");
		Keyword[] keywords = gson.fromJson(json, Keyword[].class);

		if (keywords != null) {
			for (Keyword keyword : keywords) {
				BaseMod.addKeyword("ythical", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
			}
		}
		logger.debug("receiveEditKeywords finished.");
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		shapeshiftReturns.clear();
		genCards = 0;
	}

	@Override
	public boolean receivePreMonsterTurn(AbstractMonster m) {
		CardGroup[] groups = new CardGroup[]{
				AbstractDungeon.player.hand,
				AbstractDungeon.player.drawPile,
				AbstractDungeon.player.discardPile,
				AbstractDungeon.player.exhaustPile
		};
		for (CardGroup cg : groups) {
			for (int i = 0; i < cg.size(); i++) {
				AbstractCard c = cg.group.get(i);
				if (shapeshiftReturns.containsKey(c)) {
					cg.group.set(i, shapeshiftReturns.get(c));
					cg.group.get(i).stopGlowing();
					cg.group.get(i).unfadeOut();
				}
			}
		}
		shapeshiftReturns.clear();
		return true;
	}

	// ================ /LOAD THE KEYWORDS/ ===================

	// this adds "ModName: " before the ID of any card/relic/power etc.
	// in order to avoid conflicts if any other mod uses the same ID.
	public static String makeID(String idText) {
		return "DTMod:" + idText;
	}

	public static String GetCardPath(String id) {
		return "DTMod/images/cards/" + id + ".png";
	}

	public static String GetPowerPath(String id, int size) {
		return "DTMod/images/powers/" + id + "_" + size + ".png";
	}

	public static String GetOtherPath(String id) {
		return "DTMod/images/other/" + id + ".png";
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

	private static HashMap<String, Texture> imgMap = new HashMap<>();

	public static Texture loadTexture(String path) {
		if (!imgMap.containsKey(path)) {
			imgMap.put(path, ImageMaster.loadImage(path));
		}
		return imgMap.get(path);
	}
}
