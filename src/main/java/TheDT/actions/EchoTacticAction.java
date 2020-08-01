package TheDT.actions;

import TheDT.cards.EchoTactic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.DualWieldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class EchoTacticAction extends AbstractGameAction {
	private ArrayList<AbstractCard> cannotDuplicate = new ArrayList<>();
	private int discount;

	public EchoTacticAction(int discount) {
		setValues(AbstractDungeon.player, source);
		actionType = ActionType.DRAW;
		duration = Settings.ACTION_DUR_FAST;
		this.discount = discount;
	}

	public void update() {
		if (duration == Settings.ACTION_DUR_FAST) {
			ArrayList<AbstractCard> handGroup = AbstractDungeon.player.hand.group;
			for (AbstractCard c : handGroup) {
				if (c instanceof EchoTactic) {
					cannotDuplicate.add(c);
				}
			}

			if (cannotDuplicate.size() == handGroup.size()) {
				isDone = true;
				return;
			}

			if (handGroup.size() - cannotDuplicate.size() == 1) {
				for (AbstractCard c : handGroup) {
					if (!(c instanceof EchoTactic)) {
						addToTop(new MakeTempCardInHandAction(c.makeStatEquivalentCopy()));

						isDone = true;
						return;
					}
				}
			}

			handGroup.removeAll(cannotDuplicate);
			if (handGroup.size() > 0) {
				AbstractDungeon.handCardSelectScreen.open(DualWieldAction.TEXT[0], 1, false, false, false, false);
				tickDuration();
				return;
			}
		}

		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				AbstractDungeon.player.hand.addToTop(c);

				AbstractCard temp = c.makeStatEquivalentCopy();
				if (temp.costForTurn > 0) {
					temp.isCostModifiedForTurn = true;
					temp.costForTurn = Math.max(0, temp.costForTurn - discount);
				}
				if (temp.cost > 0) {
					temp.isCostModified = true;
					temp.cost = Math.max(0, temp.cost - discount);
				}
				addToTop(new MakeTempCardInHandAction(temp));
			}

			returnCards();
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			isDone = true;
		}

		this.tickDuration();
	}

	private void returnCards() {
		for (AbstractCard c : cannotDuplicate) {
			AbstractDungeon.player.hand.addToTop(c);
		}

		AbstractDungeon.player.hand.refreshHandLayout();
	}
}
