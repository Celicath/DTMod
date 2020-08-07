package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class FastLoseBlockAction extends AbstractGameAction {
	public FastLoseBlockAction(AbstractCreature target, int amount) {
		this.target = target;
		this.amount = amount;
	}

	public void update() {
		target.loseBlock(amount);
		isDone = true;
	}
}
