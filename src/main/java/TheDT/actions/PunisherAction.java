package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class PunisherAction extends AbstractGameAction {
	private DamageInfo info;
	private static final float DURATION = 0.01F;
	private static final float POST_ATTACK_WAIT_DUR = 0.1F;
	private AbstractMonster m;

	public PunisherAction(AbstractMonster target, DamageInfo info) {
		this.info = info;
		this.setValues(target, info);
		this.m = target;
		this.actionType = ActionType.DAMAGE;
		this.attackEffect = AttackEffect.SLASH_VERTICAL;
		this.duration = DURATION;
	}

	public void update() {
		if (this.target == null) {
			this.isDone = true;
		} else {
			if (this.m.hasPower(WeakPower.POWER_ID)) {
				if (this.duration == DURATION && this.target != null && this.target.currentHealth > 0) {
					if (this.info.type != DamageInfo.DamageType.THORNS && this.info.owner.isDying) {
						this.isDone = true;
						return;
					}

					AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
				}

				this.tickDuration();
				if (this.isDone && this.target != null && this.target.currentHealth > 0) {
					this.target.damage(this.info);
					if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
						AbstractDungeon.actionManager.clearPostCombatActions();
					}

					this.addToTop(new WaitAction(POST_ATTACK_WAIT_DUR));
				}
			} else {
				this.isDone = true;
			}

		}
	}
}
