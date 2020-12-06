package TheDT.potions;

import TheDT.DTModMain;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.GathererMod;
import the_gatherer.patches.PotionRarityEnum;
import the_gatherer.potions.SackPotion;

public class LesserBlazePotion extends SackPotion {
	private static final String RAW_ID = "LesserBlazePotion";
	public static final String POTION_ID = DTModMain.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserBlazePotion() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.SPHERE, PotionColor.FIRE);
		isThrown = true;
		targetRequired = true;

		updateDescription();

		liquidColor = Color.ORANGE.cpy();
		hybridColor = Color.ORANGE.cpy();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		DamageInfo info = new DamageInfo(AbstractDungeon.player, potency, DamageInfo.DamageType.THORNS);
		info.applyEnemyPowersOnly(target);
		addToBot(new DamageAction(target, info, AbstractGameAction.AttackEffect.FIRE));
	}

	public AbstractPotion makeCopy() {
		return new LesserBlazePotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
	}

	@Override
	public int getBasePotency() {
		return 20;
	}
}
