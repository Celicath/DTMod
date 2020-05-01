package TheDT.characters;

import TheDT.DTModMain;
import TheDT.Interfaces.SwitchPower;
import TheDT.actions.ApplyAggroAction;
import TheDT.cards.*;
import TheDT.patches.CardColorEnum;
import TheDT.powers.TauntPower;
import TheDT.relics.BindingString;
import TheDT.relics.PactStone;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.getCurrRoom;

public class DragonTamer extends CustomPlayer {
	public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(DTModMain.makeID("TheDT"));
	public static final Logger logger = LogManager.getLogger(DTModMain.class.getName());

	public static final int ENERGY_PER_TURN = 3;
	public static final int STARTING_HP = 45;
	public static final int MAX_HP = 45;
	public static final int STARTING_GOLD = 99;
	public static final int CARD_DRAW = 5;
	public static final int ORB_SLOTS = 0;

	public Dragon dragon;
	public AbstractCreature front = this;
	public AbstractDTCard.DTCardTarget dtTargetMode;

	public static boolean frontChangedThisTurn = false;
	public static boolean battleHarmonyYou = false;
	public static boolean battleHarmonyDragon = false;

	// The aggro of Dragon
	public int aggro;

	public boolean isReticleAttackIcon;
	public Color attackIconColor = CardHelper.getColor(255, 160, 48);
	public static Texture attackerIcon = null;

	public boolean dragonAttackAnimation;
	public boolean attackAnimation;

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

	public DragonTamer(String name, PlayerClass setClass) {
		super(name, setClass, orbTextures,
				"DTMod/images/char/TheDT/orb/vfx.png", null, null, null);

		CharSelectInfo loadout = getLoadout();
		initializeClass(DTModMain.makePath("char/TheDT/idle.png"),
				DTModMain.makePath(DTModMain.THE_DT_SHOULDER_1),
				DTModMain.makePath(DTModMain.THE_DT_SHOULDER_2),
				DTModMain.makePath(DTModMain.THE_DT_CORPSE),
				loadout, 0.0F, 0.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

		this.dialogX = (this.drawX + 0.0F * Settings.scale);
		this.dialogY = (this.drawY + 220.0F * Settings.scale);

		dragon = new Dragon(Dragon.charStrings.NAMES[0], 0.0f, 0.0f, 220.0f, 290.0f, this);
		dragon.initializeClass();
	}

	@Override
	public CharSelectInfo getLoadout() {
		return new CharSelectInfo(
				getLocalizedCharacterName(),
				charStrings.TEXT[0],
				STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
				getStartingDeck(), false);
	}

	@Override
	public ArrayList<String> getStartingDeck() {
		ArrayList<String> retVal = new ArrayList<>();

		retVal.add(DTModMain.makeID(Strike.RAW_ID));
		retVal.add(DTModMain.makeID(Strike.RAW_ID));
		retVal.add(DTModMain.makeID(Strike.RAW_ID));
		retVal.add(DTModMain.makeID(Strike.RAW_ID));
		retVal.add(DTModMain.makeID(TwinBite.RAW_ID));
		retVal.add(DTModMain.makeID(TargetDefense.RAW_ID));
		retVal.add(DTModMain.makeID(TargetDefense.RAW_ID));
		retVal.add(DTModMain.makeID(TargetDefense.RAW_ID));
		retVal.add(DTModMain.makeID(TargetDefense.RAW_ID));
		retVal.add(DTModMain.makeID(SwitchingTactic.RAW_ID));

		return retVal;
	}

	public ArrayList<String> getStartingRelics() {
		ArrayList<String> retVal = new ArrayList<>();

		retVal.add(PactStone.ID);

		UnlockTracker.markRelicAsSeen(PactStone.ID);

		return retVal;
	}

	@Override
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.playA("ATTACK_FIRE", 0.75f); // Sound Effect
		CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
				false); // Screen Effect
	}

	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return "ATTACK_FIRE";
	}

	@Override
	public int getAscensionMaxHPLoss() {
		return 4;
	}

	@Override
	public AbstractCard.CardColor getCardColor() {
		return CardColorEnum.DT_ORANGE;
	}

	@Override
	public Color getCardTrailColor() {
		return DTModMain.DT_ORANGE;
	}

	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontRed;
	}

	@Override
	public String getLocalizedCharacterName() {
		return charStrings.NAMES[1];
	}

	@Override
	public AbstractCard getStartCardForEvent() {
		return new SwitchingTactic();
	}

	@Override
	public String getTitle(AbstractPlayer.PlayerClass playerClass) {
		return charStrings.NAMES[0];
	}

	@Override
	public AbstractPlayer newInstance() {
		return new DragonTamer(this.name, this.chosenClass);
	}

	@Override
	public Color getCardRenderColor() {
		return DTModMain.DT_ORANGE;
	}

	@Override
	public Color getSlashAttackColor() {
		return DTModMain.DT_ORANGE;
	}

	@Override
	public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
		return new AbstractGameAction.AttackEffect[]{
				AbstractGameAction.AttackEffect.FIRE,
				AbstractGameAction.AttackEffect.FIRE,
				AbstractGameAction.AttackEffect.FIRE,
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
				AbstractGameAction.AttackEffect.SLASH_HEAVY,
				AbstractGameAction.AttackEffect.BLUNT_LIGHT,
				AbstractGameAction.AttackEffect.SLASH_HEAVY};
	}

	@Override
	public String getSpireHeartText() {
		return CardCrawlGame.languagePack.getEventString("DTMod:SpireHeart").DESCRIPTIONS[0];
	}

	@Override
	public String getVampireText() {
		return CardCrawlGame.languagePack.getEventString("DTMod:Vampires").DESCRIPTIONS[0];
	}

	@Override
	public void movePosition(float x, float y) {
		super.movePosition(x, y);
		dragon.move();
	}

	@Override
	public void update() {
		dragon.update();
		super.update();
		if (getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			DTModMain.targetMarker.update();
		}
	}

	@Override
	public void combatUpdate() {
		dragon.combatUpdate();
		super.combatUpdate();
	}

	@Override
	public void showHealthBar() {
		dragon.showHealthBar();
		super.showHealthBar();
	}

	@Override
	public void render(SpriteBatch sb) {
		dragon.render(sb);
		super.render(sb);
		if (getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			DTModMain.targetMarker.render(sb);
		}
	}

	@Override
	public void renderReticle(SpriteBatch sb) {
		if (isReticleAttackIcon) {
			this.reticleRendered = true;
			attackIconColor.a = this.reticleAlpha;
			sb.setColor(attackIconColor);
			sb.draw(getAttackIcon(), this.hb.cX - 64, this.hb.cY - 64, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
		} else if (dtTargetMode != AbstractDTCard.DTCardTarget.DRAGON_ONLY) {
			super.renderReticle(sb);
		}
	}

	@Override
	public void renderHand(SpriteBatch sb) {
		super.renderHand(sb);
		if (this.hoveredCard != null && (this.isDraggingCard || this.inSingleTargetMode) && this.isHoveringDropZone) {
			if (hoveredCard instanceof AbstractDTCard) {
				dtTargetMode = ((AbstractDTCard) hoveredCard).dtCardTarget;
				isReticleAttackIcon = hoveredCard.type == AbstractCard.CardType.ATTACK;
				if (dtTargetMode == AbstractDTCard.DTCardTarget.DRAGON_ONLY || dtTargetMode == AbstractDTCard.DTCardTarget.BOTH) {
					dragon.renderReticle(sb);
				}
				if (isReticleAttackIcon && (dtTargetMode == AbstractDTCard.DTCardTarget.DEFAULT || dtTargetMode == AbstractDTCard.DTCardTarget.BOTH)) {
					renderReticle(sb);
				}
			} else {
				dtTargetMode = AbstractDTCard.DTCardTarget.DEFAULT;
			}
		}
	}

	@Override
	public void renderPlayerBattleUi(SpriteBatch sb) {
		dragon.renderBattleUi(sb);
		super.renderPlayerBattleUi(sb);
	}

	@Override
	public void preBattlePrep() {
		super.preBattlePrep();
		dragon.preBattlePrep();

		aggro = 0;
		front = this;
		dragon.resetFlip();
		addAggro(dragon.getTier());

		frontChangedThisTurn = false;
		battleHarmonyYou = false;
		battleHarmonyDragon = false;

	}

	public void setFront(AbstractCreature newTarget) {
		if (front != newTarget) {
			front = newTarget;
			PowerBuffEffect effect = new PowerBuffEffect(front.hb.cX - front.animX, front.hb.cY + front.hb.height / 2.0F + 60.0f * Settings.scale,
					ApplyAggroAction.TEXT[front == this ? 2 : 3]);
			ReflectionHacks.setPrivate(effect, PowerBuffEffect.class, "targetColor", new Color(0.7f, 0.75f, 0.7f, 1.0f));
			AbstractDungeon.effectsQueue.add(effect);
			updateIntents();
			DTModMain.targetMarker.move(newTarget);
			DTModMain.targetMarker.flash();

			frontChangedThisTurn = true;
			for (AbstractPower p : AbstractDungeon.player.powers) {
				if (p instanceof SwitchPower) {
					((SwitchPower) p).onSwitch();
				}
			}
		}
	}

	public void setAggro(int aggro) {
		this.aggro = aggro;
		if (aggro > 0) {
			setFront(dragon);
		} else if (aggro < 0) {
			setFront(this);
		}
		if (AbstractDungeon.player != null) {
			AbstractDungeon.player.hand.applyPowers();
			for (AbstractRelic r : AbstractDungeon.player.relics) {
				if (r instanceof BindingString) {
					((BindingString) r).updatePulse();
				}
			}
		}
	}

	public void addAggro(int delta) {
		if (delta != 0) {
			setAggro(this.aggro + delta);
		}
	}

	public void updateIntents() {
		if (getCurrRoom().monsters != null) {
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				m.applyPowers();
			}
		}
	}

	@Override
	public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
		attackAnimation = true;
		dragonAttackAnimation = false;
		if (c instanceof AbstractDTCard) {
			if (((AbstractDTCard) c).dtCardTarget == AbstractDTCard.DTCardTarget.DRAGON_ONLY) {
				attackAnimation = false;
				dragonAttackAnimation = true;
			} else if (((AbstractDTCard) c).dtCardTarget == AbstractDTCard.DTCardTarget.BOTH) {
				attackAnimation = false;
			}
		}
		super.useCard(c, monster, energyOnUse);
		attackAnimation = true;
		dragonAttackAnimation = false;
		AbstractDungeon.actionManager.addToBottom(new ApplyAggroAction());
	}

	@Override
	public void useFastAttackAnimation() {
		if (dragonAttackAnimation) {
			dragon.useFastAttackAnimation();
		}
		if (attackAnimation) {
			super.useFastAttackAnimation();
		}
		attackAnimation = true;
	}

	@Override
	public void updateAnimations() {
		super.updateAnimations();
		dragon.updateAnimations();
	}

	@Override
	public void applyStartOfCombatLogic() {
		super.applyStartOfCombatLogic();
		dragon.applyStartOfCombatLogic();
	}

	@Override
	public void applyStartOfTurnPowers() {
		super.applyStartOfTurnPowers();
		AbstractDungeon.player = dragon; // To make FlameBarrierPower wear off on Dragon. Are you serious devs?
		dragon.applyStartOfTurnPowers();
		AbstractDungeon.player = this;
	}

	@Override
	public void applyStartOfTurnPostDrawPowers() {
		super.applyStartOfTurnPostDrawPowers();
		dragon.applyStartOfTurnPostDrawPowers();
	}

	@Override
	public void applyEndOfTurnTriggers() {
		super.applyEndOfTurnTriggers();
		dragon.applyEndOfTurnTriggers();
	}

	@Override
	public void onVictory() {
		super.onVictory();
		dragon.onVictory();
	}

	public Texture getAttackIcon() {
		if (attackerIcon == null) {
			attackerIcon = new Texture(DTModMain.makePath("ui/Attacker.png"));
		}
		return attackerIcon;
	}

	public static Dragon getDragon() {
		if (AbstractDungeon.player instanceof DragonTamer) {
			return ((DragonTamer) AbstractDungeon.player).dragon;
		}
		return null;
	}

	public static Dragon getLivingDragon() {
		if (AbstractDungeon.player instanceof DragonTamer) {
			Dragon dragon = ((DragonTamer) AbstractDungeon.player).dragon;
			if (dragon.isDead) return null;
			return dragon;
		}
		return null;
	}

	public static boolean isFrontDragon() {
		if (AbstractDungeon.player instanceof DragonTamer) {
			Dragon dragon = ((DragonTamer) AbstractDungeon.player).dragon;
			if (dragon.isDead) return false;
			return ((DragonTamer) AbstractDungeon.player).front == dragon;
		}
		return false;
	}

	public static boolean isRearYou() {
		if (AbstractDungeon.player instanceof DragonTamer) {
			Dragon dragon = ((DragonTamer) AbstractDungeon.player).dragon;
			if (dragon.isDead) return true;
			return ((DragonTamer) AbstractDungeon.player).front != AbstractDungeon.player;
		}
		return true;
	}

	public static boolean isCurrentTargetDragon(AbstractCreature monster) {
		if (AbstractDungeon.player instanceof DragonTamer && !((DragonTamer) AbstractDungeon.player).dragon.isDead) {
			if (monster.hasPower(TauntPower.POWER_ID)) {
				return ((TauntPower) monster.getPower(TauntPower.POWER_ID)).targetIsDragon;
			} else {
				return ((DragonTamer) AbstractDungeon.player).front == ((DragonTamer) AbstractDungeon.player).dragon;
			}
		} else if (AbstractDungeon.player instanceof Dragon) {
			// inside monster intent
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSolo() {
		if (AbstractDungeon.player instanceof DragonTamer) {
			return ((DragonTamer) AbstractDungeon.player).dragon.isDead;
		} else {
			return true;
		}
	}

	public static int getAggro() {
		if (AbstractDungeon.player instanceof DragonTamer) {
			return ((DragonTamer) AbstractDungeon.player).aggro;
		} else {
			return 0;
		}
	}
}
