package TheDT.actions;

import TheDT.DTModMain;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class HasteAction extends AbstractGameAction {
	private static final UIStrings uiStrings;
	public static final String[] TEXT;
	private AbstractPlayer p;
	private ArrayList<AbstractCard> nonPowers = new ArrayList<>();

	public HasteAction() {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			for (AbstractCard c : p.hand.group) {
				if (c.type != AbstractCard.CardType.POWER) {
					nonPowers.add(c);
				}
			}

			if (nonPowers.size() == p.hand.group.size()) {
				isDone = true;
				return;
			}

			if (p.hand.group.size() - nonPowers.size() == 1) {
				for (AbstractCard c : p.hand.group) {
					if (c.type == AbstractCard.CardType.POWER) {
						AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(c, null, c.energyOnUse, true, true), true);
						isDone = true;
						return;
					}
				}
			}

			this.p.hand.group.removeAll(nonPowers);
			AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
			this.tickDuration();
			return;
		}

		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(c, null, c.energyOnUse, true, true), true);
				p.hand.addToTop(c);
			}

			returnCards();
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			isDone = true;
		}

		tickDuration();
	}

	private void returnCards() {
		for (AbstractCard c : nonPowers) {
			p.hand.addToTop(c);
		}

		p.hand.refreshHandLayout();
	}

	static {
		uiStrings = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("HasteAction"));
		TEXT = uiStrings.TEXT;
	}
}
