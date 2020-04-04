package TheDT.optionCards;

import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class BondingBonus extends AbstractDTCard {
	public static final String RAW_ID = "BondingBonus";
	private static final int COST = -2;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.SELF;
	private static final DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	int index;

	public BondingBonus(int index) {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, RAW_ID, DT_CARD_TARGET);

		this.index = index;
		name = EXTENDED_DESCRIPTION[index * 2];
		rawDescription = EXTENDED_DESCRIPTION[index * 2 + 1];
		initializeTitle();
		initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		onChoseThisOption();
	}

	@Override
	public void onChoseThisOption() {
		AbstractPlayer p = AbstractDungeon.player;
		Dragon d = DragonTamer.getLivingDragon();
		switch (index) {
			case 0:
				addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 1)));
				addToBot(new ApplyPowerAction(d, d, new ArtifactPower(d, 1)));
				break;
			case 1:
				addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(15, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
				break;
			case 2:
				addToBot(new AddTemporaryHPAction(p, p, 10));
				addToBot(new AddTemporaryHPAction(d, d, 10));
				break;
			case 3:
				addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 3)));
				addToBot(new ApplyPowerAction(d, d, new StrengthPower(d, 3)));
				break;
			case 4:
				addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 2)));
				addToBot(new ApplyPowerAction(d, d, new DexterityPower(d, 2)));
				break;
			case 5:
				addToBot(new GainEnergyAction(3));
				break;
			case 6:
				addToBot(new ExpertiseAction(p, BaseMod.MAX_HAND_SIZE));
				break;
			case 7:
				for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
					addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 3, false), 3));
				}
				break;
			case 8:
				for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
					addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 3, false), 3));
				}
				break;
			case 9:
				for (int i = 0; i < 3; i++) {
					addToBot(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng), false));
				}
				break;
		}
	}

	public AbstractCard makeCopy() {
		return new TauntingStrikeDragon(null, baseDamage);
	}

	public void upgrade() {
	}
}
