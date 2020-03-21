package TheDT.cards;

import TheDT.actions.ApplyAggroAction;
import TheDT.actions.FastLoseBlockAction;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

import java.util.ArrayList;

public class RecklessFlurry extends AbstractDTCard {
	public static final String RAW_ID = "RecklessFlurry";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int RATIO = 2;
	private static final int NEW_COST = 0;

	public static final String RAW_ID_YOU = "RecklessFlurryYou";
	public static final String RAW_ID_DRAGON = "RecklessFlurryDragon";

	public RecklessFlurry() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		isMultiDamage = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = getDragon();

		AbstractDTCard choice1 = new AbstractDTCard(RAW_ID_YOU, -2, TYPE, COLOR, CardRarity.SPECIAL, TARGET, RAW_ID, DTCardTarget.DEFAULT) {
			{
				isMultiDamage = true;
				calculateCardDamage(null);
			}

			@Override
			public void upgrade() {
			}

			@Override
			public void calculateCardDamage(AbstractMonster mo) {
				baseDamage = p.currentBlock * RATIO;
				super.calculateCardDamage(mo);
			}

			@Override
			public void use(AbstractPlayer p, AbstractMonster m) {
			}

			@Override
			public void onChoseThisOption() {
				addToBot(new FastLoseBlockAction(p));
				addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));
				addToBot(new SFXAction("ATTACK_HEAVY"));
				addToBot(new VFXAction(p, new CleaveEffect(), 0.0F));
				addToBot(new DamageAllEnemiesAction(p, multiDamage, damageType, AbstractGameAction.AttackEffect.NONE, true));
				addToBot(new ApplyAggroAction());
			}
		};

		if (dragon != null) {
			ArrayList<AbstractCard> choices = new ArrayList<>();
			choices.add(choice1);
			choices.add(new AbstractDTCard(RAW_ID_DRAGON, -2, TYPE, COLOR, CardRarity.SPECIAL, TARGET, RAW_ID, DTCardTarget.DEFAULT) {
				{
					isMultiDamage = true;
					calculateCardDamage(null);
				}

				@Override
				public void upgrade() {
				}

				@Override
				public void calculateCardDamage(AbstractMonster mo) {
					dtBaseDragonDamage = dragon.currentBlock * RATIO;
					super.calculateCardDamage(mo);
				}

				@Override
				public void use(AbstractPlayer p, AbstractMonster m) {
				}

				@Override
				public void onChoseThisOption() {
					addToBot(new FastLoseBlockAction(dragon));
					addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));
					addToBot(new DamageAllEnemiesAction(dragon, dragonMultiDamage, damageType, AbstractGameAction.AttackEffect.FIRE));
					addToBot(new ApplyAggroAction());
				}
			});
			addToBot(new ChooseOneAction(choices));
		} else {
			choice1.onChoseThisOption();
		}
	}

	public AbstractCard makeCopy() {
		return new RecklessFlurry();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(NEW_COST);
		}
	}
}
