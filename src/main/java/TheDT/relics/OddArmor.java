package TheDT.relics;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.powers.OddArmorPower;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OddArmor extends CustomRelic {

	public static final String RAW_ID = "OddArmor";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);
	public static final int AMOUNT = 2;

	public OddArmor() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.SHOP, LandingSound.FLAT);
	}

	@Override
	public void atBattleStart() {
		counter = 0;
	}

	@Override
	public void atTurnStart() {
		counter++;
		if (counter % 2 == 1) {
			Dragon d = DragonTamer.getLivingDragon();
			if (d != null) {
				this.flash();
				addToBot(new ApplyPowerAction(d, d, new OddArmorPower(d, d, AMOUNT), AMOUNT));
			}
		} else {
			this.flash();
			addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
					new OddArmorPower(AbstractDungeon.player, AbstractDungeon.player, AMOUNT), AMOUNT));
		}
	}

	@Override
	public void onVictory() {
		counter = -1;
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1] + AMOUNT + DESCRIPTIONS[2];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new OddArmor();
	}
}
