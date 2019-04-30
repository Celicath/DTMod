package TheDT.characters;

import TheDT.DTMod;
import TheDT.cards.Defend;
import TheDT.cards.DisturbingTactics;
import TheDT.cards.Mulligan;
import TheDT.cards.Strike;
import TheDT.patches.CardColorEnum;
import TheDT.relics.DeckCase;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TheDT extends CustomPlayer {
	public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString("TheDT");
	public static final Logger logger = LogManager.getLogger(DTMod.class.getName());

	// =============== BASE STATS =================

	public static final int ENERGY_PER_TURN = 3;
	public static final int STARTING_HP = 52;
	public static final int MAX_HP = 52;
	public static final int STARTING_GOLD = 99;
	public static final int CARD_DRAW = 5;
	public static final int ORB_SLOTS = 0;

	// =============== /BASE STATS/ =================


	// =============== TEXTURES OF BIG ENERGY ORB ===============

	public static final String[] orbTextures = {
			"DTMod/images/char/TheDT/orb/layer1.png",
			"DTMod/images/char/TheDT/orb/layer2.png",
			"DTMod/images/char/TheDT/orb/layer3.png",
			"DTMod/images/char/TheDT/orb/layer4.png",
			"DTMod/images/char/TheDT/orb/layer5.png",
			"DTMod/images/char/TheDT/orb/layer6.png",
			"DTMod/images/char/TheDT/orb/layer1d.png",
			"DTMod/images/char/TheDT/orb/layer2d.png",
			"DTMod/images/char/TheDT/orb/layer3d.png",
			"DTMod/images/char/TheDT/orb/layer4d.png",
			"DTMod/images/char/TheDT/orb/layer5d.png",};

	// =============== /TEXTURES OF BIG ENERGY ORB/ ===============


	// =============== CHARACTER CLASS START =================

	public TheDT(String name, PlayerClass setClass) {
		super(name, setClass, orbTextures,
				"DTMod/images/char/TheDT/orb/vfx.png", null, null, null);


		// =============== TEXTURES, ENERGY, LOADOUT =================

		initializeClass(null, // required call to load textures and setup energy/loadout
				DTMod.makePath(DTMod.THE_DT_SHOULDER_1), // campfire pose
				DTMod.makePath(DTMod.THE_DT_SHOULDER_2), // another campfire pose
				DTMod.makePath(DTMod.THE_DT_CORPSE), // dead corpse
				getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

		// =============== /TEXTURES, ENERGY, LOADOUT/ =================


		// =============== ANIMATIONS =================

		this.loadAnimation(
				DTMod.makePath(DTMod.THE_DT_SKELETON_ATLAS),
				DTMod.makePath(DTMod.THE_DT_SKELETON_JSON),
				1.0f);
		AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
		e.setTime(e.getEndTime() * MathUtils.random());
		e.setTimeScale(0.75F);

		// =============== /ANIMATIONS/ =================


		// =============== TEXT BUBBLE LOCATION =================

		this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
		this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values

		// =============== /TEXT BUBBLE LOCATION/ =================

	}

	// =============== /CHARACTER CLASS END/ =================


	// Starting description and loadout
	@Override
	public CharSelectInfo getLoadout() {
		return new CharSelectInfo(
				getLocalizedCharacterName(),
				charStrings.TEXT[0],
				STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
				getStartingDeck(), false);
	}

	// Starting Deck
	@Override
	public ArrayList<String> getStartingDeck() {
		ArrayList<String> retVal = new ArrayList<>();

		logger.info("Begin loading started Deck strings");

		retVal.add(Strike.ID);
		retVal.add(Strike.ID);
		retVal.add(Strike.ID);
		retVal.add(Strike.ID);
		retVal.add(Defend.ID);
		retVal.add(DisturbingTactics.ID);

		return retVal;
	}

	// Starting Relics
	public ArrayList<String> getStartingRelics() {
		ArrayList<String> retVal = new ArrayList<>();

		retVal.add(DeckCase.ID);

		UnlockTracker.markRelicAsSeen(DeckCase.ID);

		return retVal;
	}

	// Character select screen effect
	@Override
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f); // Sound Effect
		CardCrawlGame.sound.playA("CARD_DRAW_8", 1.0f); // Sound Effect
		CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
				false); // Screen Effect
	}

	// Character select on-button-press sound effect
	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return "CARD_DRAW_8";
	}

	// Should return how much HP your maximum HP reduces by when starting a run at
	// ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
	@Override
	public int getAscensionMaxHPLoss() {
		return 4;
	}

	// Should return the card color enum to be associated with your character.
	@Override
	public AbstractCard.CardColor getCardColor() {
		return CardColorEnum.DT_ORANGE;
	}

	// Should return a color object to be used to color the trail of moving Cards
	@Override
	public Color getCardTrailColor() {
		return DTMod.DT_ORANGE;
	}

	// Should return a BitmapFont object that you can use to customize how your
	// energy is displayed from within the energy orb.
	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontRed;
	}

	// Should return class name as it appears in run history screen.
	@Override
	public String getLocalizedCharacterName() {
		return charStrings.NAMES[1];
	}

	//Which starting card should specific events give you?
	@Override
	public AbstractCard getStartCardForEvent() {
		return new Mulligan();
	}

	// The class name as it appears next to your player name in game
	@Override
	public String getTitle(AbstractPlayer.PlayerClass playerClass) {
		return charStrings.NAMES[0];
	}

	// Should return a new instance of your character, sending this.name as its name parameter.
	@Override
	public AbstractPlayer newInstance() {
		return new TheDT(this.name, this.chosenClass);
	}

	// Should return a Color object to be used to color the miniature card images in run history.
	@Override
	public Color getCardRenderColor() {
		return DTMod.DT_ORANGE;
	}

	// Should return a Color object to be used as screen tint effect when your
	// character attacks the heart.
	@Override
	public Color getSlashAttackColor() {
		return DTMod.DT_ORANGE;
	}

	// Should return an AttackEffect array of any size greater than 0. These effects
	// will be played in sequence as your character's finishing combo on the heart.
	// Attack effects are the same as used in damage action and the like.
	@Override
	public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
		return new AbstractGameAction.AttackEffect[]{
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
				AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
				AbstractGameAction.AttackEffect.SLASH_VERTICAL,
				AbstractGameAction.AttackEffect.BLUNT_LIGHT,
				AbstractGameAction.AttackEffect.SLASH_HEAVY};
	}

	// Should return a string containing what text is shown when your character is
	// about to attack the heart. For example, the defect is "NL You charge your
	// core to its maximum..."
	@Override
	public String getSpireHeartText() {
		return CardCrawlGame.languagePack.getEventString("DTMod:SpireHeart").DESCRIPTIONS[0];
	}

	// The vampire events refer to the base game characters as "brother", "sister",
	// and "broken one" respectively.This method should return a String containing
	// the full text that will be displayed as the first screen of the vampires event.
	@Override
	public String getVampireText() {
		return CardCrawlGame.languagePack.getEventString("DTMod:Vampires").DESCRIPTIONS[0];
	}
}
