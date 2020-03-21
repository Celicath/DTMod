package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class FastLoseBlockAction extends AbstractGameAction {
	public FastLoseBlockAction(AbstractCreature target) {
		this.target = target;
	}

	public void update() {
		target.loseBlock();
		isDone = true;
	}
}
