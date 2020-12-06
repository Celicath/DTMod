package TheDT.potions;

import TheDT.DTModMain;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class BlazePotion extends CustomPotion {
	public static final String POTION_ID = DTModMain.makeID("BlazePotion");
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public BlazePotion() {
		super(NAME, POTION_ID, PotionRarity.PLACEHOLDER, PotionSize.SPHERE, PotionColor.FIRE);
		isThrown = true;
		targetRequired = true;
	}

	@Override
	public void initializeData() {
		potency = this.getPotency();
		description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
		tips.clear();
		tips.add(new PowerTip(name, description));
	}

	@Override
	public void use(AbstractCreature target) {
		DamageInfo info = new DamageInfo(AbstractDungeon.player, potency, DamageInfo.DamageType.THORNS);
		info.applyEnemyPowersOnly(target);
		addToBot(new DamageAction(target, info, AbstractGameAction.AttackEffect.FIRE));
	}

	@Override
	public int getPotency(int ascensionLevel) {
		return 40;
	}

	@Override
	public AbstractPotion makeCopy() {
		return new BlazePotion();
	}

	@Override
	public int getPrice() {
		return 60;
	}
}
