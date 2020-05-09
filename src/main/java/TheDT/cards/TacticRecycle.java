package TheDT.cards;

import TheDT.Interfaces.TacticCard;
import TheDT.patches.CardColorEnum;
import TheDT.relics.TacticalNote;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.Exhume;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static TheDT.patches.CustomTags.DT_TACTIC;

public class TacticRecycle extends AbstractDTCard implements TacticCard {
	public static final String RAW_ID = "TacticRecycle";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 1;
	private static final int UPGRADE_BONUS = 1;

	public TacticRecycle() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		exhaust = true;
		baseMagicNumber = magicNumber = POWER;
		tags.add(DT_TACTIC);

		if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
			onEquipTacticalNote();
		}
	}

	@Override
	public void onEquipTacticalNote() {
		rawDescription = upgraded ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0];
		initializeDescription();
	}

	@Override
	public void onUnequipTacticalNote() {
		rawDescription = upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION;
		initializeDescription();
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new FetchAction(
				AbstractDungeon.player.exhaustPile,
				(c -> !c.cardID.equals(Exhume.ID) && !c.cardID.equals(cardID)),
				magicNumber,
				(cards) -> {
					for (AbstractCard c : cards) {
						c.current_x = CardGroup.DISCARD_PILE_X;
						c.current_y = CardGroup.DISCARD_PILE_Y;
						if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
							if (!c.isEthereal) {
								c.retain = true;
							}
						}
					}
				}));
	}

	public AbstractCard makeCopy() {
		return new TacticRecycle();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
			if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
				onEquipTacticalNote();
			} else {
				rawDescription = UPGRADE_DESCRIPTION;
				initializeDescription();
			}
		}
	}
}
