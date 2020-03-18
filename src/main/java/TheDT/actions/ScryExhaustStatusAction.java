package TheDT.actions;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class ScryExhaustStatusAction extends ScryAction {
	private float startingDuration;
	private boolean afterActionDone;
	private boolean triggerDone;
	ArrayList<AbstractCard> statuses = null;

	public ScryExhaustStatusAction(int numCards) {
		super(numCards);
		startingDuration = duration;
		afterActionDone = false;
		triggerDone = false;
	}

	@Override
	public void update() {
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			isDone = true;
		} else {
			if (duration == startingDuration) {
				for (AbstractPower p : AbstractDungeon.player.powers) {
					p.onScry();
				}

				if (AbstractDungeon.player.drawPile.isEmpty()) {
					isDone = true;
					return;
				}

				CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				if (this.amount != -1) {
					for (int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); ++i) {
						tmpGroup.addToTop(AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
					}
				} else {
					for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
						tmpGroup.addToBottom(c);
					}
				}

				statuses = new ArrayList<>();
				for (AbstractCard c : tmpGroup.group) {
					if (c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE) {
						statuses.add(c);
					}
				}
				AbstractDungeon.gridSelectScreen.open(tmpGroup, amount, true, TEXT[0]);
			} else {
				if (!afterActionDone) {
					for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
						if (!statuses.contains(c)) {
							AbstractDungeon.player.drawPile.moveToDiscardPile(c);
						}
					}

					AbstractDungeon.gridSelectScreen.selectedCards.clear();

					for (AbstractCard c : statuses) {
						AbstractDungeon.player.drawPile.moveToExhaustPile(c);
					}
					statuses.clear();
					afterActionDone = true;
				}
			}

			if (!triggerDone) {
				for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
					c.triggerOnScry();
				}
				triggerDone = true;
			}

			tickDuration();
		}
	}
}
