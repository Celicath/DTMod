package TheDT.actions;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HiddenCardAction extends AbstractGameAction {
	public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("DiscardPileToHandAction").TEXT;
	private AbstractPlayer p;
	private boolean upgraded;

	public HiddenCardAction(int amount, boolean upgraded) {
		this.p = AbstractDungeon.player;
		this.setValues(this.p, AbstractDungeon.player, amount);
		this.actionType = ActionType.CARD_MANIPULATION;
		this.upgraded = upgraded;
		this.duration = Settings.ACTION_DUR_MED;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_MED) {

			if (p.discardPile.size() <= amount) {
				for (AbstractCard c : p.discardPile.group) {
					doAction(c);
				}
				this.p.hand.refreshHandLayout();
				this.isDone = true;
			} else {
				AbstractDungeon.gridSelectScreen.open(p.discardPile, this.amount, TEXT[0], false);
				this.tickDuration();
			}
		} else {
			if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
				for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
					doAction(c);
				}

				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				this.p.hand.refreshHandLayout();

				for (AbstractCard c : p.discardPile.group) {
					c.unhover();
					c.target_x = (float) CardGroup.DISCARD_PILE_X;
					c.target_y = 0.0F;
				}

				this.isDone = true;
			}

			this.tickDuration();
		}
	}

	private void doAction(AbstractCard card) {
		if (this.p.hand.size() < BaseMod.MAX_HAND_SIZE) {
			this.p.hand.addToHand(card);
			this.p.discardPile.removeCard(card);
		}
		if (upgraded) {
			float tmp = card.costForTurn * 2;
			for (final AbstractPower p : AbstractDungeon.player.powers) {
				tmp = p.modifyBlock(tmp);
			}
			if (tmp < 0.0f) {
				tmp = 0.0f;
			}
			int block = MathUtils.floor(tmp);
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
		}

		card.lighten(false);
		card.unhover();
		card.target_x = (float) CardGroup.DISCARD_PILE_X;
		card.target_y = 0.0F;
	}
}
