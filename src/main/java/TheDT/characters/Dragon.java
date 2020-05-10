package TheDT.characters;

import TheDT.DTModMain;
import TheDT.actions.DragonFaintAction;
import TheDT.cards.HardSkin;
import TheDT.optionals.FriendlyMinionHelper;
import TheDT.patches.CardColorEnum;
import TheDT.powers.BirdFacePower;
import TheDT.powers.BondingPower;
import TheDT.powers.FiercePower;
import TheDT.powers.GreedyPower;
import TheDT.utils.GrayscaleShader;
import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.mod.stslib.patches.tempHp.PlayerDamage;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Dragon extends CustomPlayer implements CustomSavable<ArrayList<Integer>> {
	public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(DTModMain.makeID("Dragon"));
	public static final CharacterStrings dragonGrowthStrings = CardCrawlGame.languagePack.getCharacterString(DTModMain.makeID("DragonGrowth"));

	public static final int STARTING_HP = 50;
	public static final int MAX_HP = 50;

	public static final float OFFSET_X = 192.0f;
	public static final float OFFSET_Y = 72.0f;
	private static final Logger logger = LogManager.getLogger(AbstractPlayer.class.getName());
	public static final Color deadColor = new Color(0.8f, 0.8f, 0.8f, 0.75f);
	public DragonTamer master;
	public Texture img;
	public static Texture[][] imgs = null;
	public Color attackIconColor = CardHelper.getColor(255, 80, 80);

	private float hoverTimer;
	private Color nameColor;
	private Color nameBgColor;

	public int tier2Perk = -1;
	public int tier3Perk = -1;

	public Dragon(String name, float hb_x, float hb_y, float hb_w, float hb_h, DragonTamer master) {
		super(name, master.chosenClass, null,
				null, null, null, null);
		logger.info("Dragon constructor called");

		this.name = name;

		this.hb_h = hb_h * Settings.scale;
		this.hb_w = hb_w * Settings.scale;
		this.hb_x = hb_x * Settings.scale;
		this.hb_y = hb_y * Settings.scale;
		this.hb = new Hitbox(this.hb_w, this.hb_h);
		this.healthHb = new Hitbox(this.hb.width, 72.0F * Settings.scale);

		this.drawX = (float) Settings.WIDTH * 0.25F + OFFSET_X * Settings.scale;
		this.drawY = AbstractDungeon.floorY + OFFSET_Y * Settings.scale;
		this.dialogX = (this.drawX + 20.0F * Settings.scale);
		this.dialogY = (this.drawY + 240.0F * Settings.scale);

		this.master = master;
		this.isPlayer = true;

		this.blights = master.blights;

		this.hoverTimer = 0.0F;
		this.nameColor = new Color();
		this.nameBgColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);

		if (imgs == null) {
			imgs = new Texture[4][];
			imgs[1] = new Texture[]{
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/baby.png")),
			};
			imgs[2] = new Texture[]{
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/growing1.png")),
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/growing2.png")),
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/growing3.png")),
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/growing4.png")),
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/growing5.png"))
			};
			imgs[3] = new Texture[]{
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/mature1.png")),
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/mature2.png")),
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/mature3.png")),
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/mature4.png")),
					ImageMaster.loadImage(DTModMain.makePath("char/Dragon/mature5.png")),
			};
		}


		BaseMod.addSaveField(DTModMain.makeID("Dragon"), this);
	}

	@Override
	public ArrayList<Integer> onSave() {
		logger.info("Dragon onSave called");
		ArrayList<Integer> data = new ArrayList<>();
		data.add(currentHealth);
		data.add(maxHealth);
		data.add(tier2Perk);
		data.add(tier3Perk);
		return data;
	}

	@Override
	public void onLoad(ArrayList<Integer> data) {
		logger.info("Dragon onLoad called");
		if (data == null) {
			return;
		}
		if (data.size() >= 2) {
			currentHealth = data.get(0);
			maxHealth = data.get(1);
		}

		for (int i = 2; i < data.size(); i++) {
			switch (i) {
				case 2:
					tier2Perk = data.get(i);
					break;
				case 3:
					tier3Perk = data.get(i);
					break;
			}
			setImage(i, data.get(i));
		}
	}

	void setImage(int tier, int choice) {
		if (choice < 0) return;
		img = imgs[tier][choice];
	}

	public void grow(int tier, int choice) {
		switch (tier) {
			case 2:
				tier2Perk = choice;
				if (choice == 0) {
					this.increaseMaxHp(7, true);
				}
				break;
			case 3:
				tier3Perk = choice;
				if (choice == 0) {
					this.increaseMaxHp(10, true);
				}
				break;
		}
		setImage(tier, choice);
	}

	@Override
	public ArrayList<String> getStartingDeck() {
		return new ArrayList<>();
	}

	@Override
	public ArrayList<String> getStartingRelics() {
		return new ArrayList<>();
	}

	@Override
	public CharSelectInfo getLoadout() {
		return null;
	}

	@Override
	public String getTitle(AbstractPlayer.PlayerClass playerClass) {
		return charStrings.NAMES[0];
	}

	@Override
	public Color getCardRenderColor() {
		return DTModMain.DT_ORANGE;
	}

	@Override
	public AbstractCard getStartCardForEvent() {
		return new HardSkin();
	}

	@Override
	public Color getCardTrailColor() {
		return DTModMain.DT_ORANGE;
	}

	@Override
	public int getAscensionMaxHPLoss() {
		return 5;
	}

	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontRed;
	}

	@Override
	public void doCharSelectScreenSelectEffect() {
	}

	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return "ATTACK_FIRE";
	}

	@Override
	public String getLocalizedCharacterName() {
		return charStrings.TEXT[1];
	}

	@Override
	public AbstractPlayer newInstance() {
		return new DragonTamer(this.name, this.chosenClass);
	}

	@Override
	public AbstractCard.CardColor getCardColor() {
		return CardColorEnum.DT_ORANGE;
	}

	@Override
	public Color getSlashAttackColor() {
		return DTModMain.DT_ORANGE;
	}

	@Override
	public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
		return null;
	}

	@Override
	public String getSpireHeartText() {
		return "";
	}

	@Override
	public String getVampireText() {
		return "";
	}

	public void initializeClass() {
		setImage(1, 0);
		this.currentHealth = STARTING_HP;
		this.maxHealth = MAX_HP;
	}

	@Override
	public void damage(DamageInfo info) {
		int damageAmount = info.output;
		boolean hadBlock = true;
		if (currentBlock == 0) {
			hadBlock = false;
		}

		if (damageAmount > 1 && this.hasPower("IntangiblePlayer")) {
			damageAmount = 1;
		}

		damageAmount = this.decrementBlock(info, damageAmount);

		// Make Temp HP work on Dragon
		int[] tempDA = new int[]{damageAmount};
		boolean[] tempHB = new boolean[]{hadBlock};
		PlayerDamage.Insert(this, info, tempDA, tempHB);
		damageAmount = tempDA[0];
		hadBlock = tempHB[0];

		if (info.owner != null) {
			for (AbstractPower power : info.owner.powers) {
				power.onAttack(info, damageAmount, this);
			}
			for (AbstractPower power : this.powers) {
				power.onAttacked(info, damageAmount);
			}

		} else {
			logger.info("NO OWNER, DON'T TRIGGER POWERS");
		}

		if (damageAmount > 0) {
			for (AbstractPower power : this.powers) {
				power.onLoseHp(damageAmount);
			}
			if (info.owner != null) {
				for (AbstractPower power : info.owner.powers) {
					power.onInflictDamage(info, damageAmount, this);
				}
			}
			if (info.owner != this) {
				this.useStaggerAnimation();
			}

			this.currentHealth -= damageAmount;

			AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, damageAmount));
			if (this.currentHealth < 0) {
				this.currentHealth = 0;
			}

			this.healthBarUpdatedEvent();
			if ((float) this.currentHealth <= (float) this.maxHealth / 2.0F && !this.isBloodied) {
				this.isBloodied = true;

				for (AbstractRelic r : master.relics) {
					if (r != null) {
						// r.onDragonBloodied();
					}
				}
			}

			if (this.currentHealth < 1) {
				isDead = true;

				AbstractDungeon.actionManager.addToBottom(new DragonFaintAction(master));

				currentHealth = 0;
				if (currentBlock > 0) {
					loseBlock();
					AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
				}
			}
		} else if (this.currentBlock > 0) {
			AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, AbstractPlayer.uiStrings.TEXT[0]));
		} else if (!hadBlock) {
			AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, 0));
		}
	}

	public void movePosition(float x, float y) {
		this.drawX = x;
		this.drawY = y;
		this.dialogX = this.drawX + 0.0F * Settings.scale;
		this.dialogY = this.drawY + 170.0F * Settings.scale;
		this.animX = 0.0F;
		this.animY = 0.0F;
		this.refreshHitboxLocation();
	}

	@Override
	public void update() {
		this.hb.update();
		this.updateHealthBar();
		this.updatePowers();
		this.healthHb.update();
		this.updateReticle();
		this.tint.update();
		if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.EVENT) {
			// update orb-like things
		}

		if (master.flipHorizontal != this.flipHorizontal) {
			this.flipHorizontal = master.flipHorizontal;
			move();
		}

		this.updateEscapeAnimation();
	}

	public void resetFlip() {
		this.flipHorizontal = false;
		move();
	}

	public void move() {
		float x = flipHorizontal ? (master.drawX - OFFSET_X * Settings.scale) : (master.drawX + OFFSET_X * Settings.scale);
		movePosition(x, master.drawY + OFFSET_Y * Settings.scale);
		if (DragonTamer.isFrontDragon()) {
			DTModMain.targetMarker.move(this);
		}
	}


	@Override
	public void combatUpdate() {
		AbstractPlayer prevPlayer = AbstractDungeon.player;
		AbstractDungeon.player = this;
		stance.update();
		AbstractDungeon.player = prevPlayer;
	}

	@Override
	protected void updateEscapeAnimation() {
		if (this.escapeTimer != 0.0F) {
			this.escapeTimer -= Gdx.graphics.getDeltaTime();
			if (this.flipHorizontal) {
				this.drawX -= Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
			} else {
				this.drawX += Gdx.graphics.getDeltaTime() * 500.0F * Settings.scale;
			}
		}

		if (this.escapeTimer < 0.0F) {
			AbstractDungeon.getCurrRoom().endBattle();
			this.flipHorizontal = false;
			this.isEscaping = false;
			this.escapeTimer = 0.0F;
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		stance.render(sb);
		if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !this.isDead) {
			this.renderHealth(sb);
		}

		if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
			if (atlas != null) {
				renderPlayerImage(sb);
			} else {
				if (isDead) {
					sb.setShader(GrayscaleShader.grayscaleShader);
					sb.setColor(deadColor);
				} else {
					sb.setColor(Color.WHITE);
				}
				sb.draw(img, drawX - img.getWidth() * Settings.scale / 2.0F + animX, drawY, img.getWidth() * Settings.scale, img.getHeight() * Settings.scale, 0, 0, img.getWidth(), img.getHeight(), flipHorizontal, flipVertical);
				if (isDead) {
					sb.setShader(null);
				}
			}

			hb.render(sb);
			healthHb.render(sb);
			renderName(sb);
		}
	}

	private void renderName(SpriteBatch sb) {
		if (!this.hb.hovered) {
			this.hoverTimer = MathHelper.fadeLerpSnap(this.hoverTimer, 0.0F);
		} else {
			this.hoverTimer += Gdx.graphics.getDeltaTime();
		}

		if ((!AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.hoveredCard == null || AbstractDungeon.player.hoveredCard.target == AbstractCard.CardTarget.ENEMY) && !this.isDying) {
			if (this.hoverTimer != 0.0F) {
				this.nameColor.a = Math.min(this.hoverTimer * 2.0F, 1.0F);
			} else {
				this.nameColor.a = MathHelper.slowColorLerpSnap(this.nameColor.a, 0.0F);
			}

			float tmp = Interpolation.exp5Out.apply(1.5F, 2.0F, this.hoverTimer);
			this.nameColor.r = Interpolation.fade.apply(Color.DARK_GRAY.r, Settings.CREAM_COLOR.r, this.hoverTimer * 10.0F);
			this.nameColor.g = Interpolation.fade.apply(Color.DARK_GRAY.g, Settings.CREAM_COLOR.g, this.hoverTimer * 3.0F);
			this.nameColor.b = Interpolation.fade.apply(Color.DARK_GRAY.b, Settings.CREAM_COLOR.b, this.hoverTimer * 3.0F);
			float y = Interpolation.exp10Out.apply(this.healthHb.cY, this.healthHb.cY - 8.0F * Settings.scale, this.nameColor.a);
			float x = this.hb.cX - this.animX;
			this.nameBgColor.a = this.nameColor.a / 2.0F * this.hbAlpha;
			sb.setColor(this.nameBgColor);
			TextureAtlas.AtlasRegion img = ImageMaster.MOVE_NAME_BG;
			sb.draw(img, x - (float) img.packedWidth / 2.0F, y - (float) img.packedHeight / 2.0F, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight, Settings.scale * tmp, Settings.scale * 2.0F, 0.0F);
			Color var10000 = this.nameColor;
			var10000.a *= this.hbAlpha;
			FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, this.name, x, y, this.nameColor);
		}
	}

	public int getTier() {
		return tier2Perk == -1 ? 1 : tier3Perk == -1 ? 2 : 3;
	}

	@Override
	public void renderReticle(SpriteBatch sb) {
		if (master.isReticleAttackIcon) {
			reticleRendered = true;
			attackIconColor.a = this.reticleAlpha;
			sb.setColor(attackIconColor);
			sb.draw(master.getAttackIcon(), this.hb.cX - 64, this.hb.cY - 64, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
		} else {
			super.renderReticle(sb);
		}
	}

	public void renderBattleUi(SpriteBatch sb) {
		if ((this.hb.hovered || this.healthHb.hovered) && !AbstractDungeon.isScreenUp) {
			this.renderPowerTips(sb);
		}
	}

	@Override
	public void preBattlePrep() {
		powers.clear();
		healthBarUpdatedEvent();
		if (DTModMain.isFriendlyMinionsLoaded) {
			FriendlyMinionHelper.clearMinions(this);
		}
	}

	@Override
	public void applyStartOfCombatLogic() {
		switch (tier2Perk) {
			case 1:
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
						this, this, new StrengthPower(this, 1), 1));
				break;
			case 2:
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
						this, this, new DexterityPower(this, 1), 1));
				break;
			case 3:
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
						AbstractDungeon.player, AbstractDungeon.player, new BondingPower(AbstractDungeon.player, AbstractDungeon.player, 2), 2));
				break;
			case 4:
				AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
						this, this, new EnergizedPower(this, 1), 1));
				break;
		}
		switch (tier3Perk) {
			case 1:
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
						this, this, new FiercePower(this, 3), 3));
				break;
			case 2:
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
						this, this, new ArtifactPower(this, 2), 2));
				break;
			case 3:
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
						this, this, new BirdFacePower(this, 1), 1));
				break;
			case 4:
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
						this, this, new GreedyPower(this, 75), 75));
				break;
		}
	}

	@Override
	public void onVictory() {
		if (isDead) {
			isDead = false;
			if (currentHealth == 0) {
				heal(1);
			}
		}
		for (AbstractPower p : powers) {
			p.onVictory();
		}
	}

	@Override
	public void renderPowerTips(SpriteBatch sb) {
		if (isDead) {
			ArrayList<PowerTip> tips = new ArrayList<>();
			tips.add(new PowerTip(charStrings.NAMES[0], charStrings.TEXT[0]));

			if (hb.cX + hb.width / 2.0F < TIP_X_THRESHOLD) {
				TipHelper.queuePowerTips(hb.cX + hb.width / 2.0F + TIP_OFFSET_R_X, hb.cY + TipHelper.calculateAdditionalOffset(tips, hb.cY), tips);
			} else {
				TipHelper.queuePowerTips(hb.cX - hb.width / 2.0F + TIP_OFFSET_L_X, hb.cY + TipHelper.calculateAdditionalOffset(tips, hb.cY), tips);
			}
		} else {
			super.renderPowerTips(sb);
		}
	}

	@Override
	public void heal(int healAmount) {
		AbstractPlayer prevPlayer = AbstractDungeon.player;
		AbstractDungeon.player = this;
		super.heal(healAmount);
		AbstractDungeon.player = prevPlayer;
	}

	@Override
	public void heal(int healAmount, boolean showEffect) {
		AbstractPlayer prevPlayer = AbstractDungeon.player;
		AbstractDungeon.player = this;
		super.heal(healAmount, showEffect);
		AbstractDungeon.player = prevPlayer;
	}
}
