package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.optionCards.ChoiceAttackerDragon;
import TheDT.optionCards.ChoiceAttackerYou;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class TriAttack extends AbstractDTCard {
	public static final String RAW_ID = "TriAttack";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	public static final String RAW_ID_YOU = "TriAttackYou";
	public static final String RAW_ID_DRAGON = "TriAttackDragon";

	private static final int DAMAGE = 6;
	private static final int UPGRADE_DAMAGE = 2;
	private static final int HITS = 3;

	public TriAttack() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;
		dtBaseDragonDamage = DAMAGE;
		magicNumber = baseMagicNumber = HITS;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		if (dragon != null) {
			for (int i = 0; i < magicNumber; i++) {
				ArrayList<AbstractCard> choices = new ArrayList<>();
				choices.add(new ChoiceAttackerYou(RAW_ID_YOU, m, baseDamage, damage));
				choices.add(new ChoiceAttackerDragon(RAW_ID_DRAGON, m, dtBaseDragonDamage, dtDragonDamage));
				addToBot(new AbstractGameAction() {
					@Override
					public void update() {
						if (!m.isDeadOrEscaped()) {
							addToTop(new ChooseOneAction(choices));
						}
						isDone = true;
					}
				});
			}
		} else {
			for (int i = 0; i < magicNumber; i++) {
				addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			}
		}
	}

	public AbstractCard makeCopy() {
		return new TriAttack();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
		}
	}
}
