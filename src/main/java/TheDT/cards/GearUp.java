package TheDT.cards;

import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Safety;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class GearUp extends AbstractDTCard {
	public static final String RAW_ID = "GearUp";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int MAGIC = 2;

	public static ArrayList<AbstractCard> previews;
	public static ArrayList<AbstractCard> previewsPlus;

	static {
		previews = new ArrayList<>();
		previews.add(new Safety());
		previews.add(new HardSkin());
		previewsPlus = new ArrayList<>();
		previewsPlus.add(new Safety() {{
			this.upgrade();
		}});
		previewsPlus.add(new HardSkin() {{
			this.upgrade();
		}});
	}

	public GearUp() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = MAGIC;
		cardsToPreview = previews.get(0);
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		boolean result = super.canUse(p, m);
		if (result && DragonTamer.getLivingDragon() == null) {
			cantUseMessage = dragonNotAvailableMessage();
			return false;
		}
		return result;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (upgraded) {
			for (AbstractCard c : previewsPlus) {
				addToBot(new MakeTempCardInHandAction(c.makeStatEquivalentCopy(), magicNumber));
			}
		} else {
			for (AbstractCard c : previews) {
				addToBot(new MakeTempCardInHandAction(c.makeStatEquivalentCopy(), magicNumber));
			}
		}
	}

	public AbstractCard makeCopy() {
		return new GearUp();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}


	@Override
	public void renderCardPreview(SpriteBatch sb) {
		if (AbstractDungeon.player == null || !AbstractDungeon.player.isDraggingCard) {
			int index = 0;
			for (AbstractCard c : upgraded ? previewsPlus : previews) {
				float dx = (AbstractCard.IMG_WIDTH * 0.9f - 5f) * drawScale;
				float dy = (AbstractCard.IMG_HEIGHT * 0.4f - 5f) * drawScale;
				if (current_x > Settings.WIDTH * 0.75f) {
					c.current_x = current_x + dx;
				} else {
					c.current_x = current_x - dx;
				}
				if (index == 0) {
					c.current_y = current_y + dy;
				} else {
					c.current_y = current_y - dy;
				}
				c.drawScale = drawScale * 0.8f;
				c.render(sb);
				index++;
			}
		}
	}

	@Override
	public void renderCardPreviewInSingleView(SpriteBatch sb) {
		int index = 0;
		for (AbstractCard c : upgraded ? previewsPlus : previews) {
			c.current_x = 485.0F * Settings.scale;
			c.current_y = (795.0F - 510.0F * index) * Settings.scale;
			c.drawScale = 0.8f;
			c.render(sb);
			index++;
		}
	}
}
