package TheDT.relics;

import TheDT.DTMod;
import TheDT.characters.TheDT;
import TheDT.powers.OddArmorPower;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OddArmor extends CustomRelic {

	public static final String RAW_ID = "OddArmor";
	public static final String ID = DTMod.makeID(RAW_ID);
	public static final String IMG = DTMod.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTMod.GetRelicOutlinePath(RAW_ID);
	public static final int AMOUNT = 2;

	public OddArmor() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.STARTER, LandingSound.FLAT);
	}

	@Override
	public void atBattleStart() {
		counter = 0;
	}

	@Override
	public void atTurnStart() {
		counter++;
		if (counter % 2 == 1) {
			if (AbstractDungeon.player instanceof TheDT) {
				this.flash();
				TheDT dt = (TheDT) AbstractDungeon.player;
				addToBot(new ApplyPowerAction(dt.dragon, dt.dragon,
						new OddArmorPower(dt.dragon, dt.dragon, AMOUNT), AMOUNT));
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
