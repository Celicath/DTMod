package TheDT.actions;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.powers.BondingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

public class DragonFaintAction extends AbstractGameAction {
	DragonTamer master;

	public DragonFaintAction(DragonTamer master) {
		actionType = ActionType.SPECIAL;
		this.master = master;
	}

	public void update() {
		isDone = true;

		Dragon d = master.dragon;
		Iterator<AbstractPower> it = d.powers.iterator();
		while (it.hasNext()) {
			AbstractPower p = it.next();
			if (p.type == AbstractPower.PowerType.DEBUFF) {
				p.onRemove();
				it.remove();
			}
		}

		master.setAggro(0);
		master.setFront(master);
		addToTop(new RemoveSpecificPowerAction(master, master, BondingPower.POWER_ID));
		AbstractDungeon.onModifyPower();
	}
}
