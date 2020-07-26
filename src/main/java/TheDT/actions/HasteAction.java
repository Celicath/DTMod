package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HasteAction extends AbstractGameAction {
	private AbstractPlayer p;
	private boolean upgraded;

	public HasteAction(boolean upgraded) {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
		this.upgraded = upgraded;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			for (AbstractCard c : p.hand.group) {
				if (c.type == AbstractCard.CardType.POWER) {
					tmp.addToBottom(c);
				}
			}

			if (tmp.size() > 0) {
				if (upgraded) {
					for (AbstractCard c : tmp.group) {
						AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(c, null, c.energyOnUse, true, true), true);
					}
				} else {
					AbstractCard c = tmp.getRandomCard(AbstractDungeon.cardRandomRng);
					AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(c, null, c.energyOnUse, true, true), true);
				}
			}
			isDone = true;
		}

		tickDuration();
	}
}
