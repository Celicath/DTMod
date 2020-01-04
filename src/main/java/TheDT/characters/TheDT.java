package TheDT.characters;

import TheDT.DTMod;
import TheDT.actions.AddAggroAction;
import TheDT.cards.*;
import TheDT.patches.CardColorEnum;
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
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TheDT extends CustomPlayer {
	public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString("TheDT");
	public static final Logger logger = LogManager.getLogger(DTMod.class.getName());

	public static final int ENERGY_PER_TURN = 3;
	public static final int STARTING_HP = 40;
	public static final int MAX_HP = 40;
	public static final int STARTING_GOLD = 99;
	public static final int CARD_DRAW = 5;
	public static final int ORB_SLOTS = 0;

	public Dragon dragon;
	public AbstractCreature front = this;
	public AbstractDTCard.DTCardTarget dtTargetMode;

	public int aggro;

	public boolean isReticleAttackIcon;
	public Color attackIconColor = CardHelper.getColor(255, 160, 48);
	public static Texture attackerIcon = null;

	public boolean animDragonAttack;

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

	public TheDT(String name, PlayerClass setClass) {
		super(name, setClass, orbTextures,
				"DTMod/images/char/TheDT/orb/vfx.png", null, null, null);

		CharSelectInfo loadout = getLoadout();
		initializeClass(DTMod.makePath("char/TheDT/idle.png"),
				DTMod.makePath(DTMod.THE_DT_SHOULDER_1),
				DTMod.makePath(DTMod.THE_DT_SHOULDER_2),
				DTMod.makePath(DTMod.THE_DT_CORPSE),
				loadout, 0.0F, 0.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

		this.dialogX = (this.drawX + 0.0F * Settings.scale);
		this.dialogY = (this.drawY + 220.0F * Settings.scale);

		dragon = new Dragon(charStrings.TEXT[1], 0.0f, 0.0f, 220.0f, 290.0f, this);
		dragon.initializeClass(loadout);
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

		logger.info("Begin loading started Deck strings");

		retVal.add(Strike.ID);
		retVal.add(Strike.ID);
		retVal.add(Strike.ID);
		retVal.add(Strike.ID);
		retVal.add(TargetDefense.ID);
		retVal.add(TargetDefense.ID);
		retVal.add(TargetDefense.ID);
		retVal.add(DoubleAttack.ID);
		retVal.add(HardSkin.ID);
		retVal.add(SwitchingTactics.ID);

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
		return DTMod.DT_ORANGE;
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
		return new HardSkin();
	}

	@Override
	public String getTitle(AbstractPlayer.PlayerClass playerClass) {
		return charStrings.NAMES[0];
	}

	@Override
	public AbstractPlayer newInstance() {
		return new TheDT(this.name, this.chosenClass);
	}

	@Override
	public Color getCardRenderColor() {
		return DTMod.DT_ORANGE;
	}

	@Override
	public Color getSlashAttackColor() {
		return DTMod.DT_ORANGE;
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
		dragon.movePosition(x + Dragon.OFFSET_X, y + Dragon.OFFSET_Y);
	}

	@Override
	public void update() {
		dragon.update();
		super.update();
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
		front = dragon;
		addAggro(3);
	}

	public void setFront(AbstractCreature newTarget) {
		if (front != newTarget) {
			front = newTarget;
			PowerBuffEffect effect = new PowerBuffEffect(front.hb.cX - front.animX, front.hb.cY + front.hb.height / 2.0F,
					AddAggroAction.TEXT[front == this ? 2 : 3]);
			ReflectionHacks.setPrivate(effect, PowerBuffEffect.class, "targetColor", new Color(0.7f, 0.75f, 0.7f, 1.0f));
			AbstractDungeon.effectsQueue.add(effect);
			updateIntents();
		}
	}

	public void setAggro(int aggro) {
		if (AbstractDungeon.player != null) {
			AbstractDungeon.player.hand.applyPowers();
		}
		this.aggro = aggro;
		if (aggro > 0) {
			setFront(dragon);
		} else if (aggro < 0) {
			setFront(this);
		}
	}

	public void addAggro(int delta) {
		if (delta != 0) {
			setAggro(this.aggro + delta);
		}
	}

	public void updateIntents() {
		if (AbstractDungeon.getCurrRoom().monsters != null) {
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				m.applyPowers();
			}
		}
	}

	@Override
	public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
		animDragonAttack = false;
		if (c instanceof AbstractDTCard) {
			if (((AbstractDTCard) c).dtCardTarget == AbstractDTCard.DTCardTarget.DRAGON_ONLY) {
				animDragonAttack = true;
			}
		}
		super.useCard(c, monster, energyOnUse);
		if (c.type == AbstractCard.CardType.ATTACK && c.costForTurn != 0 && !c.freeToPlayOnce) {
			AbstractCreature attacker = DTMod.getAttacker(c);
			if (attacker != null) {
				AbstractDungeon.actionManager.addToBottom(new AddAggroAction(attacker, c.costForTurn));
			}
		}
	}

	@Override
	public void useFastAttackAnimation() {
		if (animDragonAttack) {
			dragon.useFastAttackAnimation();
		} else {
			super.useFastAttackAnimation();
		}
	}

	@Override
	public void updateAnimations() {
		super.updateAnimations();
		dragon.updateAnimations();
	}

	public Texture getAttackIcon() {
		if (attackerIcon == null) {
			attackerIcon = new Texture(DTMod.makePath("ui/Attacker.png"));
		}
		return attackerIcon;
	}
}
