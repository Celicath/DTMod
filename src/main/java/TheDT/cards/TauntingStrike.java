package TheDT.cards;

import TheDT.actions.ApplyAggroAction;
import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.optionCards.ChoiceAttackerDragon;
import TheDT.optionCards.ChoiceAttackerYou;
import TheDT.patches.CardColorEnum;
import TheDT.powers.TauntPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
	private static final int UPGRADE_DAMAGE = 4;

	public static final String RAW_ID_YOU = "TauntingStrikeYou";
	public static final String RAW_ID_DRAGON = "TauntingStrikeDragon";

	public TauntingStrike() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;
		dtBaseDragonDamage = DAMAGE;

		tags.add(CardTags.STRIKE);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		if (dragon != null) {
			ArrayList<AbstractCard> choices = new ArrayList<>();
			choices.add(new ChoiceAttackerYou(RAW_ID_YOU, m, baseDamage, damage) {
				@Override
				public void onChoseThisOption() {
					AbstractPlayer p = AbstractDungeon.player;
					addToBot(new FastAnimateFastAttackAction(p));
					addToBot(new DamageAction(targetMonster, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
					TauntPower tp = (TauntPower) (targetMonster.getPower(TauntPower.POWER_ID));
					if (tp != null && tp.targetIsDragon) {
						addToBot(new RemoveSpecificPowerAction(targetMonster, targetMonster, TauntPower.POWER_ID));
					}
					addToBot(new ApplyPowerAction(targetMonster, p, new TauntPower(targetMonster, false, 1), 1));
					addToBot(new ApplyAggroAction());
				}
			});
			choices.add(new ChoiceAttackerDragon(RAW_ID_DRAGON, m, dtBaseDragonDamage, dtDragonDamage) {
				@Override
				public void onChoseThisOption() {
					Dragon dragon = DragonTamer.getLivingDragon();
					if (dragon != null) {
						addToBot(new FastAnimateFastAttackAction(dragon));
						addToBot(new DamageAction(targetMonster, new DamageInfo(dragon, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
						TauntPower tp = (TauntPower) (targetMonster.getPower(TauntPower.POWER_ID));
						if (tp != null && !tp.targetIsDragon) {
							addToBot(new RemoveSpecificPowerAction(targetMonster, targetMonster, TauntPower.POWER_ID));
						}
						addToBot(new ApplyPowerAction(targetMonster, dragon, new TauntPower(targetMonster, true, 1), 1));
					}
					addToBot(new ApplyAggroAction());
				}
			});
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
