package TheDT.actions;

import TheDT.DTModMain;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
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

	public HasteAction() {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
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
				AbstractCard c = tmp.getRandomCard(AbstractDungeon.cardRandomRng);
				AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(c, null, c.energyOnUse, true, true), true);
			}
			isDone = true;
		}

		tickDuration();
	}

	static {
		uiStrings = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("HasteAction"));
		TEXT = uiStrings.TEXT;
	}
}
