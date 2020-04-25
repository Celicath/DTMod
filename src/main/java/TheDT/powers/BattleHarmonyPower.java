package TheDT.powers;

import TheDT.DTModMain;
import TheDT.characters.DragonTamer;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BattleHarmonyPower extends AbstractPower {
	public static final String RAW_ID = "BattleHarmonyPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public BattleHarmonyPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.loadRegion("devotion");
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner == owner) {
			if (owner == AbstractDungeon.player && !DragonTamer.battleHarmonyYou) {
				DragonTamer.battleHarmonyYou = true;
				if (DragonTamer.battleHarmonyDragon) {
					activate();
				}
			} else if (owner == DragonTamer.getLivingDragon() && !DragonTamer.battleHarmonyDragon) {
				DragonTamer.battleHarmonyDragon = true;
				if (DragonTamer.battleHarmonyYou) {
					activate();
				}
			}
		}
	}

	private void activate() {
		flash();
		AbstractPlayer p = AbstractDungeon.player;
		addToBot(new ApplyPowerAction(p, p, new BondingPower(p, p, amount), amount));
	}
}
