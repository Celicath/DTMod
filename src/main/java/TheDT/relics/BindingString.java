package TheDT.relics;

import TheDT.DTModMain;
import TheDT.characters.DragonTamer;
import TheDT.powers.BondingPower;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BindingString extends CustomRelic {

	public static final String RAW_ID = "BindingString";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);

	public static final int AMOUNT = 2;

	public BindingString() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.RARE, LandingSound.FLAT);
	}

	public void atTurnStart() {
		AbstractPlayer p = AbstractDungeon.player;
		if (p instanceof DragonTamer && !DragonTamer.isSolo() && DragonTamer.getAggro() == 0) {
			flash();
			addToBot(new RelicAboveCreatureAction(p, this));
			addToBot(new ApplyPowerAction(p, p, new BondingPower(p, p, AMOUNT), AMOUNT));
		}
	}

	public void updatePulse() {
		AbstractPlayer p = AbstractDungeon.player;
		if (p instanceof DragonTamer && !DragonTamer.isSolo() && DragonTamer.getAggro() == 0) {
			beginLongPulse();
		} else {
			stopPulse();
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new BindingString();
	}
}
