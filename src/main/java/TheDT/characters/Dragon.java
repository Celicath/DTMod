package TheDT.characters;

import TheDT.DTMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dragon extends AbstractCreature {
	public static final float OFFSET_X = 160.0f;
	public static final float OFFSET_Y = 60.0f;
	private static final int HP_BONUS_RATIO = 13;
	private static final Logger logger = LogManager.getLogger(AbstractPlayer.class.getName());
	public TheDT master;
	public Texture img;
	public Color attackIconColor = CardHelper.getColor(255.0f, 80.0f, 80.0f);

	public Dragon(String name, float hb_x, float hb_y, float hb_w, float hb_h, TheDT master) {
		super();
		this.name = name;

		this.hb_h = hb_h * Settings.scale;
		this.hb_w = hb_w * Settings.scale;
		this.hb_x = hb_x * Settings.scale;
		this.hb_y = hb_y * Settings.scale;
		this.hb = new Hitbox(this.hb_w, this.hb_h);
		this.healthHb = new Hitbox(this.hb.width, 72.0F * Settings.scale);

		this.drawX = (float) Settings.WIDTH * 0.25F + OFFSET_X;
		this.drawY = AbstractDungeon.floorY + OFFSET_Y;
		this.dialogX = (this.drawX + 20.0F * Settings.scale);
		this.dialogY = (this.drawY + 240.0F * Settings.scale);

		this.master = master;
		this.isPlayer = true;
	}

	public void initializeClass(CharSelectInfo info) {
		this.img = ImageMaster.loadImage(DTMod.makePath("char/TheDT/dragon.png"));
		this.maxHealth = info.maxHp * (100 + HP_BONUS_RATIO) / 100;
		this.currentHealth = info.currentHp * (100 + HP_BONUS_RATIO) / 100;
	}

	public void damage(DamageInfo info) {
		int damageAmount = info.output;
		boolean hadBlock = true;
		if (this.currentBlock == 0) {
			hadBlock = false;
		}

		if (damageAmount > 1 && this.hasPower("IntangiblePlayer")) {
			damageAmount = 1;
		}

		damageAmount = this.decrementBlock(info, damageAmount);

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
				for (AbstractPower power : this.powers) {
					power.onAttacked(info, damageAmount);
				}
			}
			if (info.owner != this) {
				this.useStaggerAnimation();
			}

			this.currentHealth -= damageAmount;

			AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, damageAmount));
			if (this.currentHealth < 0) {
				this.currentHealth = 0;
			} else if (this.currentHealth < this.maxHealth / 4) {
				AbstractDungeon.topLevelEffects.add(new BorderFlashEffect(new Color(1.0F, 0.1F, 0.05F, 0.0F)));
			}

			this.healthBarUpdatedEvent();
			if ((float) this.currentHealth <= (float) this.maxHealth / 2.0F && !this.isBloodied) {
				this.isBloodied = true;

				for (AbstractRelic r : master.relics) {
					if (r != null) {
						// r.onDragonBloodied(); TODO: Make interface
					}
				}
			}

			if (this.currentHealth < 1) {
				// TODO: maybe add some dragon revival things

				this.isDead = true;
				this.currentHealth = 0;
				if (this.currentBlock > 0) {
					this.loseBlock();
					AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
				}
			}
		} else if (this.currentBlock > 0) {
			AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, AbstractPlayer.BLOCKED_STRING));
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

		this.updateEscapeAnimation();
	}

	private void updateEscapeAnimation() {
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
		if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
			if (this.damageFlash) {
				ShaderHelper.setShader(sb, ShaderHelper.Shader.WHITE_SILHOUETTE);
			}

			sb.setColor(Color.WHITE);
			sb.draw(this.img, this.drawX - (float) this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY, (float) this.img.getWidth() * Settings.scale, (float) this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);

			if (this.damageFlash) {
				ShaderHelper.setShader(sb, ShaderHelper.Shader.DEFAULT);
				--this.damageFlashFrames;
				if (this.damageFlashFrames == 0) {
					this.damageFlash = false;
				}
			}

			this.hb.render(sb);
			this.healthHb.render(sb);

			this.hb.render(sb);
			this.healthHb.render(sb);
		}

		if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !this.isDead) {
			this.renderHealth(sb);
		}
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
	public boolean isDeadOrEscaped() {
		return this.isDying || this.halfDead;
	}

	public void preBattlePrep() {
		powers.clear();
		healthBarUpdatedEvent();
	}
}
