package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GainBurnPerEnemyAction extends AbstractGameAction {

	public GainBurnPerEnemyAction() {

	}

	public void update() {
		int count = 0;
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!mo.isDeadOrEscaped()) {
				count++;
			}
		}
		addToBot(new MakeTempCardInDrawPileAction(new Burn(), count, true, true));

		isDone = true;
	}
}
