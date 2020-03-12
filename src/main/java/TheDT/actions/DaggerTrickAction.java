package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;

public class DaggerTrickAction extends AbstractGameAction {
	private boolean freeToPlayOnce;
	private int damage;
	private AbstractPlayer p;
	private DamageInfo.DamageType damageTypeForTurn;
	private int energyOnUse;

	public DaggerTrickAction(AbstractPlayer p, AbstractCreature target, int damage, DamageInfo.DamageType damageTypeForTurn, boolean freeToPlayOnce, int energyOnUse) {
		this.p = p;
		this.target = target;
		this.damage = damage;
		this.freeToPlayOnce = freeToPlayOnce;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.actionType = ActionType.SPECIAL;
		this.damageTypeForTurn = damageTypeForTurn;
		this.energyOnUse = energyOnUse;
	}

	public void update() {
		int effect = EnergyPanel.totalCount + 1;
		if (energyOnUse != -1) {
			effect = energyOnUse + 1;
		}

		if (p.hasRelic(ChemicalX.ID)) {
			effect += 2;
			p.getRelic(ChemicalX.ID).flash();
		}

		if (effect > 0) {
			addToTop(new FreezeAggroAction(false));
			for (int i = 0; i < effect; ++i) {
				addToTop(new DamageAction(target, new DamageInfo(p, damage, damageTypeForTurn), true));
				if (target != null && target.hb != null) {
					addToTop(new VFXAction(new ThrowDaggerEffect(target.hb.cX, target.hb.cY)));
				}
			}
			addToTop(new FreezeAggroAction(true));

			if (!freeToPlayOnce) {
				p.energy.use(EnergyPanel.totalCount);
			}
		}

		isDone = true;
	}
}
