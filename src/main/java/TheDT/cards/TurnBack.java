package TheDT.cards;

import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class TurnBack extends AbstractDTCard implements StartupCard {
	public static final String RAW_ID = "TurnBack";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 2;
	private static final int DRAW = 1;

	public TurnBack() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		dtBaseDragonBlock = POWER;
		magicNumber = baseMagicNumber = DRAW;
	}

	@Override
	public boolean atBattleStartPreDraw() {
		AbstractCard c = this.makeStatEquivalentCopy();
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				c.applyPowers();
				c.use(AbstractDungeon.player, null);
				AbstractDungeon.effectList.add(0, new ShowCardBrieflyEffect(c));
				isDone = true;
			}
		});
		return false;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		if (DragonTamer.isRearYou()) {
			rawDescription = DESCRIPTION;
		} else {
			rawDescription = EXTENDED_DESCRIPTION[0];
		}
		initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (DragonTamer.isRearYou()) {
			addToBot(new GainBlockAction(p, p, block));
		} else {
			addToBot(new GainBlockAction(((DragonTamer) AbstractDungeon.player).dragon, ((DragonTamer) AbstractDungeon.player).dragon, dtDragonBlock));
		}
		addToBot(new DrawCardAction(magicNumber));
	}

	public AbstractCard makeCopy() {
		return new TurnBack();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
