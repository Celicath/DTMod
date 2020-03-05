package TheDT.relics;

import TheDT.DTMod;
import TheDT.characters.TheDT;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PactStone extends CustomRelic {

	public static final String RAW_ID = "PactStone";
	public static final String ID = DTMod.makeID(RAW_ID);
	public static final String IMG = DTMod.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTMod.GetRelicOutlinePath(RAW_ID);
	public static final int AMOUNT = 6;

	public PactStone() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.STARTER, LandingSound.FLAT);
	}

	@Override
	public void atBattleStart() {
		this.flash();
		addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, AMOUNT));
		if (AbstractDungeon.player instanceof TheDT) {
			TheDT dt = (TheDT) AbstractDungeon.player;
			addToBot(new RelicAboveCreatureAction(dt.dragon, this));
			addToBot(new AddTemporaryHPAction(dt.dragon, dt.dragon, AMOUNT));
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new PactStone();
	}
}
