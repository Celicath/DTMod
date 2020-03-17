package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.optionCards.TauntingStrikeDragon;
import TheDT.optionCards.TauntingStrikeYou;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class TauntingStrike extends AbstractDTCard {
	public static final String RAW_ID = "TauntingStrike";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int DAMAGE = 10;
	private static final int UPGRADE_DAMAGE = 3;

	public TauntingStrike() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;
		dtBaseDragonDamage = DAMAGE;

		tags.add(CardTags.STRIKE);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = getDragon();

		if (dragon != null) {
			ArrayList<AbstractCard> choices = new ArrayList<>();
			choices.add(new TauntingStrikeYou(m, damage));
			choices.add(new TauntingStrikeDragon(m, dtDragonDamage));
			if (upgraded) {
				for (AbstractCard c : choices) {
					c.upgrade();
				}
			}
			addToBot(new ChooseOneAction(choices));
		} else {
			addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		}
	}

	public AbstractCard makeCopy() {
		return new TauntingStrike();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
		}
	}
}
