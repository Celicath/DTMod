package TheDT.relics;

import TheDT.DTModMain;
import TheDT.characters.DragonTamer;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PactStone extends CustomRelic {

	public static final String RAW_ID = "PactStone";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);
	public static final int AMOUNT = 6;

	public PactStone() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.STARTER, LandingSound.SOLID);
	}

	@Override
	public void atBattleStart() {
		this.flash();
		addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, AMOUNT));
		if (AbstractDungeon.player instanceof DragonTamer) {
			DragonTamer dt = (DragonTamer) AbstractDungeon.player;
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
