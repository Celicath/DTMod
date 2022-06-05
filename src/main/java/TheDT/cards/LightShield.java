package TheDT.cards;

import TheDT.DTModMain;
import TheDT.Interfaces.OnBondingActivateCard;
import TheDT.actions.DisableResonanceFormAction;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.patches.CustomTags;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LightShield extends AbstractDTCard implements OnBondingActivateCard {
	public static final String RAW_ID = "LightShield";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.BOTH;

	private static final int POWER = 14;
	private static final int UPGRADE_BONUS = 4;

	public LightShield() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		dtBaseDragonBlock = POWER;
		tags.add(CustomTags.DT_BONDING);
	}

	@Override
	public void onBondingActivate() {
		updateCost(-1);
	}

	void configureCostsOnNewCard() {
		updateCost(-DTModMain.bondingBonuses);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		addToBot(new DisableResonanceFormAction(true));
		addToBot(new GainBlockAction(p, p, block));
		if (dragon != null) {
			addToBot(new GainBlockAction(dragon, dragon, dtDragonBlock));
		}
		addToBot(new DisableResonanceFormAction(false));
	}

	public AbstractCard makeCopy() {
		LightShield temp = new LightShield();

		if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null) {
			temp.configureCostsOnNewCard();
		}
		return temp;
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
