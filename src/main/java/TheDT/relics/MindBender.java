package TheDT.relics;

import TheDT.DTModMain;
import TheDT.utils.RelicHelper;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MindBender extends CustomRelic {

	public static final String RAW_ID = "MindBender";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);

	public MindBender() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.SHOP, LandingSound.MAGICAL);
		RelicHelper.removeStrikeTip(this);
		tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[2]));
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MindBender();
	}
}
