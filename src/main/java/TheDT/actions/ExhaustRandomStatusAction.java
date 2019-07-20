package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javafx.util.Pair;

import java.util.ArrayList;

public class ExhaustRandomStatusAction extends AbstractGameAction {
	private AbstractPlayer p;

	public ExhaustRandomStatusAction() {
		p = AbstractDungeon.player;
		duration = Settings.ACTION_DUR_FAST;
		actionType = ActionType.CARD_MANIPULATION;
		amount = 1;
	}

	public void update() {
		ArrayList<Pair<AbstractCard, CardGroup>> list = new ArrayList<>();
		CardGroup[] groups = new CardGroup[]{p.drawPile, p.discardPile, p.hand};
		if (this.duration == Settings.ACTION_DUR_FAST) {
			for (CardGroup cg : groups) {
				for (AbstractCard c : cg.group) {
					if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
						list.add(new Pair<>(c, cg));
					}
				}
			}
			if (list.size() > 0) {
				Pair<AbstractCard, CardGroup> pair = list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
				AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(pair.getKey(), pair.getValue()));
			}
			isDone = true;
		}
	}
}
