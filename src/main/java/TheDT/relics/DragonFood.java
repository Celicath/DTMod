package TheDT.relics;

import TheDT.DTModMain;
import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DragonFood extends CustomRelic {

	public static final String RAW_ID = "DragonFood";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);
	public static final int AMOUNT = 3;

	public DragonFood() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.UNCOMMON, LandingSound.HEAVY);
	}

	public void onVictory() {
		flash();
		Dragon d = DragonTamer.getLivingDragon();
		if (d != null) {
			addToTop(new RelicAboveCreatureAction(d, this));
			d.heal(AMOUNT);
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new DragonFood();
	}
}
