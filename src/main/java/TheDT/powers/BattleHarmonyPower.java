package TheDT.powers;

import TheDT.DTModMain;
import TheDT.Interfaces.RecoloredPower;
import TheDT.characters.DragonTamer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BattleHarmonyPower extends AbstractPower implements RecoloredPower {
	public static final String RAW_ID = "BattleHarmonyPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public static boolean youAttacked = false;
	public static boolean dragonAttacked = false;

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
	public Color getIconColor() {
		return new Color(1.0f, 0.9f, 0.35f, 1.0f);
	}

	@Override
	public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
		super.renderIcons(sb, x, y, new Color(1.0f, 0.9f, 0.35f, c.a));
	}

	@Override
	public void atStartOfTurn() {
		youAttacked = false;
		dragonAttacked = false;
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner == owner) {
			if (owner == AbstractDungeon.player && !youAttacked) {
				youAttacked = true;
				if (dragonAttacked) {
					activate();
				}
			} else if (owner == DragonTamer.getLivingDragon() && !dragonAttacked) {
				dragonAttacked = true;
				if (youAttacked) {
					activate();
				}
			}
		}
	}

	private void activate() {
		flash();
		youAttacked = false;
		dragonAttacked = false;
		AbstractPlayer p = AbstractDungeon.player;
		addToBot(new ApplyPowerAction(p, p, new BondingPower(p, p, amount), amount));
	}
}
